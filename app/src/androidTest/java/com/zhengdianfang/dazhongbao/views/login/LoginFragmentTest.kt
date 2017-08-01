package com.zhengdianfang.dazhongbao.views.login

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.zhengdianfang.dazhongbao.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by dfgzheng on 31/07/2017.
 */
@LargeTest
@RunWith(AndroidJUnit4::class)
class LoginFragmentTest {

    @Rule
    @JvmField var mLoginActivityTestRole = ActivityTestRule(LoginActivity::class.java)


    @Test
    fun should_views_display_in_activity() {
        onView(withId(R.id.phoneEditText)).check(matches(isDisplayed()))
        onView(withId(R.id.passwordEditText)).check(matches(isDisplayed()))
        onView(withId(R.id.loginButton)).check(matches(isDisplayed()))
        onView(withId(R.id.weixinButton)).check(matches(isDisplayed()))
        onView(withId(R.id.organizationButton)).check(matches(isDisplayed()))
    }
}