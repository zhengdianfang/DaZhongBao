package com.zhengdianfang.dazhongbao.views.components

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Toast
import com.zhengdianfang.dazhongbao.CApplication
import com.zhengdianfang.dazhongbao.models.product.Product
import com.zhengdianfang.dazhongbao.presenters.ProductPresenter
import com.zhengdianfang.dazhongbao.views.product.IProductList

/**
 * Created by dfgzheng on 10/08/2017.
 */
class ProductRecyclerView(context: Context?, val status: Int) : RecyclerView(context), IProductList {

    val products = mutableListOf<Product>()
    val productAdapter by lazy { RecyclerViewAdapter(products) }
    val mProductPresenter by lazy { ProductPresenter() }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        mProductPresenter.attachView(this)
        layoutManager = LinearLayoutManager(context ,  LinearLayoutManager.VERTICAL, false)
        adapter = productAdapter
        mProductPresenter.fetchProductList(CApplication.INSTANCE.loginUser?.token, 0, status)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        mProductPresenter.detachView()
    }

    override fun showLoadingDialog() {
    }

    override fun hideLoadingDialog() {
    }

    override fun validateErrorUI(errorMsgResId: Int) {
        Toast.makeText(context, errorMsgResId, Toast.LENGTH_SHORT).show()
    }

    override fun networkError(errorMsg: String) {
        Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show()
    }

    override fun receiverProductList(list: List<Product>) {
        products.clear()
        products.addAll(list)
        productAdapter.notifyDataSetChanged()
    }
    override fun noLogin() { }
}