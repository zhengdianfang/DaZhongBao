package com.zhengdianfang.dazhongbao.views.components

import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.zhengdianfang.dazhongbao.R
import com.zhengdianfang.dazhongbao.models.product.Product

/**
 * Created by dfgzheng on 05/08/2017.
 */
class ProductItemViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
    val stockNameView = itemView?.findViewById<TextView>(R.id.stockNameView)
    val stockCodeView = itemView?.findViewById<TextView>(R.id.stockCodeView)
    val soldCountView = itemView?.findViewById<TextView>(R.id.soldCountView)
    val industryNameView = itemView?.findViewById<TextView>(R.id.industryNameView)
    val nowUnitPriceView = itemView?.findViewById<TextView>(R.id.nowUnitPriceView)
    val attentionButton = itemView?.findViewById<Button>(R.id.attentionButton)

    fun setData(product: Product){
        stockCodeView?.text = "[${product.sharesCode}]"
        stockNameView?.text = product.sharesName
        soldCountView?.text =  itemView?.context?.getString(R.string.sold_count_value, product.soldCount)
        industryNameView?.text = product.industry
        nowUnitPriceView?.text = itemView?.context?.getString(R.string.price_unit_value, product.nowUnitPrice.toString())
        if (product.attention == 0){
            attentionButton?.setText(R.string.un_attention)
            attentionButton?.setTextColor(Color.WHITE)
            attentionButton?.setBackgroundResource(R.drawable.product_item_attentioned_button_background)
        }else {
            attentionButton?.setText(R.string.attentioned)
            attentionButton?.setTextColor(ContextCompat.getColor(itemView?.context, R.color.activity_login_weixin_button_text_color))
            attentionButton?.setBackgroundResource(R.drawable.product_item_un_attention_button_background)
        }
    }
}
