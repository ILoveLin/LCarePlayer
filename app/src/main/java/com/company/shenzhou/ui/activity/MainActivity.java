package com.company.shenzhou.ui.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.company.shenzhou.R;
import com.company.shenzhou.base.ActivityCollector;
import com.company.shenzhou.base.BaseActivity;
import com.company.shenzhou.ui.fragment.MineFragment;
import com.company.shenzhou.ui.fragment.user.UserFragment;
import com.company.shenzhou.ui.fragment.video.VideoFragment;
import com.company.shenzhou.utils.FileUtil;
import com.company.shenzhou.utils.KeyboardWatcher;
import com.company.shenzhou.view.dialog.MessageDialog;
import com.hjq.base.BaseDialog;
import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.yun.common.contant.Constants;
import com.yun.common.utils.LogUtils;
import com.yun.common.utils.SharePreferenceUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements KeyboardWatcher.SoftKeyboardStateListener {
    @BindView(R.id.tv_tab_homepage)
    TextView tvTabFirstPage;   //首页
    @BindView(R.id.tv_tab_drug_query)
    TextView tvTabSecondPage;  //药品查询
    @BindView(R.id.tv_tab_recomment)
    TextView tvTabThirdPage;  //咨询
    @BindView(R.id.tv_tab_mine)
    TextView tvTabFourthPage;        //我的
    @BindView(R.id.iv_tab_mine)
    ImageView ivTabMine;        //我的小红点
    @BindView(R.id.rel_tab_mine)
    RelativeLayout relTabMine;      //整体的我的relative　　　
    @BindView(R.id.layout_bottom_lin)
    LinearLayout layoutBottomLin;
    @BindView(R.id.ll_main_bottom)
    LinearLayout linearBottom;
    private FragmentManager fragmentManager;
    private Integer valTab;
    private MessageDialog.Builder mExitDialog;

    @Override
    public int getContentViewId() {
        return R.layout.activity_main;

    }

    @Override
    public void init() {
        setTitleBarVisibility(View.GONE);
        linearBottom.setVisibility(View.VISIBLE);
        fragmentManager = getSupportFragmentManager();
        valTab = (Integer) SharePreferenceUtil.get(this, SharePreferenceUtil.DYNAMIC_SWITCH_TAB, Constants.TAB_01);

        setChoiceItem(valTab);
//        PlayerFactory.setPlayManager(SystemPlayerManager.class);     //这个是GSYVideoPlayer 设置所有流媒体格式播放内核
        requestPermission();

        KeyboardWatcher.with(this)
                .setListener(this);

        //可用空间不足2GB弹出对话框
        if (2 > Float.parseFloat(FileUtil.getROMAvailableSize(this).replace("GB", "").trim())) {
            showWarningDialog();
        }

    }

    private void showWarningDialog() {
        mExitDialog = new MessageDialog.Builder(this);
        // 标题可以不用填写
        mExitDialog.setTitle("警告!")
                // 内容必须要填写
                .setMessage("设备可用空间不足2GB,录制视频可能导致保存视频失败!")
                // 确定按钮文本
                .setConfirm(getString(R.string.common_confirm))
                // 设置 null 表示不显示取消按钮
                .setCancel(getString(R.string.common_cancel))
                // 设置点击按钮后不关闭对话框
                //.setAutoDismiss(false)
                .setCanceledOnTouchOutside(false)
                .setListener(new MessageDialog.OnListener() {

                    @Override
                    public void onConfirm(BaseDialog dialog) {
                    }

                    @Override
                    public void onCancel(BaseDialog dialog) {
                    }
                })
                .show();
    }

    private void requestPermission() {
        XXPermissions.with(this)
                // 不适配 Android 11 可以这样写
                //.permission(Permission.Group.STORAGE)
                // 适配 Android 11 需要这样写，这里无需再写 Permission.Group.STORAGE
                .permission(Permission.MANAGE_EXTERNAL_STORAGE)
                .permission(Permission.RECORD_AUDIO)
                .permission(Permission.CAMERA)
//                .permission(Permission.WRITE_EXTERNAL_STORAGE)
//                .permission(Permission.READ_EXTERNAL_STORAGE)
                .permission(Permission.READ_PHONE_STATE)
                .request(new OnPermissionCallback() {

                    @Override
                    public void onGranted(List<String> permissions, boolean all) {
                        if (all) {
//                            showToast("获取存储权限成功");
                        }
                    }

                    @Override
                    public void onDenied(List<String> permissions, boolean never) {
                        if (never) {
                            showToast("被永久拒绝授权，请手动授予存储权限");
                            // 如果是被永久拒绝就跳转到应用权限系统设置页面
                            XXPermissions.startPermissionActivity(MainActivity.this, permissions);
                        } else {
                            showToast("获取存储权限失败");
                        }
                    }
                });

    }

    private UserFragment firstFragment;
    private VideoFragment secondFragment;
    private MineFragment thirdFragment;

    private void setChoiceItem(Integer index) {
        if (index < 0) {
            index = Constants.TAB_01;
        }
        SharePreferenceUtil.put(this, SharePreferenceUtil.DYNAMIC_SWITCH_TAB, index);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hideFragments(transaction);
        switch (index) {
            case Constants.TAB_01:   //1　　
                if (firstFragment == null) {
                    firstFragment = new UserFragment();
                    transaction.add(R.id.ll_content, firstFragment);
                } else {
                    transaction.show(firstFragment);
                }
                tvTabFirstPage.setSelected(true);
                tvTabSecondPage.setSelected(false);
                tvTabThirdPage.setSelected(false);
                tvTabFourthPage.setSelected(false);
                overAnim(tvTabFirstPage);
                break;
            case Constants.TAB_02://药品查询  //2
                if (secondFragment == null) {
                    secondFragment = new VideoFragment();
                    transaction.add(R.id.ll_content, secondFragment);
                } else {
                    transaction.show(secondFragment);
                }
                tvTabFirstPage.setSelected(false);
                tvTabSecondPage.setSelected(true);
                tvTabThirdPage.setSelected(false);
                tvTabFourthPage.setSelected(false);
                overAnim(tvTabSecondPage);
                break;
            case Constants.TAB_03:   //3
                if (thirdFragment == null) {
                    thirdFragment = new MineFragment();
                    transaction.add(R.id.ll_content, thirdFragment);
                } else {
                    transaction.show(thirdFragment);
                }
                tvTabFirstPage.setSelected(false);
                tvTabSecondPage.setSelected(false);
                tvTabThirdPage.setSelected(true);
                tvTabFourthPage.setSelected(false);
                overAnim(tvTabThirdPage);
                break;
        }
        transaction.commit();
    }

    @OnClick({R.id.tv_tab_homepage, R.id.tv_tab_drug_query, R.id.tv_tab_recomment, R.id.rel_tab_mine})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_tab_homepage:    //1
                setChoiceItem(Constants.TAB_01);
                break;
            case R.id.tv_tab_drug_query:   // 2
                setChoiceItem(Constants.TAB_02);
                break;
            case R.id.tv_tab_recomment:    //3
                setChoiceItem(Constants.TAB_03);
                break;
            default:
        }
    }

    // 隐藏所有的Fragment,避免fragment混乱
    private void hideFragments(FragmentTransaction transaction) {
        if (firstFragment != null) {
            transaction.hide(firstFragment);
        }
        if (secondFragment != null) {
            transaction.hide(secondFragment);

        }
        if (thirdFragment != null) {
            transaction.hide(thirdFragment);
        }
    }

    private void overAnim(View view) {
        final ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, View.SCALE_X, 0.8f, 1.0f);
        final ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, View.SCALE_Y, 0.8f, 1.0f);
        final ObjectAnimator translationY = ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, 0, 0);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(scaleX, scaleY, translationY);
        set.setDuration(200);
        set.start();
    }


    // 用来计算返回键的点击间隔时间
    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                exitTime = System.currentTimeMillis();
                showToast("再按一次退出程序");
            } else {
                SharePreferenceUtil.put(this, SharePreferenceUtil.DYNAMIC_SWITCH_TAB, 0);
                ActivityCollector.removeAll();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @Override
    public void onSoftKeyboardOpened(int keyboardHeight) {
        LogUtils.e("onSoftKeyboardOpened");
        linearBottom.setVisibility(View.GONE);

    }

    @Override
    public void onSoftKeyboardClosed() {
        LogUtils.e("onSoftKeyboardOpened======Closed");

        linearBottom.setVisibility(View.VISIBLE);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mExitDialog) {
            mExitDialog = null;
        }
    }
}