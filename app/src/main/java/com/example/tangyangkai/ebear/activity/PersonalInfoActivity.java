package com.example.tangyangkai.ebear.activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tangyangkai.ebear.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PersonalInfoActivity extends AppCompatActivity {

    private Context context;
    @Bind(R.id.title_tv)
    TextView titleTv;
    @Bind(R.id.back_img)
    ImageView backImg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);
        ButterKnife.bind(this);
        context = this;
        initviews();
    }

    private void initviews() {
        titleTv.setText("个人信息");
        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
