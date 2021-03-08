package com.company.shenzhou.ui.activity.login;


import android.content.Intent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.company.shenzhou.R;
import com.company.shenzhou.base.BaseActivity;
import com.company.shenzhou.bean.Constants;
import com.company.shenzhou.ui.activity.MainActivity;
import com.google.gson.Gson;
import com.yun.common.utils.SharePreferenceUtil;
import com.yun.common.utils.StatusBarUtil;
import com.yun.common.utils.StatusBarUtils;

/**
 * Created by Lovelin on 2019/3/27
 * <p>
 * Describe:启动页
 */
public class SplashActivity extends BaseActivity {
    private Boolean isFirstIn;
    private ImageView ivSplash;
    private Boolean isLogined;
    private Gson mGson = new Gson();

    @Override
    public int getContentViewId() {
        return R.layout.activity_splash;
    }

    @Override
    public void init() {
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    public void initView() {
//        StatusBarUtils.setColor(this, getResources().getColor(R.color.color_transparent), 0);
//        StatusBarUtils.setColor(this, getResources().getColor(R.color.color_7BBE7B), 0);
        StatusBarUtils.setColor(this, getResources().getColor(R.color.white), 0);
        StatusBarUtil.darkMode(this, true);  //设置了状态栏文字的颜色
        setTitleBarVisibility(View.GONE);
        ivSplash = findViewById(R.id.iv_splash);
        //是否第一次进入app
        isFirstIn = (Boolean) SharePreferenceUtil.get(this, Constants.SP_IS_FIRST_IN, true);
        //是否登入
        isLogined = (Boolean) SharePreferenceUtil.get(this, Constants.Is_Logined, false);
        // 从浅到深,从百分之10到百分之百
        AlphaAnimation aa = new AlphaAnimation(0.3f, 1.0f);
        aa.setDuration(1500);// 设置动画时间
        ivSplash.setAnimation(aa);// 给image设置动画
        aa.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                initData();

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    public void initData() {
        switchGoing();
    }

    //判断进入那个activity
    private void switchGoing() {
//            Intent intent = new Intent();
//            if (!isLogined) {  //登入成功 ,false==未登录
//                intent.setClass(SplashActivity.this, LoginAnimatorActivity.class);
//            } else {   //已经登陆
//                intent.setClass(SplashActivity.this, MainActivity.class);
//            }
//            startActivity(intent);
//            finish();

        if (isFirstIn) {
            SharePreferenceUtil.put(SplashActivity.this, Constants.SP_IS_FIRST_IN, true);
            Intent intent = new Intent();
            intent.setClass(SplashActivity.this, GuideActivity.class);
            startActivity(intent);
            finish();
        } else {  //不是第一次进App,判断是否登陆过
            Intent intent = new Intent();
            if (!isLogined) {  //登入成功 ,false==未登录
                intent.setClass(SplashActivity.this, LoginAnimatorActivity.class);
            } else {   //已经登陆
                intent.setClass(SplashActivity.this, MainActivity.class);
            }
            startActivity(intent);
            finish();
        }

    }
}
