package com.zhengdianfang.dazhongbao.views.components.refreshLayout

import android.view.MotionEvent

/**
 * Created by dfgzheng on 05/08/2017.
 */
interface OnGestureListener {

    fun onDown(e: MotionEvent)

    fun onScroll(donwEvent: MotionEvent, currentEvent: MotionEvent, distanceX: Float, distanceY: Float)

    fun onUp(e: MotionEvent, isFling: Boolean)

    fun onFling(donwEvent: MotionEvent, upEvent: MotionEvent, velocityX: Float, velocityY: Float)
}