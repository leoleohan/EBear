package com.example.tangyangkai.ebear.utils;


import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

import com.example.tangyangkai.ebear.R;
import com.example.tangyangkai.ebear.adapter.CommonPageAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ImgScanHelper extends Dialog implements
        View.OnClickListener {

    private List<String> mListImgUrls=new ArrayList<String>();

    private Context mContext;
    private ViewPager mViewPager;
    private int mClickItem;


    public ImgScanHelper(Context context, List<String> imgUrlss, int clickItem) {
        super(context, R.style.CustomDialog_fill);
        this.mContext = context;
        this.mListImgUrls = imgUrlss;
        this.mClickItem = clickItem;
        initView();
    }

    private void initView() {
        mViewPager = new ViewPager(mContext);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        mViewPager.setLayoutParams(params);
        mViewPager.setBackgroundColor(0xFF000000);
        setContentView(mViewPager);
        setParams();
        initViewPager();
    }

    private void setParams() {
        LayoutParams lay = this.getWindow().getAttributes();
        DisplayMetrics dm = new DisplayMetrics();
        this.getWindow().getWindowManager().getDefaultDisplay().getMetrics(dm);
        Rect rect = new Rect();
        View view = getWindow().getDecorView();
        view.getWindowVisibleDisplayFrame(rect);
        lay.height = dm.heightPixels - rect.top;
        lay.width = dm.widthPixels;
    }

    private void initViewPager() {
        if (mListImgUrls != null && mListImgUrls.size() > 0) {
            List<View> listImgs = new ArrayList<View>();
            for (int i = 0; i < mListImgUrls.size(); i++) {
                ImageView iv = new ImageView(mContext);
                LayoutParams params = new LayoutParams(
                        LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
                iv.setLayoutParams(params);
                listImgs.add(iv);
                iv.setOnClickListener(this);

                //加载本地图片

                // 加载网络图片
                ImageLoader.getInstance().displayImage(mListImgUrls.get(i).trim(), iv);
            }
            if (listImgs.size() > 0) {
                CommonPageAdapter pageAdapter = new CommonPageAdapter(listImgs);
                mViewPager.setAdapter(pageAdapter);
                mViewPager.setCurrentItem(mClickItem);
            }
        }
    }

    @Override
    public void onClick(View v) {
        this.dismiss();
    }

    @Override
    public void dismiss() {
        super.dismiss();

    }

}
