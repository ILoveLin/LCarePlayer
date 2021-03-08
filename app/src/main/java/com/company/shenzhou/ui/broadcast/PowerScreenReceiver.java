package com.company.shenzhou.ui.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.yun.common.utils.LogUtils;

/**
 * LoveLin
 * <p>
 * Describe监听屏幕熄屏亮屏 的广播
 */
public class PowerScreenReceiver extends BroadcastReceiver {
    private static final String TAG = "PowerScreenReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_SCREEN_OFF.equals(intent.getAction())) {
            if (mListener != null) {
                mListener.screenOff();
            }
            LogUtils.e("receive screen off=======关闭了");
        } else if (Intent.ACTION_SCREEN_ON.equals(intent.getAction())) {
            LogUtils.e("receive screen on========打开了");
        }
    }

    public interface onScreenListener {
        void screenOff();

    }

    private onScreenListener mListener;

    public void setOnScreenListener(onScreenListener mListener) {
        this.mListener = mListener;
    }

}
