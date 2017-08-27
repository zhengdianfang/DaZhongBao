package com.zhengdianfang.dazhongbao.views.product.adapter

import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.DrawableCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.zhengdianfang.dazhongbao.R
import com.zhengdianfang.dazhongbao.helpers.DateUtils
import com.zhengdianfang.dazhongbao.helpers.PixelUtils
import com.zhengdianfang.dazhongbao.helpers.SpannableStringUtils
import com.zhengdianfang.dazhongbao.models.product.Bid

/**
 * Created by dfgzheng on 13/08/2017.
 */
class ProductBidItemViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {

    private val realNameView by lazy { itemView?.findViewById<TextView>(R.id.realnameView)!! }
    private val priceView by lazy { itemView?.findViewById<TextView>(R.id.priceView)!! }
    private val timeGapView by lazy { itemView?.findViewById<TextView>(R.id.timeGapView)!! }

    fun setData(bid: Bid, position: Int) {
        with(bid) {
            val context = itemView?.context!!
            val drawable = DrawableCompat.wrap(ContextCompat.getDrawable(context, R.drawable.bid_user_realname_active_background))
            realNameView.background = drawable
            realNameView.text = realname?.toCharArray()?.first().toString()
            if (position == 0) {
                realNameView.setTextColor(Color.WHITE)
                priceView.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary))
                priceView.text = SpannableStringUtils.addSizeSpan("￥${price}", "${price}", PixelUtils.sp2px(context, 20f).toInt())
                DrawableCompat.setTint(drawable, ContextCompat.getColor(context, R.color.c_f9b416))
                itemView?.setBackgroundColor(ContextCompat.getColor(context, R.color.product_detail_info_background))
                timeGapView.text = context.getString(R.string.top_price_label)
                timeGapView.setCompoundDrawablesRelativeWithIntrinsicBounds(R.mipmap.the_new_mark_icon, 0, 0, 0)
            } else {
                realNameView.setTextColor(ContextCompat.getColor(context, R.color.bottom_bar_tab_text_color))
                priceView.setTextColor(ContextCompat.getColor(context, R.color.bottom_bar_tab_text_color))
                priceView.text = SpannableStringUtils.addSizeSpan("￥${price}", "${price}", PixelUtils.sp2px(context, 20f).toInt())
                itemView?.setBackgroundColor(Color.WHITE)
                DrawableCompat.setTint(drawable, ContextCompat.getColor(context, R.color.bottom_bar_divider_color))
                timeGapView.text = DateUtils.formatDateStr2Desc(System.currentTimeMillis(), ctime)
                timeGapView.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0)
            }
        }
    }
}

