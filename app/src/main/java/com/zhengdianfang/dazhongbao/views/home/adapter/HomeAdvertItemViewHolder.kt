package com.zhengdianfang.dazhongbao.views.home.adapter

import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.View
import com.zhengdianfang.dazhongbao.R
import com.zhengdianfang.dazhongbao.models.product.Advert
import com.zhengdianfang.dazhongbao.views.components.miraclePageView.MiracleViewPager

/**
 * Created by dfgzheng on 05/08/2017.
 */
class HomeAdvertItemViewHolder(itemView: View?, adverts: MutableList<Advert>) : RecyclerView.ViewHolder(itemView) {
    val advertViewPager by lazy { itemView?.findViewById<MiracleViewPager>(R.id.advertViewPager)!! }

    init {

        advertViewPager.adapter = AdvertViewPagerAdapter(adverts)
        advertViewPager.initIndicator()
        advertViewPager.indicator?.setGravity(Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL)
        advertViewPager.indicator?.build()
    }
}