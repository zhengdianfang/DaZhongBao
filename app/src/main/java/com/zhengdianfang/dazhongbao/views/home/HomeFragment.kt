package com.zhengdianfang.dazhongbao.views.home


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import com.zhengdianfang.dazhongbao.CApplication
import com.zhengdianfang.dazhongbao.R
import com.zhengdianfang.dazhongbao.models.product.Advert
import com.zhengdianfang.dazhongbao.presenters.AdvertPresenter
import com.zhengdianfang.dazhongbao.views.basic.BaseFragment
import com.zhengdianfang.dazhongbao.views.components.miraclePageView.MiracleViewPager
import com.zhengdianfang.dazhongbao.views.components.miraclePageView.transformer.MiracleScaleTransformer
import com.zhengdianfang.dazhongbao.views.home.adapter.AdvertViewPagerAdapter
import com.zhengdianfang.dazhongbao.views.home.adapter.TabViewPagerAdapter
import com.zhengdianfang.dazhongbao.views.setting.NotifyMessagesActivity


/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : BaseFragment(), AdvertPresenter.IAdvertBannerView, AdvertPresenter.IIndexCountView{

    private val tabs = arrayOf("3,4,5", "0")

    private val tabViewPagerAdapter by lazy { TabViewPagerAdapter(tabs) }
    private val mAdvertViewPager by lazy { view?.findViewById<MiracleViewPager>(R.id.advertViewPager)!! }
    private val mTabViewPager by lazy { view?.findViewById<MiracleViewPager>(R.id.tabViewPager)!! }
    private val tabOneView by lazy { view?.findViewById<TextView>(R.id.tabOne)!! }
    private val tabTwoView by lazy { view?.findViewById<TextView>(R.id.tabTwo)!! }
    private val dealCountView by lazy { view?.findViewById<TextView>(R.id.dealCountView)!! }
    private val advertPresenter by lazy { AdvertPresenter() }
    private val messageButton by lazy { view?.findViewById<ImageButton>(R.id.messageButton)!! }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        advertPresenter.attachView(this)
        setupViewPager()
        setupTabViewPager()
        messageButton.setOnClickListener {
            startActivity(Intent(activity, NotifyMessagesActivity::class.java))
        }
        advertPresenter.fetchAdvertBanner(CApplication.INSTANCE.loginUser?.token!!)
        advertPresenter.fetchIndexCount(CApplication.INSTANCE.loginUser?.token!!)

    }

    private fun setupViewPager() {
        mAdvertViewPager.setMultiScreen(0.75f)
        mAdvertViewPager.setPageTransformer(false, MiracleScaleTransformer())
        mAdvertViewPager.setInfiniteLoop(true)
        mAdvertViewPager.adapter = AdvertViewPagerAdapter(mutableListOf())
        mAdvertViewPager.initIndicator()
        mAdvertViewPager.indicator?.setGravity(Gravity.CENTER_HORIZONTAL or Gravity.BOTTOM)
                ?.setOrientation(MiracleViewPager.Orientation.HORIZONTAL)
                ?.setFocusResId(R.mipmap.advert_viewpager_force_indicator)
                ?.setNormalResId(R.mipmap.advert_viewpager_normal_indicator)
                ?.build()

    }

    private fun setupTabViewPager() {
        mTabViewPager.adapter = tabViewPagerAdapter
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
        advertPresenter.detachView()
    }

    override fun onBackPressed(): Boolean {
        getParentActivity().finish()
        return true
    }

    override fun receiveBanner(advertList: MutableList<Advert>) {
        mAdvertViewPager.adapter = AdvertViewPagerAdapter(advertList)
    }

    override fun receiveIndexCount(dealCount: Int, productCount: Int, messageCount: Int) {
        dealCountView.text = getString(R.string.fragment_home_deal_count_label, dealCount, productCount)
    }
}
