package com.zhengdianfang.dazhongbao.views.basic

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import com.jcodecraeer.xrecyclerview.XRecyclerView
import com.zhengdianfang.dazhongbao.helpers.Constants

/**
 * Created by dfgzheng on 21/08/2017.
 */
abstract class BaseListFragment<T> : BaseFragment(), XRecyclerView.LoadingListener{

    protected  val datas = mutableListOf<T>()
    protected var pageNumber: Int = 0
    protected val recyclerView by lazy { createRecyclerView() }
    protected val adapter by lazy { createRecyclerViewAdapter() }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        recyclerView.adapter = adapter
        recyclerView.setLoadingListener(this)

    }

    override fun onRefresh() {
        pageNumber = 0
        requestList(pageNumber)
    }

    override fun onLoadMore() {
        ++pageNumber
        requestList(pageNumber)
    }

    protected fun reponseProcessor(list: MutableList<T>){
        if (isRefresh()){
            datas.clear()
        }
        datas.addAll(list)
        recyclerView.setLoadingMoreEnabled(datas.count() % Constants.PAGE_SIZE == 0)
        adapter.notifyDataSetChanged()
        recyclerView.refreshComplete()
        recyclerView.loadMoreComplete()
    }
    protected fun reponseProcessor(list: MutableList<T>, isCache: Boolean){
        if (isRefresh()){
            datas.clear()
        }
        datas.addAll(list)
        recyclerView.setLoadingMoreEnabled(datas.count() % Constants.PAGE_SIZE == 0)
        adapter.notifyDataSetChanged()
        if (isCache.not()) {
            recyclerView.refreshComplete()
            recyclerView.loadMoreComplete()
        }
    }
    protected fun isRefresh():Boolean{
        return pageNumber == 0
    }
    override fun showLoadingDialog() { }
    override fun hideLoadingDialog() { }

    abstract fun requestList(pageNumber: Int)
    abstract fun createRecyclerView(): XRecyclerView
    abstract fun createRecyclerViewAdapter(): RecyclerView.Adapter<*>
}