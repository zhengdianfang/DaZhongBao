package com.zhengdianfang.dazhongbao.views.home.adapter

import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.zhengdianfang.dazhongbao.R
import com.zhengdianfang.dazhongbao.models.product.Advert

/**
 * Created by dfgzheng on 05/08/2017.
 */
class AdvertViewPagerAdapter(private val adverts: MutableList<Advert>) : PagerAdapter() {
    override fun isViewFromObject(view: View?, `object`: Any?): Boolean {
        return view == `object`
    }

    override fun getCount(): Int {
        return adverts.size
    }

    override fun instantiateItem(container: ViewGroup?, position: Int): Any {
        val imageView = ImageView(container?.context)
        Glide.with(imageView.context).load(adverts[position].banner).apply(RequestOptions().placeholder(R.mipmap.banner_test).error(R.mipmap.banner_test)).into(imageView)
        container?.addView(imageView)
        return imageView
    }

    override fun destroyItem(container: ViewGroup?, position: Int, `object`: Any?) {
        container?.removeView(`object` as View)
    }
}