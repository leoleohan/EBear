package com.example.tangyangkai.ebear.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.tangyangkai.ebear.R;
import com.example.tangyangkai.ebear.utils.UiUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;

public class LoginActivity extends AppCompatActivity {


    private Context context;
    @Bind(R.id.login_username_etv)
    EditText usernameEt;
    @Bind(R.id.login_password_etv)
    EditText passwordEt;
    @Bind(R.id.login_login_btn)
    Button loginBtn;
    @Bind(R.id.login_register_tv)
    TextView registerTv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        initviews();
        context = this;
    }

    private void initviews() {

        registerTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(LoginActivity.this,
                        RegisterActivity.class), 1);
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userLogin();
            }
        });


    }

    //用户登录
    private void userLogin() {
        if (UiUtil.isEditTextNull(usernameEt) || UiUtil.isEditTextNull(passwordEt)) {
            UiUtil.showToast(context, "请输入完整信息");
            return;
        }

        BmobUser user = new BmobUser();
        user.setUsername(usernameEt.getText().toString().trim());
        user.setPassword(passwordEt.getText().toString().trim());
        user.login(this, new SaveListener() {
            @Override
            public void onSuccess() {
                UiUtil.showToast(context, "登录成功");
                startActivity(new Intent(context, MainActivity.class));
                finish();
            }

            @Override
            public void onFailure(int code, String msg) {
                // TODO Auto-generated method stub
                UiUtil.showToast(context, " 登录失败:" + msg);
            }
        });


    }

    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, intent);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1:
                    Bundle data = intent.getExtras();
                    usernameEt.setText(data.getString("username"));
                    break;

                default:
                    break;
            }
        }

    }
}
