package com.zhengdianfang.dazhongbao.IdlingResources

import android.support.test.espresso.IdlingResource

/**
 * Created by dfgzheng on 03/08/2017.
 */
class ElapsedTimeIdlingResource(val waitingTime: Long): IdlingResource{
    private var startTime: Long  = System.currentTimeMillis()
    private var resourceCallback: IdlingResource.ResourceCallback? = null

    override fun getName(): String {
        return ElapsedTimeIdlingResource::class.java.name + ":" + this.waitingTime
    }

    fun reset(){
        this.startTime = System.currentTimeMillis()
    }

    override fun isIdleNow(): Boolean {
        val elapsed = System.currentTimeMillis() - startTime
        val idle = elapsed >= this.waitingTime
        if (idle) {
            this.resourceCallback?.onTransitionToIdle()
        }
        return idle
    }

    override fun registerIdleTransitionCallback(resourceCallback: IdlingResource.ResourceCallback?) {
        this.resourceCallback = resourceCallback
    }
}