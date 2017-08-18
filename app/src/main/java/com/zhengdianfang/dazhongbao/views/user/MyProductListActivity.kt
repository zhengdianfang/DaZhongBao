package com.zhengdianfang.dazhongbao.views.user

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import com.zhengdianfang.dazhongbao.R
import com.zhengdianfang.dazhongbao.models.product.Product
import com.zhengdianfang.dazhongbao.views.basic.BaseActivity
import com.zhengdianfang.dazhongbao.views.components.Toolbar
import com.zhengdianfang.dazhongbao.views.user.adapter.MyProductRecyclerAdapter

class MyProductListActivity : BaseActivity() {

    private val toolBar by lazy { findViewById<Toolbar>(R.id.toolbar) }
    private val myProductRecyclerView by lazy { findViewById<RecyclerView>(R.id.myProductRecyclerView) }
    private val myProducts = mutableListOf<Product>()
    private val myProductAdapter by lazy { MyProductRecyclerAdapter(myProducts) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_product_list)
        toolBar.backListener = {
            onBackPressed()
        }

        myProductRecyclerView.adapter = myProductAdapter
    }

    override fun onBackPressed() {
        finish()
    }
}
