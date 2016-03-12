package com.example.tangyangkai.ebear.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tangyangkai.ebear.R;
import com.example.tangyangkai.ebear.utils.UiUtil;
import com.example.tangyangkai.ebear.view.RippleView;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.UpdateListener;

public class UpdatePasswordActivity extends AppCompatActivity {

    private Context context;

    @Bind(R.id.title_tv)
    TextView titleTv;
    @Bind(R.id.back_img)
    ImageView backImg;
    @Bind(R.id.update_pass_et)
    EditText oldpassEt;
    @Bind(R.id.update_newpass_et)
    EditText newpassEt;
    @Bind(R.id.update_againpass_et)
    EditText againpassEt;
    @Bind(R.id.update_save_rv)
    RippleView updateSaveRv;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);
        ButterKnife.bind(this);
        context = this;
        initviews();
    }

    private void initviews() {
        titleTv.setText("修改密码");
        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        updateSaveRv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(UiUtil.isEditTextNull(oldpassEt)||UiUtil.isEditTextNull(newpassEt)||UiUtil.isEditTextNull(againpassEt)){
                    UiUtil.showToast(context,"请输入完整信息");
                    return;
                }
                if(!newpassEt.getText().toString().trim().equals(againpassEt.getText().toString().trim())){
                    UiUtil.showToast(context,"两次密码不一致");
                    return;
                }

                BmobUser.updateCurrentUserPassword(context,oldpassEt.getText().toString(), newpassEt.getText().toString(), new UpdateListener() {

                    @Override
                    public void onSuccess() {
                        // TODO Auto-generated method stub
                        UiUtil.showToast(context, "密码修改成功，可以用新密码进行登录啦");

                    }

                    @Override
                    public void onFailure(int code, String msg) {
                        // TODO Auto-generated method stub
                        UiUtil.showToast(context, "密码修改失败：" + msg + "(" + code + ")");
                    }
                });


            }
        });
    }
}
