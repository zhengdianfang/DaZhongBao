package com.zhengdianfang.dazhongbao.views.product.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zhengdianfang.dazhongbao.R
import com.zhengdianfang.dazhongbao.models.product.Bid

/**
 * Created by dfgzheng on 13/08/2017.
 */
class ProductRecyclerViewAdapter(private val bidList: MutableList<Bid>, private val productNotesFooterView: View) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        if (getItemViewType(position) == 0) {
            (holder as ProductBidItemViewHolder).setData(bidList[position], position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        if(viewType == 1){
           return object : RecyclerView.ViewHolder(productNotesFooterView){}
        }
        return ProductBidItemViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.product_bid_item, parent, false))
    }

    override fun getItemViewType(position: Int): Int {
        if (position == itemCount - 1) {
           return 1
        }
        return 0
    }

    override fun getItemCount(): Int {
       return bidList.count() + 1
    }

}