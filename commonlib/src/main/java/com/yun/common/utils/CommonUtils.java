package com.yun.common.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.inputmethod.InputMethodManager;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CommonUtils {

    /**
     * 跳转到权限设置界面
     */
    public static void getAppDetailSettingIntent(Context context) {
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT >= 9) {
            intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(Uri.fromParts("package", context.getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            intent.setAction(Intent.ACTION_VIEW);
            intent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            intent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
        }
        context.startActivity(intent);
    }

    /**
     * 获得屏幕宽度
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Activity context) {
        DisplayMetrics metric = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(metric);
        return metric.widthPixels;     // 屏幕宽度（像素
    }

    public static void closeKeyboard(Activity activity) {
        //拿到InputMethodManager
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        //如果window上view获取焦点 && view不为空
        if (imm.isActive() && activity.getCurrentFocus() != null) {
            //拿到view的token 不为空
            if (activity.getCurrentFocus().getWindowToken() != null) {
                //表示软键盘窗口总是隐藏，除非开始时以SHOW_FORCED显示。
                imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    /**
     * 获得屏幕宽度
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Activity context) {
        DisplayMetrics metric = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(metric);
        return metric.heightPixels;     // 屏幕宽度（像素
    }

    /**
     * 手机号有效判断
     * Return true if it's valid
     */
    public static boolean checkPhoneNumberValid(String phoneNumber) {
        boolean isValid = false;
        String expression = "[1][3578][0-9]{9}";

        if (phoneNumber.matches(expression)) {
            isValid = true;
        }

        return isValid;
    }


    /**
     * 检查email地址是否有效
     *
     * @param email
     * @return Return true if it's valid
     */
    public static boolean checkEmailValid(String email) {
        boolean isValid = false;
        String expression = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";

        if (email.matches(expression)) {
            isValid = true;
        }

        return isValid;
    }

    /**
     * @param num
     * @return Return true if it's valid
     */
    public static boolean checkNumberValid(String num) {
        boolean isValid = false;
        String expression = "[0-9]*";

        if (num.matches(expression)) {
            isValid = true;
        }

        return isValid;
    }

    /**
     * 检查QQ号码是否有效
     *
     * @param num
     * @return Return true if it's valid
     */
    public static boolean checkQQNumberValid(String num) {
        boolean isValid = false;
        String expression = "[0-9]{5,11}";

        if (num.matches(expression)) {
            isValid = true;
        }

        return isValid;
    }

    /**
     * 将读取图片的方向
     *
     * @param path
     * @return int
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 根据bitmap的方向旋转
     *
     * @param angle ，bitmap
     * @return Bitmap
     */

    public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
        // 旋转图片 动作
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizedBitmap;
    }

    /**
     * 把bitmap转成圆形
     */
    public static Bitmap toRoundBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int r = 0;

        if (width < height) {
            r = width;
        } else {
            r = height;
        }
        //构建一个bitmap
        Bitmap backgroundBm = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        //new一个Canvas，在backgroundBmp上画图
        Canvas canvas = new Canvas(backgroundBm);
        Paint p = new Paint();
        //设置边缘光滑，去掉锯齿
        p.setAntiAlias(true);
        RectF rect = new RectF(0, 0, r, r);
        //通过制定的rect画一个圆角矩形，当圆角X轴方向的半径等于Y轴方向的半径时，
        //且都等于r/2时，画出来的圆角矩形就是圆形
        canvas.drawRoundRect(rect, r / 2, r / 2, p);
        //设置当两个图形相交时的模式，SRC_IN为取SRC图形相交的部分，多余的将被去掉
        p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        //canvas将bitmap画在backgroundBmp上
        canvas.drawBitmap(bitmap, null, rect, p);
        return backgroundBm;
    }

    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static int px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * @return String 变更日期显示格式方法，显示时分秒
     * <p>
     * 一周内不是昨天和今天的显示星期
     ****/

    public static String changeTimeFormat(String format, String objectTime) {
        String ret = "";
        String day = "";
        try {
            String[] weekDaysName = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五",
                    "星期六"};
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            Date nowDate = new Date();
            Calendar now = Calendar.getInstance();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(sdf.parse(objectTime));
            int intWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
            day = weekDaysName[intWeek];
            long create = sdf.parse(objectTime).getTime();
            long ms = 1000 * (now.get(Calendar.HOUR_OF_DAY) * 3600
                    + now.get(Calendar.MINUTE) * 60 + now.get(Calendar.SECOND));// 毫秒数
            long ms_now = now.getTimeInMillis();
            if (ms_now - create < ms) {
                ret = msgTimeFormate(objectTime);
            } else if (ms_now - create < (ms + 24 * 3600 * 1000)) {
                ret = "昨天" + objectTime.substring(10);
            } else if (isSameWeekDates(nowDate, sdf.parse(objectTime))) {
                ret = day + objectTime.substring(10);
            } else {
                ret = objectTime;
            }
            return ret;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    /**
     * 判断两个日期是否在同一周
     *
     * @param date1
     * @param date2
     * @return
     */
    public static boolean isSameWeekDates(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(date1);
        cal2.setTime(date2);
        int subYear = cal1.get(Calendar.YEAR) - cal2.get(Calendar.YEAR);
        if (0 == subYear) {
            if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2
                    .get(Calendar.WEEK_OF_YEAR))
                return true;
        } else if (1 == subYear && 11 == cal2.get(Calendar.MONTH)) {
            // 如果12月的最后一周横跨来年第一周的话则最后一周即算做来年的第一周
            if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2
                    .get(Calendar.WEEK_OF_YEAR))
                return true;
        } else if (-1 == subYear && 11 == cal1.get(Calendar.MONTH)) {
            if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2
                    .get(Calendar.WEEK_OF_YEAR))
                return true;
        }
        return false;
    }

    /**
     * @param objectTime
     * @return String
     * @Title: msgTimeFormate
     * @Description: yyyy-MM-dd HH:mm:ss 转为HH:mm
     */
    public static String msgTimeFormate(String objectTime) {
        String ret = "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat sdfTiem = new SimpleDateFormat("HH:mm");
            ret = sdfTiem.format(sdf.parse(objectTime));
        } catch (Exception e) {
            LogUtils.e("====msgTimeFormate====e==" + e.toString());
        }
        return ret;
    }

    public static void call(Activity activity, String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phone);
        intent.setData(data);
        activity.startActivity(intent);
    }

    public static String formatDecimal(String format, double value) {
        DecimalFormat df = new DecimalFormat(format);
        return df.format(value);
    }

    /**
     * 获取当前应用版本名
     *
     * @param context 上下文
     * @return 版本名
     */
    public static String getVersionName(Context context) {
        PackageManager packageManager = context.getPackageManager();
        String version = "";
        try {
            PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            version = packInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;

    }

    /**
     * 获取当前应用版本号
     *
     * @param context 上下文
     * @return 版本号
     */
    public static int getVersionCode(Context context) {
        PackageManager packageManager = context.getPackageManager();
        int versionCode = 0;
        try {
            PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            versionCode = packInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

}

