package com.zhengdianfang.dazhongbao.views.product.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.zhengdianfang.dazhongbao.R
import com.zhengdianfang.dazhongbao.models.product.Product
import kotlin.properties.Delegates

/**
 * Created by dfgzheng on 13/08/2017.
 */
class ProductRecyclerViewAdapter(val product: Product?, val bidPriceList: MutableList<String>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val HEADER_COUNT = 2
    private val PRODUCT_INFO_ITEM = 0
    private val PRODUCT_BID_ITEM = 1
    private val PRODUCT_NOTES_ITEM = 2

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        var viewHolder: RecyclerView.ViewHolder by Delegates.notNull<RecyclerView.ViewHolder>()
        when(viewType) {
            PRODUCT_INFO_ITEM -> {
                viewHolder = ProductDetailHeaderViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.product_detail_header, parent, false))
            }
            PRODUCT_BID_ITEM -> {
                viewHolder = ProductDidItemViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.product_bid_item, parent, false))
            }
            PRODUCT_NOTES_ITEM -> {
                viewHolder = ProductDetailFooterViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.product_notes_footer, parent, false))
            }
        }
        return viewHolder
    }

    override fun getItemCount(): Int {
       return bidPriceList.size + HEADER_COUNT
    }

    override fun getItemViewType(position: Int): Int {
        if (position == 0){
           return PRODUCT_INFO_ITEM
        }else if (position == itemCount - 1){
           return PRODUCT_BID_ITEM
        }
        return PRODUCT_NOTES_ITEM
    }
}