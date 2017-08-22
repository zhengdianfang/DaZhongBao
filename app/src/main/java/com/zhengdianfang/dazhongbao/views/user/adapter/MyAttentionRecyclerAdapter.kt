package com.zhengdianfang.dazhongbao.views.user.adapter

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.zhengdianfang.dazhongbao.R
import com.zhengdianfang.dazhongbao.helpers.ViewsUtils
import com.zhengdianfang.dazhongbao.models.product.Product
import com.zhengdianfang.dazhongbao.views.product.ProductDetailActivity

/**
 * Created by dfgzheng on 18/08/2017.
 */

class MyAttentionRecyclerAdapter(private val myProducts: MutableList<Product>) : RecyclerView.Adapter<MyAttentionRecyclerViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MyAttentionRecyclerViewHolder {
        return MyAttentionRecyclerViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.my_attention_list_item, parent, false))
    }

    override fun onBindViewHolder(holder: MyAttentionRecyclerViewHolder?, position: Int) {
        holder?.setData(myProducts[position])
        holder?.removeButton?.setOnClickListener {
            notifyItemRemoved(position + 1)
            myProducts.removeAt(position)
        }
    }

    override fun getItemCount(): Int {
        return myProducts.count()
    }

}

class MyAttentionRecyclerViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {

    private val sharesNameView = itemView?.findViewById<TextView>(R.id.sharesNameView)!!
    private val soldCountView = itemView?.findViewById<TextView>(R.id.soldCountView)!!
    private val industryNameView = itemView?.findViewById<TextView>(R.id.industryNameView)!!
    private val nowUnitPriceView = itemView?.findViewById<TextView>(R.id.nowUnitPriceView)!!
    private val statusView = itemView?.findViewById<TextView>(R.id.statusView)!!
    private val payButton = itemView?.findViewById<Button>(R.id.payButton)!!
    val removeButton = itemView?.findViewById<Button>(R.id.removeButton)!!

    fun setData(product: Product) {
        val context = itemView?.context!!
        sharesNameView.text = ViewsUtils.renderSharesNameAndCode(product.sharesName, product.sharesCode)
        soldCountView.text = ViewsUtils.renderSharesSoldCount(context, product.soldCount)
        industryNameView.text = product.industry

        nowUnitPriceView.text = ViewsUtils.renderSharesPrice(context, product.basicUnitPrice, R.string.start_auction_price)
        statusView.text = ViewsUtils.renderStatusView(context, product, {canPay, _ ->
           payButton.visibility = if(canPay) View.VISIBLE else View.GONE
        })
        itemView.setOnClickListener {
            context.startActivity(Intent(context, ProductDetailActivity::class.java).putExtra("productId", product.id))
        }
    }

}