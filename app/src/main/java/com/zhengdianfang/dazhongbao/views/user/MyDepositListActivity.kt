package com.zhengdianfang.dazhongbao.views.user

import android.graphics.Rect
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.View
import com.zhengdianfang.dazhongbao.CApplication
import com.zhengdianfang.dazhongbao.R
import com.zhengdianfang.dazhongbao.helpers.PixelUtils
import com.zhengdianfang.dazhongbao.models.product.Product
import com.zhengdianfang.dazhongbao.presenters.UserPresenter
import com.zhengdianfang.dazhongbao.views.basic.BaseListActivity
import com.zhengdianfang.dazhongbao.views.components.Toolbar
import com.zhengdianfang.dazhongbao.views.user.adapter.MyDepositItemAdapter

class MyDepositListActivity : BaseListActivity<Product>(), UserPresenter.IUserDepositListView {
    private val userPresenter = UserPresenter()
    private val toolBar by lazy { findViewById<Toolbar>(R.id.toolbar) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_deposit)
        userPresenter.attachView(this)
        toolBar.backListener = {
            onBackPressed()
        }
        setupRecyclerView()
        recyclerView.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect?, view: View?, parent: RecyclerView?, state: RecyclerView.State?) {
                if (parent?.getChildAdapterPosition(view) != 0)
                    if(outRect != null){
                        outRect!!.bottom += PixelUtils.dp2px(view?.context!!, 8f ).toInt()
                    }

            }
        })
        autoRefresh()
    }

    override fun onDestroy() {
        super.onDestroy()
        userPresenter.detachView()
    }

    override fun onBackPressed() {
        finish()
    }

    override fun requestList(pageNumber: Int) {
        userPresenter.fetchDepositProducts(CApplication.INSTANCE.loginUser?.token!!)
    }

    override fun createRecyclerView(): RecyclerView {
        return findViewById(R.id.productRecyclerView)
    }

    override fun createRecyclerViewAdapter(): RecyclerView.Adapter<*> {
        return MyDepositItemAdapter(datas)
    }

    override fun receiveUserDepositList(list: MutableList<Product>) {
        reponseProcessor(list)
    }
}
