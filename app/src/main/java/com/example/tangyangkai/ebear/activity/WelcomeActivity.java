package com.example.tangyangkai.ebear.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.tangyangkai.ebear.R;
import com.example.tangyangkai.ebear.utils.UiUtil;

import java.util.Timer;
import java.util.TimerTask;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;

public class WelcomeActivity extends AppCompatActivity {


    private SharedPreferences sp;
    private boolean isFirstRun;
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        context = this;


        sp = getSharedPreferences("EbearInfo", context.MODE_PRIVATE);
        isFirstRun = sp.getBoolean("isFirstLogin", true);

        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (isFirstRun) {
                    startActivity(new Intent(context, LoginActivity.class));
                    finish();
                } else {
                    BmobUser user = new BmobUser();
                    user.setUsername(sp.getString("username", null));
                    user.setPassword(sp.getString("password", null));
                    user.login(context, new SaveListener() {
                        @Override
                        public void onSuccess() {


                            //获取到当前用户的信息

                            Intent intent = new Intent(context, MainActivity.class);
                            startActivity(intent);
                            finish();
                            UiUtil.showToast(context, "登录成功");

                        }

                        @Override
                        public void onFailure(int code, String msg) {
                            Intent intent = new Intent(context, LoginActivity.class);
                            startActivity(intent);
                            finish();

                        }
                    });
                }
            }
        };
        timer.schedule(task, 2000);


    }
}