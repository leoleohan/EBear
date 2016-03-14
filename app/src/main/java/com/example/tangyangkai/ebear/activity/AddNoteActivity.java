package com.example.tangyangkai.ebear.activity;

import android.Manifest;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bmob.BTPFileResponse;
import com.bmob.BmobProFile;
import com.example.tangyangkai.ebear.R;
import com.example.tangyangkai.ebear.adapter.PictureAdapter;
import com.example.tangyangkai.ebear.circlebutton.CircularProgressButton;
import com.example.tangyangkai.ebear.model.ImageBean;
import com.example.tangyangkai.ebear.model.Note;
import com.example.tangyangkai.ebear.model.Person;
import com.example.tangyangkai.ebear.utils.Bimp;
import com.example.tangyangkai.ebear.utils.BitmapUtils;
import com.example.tangyangkai.ebear.utils.DateDialog;
import com.example.tangyangkai.ebear.utils.FileUtils;
import com.example.tangyangkai.ebear.utils.GetDate;
import com.example.tangyangkai.ebear.utils.SelectPicPopupWindow;
import com.example.tangyangkai.ebear.utils.TimeDialog;
import com.example.tangyangkai.ebear.utils.UiUtil;
import com.example.tangyangkai.ebear.view.NoScrollGridView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadBatchListener;

public class AddNoteActivity extends AppCompatActivity {


    private Context context;
    private LocationManager locationManager;
    private String provider;

    @Bind(R.id.title_tv)
    TextView titleTv;
    @Bind(R.id.back_img)
    ImageView backImg;
    @Bind(R.id.add_date_tv)
    TextView dateTv;
    @Bind(R.id.add_time_tv)
    TextView timeTv;
    @Bind(R.id.add_address_tv)
    EditText addressTv;
    @Bind(R.id.add_save_btn)
    CircularProgressButton addSaveBtn;
    @Bind(R.id.add_gv)
    NoScrollGridView addGv;
    @Bind(R.id.add_note_tv)
    EditText addNoteEt;

    private PictureAdapter adapter;
    private SelectPicPopupWindow menuWindow;
    private AddNoteActivity instance;
    private String filepath;
    //公共静态bitmap
    public static Bitmap bitmap;
    private static final int TAKE_PICTURE = 0;
    private String[] paths;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        ButterKnife.bind(this);
        instance = this;
        context = this;
        initviews();
    }

    private void initviews() {
        titleTv.setText("E-Bear一下");
        dateTv.setText(GetDate.lastDay());
        dateTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DateDialog dialog = new DateDialog(context);
                dialog.setDate(0, dateTv);
            }
        });
        timeTv.setText(GetDate.currentDayTime());
        timeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimeDialog dialog = new TimeDialog(context);
                dialog.setTime(0, timeTv);
            }
        });


        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        provider = LocationManager.NETWORK_PROVIDER;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = locationManager.getLastKnownLocation(provider);
        if (location != null) {
            addressTv.setText(getLocationAddress(location.getLatitude(), location.getLongitude()));
        }


        addSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                simulateSuccessProgress(addSaveBtn);
                if (Bimp.tempSelectBitmap != null && Bimp.tempSelectBitmap.size() > 0) {
                    final Person person = BmobUser.getCurrentUser(AddNoteActivity.this, Person.class);

                    final Note note = new Note();
                    note.setUserId(person.getObjectId());
                    note.setUser_icon(person.getUser_icon());
                    note.setNickname(person.getNickname());
                    note.setNote(addNoteEt.getText().toString().trim());
                    note.setAddress(addressTv.getText().toString().trim());
                    note.setTime(dateTv.getText().toString() + "  " + timeTv.getText().toString());
                    int size = Bimp.tempSelectBitmap.size();
                    paths = new String[size];
                    for (int i = 0; i < size; i++) {
                        paths[i] = Bimp.tempSelectBitmap.get(i).getPath();
                    }
                    Bmob.uploadBatch(context, paths, new UploadBatchListener() {

                        @Override
                        public void onSuccess(List<BmobFile> files, List<String> urls) {
                            // TODO Auto-generated method stub
                            //1、files-上传完成后的BmobFile集合，是为了方便大家对其上传后的数据进行操作，例如你可以将该文件保存到表中
                            //2、urls-上传文件的服务器地址
                            if (urls.size() == paths.length) {
                                note.setImgs(urls.toString());
                                note.save(context, new SaveListener() {
                                    @Override
                                    public void onSuccess() {
                                        UiUtil.showToast(context, "上传成功");
                                        finish();
                                    }

                                    @Override
                                    public void onFailure(int i, String s) {

                                    }
                                });
                            }

                        }

                        @Override
                        public void onError(int statuscode, String errormsg) {
                            // TODO Auto-generated method stub
                        }

                        @Override
                        public void onProgress(int curIndex, int curPercent, int total, int totalPercent) {
                            // TODO Auto-generated method stub
                            //1、curIndex--表示当前第几个文件正在上传
                            //2、curPercent--表示当前上传文件的进度值（百分比）
                            //3、total--表示总的上传文件数
                            //4、totalPercent--表示总的上传进度（百分比）
                        }
                    });
                } else {
                    Person person = BmobUser.getCurrentUser(AddNoteActivity.this, Person.class);
                    Note note = new Note();
                    note.setUserId(person.getObjectId());
                    note.setUser_icon(person.getUser_icon());
                    note.setNickname(person.getNickname());
                    note.setNote(addNoteEt.getText().toString().trim());
                    note.setAddress(addressTv.getText().toString().trim());
                    note.setTime(dateTv.getText().toString() + "  " + timeTv.getText().toString());
                    note.save(context, new SaveListener() {

                        @Override
                        public void onSuccess() {
                            UiUtil.showToast(context, "发布成功");
                        }

                        @Override
                        public void onFailure(int code, String arg0) {
                            UiUtil.showToast(context, "发布失败");
                        }
                    });
                }
            }
        });


        adapter = new PictureAdapter(this);
        addGv.setSelector(new ColorDrawable(Color.TRANSPARENT));
        addGv.setAdapter(adapter);
        addGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == Bimp.getTempSelectBitmap().size()) {
                    selectImgs();
                } else {
                    Intent intent = new Intent(instance,
                            GalleryActivity.class);
                    intent.putExtra("ID", i);
                    startActivity(intent);
                }
            }
        });

    }

    private void selectImgs() {

        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(instance.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        menuWindow = new SelectPicPopupWindow(AddNoteActivity.this, itemsOnClick);
        //设置弹窗位置
        menuWindow.showAtLocation(AddNoteActivity.this.findViewById(R.id.llImage), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    private View.OnClickListener itemsOnClick = new View.OnClickListener() {
        public void onClick(View v) {
            menuWindow.dismiss();
            switch (v.getId()) {
                case R.id.item_popupwindows_camera:        //点击拍照按钮
                    goCamera();
                    break;
                case R.id.item_popupwindows_Photo:       //点击从相册中选择按钮
                    Intent intent = new Intent(instance,
                            AlbumActivity.class);
                    startActivity(intent);
                    break;
                default:
                    break;
            }
        }

    };

    private void goCamera() {
        filepath = FileUtils.iniFilePath(instance);
        File file = new File(filepath);
        // 启动Camera
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        startActivityForResult(intent, TAKE_PICTURE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PICTURE:
                if (Bimp.tempSelectBitmap.size() < 4 && resultCode == RESULT_OK) {

                    String fileName = String.valueOf(System.currentTimeMillis());

                    Bitmap bm = BitmapUtils.getCompressedBitmap(instance, filepath);
                    FileUtils.saveBitmap(bm, fileName);

                    ImageBean takePhoto = new ImageBean();
                    takePhoto.setBitmap(bm);
                    takePhoto.setPath(filepath);
                    Bimp.tempSelectBitmap.add(takePhoto);
                }
                break;
        }
    }


    //转为地址信息
    private String getLocationAddress(double lat, double lng) {
        String add = " ";
        String addPro = " ";
        Geocoder geoCoder = new Geocoder(this.getBaseContext(),
                Locale.getDefault());
        try {
            List<Address> addresses = geoCoder.getFromLocation(lat, lng, 1);
            if (addresses.size() != 0) {
                Address address = addresses.get(0);

                if (address.getAdminArea().equals(address.getLocality())) {
                    addPro = address.getAdminArea();
                } else {
                    addPro = address.getAdminArea() + " "
                            + address.getLocality();
                }


                int maxLine = address.getMaxAddressLineIndex();
                if (maxLine >= 2) {
                    add = address.getAddressLine(1) + " "
                            + address.getAddressLine(2);
                } else {
                    add = address.getAddressLine(1);
                }
            } else {
                add = "失败";
            }

        } catch (IOException e) {
            add = "异常";
            e.printStackTrace();
        }
        return addPro + " " + add;
    }

    private void simulateSuccessProgress(final CircularProgressButton button) {
        ValueAnimator widthAnimation = ValueAnimator.ofInt(1, 100);
        widthAnimation.setDuration(5000);
        widthAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        widthAnimation
                .addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        Integer value = (Integer) animation.getAnimatedValue();
                        button.setProgress(value);
                        if (value == 100) {
                            addSaveBtn.setProgress(0);
                        }

                    }
                });
        widthAnimation.start();
    }
}
