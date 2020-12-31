package com.shijingfeng.tencent_x5

import androidx.annotation.Keep
import com.shijingfeng.apt_data.annotations.ModuleEventReceiver
import com.shijingfeng.apt_data.interfaces.ModuleEventListener
import com.shijingfeng.base.common.constant.DISPATCHER_GROUP_APPLICATION
import com.shijingfeng.base.util.LOG_LIFECYCLE
import com.shijingfeng.base.util.e

/**
 * Function: 模块 Application初始化
 * Date: 2020/5/25 22:12
 * Description:
 * @author ShiJingFeng
 */
@Keep // TencentX5ModuleAppInit是通过反射调用，所以应防止被混淆
@ModuleEventReceiver(
    group = DISPATCHER_GROUP_APPLICATION
)
internal class TencentX5ModuleAppInit : ModuleEventListener {

    /**
     * 接收回调
     */
    override fun onReceive(data: Map<String, Any>): Boolean {
        e(LOG_LIFECYCLE, "tencent_x5 receiver onCreate")
        return super.onReceive(data)
    }

}