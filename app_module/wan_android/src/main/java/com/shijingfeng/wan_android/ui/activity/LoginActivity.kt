package com.shijingfeng.wan_android.ui.activity

import android.util.SparseArray
import android.view.KeyEvent
import android.view.View.VISIBLE
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.ActivityUtils
import com.shijingfeng.base.arouter.ACTIVITY_WAN_ANDROID_LOGIN
import com.shijingfeng.base.base.viewmodel.factory.createCommonViewModelFactory
import com.shijingfeng.base.util.RESOURCE_TYPE_DRAWABLE
import com.shijingfeng.base.util.getStringById
import com.shijingfeng.wan_android.BR
import com.shijingfeng.wan_android.R
import com.shijingfeng.wan_android.base.WanAndroidBaseActivity
import com.shijingfeng.wan_android.common.global.setThemeBackground
import com.shijingfeng.wan_android.common.global.setThemeTextColor
import com.shijingfeng.wan_android.databinding.ActivityWanAndroidLoginBinding
import com.shijingfeng.wan_android.source.repository.LoginRepository
import com.shijingfeng.wan_android.view_model.LoginViewModel
import com.shijingfeng.wan_android.source.network.getLoginNetworkSourceInstance
import com.shijingfeng.wan_android.source.repository.getLoginRepositoryInstance

/**
 * Function: 登录 Activity
 * Date: 2020/2/4 20:55
 * Description:
 * @author ShiJingFeng
 */
@Route(path = ACTIVITY_WAN_ANDROID_LOGIN)
internal class LoginActivity : WanAndroidBaseActivity<ActivityWanAndroidLoginBinding, LoginViewModel>() {

    /**
     * 获取视图ID
     *
     * @return 视图ID
     */
    override fun getLayoutId() = R.layout.activity_wan_android_login

    /**
     * 获取ViewModel
     * @return ViewModel
     */
    override fun getViewModel(): LoginViewModel? {
        val loginRepository: LoginRepository = getLoginRepositoryInstance(
            networkSource = getLoginNetworkSourceInstance()
        )
        val factory = createCommonViewModelFactory(
            repository = loginRepository
        )

        return createViewModel(LoginViewModel::class.java, factory)
    }

    /**
     * 初始化 DataBinding 变量ID 和 变量实体类 Map
     * @return DataBinding 变量SparseArray
     */
    override fun getVariableSparseArray() = SparseArray<Any>().apply {
        put(BR.loginViewModel, mViewModel)
    }

    /**
     * 初始化数据
     */
    override fun initData() {
        super.initData()
        mDataBinding.includeTitleBar.tvTitle.text = getStringById(R.string.登录)
        mDataBinding.includeTitleBar.tvOperate.text = getStringById(R.string.注册)
        mDataBinding.includeTitleBar.tvOperate.visibility = VISIBLE

        setThemeBackground(
            mDataBinding.tvLogin,
            resName = getStringById(R.string.drawable_id_selector_submit)
        )
        setThemeTextColor(
            mDataBinding.tvRetrievePassword
        )
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        when(keyCode) {
            KeyEvent.KEYCODE_BACK -> {
                val size = ActivityUtils.getActivityList().size

                if (size <= 1) {
                    mViewModel?.doubleDownExitApp()
                } else {
                    finish()
                }
                return true
            }
        }
        return super.onKeyDown(keyCode, event)
    }
}