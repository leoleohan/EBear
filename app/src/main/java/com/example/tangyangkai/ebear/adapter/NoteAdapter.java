package com.example.tangyangkai.ebear.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.tangyangkai.ebear.R;
import com.example.tangyangkai.ebear.model.Note;
import com.example.tangyangkai.ebear.model.Person;
import com.example.tangyangkai.ebear.view.NoScrollGridView;
import com.example.tangyangkai.ebear.view.RoundImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobUser;

/**
 * Created by Administrator on 2016/3/10.
 */
public class NoteAdapter extends BaseAdapter {

    public class ViewHolder {
        @Bind(R.id.item_user_img)
        public RoundImageView userImg;
        @Bind(R.id.item_nickname_tv)
        public TextView nickname;
        @Bind(R.id.item_time_tv)
        public TextView time;
        @Bind(R.id.item_note_tv)
        public TextView note;
        @Bind(R.id.item_address_tv)
        public TextView address;
        @Bind(R.id.item_img_gv)
        public NoScrollGridView imgGridView;


    }

    private Context context;
    private LayoutInflater layoutinflater;
    private List<Note> notes = new ArrayList<>();
    private GridViewImgAdapter gridAdapter;

    public NoteAdapter(Context context) {
        this.context = context;
        this.layoutinflater = LayoutInflater.from(context);
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }

    @Override
    public int getCount() {
        if (notes != null) {
            return notes.size();
        } else {
            return 0;
        }

    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int id) {
        return id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        ViewHolder viewHolder = new ViewHolder();
        if (convertView == null) {
            convertView = layoutinflater.inflate(R.layout.lv_item_note,
                    null);
            viewHolder.imgGridView = (NoScrollGridView) convertView.findViewById(R.id.item_img_gv);
            ButterKnife.bind(viewHolder, convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Note note = notes.get(position);
        List<String> urls = new ArrayList<>();


        Person person = BmobUser.getCurrentUser(context, Person.class);


        if (note.getImgs() != null) {
            String imgUrls = note.getImgs().substring(1, note.getImgs().length() - 1);
            if (!TextUtils.isEmpty(imgUrls)) {
                String[] arr = imgUrls.split(",");
                urls = Arrays.asList(arr);
            }


            if (note.getUserId().equals(person.getObjectId())) {
                ImageLoader.getInstance().displayImage(person.getUser_icon(), viewHolder.userImg);
                viewHolder.nickname.setText(person.getNickname());
            } else {
                ImageLoader.getInstance().displayImage(note.getUser_icon(), viewHolder.userImg);
                viewHolder.note.setText(person.getNickname());
            }

            viewHolder.address.setText(note.getAddress());

            viewHolder.note.setText(note.getNote());
            viewHolder.time.setText(note.getTime());
            if (viewHolder.imgGridView != null) {
                gridAdapter = new GridViewImgAdapter(context, urls);
                viewHolder.imgGridView.setAdapter(gridAdapter);
            }
        } else {


            if (note.getUserId().equals(person.getObjectId())) {
                ImageLoader.getInstance().displayImage(person.getUser_icon(), viewHolder.userImg);
                viewHolder.nickname.setText(person.getNickname());
            } else {
                ImageLoader.getInstance().displayImage(note.getUser_icon(), viewHolder.userImg);
                viewHolder.note.setText(person.getNickname());
            }
            viewHolder.address.setText(note.getAddress());
            viewHolder.note.setText(note.getNote());
            viewHolder.time.setText(note.getTime());
        }


        return convertView;
    }
}
