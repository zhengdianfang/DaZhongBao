package com.zhengdianfang.dazhongbao.views.product.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.zhengdianfang.dazhongbao.R
import com.zhengdianfang.dazhongbao.models.product.Product

/**
 * Created by dfgzheng on 13/08/2017.
 */
class ProductDetailHeaderViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {

   private val attentionButton by lazy { itemView?.findViewById<Button>(R.id.attentionButton)!! }
   private val yestodayClosePriceView by lazy { itemView?.findViewById<TextView>(R.id.yestodyClosePriceView)!! }
   private val soldCountView by lazy { itemView?.findViewById<TextView>(R.id.soldCountView)!! }
   private val limitTimeView by lazy { itemView?.findViewById<TextView>(R.id.limitTimeView)!! }

   fun setData(product: Product?) {
       yestodayClosePriceView.text = product?.yestodayClosePrice
       soldCountView.text = product?.soldCount.toString()
       if (product?.limitTime!!  > 0){
           limitTimeView.setText( R.string.yes_label)
       }else{
           limitTimeView.setText( R.string.no_label)
       }
       attentionButton.visibility = if (product?.attention == 0) View.GONE else View.VISIBLE

   }

}