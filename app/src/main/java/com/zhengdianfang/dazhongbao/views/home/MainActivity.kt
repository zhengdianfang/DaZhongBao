package com.zhengdianfang.dazhongbao.views.home

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.view.View
import com.zhengdianfang.dazhongbao.CApplication
import com.zhengdianfang.dazhongbao.R
import com.zhengdianfang.dazhongbao.helpers.AppUtils
import com.zhengdianfang.dazhongbao.presenters.BasePresenter
import com.zhengdianfang.dazhongbao.presenters.UserPresenter
import com.zhengdianfang.dazhongbao.views.basic.BaseActivity
import com.zhengdianfang.dazhongbao.views.components.BottomBar

class MainActivity : BaseActivity(), BasePresenter.ICheckUserIntegrityView {

    private val bottomBar by lazy { findViewById<BottomBar>(R.id.bottomBar) }
    private val viewPage by lazy { findViewById<ViewPager>(R.id.homeViewPager) }
    private val fragments = arrayOf(HomeFragment(), AuctionFragment(), PushFragment(), PersonalFragment())
    private val userPresenter = UserPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userPresenter.attachView(this)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setStatusBarTheme(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR, ContextCompat.getColor(this.applicationContext, R.color.colorPrimary))
        }
        setContentView(R.layout.activity_main)

        bottomBar.changeTabListener =  { tabIndex ->
            viewPage.currentItem = tabIndex
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (tabIndex == 0){
                    setStatusBarTheme(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR, ContextCompat.getColor(this.applicationContext, R.color.colorPrimary))
                }else{
                    setStatusBarTheme(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR, Color.WHITE)
                }
            }
        }

        viewPage.offscreenPageLimit = fragments.size
        viewPage.adapter = MainFragmentAdapter(supportFragmentManager)
        userPresenter.updateUMengToken(CApplication.INSTANCE.loginUser?.token!!, CApplication.INSTANCE.umengToken)
    }

    override fun onDestroy() {
        super.onDestroy()
        userPresenter.detachView()
    }

    fun resetCurrentTab() {
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

    override fun notIntegrity(type: Int) {
        AppUtils.interityUserInfo(this, type)
    }
}
