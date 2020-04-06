package com.shijingfeng.wan_android.view_model

import com.blankj.utilcode.util.ToastUtils
import com.kingja.loadsir.callback.Callback.OnReloadListener
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import com.shijingfeng.base.common.constant.*
import com.shijingfeng.base.entity.event.live_data.ListDataChangeEvent
import com.shijingfeng.base.livedata.SingleLiveEvent
import com.shijingfeng.base.util.getPositionById
import com.shijingfeng.base.util.getStringById
import com.shijingfeng.wan_android.R
import com.shijingfeng.wan_android.base.WanAndroidBaseViewModel
import com.shijingfeng.wan_android.entity.event.ArticleCollectionEvent
import com.shijingfeng.wan_android.entity.network.PersonalCollectionArticleItem
import com.shijingfeng.wan_android.source.repository.PersonalCollectionArticleRepository
import com.shijingfeng.wan_android.ui.fragment.PersonalCollectionArticleFragment
import org.greenrobot.eventbus.EventBus
import java.util.ArrayList

/**
 * Function: 我的收藏 -> 收藏文章列表 ViewModel
 * Date: 2020/3/24 22:56
 * Description:
 * @author ShiJingFeng
 */
internal class PersonalCollectionArticleViewModel(
    repository: PersonalCollectionArticleRepository? = null
) : WanAndroidBaseViewModel<PersonalCollectionArticleRepository>(repository) {

    /** 第一页 页码  */
    private val FIRST_PAGE = 0

    /** 当前页码  */
    private var mPage = FIRST_PAGE
    /** 数据操作类型  */
    private var mDataOperateType = DATA_OPERATE_TYPE_LOAD
    /** 文章收藏列表 数据列表 */
    val mArticleCollectedListItemList = ArrayList<PersonalCollectionArticleItem>()

    /** 列表数据改变 LiveData Event  */
    val mListDataChangeEvent = SingleLiveEvent<ListDataChangeEvent<PersonalCollectionArticleItem>>()

    /** LoadService 重新加载监听器  */
    val mReloadListener = OnReloadListener {
        if (mLoadServiceStatus == LOAD_SERVICE_LOADING) {
            return@OnReloadListener
        }
        showCallback(LOAD_SERVICE_LOADING)
        load()
    }
    /** 下拉刷新  */
    val mOnRefreshListener = OnRefreshListener { refresh() }
    /** 上拉加载  */
    val mOnLoadMoreListener = OnLoadMoreListener { loadMore() }

    /**
     * 初始化
     */
    override fun init() {
        super.init()
        load()
    }

    /**
     * 加载数据
     */
    private fun load() {
        mDataOperateType = DATA_OPERATE_TYPE_LOAD
        getArticleCollectedList(FIRST_PAGE)
    }

    /**
     * 下拉刷新
     */
    fun refresh() {
        mDataOperateType = DATA_OPERATE_TYPE_REFRESH
        getArticleCollectedList(FIRST_PAGE)
    }

    /**
     * 上拉加载
     */
    private fun loadMore() {
        mDataOperateType = DATA_OPERATE_TYPE_LOAD_MORE
        getArticleCollectedList(mPage + 1)
    }

    /**
     * 获取 文章收藏列表
     * @param page 页码 (从 0 开始)
     */
    private fun getArticleCollectedList(page: Int) {
        mRepository?.getArticleCollectedList(page, onSuccess = onSuccessLabel@{ articleCollectedList ->
            val articleCollectedItemList = articleCollectedList?.articleCollectedItemList
            val event =
                ListDataChangeEvent<PersonalCollectionArticleItem>()

            when (mDataOperateType) {
                // 加载数据
                DATA_OPERATE_TYPE_LOAD -> {
                    mPage = FIRST_PAGE
                    mArticleCollectedListItemList.clear()
                    if (!articleCollectedItemList.isNullOrEmpty()) {
                        mArticleCollectedListItemList.addAll(articleCollectedItemList)
                    }

                    event.type = LOAD
                    event.dataList = articleCollectedItemList

                    mListDataChangeEvent.value = event
                    showCallback(if (mArticleCollectedListItemList.isEmpty()) LOAD_SERVICE_EMPTY else LOAD_SERVICE_SUCCESS)
                }
                // 下拉刷新
                DATA_OPERATE_TYPE_REFRESH -> {
                    mPage = FIRST_PAGE
                    mArticleCollectedListItemList.clear()
                    if (!articleCollectedItemList.isNullOrEmpty()) {
                        mArticleCollectedListItemList.addAll(articleCollectedItemList)
                    }

                    event.type = REFRESH
                    event.dataList = articleCollectedItemList

                    mListDataChangeEvent.value = event
                    updateRefreshLoadMoreStatus(REFRESH_SUCCESS)
                    // 数据为空
                    if (mArticleCollectedListItemList.isEmpty()) {
                        showCallback(LOAD_SERVICE_EMPTY)
                    }
                }
                // 上拉加载
                DATA_OPERATE_TYPE_LOAD_MORE -> {
                    if (articleCollectedItemList.isNullOrEmpty()) {
                        updateRefreshLoadMoreStatus(LOAD_MORE_ALL)
                        return@onSuccessLabel
                    }
                    ++mPage

                    event.type = ADD
                    event.dataList = articleCollectedItemList
                    event.extraData = mArticleCollectedListItemList.size

                    mArticleCollectedListItemList.addAll(articleCollectedItemList)
                    mListDataChangeEvent.value = event
                    updateRefreshLoadMoreStatus(LOAD_MORE_SUCCESS)
                }
                else -> {}
            }
        }, onFailure = {
            when (mDataOperateType) {
                // 加载数据
                DATA_OPERATE_TYPE_LOAD -> showCallback(LOAD_SERVICE_LOAD_FAIL)
                // 下拉刷新
                DATA_OPERATE_TYPE_REFRESH -> updateRefreshLoadMoreStatus(REFRESH_FAIL)
                // 上拉加载
                DATA_OPERATE_TYPE_LOAD_MORE -> updateRefreshLoadMoreStatus(LOAD_MORE_FAIL)
                else -> {}
            }
        })
    }

    /**
     * 取消收藏
     * @param articleCollectedListItem 文章收藏列表 Item 实体
     */
    fun uncollectedInCollectedList(articleCollectedListItem: PersonalCollectionArticleItem) {
        val articleId = articleCollectedListItem.identity
        val originalId = articleCollectedListItem.originId

        mRepository?.uncollectedInCollectedList(articleId, originalId, onSuccess = {
            val position = getPositionById(articleId, mArticleCollectedListItemList)

            if (position != -1) {
                val event =
                    ListDataChangeEvent<PersonalCollectionArticleItem>()
                val indexList = listOf(position)

                event.type = REMOVE
                event.indexList = indexList
                event.extraData = mArticleCollectedListItemList.size

                mArticleCollectedListItemList.removeAt(position)
                mListDataChangeEvent.value = event

                if (mArticleCollectedListItemList.size <= 0) {
                    //数据为空
                    showCallback(LOAD_SERVICE_EMPTY)
                }
                EventBus.getDefault().post(
                    ArticleCollectionEvent(
                    fromName = PersonalCollectionArticleFragment::class.java.name,
                    id = articleId,
                    collected = false
                ))
            } else {
                ToastUtils.showShort(getStringById(R.string.已经取消收藏))
            }
        })
    }

}