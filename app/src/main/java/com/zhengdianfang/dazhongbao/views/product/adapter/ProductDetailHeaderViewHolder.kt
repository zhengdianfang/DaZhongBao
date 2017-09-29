package com.zhengdianfang.dazhongbao.views.product.adapter

import android.content.Context
import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.zhengdianfang.dazhongbao.R
import com.zhengdianfang.dazhongbao.helpers.DateUtils
import com.zhengdianfang.dazhongbao.helpers.SpannableStringUtils
import com.zhengdianfang.dazhongbao.models.product.Product
import java.text.DecimalFormat

/**
 * Created by dfgzheng on 13/08/2017.
 */
class ProductDetailHeaderViewHolder(itemView: View?, private val intentionOnClick: (product: Long)->Unit) : RecyclerView.ViewHolder(itemView) {

    private val attentionButton by lazy { itemView?.findViewById<Button>(R.id.attentionButton)!! }
    private val yestodayClosePriceView by lazy { itemView?.findViewById<TextView>(R.id.yestodyClosePriceView)!! }
    private val soldCountView by lazy { itemView?.findViewById<TextView>(R.id.soldCountView)!! }
    private val limitTimeView by lazy { itemView?.findViewById<TextView>(R.id.limitTimeView)!! }
    private val headerStatusTipView by lazy { itemView?.findViewById<TextView>(R.id.headerStatusTipView)!! }
    private val bidCountView by lazy { itemView?.findViewById<TextView>(R.id.bidCountView)!! }

    fun setData(product: Product) {
        if (null != product){
            val context = itemView?.context!!
            yestodayClosePriceView.text = DecimalFormat("#.00").format(product.yestodayClosePrice)
            soldCountView.text = DecimalFormat("#,###").format(product.soldCount)
            if (product.limitTime  > 0){
                limitTimeView.setText( R.string.yes_label)
            }else{
                limitTimeView.setText( R.string.no_label)
            }
            attention(context,product.attention == 1, product.id)
            when(product.check_status){
                1, 2, 3 ,4 -> {
                    headerStatusTipView.text = context.getString(R.string.no_bid_wait_for_auction_start)
                    bidCountView.text = ""
                    (headerStatusTipView.parent as View).setBackgroundColor(ContextCompat.getColor(context, R.color.c_fafafa))
                }
                0, 5, 6 -> {
                    headerStatusTipView.text = context.getString(R.string.new_bid_label, context.getString(R.string.finish_auction_time, DateUtils.formatTime(product.endDateTime)))
                    val highlightColor = ContextCompat.getColor(context, R.color.colorPrimary)
                    bidCountView.text = SpannableStringUtils.addColorSpan(context.getString(R.string.bid_count_label1, product.bidcount), product.bidcount.toString(), highlightColor)
                    (headerStatusTipView.parent as View).setBackgroundColor(Color.TRANSPARENT)
                }
            }
        }
    }

    fun attention(context: Context, attention: Boolean, productId: Long) {
        if (attention) {
            attentionButton.setBackgroundResource(R.drawable.product_item_attentioned_button_background)
            attentionButton.setTextColor(Color.WHITE)
            attentionButton.setText(R.string.attentioned)
        } else {
            attentionButton.setBackgroundResource(R.drawable.activity_product_detail_button_background)
            attentionButton.setTextColor(ContextCompat.getColor(context, R.color.c_8c6465))
            attentionButton.setText(R.string.un_attention)
        }
        attentionButton.setOnClickListener {
            intentionOnClick(productId)
        }
    }

}