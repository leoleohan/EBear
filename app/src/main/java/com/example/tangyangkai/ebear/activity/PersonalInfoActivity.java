package com.example.tangyangkai.ebear.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.tangyangkai.ebear.R;
import com.example.tangyangkai.ebear.model.Note;
import com.example.tangyangkai.ebear.model.Person;
import com.example.tangyangkai.ebear.utils.Calculate;
import com.example.tangyangkai.ebear.utils.DateDialog;
import com.example.tangyangkai.ebear.utils.GetDate;
import com.example.tangyangkai.ebear.utils.UiUtil;
import com.example.tangyangkai.ebear.view.RippleView;
import com.example.tangyangkai.ebear.view.RoundImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.UpdateListener;
import me.drakeet.materialdialog.MaterialDialog;

public class PersonalInfoActivity extends AppCompatActivity {

    private Context context;
    private String sex;

    @Bind(R.id.title_tv)
    TextView titleTv;
    @Bind(R.id.back_img)
    ImageView backImg;
    @Bind(R.id.info_bir_rl)
    RelativeLayout infoBirRl;
    @Bind(R.id.info_bir_tv)
    TextView infoBirTv;
    @Bind(R.id.info_sex_rg)
    RadioGroup infoSexRg;
    @Bind(R.id.info_man_rb)
    RadioButton infoManRb;
    @Bind(R.id.info_woman_rb)
    RadioButton infoWomanRb;
    @Bind(R.id.info_name_et)
    EditText infoNameEt;
    @Bind(R.id.info_save_rv)
    RippleView infoSaveRv;
    @Bind(R.id.info_head_img)
    RoundImageView infoHeadImg;

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
        infoBirRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DateDialog dialog = new DateDialog(context);
                dialog.setDate(0, infoBirTv);
            }
        });

        infoSexRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.info_man_rb:
                        infoManRb.setBackground(getResources().getDrawable(R.drawable.man_focus));
                        infoWomanRb.setBackground(getResources().getDrawable(R.drawable.woman_normal));
                        sex = "男";
                        break;
                    case R.id.info_woman_rb:
                        infoManRb.setBackground(getResources().getDrawable(R.drawable.man_normal));
                        infoWomanRb.setBackground(getResources().getDrawable(R.drawable.woman_focus));
                        sex = "女";
                        break;
                    default:
                        break;
                }
            }
        });

        infoSaveRv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Person newUser = new Person();
                newUser.setNickname(infoNameEt.getText().toString().trim());
                //计算年龄
                Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);

                int age = Calculate.calculateAge(infoBirTv.getText().toString(), month, year);

                newUser.setAge(age);
                newUser.setSex(sex);
                newUser.setBirthday(infoBirTv.getText().toString());
                BmobUser bmobUser = BmobUser.getCurrentUser(context);
                newUser.update(context, bmobUser.getObjectId(), new UpdateListener() {
                    @Override
                    public void onSuccess() {
                        // TODO Auto-generated method stub

                        //修改listview里面的值

                        UiUtil.showToast(context, "更新用户信息成功");
                        finish();
                    }

                    @Override
                    public void onFailure(int code, String msg) {
                        // TODO Auto-generated method stub
                        UiUtil.showToast(context, "更新用户信息失败:" + msg);
                    }
                });


            }
        });
        //加载进来初始化信息
        Person person = BmobUser.getCurrentUser(PersonalInfoActivity.this, Person.class);
        if (person.getNickname() != null) {
            infoNameEt.setText(person.getNickname());
        } else {
            infoNameEt.setText(person.getUsername());
        }
        if (person.getAge() != null && person.getBirthday() != null) {
            infoBirTv.setText(person.getBirthday());
        } else {
            infoBirTv.setText(GetDate.lastDay());
        }
         if (person.getSex() == null) {
            infoManRb.setBackground(getResources().getDrawable(R.drawable.man_focus));
            infoWomanRb.setBackground(getResources().getDrawable(R.drawable.woman_normal));
        }else if (person.getSex().equals("男")) {
            infoManRb.setBackground(getResources().getDrawable(R.drawable.man_focus));
            infoWomanRb.setBackground(getResources().getDrawable(R.drawable.woman_normal));
        } else if (person.getSex().equals("女")) {
            infoManRb.setBackground(getResources().getDrawable(R.drawable.man_normal));
            infoWomanRb.setBackground(getResources().getDrawable(R.drawable.woman_focus));
        }


        if (person.getUser_icon() != null) {
            ImageLoader.getInstance().displayImage(person.getUser_icon(), infoHeadImg);

        } else {
            infoHeadImg.setBackground(getResources().getDrawable(R.drawable.defult_img));
        }


    }

}
