package com.zhengdianfang.dazhongbao.IdlingResources

import android.support.test.espresso.IdlingResource
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Created by dfgzheng on 03/08/2017.
 */
class SimpleIdlingResource : IdlingResource{
    private val mIsIdleNow = AtomicBoolean(true)
    private var mCallback: IdlingResource.ResourceCallback? = null

    override fun getName(): String {
        return this.javaClass.name
    }

    override fun isIdleNow(): Boolean {
        return mIsIdleNow.get()
    }

    override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback?) {
       this.mCallback = callback
    }

    fun setIdleState(isIdleNow: Boolean) {
        this.mIsIdleNow.set(isIdleNow)
        if (isIdleNow && null != mCallback){
            mCallback?.onTransitionToIdle()
        }
    }
}