package com.zhengdianfang.dazhongbao.views.user.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.zhengdianfang.dazhongbao.CApplication
import com.zhengdianfang.dazhongbao.R
import com.zhengdianfang.dazhongbao.helpers.ViewsUtils
import com.zhengdianfang.dazhongbao.models.product.Product
import com.zhengdianfang.dazhongbao.presenters.PushBidPresenter

/**
 * Created by dfgzheng on 25/08/2017.
 */
class MyAucationItemAdapter(private val products: MutableList<Product>, private val pushBidPresenter: PushBidPresenter): RecyclerView.Adapter<MyAuctionItemViewHolder>() {

    override fun onBindViewHolder(holder: MyAuctionItemViewHolder?, position: Int) {
        holder?.setData(products[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MyAuctionItemViewHolder {
        return MyAuctionItemViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.my_auction_list_item, parent, false), pushBidPresenter)
    }

    override fun getItemCount(): Int {
        return products.count()
    }

}


class MyAuctionItemViewHolder(itemView: View?, private val pushBidPresenter: PushBidPresenter) : RecyclerView.ViewHolder(itemView) {

    private val sharesNameView by lazy { itemView?.findViewById<TextView>(R.id.sharesNameView)!! }
    private val statusView by lazy { itemView?.findViewById<TextView>(R.id.statusView)!! }
    private val industryNameView by lazy { itemView?.findViewById<TextView>(R.id.industryNameView)!! }
    private val basicPriceView by lazy { itemView?.findViewById<TextView>(R.id.basicPriceView)!! }
    private val soldCountView by lazy { itemView?.findViewById<TextView>(R.id.soldCountView)!! }
    private val nowUnitPriceView by lazy { itemView?.findViewById<TextView>(R.id.nowUnitPriceView)!! }
    private val cancelBidViewGroup by lazy { itemView?.findViewById<ViewGroup>(R.id.cancelBidViewGroup)!! }

    fun setData(product: Product) {
        val context = itemView?.context!!
        sharesNameView.text = ViewsUtils.renderSharesNameAndCode(product.sharesName, product.sharesCode)
        statusView.text = ViewsUtils.renderStatusView(context, product , { _, _ ->})
        industryNameView.text = product.industry
        basicPriceView.text = ViewsUtils.renderSharesPrice(context, product.basicUnitPrice, R.string.start_auction_price)
        soldCountView.text = ViewsUtils.renderSharesSoldCount(context, product.soldCount)
        nowUnitPriceView.text = ViewsUtils.renderSharesPrice(context, product.nowUnitPrice, R.string.now_price_label)
        cancelBidViewGroup.removeAllViews()
        ViewsUtils.renderBidListView(context, product, removeOnClick = {id->
            pushBidPresenter.removeBid(CApplication.INSTANCE.loginUser?.token!!, id)
        }).forEach {
            cancelBidViewGroup.addView(it)
        }
    }

}