package com.company.shenzhou.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.company.shenzhou.R;
import com.company.shenzhou.view.pagestate.PageManager;
import com.company.shenzhou.utils.NetworkUtil;
import com.yun.common.contant.Constants;
import com.yun.common.utils.SharePreferenceUtil;
import com.yun.common.utils.ToastUtil;
import com.yun.common.utils.popupwindow.PopupWindowTwoButton;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Lovelin on 2019/4/27
 * <p>
 * Describe:
 */
public abstract class BaseFragment extends Fragment {
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
    @BindView(R.id.rl_top)
    RelativeLayout mTitleLayout;
    Unbinder unbinder;
    @BindString(R.string.empty_error_net_server)
    public String errorServer;
    private ViewGroup mRootView;
    private View mContentView;
    private Context mContext;
    private PageManager mPageManager;
    private PopupWindowTwoButton popLoginButton;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.activity_base_layout, container, false);
        mRootView = rootView;
        mContentView = LayoutInflater.from(getActivity()).inflate(getContentViewId(), (ViewGroup) rootView.findViewById(R.id.root_layout), false);
        rootView.addView(mContentView);
        //????????????????????? ???????????? https://github.com/hss01248/PageStateManager
        //?????????????????????pageManager ??????????????????pagerManager????????????????????? setPageStateView??????setCustomPageStateView
        //???????????????????????????loading??????error??????????????????????????????????????????
//        PageManager.initInApp(getActivity().getApplicationContext());
//        mPageManager = PageManager.init(mContentView, false, new Runnable() {
//            @Override
//            public void run() {
//                onClickRetry();
//            }
//        });
//        //?????????????????? ???????????? https://github.com/JakeWharton/butterknife
        mContext = getActivity();
//        if (httpRequest == null) {
//            httpRequest = new BaseService();
//        }
//        setTitleBarVisibility(View.GONE);
        unbinder = ButterKnife.bind(this, rootView);
        init(rootView);
        return rootView;
    }

    /**
     * ????????????layout?????????
     *
     * @return ??????layout
     */
    public abstract int getContentViewId();

    /**
     * ?????????onCreate ????????????????????????
     * @param rootView
     */
    protected abstract void init(ViewGroup rootView);

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onDestroy() {
//        unbinder.unbind();
        super.onDestroy();
    }

    //------------------------title_bar ??????-----------------

    /**
     * ?????????????????????
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
     * ???????????????????????????
     *
     * @param visibility
     */
    public void setTitleBarVisibility(int visibility) {
        mTitleLayout.setVisibility(visibility);
    }

    /**
     * ??????????????????
     *
     * @param resColor
     */
    public void setTitleBackrounColor(int resColor) {
        mTitleLayout.setBackgroundColor(getResources().getColor(resColor));
    }

    /**
     * ??????????????????????????????
     *
     * @param visibility
     */
    public void setTitleLeftBtnVisibility(int visibility) {
        mTitleLeftBtn.setVisibility(visibility);
    }

    /**
     * ??????????????????????????????(????????????)
     *
     * @param visibility
     */
    public void setTitleRegisterLeftBtnVisibility(int visibility) {
        mTitleRegisterLeftBtn.setVisibility(visibility);
    }

    /**
     * ????????????????????????imageBtn??????
     *
     * @param visibility
     */
    public void setTitleRightBtnVisibility(int visibility) {
        mTitleRightBtn.setVisibility(visibility);

    }

    /**
     * ????????????imageBtn????????????
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
     * ????????????????????????????????????
     *
     * @param imgId
     */
    public void setTitleRightTvbtnBackgroundResources(int imgId) {
        mTitleRightTvBtn.setBackgroundResource(imgId);
        mTitleRightTvBtn.setVisibility(View.VISIBLE);
    }

    /**
     * ??????????????????????????????
     *
     * @param name
     */
    public void setTitleRightTvbtnName(String name) {
        mTitleRightTvBtn.setText(name);
        mTitleRightTvBtn.setVisibility(View.VISIBLE);
    }

    /**
     * ????????????????????????
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
     * title ????????????????????????
     *
     * @param v
     */
    @OnClick(R.id.ib_left)
    public void onClickTitleLeftBtn(View v) {

    }

    /**
     * title ??????????????????????????????
     *
     * @param v
     */
    @OnClick(R.id.ib_left_1)
    public void onClickTitleRegisterLeftBtn(View v) {

    }

    /**
     * title ??????tv??????????????????
     *
     * @param v
     */
    @OnClick(R.id.tv_right)
    public void onClickTitleRightTvBtn(View v) {

    }

    /**
     * title ????????????????????????
     *
     * @param v
     */
    @OnClick(R.id.ib_right)
    public void onClickTitleRightBtn(View v) {

    }

    //------------------------ ?????????????????? -----------------

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
     * ????????????
     * <p>
     * true  ??????  false ?????????
     */
    protected boolean popupLogin() {
        if (TextUtils.isEmpty(SharePreferenceUtil.get(mContext, SharePreferenceUtil.UID, "").toString())) {

            if (null == popLoginButton) {
                popLoginButton = new PopupWindowTwoButton(getActivity());
            }

            popLoginButton.getTv_content().setText("????????????????????????");
            popLoginButton.getTv_ok().setText("??????");
            popLoginButton.getTv_cancel().setText("??????");
            popLoginButton.getTv_ok().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    startActivity(Intents.openLoginActivity(mContext));     //?????????????????????
                    popLoginButton.dismiss();
                }
            });
            popLoginButton.getTv_cancel().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popLoginButton.dismiss();
                }
            });
            popLoginButton.showPopupWindow(new View(mContext), Gravity.CENTER);

            return false;
        }
        return true;
    }

    /**
     * ??????error ??????trybtn?????????
     *
     * @param resid
     */
    public void setErrorTryBtnBg(int resid) {
        if (mPageManager != null)
            mPageManager.setErrorTryBtnBg(resid);
    }

    /**
     * ???????????????????????????
     */
    public void showErrorServer() {
        mPageManager.showEmpty(getResources().getString(R.string.empty_error_net_server), R.mipmap.empty_error_net_server);
    }


    /**
     * ????????????????????????
     */
    public void setPageStateView() {
        PageManager.initInApp(getActivity().getApplicationContext());
        mPageManager = PageManager.init(mContentView, false, new Runnable() {
            @Override
            public void run() {
                onClickRetry();
            }
        });
        if (mPageManager.BASE_LOADING_LAYOUT_ID == R.layout.page_loading) {
            ImageView loadingView = (ImageView) mRootView.findViewById(R.id.page_loading_iv);
            AnimationDrawable anim = (AnimationDrawable) loadingView.getBackground();
            anim.start();
        }
    }


    /**
     * ?????????view?????????loadding
     */
    public void setPageStateView(View view) {
        PageManager.initInApp(getActivity().getApplicationContext());
        mPageManager = PageManager.init(view, false, new Runnable() {
            @Override
            public void run() {
                onClickRetry();
            }
        });
        if (mPageManager.BASE_LOADING_LAYOUT_ID == R.layout.page_loading) {
            ImageView loadingView = (ImageView) mRootView.findViewById(R.id.page_loading_iv);
            AnimationDrawable anim = (AnimationDrawable) loadingView.getBackground();
            anim.start();
        }
    }


    /**
     * ???????????????????????????
     *
     * @param layoutIdOfEmpty
     * @param layoutIdOfLoading
     * @param layoutIdOfError
     */
    public void setCustomPageStateView(int layoutIdOfEmpty, int layoutIdOfLoading, int layoutIdOfError) {
        PageManager.initInApp(getActivity().getApplicationContext(), layoutIdOfEmpty, layoutIdOfLoading, layoutIdOfError);
        mPageManager = PageManager.init(mContentView, false, new Runnable() {
            @Override
            public void run() {
                onClickRetry();
            }
        });
        if (layoutIdOfLoading == R.layout.page_loading) {
            ImageView loadingView = (ImageView) mRootView.findViewById(R.id.page_loading_iv);
            AnimationDrawable anim = (AnimationDrawable) loadingView.getBackground();
            anim.start();
        }
    }

    /**
     * ?????????????????????
     */
    protected void onClickRetry() {
        if (NetworkUtil.CheckConnection(getActivity())) {
            showContent();
        } else {
            showError();
        }
    }

    //-----------------------------????????????---------------------------------------------

    /**
     * ??????activity
     *
     * @param ActivityClass
     */
    public void openActivity(Class<? extends Activity> ActivityClass) {
        Intent intent = new Intent(getActivity(), ActivityClass);
        startActivity(intent);
    }


    /**
     * ??????activity
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
     * ??????activity
     *
     * @param ActivityClass
     * @param b
     */
    public void openActivity(Class<? extends Activity> ActivityClass, Bundle b) {
        Intent intent = new Intent(getActivity(), ActivityClass);
        intent.putExtras(b);
        startActivity(intent);
    }

    /**
     * ??????toast??????
     *
     * @param text
     */
    public void showToast(String text) {
        ToastUtil.showToastCenter(getActivity(), text);
    }

    /**
     * ??????????????????
     *
     * @param v
     * @param msg
     */
    public void showPrompt(View v, String msg) {
        final PopupWindowTwoButton twoButton = new PopupWindowTwoButton(getActivity());
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
     * ?????????????????????
     */
    public void showNetDialog() {
        final PopupWindowTwoButton twoButton = new PopupWindowTwoButton(getActivity());
        twoButton.getTv_title().setVisibility(View.GONE);
        twoButton.getTv_content().setText("???????????????????????????????????????????????????");
        twoButton.getTv_ok().setText("?????????");
        twoButton.getTv_cancel().setText("??????");
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
}
