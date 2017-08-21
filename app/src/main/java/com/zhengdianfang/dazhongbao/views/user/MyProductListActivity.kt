package com.zhengdianfang.dazhongbao.views.user

import android.graphics.Rect
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.View
import com.jcodecraeer.xrecyclerview.XRecyclerView
import com.zhengdianfang.dazhongbao.CApplication
import com.zhengdianfang.dazhongbao.R
import com.zhengdianfang.dazhongbao.models.product.Product
import com.zhengdianfang.dazhongbao.presenters.UserPresenter
import com.zhengdianfang.dazhongbao.views.basic.BaseListActivity
import com.zhengdianfang.dazhongbao.views.components.Toolbar
import com.zhengdianfang.dazhongbao.views.components.refreshLayout.utils.PixelUtils
import com.zhengdianfang.dazhongbao.views.user.adapter.MyProductRecyclerAdapter

class MyProductListActivity : BaseListActivity<Product>(), UserPresenter.IUserProductListView {

    private val toolBar by lazy { findViewById<Toolbar>(R.id.toolbar) }
    private val userPresenter by lazy { UserPresenter() }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_product_list)
        setupRecyclerView()
        userPresenter.attachView(this)
        toolBar.backListener = {
            onBackPressed()
        }
        recyclerView.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect?, view: View?, parent: RecyclerView?, state: RecyclerView.State?) {
                if (parent?.getChildAdapterPosition(view) != 0)
                if(outRect != null){
                    outRect!!.bottom += PixelUtils.dp2px(view?.context!!, 8f ).toInt()
                }

            }
        })

        recyclerView.refresh()

    }


    override fun onDestroy() {
        super.onDestroy()
        userPresenter.detachView()
    }

    override fun onBackPressed() {
        finish()
    }

    override fun receiveUserProductList(list: MutableList<Product>) {
        reponseProcessor(list)
    }

    override fun requestList(pageNumber: Int) {
        userPresenter.fetchUserPushedProducts(CApplication.INSTANCE.loginUser?.token!!)
    }

    override fun createRecyclerView(): XRecyclerView {
        return  findViewById(R.id.myProductRecyclerView)
    }

    override fun createRecyclerViewAdapter(): RecyclerView.Adapter<*> {
        return  MyProductRecyclerAdapter(datas)
    }
}
