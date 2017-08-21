package com.zhengdianfang.dazhongbao.views.home.adapter

import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.DrawableCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.zhengdianfang.dazhongbao.R
import com.zhengdianfang.dazhongbao.helpers.Constants
import com.zhengdianfang.dazhongbao.helpers.DateUtils
import com.zhengdianfang.dazhongbao.helpers.SpannableStringUtils
import com.zhengdianfang.dazhongbao.models.product.Product
import com.zhengdianfang.dazhongbao.presenters.ProductDetailPresenter
import com.zhengdianfang.dazhongbao.views.components.refreshLayout.utils.PixelUtils
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by dfgzheng on 21/08/2017.
 */
class AuctionItemAdapter(private val auctionProducts: MutableList<Product>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        if (getItemViewType(position) == 0){
            (holder as AuctionFirstItemViewHolder).setData(auctionProducts[position])
        }else{
            (holder as AuctionNormalItemViewHolder).setData(auctionProducts[position]) }
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

    fun setData(product: Product){
        val context = itemView?.context!!
        val highlightColor = ContextCompat.getColor(context, R.color.c_f43d3d)
        shareCodeView.text = "[${product.sharesCode}]"
        industryView.text = product.industry
        basicPriceView.text = context.getString(R.string.start_auction_price, product.basicUnitPrice.toString())
        val soldCount = product.soldCount.toDouble() / Constants.SOLD_COUNT_BASE_UNIT.toDouble()
        val soldCountString = context.getString(R.string.sold_count_value, soldCount.toString())!!
        val soldCountSpannedString = SpannableStringUtils.addColorSpan(context.getString(R.string.product_item_to_sell,soldCountString)!!,
                soldCountString, highlightColor, PixelUtils.sp2px(context, 16f).toInt())
        soldCountView.text = soldCountSpannedString
        val wantPriceString = context.getString(R.string.price_unit_value, product.basicUnitPrice.toString())!!
        val wantPriceSpannedString = SpannableStringUtils.addColorSpan(context.getString(R.string.product_item_will_pay_price, wantPriceString)!!,
                wantPriceString, highlightColor, PixelUtils.sp2px(context, 14f).toInt())
        nowUnitPriceView.text = wantPriceSpannedString
        if (product.limitTime == 0){
            limitTimeView.text = context.getString(R.string.fragment_push_limit_label)
            limitTimeView.visibility = View.VISIBLE
        }else {
            limitTimeView.visibility = View.GONE
        }
        productDetailPresenter.getStatusView(product, {textResId, backgroundColorId ->
            val drawable = ContextCompat.getDrawable(context, R.drawable.status_button_background)
            val wrappedDrawable = DrawableCompat.wrap(drawable)
            DrawableCompat.setTint(wrappedDrawable, ContextCompat.getColor(context, backgroundColorId))
            statusButton.background = drawable
            statusButton.setText(textResId)

        })
        val format = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")

        endTimeView.text = context.getString(R.string.finish_auction_time, format.format(Date(product.endDateTime)))
    }

}

class AuctionNormalItemViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
    private val sharesNameView = itemView?.findViewById<TextView>(R.id.sharesNameView)!!
    private val soldCountView = itemView?.findViewById<TextView>(R.id.soldCountView)!!
    private val industryNameView = itemView?.findViewById<TextView>(R.id.industryNameView)!!
    private val basicPriceView = itemView?.findViewById<TextView>(R.id.basicPriceView)!!
    private val timeGapView = itemView?.findViewById<TextView>(R.id.timeGapView)!!
    private val attentionButton = itemView?.findViewById<Button>(R.id.attentionButton)!!

    fun setData(product: Product){
        val context = itemView?.context!!
        sharesNameView.text = "${product.sharesName} [${product.sharesCode}]"
        val highlightColor = ContextCompat.getColor(context, R.color.c_f43d3d)

        val soldCount = product.soldCount.toDouble() / Constants.SOLD_COUNT_BASE_UNIT.toDouble()
        val soldCountString = context.getString(R.string.sold_count_value, soldCount.toString())!!
        val soldCountSpannedString = SpannableStringUtils.addColorSpan(context.getString(R.string.product_item_to_sell,soldCountString)!!,
                soldCountString, highlightColor, PixelUtils.sp2px(context, 16f).toInt())
        soldCountView.text = soldCountSpannedString
        industryNameView.text = product.industry

        val wantPriceString = context.getString(R.string.price_unit_value, product.basicUnitPrice.toString())!!
        val wantPriceSpannedString = SpannableStringUtils.addColorSpan(context.getString(R.string.product_item_will_pay_price, wantPriceString)!!,
                wantPriceString, highlightColor, PixelUtils.sp2px(context, 14f).toInt())
        basicPriceView.text = wantPriceSpannedString
        if (product.attention == 0){
            attentionButton.setText(R.string.un_attention)
            attentionButton.setTextColor(Color.WHITE)
            attentionButton.setBackgroundResource(R.drawable.product_item_attentioned_button_background)
        }else {
            attentionButton.setText(R.string.attentioned)
            attentionButton.setTextColor(ContextCompat.getColor(itemView?.context, R.color.activity_login_weixin_button_text_color))
            attentionButton.setBackgroundResource(R.drawable.product_item_un_attention_button_background)
        }
        val (day , hour, _) = DateUtils.diffTime(Date(System.currentTimeMillis()), Date(product.startDateTime))
        val statusString = context.getString(R.string.my_start_gap_time, day.toString(), hour.toString())
        timeGapView.text = SpannableStringUtils.addColorSpan(context.getString(R.string.my_start_gap_label, statusString), statusString, ContextCompat.getColor(context, R.color.colorPrimary), PixelUtils.sp2px(context, 14f).toInt())
    }

}