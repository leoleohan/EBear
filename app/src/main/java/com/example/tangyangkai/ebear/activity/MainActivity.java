package com.example.tangyangkai.ebear.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.TextView;

import com.example.tangyangkai.ebear.R;
import com.example.tangyangkai.ebear.adapter.FragmentAdapter;
import com.example.tangyangkai.ebear.model.Person;
import com.example.tangyangkai.ebear.myinterface.ScrollDirectionListener;
import com.example.tangyangkai.ebear.view.PagerSlidingTabStrip;
import com.example.tangyangkai.ebear.view.RippleView;
import com.example.tangyangkai.ebear.view.RoundImageView;
import com.example.tangyangkai.ebear.view.floatbutton.FloatingActionButton;
import com.example.tangyangkai.ebear.view.floatbutton.FloatingActionsMenu;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobUser;

public class MainActivity extends AppCompatActivity {

    private ActionBarDrawerToggle mDrawerToggle;
    private FragmentPagerAdapter adapter;

    private List<Fragment> fragments = new ArrayList<>();

    @Bind(R.id.main_toolbar)
    Toolbar toolbar;
    @Bind(R.id.main_dl)
    DrawerLayout mainDl;
    @Bind(R.id.main_pager_tab)
    PagerSlidingTabStrip mainTab;
    @Bind(R.id.main_pager)
    ViewPager mainPager;
    @Bind(R.id.multiple_actions)
    FloatingActionsMenu floatingActionsMenu;
    @Bind(R.id.main_home_rv)
    RippleView mainHomeRv;
    @Bind(R.id.main_attention_rv)
    RippleView mainAttentionRv;
    @Bind(R.id.main_mine_rv)
    RippleView mainPersonalRv;
    @Bind(R.id.main_change_rv)
    RippleView mainChangeRv;
    @Bind(R.id.main_username)
    TextView mainUsernameTv;
    @Bind(R.id.main_head_img)
    RoundImageView mainHeadImg;
    private Context context;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    public static MainActivity instance = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initviews();
        context = this;
        instance = this;
    }

    private void initviews() {
        sp = getSharedPreferences("EbearInfo", context.MODE_PRIVATE);
        editor = sp.edit();
        setSupportActionBar(toolbar);

        //设置返回键可用
        getSupportActionBar().setHomeButtonEnabled(true);
        //加上返回图标
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //创建返回键，并实现打开关闭监听
        mDrawerToggle = new ActionBarDrawerToggle(this, mainDl, toolbar, R.string.app_name, R.string.app_name);
        mDrawerToggle.syncState();
        mainDl.setDrawerListener(mDrawerToggle);


        for (int i = 0; i < 3; i++) {
            Fragment fragment = new Fragment();
            fragments.add(fragment);
        }
        adapter = new FragmentAdapter(this.getSupportFragmentManager());
        mainPager.setAdapter(adapter);
        mainTab.setViewPager(mainPager);
        //改变颜色
        mainTab.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                colorChange(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //tab默认设置
        initTabsValue();
//设置通知栏颜色
        // setNotificationBarColor(Color.BLACK);

        FloatingActionButton fbUpdate = (FloatingActionButton) findViewById(R.id.fb_update);
        fbUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        FloatingActionButton fbWrite = (FloatingActionButton) findViewById(R.id.fb_new);
        fbWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AddNoteActivity.class));
                floatingActionsMenu.toggle();
            }
        });
        FloatingActionButton fbMy = (FloatingActionButton) findViewById(R.id.fb_person);
        fbMy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, PersonalActivity.class));
                floatingActionsMenu.toggle();
            }
        });


        mainPersonalRv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, PersonalActivity.class));
            }
        });
        mainChangeRv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                editor.clear();
                editor.commit();

                BmobUser.logOut(context);   //清除缓存用户对象
                BmobUser currentUser = BmobUser.getCurrentUser(context); // 现在的currentUser是null了
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            }
        });


        //进来初始化数据

        Person person = BmobUser.getCurrentUser(MainActivity.this, Person.class);
        mainUsernameTv.setText("欢迎你," + person.getNickname());
        if (person.getUser_icon() != null) {
            ImageLoader.getInstance().displayImage(person.getUser_icon(), mainHeadImg);

        } else {
            mainHeadImg.setBackground(getResources().getDrawable(R.drawable.defult_img));
        }


    }

    //悬浮按钮的折叠与隐藏
    public void hideFab() {
        floatingActionsMenu.hide();
    }


    public FloatingActionsMenu getFloatingActionsMenu() {
        return floatingActionsMenu;
    }

    //界面颜色更改
    private void colorChange(int position) {

        int color[] = new int[]{getResources().getColor(R.color.colorPrimary), getResources().getColor(R.color.mine_color), getResources().getColor(R.color.attention_color)};
        mainTab.setBackgroundColor(color[position]);
        toolbar.setBackgroundColor(color[position]);
        mainTab.setIndicatorColor(colorBurn(color[position]));
    }

    private void initTabsValue() {

        // 底部游标颜色
        mainTab.setIndicatorColor(Color.TRANSPARENT);
        // tab的分割线颜色
        mainTab.setDividerColor(Color.TRANSPARENT);
        // tab背景
        mainTab.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        // tab底线高度
        mainTab.setUnderlineHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                1, getResources().getDisplayMetrics()));
        // 游标高度
        mainTab.setIndicatorHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                5, getResources().getDisplayMetrics()));
        // 选中的文字颜色
        mainTab.setSelectedTextColor(Color.WHITE);
        // 正常文字颜色
        mainTab.setTextColor(Color.GRAY);
    }

    /**
     * 颜色加深处理
     *
     * @param RGBValues RGB的值，由alpha（透明度）、red（红）、green（绿）、blue（蓝）构成，
     *                  Android中我们一般使用它的16进制，
     *                  例如："#FFAABBCC",最左边到最右每两个字母就是代表alpha（透明度）、
     *                  red（红）、green（绿）、blue（蓝）。每种颜色值占一个字节(8位)，值域0~255
     *                  所以下面使用移位的方法可以得到每种颜色的值，然后每种颜色值减小一下，在合成RGB颜色，颜色就会看起来深一些了
     * @return
     */
    private int colorBurn(int RGBValues) {
        int alpha = RGBValues >> 24;
        int red = RGBValues >> 16 & 0xFF;
        int green = RGBValues >> 8 & 0xFF;
        int blue = RGBValues & 0xFF;
        red = (int) Math.floor(red * (1 - 0.1));
        green = (int) Math.floor(green * (1 - 0.1));
        blue = (int) Math.floor(blue * (1 - 0.1));
        return Color.rgb(red, green, blue);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (floatingActionsMenu.isExpanded()) {
                floatingActionsMenu.collapse();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Person person = BmobUser.getCurrentUser(MainActivity.this, Person.class);
        if (person.getNickname() != null) {
            mainUsernameTv.setText("欢迎你," + person.getNickname());
        } else {
            mainUsernameTv.setText("欢迎你," + person.getUsername());
        }
        if (person.getUser_icon() != null) {
            ImageLoader.getInstance().displayImage(person.getUser_icon(), mainHeadImg);

        } else {
            mainHeadImg.setBackground(getResources().getDrawable(R.drawable.defult_img));
        }


    }
}
