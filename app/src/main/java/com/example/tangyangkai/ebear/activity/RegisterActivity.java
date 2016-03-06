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

public class RegisterActivity extends AppCompatActivity {

    private Context context;

    @Bind(R.id.register_username_etv)
    EditText usernameEt;
    @Bind(R.id.register_password_etv)
    EditText passwordEt;
    @Bind(R.id.register_anginpass_etv)
    EditText passwordAgainEt;


    @Bind(R.id.register_btn)
    Button registerBtn;
    @Bind(R.id.register_register_tv)
    TextView registerTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        context = this;
        initviews();
    }

    private void initviews() {

        registerTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (UiUtil.isEditTextNull(usernameEt) || UiUtil.isEditTextNull(passwordEt) || UiUtil.isEditTextNull(passwordAgainEt)) {
                    UiUtil.showToast(context, "请输入完整信息");
                    return;
                }

                if (!passwordEt.getText().toString().trim().equals(passwordAgainEt.getText().toString().trim())) {
                    UiUtil.showToast(context, "两次密码不一致，请重新输入");
                    return;
                }

                registerUser();


            }
        });
    }

    //注册实现
    private void registerUser() {

        //首先验证是否存在相同的用户


        //开始注册
        BmobUser user = new BmobUser();
        user.setUsername(usernameEt.getText().toString().trim());
        user.setPassword(passwordEt.getText().toString().trim());

        user.signUp(context, new SaveListener() {
            @Override
            public void onSuccess() {
                UiUtil.showToast(context, "注册成功");
                Intent intent = getIntent();
                intent.putExtra("username", usernameEt.getText().toString().trim());
                setResult(RESULT_OK, intent);
                finish();
            }

            @Override
            public void onFailure(int i, String msg) {
                UiUtil.showToast(context, msg);
            }
        });

    }
}
