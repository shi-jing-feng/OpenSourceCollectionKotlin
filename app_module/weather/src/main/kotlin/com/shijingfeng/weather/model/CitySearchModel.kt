package com.shijingfeng.weather.model

import com.shijingfeng.base.common.constant.AMAP_SERVICE_KEY
import com.shijingfeng.base.common.extension.onFailure
import com.shijingfeng.base.common.extension.onSuccess
import com.shijingfeng.base.http.exception.ServerException
import com.shijingfeng.base.http.exception.handle
import com.shijingfeng.base.mvp.model.BaseModel
import com.shijingfeng.base.util.RetrofitUtil
import com.shijingfeng.base.util.e
import com.shijingfeng.weather.api.CitySearchApi
import com.shijingfeng.weather.common.constant.*
import com.shijingfeng.weather.common.constant.AMAP_CITY
import com.shijingfeng.weather.common.constant.AMAP_COUNTRY
import com.shijingfeng.weather.common.constant.AMAP_PROVINCE
import com.shijingfeng.weather.common.constant.AMAP_SERVER_SUCCESS
import com.shijingfeng.weather.common.constant.AMAP_STREET
import com.shijingfeng.weather.common.global.cityDataMap
import com.shijingfeng.weather.contract.CitySearchContract
import com.shijingfeng.weather.entity.CitySearchEntity
import com.shijingfeng.weather.entity.CitySearchInfoEntity
import com.shijingfeng.weather.entity.realm.CityData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Function: 城市搜索 Model
 * Date: 2020/10/22 17:16
 * Description:
 * @author ShiJingFeng
 */
internal class CitySearchModel(
    presenter: CitySearchContract.Presenter
) : BaseModel<CitySearchContract.Presenter>(
    presenter
), CitySearchContract.Model {

    private val mCitySearchApi = RetrofitUtil.create(CitySearchApi::class.java)

    /**
     * 搜索
     *
     * @param keywords 搜索关键词
     * @param page 页码 (默认值是 1)
     * @param onSuccess 成功回调
     * @param onFailure 失败回调
     */
    override fun search(
        keywords: String,
        page: Int,
        onSuccess: onSuccess<CitySearchEntity>,
        onFailure: onFailure
    ) {
        addDisposable(
            mCitySearchApi
                .search(linkedMapOf<String, Any>().apply {
                    put("key", AMAP_SERVICE_KEY)
                    put("keywords", keywords)
                    put("subdistrict", 0)
                    put("page", page)
                    // 注意此处最大应设为20，因为根据经纬度查询地址的接口一次批量最大支持的数量为20
                    put("offset", 20)
                })
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ citySearch ->
                    if (citySearch.status == AMAP_SERVER_SUCCESS) {
                        val newDistrictList = arrayListOf<CitySearchInfoEntity>()
                        val locationStr = StringBuilder()

                        citySearch.districts.forEach { citySearchInfo ->
                            citySearchInfo.run {
                                // 过滤掉 国家 省(包括 直辖市，自治区，特别行政区，台湾省) 乡镇
                                val pass = level != AMAP_COUNTRY
                                        && level != AMAP_PROVINCE
                                        && level != AMAP_STREET
                                        && lngLatStr.isNotEmpty()

                                if (pass) {
                                    // 是否已添加
                                    isAdded = cityDataMap.contains(adCode)
                                    newDistrictList.add(citySearchInfo)
                                    // 拼接经纬度，用于批量获取地址信息
                                    if (locationStr.isNotEmpty()) {
                                        locationStr.append("|")
                                    }
                                    locationStr.append(lngLatStr)
                                }
                            }
                        }
                        citySearch.districts = newDistrictList
                        if (citySearch.districts.isEmpty()) {
                            onSuccess.invoke(citySearch)
                            return@subscribe
                        }
                        // 请求 经纬度 批量转 地址 接口
                        mCitySearchApi.getAddressByLngLat(location = locationStr.toString())
                            .subscribeOn(Schedulers.io())
                            .unsubscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({ inverseGeocode ->
                                if (inverseGeocode.status == AMAP_SERVER_SUCCESS) {
                                    // 设置 城市名称(包括上几级行政区的城市名称)
                                    citySearch.districts.forEachIndexed { index, citySearchInfo ->
                                        val inverseGeocodeInfoDetail = inverseGeocode.dataList[index].detail

                                        if (citySearchInfo.level.isNotEmpty()) {
                                            val province = inverseGeocodeInfoDetail.province
                                            val city = inverseGeocodeInfoDetail.city
                                            val district = inverseGeocodeInfoDetail.district

                                            when (citySearchInfo.level) {
                                                // 地级市
                                                AMAP_CITY -> citySearchInfo.cityFullName = if (city.isEmpty()) {
                                                    province
                                                } else {
                                                    // 当城市是省直辖县时返回为空，以及城市为北京、上海、天津、重庆四个直辖市时，city返回为空
                                                    "${province}-${city}"
                                                }
                                                // 市区(包括直辖市市区), 县级市, 县
                                                AMAP_DISTRICT -> citySearchInfo.cityFullName = if (city.isEmpty()) {
                                                    // 当城市是省直辖县时返回为空，以及城市为北京、上海、天津、重庆四个直辖市时，city返回为空
                                                    "${province}-${district}"
                                                } else {
                                                    "${province}-${city}-${district}"
                                                }
                                            }
                                        }
                                    }
                                    onSuccess.invoke(citySearch)
                                } else {
                                    onFailure.invoke(handle(ServerException(inverseGeocode.infoCode.toInt(), inverseGeocode.info)))
                                }
                            }, { throwable ->
                                onFailure.invoke(handle(throwable))
                            })
                    } else {
                        onFailure.invoke(handle(ServerException(citySearch.infoCode.toInt(), citySearch.info)))
                    }
                }, { throwable ->
                    onFailure.invoke(handle(throwable))
                })
        )
    }

    /**
     * 获取城市数据(包括行政数据和天气数据)
     *
     * @param longitude 经度
     * @param latitude 纬度
     * @param onSuccess 成功回调
     * @param onFailure 失败回调
     */
    override fun getCityData(
        longitude: Double,
        latitude: Double,
        onSuccess: onSuccess<CityData>,
        onFailure: onFailure
    ) {

    }

}