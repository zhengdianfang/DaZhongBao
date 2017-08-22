package com.zhengdianfang.dazhongbao.views.product.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView

/**
 * Created by dfgzheng on 13/08/2017.
 */
class ProductDetailFooterViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
    fun setData(description: String?) {
        (itemView as TextView).text = description
    }

}