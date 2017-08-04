package com.zhengdianfang.dazhongbao.views.login

import android.os.SystemClock
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.action.ViewActions.typeText
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.RootMatchers.withDecorView
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.zhengdianfang.dazhongbao.R
import org.hamcrest.Matchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by dfgzheng on 04/08/2017.
 */
@LargeTest
@RunWith(AndroidJUnit4::class)
class ModifyPasswordFragmentTest {
    @Rule
    @JvmField var mLoginActivityTestRole = ActivityTestRule(LoginActivity::class.java)

    @Before
    fun setUp() {
        mLoginActivityTestRole.activity.supportFragmentManager.beginTransaction()
                .add(android.R.id.content, ModifyPasswordFragment()).commitAllowingStateLoss()
    }

    @Test
    fun should_views_display() {
        onView(withId(R.id.modifyPasswordSubmitButton)).check(matches(isDisplayed()))
        onView(withId(R.id.modifyPasswordEditText)).check(matches(isDisplayed()))
        onView(withId(R.id.modifyPasswordConfirmEditText)).check(matches(isDisplayed()))
    }

    @Test
    fun validate_password_When_user_click_submit_button() {
        onView(withId(R.id.modifyPasswordSubmitButton)).perform(click())
        onView(withText(R.string.please_input_password))
                .inRoot(withDecorView(not(mLoginActivityTestRole.activity.window.decorView))).check(matches(isDisplayed()))
        SystemClock.sleep(2000)

        onView(withId(R.id.modifyPasswordEditText)).perform(typeText("123"))
        onView(withId(R.id.modifyPasswordSubmitButton)).perform(click())
        onView(withText(R.string.please_input_legal_password))
                .inRoot(withDecorView(not(mLoginActivityTestRole.activity.window.decorView))).check(matches(isDisplayed()))
    }

    @Test
    fun validate_double_passwords_equal_When_user_click_submit_button() {
        onView(withId(R.id.modifyPasswordEditText)).perform(typeText("123456"))
        onView(withId(R.id.modifyPasswordConfirmEditText)).perform(typeText("1233336"))
        onView(withId(R.id.modifyPasswordSubmitButton)).perform(click())
        onView(withText(R.string.toast_input_confirm_password_not_equal))
                .inRoot(withDecorView(not(mLoginActivityTestRole.activity.window.decorView))).check(matches(isDisplayed()))
    }

    @Test
    fun request_set_password_api_When_user_click_submit_button() {
        onView(withId(R.id.modifyPasswordEditText)).perform(typeText("123456"))
        onView(withId(R.id.modifyPasswordConfirmEditText)).perform(typeText("123456"))
        onView(withId(R.id.modifyPasswordSubmitButton)).perform(click())
        SystemClock.sleep(2000)
        onView(withText(R.string.toast_set_password_successful))
                .inRoot(withDecorView(not(mLoginActivityTestRole.activity.window.decorView))).check(matches(isDisplayed()))
    }
}