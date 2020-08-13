package com.shijingfeng.app

import androidx.annotation.Keep
import com.shijingfeng.base.interfaces.AppInit
import com.shijingfeng.base.util.LOG_LIFECYCLE
import com.shijingfeng.base.util.e

/**
 * Function: 模块 Application初始化 (类名不要变化(反射的缘故), 除非包括其他模块全局更改类名为同一个 并在 base模块中更改反射类名)
 * Date: 2020/5/25 22:12
 * Description:
 * @author ShiJingFeng
 */
@Keep // ModuleAppInit是通过反射调用，所以应防止被混淆
internal class ModuleAppInit : AppInit {

    /**
     * 初始化 (对应 Application OnCreate())
     */
    override fun onCreate() {
        super.onCreate()
        e(LOG_LIFECYCLE, "app ModuleAppInit onCreate")
    }

}