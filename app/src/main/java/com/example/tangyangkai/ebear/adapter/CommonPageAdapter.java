package com.example.tangyangkai.ebear.adapter;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

public class CommonPageAdapter extends PagerAdapter {
	private List<View> listDatas;

	public CommonPageAdapter(List<View> listDatas) {
		this.listDatas = listDatas;
	}

	@Override
	public int getCount() {
		return listDatas == null ? 0 : listDatas.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		View item = listDatas.get(position);
		container.addView(item);
		return item;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView(listDatas.get(position));
	}
}
