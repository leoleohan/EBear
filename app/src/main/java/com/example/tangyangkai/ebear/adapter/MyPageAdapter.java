package com.example.tangyangkai.ebear.adapter;

import android.app.Activity;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;


import java.util.ArrayList;

/**
 * Created by EasyShare005 on 2015/7/1.
 */
public class MyPageAdapter extends PagerAdapter {
    private ArrayList<View> listViews;
    private Activity activity;

    private int size;

    public MyPageAdapter(ArrayList<View> listViews, Activity activity) {
        this.listViews = listViews;
        size = listViews == null ? 0 : listViews.size();
        this.activity = activity;
    }

    public void setListViews(ArrayList<View> listViews) {
        this.listViews = listViews;
        size = listViews == null ? 0 : listViews.size();
    }

    public int getCount() {
        return size;
    }

    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    public void destroyItem(View arg0, int arg1, Object arg2) {
        ((ViewPager) arg0).removeView(listViews.get(arg1 % size));
    }

    public void finishUpdate(View arg0) {
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ((ViewPager) container).addView(listViews.get(position ),0);
        return listViews.get(position);
    }

    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

}
