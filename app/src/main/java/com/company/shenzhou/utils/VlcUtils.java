package com.company.shenzhou.utils;

import java.util.Formatter;
import java.util.Locale;

/**
 * LoveLin
 * <p>
 * Describe  vlc 相关操作工具类
 */
public class VlcUtils {

    /**
     * 将长度转换为时间
     *
     * @param timeMs
     * @return
     */
    public static String stringForTime(int timeMs) {
        StringBuilder mFormatBuilder = new StringBuilder();
        Formatter mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
        int totalSeconds = timeMs / 1000;
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;
        mFormatBuilder.setLength(0);
        if (hours > 0) {
            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }


}
