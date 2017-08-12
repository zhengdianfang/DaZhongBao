package com.zhengdianfang.dazhongbao.views.home


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.zhengdianfang.dazhongbao.R
import com.zhengdianfang.dazhongbao.models.product.Advert
import com.zhengdianfang.dazhongbao.views.basic.BaseFragment
import com.zhengdianfang.dazhongbao.views.components.miraclePageView.MiracleViewPager
import com.zhengdianfang.dazhongbao.views.components.miraclePageView.transformer.MiracleScaleTransformer
import com.zhengdianfang.dazhongbao.views.home.adapter.AdvertViewPagerAdapter
import com.zhengdianfang.dazhongbao.views.home.adapter.TabViewPagerAdapter


/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : BaseFragment(){

    private val tabs = intArrayOf(-1, -1)

    private val adverts = mutableListOf<Advert>()
    private val advertViewPagerAdapter by lazy { AdvertViewPagerAdapter(adverts) }
    private val tabViewPagerAdapter by lazy { TabViewPagerAdapter(tabs) }
    private val mAdvertViewPager by lazy { view?.findViewById<MiracleViewPager>(R.id.advertViewPager)!! }
    private val mTabViewPager by lazy { view?.findViewById<MiracleViewPager>(R.id.tabViewPager)!! }
    private val tabOneView by lazy { view?.findViewById<TextView>(R.id.tabOne)!! }
    private val tabTwoView by lazy { view?.findViewById<TextView>(R.id.tabTwo)!! }
    private val dealCountView by lazy { view?.findViewById<TextView>(R.id.dealCountView)!! }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mAdvertViewPager.setMultiScreen(0.75f)
        mAdvertViewPager.setPageTransformer(false, MiracleScaleTransformer())
        mAdvertViewPager.setInfiniteLoop(true)
//        mAdvertViewPager.setAutoScroll(2000)
        mAdvertViewPager.adapter = advertViewPagerAdapter
        mTabViewPager.adapter = tabViewPagerAdapter

        mAdvertViewPager.initIndicator()
        mAdvertViewPager.indicator?.setGravity(Gravity.CENTER_HORIZONTAL or Gravity.BOTTOM)
                ?.setOrientation(MiracleViewPager.Orientation.HORIZONTAL)
                ?.setFocusResId(R.mipmap.advert_viewpager_force_indicator)
                ?.setNormalResId(R.mipmap.advert_viewpager_normal_indicator)
                ?.build()

        tabOneView.setOnClickListener {
           mTabViewPager.currentItem = 0
        }
        tabTwoView.setOnClickListener {
            mTabViewPager.currentItem = 1
        }

        mTabViewPager.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
               switchTab(position)
            }

        })
        dealCountView.text = getString(R.string.fragment_home_deal_count_label, 0, 0)

    }

    private fun switchTab(positon: Int) {
       if (positon == 0){
           tabOneView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.mipmap.fragment_home_tab_force_indicator)
           tabTwoView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.mipmap.fragment_home_tab_normal_indicator)
       }else if (positon == 1){
           tabOneView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.mipmap.advert_viewpager_normal_indicator)
           tabTwoView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.mipmap.fragment_home_tab_force_indicator)
       }
    }


    override fun onDestroyView() {
        super.onDestroyView()
    }

}
