package com.zhengdianfang.dazhongbao.views.components

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import com.jcodecraeer.xrecyclerview.XRecyclerView
import com.zhengdianfang.dazhongbao.CApplication
import com.zhengdianfang.dazhongbao.RxBusUtils
import com.zhengdianfang.dazhongbao.helpers.RxBus
import com.zhengdianfang.dazhongbao.models.product.Product
import com.zhengdianfang.dazhongbao.presenters.ProductPresenter
import com.zhengdianfang.dazhongbao.views.basic.BaseActivity
import com.zhengdianfang.dazhongbao.views.product.IProductList
import io.reactivex.disposables.Disposable

/**
 * Created by dfgzheng on 10/08/2017.
 */
class ProductRecyclerView(context: Context?, val status: String) : XRecyclerView(context), IProductList {

    private val products = mutableListOf<Product>()
    private val productAdapter by lazy { RecyclerViewAdapter(context!!, products) }
    val mProductPresenter by lazy { ProductPresenter() }
    private var pageNumber = 0
    private var followDisposable: Disposable? = null

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        mProductPresenter.attachView(this)
        layoutManager = LinearLayoutManager(context ,  LinearLayoutManager.VERTICAL, false)
        adapter = productAdapter


        setLoadingListener(object : XRecyclerView.LoadingListener {
            override fun onLoadMore() {
                ++pageNumber
                mProductPresenter.fetchProductList(CApplication.INSTANCE.loginUser?.token, pageNumber, status, "dealDateTime")
            }

            override fun onRefresh() {
                pageNumber = 0
                mProductPresenter.fetchProductList(CApplication.INSTANCE.loginUser?.token, pageNumber, status, "dealDateTime")
            }

        })
        followDisposable = RxBusUtils.registerFollowAndUnFollowProductActionsForRecyclerView(products, {pos -> productAdapter.notifyItemChanged(pos + 1)})
        this.refresh()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        mProductPresenter.detachView()
        productAdapter.destory()
        RxBus.instance.unregister(followDisposable)
    }

    override fun showLoadingDialog() {
       if(context is BaseActivity){
           (context as BaseActivity).showLoadingDialog()
       }
    }
    override fun hideLoadingDialog() {
        if(context is BaseActivity){
            (context as BaseActivity).hideLoadingDialog()
        }
    }

    override fun validateErrorUI(errorMsgResId: Int) {
        if(context is BaseActivity){
            (context as BaseActivity)?.toast(errorMsgResId)
        }
    }

    override fun networkError(errorMsg: String) {
        if(context is BaseActivity){
            (context as BaseActivity)?.toast(errorMsg)
        }
    }

    override fun receiverProductList(list: List<Product>, isCache: Boolean) {
        if (pageNumber == 0){
            products.clear()
        }
        products.addAll(list)
        productAdapter.notifyDataSetChanged()
        this.setLoadingMoreEnabled(products.count() % com.zhengdianfang.dazhongbao.helpers.Constants.PAGE_SIZE == 0)
        if (!isCache){
            this.refreshComplete()
            this.loadMoreComplete()
        }
    }
    override fun noLogin() { }

}