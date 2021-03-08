package com.company.shenzhou.ui.activity.login;


import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.company.shenzhou.R;
import com.company.shenzhou.base.BaseActivity;
import com.company.shenzhou.base.help.KeyboardWatcher;
import com.company.shenzhou.bean.dbbean.UserDBRememberBean;
import com.company.shenzhou.bean.Constants;
import com.company.shenzhou.bean.dbbean.UserDBRememberBean;
import com.company.shenzhou.ui.activity.MainActivity;
import com.company.shenzhou.utils.CommonUtil;
import com.company.shenzhou.utils.ScreenSizeUtil;
import com.company.shenzhou.utils.db.UserDBRememberBeanUtils;
import com.company.shenzhou.utils.db.UserDBRememberBeanUtils;
import com.company.shenzhou.view.ListHistoryPopup;
import com.hjq.base.BasePopupWindow;
import com.hjq.base.action.AnimAction;
import com.yun.common.utils.LogUtils;
import com.yun.common.utils.SharePreferenceUtil;
import com.yun.common.utils.StatusBarUtil;
import com.yun.common.utils.StatusBarUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Lovelin on 2020/2/18
 * <p>
 * Describe:带动画的登入界面
 */
public class LoginAnimatorActivity extends BaseActivity implements KeyboardWatcher.SoftKeyboardStateListener {
    @BindView(R.id.iv_login_logo)
    ImageView mLogoView;
    @BindView(R.id.username_right)
    ImageButton username_right;
    @BindView(R.id.ll_login_body)
    LinearLayout mBodyLayout;
    @BindView(R.id.relative_username_00)
    RelativeLayout relative_username_00;
    @BindView(R.id.relative_root)
    RelativeLayout relative_root;
    @BindView(R.id.et_login_phone)
    EditText mPhoneView;
    @BindView(R.id.et_login_password)
    EditText mPasswordView;
    @BindView(R.id.btn_login_commit)
    Button mCommitView;
    @BindView(R.id.v_login_blank)
    View mBlankView;
    @BindView(R.id.checkbox)
    CheckBox checkbox;
    @BindView(R.id.linear_login_root)
    LinearLayout linear_login_root;
    private final float mLogoScale = 0.8f;   //缩放比例
    private final int mAnimTime = 300;      //动画时间
    private String username;
    private String password;
    private boolean isRemember;
    private boolean isAdmin;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    mPhoneView.setText("admin");
                    mPasswordView.setText("");
                    break;
                case 1:  //点击历史记录之后的操作
                    LogUtils.e("path=====录像--是否存在=====" + UserDBRememberBeanUtils.queryListIsExist((String) msg.obj));

                    if (UserDBRememberBeanUtils.queryListIsExist((String) msg.obj)) {
                        UserDBRememberBean userDBRememberBean = UserDBRememberBeanUtils.queryListByName((String) msg.obj);
                        username_right.setTag("close");
                        username_right.setImageResource(R.drawable.login_icon_down);
                        if ("Yes".equals(userDBRememberBean.getRemember())) {
                            checkbox.setChecked(true);
                            mPhoneView.setText("" + userDBRememberBean.getUsername());
                            mPasswordView.setText("" + userDBRememberBean.getPassword());
                        } else {
                            mPhoneView.setText("" + userDBRememberBean.getUsername());
                            mPasswordView.setText("");
                            checkbox.setChecked(false);
                        }
                    }
                    break;
            }
        }
    };
    private RecyclerView textrecyclerview;
    private int mPhoneViewWidth;
    private ListHistoryPopup.Builder historyBuilder;

    @Override
    public int getContentViewId() {
        return R.layout.activity_login;
    }

    @Override
    public void init() {
        initView();
    }

    private void initView() {
        setTitleBarVisibility(View.GONE);
        setTitleLeftBtnVisibility(View.INVISIBLE);
        setTitleRightBtnVisibility(View.INVISIBLE);
        setTitleName("");
        setPageStateView();
        setMyLayoutParams();
        isRemember = (boolean) SharePreferenceUtil.get(LoginAnimatorActivity.this, SharePreferenceUtil.Current_RememberPassword, false);
        isAdmin = (boolean) SharePreferenceUtil.get(LoginAnimatorActivity.this, Constants.IS_Admin, false);
        if (!isAdmin) {  //没有创建admin用户,才创建超级用户，并且创建之后设置账号密码
            setCurrentUserMsg();
            mHandler.sendEmptyMessageDelayed(0,300);
        }

        StatusBarUtils.setColor(this, getResources().getColor(R.color.white), 0);
        StatusBarUtil.darkMode(this, true);  //设置了状态栏文字的颜色
//        StatusBarUtils.setColor(this, getResources().getColor(R.color.white), 0);
//        StatusBarUtil.darkMode(this, false);  //设置了状态栏文字的颜色
        post(new Runnable() {
            @Override
            public void run() {
                // 因为在小屏幕手机上面，因为计算规则的因素会导致动画效果特别夸张，所以不在小屏幕手机上面展示这个动画效果
                if (mBlankView.getHeight() > mBodyLayout.getHeight()) {
                    // 只有空白区域的高度大于登录框区域的高度才展示动画
                    KeyboardWatcher.with(LoginAnimatorActivity.this)
                            .setListener(LoginAnimatorActivity.this);
                }
            }
        });
        //初始化是否记住密码
        initRememberLogin();
    }


    private void setMyLayoutParams() {
        LinearLayout linear_login_root = findViewById(R.id.linear_login_root);
        TextView tv_top = findViewById(R.id.tv_top);
        TextView tv_under = findViewById(R.id.tv_under);
        int width = linear_login_root.getWidth();
        int screenWidth = ScreenSizeUtil.getScreenWidth(this);
        int i3 = new Double(screenWidth * 0.3).intValue();
        int i2 = new Double(screenWidth * 0.25).intValue();

//        LinearLayout.LayoutParams tv_topParams1 = new LinearLayout.LayoutParams(i, i);
        LogUtils.e("TAG==isAdmin===" + screenWidth);
        LogUtils.e("TAG==isAdmin===" + screenWidth * 0.25);
        LogUtils.e("TAG==isAdmin===" + screenWidth * 0.5);
        ViewGroup.LayoutParams layoutParams1 = tv_top.getLayoutParams();
        ViewGroup.LayoutParams layoutParams2 = tv_under.getLayoutParams();

//        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);//工具类哦
//        LinearLayout.LayoutParams LoginParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);//工具类哦
//        mCommitView
        if (screenWidth >= 1600) {
            layoutParams.leftMargin = 50;  //
            layoutParams.rightMargin = 50; //
            layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);//addRule参数对应RelativeLayout XML布局的属性
            layoutParams.topMargin = 500;
        } else if (screenWidth == 1080) {
            layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);//addRule参数对应RelativeLayout XML布局的属性
            layoutParams.leftMargin = 25;  //
            layoutParams.rightMargin = 25; //130
            layoutParams.topMargin = 150;
        } else if (screenWidth == 720) {
            layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);//addRule参数对应RelativeLayout XML布局的属性
            layoutParams.leftMargin = 20;
            layoutParams.rightMargin = 20; //80
            tv_top.setWidth(screenWidth * 25);
            tv_under.setHeight(screenWidth * 25);
        } else if (screenWidth < 720) {
            layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);//addRule参数对应RelativeLayout XML布局的属性
            layoutParams.leftMargin = 15;
            layoutParams.rightMargin = 15; //50
            layoutParams.topMargin = -10;
        }
        tv_top.setWidth(i3);
        tv_top.setHeight(i3);
        tv_under.setHeight(i2);
        tv_under.setWidth(i2);
        linear_login_root.setLayoutParams(layoutParams);
        tv_top.setLayoutParams(layoutParams1);
        tv_under.setLayoutParams(layoutParams2);
    }

    /**
     * 因为系统默认给的账号密码admin 权限是超级用户:2
     */
    private void setCurrentUserMsg() {
        SharePreferenceUtil.put(LoginAnimatorActivity.this, SharePreferenceUtil.Current_Username, "admin");
        SharePreferenceUtil.put(LoginAnimatorActivity.this, SharePreferenceUtil.Current_Password, "admin");
        SharePreferenceUtil.put(LoginAnimatorActivity.this, SharePreferenceUtil.Current_UserType, 2);
        SharePreferenceUtil.put(LoginAnimatorActivity.this, SharePreferenceUtil.Current_ID, 1);
        SharePreferenceUtil.put(LoginAnimatorActivity.this, SharePreferenceUtil.Current_Admin_ChangePassword, false);    //超级用户只能修改一次密码
        SharePreferenceUtil.get(LoginAnimatorActivity.this, Constants.IS_Admin, true);  //表示存过了
        //存入数据库
        long ID = 1;
        UserDBRememberBean UserDBRememberBean = new UserDBRememberBean();
        UserDBRememberBean.setUsername("admin");
        UserDBRememberBean.setPassword("admin");
        UserDBRememberBean.setTag("admin");
        UserDBRememberBean.setRemember("No");
        UserDBRememberBean.setUserType(2);
        UserDBRememberBean.setId(ID);
        UserDBRememberBeanUtils.insertOrReplaceData(UserDBRememberBean);
        //只要存就表示 安装过,初始化admin 用户
        SharePreferenceUtil.put(LoginAnimatorActivity.this, Constants.IS_Admin, true);
        boolean isExist = UserDBRememberBeanUtils.queryListIsExist("admin");
        LogUtils.e("DB=====isExist===" + isExist);
        String str = "admin";
        List<UserDBRememberBean> UserDBRememberBeanList = UserDBRememberBeanUtils.queryListByMessage(str);
        for (int i = 0; i < UserDBRememberBeanList.size(); i++) {
            String username = UserDBRememberBeanList.get(i).getUsername();
            String password = UserDBRememberBeanList.get(i).getPassword();
            LogUtils.e("DB=====username===" + username + "==password==" + password);
        }
        LogUtils.e("DB=====isExist===" + isExist);
    }


    private void initRememberLogin() {
        if (isRemember) {
            //记住密码直接sp里面取,注意更改当前用户信息,需要实时更新Sp
            String rememberName = (String) SharePreferenceUtil.get(LoginAnimatorActivity.this, SharePreferenceUtil.Current_Username, "");
            String rememberPassword = (String) SharePreferenceUtil.get(LoginAnimatorActivity.this, SharePreferenceUtil.Current_Password, "");
            int rememberType = (int) SharePreferenceUtil.get(LoginAnimatorActivity.this, SharePreferenceUtil.Current_UserType, 0);


            mPhoneView.setText(rememberName);
            mPasswordView.setText(rememberPassword);
            checkbox.setChecked(true);
        }
    }


    /**
     * 无网络的时候 刷新数据
     */
    @Override
    protected void onClickRetry() {
        super.onClickRetry();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        initRefreshData();

        mPhoneView.getViewTreeObserver().addOnDrawListener(new ViewTreeObserver.OnDrawListener() {
            @Override
            public void onDraw() {
                mPhoneViewWidth = mPhoneView.getWidth();

            }
        });

        int i = ScreenSizeUtil.dp2px(this, 80);
        int screenWidth = ScreenSizeUtil.getScreenWidth(this) - ScreenSizeUtil.dp2px(this, 80);
        LogUtils.e("==========i=========" + i);
        LogUtils.e("==========screenWidth=========" + screenWidth);
        showHistoryDialog();

    }


    private String currentSelectedHistoryName = "";

    private void showHistoryDialog() {
        List<UserDBRememberBean> list = UserDBRememberBeanUtils.queryAll(UserDBRememberBean.class);
        LogUtils.e("==========数据库======list===" + list.size());
        ArrayList<String> nameList = CommonUtil.getNameList(list);
        username_right.setImageResource(R.drawable.login_icon_down);
        username_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtils.e("==========Tag======Tag===" + username_right.getTag());
                if ("close".equals(username_right.getTag())) {
                    username_right.setTag("open");
                    username_right.setImageResource(R.drawable.login_icon_up);

                } else {
                    username_right.setTag("close");
                    username_right.setImageResource(R.drawable.login_icon_down);
                }
                historyBuilder = new ListHistoryPopup.Builder(LoginAnimatorActivity.this);
                View contentView = historyBuilder.getContentView();
                int visibility = contentView.getVisibility();
                LogUtils.e("==========Tag======visibility===" + visibility);
                historyBuilder.setList(nameList)
                        .setGravity(Gravity.CENTER)
                        .setAutoDismiss(true)
                        .setOutsideTouchable(false)
                        .setWidth(mPhoneViewWidth + 60)
                        .setXOffset(-30)
                        .setHeight(650)
                        .setAnimStyle(AnimAction.ANIM_SCALE)
                        .setListener((ListHistoryPopup.OnListener<String>) (popupWindow, position, str) -> {
                            Message tempMsg = mHandler.obtainMessage();
                            tempMsg.what = 1;
                            tempMsg.obj = str;
                            mHandler.sendMessage(tempMsg);

                        })
                        .showAsDropDown(mPhoneView);

                historyBuilder.getPopupWindow().addOnDismissListener(new BasePopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss(BasePopupWindow popupWindow) {
                        username_right.setTag("close");
                        username_right.setImageResource(R.drawable.login_icon_down);
                    }
                });

            }
        });
    }

    /**
     * 判断是否记住密码
     */
    private void initRefreshData() {
        Boolean isRemember = (Boolean) SharePreferenceUtil.get(this, SharePreferenceUtil.Current_RememberPassword, false);
        LogUtils.e("TAG====登入==isRemember===" + isRemember);

        if (isRemember) {
            String name = (String) SharePreferenceUtil.get(this, SharePreferenceUtil.Current_Username, "");
            String password = (String) SharePreferenceUtil.get(this, SharePreferenceUtil.Current_Password, "");
            mPhoneView.setText("" + name);
            mPasswordView.setText("" + password);
            LogUtils.e("TAG====登入==name===" + name);
            LogUtils.e("TAG====登入==password===" + password);
        } else {
            mPhoneView.setText("");
            mPasswordView.setText("");
        }
    }


    @OnClick({R.id.checkbox, R.id.btn_login_commit})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.checkbox://记住密码
                SharePreferenceUtil.put(LoginAnimatorActivity.this, SharePreferenceUtil.Current_RememberPassword, checkbox.isChecked());  //是否记住密码
                if (checkbox.isChecked()) {
                    checkbox.setChecked(true);
                } else {
                    checkbox.setChecked(false);
                }
                break;
            case R.id.btn_login_commit:  //登入
                checkData();
                break;

            default:
                break;
        }
    }


    private void checkData() {
        username = mPhoneView.getText().toString().trim();
        password = mPasswordView.getText().toString().trim();
        if ("".equals(username) && TextUtils.isEmpty(username)) {
            showToast("用户名不能为空!");
            return;
        } else {
            checkDBDataToChangeCurrentUserMsg();
        }
    }

    /**
     * 查询数据库然后更新当前用户信息
     */
    private void checkDBDataToChangeCurrentUserMsg() {
        boolean isExist = UserDBRememberBeanUtils.queryListIsExist(username);
        LogUtils.e("isExist===" + isExist);
        if (isExist) {  //存在
            List<UserDBRememberBean> mList = UserDBRememberBeanUtils.queryListByMessage(username);
            String dbusername = mList.get(0).getUsername().toString().trim();
            String dbpassword = mList.get(0).getPassword().toString().trim();
            int dbusertype = mList.get(0).getUserType();
            Long id = mList.get(0).getId();
            LogUtils.e("TAG==登录--dbusername===" + dbusername + "====dbpassword==" + dbpassword + "====dbusertype==" + dbusertype + "====id==" + id);

            if (password.equals(dbpassword)) {  //判断数据库密码和输入密码是否一致,之后更新SP的当前用户信息
                SharePreferenceUtil.put(LoginAnimatorActivity.this, SharePreferenceUtil.Current_RememberPassword, checkbox.isChecked());  //是否记住密码
                SharePreferenceUtil.put(LoginAnimatorActivity.this, SharePreferenceUtil.Current_Username, dbusername);
                SharePreferenceUtil.put(LoginAnimatorActivity.this, SharePreferenceUtil.Current_Password, dbpassword);
                SharePreferenceUtil.put(LoginAnimatorActivity.this, SharePreferenceUtil.Current_UserType, dbusertype);
                SharePreferenceUtil.put(LoginAnimatorActivity.this, SharePreferenceUtil.Current_ID, id);
                SharePreferenceUtil.put(LoginAnimatorActivity.this, Constants.SP_IS_FIRST_IN, false);   //false 不是第一次登入了
                SharePreferenceUtil.put(LoginAnimatorActivity.this, Constants.Is_Logined, true);   //false  登录的标志 true表示登录了
                SharePreferenceUtil.put(LoginAnimatorActivity.this, SharePreferenceUtil.Current_ToastShow, "Yes");  //为了解决第一次登入toast的bug

                startActivity(new Intent(LoginAnimatorActivity.this, MainActivity.class));
                String name = (String) SharePreferenceUtil.get(LoginAnimatorActivity.this, SharePreferenceUtil.Current_Username, "");
                String pass = (String) SharePreferenceUtil.get(LoginAnimatorActivity.this, SharePreferenceUtil.Current_Password, "");
                int type = (int) SharePreferenceUtil.get(LoginAnimatorActivity.this, SharePreferenceUtil.Current_UserType, 0);

                String currentPhone = mPhoneView.getText().toString();
                String currentPassword = mPasswordView.getText().toString();
                //提交数据的时候,对当前用户是否记住密码更新DB
                boolean isExistt = UserDBRememberBeanUtils.queryListIsExist(currentPhone);
                if (checkbox.isChecked()) {
                    SharePreferenceUtil.put(LoginAnimatorActivity.this, SharePreferenceUtil.Current_Username, dbusername);
                    SharePreferenceUtil.put(LoginAnimatorActivity.this, SharePreferenceUtil.Current_Password, dbpassword);
                    LogUtils.e("TAG====登入==db--username===" + dbusername);
                    LogUtils.e("TAG====登入==db--password===" + dbpassword);
                    if (!"".equals(currentPassword)) {  //密码不为空的时候才做操作
                        if (isExistt) { //存在
                            UserDBRememberBean userDBRememberBean = UserDBRememberBeanUtils.queryListByName(currentPhone);
                            userDBRememberBean.setId(userDBRememberBean.getId());
                            userDBRememberBean.setRemember("Yes");
                            UserDBRememberBeanUtils.updateData(userDBRememberBean);
                        }
                    }
                } else {
                    if (!"".equals(currentPassword)) {  //密码不为空的时候才做操作
                        if (isExistt) { //存在
                            UserDBRememberBean userDBRememberBean = UserDBRememberBeanUtils.queryListByName(currentPhone);
                            userDBRememberBean.setId(userDBRememberBean.getId());
                            userDBRememberBean.setRemember("No");
                            UserDBRememberBeanUtils.updateData(userDBRememberBean);
                        }
                    }
                }
                finish();
            } else {
                showToast("密码输入错误");
            }
        } else {
            showToast("账户不存在");
        }
    }

    /**
     * 实现方法
     *
     * @param keyboardHeight 软键盘高度
     */
    @Override
    public void onSoftKeyboardOpened(int keyboardHeight) {
        int screenHeight = getResources().getDisplayMetrics().heightPixels;
        int[] location = new int[2];
        // 获取这个 View 在屏幕中的坐标（左上角）
        mBodyLayout.getLocationOnScreen(location);
        //int x = location[0];
        int y = location[1];
        int bottom = screenHeight - (y + mBodyLayout.getHeight());
        if (keyboardHeight > bottom) {
            // 执行位移动画
            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(mBodyLayout, "translationY", 0, -(keyboardHeight - bottom));
            objectAnimator.setDuration(mAnimTime);
            objectAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
            objectAnimator.start();
            // 执行缩小动画
            mLogoView.setPivotX(mLogoView.getWidth() / 2f);
            mLogoView.setPivotY(mLogoView.getHeight());
            AnimatorSet animatorSet = new AnimatorSet();
            ObjectAnimator scaleX = ObjectAnimator.ofFloat(mLogoView, "scaleX", 1.0f, mLogoScale);
            ObjectAnimator scaleY = ObjectAnimator.ofFloat(mLogoView, "scaleY", 1.0f, mLogoScale);
            ObjectAnimator translationY = ObjectAnimator.ofFloat(mLogoView, "translationY", 0.0f, -(keyboardHeight - bottom));
            animatorSet.play(translationY).with(scaleX).with(scaleY);
            animatorSet.setDuration(mAnimTime);
            animatorSet.start();
        }
    }

    @Override
    public void onSoftKeyboardClosed() {
        // 执行位移动画
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(mBodyLayout, "translationY", mBodyLayout.getTranslationY(), 0);
        objectAnimator.setDuration(mAnimTime);
        objectAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        objectAnimator.start();
        if (mLogoView.getTranslationY() == 0) {
            return;
        }
        // 执行放大动画
        mLogoView.setPivotX(mLogoView.getWidth() / 2f);
        mLogoView.setPivotY(mLogoView.getHeight());
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(mLogoView, "scaleX", mLogoScale, 1.0f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(mLogoView, "scaleY", mLogoScale, 1.0f);
        ObjectAnimator translationY = ObjectAnimator.ofFloat(mLogoView, "translationY", mLogoView.getTranslationY(), 0);
        animatorSet.play(translationY).with(scaleX).with(scaleY);
        animatorSet.setDuration(mAnimTime);
        animatorSet.start();
    }


    /**
     * 延迟执行
     */
    public final boolean post(Runnable r) {
        return postDelayed(r, 0);
    }

    /**
     * 延迟一段时间执行
     */
    public final boolean postDelayed(Runnable r, long delayMillis) {
        if (delayMillis < 0) {
            delayMillis = 0;
        }
        return postAtTime(r, SystemClock.uptimeMillis() + delayMillis);
    }


    private static final Handler HANDLER = new Handler(Looper.getMainLooper());
    public final Object mHandlerToken = hashCode();

    /**
     * 在指定的时间执行
     */
    public final boolean postAtTime(Runnable r, long uptimeMillis) {
        // 发送和这个 Activity 相关的消息回调
        return HANDLER.postAtTime(r, mHandlerToken, uptimeMillis);
    }
}