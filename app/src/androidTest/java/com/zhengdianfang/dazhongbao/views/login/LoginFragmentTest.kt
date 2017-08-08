package com.zhengdianfang.dazhongbao.views.login

import android.os.SystemClock
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.*
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.RootMatchers.withDecorView
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.zhengdianfang.dazhongbao.R
import org.hamcrest.Matchers.not
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

    @Test
    fun should_legal_phoneNumber_toast_error_When_user_click_login_button() {
        onView(withId(R.id.loginButton)).perform(click())
        onView(withText(R.string.please_input_phonenumber))
                .inRoot(withDecorView(not(mLoginActivityTestRole.activity.window.decorView))).check(matches(isDisplayed()))
        SystemClock.sleep(3000)
        onView(withId(R.id.phoneEditText)).perform(typeText("185111"), closeSoftKeyboard())
        onView(withId(R.id.loginButton)).perform(click())
        onView(withText(R.string.please_input_legal_phonenumber))
                .inRoot(withDecorView(not(mLoginActivityTestRole.activity.window.decorView))).check(matches(isDisplayed()))

    }

    @Test
    fun should_illegal_password_toast_error_When_user_click_login_button() {
        onView(withId(R.id.phoneEditText)).perform(typeText("18511177916"), closeSoftKeyboard())
        onView(withId(R.id.loginButton)).perform(click())
        onView(withText(R.string.please_input_password))
                .inRoot(withDecorView(not(mLoginActivityTestRole.activity.window.decorView))).check(matches(isDisplayed()))
        SystemClock.sleep(3000)
        onView(withId(R.id.passwordEditText)).perform(typeText("1234"), closeSoftKeyboard())
        onView(withId(R.id.loginButton)).perform(click())
        onView(withText(R.string.please_input_legal_password))
                .inRoot(withDecorView(not(mLoginActivityTestRole.activity.window.decorView))).check(matches(isDisplayed()))
    }

    @Test
    fun should_login_success_toast_When_user_click_login_button() {
        onView(withId(R.id.phoneEditText)).perform(typeText("18511177916"), closeSoftKeyboard())
        onView(withId(R.id.passwordEditText)).perform(typeText("1234567"), closeSoftKeyboard())
        onView(withId(R.id.loginButton)).perform(click())
        SystemClock.sleep(2000)
        onView(withText(R.string.toast_login_success))
                .inRoot(withDecorView(not(mLoginActivityTestRole.activity.window.decorView))).check(matches(isDisplayed()))
    }

    @Test
    fun should_goto_phone_register_page_When_user_click_organization_button() {
        onView(withId(R.id.organizationButton)).perform(click())
        onView(withId(R.id.submitButton)).check(matches(isDisplayed()))
    }
}