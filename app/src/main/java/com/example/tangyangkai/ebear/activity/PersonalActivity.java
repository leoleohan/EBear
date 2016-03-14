package com.example.tangyangkai.ebear.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.tangyangkai.ebear.R;
import com.example.tangyangkai.ebear.model.Note;
import com.example.tangyangkai.ebear.model.Person;
import com.example.tangyangkai.ebear.utils.BitmapUtils;
import com.example.tangyangkai.ebear.utils.HankkinUtils;
import com.example.tangyangkai.ebear.utils.UiUtil;
import com.example.tangyangkai.ebear.view.PullToZoomScrollViewEx;
import com.example.tangyangkai.ebear.view.RippleView;
import com.example.tangyangkai.ebear.view.RoundImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.pnikosis.materialishprogress.ProgressWheel;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import me.drakeet.materialdialog.MaterialDialog;

public class PersonalActivity extends AppCompatActivity {

    private ImageView backImg;
    private Context context;
    private Uri imageUri;
    private MaterialDialog loadDialog;
    private RoundImageView imageview;
    private TextView nicknameTv;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private List<Note> noteList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);
        initViews();
        context = this;
    }


    private void initViews() {
        sp = getSharedPreferences("EbearInfo", context.MODE_PRIVATE);
        editor = sp.edit();
        backImg = (ImageView) findViewById(R.id.back_img);
        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

            }
        });

        PullToZoomScrollViewEx scrollView = (PullToZoomScrollViewEx) findViewById(R.id.my_pull_scroll);

        loadViewForCode(scrollView);

        setPullToZoomViewLayoutParams(scrollView);


        //进来初始化数据
        Person person = BmobUser.getCurrentUser(PersonalActivity.this, Person.class);
        if (person.getUser_icon() != null) {
            ImageLoader.getInstance().displayImage(person.getUser_icon(), imageview);

        } else {
            imageview.setBackground(getResources().getDrawable(R.drawable.defult_img));
        }
        if (person.getNickname() != null) {
            nicknameTv.setText(person.getNickname());
        } else {
            nicknameTv.setText(person.getUsername());
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        //进来初始化数据
        Person person = BmobUser.getCurrentUser(PersonalActivity.this, Person.class);
        if (person.getUser_icon() != null) {
            ImageLoader.getInstance().displayImage(person.getUser_icon(), imageview);

        } else {
            imageview.setBackground(getResources().getDrawable(R.drawable.defult_img));
        }
        if (person.getNickname() != null) {
            nicknameTv.setText(person.getNickname());
        } else {
            nicknameTv.setText(person.getUsername());
        }


    }

    //事件处理必须写在这里
    private void loadViewForCode(PullToZoomScrollViewEx scrollView) {
        View headView = LayoutInflater.from(this).inflate(R.layout.profile_head_view, null, false);
        View zoomView = LayoutInflater.from(this).inflate(R.layout.profile_zoom_view, null, false);
        View contentView = LayoutInflater.from(this).inflate(R.layout.profile_contect_view, null, false);
        scrollView.setHeaderView(headView);
        scrollView.setZoomView(zoomView);
        scrollView.setScrollContentView(contentView);


        RippleView personalRv = (RippleView) contentView.findViewById(R.id.personal_mine_rv);
        personalRv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PersonalActivity.this, PersonalInfoActivity.class));
            }
        });

        RippleView updateRv = (RippleView) contentView.findViewById(R.id.personal_set_rv);
        updateRv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PersonalActivity.this, UpdatePasswordActivity.class));
            }
        });


        RippleView quitRv = (RippleView) contentView.findViewById(R.id.personal_change_rv);
        quitRv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MainActivity.instance.finish();
                editor.clear();
                editor.commit();

                BmobUser.logOut(context);   //清除缓存用户对象
                BmobUser currentUser = BmobUser.getCurrentUser(context); // 现在的currentUser是null了
                startActivity(new Intent(PersonalActivity.this, LoginActivity.class));
                finish();
            }
        });

        nicknameTv = (TextView) headView.findViewById(R.id.tv_user_name);
        imageview = (RoundImageView) headView.findViewById(R.id.iv_user_head);
        imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View dialogView = LayoutInflater.from(PersonalActivity.this).inflate(R.layout.view_select_img, null, false);
                final MaterialDialog dialog = new MaterialDialog(context).setView(dialogView);
                dialog.show();

                TextView cancelTv = (TextView) dialogView.findViewById(R.id.tv_cancel);
                cancelTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                TextView caemraTv = (TextView) dialogView.findViewById(R.id.tv_comear);
                caemraTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startCaemra();
                        dialog.dismiss();
                    }
                });
                TextView galleryTv = (TextView) dialogView.findViewById(R.id.tv_gallery);
                galleryTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startGralley();
                        dialog.dismiss();
                    }
                });
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


    //开始拍照
    private void startCaemra() {
        // 指定照相机拍照后图片的存储路径，这里存储在自己定义的文件夹下
        if (Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())) {
            // 创建一个文件夹对象，赋值为外部存储器的目录
            File sdcardDir = Environment.getExternalStorageDirectory();
            // 得到一个路径，内容是sdcard的文件夹路径和名字
            String path = sdcardDir.getPath()
                    + "/EBear/cardImages";
            File photofile = new File(path);
            if (!photofile.exists()) {
                // 若不存在，创建目录，可以在应用启动的时候创建
                photofile.mkdirs();
            } else {
                imageUri = Uri.fromFile(new File(photofile,
                        "myheadimg.jpg"));
                // 拍照我们用Action为MediaStore.ACTION_IMAGE_CAPTURE，
                // 有些人使用其他的Action但我发现在有些机子中会出问题，所以优先选择这个
                Intent intent = new Intent(
                        MediaStore.ACTION_IMAGE_CAPTURE);
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                // 保存照片在自定义的文件夹下面
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, 1);
            }
        } else {
            UiUtil.showToast(context, "SD卡不可用");
            return;

        }


    }

    //选择图库
    private void startGralley() {
        try {
            // 选择照片的时候也一样，我们用Action为Intent.ACTION_GET_CONTENT，
            // 有些人使用其他的Action但我发现在有些机子中会出问题，所以优先选择这个
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, 2);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        Uri uri = null;
        //如果返回的是拍照上传
        if (data == null) {
            uri = imageUri;
        } //返回的是图库上传
        else {
            uri = data.getData();
        }
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1:
                    intent.setDataAndType(uri, "image/*");
                    //下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
                    intent.putExtra("crop", true);
                    // 设置裁剪尺寸
                    intent.putExtra("aspectX", 1);
                    intent.putExtra("aspectY", 1);
                    intent.putExtra("outputX", 320);
                    intent.putExtra("outputY", 320);
                    intent.putExtra("return-data", true);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                    startActivityForResult(intent, 3);
                    break;
                case 2:
                    intent.setDataAndType(uri, "image/*");
                    intent.putExtra("crop", true);
                    // 设置裁剪尺寸
                    intent.putExtra("aspectX", 1);
                    intent.putExtra("aspectY", 1);
                    intent.putExtra("outputX", 320);
                    intent.putExtra("outputY", 320);
                    intent.putExtra("return-data", true);
                    // 创建一个文件夹对象，赋值为外部存储器的目录
                    File sdcardDir = Environment.getExternalStorageDirectory();
                    // 得到一个路径，内容是sdcard的文件夹路径和名字
                    String path = sdcardDir.getPath()
                            + "/EBear/cardImages";

                    File photofile = new File(path);
                    uri = Uri.fromFile(new File(photofile, "myheadimg.jpg"));
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                    startActivityForResult(intent, 3);

                    break;
                case 3:
                    if (data != null) {
                        showLoadingDialog();
                        Bundle bundle = data.getExtras();
                        Bitmap myBitmap = bundle.getParcelable("data");
                        uploadHeadImg();
                        imageview.setImageBitmap(myBitmap);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    //上传图片到服务器
    private void uploadHeadImg() {
        File sdcardDir = Environment.getExternalStorageDirectory();
        String picPath = sdcardDir.getPath()
                + "/EBear/cardImages/myheadimg.jpg";
        final BmobFile bmobFile = new BmobFile(new File(picPath));
        bmobFile.uploadblock(context, new UploadFileListener() {

            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub
                //bmobFile.getUrl()---返回的上传文件的地址（不带域名）
                //bmobFile.getFileUrl(context)--返回的上传文件的完整地址（带域名）
                String img_url = bmobFile.getFileUrl(context);
                update(img_url);
                Log.e("url!!!!!", img_url);
            }

            @Override
            public void onProgress(Integer value) {
                // TODO Auto-generated method stub
                // 返回的上传进度（百分比）
            }

            @Override
            public void onFailure(int code, String msg) {
                // TODO Auto-generated method stub
                dimissDialog();
                UiUtil.showToast(context, "上传文件失败：" + msg);
            }
        });
    }


    //更新用户信息
    private void update(final String url) {
        Person newUser = new Person();
        newUser.setUser_icon(url);
        BmobUser bmobUser = BmobUser.getCurrentUser(context);
        newUser.update(context, bmobUser.getObjectId(), new UpdateListener() {
            @Override
            public void onSuccess() {


                // TODO Auto-generated method stub
                //通知listview进行修改

                dimissDialog();

                UiUtil.showToast(context, "上传头像成功");
            }

            @Override
            public void onFailure(int code, String msg) {
                // TODO Auto-generated method stub
                dimissDialog();
                UiUtil.showToast(context, "上传头像失败:" + msg);
            }
        });
    }


    //进度条对话框
    public void showLoadingDialog() {
        loadDialog = new MaterialDialog(this);
        View view = LayoutInflater.from(this).inflate(R.layout.loading, null, false);
        ProgressWheel wheel = (ProgressWheel) view.findViewById(R.id.pw_loading);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(80, 80);
        params.height = HankkinUtils.dip2px(this, 80);
        params.width = HankkinUtils.dip2px(this, 80);
        wheel.setLayoutParams(params);
        wheel.setBackgroundColor(getResources().getColor(R.color.light_white));
        loadDialog.setView(view);
        loadDialog.setBackgroundResource(getResources().getColor(R.color.transparent));
        loadDialog.show();
    }

    public void dimissDialog() {
        if (loadDialog != null) {
            loadDialog.dismiss();
        }
    }

}
