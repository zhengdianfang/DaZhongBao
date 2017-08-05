package com.zhengdianfang.dazhongbao.views.home.adapter

import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.zhengdianfang.dazhongbao.models.product.Advert

/**
 * Created by dfgzheng on 05/08/2017.
 */
class AdvertViewPagerAdapter(val adverts: MutableList<Advert>?) : PagerAdapter() {
    override fun isViewFromObject(view: View?, `object`: Any?): Boolean {
        return view == `object`
    }

    override fun getCount(): Int {
        return adverts?.size ?: 0
    }

    override fun instantiateItem(container: ViewGroup?, position: Int): Any {
        val imageView = ImageView(container?.context)
        Glide.with(imageView.context).load(adverts?.get(position)?.image). into(imageView)
        container?.addView(imageView)
        return imageView
    }

    override fun destroyItem(container: ViewGroup?, position: Int, `object`: Any?) {
        container?.removeView(`object` as View)
    }
}