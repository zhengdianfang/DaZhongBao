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
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by dfgzheng on 03/08/2017.
 */
@LargeTest
@RunWith(AndroidJUnit4::class)
class PhoneRegisterFragmentTest{

    @Rule
    @JvmField var mLoginActivityTestRole = ActivityTestRule(LoginActivity::class.java)

    @Before
    fun setUp() {
        onView(withId(R.id.organizationButton)).perform(click())
        SystemClock.sleep(2000)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun validate_views_display() {
        onView(withId(R.id.submitButton)).check(matches(isDisplayed()))
        onView(withId(R.id.registerPhoneEditText)).check(matches(isDisplayed()))
        onView(withId(R.id.smsCodeEditText)).check(matches(isDisplayed()))
        onView(withId(R.id.getSmsCodeButton)).check(matches(isDisplayed()))
        onView(withId(R.id.recommendEditText)).check(matches(isDisplayed()))
    }

    @Test
    fun should_validate_illegal_phoneNumber_When_user_click_get_sms_code_button(){
        onView(withId(R.id.getSmsCodeButton)).perform(click())
        onView(withText(R.string.please_input_phonenumber))
                .inRoot(withDecorView(not(mLoginActivityTestRole.activity.window.decorView))).check(matches(isDisplayed()))
        SystemClock.sleep(2000)
        onView(withId(R.id.registerPhoneEditText)).perform(typeText("185111"), closeSoftKeyboard())
        onView(withId(R.id.getSmsCodeButton)).perform(click())
        onView(withText(R.string.please_input_legal_phonenumber))
                .inRoot(withDecorView(not(mLoginActivityTestRole.activity.window.decorView))).check(matches(isDisplayed()))

    }

    @Test
    fun should_toast_receiver_sms_code_send_When_user_click_get_sms_code_button(){
        onView(withId(R.id.registerPhoneEditText)).perform(typeText("18511177916"), closeSoftKeyboard())
        onView(withId(R.id.getSmsCodeButton)).perform(click())
        SystemClock.sleep(2000)
        onView(withId(R.id.getSmsCodeButton)).check(matches(withText(mLoginActivityTestRole.activity.getString(R.string.button_diable_timer_alert, 60))))
        onView(withText(R.string.toast_sms_code_send_successful))
                .inRoot(withDecorView(not(mLoginActivityTestRole.activity.window.decorView))).check(matches(isDisplayed()))
    }

    @Test
    fun should_validate_input_text_message_When_user_click_register_button() {
        onView(withId(R.id.submitButton)).perform(click())
        onView(withText(R.string.please_input_phonenumber))
                .inRoot(withDecorView(not(mLoginActivityTestRole.activity.window.decorView))).check(matches(isDisplayed()))

        SystemClock.sleep(2000)
        onView(withId(R.id.registerPhoneEditText)).perform(typeText("185111"), closeSoftKeyboard())
        onView(withId(R.id.submitButton)).perform(click())
        onView(withText(R.string.please_input_legal_phonenumber))
                .inRoot(withDecorView(not(mLoginActivityTestRole.activity.window.decorView))).check(matches(isDisplayed()))

        SystemClock.sleep(2000)
        onView(withId(R.id.registerPhoneEditText)).perform(clearText(), typeText("18511177916"), closeSoftKeyboard())
        onView(withId(R.id.submitButton)).perform(click())
        onView(withText(R.string.please_input_sms_code))
                .inRoot(withDecorView(not(mLoginActivityTestRole.activity.window.decorView))).check(matches(isDisplayed()))

    }

    @Test
    fun should_receiver_register_successful_message_When_user_click_register_button(){
        onView(withId(R.id.registerPhoneEditText)).perform(typeText("18511177916"), closeSoftKeyboard())
        onView(withId(R.id.smsCodeEditText)).perform(typeText("185"), closeSoftKeyboard())
        onView(withId(R.id.submitButton)).perform(click())
        SystemClock.sleep(2000)
        onView(withText(R.string.toast_register_successful))
                .inRoot(withDecorView(not(mLoginActivityTestRole.activity.window.decorView))).check(matches(isDisplayed()))
    }
}