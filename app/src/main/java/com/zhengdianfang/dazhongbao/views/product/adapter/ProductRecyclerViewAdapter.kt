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
class ProductRecyclerViewAdapter(private val bidList: MutableList<Bid>, private val productNotesHeaderView: View, private val productNotesFooterView: View) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val HEADER = 0
    private val ITEM = 1
    private val FOOTR = 2

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        if (getItemViewType(position) == ITEM) {
            (holder as ProductBidItemViewHolder).setData(bidList[position - 1], position - 1)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        if(viewType == FOOTR){
           return object : RecyclerView.ViewHolder(productNotesFooterView){}
        }
        if(viewType == HEADER){
            return object : RecyclerView.ViewHolder(productNotesHeaderView){}
        }
        return ProductBidItemViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.product_bid_item, parent, false))
    }

    override fun getItemViewType(position: Int): Int {
        if (position == 0){
           return HEADER
        }
        if (position == itemCount - 1) {
           return FOOTR
        }
        return ITEM
    }

    override fun getItemCount(): Int {
       return bidList.count() + 1
    }

}