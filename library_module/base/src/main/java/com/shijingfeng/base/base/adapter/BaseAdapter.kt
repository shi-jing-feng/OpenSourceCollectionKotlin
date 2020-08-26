package com.shijingfeng.base.base.adapter

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.shijingfeng.base.base.adapter.viewholder.CommonViewHolder
import com.shijingfeng.base.base.adapter.viewholder.CommonViewHolder.Companion.createCommonViewHolder
import com.shijingfeng.base.base.entity.BaseEntity
import com.shijingfeng.base.common.extension.onItemEvent

/**
 * Function: 通用 RecyclerView Adapter
 * Date: 2020/1/24 10:13
 * Description:
 * @author ShiJingFeng
 */
abstract class BaseAdapter<T : BaseEntity>(
    context: Context,
    layoutId: Int,
    dataList: List<T>? = null
) : RecyclerView.Adapter<CommonViewHolder>() {

    protected val mContext = context
    protected val mLayoutId = layoutId
    protected var mDataList = dataList

    /** 当前操作的Item Position  */
    protected var mChoiceItemPosition = -1
    /** 回调监听器 */
    protected var mOnItemEvent: onItemEvent? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CommonViewHolder {
        return createCommonViewHolder(
            mContext,
            mLayoutId,
            parent
        )
    }

    override fun onBindViewHolder(
        holder: CommonViewHolder,
        position: Int
    ) {}

    override fun onBindViewHolder(
        holder: CommonViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty()) {
            convert(holder, mDataList!![position], position)
        } else {
            partialConvert(holder, mDataList!![position], position, payloads)
        }
    }

    /**
     * 用户自定义处理数据 (单个Item内 全局刷新)
     * @param holder ViewHolder
     * @param data 数据
     * @param position 下标位置
     */
    protected abstract fun convert(
        holder: CommonViewHolder,
        data: T,
        position: Int
    )

    /**
     * 用户自定义处理数据 (单个Item内 局部刷新)
     * @param holder ViewHolder
     * @param data 数据
     * @param position 下标位置
     * @param payloads 局部刷新标记 (注：为什么是List，而不是Object, 因为在Item中的View刷新之前，可能存在短时间刷新多次，所以就会有多个占位符存入List中)
     */
    protected open fun partialConvert(
        holder: CommonViewHolder,
        data: T,
        position: Int,
        payloads: List<Any>
    ) {}

    /**
     * 设置回调监听器
     * @param onItemEvent 回调监听器
     */
    fun setOnItemEventListener(onItemEvent: onItemEvent?) {
        this.mOnItemEvent = onItemEvent
    }

    override fun getItemCount(): Int = (mDataList?.size ?: 0)

    /**
     * 获取当前操作的 Item Position
     * @return 当前操作的 Item Position
     */
    fun getChoiceItemPosition() = mChoiceItemPosition

}