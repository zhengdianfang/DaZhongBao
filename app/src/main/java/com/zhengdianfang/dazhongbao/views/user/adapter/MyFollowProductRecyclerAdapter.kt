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

class MyFollowProductRecyclerAdapter(val followProducts: MutableList<Product>) : RecyclerView.Adapter<MyFollowProductRecyclerViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MyFollowProductRecyclerViewHolder {
        return MyFollowProductRecyclerViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.attetion_item_layout, parent, false))
    }

    override fun onBindViewHolder(holder: MyFollowProductRecyclerViewHolder?, position: Int) {
        holder?.setData(followProducts[position])
    }

    override fun getItemCount(): Int {
        return followProducts.count()
    }

}

class MyFollowProductRecyclerViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {


    fun setData(product: Product) {

    }
}