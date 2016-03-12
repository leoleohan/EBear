package com.example.tangyangkai.ebear.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.tangyangkai.ebear.R;
import com.example.tangyangkai.ebear.model.Note;
import com.example.tangyangkai.ebear.view.RoundImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

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


    }

    private Context context;
    private LayoutInflater layoutinflater;
    private List<Note> notes;

    public NoteAdapter(Context context) {
        this.context = context;
        this.layoutinflater = LayoutInflater.from(context);
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }

    @Override
    public int getCount() {
        if (notes!=null) {
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

        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = layoutinflater.inflate(R.layout.lv_item_note,
                    null);
            viewHolder = new ViewHolder();
            ButterKnife.bind(viewHolder, convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Note note = notes.get(position);
        ImageLoader.getInstance().displayImage(note.getUser_icon(), viewHolder.userImg);
        viewHolder.address.setText(note.getAddress());
        viewHolder.nickname.setText(note.getNickname());
        viewHolder.note.setText(note.getNote());
        viewHolder.time.setText(note.getTime());
        return convertView;
    }
}
