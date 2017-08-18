package com.zhengdianfang.dazhongbao.views.user

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import com.zhengdianfang.dazhongbao.R
import com.zhengdianfang.dazhongbao.models.product.Product
import com.zhengdianfang.dazhongbao.views.basic.BaseActivity
import com.zhengdianfang.dazhongbao.views.components.Toolbar
import com.zhengdianfang.dazhongbao.views.user.adapter.MyFollowProductRecyclerAdapter

class MyFollowProductListActivity : BaseActivity() {

    private val toolBar by lazy { findViewById<Toolbar>(R.id.toolbar) }
    private val myFollowProductRecyclerView by lazy { findViewById<RecyclerView>(R.id.myFollowProductRecyclerView) }
    private val followProducts = mutableListOf<Product>()
    private val myFollowProductAdapter by lazy { MyFollowProductRecyclerAdapter(followProducts) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_follow_product_list)

        toolBar.backListener = {onBackPressed()}
        myFollowProductRecyclerView.adapter = myFollowProductAdapter

    }

    override fun onBackPressed() {
        finish()
    }
}
