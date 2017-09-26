package com.zhengdianfang.dazhongbao.views.home


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.zhengdianfang.dazhongbao.CApplication

import com.zhengdianfang.dazhongbao.R
import com.zhengdianfang.dazhongbao.helpers.Action
import com.zhengdianfang.dazhongbao.helpers.Constants
import com.zhengdianfang.dazhongbao.helpers.RxBus
import com.zhengdianfang.dazhongbao.models.product.Product
import com.zhengdianfang.dazhongbao.presenters.FollowProductPresenter
import com.zhengdianfang.dazhongbao.presenters.ProductPresenter
import com.zhengdianfang.dazhongbao.views.basic.BaseFragment
import com.zhengdianfang.dazhongbao.views.components.RecyclerViewAdapter
import com.zhengdianfang.dazhongbao.views.product.IProductList
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer


/**
 * A simple [Fragment] subclass.
 */
class HomeProductListFragment : BaseFragment(), IProductList, FollowProductPresenter.IFollowProductView {

    private val refreshLayout by lazy { view?.findViewById<SmartRefreshLayout>(R.id.refreshLayout)!! }
    private val productRecyclerView by lazy { view?.findViewById<RecyclerView>(R.id.productRecyclerView)!! }
    private val mProductPresenter by lazy { ProductPresenter() }
    private val followProductPresenter = FollowProductPresenter()
    private val products = arrayListOf<Product>()
    private val productAdapter by lazy { RecyclerViewAdapter(products, {id, follow ->
       if (follow) {
           followProductPresenter.followProduct(CApplication.INSTANCE.loginUser?.token!!, id)
       }else{
           followProductPresenter.unfollowProduct(CApplication.INSTANCE.loginUser?.token!!, id)
       }
    })}
    private var pageNumber = 0
    private val status by lazy { arguments.getString("status") }
    private var mDisposable: Disposable? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_home_product_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mProductPresenter.attachView(this)
        followProductPresenter.attachView(this)
        refreshLayout.setOnLoadmoreListener {
            ++pageNumber
            mProductPresenter.fetchProductList(CApplication.INSTANCE.loginUser?.token, pageNumber, status, "dealDateTime")
        }
        mDisposable = RxBus.instance.register(arrayOf(Action.HOME_PAGE_REFRESH_START_ACTION, Action.FOLLOW_PRODUCT_ACTION, Action.CANCEL_FOLLOW_PRODUCT_ACTION), Consumer {action ->
            when(action.type){
                Action.HOME_PAGE_REFRESH_START_ACTION -> onRefresh()
                Action.FOLLOW_PRODUCT_ACTION, Action.CANCEL_FOLLOW_PRODUCT_ACTION ->  {
                    val findItem = this.products.find { it.id == action.data }
                    if (findItem != null){
                        findItem.attention = if (findItem.attention == 0) 1 else 0
                        productAdapter.notifyItemChanged(this.products.indexOf(findItem))
                    }
                }
            }
        })
        productRecyclerView.adapter = productAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        followProductPresenter.detachView()
        mProductPresenter.detachView()
        RxBus.instance.unregister(mDisposable)
    }

    override fun receiverProductList(list: List<Product>, isCache: Boolean) {
        if (pageNumber == 0){
            products.clear()
            RxBus.instance.post(Action(Action.HOME_PAGE_REFRESH_END_ACTION, ""))
        }
        products.addAll(list)
        productAdapter.notifyDataSetChanged()
        refreshLayout.isEnableLoadmore = products.count() % Constants.PAGE_SIZE == 0
        if (!isCache){
            refreshLayout.finishLoadmore()
        }
    }

    override fun followSuccess(msg: String) {
        toast(msg)
    }

    override fun unfollowSuccess(msg: String) {
        toast(msg)
    }

    private fun onRefresh() {
        pageNumber = 0
        mProductPresenter.fetchProductList(CApplication.INSTANCE.loginUser?.token, pageNumber, status, "dealDateTime")
    }

}// Required empty public constructor
