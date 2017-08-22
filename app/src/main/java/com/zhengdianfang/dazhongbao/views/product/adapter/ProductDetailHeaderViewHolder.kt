package com.zhengdianfang.dazhongbao.views.product.adapter

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.zhengdianfang.dazhongbao.CApplication
import com.zhengdianfang.dazhongbao.R
import com.zhengdianfang.dazhongbao.models.product.Product
import com.zhengdianfang.dazhongbao.presenters.FollowProductPresenter

/**
 * Created by dfgzheng on 13/08/2017.
 */
class ProductDetailHeaderViewHolder(itemView: View?, private val followProductPresenter: FollowProductPresenter) : RecyclerView.ViewHolder(itemView) {

   private val attentionButton by lazy { itemView?.findViewById<Button>(R.id.attentionButton)!! }
   private val yestodayClosePriceView by lazy { itemView?.findViewById<TextView>(R.id.yestodyClosePriceView)!! }
   private val soldCountView by lazy { itemView?.findViewById<TextView>(R.id.soldCountView)!! }
   private val limitTimeView by lazy { itemView?.findViewById<TextView>(R.id.limitTimeView)!! }

   fun setData(product: Product) {
       if (null != product){
           val context = itemView?.context!!
           yestodayClosePriceView.text = product.yestodayClosePrice.toString()
           soldCountView.text = product.soldCount.toString()
           if (product.limitTime  > 0){
               limitTimeView.setText( R.string.yes_label)
           }else{
               limitTimeView.setText( R.string.no_label)
           }
           attention(context,product.attention == 1, product.id)
       }
   }

   fun attention(context: Context, attention: Boolean, productId: Long) {
       if (attention) {
           attentionButton.setBackgroundResource(R.drawable.product_item_attentioned_button_background)
           attentionButton.setTextColor(ContextCompat.getColor(context, R.color.bottom_bar_tab_text_color))
           attentionButton.setText(R.string.attentioned)
       } else {
           attentionButton.setBackgroundResource(R.drawable.activity_product_detail_button_background)
           attentionButton.setTextColor(ContextCompat.getColor(context, R.color.c_8c6465))
           attentionButton.setText(R.string.un_attention)
           attentionButton.setOnClickListener {
               followProductPresenter.followProduct(CApplication.INSTANCE.loginUser?.token!!, productId)
           }
       }
   }

}