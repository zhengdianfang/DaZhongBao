package com.zhengdianfang.dazhongbao.views.home.adapter

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.zhengdianfang.dazhongbao.views.home.HomeProductListFragment

/**
 * Created by dfgzheng on 05/08/2017.
 */
class TabViewPagerAdapter(private val context: Context, val tabs: Array<String>, fm: FragmentManager?) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        val bundle = Bundle()
        bundle.putString("status", tabs[position])
        return Fragment.instantiate(context, HomeProductListFragment::class.java.name, bundle)
    }

    override fun getCount(): Int {
        return tabs.size
    }
}