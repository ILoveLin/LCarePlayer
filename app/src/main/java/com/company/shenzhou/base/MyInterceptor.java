package com.company.shenzhou.base;

import android.content.Context;
import android.util.Log;

import com.yun.common.utils.SharePreferenceUtil;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Lovelin on 2019/5/10
 * <p>
 * Describe:拦截器  添加header
 */
public class MyInterceptor implements Interceptor {
    private Context mContext;

    public MyInterceptor(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        String userid = (String) SharePreferenceUtil.get(mContext, "USERID", "");
        Log.e("Net", "login==GetUserID====调试头====" + userid + "");
        Request request = chain.request().newBuilder()
//                .addHeader("device", "android")
//                .addHeader("token", token)
                .addHeader("userid", userid)
                .build();

        return chain.proceed(request);

    }
}
