package com.example.tangyangkai.ebear.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.tangyangkai.ebear.fragment.AttentionFragment;
import com.example.tangyangkai.ebear.fragment.HomeFragment;
import com.example.tangyangkai.ebear.fragment.MineFragment;

/**
 * Created by Administrator on 2016/3/4.
 */
public class FragmentAdapter extends FragmentPagerAdapter {
    public FragmentAdapter(FragmentManager fm) {
        super(fm);
    }


    private HomeFragment fragmentHome;
    private AttentionFragment fragmentAttention;
    private MineFragment fragmentMine;

    private String[] titles = new String[]{"主页", "关注", "我的"};


    public void set(){};



    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                if (fragmentHome == null) {
                    fragmentHome = new HomeFragment();
                }
                return fragmentHome;
            case 1:
                if (fragmentAttention == null) {
                    fragmentAttention = new AttentionFragment();
                }
                return fragmentAttention;
            case 2:
                if (fragmentMine == null) {
                    fragmentMine = new MineFragment();
                }
                return fragmentMine;


            default:
                return null;
        }


    }

    @Override
    public int getCount() {

        return titles.length;
    }

    @Override
    public String getPageTitle(int position) {
        return titles[position];
    }
}
