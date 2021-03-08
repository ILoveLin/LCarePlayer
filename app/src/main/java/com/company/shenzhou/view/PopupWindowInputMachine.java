package com.company.shenzhou.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.company.shenzhou.R;
import com.company.shenzhou.utils.ClearEditText;
import com.yun.common.utils.DensityUtils;
import com.yun.common.utils.ScreenUtils;

/**
 * LoveLin
 * <p>
 * Describe
 */
public class PopupWindowInputMachine extends PopupWindow {

    private Activity mContext;
    private ClearEditText cet_cme_account;
    private ClearEditText cet_cme_password;
    private ClearEditText cet_cme_title;
    private ClearEditText cet_cme_ip;
    private ClearEditText cet_cme_note_message;
    private ClearEditText cet_cme_port;
    private ClearEditText cet_cme_start_type;
    private TextView makeSure;
    private TextView makeCancle;
    private final LinearLayout linear_pop_change;
    private final ClearEditText cet_cme_mic_port;


    public PopupWindowInputMachine(Activity context) {
        this.mContext = context;

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.pop_addandchange_videomessage, null);
        linear_pop_change = (LinearLayout) view.findViewById(R.id.linear_pop_change);
        cet_cme_account = (ClearEditText) view.findViewById(R.id.cet_cme_account);
        cet_cme_password = (ClearEditText) view.findViewById(R.id.cet_cme_password);
        cet_cme_title = (ClearEditText) view.findViewById(R.id.cet_cme_title);
        cet_cme_ip = (ClearEditText) view.findViewById(R.id.cet_cme_ip);
        cet_cme_note_message = (ClearEditText) view.findViewById(R.id.cet_cme_note_message);
        cet_cme_port = (ClearEditText) view.findViewById(R.id.cet_cme_port);
        cet_cme_mic_port = (ClearEditText) view.findViewById(R.id.cet_cme_mic_port);
//        cet_cme_start_type = (ClearEditText) view.findViewById(R.id.cet_cme_start_type);
        makeSure = (TextView) view.findViewById(R.id.tv_bt_ok);
        makeCancle = (TextView) view.findViewById(R.id.tv_bt_cancel);


        int w = ScreenUtils.getScreenWidth(mContext) - DensityUtils.dp2px(mContext, 60);
        int h = context.getWindowManager().getDefaultDisplay().getHeight();
        // 设置SelectPicPopupWindow的View
        this.setContentView(view);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(w);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        this.setBackgroundDrawable(new BitmapDrawable());
        // mPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
        // 设置SelectPicPopupWindow弹出窗体动画效果

    }

    public LinearLayout getLinear_pop_change() {
        return linear_pop_change;
    }

    /**
     * 显示popupWindow
     *
     * @param parent
     */
    public void showPopupWindow(View parent, int gravity) {
        if (!this.isShowing()) {
            // 以下拉方式显示popupwindow
            setBackgroundAlpha(mContext, 0.5f);
            this.showAtLocation(parent, gravity, 0, 0);
        } else {
            this.dismiss();
        }

    }

    @Override
    public void dismiss() {
        super.dismiss();
        setBackgroundAlpha(mContext, 1f);
    }

    public static void setBackgroundAlpha(Activity activity, float bgAlpha) {
        Window window = activity.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        window.setAttributes(lp);
    }
    public TextView getMakeSure() {
        return makeSure;
    }

    public void setMakeSure(TextView makeSure) {
        this.makeSure = makeSure;
    }

    public TextView getMakeCancle() {
        return makeCancle;
    }

    public void setMakeCancle(TextView makeCancle) {
        this.makeCancle = makeCancle;
    }

    public ClearEditText getCet_cme_account() {
        return cet_cme_account;
    }

    public void setCet_cme_account(ClearEditText cet_cme_account) {
        this.cet_cme_account = cet_cme_account;
    }

    public ClearEditText getCet_cme_mic_port() {
        return cet_cme_mic_port;
    }

    public ClearEditText getCet_cme_password() {
        return cet_cme_password;
    }

    public void setCet_cme_password(ClearEditText cet_cme_password) {
        this.cet_cme_password = cet_cme_password;
    }

    public ClearEditText getCet_cme_title() {
        return cet_cme_title;
    }

    public void setCet_cme_title(ClearEditText cet_cme_title) {
        this.cet_cme_title = cet_cme_title;
    }

    public ClearEditText getCet_cme_ip() {
        return cet_cme_ip;
    }

    public void setCet_cme_ip(ClearEditText cet_cme_ip) {
        this.cet_cme_ip = cet_cme_ip;
    }

    public ClearEditText getCet_cme_note_message() {
        return cet_cme_note_message;
    }

    public void setCet_cme_note_message(ClearEditText cet_cme_note_message) {
        this.cet_cme_note_message = cet_cme_note_message;
    }

    public ClearEditText getCet_cme_port() {
        return cet_cme_port;
    }

    public void setCet_cme_port(ClearEditText cet_cme_port) {
        this.cet_cme_port = cet_cme_port;
    }

    public ClearEditText getCet_cme_start_type() {
        return cet_cme_start_type;
    }

    public void setCet_cme_start_type(ClearEditText cet_cme_start_type) {
        this.cet_cme_start_type = cet_cme_start_type;
    }

    /**
     * 使用教程
     */
//    private void showSaveDialog() {
//        final PopupWindowTwoButton twoButton = new PopupWindowTwoButton((Activity) mContext);
//        twoButton.getTv_content().setText("您确定退出吗？");
//        twoButton.getTv_ok().setText("确定");
//        twoButton.getTv_cancel().setText("取消");
//        twoButton.getTv_ok().setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                SharePreferenceUtil.put(mContext, Global.LOGIN_FLAG, false);
//                SharePreferenceUtil.put(mContext, Global.KEY_ID, "");
//                mView.showMyToast("退出成功");
//                twoButton.dismiss();
//                mView.openActivity();
//            }
//        });
//        twoButton.getTv_cancel().setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                twoButton.dismiss();
//            }
//        });
//        twoButton.showPopupWindow(mView.getExitView(), Gravity.CENTER);
//    }
}
