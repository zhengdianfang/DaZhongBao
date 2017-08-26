package com.zhengdianfang.dazhongbao.views.product

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.jcodecraeer.xrecyclerview.XRecyclerView
import com.zhengdianfang.dazhongbao.CApplication
import com.zhengdianfang.dazhongbao.R
import com.zhengdianfang.dazhongbao.helpers.Action
import com.zhengdianfang.dazhongbao.helpers.RxBus
import com.zhengdianfang.dazhongbao.models.product.Bid
import com.zhengdianfang.dazhongbao.models.product.Product
import com.zhengdianfang.dazhongbao.presenters.FollowProductPresenter
import com.zhengdianfang.dazhongbao.presenters.ProductDetailPresenter
import com.zhengdianfang.dazhongbao.views.basic.BaseActivity
import com.zhengdianfang.dazhongbao.views.components.Toolbar
import com.zhengdianfang.dazhongbao.views.product.adapter.ProductDetailFooterViewHolder
import com.zhengdianfang.dazhongbao.views.product.adapter.ProductDetailHeaderViewHolder
import com.zhengdianfang.dazhongbao.views.product.adapter.ProductRecyclerViewAdapter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import java.util.concurrent.TimeUnit

class ProductDetailActivity : BaseActivity() , ProductDetailPresenter.IProductInfoView, FollowProductPresenter.IFollowProductView {

    companion object {
        fun startActivity(context: Context, productId: Long){
            context.startActivity(Intent(context, ProductDetailActivity::class.java).putExtra("productId", productId))
        }
    }

    private var product: Product? = null
    private val bidList = arrayListOf<Bid>()
    private val productRecyclerViewAdapter by lazy { ProductRecyclerViewAdapter(bidList, productNotesFooterViewHolder.itemView) }
    private val productRecyclerView by lazy { findViewById<XRecyclerView>(R.id.productRecyclerView) }
    private val productDetailPresenter by lazy { ProductDetailPresenter() }
    private val followProductPresenter by lazy { FollowProductPresenter() }

    private val statusView by lazy { findViewById<TextView>(R.id.statusView) }
    private val statusInfoView by lazy { findViewById<TextView>(R.id.statusInfoView) }
    private val toolBar by lazy { findViewById<Toolbar>(R.id.toolbar) }
    private val loadingView by lazy { findViewById<View>(R.id.loadingView) }
    private val productId by lazy { intent.getLongExtra("productId", -1L) }

    private val productDetailHeaderViewHolder by lazy { ProductDetailHeaderViewHolder(LayoutInflater.from(this).inflate(R.layout.product_detail_header, productRecyclerView, false),{productId -> intentionOnClick(productId)} )}
    private val productNotesFooterViewHolder by lazy { ProductDetailFooterViewHolder(LayoutInflater.from(this).inflate(R.layout.product_notes_footer, productRecyclerView, false)) }
    private val timerObservable = Observable.timer(1, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread())
    private var timerDisposable: Disposable? = null
    private var followDisposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        productDetailPresenter.attachView(this)
        followProductPresenter.attachView(this)
        setupRecyclerView()
        followDisposable = RxBus.instance.register(arrayOf(Action.FOLLOW_PRODUCT_ACTION, Action.CANCEL_FOLLOW_PRODUCT_ACTION), Consumer { (type, data)->
            if (data is Long) {
                productDetailHeaderViewHolder.attention(this.applicationContext, type == Action.FOLLOW_PRODUCT_ACTION, productId)
                this.product?.attention = if(type == Action.FOLLOW_PRODUCT_ACTION) 1 else 0
            }
        })
        productRecyclerView.refresh()
    }

    override fun onDestroy() {
        super.onDestroy()
        productDetailPresenter.detachView()
        followProductPresenter.detachView()
        RxBus.instance.unregister(followDisposable)
    }

    private fun setupRecyclerView() {
        productRecyclerView.addHeaderView(productDetailHeaderViewHolder.itemView)
        productRecyclerView.setLoadingListener(object : XRecyclerView.LoadingListener {
            override fun onLoadMore() {
            }

            override fun onRefresh() {
                productDetailPresenter.fetchProductInfoAndBidList(productId)
            }

        })
        productRecyclerView.setLoadingMoreEnabled(false)
        productRecyclerView.adapter = productRecyclerViewAdapter
    }

    private fun renderProductHeaderView(product: Product) {
        productDetailHeaderViewHolder.setData(product)
    }


    override fun renderProductInfo(product: Product) {
        this.product = product
        renderProductHeaderView(product)
        renderToolbar()
        val (textResId, backgroundColorId) = productDetailPresenter.getStatusViewStyle(this, product)
        val notesString = productDetailPresenter.getStatusNoteString(this.applicationContext, product)
//        if (productDetailPresenter.canStartTimer(productDetailPresenter.getStatusViewType(product))){
//            timerDisposable = timerObservable.subscribe{
//                statusInfoView.text = productDetailPresenter.getStatusNoteString(this.applicationContext, product!!)
//            }
//        }else {
//            if (timerDisposable?.isDisposed == false){
//                timerDisposable?.dispose()
//            }
//        }
        renderActionBar(backgroundColorId, textResId, notesString, productDetailPresenter.getStatusViewType(product))
        renderList()
        renderNotesFooter(product)
        productRecyclerView.refreshComplete()
        loadingView.visibility = View.GONE
    }

    private fun renderNotesFooter(product: Product) {
        productNotesFooterViewHolder.setData(product.description)
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

    private fun intentionOnClick(productId: Long){
        if(product?.attention == 1){
            followProductPresenter.unfollowProduct(CApplication.INSTANCE.loginUser?.token!!, productId)
        }else {
            followProductPresenter.followProduct(CApplication.INSTANCE.loginUser?.token!!, productId)
        }
    }

    private fun renderActionBar(backgroundColorResId: Int, textResId: Int, statusInfoString: String, buttonType: Int) {
        statusView.setText(textResId)
        statusView.setBackgroundColor(ContextCompat.getColor(this.applicationContext, backgroundColorResId))
        if(!TextUtils.isEmpty(statusInfoString)){
            statusInfoView.text = statusInfoString
            statusInfoView.visibility = View.VISIBLE
        }else {
            statusInfoView.visibility = View.GONE
        }
        statusView.setOnClickListener {
            when(buttonType){
                ProductDetailPresenter.SUMBIT_ATTETION_BUTTON_TYPE -> {
                    productDetailPresenter.addBidIntention(productId)
                }
                ProductDetailPresenter.PAY_BOND_BUTTON_TYPE -> {
                    val fragment = PayBondFragment()
                    fragment.product = this.product
                    startFragment(android.R.id.content, fragment)
                }
                ProductDetailPresenter.AUCTIONING_BUTTON_TYPE -> {
                    val fragment = CreateBidFragment()
                    fragment.product = this.product
                    startFragment(android.R.id.content, fragment)
                }
            }
        }
    }

    override fun followSuccess(msg: String) {
        toast(msg)
    }

    override fun renderBidList(list: MutableList<Bid>) {
        bidList.clear()
        bidList.addAll(list)
        productRecyclerViewAdapter.notifyDataSetChanged()
    }

    override fun unfollowSuccess(msg: String) {
        toast(msg)
    }
}
