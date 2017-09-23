package com.zhengdianfang.dazhongbao.views.user

import android.graphics.Rect
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.View
import com.zhengdianfang.dazhongbao.CApplication
import com.zhengdianfang.dazhongbao.R
import com.zhengdianfang.dazhongbao.helpers.Action
import com.zhengdianfang.dazhongbao.helpers.PixelUtils
import com.zhengdianfang.dazhongbao.helpers.RxBus
import com.zhengdianfang.dazhongbao.models.product.Product
import com.zhengdianfang.dazhongbao.presenters.FollowProductPresenter
import com.zhengdianfang.dazhongbao.presenters.UserPresenter
import com.zhengdianfang.dazhongbao.views.basic.BaseListActivity
import com.zhengdianfang.dazhongbao.views.components.Toolbar
import com.zhengdianfang.dazhongbao.views.user.adapter.MyAttentionRecyclerAdapter
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer

class MyAttentionActivity : BaseListActivity<Product>(), UserPresenter.IUserAttentionListView, FollowProductPresenter.IFollowProductView {

    private val userPresenter = UserPresenter()
    private val followProductPresenter = FollowProductPresenter()
    private val toolBar by lazy { findViewById<Toolbar>(R.id.toolbar) }
    private var unFollowDisposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_attention)
        userPresenter.attachView(this)
        followProductPresenter.attachView(this)
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

        unFollowDisposable = RxBus.instance.register(Action.CANCEL_FOLLOW_PRODUCT_ACTION, Consumer { (type, data) ->
            if (data is Long) {
                val product = datas.find { it.id == data}
                if (product != null) {
                    adapter.notifyItemRemoved(datas.indexOf(product) + 1)
                    datas.remove(product)
                }
            }
        })
        autoRefresh()
    }

    override fun onDestroy() {
        super.onDestroy()
        userPresenter.detachView()
        followProductPresenter.detachView()
        RxBus.instance.unregister(unFollowDisposable)
    }


    override fun onBackPressed() {
        finish()
    }

    override fun requestList(pageNumber: Int) {
        userPresenter.fetchUserAttentionProducts(CApplication.INSTANCE.loginUser?.token!!)
    }

    override fun createRecyclerView(): RecyclerView {
        return findViewById(R.id.myAttentionRecyclerView)
    }

    override fun createRecyclerViewAdapter(): RecyclerView.Adapter<*> {
        return MyAttentionRecyclerAdapter(datas, { productId ->
            followProductPresenter.unfollowProduct(CApplication.INSTANCE.loginUser?.token!!, productId)
        })
    }

    override fun receiveUserAttentionList(list: MutableList<Product> , isCache: Boolean) {
        reponseProcessor(list, isCache)
    }

    override fun followSuccess(msg: String) {
        toast(msg)
    }

    override fun unfollowSuccess(msg: String) {
        toast(msg)
    }
}
