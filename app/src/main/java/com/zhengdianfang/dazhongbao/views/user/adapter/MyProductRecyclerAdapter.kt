package com.zhengdianfang.dazhongbao.views.user.adapter

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.zhengdianfang.dazhongbao.R
import com.zhengdianfang.dazhongbao.helpers.*
import com.zhengdianfang.dazhongbao.models.product.Product
import com.zhengdianfang.dazhongbao.views.basic.BaseActivity
import com.zhengdianfang.dazhongbao.views.product.PayBondFragment
import com.zhengdianfang.dazhongbao.views.product.ProductDetailActivity
import java.util.*

/**
 * Created by dfgzheng on 18/08/2017.
 */

class MyProductRecyclerAdapter(private val myProducts: MutableList<Product>) : RecyclerView.Adapter<MyProductRecyclerViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MyProductRecyclerViewHolder {
        return MyProductRecyclerViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.my_product_list_item, parent, false))
    }

    override fun onBindViewHolder(holder: MyProductRecyclerViewHolder?, position: Int) {
        holder?.setData(myProducts[position])
    }

    override fun getItemCount(): Int {
        return myProducts.count()
    }

}

class MyProductRecyclerViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {

    private val sharesNameView = itemView?.findViewById<TextView>(R.id.sharesNameView)!!
    private val soldCountView = itemView?.findViewById<TextView>(R.id.soldCountView)!!
    private val industryNameView = itemView?.findViewById<TextView>(R.id.industryNameView)!!
    private val basicPriceView = itemView?.findViewById<TextView>(R.id.basicPriceView)!!
    private val statusView = itemView?.findViewById<TextView>(R.id.statusView)!!
    private val payButton = itemView?.findViewById<Button>(R.id.payButton)!!
    private val bidListViewGroup = itemView?.findViewById<ViewGroup>(R.id.bidListViewGroup)!!

    fun setData(product: Product) {
        val context = itemView?.context!!
        sharesNameView.text = ViewsUtils.renderSharesNameAndCode(product.sharesName, product.sharesCode)
        soldCountView.text = ViewsUtils.renderSharesSoldCount(context, product.soldCount)
        industryNameView.text = product.industry

        basicPriceView.text = ViewsUtils.renderSharesPrice(context, product.basicUnitPrice, R.string.starting_price_label)
        statusView.text = ViewsUtils.renderStatusView(context, product, {canPay, dealSuccess ->
            payButton.visibility = if (canPay) View.VISIBLE else View.GONE
            bidListViewGroup.visibility = if (dealSuccess) View.VISIBLE else View.GONE
        })
        renderStatusView(context, product)
        itemView.setOnClickListener {
            ProductDetailActivity.startActivity(context, product.id)
        }
        payButton.setOnClickListener {
           if(context is BaseActivity){
               val fragment = PayBondFragment()
               fragment.product = product
               context.startFragment(android.R.id.content, fragment, "myProductItem")
           }
        }
    }

    private fun renderStatusView(context: Context, product: Product) {
        val highlightColor = ContextCompat.getColor(context, R.color.colorPrimary)
        val textSize = PixelUtils.sp2px(context, 14f)
        payButton.visibility = View.GONE
        bidListViewGroup.visibility = View.GONE
        when(product.check_status){
            1 -> {
                val statusString = context.getString(R.string.status_verifing_label)
                statusView.text = SpannableStringUtils.addColorSpan(context.getString(R.string.status_label, statusString), statusString, highlightColor, textSize.toInt())
            }
            2 -> {
                val statusString = context.getString(R.string.verify_fail_status)
                statusView.text = SpannableStringUtils.addColorSpan(context.getString(R.string.status_label, statusString), statusString, highlightColor, textSize.toInt())
            }
            3 -> {
                val statusString = context.getString(R.string.product_status_itention_info)
                statusView.text = SpannableStringUtils.addColorSpan(context.getString(R.string.status_label, statusString), statusString, highlightColor, textSize.toInt())
            }
            4 -> {
                if(product.bond_status != 2){
                    val statusString = context.getString(R.string.product_status_margin)
                    statusView.text = SpannableStringUtils.addColorSpan(context.getString(R.string.status_label, statusString), statusString, highlightColor, textSize.toInt())
                    payButton.visibility = View.VISIBLE                }else{
                    val (day , hour, _) = DateUtils.diffTime(Date(System.currentTimeMillis()), Date(product.startDateTime))
                    val statusString = context.getString(R.string.my_start_gap_time, day.toString(), hour.toString())
                    statusView.text = SpannableStringUtils.addColorSpan(context.getString(R.string.my_start_gap_label, statusString), statusString, highlightColor, textSize.toInt())
                }
            }
            5 -> {
                val statusString = context.getString(R.string.product_status_auctioning)
                statusView.text = SpannableStringUtils.addColorSpan(context.getString(R.string.status_label, statusString), statusString, highlightColor, textSize.toInt())
            }
            6 -> {
                val statusString = context.getString(R.string.ready_beal_status)
                statusView.text = SpannableStringUtils.addColorSpan(context.getString(R.string.status_label, statusString), statusString, highlightColor, textSize.toInt())
                bidListViewGroup.visibility = View.VISIBLE
                renderBidListView(context, product)
            }

        }
    }

    private fun renderBidListView(context: Context, product: Product) {
        val bidListView = bidListViewGroup.findViewById<ViewGroup>(R.id.bidListView)
        val dealSoldCountView = bidListViewGroup.findViewById<TextView>(R.id.dealSoldCountView)
        val totalSoldPriceView = bidListViewGroup.findViewById<TextView>(R.id.totalSoldPriceView)
        val expandableView = bidListViewGroup.findViewById<TextView>(R.id.expandableView)
        var soldTotalCount = 0
        var totalSoldPrice = 0.0
        dealSoldCountView.text = context.getString(R.string.auction_success_total_count, (soldTotalCount / Constants.SOLD_COUNT_BASE_UNIT).toString())
        totalSoldPriceView.text = context.getString(R.string.my_product_item_total_price_label, (totalSoldPrice / Constants.SOLD_COUNT_BASE_UNIT).toString())
        bidListView.removeAllViews()
        if(product.deal.isEmpty()) {
            bidListView.visibility = View.GONE
        }else {
            bidListView.visibility = View.VISIBLE
            product.deal.forEach {
                soldTotalCount += it.count
                totalSoldPrice += (it.count * it.price)
                val itemView =  LayoutInflater.from(context).inflate(R.layout.my_product_bid_list_item_layout, null, false)
                itemView.findViewById<TextView>(R.id.realnameView).text = it.realname
                itemView.findViewById<TextView>(R.id.dealCountView).text =  "${context.getString(R.string.sold_count_value, (it.count / Constants.SOLD_COUNT_BASE_UNIT).toString())}${context.getString(R.string.stock_unit)}"
                itemView.findViewById<TextView>(R.id.dealUnitPriceView).text = "${it.price}${context.getString(R.string.fragment_push_price_unit)}"
                itemView.findViewById<TextView>(R.id.dealTotlaPriceView).text = context.getString(R.string.my_product_item_total_price_label, (it.price * it.count / Constants.SOLD_COUNT_BASE_UNIT).toString())
                bidListView.addView(itemView)
            }
            expandableView.setOnClickListener {
                if(bidListView.isShown) {
                    bidListView.visibility = View.GONE
                    expandableView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_up_icon, 0)
                }else {
                    bidListView.visibility = View.VISIBLE
                    expandableView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_down_icon, 0)
                }
            }
        }
    }
}