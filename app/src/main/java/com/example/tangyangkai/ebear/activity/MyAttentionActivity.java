package com.example.tangyangkai.ebear.activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.tangyangkai.ebear.R;
import com.example.tangyangkai.ebear.adapter.MyAttentionAdapter;
import com.example.tangyangkai.ebear.model.MyAttention;
import com.example.tangyangkai.ebear.model.Person;
import com.example.tangyangkai.ebear.utils.UiUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;

public class MyAttentionActivity extends AppCompatActivity {


    private Context context;
    @Bind(R.id.title_tv)
    TextView titleTv;
    @Bind(R.id.back_img)
    ImageView backImg;
    @Bind(R.id.attention_lv)
    ListView listview;
    private MyAttentionAdapter adapter;
    private List<Person> personList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_attention);
        ButterKnife.bind(this);
        context = this;
        initviews();
        getMyAttentions();
    }


    private void initviews() {
        titleTv.setText("我的关注");
        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        adapter = new MyAttentionAdapter(context);
        listview.setAdapter(adapter);
    }

    private void getMyAttentions() {
        Person person = BmobUser.getCurrentUser(context, Person.class);
        BmobQuery<MyAttention> query = new BmobQuery<MyAttention>();
        query.addWhereEqualTo("userId", person.getObjectId());
        query.findObjects(context, new FindListener<MyAttention>() {
                    @Override
                    public void onSuccess(List<MyAttention> list) {


                        if (list.size() == 0) {
                            UiUtil.showToast(context, "还没有添加关注呢");
                        }


                        for (int i = 0; i < list.size(); i++) {
                            Log.e("**", String.valueOf(list.size()));
                            BmobQuery<Person> personQuery = new BmobQuery<Person>();
                            MyAttention attention=new MyAttention();
                            attention=list.get(i);
                            personQuery.addWhereEqualTo("objectId", attention.getAtentionId());

                            personQuery.findObjects(context, new FindListener<Person>() {
                                @Override
                                public void onSuccess(List<Person> list) {
                                    for(int i=0;i<list.size();i++){
                                        personList.add(list.get(i));
                                    }
                                    adapter.setPersons(personList);
                                    adapter.notifyDataSetChanged();
                                }

                                @Override
                                public void onError(int i, String s) {

                                }
                            });


                        }


                    }

                    @Override
                    public void onError(int i, String s) {

                    }


                }

        );


    }

    @Override
    protected void onResume() {
        super.onResume();
        getMyAttentions();
    }
}
