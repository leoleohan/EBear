package com.example.tangyangkai.ebear.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.tangyangkai.ebear.R;
import com.example.tangyangkai.ebear.activity.AddNoteActivity;
import com.example.tangyangkai.ebear.activity.MainActivity;
import com.example.tangyangkai.ebear.activity.NoteDetailActivity;
import com.example.tangyangkai.ebear.adapter.NoteAdapter;
import com.example.tangyangkai.ebear.model.MyAttention;
import com.example.tangyangkai.ebear.model.Note;
import com.example.tangyangkai.ebear.model.Person;
import com.example.tangyangkai.ebear.myinterface.ScrollDirectionListener;
import com.example.tangyangkai.ebear.utils.UiUtil;
import com.example.tangyangkai.ebear.view.RefreshLayout;
import com.example.tangyangkai.ebear.view.floatbutton.FloatingActionButton;
import com.example.tangyangkai.ebear.view.floatbutton.FloatingActionsMenu;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;


public class HomeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, RefreshLayout.OnLoadListener {

    private NoteAdapter adapter;
    private List<Note> noteList = new ArrayList<>();
    private int x, y;
    private Context context;
    @Bind(R.id.home_rl)
    RefreshLayout swipeLayout;
    @Bind(R.id.home_lv)
    ListView listview;
    private FloatingActionsMenu fab;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context = getActivity();
        initviews();

    }

    private void initviews() {
        swipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        swipeLayout.setChildView(listview);
        adapter = new NoteAdapter(context);
        listview.setAdapter(adapter);
        getNotes();
        setListener();


        //设置滑动禁止加载图片
        listview.setOnScrollListener(new PauseOnScrollListener(ImageLoader.getInstance(), true, false));
        //设置滑动时隐藏悬浮按钮
        fab = MainActivity.instance.getFloatingActionsMenu();
        if (fab != null) {
            fab.attachToListView(listview, new ScrollDirectionListener() {
                @Override
                public void onScrollDown() {
                    Log.d("ListViewFragment", "onScrollDown()");
                }

                @Override
                public void onScrollUp() {
                    Log.d("ListViewFragment", "onScrollUp()");
                }
            }, new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {
                    Log.d("ListViewFragment", "onScrollStateChanged()");
                }

                @Override

                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                }
            });
        }



        fab.getChildAt(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UiUtil.showToast(context,"已刷新");
                getNotes();
                fab.toggle();
            }
        });



        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                noteList.get(i);
                Intent intent = new Intent(getActivity(), NoteDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("note", (Serializable) noteList.get(i));
                intent.putExtras(bundle);
                startActivity(intent);
        }
        });


    }


    private void setListener() {
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setOnLoadListener(this);


    }

    //上拉加载
    @Override
    public void onLoad() {
        BmobQuery<Note> query = new BmobQuery<Note>();
        query.order("-createdAt");// 按照时间降序
        //跳过前面已加载数据进行查询
        y = 8 + x;
        query.setSkip(y);
        Log.e("***", String.valueOf(y));
        Log.e("**!!!*", String.valueOf(x));
        //每次加载四条数据
        query.setLimit(8);

        query.findObjects(context, new FindListener<Note>() {
            @Override
            public void onSuccess(List<Note> list) {


                if (list.size() == 0) {
                    UiUtil.showToast(context, "没有更多的数据可以显示了");
                    //更新数据以后结束刷新
                    swipeLayout.setLoading(false);
                } else {
                    for (int i = 0; i < list.size(); i++) {
                        x = list.size();
                        noteList.add(list.get(i));
                        adapter.setNotes(noteList);
                        adapter.notifyDataSetChanged();

                        //更新数据以后结束刷新
                        swipeLayout.setLoading(false);
                    }
                }


            }

            @Override
            public void onError(int i, String s) {
                UiUtil.showToast(context, "查询失败");
            }
        });
    }

    //下拉刷新
    @Override
    public void onRefresh() {
        noteList.clear();
        getNotes();
    }

    private void getNotes() {
        BmobQuery<Note> query = new BmobQuery<Note>();

        query.order("-createdAt");// 按照时间降序
        //每次加载四条数据
        query.setLimit(8);

        query.findObjects(context, new FindListener<Note>() {
            @Override
            public void onSuccess(List<Note> list) {

                noteList.clear();
                y = 8;
                x = 0;
                for (int i = 0; i < list.size(); i++) {

                    noteList.add(list.get(i));
                }
                adapter.setNotes(list);
                adapter.notifyDataSetChanged();
                //更新数据以后结束刷新
                swipeLayout.setRefreshing(false);
            }

            @Override
            public void onError(int i, String s) {
                UiUtil.showToast(context, "查询失败");
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        noteList.clear();
        getNotes();
    }
}
