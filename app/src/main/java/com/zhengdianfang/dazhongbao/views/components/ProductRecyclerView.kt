package com.zhengdianfang.dazhongbao.views.components

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import com.jcodecraeer.xrecyclerview.XRecyclerView
import com.zhengdianfang.dazhongbao.CApplication
import com.zhengdianfang.dazhongbao.models.product.Product
import com.zhengdianfang.dazhongbao.presenters.ProductPresenter
import com.zhengdianfang.dazhongbao.views.product.IProductList

/**
 * Created by dfgzheng on 10/08/2017.
 */
class ProductRecyclerView(context: Context?, val status: String) : XRecyclerView(context), IProductList {

    private val products = mutableListOf<Product>()
    private val productAdapter by lazy { RecyclerViewAdapter(context!!, products) }
    val mProductPresenter by lazy { ProductPresenter() }
    private var pageNumber = 0

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
        this.refresh()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        mProductPresenter.detachView()
        productAdapter.destory()
    }

    override fun showLoadingDialog() { }
    override fun hideLoadingDialog() { }

    override fun validateErrorUI(errorMsgResId: Int) {
        Toast.makeText(context, errorMsgResId, Toast.LENGTH_SHORT).show()
    }

    override fun networkError(errorMsg: String) {
        Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show()
    }

    override fun receiverProductList(list: List<Product>) {
        if (pageNumber == 0){
            products.clear()
        }
        products.addAll(list)
        productAdapter.notifyDataSetChanged()
        this.setLoadingMoreEnabled(products.count() % com.zhengdianfang.dazhongbao.helpers.Constants.PAGE_SIZE == 0)
        this.refreshComplete()
        this.loadMoreComplete()
    }
    override fun noLogin() { }

}