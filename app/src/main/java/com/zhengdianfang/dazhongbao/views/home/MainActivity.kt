package com.zhengdianfang.dazhongbao.views.home

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import com.zhengdianfang.dazhongbao.CApplication
import com.zhengdianfang.dazhongbao.R
import com.zhengdianfang.dazhongbao.views.basic.BaseActivity
import com.zhengdianfang.dazhongbao.views.components.BottomBar
import com.zhengdianfang.dazhongbao.views.login.LoginActivity

class MainActivity : BaseActivity(), ViewPager.OnPageChangeListener  {

    private val bottomBar by lazy { findViewById<BottomBar>(R.id.bottomBar) }
    private val viewPage by lazy { findViewById<ViewPager>(R.id.homeViewPager) }
    private val fragments = arrayOf(HomeFragment(), AuctionFragment(), PushFragment(), PersonalFragment())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomBar.changeTabListener =  { tabIndex ->
            if (isLogin(tabIndex)) {
                viewPage.currentItem = tabIndex
            }else {
                startActivity(Intent(MainActivity@this, LoginActivity::class.java))
                resetCurrentTab()
            }
        }

        viewPage.offscreenPageLimit = fragments.size
        viewPage.adapter = MainFragmentAdapter(supportFragmentManager)
        viewPage.addOnPageChangeListener(this)
    }
    override fun onPageScrollStateChanged(state: Int) {
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }

    private  fun isLogin(index: Int): Boolean {
       return index <= 1 || CApplication.INSTANCE.isLogin()
    }

    override fun onPageSelected(position: Int) {
        if (isLogin(position)) {
            bottomBar.setCurrentTab(position)
        }else {
            startActivity(Intent(MainActivity@this, LoginActivity::class.java))
            resetCurrentTab()
        }
    }

    private fun resetCurrentTab() {
        viewPage.currentItem = 0
        bottomBar.setCurrentTab(0)
    }

    open inner class MainFragmentAdapter(fm: FragmentManager?) : FragmentStatePagerAdapter(fm) {
        override fun getItem(position: Int): Fragment {
            return fragments[position]
        }

        override fun getCount(): Int {
            return fragments.size
        }

    }
}
