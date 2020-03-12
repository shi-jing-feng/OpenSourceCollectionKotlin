/** 生成的 Java 类名 */
@file:JvmName("ViewViewAdapter")
package com.shijingfeng.base.base.adapter.databinding

import android.view.View
import androidx.annotation.IntRange
import androidx.databinding.BindingAdapter
import com.blankj.utilcode.util.ClickUtils

/**
 * Function: View View适配器
 * Date: 2020/1/27 16:02
 * Description:
 * @author ShiJingFeng
 */

@BindingAdapter(
    value = ["onClick", "disEnableClickThrottle", "clickThrottleMs"],
    requireAll = false
)
fun onClick(
    view: View,
    listener: View.OnClickListener?,
    disEnableClickThrottle: Boolean,
    @IntRange(from = 0) clickThrottleMs: Int
) {
    if (listener == null) {
        return
    }
    if (disEnableClickThrottle) {
        view.setOnClickListener(listener)
    } else {
        if (clickThrottleMs <= 0) {
            ClickUtils.applySingleDebouncing(view, listener)
        } else {
            ClickUtils.applySingleDebouncing(view, clickThrottleMs.toLong(), listener)
        }
    }
}