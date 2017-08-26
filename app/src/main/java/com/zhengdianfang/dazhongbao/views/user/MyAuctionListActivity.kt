package com.zhengdianfang.dazhongbao.views.user


import android.graphics.Rect
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.View
import com.jcodecraeer.xrecyclerview.XRecyclerView
import com.orhanobut.logger.Logger
import com.zhengdianfang.dazhongbao.CApplication
import com.zhengdianfang.dazhongbao.R
import com.zhengdianfang.dazhongbao.helpers.Action
import com.zhengdianfang.dazhongbao.helpers.PixelUtils
import com.zhengdianfang.dazhongbao.helpers.RemoveBidResult
import com.zhengdianfang.dazhongbao.helpers.RxBus
import com.zhengdianfang.dazhongbao.models.product.Product
import com.zhengdianfang.dazhongbao.presenters.PushBidPresenter
import com.zhengdianfang.dazhongbao.presenters.UserPresenter
import com.zhengdianfang.dazhongbao.views.basic.BaseListActivity
import com.zhengdianfang.dazhongbao.views.components.Toolbar
import com.zhengdianfang.dazhongbao.views.user.adapter.MyAucationItemAdapter
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer


/**
 * A simple [Fragment] subclass.
 */
class MyAuctionListActivity : BaseListActivity<Product>(), PushBidPresenter.IRemoveBidView , UserPresenter.IUserAuctionListView{

    private val pushPresenter = PushBidPresenter()
    private val userPresenter = UserPresenter()
    private val toolBar by lazy { findViewById<Toolbar>(R.id.toolbar) }

    private var removeBidDisposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_my_auction_list)
        pushPresenter.attachView(this)
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
        removeBidDisposable = RxBus.instance.register(Action.REMOVE_BID_ACTION, Consumer { (type, data) ->
            if (data is RemoveBidResult) {
                val filters = datas.filter { it.id == data.productId }
                filters.forEach {
                    it.mybids = it.mybids?.filter { it.bidid != data.bidId }?.toMutableList()
                    Logger.d("remove bid result : ${it.mybids}")
                    adapter.notifyItemChanged(datas.indexOf(it) + 1)
                }
            }
        })
        recyclerView.refresh()
    }


    override fun onDestroy() {
        super.onDestroy()
        pushPresenter.detachView()
        userPresenter.detachView()
        RxBus.instance.unregister(removeBidDisposable)
    }

    override fun requestList(pageNumber: Int) {
        userPresenter.fetchUserAuctionProducts(CApplication.INSTANCE.loginUser?.token!!)
    }

    override fun createRecyclerView(): XRecyclerView {
        return findViewById(R.id.productRecyclerView)!!
    }

    override fun createRecyclerViewAdapter(): RecyclerView.Adapter<*> {
        return MyAucationItemAdapter(datas, pushPresenter)
    }

    override fun removeBidSuccess(msg: String) {
        toast(msg)
    }

    override fun receiveUserAuctionList(list: MutableList<Product>) {
        reponseProcessor(list)
    }
}// Required empty public constructor
