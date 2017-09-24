package com.zhengdianfang.dazhongbao.views.user.adapter

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.zhengdianfang.dazhongbao.R
import com.zhengdianfang.dazhongbao.helpers.SpannableStringUtils
import com.zhengdianfang.dazhongbao.helpers.ViewsUtils
import com.zhengdianfang.dazhongbao.models.product.Product

/**
 * Created by dfgzheng on 27/08/2017.
 */

class MyDepositItemAdapter(private val products: MutableList<Product>): RecyclerView.Adapter<MyDepositItemViewHodler>() {

    override fun onBindViewHolder(holder: MyDepositItemViewHodler?, position: Int) {
        holder?.setData(products[position])
    }

    override fun getItemCount(): Int {
        return products.count()
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MyDepositItemViewHodler {
        return MyDepositItemViewHodler(LayoutInflater.from(parent?.context).inflate(R.layout.my_deposit_item_layout, parent, false))
    }

}

class MyDepositItemViewHodler(itemView: View?) : RecyclerView.ViewHolder(itemView) {

    private val sharesNameView by lazy { itemView?.findViewById<TextView>(R.id.sharesNameView)!! }
    private val statusView by lazy { itemView?.findViewById<TextView>(R.id.statusView)!! }
    private val industryNameView by lazy { itemView?.findViewById<TextView>(R.id.industryNameView)!! }
    private val soldCountView by lazy { itemView?.findViewById<TextView>(R.id.soldCountView)!! }
    private val depositPriceView by lazy { itemView?.findViewById<TextView>(R.id.depositPriceView)!! }

    fun setData(product: Product) {
        val context = itemView?.context!!
        sharesNameView.text = ViewsUtils.renderSharesNameAndCode(context, product.sharesName, product.sharesCode)
        statusView.text = ViewsUtils.renderStatusView(context, product , { _, _ ->})
        industryNameView.text = ViewsUtils.renderIndustryView(context, ViewsUtils.renderIndustryView(context, product.industry))
        soldCountView.text = ViewsUtils.renderSharesSoldCount(context, product.soldCount)
        when(product.bond_status){
            1 -> {
                val statusString = context.getString(R.string.no_pay_label)
                val depositPrice = product.bond.toString()
                val showString = context.getString(R.string.my_deposit_price_status_label, depositPrice, statusString)
                var spanableString =  SpannableStringUtils.addColorSpan(showString, depositPrice, ContextCompat.getColor(context, R.color.c_3c3c3c))
                spanableString =  SpannableStringUtils.addColorSpan(spanableString, statusString,  ContextCompat.getColor(context, R.color.c_f43d3d))
                depositPriceView.text = spanableString
            }
            2 -> {
                val statusString = context.getString(R.string.paied_label)
                val depositPrice = product.bond.toString()
                val showString = "${context.getString(R.string.my_deposit_price_status_label)} $depositPrice ($statusString)"
                var spanableString =  SpannableStringUtils.addColorSpan(showString, depositPrice, ContextCompat.getColor(context, R.color.c_3c3c3c))
                spanableString =  SpannableStringUtils.addColorSpan(spanableString, "($statusString)", ContextCompat.getColor(context, R.color.c_f9b416))
                depositPriceView.text = spanableString
            }
        }
    }
}