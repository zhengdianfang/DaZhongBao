package com.zhengdianfang.dazhongbao.views.components

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.zhengdianfang.dazhongbao.R
import com.zhengdianfang.dazhongbao.models.product.Product

/**
 * Created by dfgzheng on 05/08/2017.
 */
class RecyclerViewAdapter(val products: MutableList<Product>) : RecyclerView.Adapter<ProductItemViewHolder>() {

    override fun onBindViewHolder(holder: ProductItemViewHolder?, position: Int) {
        holder?.setData(products[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ProductItemViewHolder {
        val viewHolder = ProductItemViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.fragment_home_product_item_layout, parent, false))
        return viewHolder
    }

    override fun getItemCount(): Int {
       return products.size
    }
}