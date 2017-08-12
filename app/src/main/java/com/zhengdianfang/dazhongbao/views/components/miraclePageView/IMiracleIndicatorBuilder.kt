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

import android.graphics.Bitmap


/**
 * Created by mikeafc on 15/11/25.
 */
interface IMiracleIndicatorBuilder {
    /**
     * Set focused color for indicator.
     * @param focusColor
     * *
     * @return
     */
    fun setFocusColor(focusColor: Int): IMiracleIndicatorBuilder

    /**
     * Set normal color for indicator.
     * @param normalColor
     * *
     * @return
     */
    fun setNormalColor(normalColor: Int): IMiracleIndicatorBuilder

    /**
     * Set stroke color for indicator.
     * @param strokeColor
     * *
     * @return
     */
    fun setStrokeColor(strokeColor: Int): IMiracleIndicatorBuilder

    /**
     * Set stroke width for indicator.
     * @param strokeWidth
     * *
     * @return
     */
    fun setStrokeWidth(strokeWidth: Int): IMiracleIndicatorBuilder

    /**
     * Set spacing between indicator item ，the default value is item's height.
     * @param indicatorPadding
     * *
     * @return
     */
    fun setIndicatorPadding(indicatorPadding: Int): IMiracleIndicatorBuilder

    /**
     * Set the corner radius of the indicator item.
     * @param radius
     * *
     * @return
     */
    fun setRadius(radius: Int): IMiracleIndicatorBuilder

    /**
     * Sets the orientation of the layout.
     * @param orientation
     * *
     * @return
     */
    fun setOrientation(orientation: MiracleViewPager.Orientation): IMiracleIndicatorBuilder

    /**
     * Set the location at which the indicator should appear on the screen.

     * @param gravity android.view.Gravity
     * *
     * @return
     */
    fun setGravity(gravity: Int): IMiracleIndicatorBuilder

    /**
     * Set focused resource ID for indicator.
     * @param focusResId
     * *
     * @return
     */
    fun setFocusResId(focusResId: Int): IMiracleIndicatorBuilder

    /**
     * Set normal resource ID for indicator.
     * @param normalResId
     * *
     * @return
     */
    fun setNormalResId(normalResId: Int): IMiracleIndicatorBuilder

    /**
     * Set focused icon for indicator.
     * @param bitmap
     * *
     * @return
     */
    fun setFocusIcon(bitmap: Bitmap): IMiracleIndicatorBuilder

    /**
     * Set normal icon for indicator.
     * @param bitmap
     * *
     * @return
     */
    fun setNormalIcon(bitmap: Bitmap): IMiracleIndicatorBuilder

    /**
     * Set margins for indicator.
     * @param left   the left margin in pixels
     * *
     * @param top    the top margin in pixels
     * *
     * @param right  the right margin in pixels
     * *
     * @param bottom the bottom margin in pixels
     * *
     * @return
     */
    fun setMargin(left: Int, top: Int, right: Int, bottom: Int): IMiracleIndicatorBuilder

    /**
     * Combine all of the options that have been set and return a new IMiracleIndicatorBuilder object.
     */
    fun build()
}
