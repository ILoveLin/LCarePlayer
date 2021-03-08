package com.company.shenzhou.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.company.shenzhou.R;
import com.company.shenzhou.utils.NetworkUtil;
import com.company.shenzhou.view.pagestate.PageManager;
import com.yun.common.contant.Constants;
import com.yun.common.utils.KeyBoardUtils;
import com.yun.common.utils.StatusBarUtil;
import com.yun.common.utils.StatusBarUtils;
import com.yun.common.utils.ToastUtil;
import com.yun.common.utils.popupwindow.PopupWindowTwoButton;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Lovelin on 2019/4/27
 * <p>
 * Describe:activity的基类
 */
public abstract class BaseActivity extends AppCompatActivity {
    private String TAG = BaseActivity.class.getSimpleName();
    @BindView(R.id.ib_left)
    ImageButton mTitleLeftBtn;
    @BindView(R.id.ib_left_1)
    ImageButton mTitleRegisterLeftBtn;
    @BindView(R.id.tv_title)
    TextView mTitleTv;
    @BindView(R.id.tv_right)
    TextView mTitleRightTvBtn;
    @BindView(R.id.ib_right)
    ImageButton mTitleRightBtn;
    @BindView(R.id.fake_status_bar)
    View mFakeStatusBar;
    @BindView(R.id.rl_top)
    RelativeLayout mTitleLayout;
    @BindView(R.id.root_layout)
    LinearLayout rootLayout;
    private Context mContext;
    private Unbinder unbinder;
    private View mContentView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_layout);
        mContext = this;
        ActivityCollector.getInstance().addActivity(this);   // activity的管理类,管理全局的activity
        ViewGroup rootView = (ViewGroup) findViewById(R.id.root_layout);
        mContentView = LayoutInflater.from(this).inflate(getContentViewId(), rootView, false);
        rootView.addView(mContentView);
        unbinder = ButterKnife.bind(this);

        //设置状态栏
//        StatusBarUtils.setStatusTransparent(this, false);
//        rootView.setPadding(0, StatusBarUtils.getStatusBarHeight(this), 0, 0);

//        ViewGroup rootView = getWindow().getDecorView().findViewById(R.id.root_layout);
        //设置向下移动状态栏的高度
        //页面状态管理库 文档查看 https://github.com/hss01248/PageStateManager
        //这里不再初始化pageManager ，在需要添加pagerManager的页面手动调用 setPageStateView或者setCustomPageStateView
        //这样可以减少不需要loading或者error的页面添加，减少页面嵌套层级
//        PageManager.initInApp(getActivity().getApplicationContext());
//        mPageManager = PageManager.init(mContentView, false, new Runnable() {
//            @Override
//            public void run() {
//                onClickRetry();
//            }
//        });

//        StatusBarUtils.setColor(getActivity(), getResources().getColor(R.color.color_transparent), 0);
//        StatusBarUtil.darkMode(getActivity(), true);  //设置了状态栏文字的颜色
//        StatusBarUtils.setTranslucentForImageView(getActivity(), 0, relative_recommend);

//
        StatusBarUtils.setColor(this, getResources().getColor(R.color.white), 0);
        StatusBarUtil.darkMode(this, true);  //设置了状态栏文字的颜色
//        StatusBarUtils.setTranslucentForImageViewInFragment(this, rootLayout);
//        StatusBarUtils.setColor(this, getResources().getColor(R.color.color_transparent), 0);
//        StatusBarUtils.setColor(this, getResources().getColor(R.color.color_transparent), 0);

        //子类初始化
        init();
    }
    //----------------------------生命周期方法------------------------
    //----------------------------生命周期方法------------------------
    //----------------------------生命周期方法------------------------

    /**
     * 返回布局layout的资源
     *
     * @return 布局layout
     */
    public abstract int getContentViewId();

    /**
     * 相当于onCreate 做一些初始化操作
     */
    public abstract void init();


    @Override
    protected void onDestroy() {
        unbinder.unbind();
        ActivityCollector.getInstance().removeActivity(this);
        super.onDestroy();
    }


    //------------------------title_bar 操作-----------------
    //------------------------title_bar 操作-----------------
    //------------------------title_bar 操作-----------------
    //------------------------title_bar 操作-----------------


    /**
     * 设置标题栏名称
     *
     * @param title
     */
    public void setTitleName(String title) {
        mTitleTv.setText(title);
    }

    public void setTitleName(String title, int resCoclo) {
        mTitleTv.setText(title);
        mTitleTv.setTextColor(resCoclo);
    }

    /**
     * 隐藏或者显示标题栏
     *
     * @param visibility
     */
    public void setTitleBarVisibility(int visibility) {
        mTitleLayout.setVisibility(visibility);
    }

    /**
     * 设置标题原色
     *
     * @param resColor
     */
    public void setTitleBackrounColor(int resColor) {
        mTitleLayout.setBackgroundColor(getResources().getColor(resColor));
    }

    /**
     * 隐藏或者显示左边按钮
     *
     * @param visibility
     */
    public void setTitleLeftBtnVisibility(int visibility) {
        mTitleLeftBtn.setVisibility(visibility);
    }

    /**
     * 隐藏或者显示左边按钮(注册登入)
     *
     * @param visibility
     */
    public void setTitleRegisterLeftBtnVisibility(int visibility) {
        mTitleRegisterLeftBtn.setVisibility(visibility);
    }

    /**
     * 隐藏或者显示右边imageBtn按钮
     *
     * @param visibility
     */
    public void setTitleRightBtnVisibility(int visibility) {
        mTitleRightBtn.setVisibility(visibility);

    }

    /**
     * 设置右边imageBtn按钮图片
     *
     * @param imgId
     */
    public void setTitleRightBtnResources(int imgId) {
        mTitleRightBtn.setImageResource(imgId);
        mTitleRightBtn.setVisibility(View.VISIBLE);
    }

    public ImageButton getRightView() {
        return mTitleRightBtn;
    }

    /**
     * 设置右边文字按钮背景图片
     *
     * @param imgId
     */
    public void setTitleRightTvbtnBackgroundResources(int imgId) {
        mTitleRightTvBtn.setBackgroundResource(imgId);
        mTitleRightTvBtn.setVisibility(View.VISIBLE);
    }

    /**
     * 设置右边文字按钮名字
     *
     * @param name
     */
    public void setTitleRightTvbtnName(String name) {
        mTitleRightTvBtn.setText(name);
        mTitleRightTvBtn.setVisibility(View.VISIBLE);
    }

    /**
     * 设置右边名字颜色
     *
     * @param name
     * @param color
     */
    public void setTitleRightTvbtnName(String name, String color) {
        mTitleRightTvBtn.setText(name);
        mTitleRightTvBtn.setTextColor(Color.parseColor(color));
        mTitleRightTvBtn.setVisibility(View.VISIBLE);
    }

    /**
     * 设置右边名字颜色
     *
     * @param name
     * @param color
     */
    public void setTitleRightTvbtnName(String name, int color) {
        mTitleRightTvBtn.setText(name);
        mTitleRightTvBtn.setTextColor(color);
        mTitleRightTvBtn.setVisibility(View.VISIBLE);
    }


    /**
     * title 左侧按钮点击事件
     *
     * @param v
     */
    @OnClick(R.id.ib_left)
    public void onClickTitleLeftBtn(View v) {
        this.finish();
    }

    /**
     * title 左侧注册按钮点击事件
     *
     * @param v
     */
    @OnClick(R.id.ib_left_1)
    public void onClickTitleRegisterLeftBtn(View v) {
        this.finish();
    }

    /**
     * title 右侧tv按钮点击事件
     *
     * @param v
     */
    @OnClick(R.id.tv_right)
    public void onClickTitleRightTvBtn(View v) {

    }

    /**
     * title 右侧按钮点击事件
     *
     * @param v
     */
    @OnClick(R.id.ib_right)
    public void onClickTitleRightBtn(View v) {

    }


    @BindString(R.string.error_net_toast)
    public String errorNetToast;
    @BindString(R.string.determine)
    public String determine;
    @BindString(R.string.determine2)
    public String determine2;
    @BindString(R.string.cancel)
    public String cancel;
    @BindString(R.string.empty_error_net_server)
    public String errorServer;
    protected PageManager mPageManager;


    //------------------------ 页面状态修改 -----------------
    //------------------------ 页面状态修改 -----------------
    //------------------------ 页面状态修改 -----------------

    public void showLoading() {
        if (mPageManager != null)
            mPageManager.showLoading();
    }

    public void showContent() {
        if (mPageManager != null)
            mPageManager.showContent();
    }

    public void showEmpty() {
        if (mPageManager != null)
            mPageManager.showEmpty();
    }

    public void showEmpty(String msg) {
        if (mPageManager != null)
            mPageManager.showEmpty(msg);
    }

    public void showEmpty(String msg, int imgId) {
        if (mPageManager != null)
            mPageManager.showEmpty(msg, imgId);
    }

    public void setEmptyBgResourse(int color) {
        if (mPageManager != null)
            mPageManager.setEmptyBgResourse(color);
    }

    public void showError() {
        if (mPageManager != null)
            mPageManager.showError();
    }

    public void showError(CharSequence errorMsg) {
        if (mPageManager != null)
            mPageManager.showError(errorMsg);
    }

    public void showError(CharSequence errorMsg, int imgid) {
        if (mPageManager != null)
            mPageManager.showError(errorMsg, imgid);
    }

    /**
     * 设置error 页面trybtn的背景
     *
     * @param resid
     */
    public void setErrorTryBtnBg(int resid) {
        if (mPageManager != null)
            mPageManager.setErrorTryBtnBg(resid);
    }

    /**
     * 服务器返回错误页面
     */
    public void showErrorServer() {
        mPageManager.showEmpty(getResources().getString(R.string.empty_error_net_server), R.mipmap.empty_error_net_server);
    }

    /**
     * 在指定view上加载loadding
     */
    public void setPageStateView(View view) {
        PageManager.initInApp(getApplicationContext());
        mPageManager = PageManager.init(view, false, new Runnable() {
            @Override
            public void run() {
                onClickRetry();
            }
        });
        if (mPageManager.BASE_LOADING_LAYOUT_ID == R.layout.page_loading) {
            ImageView loadingView = (ImageView) findViewById(R.id.page_loading_iv);
            AnimationDrawable anim = (AnimationDrawable) loadingView.getBackground();
            anim.start();
        }
    }


    /**
     * 使用默认页面管理
     */
    public void setPageStateView() {
        PageManager.initInApp(getApplicationContext());
        mPageManager = PageManager.init(mContentView, false, new Runnable() {
            @Override
            public void run() {
                onClickRetry();
            }
        });
        if (mPageManager.BASE_LOADING_LAYOUT_ID == R.layout.page_loading) {
            ImageView loadingView = (ImageView) findViewById(R.id.page_loading_iv);
            AnimationDrawable anim = (AnimationDrawable) loadingView.getBackground();
            anim.start();
        }
    }

    /**
     * 自定义各种状态页面
     *
     * @param layoutIdOfEmpty
     * @param layoutIdOfLoading
     * @param layoutIdOfError
     */
    public void setCustomPageStateView(int layoutIdOfEmpty, int layoutIdOfLoading, int layoutIdOfError) {
        PageManager.initInApp(getApplicationContext(), layoutIdOfEmpty, layoutIdOfLoading, layoutIdOfError);
        mPageManager = PageManager.init(mContentView, false, new Runnable() {
            @Override
            public void run() {
                onClickRetry();
            }
        });
        if (layoutIdOfLoading == R.layout.page_loading) {
            ImageView loadingView = (ImageView) findViewById(R.id.page_loading_iv);
            AnimationDrawable anim = (AnimationDrawable) loadingView.getBackground();
            anim.start();
        }
    }

    /**
     * 点击了错误重试
     */
    protected void onClickRetry() {

    }

    //------------------------------基础方法---------------------------------------------
    //------------------------------基础方法---------------------------------------------
    //------------------------------基础方法---------------------------------------------


    /**
     * 打开activity
     *
     * @param ActivityClass
     */
    public void openActivity(Class<? extends Activity> ActivityClass) {
        Intent intent = new Intent(this, ActivityClass);
        startActivity(intent);
    }

    /**
     * 打开activity
     *
     * @param ActivityClass
     * @param b
     */
    public void openActivityNoAnim(Class<? extends Activity> ActivityClass, Bundle b) {
        Intent intent = new Intent(mContext, ActivityClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.putExtras(b);
        startActivity(intent);
    }

    /**
     * 打开activity
     *
     * @param ActivityClass
     */
    public void openActivityNoAnim(Class<? extends Activity> ActivityClass) {
        Intent intent = new Intent(mContext, ActivityClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    /**
     * 打开activity
     *
     * @param ActivityClass
     * @param b
     */
    public void openActivity(Class<? extends Activity> ActivityClass, Bundle b) {
        Intent intent = new Intent(this, ActivityClass);
        intent.putExtras(b);
        startActivity(intent);
    }

    /**
     * 显示toast弹框
     *
     * @param text
     */
    public void showToast(String text) {
        ToastUtil.showToastCenter(this, text);
    }

    /**
     * 弹窗知道了，需要控制软件盘的显示与隐藏
     *
     * @param editText
     * @param msg
     */
    public void showEditTextPrompt(final EditText editText, String msg) {
        KeyBoardUtils.closeKeybord(editText, this);
        final PopupWindowTwoButton twoButton = new PopupWindowTwoButton(this);
        twoButton.getTv_title().setVisibility(View.GONE);
        twoButton.getTv_ok().setText(Constants.STR_PROMPT);
        twoButton.getTv_content().setText(msg);
        twoButton.getTv_ok().setVisibility(View.GONE);
        twoButton.getLine_two().setVisibility(View.GONE);
        twoButton.getTv_cancel().setText(Constants.STR_GOT_IT);
        twoButton.getTv_cancel().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KeyBoardUtils.openKeybord(editText, mContext);
                twoButton.dismiss();
            }
        });
        twoButton.showPopupWindow(editText, Gravity.CENTER);
    }

    /**
     * 弹窗，知道了
     *
     * @param v
     * @param msg
     */
    public void showPrompt(View v, String msg) {
        final PopupWindowTwoButton twoButton = new PopupWindowTwoButton(this);
        twoButton.getTv_title().setVisibility(View.GONE);
        twoButton.getTv_ok().setText(Constants.STR_PROMPT);
        twoButton.getTv_content().setText(msg);
        twoButton.getTv_ok().setVisibility(View.GONE);
        twoButton.getLine_two().setVisibility(View.GONE);
        twoButton.getTv_cancel().setText(Constants.STR_GOT_IT);
        twoButton.getTv_cancel().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                twoButton.dismiss();
            }
        });
        twoButton.showPopupWindow(v, Gravity.CENTER);
    }

    /**
     * 网络不可用弹窗
     */
    public void showNetDialog() {
        final PopupWindowTwoButton twoButton = new PopupWindowTwoButton(this);
        twoButton.getTv_title().setVisibility(View.GONE);
        twoButton.getTv_content().setText("当前网络连接不可用，是否设置网络？");
        twoButton.getTv_ok().setText("去设置");
        twoButton.getTv_cancel().setText("取消");
        twoButton.getTv_cancel().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                twoButton.dismiss();
            }
        });
        twoButton.getTv_ok().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    NetworkUtil.openNetWorkSetting(mContext);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                twoButton.dismiss();
            }
        });
        twoButton.showPopupWindow(new View(mContext), Gravity.CENTER);
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
