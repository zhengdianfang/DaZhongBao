package com.zhengdianfang.dazhongbao.views.product

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import com.zhengdianfang.dazhongbao.R
import com.zhengdianfang.dazhongbao.models.product.Product
import com.zhengdianfang.dazhongbao.views.basic.BaseActivity
import com.zhengdianfang.dazhongbao.views.product.adapter.ProductRecyclerViewAdapter

class ProductDetailActivity : BaseActivity() {
    private val product: Product? = null
    private val bidList = arrayListOf<String>()
    private val productRecyclerViewAdapter by lazy { ProductRecyclerViewAdapter(product, bidList) }
    private val productRecyclerView by lazy { findViewById<RecyclerView>(R.id.productRecyclerView) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        productRecyclerView.adapter = productRecyclerViewAdapter
    }
}
