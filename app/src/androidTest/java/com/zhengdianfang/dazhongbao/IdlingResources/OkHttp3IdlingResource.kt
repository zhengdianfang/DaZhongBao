package com.zhengdianfang.dazhongbao.IdlingResources

import android.support.annotation.CheckResult
import android.support.test.espresso.IdlingResource

import okhttp3.Dispatcher
import okhttp3.OkHttpClient

/**
 * Created by dfgzheng on 03/08/2017.
 */

class OkHttp3IdlingResource private constructor(private val name: String, private val dispatcher: Dispatcher) : IdlingResource {
    @Volatile internal var callback: IdlingResource.ResourceCallback? = null

    init {
        dispatcher.setIdleCallback {
            val callback = this@OkHttp3IdlingResource.callback
            callback?.onTransitionToIdle()
        }
    }

    override fun getName(): String {
        return name
    }

    override fun isIdleNow(): Boolean {
        return dispatcher.runningCallsCount() == 0
    }

    override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback) {
        this.callback = callback
    }

    companion object {
        /**
         * Create a new [IdlingResource] from `client` as `name`. You must register
         * this instance using `Espresso.registerIdlingResources`.
         */
        @CheckResult
                // Extra guards as a library.
        fun create(name: String, client: OkHttpClient): OkHttp3IdlingResource {
            if (name == null) throw NullPointerException("name == null")
            if (client == null) throw NullPointerException("client == null")
            return OkHttp3IdlingResource(name, client.dispatcher())
        }
    }
}
