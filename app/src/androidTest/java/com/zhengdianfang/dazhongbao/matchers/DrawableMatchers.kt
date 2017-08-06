package com.zhengdianfang.dazhongbao.matchers

import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.StateListDrawable
import android.support.test.espresso.matcher.BoundedMatcher
import android.view.View
import android.widget.Button
import android.widget.ImageView
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.junit.internal.matchers.TypeSafeMatcher


/**
 * Created by dfgzheng on 06/08/2017.
 */
class DrawableMatchers {
    companion object {
        fun withBackground(resourceId: Int): Matcher<View> {
            return object : TypeSafeMatcher<View>() {

                override fun matchesSafely(view: View): Boolean {
                    return sameBitmap(view.context, view.background, resourceId)
                }

                override fun describeTo(description: Description) {
                    description.appendText("has background resource " + resourceId)
                }
            }
        }

        fun withCompoundDrawableForButton(resourceId: Int): Matcher<View> {
            return object : BoundedMatcher<View, Button>(Button::class.java) {
                override fun describeTo(description: Description) {
                    description.appendText("has compound drawable resource " + resourceId)
                }

                public override fun matchesSafely(button: Button): Boolean {
                    return button.compoundDrawables.any { sameBitmap(button.context, it, resourceId) }
                }
            }
        }

        fun withImageDrawable(resourceId: Int): Matcher<View> {
            return object : BoundedMatcher<View, ImageView>(ImageView::class.java!!) {
                override fun describeTo(description: Description) {
                    description.appendText("has image drawable resource " + resourceId)
                }

                public override fun matchesSafely(imageView: ImageView): Boolean {
                    return sameBitmap(imageView.context, imageView.drawable, resourceId)
                }
            }
        }

        private fun sameBitmap(context: Context, drawable: Drawable?, resourceId: Int): Boolean {
            var drawable = drawable
            var otherDrawable = context.resources.getDrawable(resourceId)
            if (drawable == null || otherDrawable == null) {
                return false
            }
            if (drawable is StateListDrawable && otherDrawable is StateListDrawable) {
                drawable = drawable.current
                otherDrawable = otherDrawable!!.current
            }
            if (drawable is BitmapDrawable) {
                val bitmap = drawable.bitmap
                val otherBitmap = (otherDrawable as BitmapDrawable).bitmap
                return bitmap.sameAs(otherBitmap)
            }
            return false
        }
    }

}