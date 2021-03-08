package com.yun.common.utils;

import android.content.Context;
import android.widget.Toast;

public class CustomToast {

	private static Toast mToast;
	public static void showToast(Context context, String msg){
		if(mToast != null){
			mToast.setText(msg);
		}else{
			mToast = Toast.makeText(context.getApplicationContext(), msg, Toast.LENGTH_SHORT);
		}
		mToast.show();
	}

	public static void showToast(Context context, String msg, int duration){
		if(mToast != null){
			mToast.setText(msg);
		}else{
			mToast = Toast.makeText(context.getApplicationContext(), msg, duration);
		}
		mToast.show();
	}

//	public static void showToast(StringCallback context, String id){
//		if(mToast != null){
//			mToast.setText(id);
//		}else{
//			mToast = Toast.makeText(context.getApplicationContext(), id, Toast.LENGTH_SHORT);
//		}
//		mToast.show();
//	}

	public static void showLongToast(Context context, String msg){
		if(mToast != null){
			mToast.setText(msg);
		}else{
			mToast = Toast.makeText(context.getApplicationContext(), msg, Toast.LENGTH_LONG);
		}
		mToast.show();
	}
}
