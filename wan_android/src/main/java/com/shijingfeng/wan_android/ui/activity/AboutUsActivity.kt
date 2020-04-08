package com.shijingfeng.wan_android.ui.activity

import android.annotation.SuppressLint
import android.text.Html
import android.text.method.LinkMovementMethod
import android.util.SparseArray
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.AppUtils
import com.shijingfeng.base.arouter.ACTIVITY_WAN_ANDROID_ABOUT_US
import com.shijingfeng.base.util.e
import com.shijingfeng.base.util.getStringById
import com.shijingfeng.wan_android.BR
import com.shijingfeng.wan_android.R
import com.shijingfeng.wan_android.base.WanAndroidBaseActivity
import com.shijingfeng.wan_android.databinding.ActivityWanAndroidAboutUsBinding
import com.shijingfeng.wan_android.view_model.AboutUsViewModel
import kotlinx.android.synthetic.main.activity_wan_android_about_us.*
import kotlinx.android.synthetic.main.layout_wan_android_title_bar.view.*

/**
 * Function: 主页 -> 侧边栏 -> 关于我们 Activity
 * Date: 20-4-6 下午11:00
 * Description:
 * @author shijingfeng
 */
@Route(path = ACTIVITY_WAN_ANDROID_ABOUT_US)
internal class AboutUsActivity : WanAndroidBaseActivity<ActivityWanAndroidAboutUsBinding, AboutUsViewModel>() {

    /**
     * 获取视图ID
     *
     * @return 视图ID
     */
    override fun getLayoutId() = R.layout.activity_wan_android_about_us

    /**
     * 获取ViewModel
     * @return ViewModel
     */
    override fun getViewModel() = createViewModel(AboutUsViewModel::class.java)

    /**
     * 初始化 DataBinding 变量ID 和 变量实体类 Map
     * @return DataBinding 变量SparseArray
     */
    override fun getVariableSparseArray() = SparseArray<Any>().apply {
        put(BR.aboutUsViewModel, mViewModel)
    }

    /**
     * 初始化数据
     */
    @SuppressLint("SetTextI18n")
    override fun initData() {
        super.initData()
        include_title_bar.tv_title.text = getStringById(R.string.关于我们)
        tv_version_name.text = getStringById(R.string.wan_android_name) + " v" + AppUtils.getAppVersionName()
        tv_content.run {
            richText = getStringById(R.string.about_us_content)
        }
    }
}