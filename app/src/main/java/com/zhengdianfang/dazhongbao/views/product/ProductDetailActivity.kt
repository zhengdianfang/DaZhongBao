package com.zhengdianfang.dazhongbao.views.product

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.widget.TextView
import com.zhengdianfang.dazhongbao.R
import com.zhengdianfang.dazhongbao.models.product.Product
import com.zhengdianfang.dazhongbao.presenters.ProductDetailPresenter
import com.zhengdianfang.dazhongbao.views.basic.BaseActivity
import com.zhengdianfang.dazhongbao.views.components.Toolbar
import com.zhengdianfang.dazhongbao.views.product.adapter.ProductRecyclerViewAdapter

class ProductDetailActivity : BaseActivity() , ProductDetailPresenter.IProductInfoView{

    private var product: Product? = null
    private val bidList = arrayListOf<String>()
    private val productRecyclerViewAdapter by lazy { ProductRecyclerViewAdapter(product, bidList) }
    private val productRecyclerView by lazy { findViewById<RecyclerView>(R.id.productRecyclerView) }
    private val productDetailPresenter by lazy { ProductDetailPresenter() }
    private val statusView by lazy { findViewById<TextView>(R.id.statusView) }
    private val statusInfoView by lazy { findViewById<TextView>(R.id.statusInfoView) }
    private val toolBar by lazy { findViewById<Toolbar>(R.id.toolbar) }
    private val productId by lazy { intent.getLongExtra("productId", -1L) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)
        productDetailPresenter.attachView(this)
        productDetailPresenter.fetchProductInfo(productId)
    }

    override fun onDestroy() {
        super.onDestroy()
        productDetailPresenter.detachView()
    }

    override fun renderProductInfo(product: Product) {
        this.product = product
        renderToolbar()
        renderList()
    }

    private fun renderToolbar() {
        toolBar.setTitle(this.product?.sharesName ?: "")
    }

    private fun renderList() {
        productRecyclerView.adapter = productRecyclerViewAdapter
    }

    override fun renderActionBar(backgroundColorResId: Int, textResId: Int){
        statusView.setText(textResId)
        statusView.setBackgroundColor(ContextCompat.getColor(this.applicationContext, backgroundColorResId))
    }

}
