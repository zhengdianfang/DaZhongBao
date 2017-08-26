/*
 *
 *  MIT License
 *
 *  Copyright (c) 2017 Alibaba Group
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 *
 */

package com.zhengdianfang.dazhongbao.views.components.miraclePageView

import android.os.Handler
import android.os.Message
import android.util.SparseIntArray


/**
 * Created by mikeafc on 15/11/25.
 */
internal class TimerHandler(private var miracleViewPager: MiracleViewPager, var listener: TimerHandlerListener?, private var interval: Long) : Handler() {

    internal interface TimerHandlerListener {
        fun callBack()
    }

    var specialInterval: SparseIntArray? = null
    var isStopped = true

    override fun handleMessage(msg: Message) {
        if (MSG_TIMER_ID == msg.what) {
            val nextIndex = miracleViewPager.nextItem
            if (listener != null) {
                listener!!.callBack()
            }
            tick(nextIndex)
        }
    }

    fun tick(index: Int) {
        sendEmptyMessageDelayed(TimerHandler.MSG_TIMER_ID, getNextInterval(index))
    }

    private fun getNextInterval(index: Int): Long {
        var next = interval
        if (specialInterval != null) {
            val has = specialInterval!!.get(index, -1).toLong()
            if (has > 0) {
                next = has
            }
        }
        return next
    }

    companion object {

        val MSG_TIMER_ID = 87108
    }

}
