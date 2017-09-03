package com.zhengdianfang.dazhongbao.views.im.components.views;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by zheng on 16/6/29.
 */

public class EmojiViewPagerAdapter extends PagerAdapter {

    private ArrayList<View> pageViews;

    public EmojiViewPagerAdapter(ArrayList<View> pageViews) {
        this.pageViews = pageViews;
    }

    @Override
    public int getCount() {
        return pageViews.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(pageViews.get(position));
        return pageViews.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
