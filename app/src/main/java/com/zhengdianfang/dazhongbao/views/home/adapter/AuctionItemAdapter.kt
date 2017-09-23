package com.zhengdianfang.dazhongbao.views.home.adapter

import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.DrawableCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.orhanobut.logger.Logger
import com.zhengdianfang.dazhongbao.R
import com.zhengdianfang.dazhongbao.helpers.DateUtils
import com.zhengdianfang.dazhongbao.helpers.ViewsUtils
import com.zhengdianfang.dazhongbao.models.product.Product
import com.zhengdianfang.dazhongbao.presenters.ProductDetailPresenter
import com.zhengdianfang.dazhongbao.views.basic.BaseActivity
import com.zhengdianfang.dazhongbao.views.components.ColorArcProgressBar
import com.zhengdianfang.dazhongbao.views.product.PayDepositFragment
import com.zhengdianfang.dazhongbao.views.product.ProductDetailActivity
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit


/**
 * Created by dfgzheng on 21/08/2017.
 */
class AuctionItemAdapter(private val auctionProducts: MutableList<Product>, private val intentionOnClick: (productId: Long)->Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val timeObservable = Observable.interval(1, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread())
    private var subscribe: Disposable? = null

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        if (getItemViewType(position) == 0){
            val auctionFirstItemViewHolder = (holder as AuctionFirstItemViewHolder)
            auctionFirstItemViewHolder.setData(auctionProducts[position])
            auctionFirstItemViewHolder.timerTextView.text = DateUtils.calTimeDistanceByHH_MM_SS(System.currentTimeMillis(), auctionProducts[position].endDateTime)
            subscribe = timeObservable.subscribe {
                auctionFirstItemViewHolder.timerTextView.text = DateUtils.calTimeDistanceByHH_MM_SS(System.currentTimeMillis(), auctionProducts[position].endDateTime)
            }
        }else{
            val auctionNormalViewHolder = holder as AuctionNormalItemViewHolder
            auctionNormalViewHolder.attentionButton.setOnClickListener {
               intentionOnClick(auctionProducts[position].id)
            }
            auctionNormalViewHolder.setData(auctionProducts[position])
        }

        holder.itemView.setOnClickListener {
            ProductDetailActivity.startActivity(holder.itemView.context, auctionProducts[position].id)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 0) {
            AuctionFirstItemViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.fragment_auction_doing_item, parent, false))
        }else{
            AuctionNormalItemViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.fragment_auction_ready_item, parent, false))
        }
    }

    override fun getItemCount(): Int {
        return auctionProducts.count()
    }

    override fun getItemViewType(position: Int): Int {
        if (position == 0){
            return 0
        }
        return 1
    }

    fun destory() {
        if (subscribe?.isDisposed!!) {
            subscribe!!.dispose()
        }
    }
}

class AuctionFirstItemViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
    private val productDetailPresenter = ProductDetailPresenter()
    private val shareCodeView by lazy { itemView?.findViewById<TextView>(R.id.shareCodeView)!! }
    private val industryView by lazy { itemView?.findViewById<TextView>(R.id.industryView)!! }
    private val statusButton by lazy { itemView?.findViewById<Button>(R.id.statusButton)!! }
    private val basicPriceView by lazy { itemView?.findViewById<TextView>(R.id.basicPriceView)!! }
    private val soldCountView by lazy { itemView?.findViewById<TextView>(R.id.soldCountView)!! }
    private val endTimeView by lazy { itemView?.findViewById<TextView>(R.id.endTimeView)!! }
    private val nowUnitPriceView by lazy { itemView?.findViewById<TextView>(R.id.nowUnitPriceView)!! }
    private val limitTimeView by lazy { itemView?.findViewById<TextView>(R.id.limitTimeView)!! }
    val timerTextView by lazy { itemView?.findViewById<TextView>(R.id.timerTextView)!! }
    private val timeProgressbar by lazy { itemView?.findViewById<ColorArcProgressBar>(R.id.timeProgressbar)!! }
    private val bidCountView by lazy { itemView?.findViewById<TextView>(R.id.bidCountView)!! }

    fun setData(product: Product){
        val context = itemView?.context!!
        shareCodeView.text = "[${product.sharesCode}]"
        industryView.text = product.sharesName
        basicPriceView.text = ViewsUtils.renderSharesPrice(context, product.basicUnitPrice, R.string.start_auction_price)
        soldCountView.text = ViewsUtils.renderSharesSoldCount(context, product.soldCount)
        nowUnitPriceView.text = ViewsUtils.renderSharesPrice(context, product.nowUnitPrice, R.string.now_price)
        if (product.limitTime == 0){
            limitTimeView.text = context.getString(R.string.fragment_push_limit_label)
            limitTimeView.visibility = View.VISIBLE
        }else {
            limitTimeView.visibility = View.GONE
        }
        val (textResId, _, onClick) = productDetailPresenter.getStatusViewStyle(context as BaseActivity, product)
        val drawable = ContextCompat.getDrawable(context, R.drawable.status_button_background)
        val wrappedDrawable = DrawableCompat.wrap(drawable)
        DrawableCompat.setTint(wrappedDrawable, ContextCompat.getColor(context, R.color.colorPrimary))
        statusButton.background = drawable
        statusButton.setText(textResId)
        statusButton.setOnClickListener {
            onClick?.invoke()
        }
        bidCountView.text = context.getString(R.string.bid_count_label, product.bidCount)
        endTimeView.text = context.getString(R.string.finish_auction_time, DateUtils.formatTime(product.endDateTime))
        timeProgressbar.max = 100
        val startTime = DateUtils.changeTimeLenght(product.startDateTime)
        val endTime = DateUtils.changeTimeLenght(product.endDateTime)
        val nowTime = System.currentTimeMillis()
        Logger.d("now : ${Math.abs(endTime - nowTime)}, total: ${Math.abs(endTime - startTime)}")
        val totalTime = Math.abs(endTime - startTime)
        val goingTime = totalTime -  Math.abs(endTime - nowTime).toDouble()
        Logger.d("cal present: ${(goingTime / totalTime  * 100).toInt()}")
        timeProgressbar.progress =  (goingTime / totalTime  * 100).toInt()
    }

}

class AuctionNormalItemViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) { private val sharesNameView = itemView?.findViewById<TextView>(R.id.sharesNameView)!!
    private val soldCountView = itemView?.findViewById<TextView>(R.id.soldCountView)!!
    private val industryNameView = itemView?.findViewById<TextView>(R.id.industryNameView)!!
    private val basicPriceView = itemView?.findViewById<TextView>(R.id.basicPriceView)!!
    private val statusView = itemView?.findViewById<TextView>(R.id.statusView)!!
    val attentionButton = itemView?.findViewById<Button>(R.id.attentionButton)!!
    private val payButton = itemView?.findViewById<Button>(R.id.payButton)!!

    fun setData(product: Product){
        val context = itemView?.context!!
        sharesNameView.text = ViewsUtils.renderSharesNameAndCode(context, product.sharesName, product.sharesCode)

        soldCountView.text = ViewsUtils.renderSharesSoldCount(context, product.soldCount)
        industryNameView.text = product.industry

        basicPriceView.text = ViewsUtils.renderSharesPrice(context, product.basicUnitPrice, R.string.intention_price_label)
        ViewsUtils.renderAttentionView(context, product.attention, {textResId, color, backgroundResId ->
            attentionButton.setText(textResId)
            attentionButton.setTextColor(color)
            attentionButton.setBackgroundResource(backgroundResId)
        })
        statusView.text = ViewsUtils.renderStatusView(context, product, { canPay, _ ->
            if(canPay) {
                payButton.visibility = View.VISIBLE
            }else {
                payButton.visibility = View.GONE
            }
        })

        payButton.setOnClickListener {
            if(context is BaseActivity){
                val fragment = PayDepositFragment()
                fragment.product = product
                context.startFragment(android.R.id.content, fragment, "myProductItem")
            }
        }

    }

}