package com.zhengdianfang.dazhongbao.helpers

import android.content.Context
import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.text.SpannableString
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.zhengdianfang.dazhongbao.R
import com.zhengdianfang.dazhongbao.models.product.Bid
import com.zhengdianfang.dazhongbao.models.product.Product

/**
 * Created by dfgzheng on 22/08/2017.
 */
object ViewsUtils {

    fun renderSharesSoldCount(context: Context, count: Long): SpannableString {
        val highlightColor = ContextCompat.getColor(context, R.color.c_f43d3d)
        val soldCount = count.toDouble() / Constants.SOLD_COUNT_BASE_UNIT.toDouble()
        val soldCountString = context.getString(R.string.sold_count_value, soldCount.toString())!!
        return  SpannableStringUtils.addColorSpan(context.getString(R.string.product_item_to_sell, soldCountString)!!,
                soldCountString, highlightColor, PixelUtils.sp2px(context, 16f).toInt())
    }

    fun renderSharesNameAndCode(sharesName: String, sharesCode: String): String {
        return "$sharesName [$sharesCode]"
    }

    fun renderSharesPrice(context: Context, price: Double, labelResId: Int): SpannableString {
        val highlightColor = ContextCompat.getColor(context, R.color.c_f43d3d)
        val priceString = context.getString(R.string.price_unit_value, price.toString())
        return SpannableStringUtils.addColorSpan(context.getString(labelResId, priceString),
                priceString, highlightColor, PixelUtils.sp2px(context, 16f).toInt())

    }

    fun renderSharesPrice(context: Context, price: Long, labelResId: Int): SpannableString {
        val highlightColor = ContextCompat.getColor(context, R.color.c_f43d3d)
        val priceString = context.getString(R.string.price_unit_value, price.toString())
        return SpannableStringUtils.addColorSpan(context.getString(labelResId, priceString),
                priceString, highlightColor, PixelUtils.sp2px(context, 16f).toInt())

    }

    fun renderStatusView(context: Context, product: Product, callback: (canPay: Boolean, dealSuccess: Boolean)-> Unit): SpannableString {
        val highlightColor = ContextCompat.getColor(context, R.color.colorPrimary)
        val textSize = PixelUtils.sp2px(context, 14f)
        var resultSpannableString = SpannableString("")
        when(product.check_status){
            1 -> {
                val statusString = context.getString(R.string.status_verifing_label)
                callback(false, false)
                resultSpannableString = SpannableStringUtils.addColorSpan(context.getString(R.string.status_label, statusString), statusString, highlightColor, textSize.toInt())
            }
            2 -> {
                val statusString = context.getString(R.string.verify_fail_status)
                callback(false, false)
                resultSpannableString = SpannableStringUtils.addColorSpan(context.getString(R.string.status_label, statusString), statusString, highlightColor, textSize.toInt())
            }
            3 -> {
                val statusString = context.getString(R.string.product_status_itention_info)
                callback(false, false)
                resultSpannableString = SpannableStringUtils.addColorSpan(context.getString(R.string.status_label, statusString), statusString, highlightColor, textSize.toInt())
            }
            4 -> {
                resultSpannableString = if(product.bond_status != 2){
                    val statusString = context.getString(R.string.to_pay_the_deposit)
                    callback(true, false)
                    SpannableStringUtils.addColorSpan(context.getString(R.string.status_label, statusString), statusString, highlightColor, textSize.toInt())
                }else{
                    callback(false, false)
                    val (day , hour, _) = DateUtils.diffTime(System.currentTimeMillis(), DateUtils.changeTimeLenght(product.startDateTime))
                    val statusString = context.getString(R.string.my_start_gap_time, day.toString(), hour.toString())
                    SpannableStringUtils.addColorSpan(context.getString(R.string.my_start_gap_label, statusString), statusString, highlightColor, textSize.toInt())
                }
            }
            5 -> {
                val statusString = context.getString(R.string.product_status_auctioning)
                callback(false, false)
                resultSpannableString = SpannableStringUtils.addColorSpan(context.getString(R.string.status_label, statusString), statusString, highlightColor, textSize.toInt())
            }
            6 -> {
                val statusString = context.getString(R.string.ready_beal_status)
                callback(false, false)
                resultSpannableString = SpannableStringUtils.addColorSpan(context.getString(R.string.status_label, statusString), statusString, highlightColor, textSize.toInt())
            }
        }
        return resultSpannableString
    }

    fun renderAttentionView(context: Context, attention: Int, callback: (textResId: Int, color: Int, backgroundResId: Int)->Unit) {
        if (attention == 0){
            callback(R.string.un_attention, Color.WHITE, R.drawable.product_item_attentioned_button_background)
        }else {
            callback(R.string.attentioned, ContextCompat.getColor(context, R.color.activity_login_weixin_button_text_color), R.drawable.product_item_un_attention_button_background)
        }

    }

    fun renderBidListView(context: Context, product: Product?, removeOnClick: (bid: Bid)->Unit): MutableList<View> {
        val itemViews = mutableListOf<View>()
        product?.mybids?.forEachIndexed { index, bid ->
            val itemView = LayoutInflater.from(context).inflate(R.layout.cancel_bond_price_item_layout, null, false)
            itemView.findViewById<TextView>(R.id.dealUnitPriceView).text = bid.price.toString() + context.getString(R.string.fragment_push_price_unit)
            itemView.findViewById<TextView>(R.id.dealCountView).text = context.getString(R.string.sold_count_value,
                    (bid.count/ Constants.SOLD_COUNT_BASE_UNIT).toString()) + context.getString(R.string.stock_unit)
            itemView.findViewById<TextView>(R.id.dealTotlaPriceView).text =
                    context.getString(R.string.total_price_label, context.getString(R.string.sold_count_value, (bid.count * bid.price / Constants.SOLD_COUNT_BASE_UNIT).toInt().toString()))
            val removeButton = itemView.findViewById<Button>(R.id.removeButton)
            when(bid.status){
                1 -> {
                    removeButton.setBackgroundResource(R.drawable.cancel_bid_button_background)
                    removeButton.setTextColor(ContextCompat.getColor(context, R.color.c_f9b416))
                    removeButton.setText(R.string.cancel_bond_price)
                    removeButton.setOnClickListener {
                        removeOnClick(bid)
                    }
                }
                2 -> {
                    removeButton.setBackgroundResource(R.drawable.md_transparent)
                    removeButton.setTextColor(ContextCompat.getColor(context, R.color.bottom_bar_tab_text_color))
                    removeButton.setText(R.string.auction_fail_label)
                }
                3 -> {
                    removeButton.setBackgroundResource(R.drawable.md_transparent)
                    removeButton.setTextColor(ContextCompat.getColor(context, R.color.bottom_bar_tab_text_color))
                    removeButton.setText(R.string.auction_success_label)
                }
            }

            itemViews.add(itemView)
        }
        return itemViews
    }
}
