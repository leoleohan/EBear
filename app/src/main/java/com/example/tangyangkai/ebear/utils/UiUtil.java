package com.example.tangyangkai.ebear.utils;



import android.content.Context;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tangyangkai.ebear.R;
import com.example.tangyangkai.ebear.application.MyAppConstant;


public class UiUtil {

	
	public static boolean isResultSuccess(Context context, String result) {
		if (result == null || result.equals(MyAppConstant.OE)) {
			Toast.makeText(
					context,
					context.getResources()
							.getString(R.string.network_exception),
					Toast.LENGTH_SHORT).show();
			return false;
		} else if (result.equals(MyAppConstant.NE)) {
			Toast.makeText(context,
					context.getResources().getString(R.string.other_exception),
					Toast.LENGTH_SHORT).show();
			return false;
		} else {
			return true;
		}

	}
	public static boolean isEditTextNull(EditText e) {  
		if (TextUtils.isEmpty(e.getText().toString().trim())) {
			return true;
		} else {
			return false;
		}
	}

	public static void showToast(Context context, String msg) {
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}
	public static void showSendCmdResult(Context context,String result){
		String[] arr=result.split("\\|");
		String msg=arr[arr.length-1];
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}

	public static void showNetworkException(Context context) {
		Toast.makeText(
				context,
				context.getResources().getString(R.string.network_exception), Toast.LENGTH_SHORT)
				.show();
	}
	
	public static void showOtherException(Context context) {
		Toast.makeText(
				context,
				context.getResources().getString(R.string.other_exception), Toast.LENGTH_SHORT)
				.show();
	}
}
