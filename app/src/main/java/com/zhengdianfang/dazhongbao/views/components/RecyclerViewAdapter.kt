package com.zhengdianfang.dazhongbao.views.components

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.zhengdianfang.dazhongbao.CApplication
import com.zhengdianfang.dazhongbao.R
import com.zhengdianfang.dazhongbao.models.product.Product

/**
 * Created by dfgzheng on 05/08/2017.
 */
class RecyclerViewAdapter(private val products: MutableList<Product>, val followAction: (id: Long, follow:Boolean)->Unit) : RecyclerView.Adapter<ProductItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ProductItemViewHolder {
        return ProductItemViewHolder(
                LayoutInflater.from(parent?.context).inflate(R.layout.fragment_home_product_item_layout, parent, false))
    }

    override fun getItemCount(): Int {
       return products.size
    }
    override fun onBindViewHolder(holder: ProductItemViewHolder?, position: Int) {
        holder?.setData(products[position])
        holder?.attentionButton?.setOnClickListener {
            val token = CApplication.INSTANCE.loginUser?.token
            if (null != token){
                val product = products[position]
                followAction(product.id, product.attention == 0)
            }
        }
    }
}