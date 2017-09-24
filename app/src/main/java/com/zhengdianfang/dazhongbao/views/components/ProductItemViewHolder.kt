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
import com.zhengdianfang.dazhongbao.helpers.ViewsUtils
import com.zhengdianfang.dazhongbao.models.product.Product
import com.zhengdianfang.dazhongbao.presenters.FollowProductPresenter
import com.zhengdianfang.dazhongbao.views.product.ProductDetailActivity

/**
 * Created by dfgzheng on 05/08/2017.
 */
class ProductItemViewHolder(itemView: View?, private val followProductPresenter: FollowProductPresenter) : RecyclerView.ViewHolder(itemView) {
    private val stockNameView = itemView?.findViewById<TextView>(R.id.sharesNameView)!!
    val soldCountView = itemView?.findViewById<TextView>(R.id.soldCountView)!!
    private val industryNameView = itemView?.findViewById<TextView>(R.id.industryNameView)!!
    private val nowUnitPriceView = itemView?.findViewById<TextView>(R.id.nowUnitPriceView)!!
    private val attentionButton = itemView?.findViewById<Button>(R.id.attentionButton)!!

    fun setData(product: Product){
        Log.d("ProductItemViewHolder", product.toString())
        val context = itemView?.context!!
        stockNameView.text = ViewsUtils.renderSharesNameAndCode(context, product.sharesName, product.sharesCode)
        val soldCount = product.soldCount / Constants.SOLD_COUNT_BASE_UNIT
        soldCountView.text = ViewsUtils.renderSharesSoldCount(context, soldCount)
        industryNameView.text = ViewsUtils.renderIndustryView(context, product.industry)
        nowUnitPriceView?.text = ViewsUtils.renderSharesPrice(context, product.basicUnitPrice, R.string.product_item_will_pay_price)
        if (product.attention == 0){
            attentionButton.setText(R.string.un_attention)
            attentionButton.setTextColor(Color.WHITE)
            attentionButton.setBackgroundResource(R.drawable.product_item_attentioned_button_background)
        }else {
            attentionButton.setText(R.string.attentioned)
            attentionButton.setTextColor(ContextCompat.getColor(itemView?.context, R.color.activity_login_weixin_button_text_color))
            attentionButton.setBackgroundResource(R.drawable.product_item_un_attention_button_background)
        }

        itemView.setOnClickListener {
            context.startActivity(Intent(itemView?.context, ProductDetailActivity::class.java).putExtra("productId", product.id))
        }

        attentionButton.setOnClickListener {
            val token = CApplication.INSTANCE.loginUser?.token
            if (null != token){
                if (product.attention == 0){
                    followProductPresenter.followProduct(token, product.id)
                }else{
                    followProductPresenter.unfollowProduct(token, product.id)
                }
            }
        }
    }
}
