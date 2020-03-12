/** 生成的 Java 类名 */
@file:JvmName("SmartRefreshLayoutViewAdapter")
package com.shijingfeng.base.base.adapter.databinding

import androidx.databinding.BindingAdapter
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener
import com.scwang.smartrefresh.layout.listener.OnRefreshListener

/**
 * Function: SmartRefreshLayout View适配器
 * Date: 2020/1/27 14:54
 * Description:
 * @author ShiJingFeng
 */

/**
 * 下拉刷新
 * @param refreshLayout SmartRefreshLayout
 * @param listener 下拉刷新监听器
 */
@BindingAdapter(value = ["onRefreshListener"], requireAll = false)
fun setOnRefreshListener(
    refreshLayout: SmartRefreshLayout,
    listener: OnRefreshListener?
) {
    if (listener == null) {
        return
    }
    refreshLayout.setOnRefreshListener(listener)
}

/**
 * 上拉加载
 * @param refreshLayout SmartRefreshLayout
 * @param listener 上拉加载监听器
 */
@BindingAdapter(value = ["onLoadMoreListener"], requireAll = false)
fun setOnLoadMoreListener(
    refreshLayout: SmartRefreshLayout,
    listener: OnLoadMoreListener?
) {
    if (listener == null) {
        return
    }
    refreshLayout.setOnLoadMoreListener(listener)
}
