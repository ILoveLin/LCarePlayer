package com.yun.common.utils.updateutils;

import android.os.Environment;
import android.os.StatFs;

/**
 * Created by 天哥哥 on 2017/2/10 0010.
 */

public class SDCardUtil {

    /**
     * 判断SDCard是否可用
     *
     * @return
     */
    public static boolean isSDCardEnable() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);

    }

    /**
     * 获取SD卡路径
     *
     * @return
     */
    public static String getSDCardPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath();

    }
    /**
     * 获取SD卡的总容量 单位byte
     *
     * @return
     */
    public static long getSDCardAllSize() {
        if (isSDCardEnable()) {
            StatFs stat = new StatFs(getSDCardPath());
            // 获取空闲的数据块的数量
            long count = stat.getBlockCount();
            // 获取单个数据块的大小（byte）
            long size = stat.getBlockSize();
            return size * count;
        }
        return 0;
    }
    /**
     * 获取SD卡的剩余容量 单位byte
     *
     * @return
     */
    public static long getSDCardFreeSize() {
        if (isSDCardEnable()) {
            StatFs stat = new StatFs(getSDCardPath());
            // 获取空闲的数据块的数量
            long availableBlocks = stat.getAvailableBlocks();
            // 获取单个数据块的大小（byte）
            long size = stat.getBlockSize();
            return size * availableBlocks;
        }
        return 0;
    }



}
