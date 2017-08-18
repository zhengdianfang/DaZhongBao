package com.zhengdianfang.dazhongbao.views.user.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zhengdianfang.dazhongbao.R
import com.zhengdianfang.dazhongbao.models.product.Product

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


    fun setData(product: Product) {

    }
}