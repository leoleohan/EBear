package com.example.tangyangkai.ebear.activity;

import android.content.Context;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tangyangkai.ebear.R;
import com.example.tangyangkai.ebear.adapter.GridViewImgAdapter;
import com.example.tangyangkai.ebear.model.Note;
import com.example.tangyangkai.ebear.model.Person;
import com.example.tangyangkai.ebear.utils.HankkinUtils;
import com.example.tangyangkai.ebear.utils.ImgScanHelper;
import com.example.tangyangkai.ebear.view.NoScrollGridView;
import com.example.tangyangkai.ebear.view.RoundImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.bmob.v3.BmobUser;

public class NoteDetailActivity extends AppCompatActivity {


    private Note note;
    private Context context;
    private TextView nicknameTv, timeTv, noteTv, addressTv;
    private RoundImageView img, userImg;
    private NoScrollGridView gridView;
    private GridViewImgAdapter mAdapter;
    private List<String> urls = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);
        context = this;
        initviews();
    }

    private void initviews() {
        note = (Note) getIntent().getSerializableExtra("note");
        nicknameTv = (TextView) findViewById(R.id.tv_username);
        timeTv = (TextView) findViewById(R.id.tv_time);
        noteTv = (TextView) findViewById(R.id.tv_pro_name);
        addressTv = (TextView) findViewById(R.id.tv_school);
        img = (RoundImageView) findViewById(R.id.iv_user_head);
        userImg = (RoundImageView) findViewById(R.id.usericon);
        gridView = (NoScrollGridView) findViewById(R.id.iv_gridview);
        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //透明导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(android.support.v7.appcompat.R.drawable.abc_ic_ab_back_material);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar.inflateMenu(R.menu.menu_prodect_detail);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_share:
                        HankkinUtils.showToast(context, "分享");
                        break;
                    case R.id.action_settings:
                        HankkinUtils.showToast(context, "举报");
                        break;
                }
                return false;
            }
        });

        CollapsingToolbarLayout collapsingAvatarToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingAvatarToolbar.setBackgroundColor(getResources().getColor(R.color.theme_color));
        collapsingAvatarToolbar.setExpandedTitleGravity(Gravity.CENTER_VERTICAL);
        collapsingAvatarToolbar.setExpandedTitleColor(getResources().getColor(R.color.theme_color));


        Person person = BmobUser.getCurrentUser(context, Person.class);

        if (note.getImgs() != null) {
            String imgUrls = note.getImgs().substring(1, note.getImgs().length() - 1);
            if (!TextUtils.isEmpty(imgUrls)) {
                String[] arr = imgUrls.split(",");
                urls = Arrays.asList(arr);
                Log.e("!!!!!!!!!!!!!",note.getImgs());
            }
            if (note.getUserId().equals(person.getObjectId())) {
                ImageLoader.getInstance().displayImage(person.getUser_icon(), img);
                ImageLoader.getInstance().displayImage(person.getUser_icon(), userImg);
                nicknameTv.setText(person.getNickname());
            } else {
                ImageLoader.getInstance().displayImage(note.getUser_icon(), img);
                ImageLoader.getInstance().displayImage(note.getUser_icon(), userImg);
                nicknameTv.setText(person.getNickname());
            }

            addressTv.setText(note.getAddress());

            noteTv.setText(note.getNote());
            timeTv.setText(note.getTime());


        } else {
            if (note.getUserId().equals(person.getObjectId())) {
                ImageLoader.getInstance().displayImage(person.getUser_icon(), img);
                ImageLoader.getInstance().displayImage(person.getUser_icon(), userImg);
                nicknameTv.setText(person.getNickname());
            } else {
                ImageLoader.getInstance().displayImage(note.getUser_icon(), img);
                ImageLoader.getInstance().displayImage(note.getUser_icon(), userImg);
                nicknameTv.setText(person.getNickname());
            }

            addressTv.setText(note.getAddress());

            noteTv.setText(note.getNote());
            timeTv.setText(note.getTime());
        }
        mAdapter = new GridViewImgAdapter(context,urls);
        gridView.setAdapter(mAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                new ImgScanHelper(context, urls, position).show();
            }
        });

    }


}
