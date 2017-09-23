package com.zhengdianfang.dazhongbao.views.basic

import android.support.v7.widget.RecyclerView
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.zhengdianfang.dazhongbao.R
import com.zhengdianfang.dazhongbao.helpers.Constants

/**
 * Created by dfgzheng on 21/08/2017.
 */
abstract class BaseListActivity<T> : BaseActivity() {

    protected  val datas = mutableListOf<T>()
    protected var pageNumber: Int = 0
    protected val recyclerView by lazy { createRecyclerView() }
    protected val adapter by lazy { createRecyclerViewAdapter() }
    protected val refreshLayout by lazy { findViewById<SmartRefreshLayout>(R.id.refreshLayout) }

    fun setupRecyclerView() {
        recyclerView.adapter = adapter
        refreshLayout.setOnRefreshListener {
            pageNumber = 0
            requestList(pageNumber)
        }
        refreshLayout.setOnLoadmoreListener {
            ++pageNumber
            requestList(pageNumber)
        }
    }

    fun autoRefresh() {
        refreshLayout.autoRefresh()
    }

    protected fun reponseProcessor(list: MutableList<T>){
        if (isRefresh()){
            datas.clear()
        }
        datas.addAll(list)
        refreshLayout.isEnableLoadmore = datas.count() % Constants.PAGE_SIZE == 0
        adapter.notifyDataSetChanged()
        refreshLayout.finishRefresh()
        refreshLayout.finishLoadmore()
    }
    protected fun reponseProcessor(list: MutableList<T>, isCache: Boolean){
        if (isRefresh()){
            datas.clear()
        }
        datas.addAll(list)
        refreshLayout.isEnableLoadmore = datas.count() % Constants.PAGE_SIZE == 0
        adapter.notifyDataSetChanged()
        if (isCache.not()) {
            refreshLayout.finishRefresh()
            refreshLayout.finishLoadmore()
        }
    }

    protected fun isRefresh():Boolean{
        return pageNumber == 0
    }

    abstract fun requestList(pageNumber: Int)
    abstract fun createRecyclerView(): RecyclerView
    abstract fun createRecyclerViewAdapter(): RecyclerView.Adapter<*>
}