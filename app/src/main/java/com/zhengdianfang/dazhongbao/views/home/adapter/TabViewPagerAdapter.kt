package com.zhengdianfang.dazhongbao.views.home.adapter

import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup
import com.zhengdianfang.dazhongbao.views.components.ProductRecyclerView

/**
 * Created by dfgzheng on 05/08/2017.
 */
class TabViewPagerAdapter(val tabs: IntArray) : PagerAdapter() {
    override fun isViewFromObject(view: View?, `object`: Any?): Boolean {
        return view == `object`
    }

    override fun getCount(): Int {
        return tabs.size
    }

    override fun instantiateItem(container: ViewGroup?, position: Int): Any {
        val recyclerView = ProductRecyclerView(container?.context, tabs[position])
        container?.addView(recyclerView)
        return recyclerView
    }

    override fun destroyItem(container: ViewGroup?, position: Int, `object`: Any?) {
        container?.removeView(`object` as View)
    }
}