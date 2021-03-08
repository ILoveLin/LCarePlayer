package com.company.shenzhou.ui.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.company.shenzhou.R;
import com.company.shenzhou.base.ActivityCollector;
import com.company.shenzhou.ui.activity.MainActivity;
import com.company.shenzhou.ui.activity.PowerExplainActivity;
import com.company.shenzhou.ui.activity.vlc.TestVlcPlayerActivity;
import com.company.shenzhou.ui.activity.login.LoginAnimatorActivity;
import com.company.shenzhou.base.BaseFragment;
import com.company.shenzhou.bean.dbbean.UserDBBean;
import com.company.shenzhou.bean.Constants;
import com.company.shenzhou.utils.ClearEditText;
import com.company.shenzhou.utils.FileUtil;
import com.company.shenzhou.utils.FileUtils;
import com.company.shenzhou.utils.ScreenSizeUtil;
import com.company.shenzhou.utils.db.UserDBUtils;
import com.company.shenzhou.view.PasswordEditText;
import com.company.shenzhou.view.PasswordV2EditText;
import com.company.shenzhou.view.PopupWindowInputChangeTowPassword;
import com.company.shenzhou.view.SettingBar;
import com.hanlyjiang.library.fileviewer.FileViewer;
import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.yun.common.utils.DensityUtils;
import com.yun.common.utils.LogUtils;
import com.yun.common.utils.ScreenUtils;
import com.yun.common.utils.SharePreferenceUtil;
import com.yun.common.utils.ToastUtil;
import com.yun.common.utils.popupwindow.PopupWindowTwoButton;
import com.yun.common.utils.popupwindow.PopupWindowVersionTwoButton;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;

/**
 * LoveLin
 * <p>
 * Describe
 */
public class MineFragment extends BaseFragment {
    public static String FILE_DIR = "/sdcard/Downloads/test/";
    @BindView(R.id.bar_mine_change_password)
    SettingBar mChangePassword;
    @BindView(R.id.bar_mine_power_level)
    SettingBar bar_mine_power_level;//权限等级
    @BindView(R.id.bar_mine_power_explain)
    SettingBar bar_mine_power_explain;//权限说明
    @BindView(R.id.bar_mine_username)
    SettingBar bar_mine_username;
    @BindView(R.id.togo)
    SettingBar togo;
    @BindView(R.id.bar_mine_version)
    SettingBar bar_mine_version;
    @BindView(R.id.bar_mine_use_pace)
    SettingBar bar_mine_use_pace;
    @BindView(R.id.bar_mine_how_use)
    SettingBar bar_mine_how_use;
    @BindView(R.id.bar_mine_exit)
    SettingBar bar_mine_exit;
    @BindView(R.id.linear_all_mine)
    LinearLayout mLinearAll;
    private PopupWindowInputChangeTowPassword changePop;
    private PopupWindowTwoButton exitPop;
    private String popType = "";
    private PopupWindowVersionTwoButton versionPop;

    @Override
    public int getContentViewId() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void init(ViewGroup rootView) {
        setTitleBarVisibility(View.VISIBLE);
        setTitleLeftBtnVisibility(View.GONE);
        initView();
        setPageStateView();
        showContent();
        responseListener();
        initData();
    }

    private void initData() {
        FILE_DIR = new File(getActivity().getFilesDir(), "test").getAbsolutePath() + File.separator;
        try {
            FileUtils.copyAssetsDir(getActivity(), "test", FILE_DIR);
        } catch (IOException e) {
            Log.e("QbSdk", "IOException:");
            e.printStackTrace();
        }
    }

    @SuppressLint("ResourceAsColor")
    private void initView() {
        setTitleName("我的");
        String username = (String) SharePreferenceUtil.get(getActivity(), SharePreferenceUtil.Current_Username, "");
        //0普通  1权限  2超级用户
        int userType = (int) SharePreferenceUtil.get(getActivity(), SharePreferenceUtil.Current_UserType, 0);
        bar_mine_username.setRightText(username + "");
        switch (userType) {
            case 0:
                bar_mine_power_level.setRightText("普通用户");
                break;
            case 1:
                bar_mine_power_level.setRightText("权限用户");
                break;
            case 2:
                bar_mine_power_level.setRightText("超级管理员");
                break;
        }
        String romAvailableSize = FileUtil.getROMAvailableSize(getActivity());
        String romTotalSize = FileUtil.getROMTotalSize(getActivity());
        LogUtils.e("总空间===" + romTotalSize);
        LogUtils.e("可用空间===" + romAvailableSize);
        bar_mine_use_pace.setRightText("" + FileUtil.getROMAvailableSize(getActivity()));

    }

    private void responseListener() {
        //修改密码
        mChangePassword.setOnClickListener((View v) -> {
            showChangeCurrentUserPasswordPop();
        });
        //版本信息
        bar_mine_version.setOnClickListener((View v) -> {
            showPopVersion();
        });
        togo.setOnClickListener((View v) -> {
            Intent intent = new Intent(getActivity(), TestVlcPlayerActivity.class);
            intent.putExtra("mTitle", "测试界面的标题!");
            startActivity(intent);
        });
        //退出
        bar_mine_exit.setOnClickListener((View v) -> {
            showExitPop();
        });
        //权限说明
        bar_mine_power_explain.setOnClickListener((View v) -> {
            startActivity(new Intent(getActivity(), PowerExplainActivity.class));
        });
        //操作手册
        bar_mine_how_use.setOnClickListener((View v) -> {
            String fileName = getFilePath("TestPDF.pdf");
            startMuPDFActivityWithExampleFile(fileName);
        });
    }

    @NonNull
    private String getFilePath(String fileName) {
        return new File(FILE_DIR + fileName).getAbsolutePath();
    }

    public void startMuPDFActivityWithExampleFile(String fileName) {
        File file = new File(fileName);
        if (!file.isFile()) {
            ToastUtil.showToastCenter(getActivity(), "文件不存在");
            return;
        }
        Uri uri = Uri.fromFile(file);
        FileViewer.startMuPDFActivityByUri(getActivity(), uri);
    }

    ///                                 sdcard/Downloads/test/TestPDF.pdf
    private String getVersionName() {
        // 获取packagemanager的实例
        PackageManager packageManager = getActivity().getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(getActivity().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String version = packInfo.versionName;
        return version;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {//横屏
            int width = ScreenUtils.getScreenWidth(getActivity()) - DensityUtils.dp2px(getActivity(), 100);
            setLayoutParams(70, width);
        } else {                                                           //竖屏
            int width = ScreenUtils.getScreenWidth(getActivity()) - DensityUtils.dp2px(getActivity(), 60);
            setLayoutParams(50, width);

        }
    }

    private void showPopVersion() {
        String showCopyrightYear = "";
        String versionName = getVersionName();
        int year = Calendar.getInstance().get(Calendar.YEAR);

        if ("2020".equals(year + "")) {
            showCopyrightYear = "2020";
        } else {
            showCopyrightYear = "2020" + "-" + year;
        }
        popType = "version";
        versionPop = new PopupWindowVersionTwoButton((Activity) getActivity());
        versionPop.getTv_version().setText("版本：" + versionName);
        versionPop.getTv_copyright().setText("版权所有(C)：" + showCopyrightYear);
        versionPop.getTv_company().setText("江西神州医疗设备有限公司");
        versionPop.getTv_update_date().setText("更新日期：2020年12月");
        versionPop.getTv_ok().setText("确定");
        versionPop.getTv_ok().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                versionPop.dismiss();
            }
        });
        int screenWidth = ScreenSizeUtil.getScreenWidth(getActivity());
        LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (screenWidth >= 1600) {
            mParams.leftMargin = 300;
            mParams.rightMargin = 300;
        } else if (screenWidth == 1080) {
            mParams.leftMargin = 130;
            mParams.rightMargin = 130;
        }
        if (versionPop != null) {
            versionPop.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
            versionPop.getLinear_pop_change().setLayoutParams(mParams);
            versionPop.showPopupWindow(mLinearAll, Gravity.CENTER);
        }
    }

    private void setLayoutParams(int i, int width) {
        LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mParams.leftMargin = i;
        mParams.rightMargin = i;
        switch (popType) {
            case "exit":
                if (exitPop != null && exitPop.isShowing()) {
                    exitPop.dismiss();
                    exitPop.setWidth(width);
                    exitPop.getLinear_pop_change().setLayoutParams(mParams);
                    exitPop.showPopupWindow(mLinearAll, Gravity.CENTER);
                }
                break;
            case "version":
                if (versionPop != null && versionPop.isShowing()) {
                    versionPop.dismiss();
                    versionPop.setWidth(width);
                    versionPop.getLinear_pop_change().setLayoutParams(mParams);
                    versionPop.showPopupWindow(mLinearAll, Gravity.CENTER);
                }
                break;
            case "change":
                if (changePop != null && changePop.isShowing()) {
                    changePop.dismiss();
                    changePop.setWidth(width);
                    changePop.getLinear_pop_change().setLayoutParams(mParams);
                    changePop.showPopupWindow(mLinearAll, Gravity.CENTER);
                }
                break;
        }
    }

    public void setDismissPop() {
        if (changePop != null) {
            changePop.dismiss();
        }
        if (exitPop != null) {
            exitPop.dismiss();
        }
        if (versionPop != null) {
            versionPop.dismiss();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        setDismissPop();
    }

    private void showChangeCurrentUserPasswordPop() {
        popType = "change";
        changePop = new PopupWindowInputChangeTowPassword(getActivity());
        changePop.getMakeSure().setText("确定");
        changePop.getMakeCancle().setText("取消");
        PasswordV2EditText cet_user_old_password = changePop.getCet_user_old_password();
        PasswordV2EditText cet_user_password = changePop.getCet_user_new_password();
        String mCurrentUsername = (String) SharePreferenceUtil.get(getActivity(), SharePreferenceUtil.Current_Username, "");
        String mCurrentPassword = (String) SharePreferenceUtil.get(getActivity(), SharePreferenceUtil.Current_Password, "");
//        current系统用户===123  账户存错了
        LogUtils.e("TAG==current系统用户===" + mCurrentUsername);
        int mCurrenType = (int) SharePreferenceUtil.get(getActivity(), SharePreferenceUtil.Current_UserType, 0);
        UserDBBean mBean = UserDBUtils.queryListByMessageToGetPassword(mCurrentUsername);
        LogUtils.e("TAG==Username===" + mBean.getUsername() + "====password==" + mBean.getPassword() +
                "====Type==" + mBean.getUserType() + "====mBean.getId()==" + mBean.getId());
        changePop.getMakeSure().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //对DB做修改或者增加的操作
                String oldpassword = cet_user_old_password.getText().toString();
                String newpassword = cet_user_password.getText().toString().trim();
                String dbPassword = mBean.getPassword();
//                if (oldpassword.equals("")) {
//                    showToast("原密码不能为空");
//                    return;
//                } else if (newpassword.equals("")) {
//                    showToast("新密码不能为空");
//                    return;
//                } else
                if (oldpassword.equals(dbPassword)) {  //输入的旧密码和DB中密码相同
                    LogUtils.e("TAG==mCurrentUsername===" + mCurrentUsername);
                    SharePreferenceUtil.put(getActivity(), SharePreferenceUtil.Current_Username, mCurrentUsername);
                    SharePreferenceUtil.put(getActivity(), SharePreferenceUtil.Current_Password, newpassword);
                    SharePreferenceUtil.put(getActivity(), SharePreferenceUtil.Current_ID, mBean.getId() + "");
                    UserDBBean userDBBean = new UserDBBean();
                    userDBBean.setUsername(mCurrentUsername);
                    userDBBean.setPassword(newpassword);
                    userDBBean.setUserType(mCurrenType);
                    userDBBean.setId(mBean.getId());
                    LogUtils.e("TAG==添加前oldpassword===" + oldpassword);
                    LogUtils.e("TAG==添加前mCurrentUsername===" + mCurrentUsername + "====newpassword==" + newpassword + "====mCurrenType==" + mCurrenType + "====mBean.getId()==" + mBean.getId());
                    //添加失败
                    UserDBUtils.updateData(userDBBean);
//                    UserDBUtils.insertOrReplaceData(userDBBean);
                    UserDBBean mBean = UserDBUtils.queryListByMessageToGetPassword(mCurrentUsername);
                    LogUtils.e("TAG=修改DB后这个用户的=Username===" + mBean.getUsername() + "====password==" + mBean.getPassword()
                            + "====Type==" + mBean.getUserType() + "====mBean.getId()==" + mBean.getId());
                    showToast("修改成功");
                } else {
                    showToast("原密码输入错误");
                }
                changePop.dismiss();
            }
        });
        changePop.getMakeCancle().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //不用对DB操作
                changePop.dismiss();
            }
        });
        changePop.showPopupWindow(mLinearAll, Gravity.CENTER);
    }

    private void showExitPop() {
        popType = "exit";
        exitPop = new PopupWindowTwoButton((Activity) getActivity());
        exitPop.getTv_content().setText("退出登录?");
        exitPop.getTv_ok().setText("退出");
        exitPop.getTv_cancel().setText("取消");
        exitPop.getTv_ok().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharePreferenceUtil.put(getActivity(), Constants.Is_Logined, false);
                startActivity(new Intent(getActivity(), LoginAnimatorActivity.class));
                String name = (String) SharePreferenceUtil.get(getActivity(), SharePreferenceUtil.Current_Username, "");
                String password = (String) SharePreferenceUtil.get(getActivity(), SharePreferenceUtil.Current_Password, "");
                int type = (int) SharePreferenceUtil.get(getActivity(), SharePreferenceUtil.Current_UserType, 0);
                SharePreferenceUtil.put(getActivity(), Constants.IS_Admin, true);  //只要推出就表示 安装过,初始化admin 用户
                LogUtils.e("TAG====退出==username===" + name + "==password==" + password);
                ActivityCollector.removeAll();
                exitPop.dismiss();
            }
        });
        exitPop.getTv_cancel().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitPop.dismiss();
            }
        });
        exitPop.showPopupWindow(mLinearAll, Gravity.CENTER);
    }
}


