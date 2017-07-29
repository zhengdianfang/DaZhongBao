package com.zhengdianfang.dazhongbao

import android.support.test.rule.ActivityTestRule
import com.zhengdianfang.dazhongbao.views.StartupActivity
import org.junit.Test
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.filters.LargeTest
import android.support.test.runner.AndroidJUnit4
import org.junit.runner.RunWith


/**
 * Created by dfgzheng on 28/07/2017.
 */
@RunWith(AndroidJUnit4::class)
@LargeTest
class StartupActivityTest {
    val mStartupActivity = ActivityTestRule(StartupActivity::class.java)

    @Test
    fun clickBtn_shouldStartupActivity() {
        onView(withId(R.id.button)).perform(click())

    }
}