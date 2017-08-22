package com.zhengdianfang.dazhongbao.views.product.adapter

import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.DrawableCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.zhengdianfang.dazhongbao.R
import com.zhengdianfang.dazhongbao.helpers.DateUtils
import com.zhengdianfang.dazhongbao.models.product.Bid

/**
 * Created by dfgzheng on 13/08/2017.
 */
class ProductBidItemViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {

    private val realNameView by lazy { itemView?.findViewById<TextView>(R.id.realnameView)!! }
    private val priceView by lazy { itemView?.findViewById<TextView>(R.id.priceView)!! }
    private val timeGapView by lazy { itemView?.findViewById<TextView>(R.id.timeGapView)!! }

    fun setData(bid: Bid, position: Int){
        val context = itemView?.context!!
        val drawable = DrawableCompat.wrap(ContextCompat.getDrawable(context, R.drawable.bid_user_realname_active_background))
        realNameView.background = drawable
        realNameView.text = bid.realname.toCharArray().first().toString()
        if (position == 0){
            realNameView.setTextColor(Color.WHITE)
            priceView.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary))
            DrawableCompat.setTint(drawable, ContextCompat.getColor(context, R.color.c_f9b416))
            itemView?.setBackgroundColor(ContextCompat.getColor(context,R.color.product_detail_info_background))
            timeGapView.text = context.getString(R.string.top_price_label)
            timeGapView.setCompoundDrawablesRelativeWithIntrinsicBounds(R.mipmap.the_new_mark_icon, 0 , 0, 0)
        }else {
            realNameView.setTextColor(Color.BLACK)
            priceView.setTextColor(Color.BLACK)
            itemView?.setBackgroundColor(Color.WHITE)
            DrawableCompat.setTint(drawable, ContextCompat.getColor(context, R.color.bottom_bar_divider_color))
            timeGapView.text = DateUtils.formatDateStr2Desc(System.currentTimeMillis(), bid.ctime)
            timeGapView.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0 , 0, 0)
        }

    }
}