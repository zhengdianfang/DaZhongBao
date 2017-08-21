package com.zhengdianfang.dazhongbao.views.user.adapter

import android.content.Context
import android.content.Intent
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.zhengdianfang.dazhongbao.R
import com.zhengdianfang.dazhongbao.helpers.Constants
import com.zhengdianfang.dazhongbao.helpers.SpannableStringUtils
import com.zhengdianfang.dazhongbao.models.product.Product
import com.zhengdianfang.dazhongbao.views.components.refreshLayout.utils.PixelUtils
import com.zhengdianfang.dazhongbao.views.product.ProductDetailActivity

/**
 * Created by dfgzheng on 18/08/2017.
 */

class MyProductRecyclerAdapter(val myProducts: MutableList<Product>) : RecyclerView.Adapter<MyProductRecyclerViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MyProductRecyclerViewHolder {
        return MyProductRecyclerViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.my_product_list_item, parent, false))
    }

    override fun onBindViewHolder(holder: MyProductRecyclerViewHolder?, position: Int) {
        holder?.setData(myProducts[position])
    }

    override fun getItemCount(): Int {
        return myProducts.count()
    }

}

class MyProductRecyclerViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {

    private val sharesNameView = itemView?.findViewById<TextView>(R.id.sharesNameView)!!
    private val soldCountView = itemView?.findViewById<TextView>(R.id.soldCountView)!!
    private val industryNameView = itemView?.findViewById<TextView>(R.id.industryNameView)!!
    private val nowUnitPriceView = itemView?.findViewById<TextView>(R.id.nowUnitPriceView)!!
    private val statusView = itemView?.findViewById<TextView>(R.id.statusView)!!
    private val payButton = itemView?.findViewById<Button>(R.id.payButton)!!

    fun setData(product: Product) {
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
        nowUnitPriceView.text = wantPriceSpannedString
        renderStatusView(context, product)
        itemView.setOnClickListener {
            context.startActivity(Intent(context, ProductDetailActivity::class.java).putExtra("productId", product.id))
        }
    }

    private fun renderStatusView(context: Context, product: Product) {
        val highlightColor = ContextCompat.getColor(context, R.color.colorPrimary)
        val textSize = PixelUtils.sp2px(context, 14f)
        payButton.visibility = View.GONE
        when(product.check_status){
            1 -> {
                val statusString = context.getString(R.string.status_verifing_label)
                statusView.text = SpannableStringUtils.addColorSpan(context.getString(R.string.status_label, statusString), statusString, highlightColor, textSize.toInt())
            }
            2 -> {
                val statusString = context.getString(R.string.verify_fail_status)
                statusView.text = SpannableStringUtils.addColorSpan(context.getString(R.string.status_label, statusString), statusString, highlightColor, textSize.toInt())
            }
            3 -> {
                val statusString = context.getString(R.string.product_status_itention_info)
                statusView.text = SpannableStringUtils.addColorSpan(context.getString(R.string.status_label, statusString), statusString, highlightColor, textSize.toInt())
            }
            4 -> {
                if(product.bond_status != 2){
                    val statusString = context.getString(R.string.product_status_margin)
                    statusView.text = SpannableStringUtils.addColorSpan(context.getString(R.string.status_label, statusString), statusString, highlightColor, textSize.toInt())
                    payButton.visibility = View.VISIBLE
                }else{
                    val statusString = context.getString(R.string.product_status_waiting_start)
                    statusView.text = SpannableStringUtils.addColorSpan(context.getString(R.string.status_label, statusString), statusString, highlightColor, textSize.toInt())
                }
            }
            5 -> {
                val statusString = context.getString(R.string.product_status_auctioning)
                statusView.text = SpannableStringUtils.addColorSpan(context.getString(R.string.status_label, statusString), statusString, highlightColor, textSize.toInt())
            }
            6 -> {
                val statusString = context.getString(R.string.ready_beal_status)
                statusView.text = SpannableStringUtils.addColorSpan(context.getString(R.string.status_label, statusString), statusString, highlightColor, textSize.toInt())
            }


        }
    }
}