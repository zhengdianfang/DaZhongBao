package com.zhengdianfang.dazhongbao.views.home


import android.graphics.Rect
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jcodecraeer.xrecyclerview.XRecyclerView
import com.zhengdianfang.dazhongbao.CApplication
import com.zhengdianfang.dazhongbao.R
import com.zhengdianfang.dazhongbao.RxBusUtils
import com.zhengdianfang.dazhongbao.helpers.PixelUtils
import com.zhengdianfang.dazhongbao.helpers.RxBus
import com.zhengdianfang.dazhongbao.models.product.Product
import com.zhengdianfang.dazhongbao.presenters.AuctionPresenter
import com.zhengdianfang.dazhongbao.presenters.FollowProductPresenter
import com.zhengdianfang.dazhongbao.views.basic.BaseListFragment
import com.zhengdianfang.dazhongbao.views.home.adapter.AuctionItemAdapter
import io.reactivex.disposables.Disposable


/**
 * A simple [Fragment] subclass.
 */
class AuctionFragment : BaseListFragment<Product>(), AuctionPresenter.IAuctionListView, FollowProductPresenter.IFollowProductView  {

    private val auctionPresenter = AuctionPresenter()
    private val followProductPresenter = FollowProductPresenter()

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_auction, container, false)
    }

    private var followDisposable: Disposable? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        auctionPresenter.attachView(this)
        followProductPresenter.attachView(this)
        setupRecyclerView()

        followDisposable = RxBusUtils.registerFollowAndUnFollowProductActionsForRecyclerView(datas, { pos -> adapter.notifyItemChanged(pos + 1)})
        recyclerView.refresh()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        auctionPresenter.detachView()
        followProductPresenter.detachView()
        RxBus.instance.unregister(followDisposable)
    }

    override fun onBackPressed(): Boolean {
        getParentActivity().finish()
        return true
    }

    override fun createRecyclerView(): XRecyclerView {
        return view?.findViewById(R.id.auctionRecyclerView)!!
    }

    override fun createRecyclerViewAdapter(): RecyclerView.Adapter<*> {
        return AuctionItemAdapter(datas, {productId ->
           followProductPresenter.followProduct(CApplication.INSTANCE.loginUser?.token!!, productId)
        })
    }

    private fun setupRecyclerView() {
        recyclerView.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect?, view: View?, parent: RecyclerView?, state: RecyclerView.State?) {
                if (parent?.getChildAdapterPosition(view) != 0)
                if (outRect != null) {
                    outRect!!.bottom += PixelUtils.dp2px(view?.context!!, 8f).toInt()
                }

            }
        })
    }

    override fun requestList(pageNumber: Int) {
        auctionPresenter.fetchAuctionList(CApplication.INSTANCE.loginUser?.token!!, pageNumber)
    }


    override fun receiveAuctionProductList(list: MutableList<Product>) {
        reponseProcessor(list)
    }

    override fun followSuccess(msg: String) {
        toast(msg)
    }

    override fun unfollowSuccess(msg: String) {
        toast(msg)
    }


}// Required empty public constructor
