package com.zhengdianfang.dazhongbao.views.home.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.zhengdianfang.dazhongbao.R
import com.zhengdianfang.dazhongbao.models.product.Advert
import com.zhengdianfang.dazhongbao.models.product.Product
import kotlin.properties.Delegates

/**
 * Created by dfgzheng on 05/08/2017.
 */
class HomeRecyclerViewAdapter(val products: MutableList<Product>?, val adverts: MutableList<Advert>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val HEADER_COUNT = 1
    private val ADVERT_HEADER_TYPE = 0
    private val PRODUCT_ITEM_TYPE = 1

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        var viewHolder: RecyclerView.ViewHolder by Delegates.notNull<RecyclerView.ViewHolder>()
        if (viewType == ADVERT_HEADER_TYPE) {
            viewHolder = HomeAdvertItemViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.fragment_home_advert_header_layout, parent, false), adverts)
        }else {
            viewHolder = HomeProductItemViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.fragment_home_product_item_layout, parent, false))
        }
        return viewHolder
    }

    override fun getItemCount(): Int {
       return (products?.size ?: 0) + 1
    }

    override fun getItemViewType(position: Int): Int {
        if(position < HEADER_COUNT){
            return ADVERT_HEADER_TYPE
        }
        return PRODUCT_ITEM_TYPE
    }

}