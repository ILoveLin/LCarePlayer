package com.yun.common.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yun.common.R;


/**
 * 作者：xujm
 * 时间：2015/12/29
 * 备注：Toast工具类
 */
public class ToastUtil {
    public static boolean isShow = true;
    /*
     *Toast点击多次处理，参考文章
     *http://blog.csdn.net/guolin_blog/article/details/51336415#rd
     * */
    private static Toast toast;

    /**
     * 短时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showShort(Context context, CharSequence message) {
        if (isShow)
            Toast.makeText(context.getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    /**
     * 短时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showShort(Context context, int message) {
        if (isShow)
            Toast.makeText(context.getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    /**
     * 长时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showLong(Context context, CharSequence message) {
        if (isShow)
            Toast.makeText(context.getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    /**
     * 长时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showLong(Context context, int message) {
        if (isShow)
            Toast.makeText(context.getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    /**
     * 自定义显示Toast时间
     *
     * @param context
     * @param message
     * @param duration
     */
    public static void show(Context context, CharSequence message, int duration) {
        if (isShow)
            Toast.makeText(context, message, duration).show();
    }

    /**
     * 自定义显示Toast时间
     *
     * @param context
     * @param message
     * @param duration
     */
    public static void show(Context context, int message, int duration) {
        if (isShow)
            Toast.makeText(context, message, duration).show();
    }

    /**
     * 自定义显示位置
     */
    public static void showGravity(Context context, String message) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }


    //toast多次点击处理
    public static void showToast(Context context, String content) {
        if (toast == null) {
            toast = Toast.makeText(context.getApplicationContext(),
                    content,
                    Toast.LENGTH_SHORT);
        } else {
            toast.setText(content);
        }
        toast.show();
    }

    /**
     * 居中弹出
     */
    public static void showToastCenter(Context a, String message) {
//        Context context= App.getInstance().getApplicationContext();
        LinearLayout layout = (LinearLayout) View.inflate(a, R.layout.toast_custom_text, null);
        TextView toastTextview = ((TextView) layout.findViewById(R.id.toast_tv));
        if(toast==null){
            toast = new Toast(a);
        }
        toast.setGravity(Gravity.CENTER, 0, 0);
        toastTextview.setText(message);
        toast.setView(layout);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }

}
