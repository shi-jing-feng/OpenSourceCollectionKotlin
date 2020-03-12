package com.shijingfeng.wan_android.adapter

import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.blankj.utilcode.util.ClickUtils
import com.blankj.utilcode.util.ConvertUtils
import com.blankj.utilcode.util.ScreenUtils
import com.shijingfeng.base.base.adapter.common.CommonAdapter
import com.shijingfeng.base.base.adapter.common.viewholder.CommonViewHolder
import com.shijingfeng.base.util.layout
import com.shijingfeng.wan_android.R
import com.shijingfeng.wan_android.constant.VIEW_NAVIGATION_CLASSIFY_DETAIL
import com.shijingfeng.wan_android.entity.network.NavigationClassifyEntity
import java.util.*

/**
 * Function: 导航分类 RecyclerView适配器
 * Date: 2020/2/4 15:27
 * Description:
 * @author ShiJingFeng
 */
class NavigationClassifyAdapter(
    mContext: Context,
    mLayoutId: Int,
    mDataList: List<NavigationClassifyEntity>? = null
) : CommonAdapter<NavigationClassifyEntity>(mContext, mLayoutId, mDataList) {

    /**
     * 用户自定义处理数据 (单个Item内 全局刷新)
     * @param holder ViewHolder
     * @param data 数据
     * @param position 下标位置
     */
    override fun convert(holder: CommonViewHolder, data: NavigationClassifyEntity, position: Int) {
        val llChild = holder.getView<LinearLayout>(R.id.ll_child)

        holder.setText(R.id.tv_name, data.name)
        if (data.articleList.isEmpty()) {
            llChild?.visibility = View.GONE
        } else {
            val totalWidth = ScreenUtils.getScreenWidth() - 2 * ConvertUtils.dp2px(15F)
            val viewList: MutableList<View> = ArrayList()

            for (navigationClassifyArticle in data.articleList) {
                val name: String = navigationClassifyArticle.title

                viewList.add(TextView(mContext).apply {
                    text = name
                    setPadding(
                        ConvertUtils.dp2px(15f),
                        ConvertUtils.dp2px(7f),
                        ConvertUtils.dp2px(15f),
                        ConvertUtils.dp2px(7f)
                    )
                    setBackgroundResource(R.drawable.shape_classify_child_bg)
                    ClickUtils.applySingleDebouncing(this) { v: View ->
                        mOnItemEventListener?.invoke(v, navigationClassifyArticle, position, VIEW_NAVIGATION_CLASSIFY_DETAIL)
                    }
                })
            }

            if (llChild != null) {
                layout(
                    linearLayout = llChild,
                    childViewList = viewList,
                    totalWidth = totalWidth,
                    interval = ConvertUtils.dp2px(10F),
                    gravity = Gravity.START
                )
                llChild.visibility = View.VISIBLE
            }
        }
    }

}