package com.example.tangyangkai.ebear.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tangyangkai.ebear.R;
import com.example.tangyangkai.ebear.model.Note;
import com.example.tangyangkai.ebear.model.Person;
import com.example.tangyangkai.ebear.view.RoundImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/3/10.
 */
public class MyAttentionAdapter extends BaseAdapter {


    public interface  deleteMyAttention{
        public void deleteAttention(int position);
    }




    public class ViewHolder {
        @Bind(R.id.attention_user_img)
        public RoundImageView userImg;
        @Bind(R.id.attention_nickname_tv)
        public TextView nickname;
        @Bind(R.id.attention_age_tv)
        public TextView age;
        @Bind(R.id.attention_sex_img)
        public ImageView sexImg;



    }

    private Context context;
    private LayoutInflater layoutinflater;
    private List<Person> persons;
    private deleteMyAttention deleteMyAttention;

    public MyAttentionAdapter(Context context) {
        this.context = context;
        this.layoutinflater = LayoutInflater.from(context);
    }


    public void setDeleteMyAttention(MyAttentionAdapter.deleteMyAttention deleteMyAttention) {
        this.deleteMyAttention = deleteMyAttention;
    }

    public void setPersons(List<Person> persons) {
        this.persons = persons;
    }

    @Override
    public int getCount() {
        if (persons!=null) {
            return persons.size();
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
            convertView = layoutinflater.inflate(R.layout.lv_item_myattention,
                    null);
            viewHolder = new ViewHolder();
            ButterKnife.bind(viewHolder, convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Person person = persons.get(position);
        viewHolder.nickname.setText(person.getNickname());
        viewHolder.age.setText(String.valueOf(person.getAge()));
        String sex=person.getSex();
        if(sex.equals("男")){
            viewHolder.sexImg.setBackground(context.getResources().getDrawable(R.drawable.man_focus));
        }else{
            viewHolder.sexImg.setBackground(context.getResources().getDrawable(R.drawable.woman_focus));
        }
        ImageLoader.getInstance().displayImage(person.getUser_icon(), viewHolder.userImg);



        return convertView;
    }
}
