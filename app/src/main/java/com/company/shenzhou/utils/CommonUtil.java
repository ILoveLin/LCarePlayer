package com.company.shenzhou.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.FileUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;


import com.company.shenzhou.bean.dbbean.UserDBRememberBean;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by shuyu on 2016/11/11.
 */

public class CommonUtil {

    // 两次点击按钮之间的点击间隔不能少于1000毫秒
    private static final int MIN_CLICK_DELAY_TIME = 1000;
    private static long lastClickTime;

    public synchronized static boolean isFastClick() {
        boolean flag = false;
        long curClickTime = System.currentTimeMillis();
        if ((curClickTime - lastClickTime) >= MIN_CLICK_DELAY_TIME) {
            flag = true;
        }
        lastClickTime = curClickTime;
        return flag;
    }

    public static String getSaveDirectory() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return Environment.getExternalStorageDirectory().getAbsolutePath();
        } else {
            return null;
        }
    }

    public static void setViewHeight(View view, int width, int height) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (null == layoutParams)
            return;
        layoutParams.width = width;
        layoutParams.height = height;
        view.setLayoutParams(layoutParams);
    }

    public static void saveBitmap(Bitmap bitmap) throws FileNotFoundException {
        if (bitmap != null) {
            File file = new File(getPath(), "GSY-" + System.currentTimeMillis() + ".jpg");
            OutputStream outputStream;
            outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            bitmap.recycle();
        }
    }

    public static String getPath() {
        String path = getAppPath(NAME);
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path;
    }

    private static final String SD_PATH = Environment.getExternalStorageDirectory().getPath();
    public static final String NAME = "GSYVideo";

    public static String getAppPath(String name) {
        StringBuilder sb = new StringBuilder();
        sb.append(SD_PATH);
        sb.append(File.separator);
        sb.append(name);
        sb.append(File.separator);
        return sb.toString();
    }


    public static ArrayList<String> getNameList(List<UserDBRememberBean> list) {
        if (null != list) {
            ArrayList<String> nameList = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                String username = list.get(i).getUsername();
                nameList.add(username);
            }
            return nameList;
        }
        return new ArrayList<>();
    }
    /**
     * 打卡软键盘
     *
     * @param mEditText 输入框
     * @param mContext  上下文
     */
    public static void openKeybord(final EditText mEditText, final Context mContext) {

        //必须要等UI绘制完成之后，打开软键盘的代码才能生效，所以要设置一个延时
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) mContext
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(mEditText, InputMethodManager.RESULT_SHOWN);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
                        InputMethodManager.HIDE_IMPLICIT_ONLY);
            }
        }, 500);
    }

    /**
     * 关闭软键盘
     *
     * @param mEditText 输入框
     * @param mContext  上下文
     */
    public static void closeKeybord(EditText mEditText, Context mContext) {
        InputMethodManager imm = (InputMethodManager) mContext
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
    }
}
