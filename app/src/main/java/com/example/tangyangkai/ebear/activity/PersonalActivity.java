package com.example.tangyangkai.ebear.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.tangyangkai.ebear.R;
import com.example.tangyangkai.ebear.view.PullToZoomScrollViewEx;
import com.example.tangyangkai.ebear.view.RippleView;

public class PersonalActivity extends AppCompatActivity {

    private ImageView backImg;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);
        initViews();
    }


    private void initViews() {
        backImg = (ImageView) findViewById(R.id.back_img);
        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

            }
        });

        PullToZoomScrollViewEx   scrollView = (PullToZoomScrollViewEx) findViewById(R.id.my_pull_scroll);

        loadViewForCode(scrollView);

        setPullToZoomViewLayoutParams(scrollView);


    }

    //事件处理必须写在这里
    private void loadViewForCode(PullToZoomScrollViewEx scrollView) {
      View  headView = LayoutInflater.from(this).inflate(R.layout.profile_head_view, null, false);
       View zoomView = LayoutInflater.from(this).inflate(R.layout.profile_zoom_view, null, false);
       View contentView = LayoutInflater.from(this).inflate(R.layout.profile_contect_view, null, false);
        scrollView.setHeaderView(headView);
        scrollView.setZoomView(zoomView);
        scrollView.setScrollContentView(contentView);


        RippleView personalRv= (RippleView) contentView.findViewById(R.id.personal_mine_rv);
        personalRv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PersonalActivity.this,PersonalInfoActivity.class));
            }
        });



    }

    // 设置头部的View的宽高。
         private void setPullToZoomViewLayoutParams(PullToZoomScrollViewEx scrollView) {
                 DisplayMetrics localDisplayMetrics = new DisplayMetrics();
                 getWindowManager().getDefaultDisplay().getMetrics(localDisplayMetrics);
                 int mScreenHeight = localDisplayMetrics.heightPixels;
                 int mScreenWidth = localDisplayMetrics.widthPixels;
                 LinearLayout.LayoutParams localObject = new LinearLayout.LayoutParams(mScreenWidth,
                                 (int) (9.0F * (mScreenWidth / 16.0F)));
                 scrollView.setHeaderLayoutParams(localObject);
             }
}
