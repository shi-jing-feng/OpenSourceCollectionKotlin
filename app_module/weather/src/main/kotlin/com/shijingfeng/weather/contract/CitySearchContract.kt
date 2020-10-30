package com.shijingfeng.weather.contract

import com.shijingfeng.base.common.extension.onFailure
import com.shijingfeng.base.common.extension.onSuccess
import com.shijingfeng.base.mvp.model.IModel
import com.shijingfeng.base.mvp.presenter.IPresenter
import com.shijingfeng.base.mvp.view.IView
import com.shijingfeng.weather.entity.CitySearchEntity
import com.shijingfeng.weather.entity.realm.CityData

/**
 * Function: 城市搜索 契约类
 * Date: 2020/10/9 12:19
 * Description:
 * @author ShiJingFeng
 */
internal interface CitySearchContract {

    /**
     * View 层
     */
    interface View : IView {

    }

    /**
     * Presenter 层
     */
    interface Presenter : IPresenter {

        /**
         * 搜索
         *
         * @param keywords 搜索关键词
         * @param page 页码 (默认值是 1)
         * @param onSuccess 成功回调
         * @param onFailure 失败回调
         */
        fun search(
            keywords: String,
            page: Int = 1,
            onSuccess: onSuccess<CitySearchEntity>,
            onFailure: onFailure
        )

        /**
         * 获取城市数据(包括行政数据和天气数据)
         *
         * @param longitude 经度
         * @param latitude 纬度
         * @param onSuccess 成功回调
         * @param onFailure 失败回调
         */
        fun getCityData(
            longitude: Double,
            latitude: Double,
            onSuccess: onSuccess<CityData>,
            onFailure: onFailure
        )

    }

    /**
     * Model 层
     */
    interface Model : IModel {

        /**
         * 搜索
         *
         * @param keywords 搜索关键词
         * @param page 页码 (默认值是 1)
         * @param onSuccess 成功回调
         * @param onFailure 失败回调
         */
        fun search(
            keywords: String,
            page: Int = 1,
            onSuccess: onSuccess<CitySearchEntity>,
            onFailure: onFailure
        )

        /**
         * 获取城市数据(包括行政数据和天气数据)
         *
         * @param longitude 经度
         * @param latitude 纬度
         * @param onSuccess 成功回调
         * @param onFailure 失败回调
         */
        fun getCityData(
            longitude: Double,
            latitude: Double,
            onSuccess: onSuccess<CityData>,
            onFailure: onFailure
        )

    }

}