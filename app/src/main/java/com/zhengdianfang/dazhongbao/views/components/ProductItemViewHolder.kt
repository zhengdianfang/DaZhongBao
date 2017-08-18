package com.zhengdianfang.dazhongbao.views.components

import android.content.Intent
import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.zhengdianfang.dazhongbao.CApplication
import com.zhengdianfang.dazhongbao.R
import com.zhengdianfang.dazhongbao.helpers.Constants
import com.zhengdianfang.dazhongbao.helpers.SpannableStringUtils
import com.zhengdianfang.dazhongbao.models.product.Product
import com.zhengdianfang.dazhongbao.presenters.FollowProductPresenter
import com.zhengdianfang.dazhongbao.views.components.refreshLayout.utils.PixelUtils
import com.zhengdianfang.dazhongbao.views.product.ProductDetailActivity

/**
 * Created by dfgzheng on 05/08/2017.
 */
class ProductItemViewHolder(itemView: View?, val followProductPresenter: FollowProductPresenter) : RecyclerView.ViewHolder(itemView) {
    val stockNameView = itemView?.findViewById<TextView>(R.id.sharesNameView)
    val soldCountView = itemView?.findViewById<TextView>(R.id.soldCountView)
    val industryNameView = itemView?.findViewById<TextView>(R.id.industryNameView)
    val nowUnitPriceView = itemView?.findViewById<TextView>(R.id.nowUnitPriceView)
    val attentionButton = itemView?.findViewById<Button>(R.id.attentionButton)

    fun setData(product: Product){
        Log.d("ProductItemViewHolder", product.toString())
        val context = itemView?.context!!
        stockNameView?.text = "${product.sharesName} [${product.sharesCode}]"
        val highlightColor = ContextCompat.getColor(context, R.color.c_f43d3d)

        val soldCount = product.soldCount.toDouble() / Constants.SOLD_COUNT_BASE_UNIT.toDouble()
        val soldCountString = context.getString(R.string.sold_count_value, soldCount.toString())!!
        val soldCountSpannedString = SpannableStringUtils.addColorSpan(context.getString(R.string.product_item_to_sell,soldCountString)!!,
                soldCountString, highlightColor, PixelUtils.sp2px(context, 16f).toInt())
        soldCountView?.text = soldCountSpannedString
        industryNameView?.text = product.industry

        val wantPriceString = context.getString(R.string.price_unit_value, product.nowUnitPrice.toString())!!
        val wantPriceSpannedString = SpannableStringUtils.addColorSpan(context.getString(R.string.product_item_will_pay_price, wantPriceString)!!,
                wantPriceString, highlightColor, PixelUtils.sp2px(context, 14f).toInt())
        nowUnitPriceView?.text = wantPriceSpannedString
        if (product.attention == 0){
            attentionButton?.setText(R.string.un_attention)
            attentionButton?.setTextColor(Color.WHITE)
            attentionButton?.setBackgroundResource(R.drawable.product_item_attentioned_button_background)
        }else {
            attentionButton?.setText(R.string.attentioned)
            attentionButton?.setTextColor(ContextCompat.getColor(itemView?.context, R.color.activity_login_weixin_button_text_color))
            attentionButton?.setBackgroundResource(R.drawable.product_item_un_attention_button_background)
        }

        itemView.setOnClickListener {
            context.startActivity(Intent(itemView?.context, ProductDetailActivity::class.java).putExtra("productId", product.id))
        }

        attentionButton?.setOnClickListener {
            val token = CApplication.INSTANCE.loginUser?.token
            if (null != token){
                followProductPresenter.followProduct(token, product.id)
            }
        }
    }
}
