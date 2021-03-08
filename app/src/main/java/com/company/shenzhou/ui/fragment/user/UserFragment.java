package com.company.shenzhou.ui.fragment.user;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.company.shenzhou.R;
import com.company.shenzhou.base.ActivityCollector;
import com.company.shenzhou.base.BaseFragment;
import com.company.shenzhou.bean.Constants;
import com.company.shenzhou.bean.dbbean.UserDBRememberBean;
import com.company.shenzhou.ui.activity.login.LoginAnimatorActivity;
import com.company.shenzhou.ui.fragment.user.adapter.UserAdapter;
import com.company.shenzhou.utils.db.UserDBRememberBeanUtils;
import com.company.shenzhou.utils.db.UserDBUtils;
import com.company.shenzhou.view.PopupWindowInputChangePassword;
import com.company.shenzhou.view.PopupWindowInputUser;
import com.company.shenzhou.view.SwitchButton;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.yun.common.utils.DensityUtils;
import com.yun.common.utils.LogUtils;
import com.yun.common.utils.ScreenUtils;
import com.yun.common.utils.SharePreferenceUtil;
import com.yun.common.utils.ToastUtil;
import com.yun.common.utils.popupwindow.PopupWindowTwoButton;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * LoveLin
 * <p> case "refresh":
 * mDataList.clear();
 * if (newBeanList != null && newBeanList.size() != 0) {
 * mDataList.addAll(newBeanList);
 * }
 * mSmartRefresh.finishRefresh();
 * mAdapter.notifyDataSetChanged();
 * <p>
 * break;
 * case "loadMore":
 * if (null != newBeanList) {
 * mDataList.addAll(newBeanList);
 * mSmartRefresh.setNoMoreData(false);
 * } else {
 * mSmartRefresh.setNoMoreData(true);
 * }
 * mSmartRefresh.finishLoadMore();
 * mAdapter.notifyDataSetChanged();
 * break;
 * Describe 设备管理界面
 */
public class UserFragment extends BaseFragment implements UserAdapter.ClickCallBack {
    @BindView(R.id.fake_status_bar)
    View mFakeStatusBar;
    //    @BindView(R.id.btn_user_add)
//    AppCompatButton mUserAdd;
    @BindView(R.id.ib_right_user)
    ImageButton mUserAdd;
    @BindView(R.id.page_empty_desc)
    TextView mEmpty;
    @BindView(R.id.tv_username)
    TextView mUsername;
    @BindView(R.id.linear_all_user)
    LinearLayout mLinearAll;
    @BindView(R.id.knowRecycleview)
    RecyclerView mRecyclerView;
    @BindView(R.id.smartRefresh)
    SmartRefreshLayout mSmartRefresh;
    private ArrayList<UserDBRememberBean> mListData = new ArrayList<>();
    private LinearLayoutManager mLinearLayoutManager;
    private UserAdapter mAdapter;
    private int currentUserType;
    private String current_username;
    private PopupWindowTwoButton deleteYourselfPop;
    private PopupWindowInputChangePassword changePasswordPop;
    private PopupWindowInputUser addUserPop;
    private String popType = "";
    private android.os.Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    showEmptyOrContentView(mListData);
                    break;

            }
        }
    };

    @Override
    public int getContentViewId() {
        return R.layout.fragment_user;
    }

    @Override
    protected void init(ViewGroup rootView) {
        setTitleBarVisibility(View.VISIBLE);
        setTitleLeftBtnVisibility(View.GONE);
        setTitleRightBtnVisibility(View.VISIBLE);
        setTitleRightBtnResources(R.drawable.selector_add_something);
        setTitleName("用户");
        setPageStateView();
        initView();
    }

    private void initView() {
        initData();
        responseListener();
        showContent();

    }

    private void initData() {
        String username = (String) SharePreferenceUtil.get(getActivity(), SharePreferenceUtil.Current_Username, "");
        mUsername.setText(username + "");
        mListData = new ArrayList<UserDBRememberBean>();
        //查询数据库所有的用户表
        currentUserType = (int) SharePreferenceUtil.get(getActivity(), SharePreferenceUtil.Current_UserType, 0);
        current_username = (String) SharePreferenceUtil.get(getActivity(), SharePreferenceUtil.Current_Username, "");
        showLoading();
        startThreadReadDBData();
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        mRecyclerView.addItemDecoration(new RecycleViewDivider(getActivity(), 1, R.drawable.recicleview_divider_line));
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mAdapter = new UserAdapter(mListData, getActivity());
        mAdapter.setClickCallBack(this);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void startThreadReadDBData() {
        new Thread(() -> {
            try {
                List mList = UserDBUtils.queryAll(UserDBRememberBean.class);
                mHandler.sendEmptyMessage(0);
                mListData.addAll(mList);
            } catch (Exception e) {
                LogUtils.e("UserFragment.java:159,数据库字段更新,请重新卸载App,在安装新的App");
            }
        }).start(); // 启动线程

    }

    @Override
    public void onItemCallBack(UserDBRememberBean bean, ArrayList<UserDBRememberBean> mList, String type) {
        //type  delete   default   update
        //mAdapter.notifyDataSetChanged();
        showEmptyOrContentView(mList);
        int userType = bean.getUserType();
        if ("password".equals(type)) {  //修改密码
            //先查询获取当前用户权限  再来做相对于操作
            switch (currentUserType) {  //当前用户权限 ---->只有超级用户才可以修改，其他用户去我的里面修改
                case 0:  //普通用户
//                    if (currentUserType == userType && current_username.equals(bean.getUsername())) { //当前是0权限，并且点击的是自己的item才能弹出修改框框
//                        showChangePasswordPop(bean, "0");
//                    } else {
//                        showToast("普通用户只能修改自己的密码");
//                    }
                    showToast("普通或者权限用户只能在我的界面修改自己的密码哦！");
                    break;
                case 1:  //权限用户
                    if (currentUserType > userType) {
                        //修改密码
                        showChangePasswordPop(bean, "1");
                    } else {
                        showToast("权限等级不够");
                    }
                    break;
                case 2:  //超级用户
                    showChangePasswordPop(bean, "2");
                    break;
            }
        } else if ("delete".equals(type)) { //删除用户
            switch (currentUserType) { //当前用户权限
                case 0:  //普通用户
                    showToast("您权限最低，不能删除任何人，包括您自己!");
                    break;
                case 1:  //权限用户
                    if (currentUserType > userType) {  //大于权限才可以删除
                        showPopDelete(bean, 1);
//                        UserDBUtils.deleteData(bean);
//                        mAdapter.setListAndNotifyDataSetChanged();
                    } else if (currentUserType == userType) {
                        String currentUsername = (String) SharePreferenceUtil.get(getActivity(), SharePreferenceUtil.Current_Username, "");
                        if (currentUsername.equals(bean.getUsername())) {
                            showPopDeleteYourself(bean);
                        } else {
                            showToast("您不能删除别人哦!");
                        }
                    } else {
                        showToast("用户权限不够!");

                    }
                    break;
                case 2:  //超级用户
                    if ("admin".equals(bean.getUsername())) {  //超级用户不能删除自己
                        showToast("超级用户不能删除自己哦!");
                    } else {
                        showPopDelete(bean, 2);
                    }
                    break;

            }
        }
    }

    /**
     * 删除用户
     *
     * @param bean
     * @param type 用户类型
     */
    private void showPopDelete(UserDBRememberBean bean, int type) {
        PopupWindowTwoButton deletePop = new PopupWindowTwoButton((Activity) getActivity());
        deletePop.getTv_content().setText("是否确认删除该用户?");
        deletePop.getTv_ok().setText("确定");
        deletePop.getTv_cancel().setText("取消");
        deletePop.getTv_ok().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserDBRememberBeanUtils.deleteData(bean);
                mAdapter.setListAndNotifyDataSetChanged();
                deletePop.dismiss();
            }
        });
        deletePop.getTv_cancel().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletePop.dismiss();
            }
        });
        deletePop.showPopupWindow(mLinearAll, Gravity.CENTER);

    }


    @Override
    public void onItemChecked(UserDBRememberBean bean, SwitchButton slide_switch, boolean type) {
        if (mRecyclerView.isComputingLayout()) {
            mRecyclerView.post(new Runnable() {
                @Override
                public void run() {
                    slide_switch.setChecked(type);
                }
            });
        } else {
            slide_switch.setChecked(type);
        }

    }

    @Override
    public void onPause() {
        super.onPause();
//        SharePreferenceUtil.put(getActivity(), SharePreferenceUtil.Current_ToastShow, "No");
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onItemToast(String str) {
        ToastUtil.showToastCenter(getActivity(), "" + str);

//        String no = (String) SharePreferenceUtil.get(getActivity(), SharePreferenceUtil.Current_ToastShow, "1");
//        if (no.equals("No")){
//            SharePreferenceUtil.put(getActivity(), SharePreferenceUtil.Current_ToastShow, "No");
//        }

    }


    //deleteYourselfPop    changePasswordPop   addUserPop
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {//横屏
            int width = ScreenUtils.getScreenWidth(getActivity()) - DensityUtils.dp2px(getActivity(), 100);
            LogUtils.e("TAG====横屏====" + width);
            setLayoutParams(70, width);
        } else {                                                           //竖屏
            int width = ScreenUtils.getScreenWidth(getActivity()) - DensityUtils.dp2px(getActivity(), 60);
            LogUtils.e("TAG====竖屏====" + width);
            setLayoutParams(50, width);

        }
    }

    private void setLayoutParams(int i, int width) {
        LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mParams.leftMargin = i;
        mParams.rightMargin = i;
        switch (popType) {
            case "add":
                if (addUserPop != null && addUserPop.isShowing()) {
                    addUserPop.dismiss();
                    addUserPop.setWidth(width);
                    addUserPop.getLinear_pop_change().setLayoutParams(mParams);
                    addUserPop.showPopupWindow(mLinearAll, Gravity.CENTER);
                }
                break;
            case "delete":
                if (deleteYourselfPop != null && deleteYourselfPop.isShowing()) {
                    deleteYourselfPop.dismiss();
                    deleteYourselfPop.setWidth(width);
                    deleteYourselfPop.getLinear_pop_change().setLayoutParams(mParams);
                    deleteYourselfPop.showPopupWindow(mLinearAll, Gravity.CENTER);
                }
                break;
            case "change":
                if (changePasswordPop != null && changePasswordPop.isShowing()) {
                    changePasswordPop.dismiss();
                    changePasswordPop.setWidth(width);
                    changePasswordPop.getLinear_pop_change().setLayoutParams(mParams);
                    changePasswordPop.showPopupWindow(mLinearAll, Gravity.CENTER);
                }
                break;

        }
    }

    public void setDismissPop() {
        if (deleteYourselfPop != null) {
            deleteYourselfPop.dismiss();
        }
        if (changePasswordPop != null) {
            changePasswordPop.dismiss();
        }
        if (addUserPop != null) {
            addUserPop.dismiss();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        setDismissPop();
    }

    private void showAddUserPop(int type) {
        popType = "add";
        addUserPop = new PopupWindowInputUser(getActivity());
        addUserPop.getMakeSure().setText("确定");
        addUserPop.getMakeCancle().setText("取消");
        addUserPop.getMakeSure().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //对DB做修改或者增加的操作
                String username = addUserPop.getCet_user_username().getText().toString().trim();
                String password = addUserPop.getCet_user_password().getText().toString().trim();
                boolean isExist = UserDBUtils.queryListIsExist(username);
                if ("".equals(username)) {
                    showToast("用户名不能为空");
                } else if (isExist) {
                    showToast("该用户已存在");
                } else {
                    UserDBRememberBean bean = new UserDBRememberBean();
                    bean.setUsername(username);
                    bean.setPassword(password);
                    bean.setTag(username);
                    bean.setRemember("No");
                    bean.setUserType(0);
                    UserDBRememberBeanUtils.insertOrReplaceData(bean);
//                    showToast("添加成功" + "username==" + username + "password==" + password);
                    List currentList = UserDBUtils.queryAll(UserDBRememberBean.class);
                    for (int i = 0; i < currentList.size(); i++) {
                        UserDBRememberBean o = (UserDBRememberBean) currentList.get(i);
                        LogUtils.e("存储的数据" + o.getUsername());
                        LogUtils.e("存储的数据" + o.getPassword());
                        LogUtils.e("存储的数据" + o.getRemember());
                    }
                    showEmptyOrContentView((ArrayList<UserDBRememberBean>) currentList);
                    mAdapter.setListAndNotifyDataSetChanged();
                }
                addUserPop.dismiss();
            }
        });
        addUserPop.getMakeCancle().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //不用对DB操作
                addUserPop.dismiss();
            }
        });
        addUserPop.showPopupWindow(mLinearAll, Gravity.CENTER);
    }

    private void showChangePasswordPop(UserDBRememberBean bean, String type) {
        popType = "change";
        changePasswordPop = new PopupWindowInputChangePassword(getActivity());
        changePasswordPop.getMakeSure().setText("确定");
        changePasswordPop.getMakeSure().setText("确定");
        changePasswordPop.getMakeCancle().setText("取消");
        changePasswordPop.getMakeSure().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //对DB做修改或者增加的操作
                //VideoDBBean beanData = getBeanData();
                String newPassword = changePasswordPop.getCet_user_password().getText().toString().trim();
                bean.setPassword(newPassword);
                UserDBRememberBeanUtils.updateData(bean);
                mAdapter.setListAndNotifyDataSetChanged();
                if (type.equals("2")) {
                    SharePreferenceUtil.put(getActivity(), SharePreferenceUtil.Current_Admin_ChangePassword, true);
                }
                showToast("密码修改成功");
                changePasswordPop.dismiss();
            }
        });
        changePasswordPop.getMakeCancle().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //不用对DB操作
                changePasswordPop.dismiss();
            }
        });
        changePasswordPop.showPopupWindow(mLinearAll, Gravity.CENTER);
    }

    private void showPopDeleteYourself(UserDBRememberBean bean) {
        popType = "delete";
        deleteYourselfPop = new PopupWindowTwoButton((Activity) getActivity());
        deleteYourselfPop.getTv_content().setText("是否确认删除你自己?");
        deleteYourselfPop.getTv_ok().setText("确定");
        deleteYourselfPop.getTv_cancel().setText("取消");
        deleteYourselfPop.getTv_ok().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserDBRememberBeanUtils.deleteData(bean);
                mAdapter.setListAndNotifyDataSetChanged();
                SharePreferenceUtil.put(getActivity(), Constants.Is_Logined, false);
                startActivity(new Intent(getActivity(), LoginAnimatorActivity.class));
                String name = (String) SharePreferenceUtil.get(getActivity(), SharePreferenceUtil.Current_Username, "");
                int type = (int) SharePreferenceUtil.get(getActivity(), SharePreferenceUtil.Current_UserType, 0);
                LogUtils.e("TAG====是否确认删除你自己==username===" + name + "==type==" + type);
                ActivityCollector.removeAll();
                deleteYourselfPop.dismiss();
            }
        });
        deleteYourselfPop.getTv_cancel().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteYourselfPop.dismiss();
            }
        });
        deleteYourselfPop.showPopupWindow(mLinearAll, Gravity.CENTER);
    }

    private void showEmptyOrContentView(ArrayList<UserDBRememberBean> mList) {
        if (mList.size() == 0) {
            mEmpty.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.INVISIBLE);
        } else {
            mEmpty.setVisibility(View.INVISIBLE);
            mRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    private void responseListener() {
        //添加用户
        getRightView().setOnClickListener((View v) -> {
            if (currentUserType == 0) { //普通用户
                showToast("无添加用户权限");
            } else if (currentUserType == 1) {
                showAddUserPop(currentUserType);
            } else if (currentUserType == 2) {
                showAddUserPop(currentUserType);
            }
        });
    }

}
