package com.zhengdianfang.dazhongbao.views.home.adapter

import android.content.Intent
import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.zhengdianfang.dazhongbao.R
import com.zhengdianfang.dazhongbao.models.product.Advert
import com.zhengdianfang.dazhongbao.views.basic.WebActivity
import com.zhengdianfang.dazhongbao.views.product.ProductDetailActivity

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
        val advert = adverts[position]
        val imageView = ImageView(container?.context)
        Glide.with(imageView.context).load(advert.banner).placeholder(R.mipmap.banner_test).error(R.mipmap.banner_test).into(imageView)
        container?.addView(imageView)
        imageView.setOnClickListener {
            val context = container?.context!!
            if (advert.mod  == 1){
                context.startActivity(Intent(context, ProductDetailActivity::class.java).putExtra("productId", advert.productId))
            }else if(advert.mod == 2) {
                WebActivity.startActivity(context, advert.name, advert.link)
            }
        }
        return imageView
    }


    override fun destroyItem(container: ViewGroup?, position: Int, `object`: Any?) {
        container?.removeView(`object` as View)
    }
}