package com.zhengdianfang.dazhongbao.views.home

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.zhengdianfang.dazhongbao.R
import com.zhengdianfang.dazhongbao.matchers.DrawableMatchers
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by dfgzheng on 06/08/2017.
 */

@LargeTest
@RunWith(AndroidJUnit4::class)
class MainActivityTest {
    @Rule
    @JvmField var mMainActivityTestRole = ActivityTestRule(MainActivity::class.java)

    @Test
    fun views_display_in_main_activity() {
        onView(withId(R.id.bottomBar)).check(matches(isDisplayed()))
        onView(withId(R.id.bottomBar)).check(matches(hasChildCount(4)))
    }

    @Test
    fun highlight_tab_item_When_switch_bottom_tab_item() {
        onView(DrawableMatchers.withCompoundDrawableForButton(R.drawable.bottom_bar_home_selected_icon)).check(matches(isDisplayed()))
    }

}