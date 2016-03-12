package com.example.tangyangkai.ebear.utils;

import java.util.Calendar;



import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.tangyangkai.ebear.R;

public class DateDialog {
	private Calendar calendar; // 通过Calendar获取系统时间
	private Context context;

	public DateDialog(Context context) {
		this.context = context;

	}

	@SuppressLint("NewApi")
	public void setDate(final int type, final TextView text) {
		calendar = Calendar.getInstance();
		// 通过自定义控件AlertDialog实现
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		View view = (LinearLayout) LayoutInflater.from(context).inflate(
				R.layout.dialog_date, null);
		final DatePicker datePicker = (DatePicker) view
				.findViewById(R.id.date_picker);
		// 设置日期简略显示 否则详细显示 包括:星期周
		datePicker.setCalendarViewShown(false);
		// 初始化当前日期
		calendar.setTimeInMillis(System.currentTimeMillis());
		datePicker.init(calendar.get(Calendar.YEAR),
				calendar.get(Calendar.MONTH),
				calendar.get(Calendar.DAY_OF_MONTH), null);
		// 设置date布局
		builder.setView(view);
		builder.setTitle("设置日期信息");
		builder.setNegativeButton("确  定",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// 日期格式
						StringBuffer sb = new StringBuffer();
						sb.append(String.format("%d-%02d-%02d",
								datePicker.getYear(),
								datePicker.getMonth() + 1,
								datePicker.getDayOfMonth()));
						text.setText(sb);
						dialog.cancel();

					}

				});

		builder.setPositiveButton("取  消",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
		builder.create().show();
	}
}
