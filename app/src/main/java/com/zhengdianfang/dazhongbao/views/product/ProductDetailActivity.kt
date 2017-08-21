package com.zhengdianfang.dazhongbao.views.product

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.widget.TextView
import com.zhengdianfang.dazhongbao.R
import com.zhengdianfang.dazhongbao.models.product.Bid
import com.zhengdianfang.dazhongbao.models.product.Product
import com.zhengdianfang.dazhongbao.presenters.FollowProductPresenter
import com.zhengdianfang.dazhongbao.presenters.ProductDetailPresenter
import com.zhengdianfang.dazhongbao.views.basic.BaseActivity
import com.zhengdianfang.dazhongbao.views.components.Toolbar
import com.zhengdianfang.dazhongbao.views.product.adapter.ProductRecyclerViewAdapter

class ProductDetailActivity : BaseActivity() , ProductDetailPresenter.IProductInfoView, FollowProductPresenter.IFollowProductView {

    private var product: Product? = null
    private val bidList = arrayListOf<Bid>()
    private val productRecyclerViewAdapter by lazy { ProductRecyclerViewAdapter(product, bidList, followProductPresenter) }
    private val productRecyclerView by lazy { findViewById<RecyclerView>(R.id.productRecyclerView) }
    private val productDetailPresenter by lazy { ProductDetailPresenter() }
    private val followProductPresenter by lazy { FollowProductPresenter() }

    private val statusView by lazy { findViewById<TextView>(R.id.statusView) }
    private val statusInfoView by lazy { findViewById<TextView>(R.id.statusInfoView) }
    private val toolBar by lazy { findViewById<Toolbar>(R.id.toolbar) }
    private val productId by lazy { intent.getLongExtra("productId", -1L) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)
        productDetailPresenter.attachView(this)
        followProductPresenter.attachView(this)
        productRecyclerView.adapter = productRecyclerViewAdapter
        productDetailPresenter.fetchProductInfoAndBidList(productId)
    }

    override fun onDestroy() {
        super.onDestroy()
        productDetailPresenter.detachView()
        followProductPresenter.detachView()
    }

    override fun renderProductInfo(product: Product) {
        this.product = product
        renderToolbar()
        renderList()
    }

    private fun renderToolbar() {
        toolBar.setTitle(this.product?.sharesName ?: "")
        toolBar.backListener = {
            onBackPressed()
        }
    }

    private fun renderList() {
        productRecyclerViewAdapter.notifyDataSetChanged()
    }

    override fun bidIntentionSuccess(msg: String) {
        toast(msg)
    }

    override fun renderActionBar(backgroundColorResId: Int, textResId: Int, statusInfoStringResId: Int, onClick: (() -> Unit)?) {
        statusView.setText(textResId)
        statusView.setBackgroundColor(ContextCompat.getColor(this.applicationContext, backgroundColorResId))
        if(statusInfoStringResId != 0){
            statusInfoView.setText(statusInfoStringResId)
        }
        statusView.setOnClickListener {
            onClick?.invoke()

        }
    }

    override fun followSuccess(msg: String, productId: Long) {
        toast(msg)
        this.product?.attention = 1
        productRecyclerViewAdapter.notifyItemChanged(0)
    }

    override fun renderBidList(list: MutableList<Bid>) {
        bidList.clear()
        bidList.addAll(list)
        productRecyclerViewAdapter.notifyDataSetChanged()
    }

}
