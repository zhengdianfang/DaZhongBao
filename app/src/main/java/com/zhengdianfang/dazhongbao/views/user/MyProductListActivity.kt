package com.zhengdianfang.dazhongbao.views.user

import android.graphics.Rect
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.View
import com.zhengdianfang.dazhongbao.CApplication
import com.zhengdianfang.dazhongbao.R
import com.zhengdianfang.dazhongbao.models.product.Product
import com.zhengdianfang.dazhongbao.presenters.UserPresenter
import com.zhengdianfang.dazhongbao.views.basic.BaseActivity
import com.zhengdianfang.dazhongbao.views.components.Toolbar
import com.zhengdianfang.dazhongbao.views.components.refreshLayout.utils.PixelUtils
import com.zhengdianfang.dazhongbao.views.user.adapter.MyProductRecyclerAdapter

class MyProductListActivity : BaseActivity(), UserPresenter.IUserProductListView {

    private val toolBar by lazy { findViewById<Toolbar>(R.id.toolbar) }
    private val myProductRecyclerView by lazy { findViewById<RecyclerView>(R.id.myProductRecyclerView) }
    private val myProducts = mutableListOf<Product>()
    private val myProductAdapter by lazy { MyProductRecyclerAdapter(myProducts) }
    private val userPresenter by lazy { UserPresenter() }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_product_list)
        userPresenter.attachView(this)
        toolBar.backListener = {
            onBackPressed()
        }
        myProductRecyclerView.adapter = myProductAdapter
        myProductRecyclerView.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect?, view: View?, parent: RecyclerView?, state: RecyclerView.State?) {
                if(outRect != null){
                    outRect!!.bottom += PixelUtils.dp2px(view?.context!!, 8f ).toInt()
                }

            }
        })

        userPresenter.fetchUserPushedProduct(CApplication.INSTANCE.loginUser?.token!!)
    }

    override fun onDestroy() {
        super.onDestroy()
        userPresenter.detachView()
    }

    override fun onBackPressed() {
        finish()
    }

    override fun receiveUserProductList(list: MutableList<Product>) {
        this.myProducts.clear()
        this.myProducts.addAll(list)
        myProductAdapter.notifyDataSetChanged()
    }
}
