package com.company.shenzhou.ui.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import static android.net.ConnectivityManager.*;

/**
 * LoveLin
 * Describe wifi网络的添加
 */
public class ConnectionChangeReceiver extends BroadcastReceiver {

    private static final String TAG = "wifiReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(WifiManager.RSSI_CHANGED_ACTION)) {
            Log.i(TAG, "wifi信号强度变化");
        }
        //wifi连接上与否
        if (intent.getAction().equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
            NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
            if (info.getState().equals(NetworkInfo.State.DISCONNECTED)) {
                Log.i(TAG, "wifi断开");
                if (mListener != null) {
                    mListener.wifiOff();
                }
            } else if (info.getState().equals(NetworkInfo.State.CONNECTED)) {
                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                //获取当前wifi名称
                Log.i(TAG, "连接到网络 " + wifiInfo.getSSID());
//                TtsManager ttsManager = new TtsManager();
//                ttsManager.checkTtsJet(context.getApplicationContext());
            }
        }
        //wifi打开与否
        if (intent.getAction().equals(WifiManager.WIFI_STATE_CHANGED_ACTION)) {
            int wifistate = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_DISABLED);
            if (wifistate == WifiManager.WIFI_STATE_DISABLED) {
                Log.i(TAG, "系统关闭wifi");
                if (mListener != null) {
                    mListener.wifiOff();
                }
            } else if (wifistate == WifiManager.WIFI_STATE_ENABLED) {
                Log.i(TAG, "系统开启wifi");
                if (mListener != null) {
                    mListener.wifiOn();
                }
            }
        }
    }


//    @Override
//    public void onReceive(Context context, Intent intent) {
//        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo mobNetInfo = connectivityManager.getNetworkInfo(TYPE_MOBILE);
//        NetworkInfo wifiNetInfo = connectivityManager.getNetworkInfo(TYPE_WIFI);
//
//        if (!mobNetInfo.isConnected() && !wifiNetInfo.isConnected()) {
//            //网络不可用
//            if (mListener != null) {
//                mListener.wifiOff();
//            }
//        } else {
//            //网络可用
//            if (mListener != null) {
//                mListener.wifiOn();
//            }
//        }
//    }


    public interface onWifiListener {
        void wifiOff();

        void wifiOn();

    }

    private onWifiListener mListener;

    public void setOnWifiListener(onWifiListener mListener) {
        this.mListener = mListener;
    }


}