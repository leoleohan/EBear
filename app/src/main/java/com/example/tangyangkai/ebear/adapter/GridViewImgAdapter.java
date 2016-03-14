package com.example.tangyangkai.ebear.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.tangyangkai.ebear.R;
import com.example.tangyangkai.ebear.utils.Bimp;
import com.example.tangyangkai.ebear.utils.HankkinUtils;
import com.example.tangyangkai.ebear.utils.UiUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Hankkin on 2015/6/30.
 */
@SuppressLint("HandlerLeak")
public class GridViewImgAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private Context context;
    private List<String> data = new ArrayList<>();


    public GridViewImgAdapter(Context context, List<String> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }


    public int getCount() {
        return (data.size());
    }


    public Object getItem(int arg0) {
        return arg0;
    }

    public long getItemId(int arg0) {
        return arg0;
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.grid_img,
                    parent, false);

            holder = new ViewHolder();
            holder.image = (ImageView) convertView
                    .findViewById(R.id.iv_item);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String url = data.get(position).trim();
        ViewGroup.LayoutParams params = holder.image.getLayoutParams();
        params.width = HankkinUtils.getScreenWidth(context) / 3;

        params.height = params.width;
        holder.image.setLayoutParams(params);
        if (holder.image != null) {
            ImageLoader.getInstance().displayImage(url, holder.image);
        }


        return convertView;
    }

    public class ViewHolder {
        public ImageView image;
    }
}