package com.shijingfeng.weather.adapter

import android.content.Context
import android.view.View.GONE
import android.view.View.VISIBLE
import com.shijingfeng.base.base.adapter.BaseAdapter
import com.shijingfeng.base.base.adapter.viewholder.CommonViewHolder
import com.shijingfeng.weather.R
import com.shijingfeng.weather.common.constant.CHOOSE_CITY
import com.shijingfeng.weather.entity.CitySearchInfoEntity

/**
 * Function: 城市搜索列表 适配器
 * Date: 2020/10/26 16:41
 * Description:
 * @author ShiJingFeng
 */
internal class CitySearchListAdapter(
    context: Context,
    dataList: List<CitySearchInfoEntity>
) : BaseAdapter<CitySearchInfoEntity>(
    context,
    layoutId = R.layout.adapter_item_city_search_list,
    dataList
) {

    /**
     * 用户自定义处理数据 (单个Item内 全局刷新)
     * @param holder ViewHolder
     * @param data 数据
     * @param position 下标位置
     */
    override fun convert(holder: CommonViewHolder, data: CitySearchInfoEntity, position: Int) {
        // 城市名称 (包括上几级行政区的城市名称)
        holder.setText(R.id.tv_city_name, if (data.cityFullName.isNotEmpty()) data.cityFullName else data.cityName)
        // 是否已添加
        holder.setVisibility(R.id.tv_added, if (data.isAdded) VISIBLE else GONE)
        holder.setOnClickListener(
            view = holder.itemView,
            listener = { view ->
                mOnItemEvent?.invoke(view, data, position, CHOOSE_CITY)
            }
        )
    }

}