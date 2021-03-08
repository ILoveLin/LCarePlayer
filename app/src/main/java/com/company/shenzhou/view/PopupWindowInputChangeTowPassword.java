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
public class PopupWindowInputChangeTowPassword extends PopupWindow {

    private Activity mContext;
    private PasswordV2EditText cet_user_old_password;
    private PasswordV2EditText cet_user_new_password;
    private TextView tv_two_bt_title;
    private TextView tv_add;
    private TextView makeSure;
    private TextView makeCancle;
    private final LinearLayout linear_pop_change;


    public PopupWindowInputChangeTowPassword(Activity context) {
        this.mContext = context;

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.pop_change_two_password, null);
        linear_pop_change = (LinearLayout) view.findViewById(R.id.linear_pop_change);
        tv_add = (TextView) view.findViewById(R.id.tv_add);
        tv_two_bt_title = (TextView) view.findViewById(R.id.tv_two_bt_title);
        cet_user_old_password = (PasswordV2EditText) view.findViewById(R.id.cet_user_old_password);
        cet_user_new_password = (PasswordV2EditText) view.findViewById(R.id.cet_user_new_password);
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

    public TextView getTv_two_bt_title() {
        return tv_two_bt_title;
    }

    public void setTv_two_bt_title(TextView tv_two_bt_title) {
        this.tv_two_bt_title = tv_two_bt_title;
    }

    public TextView getTv_add() {
        return tv_add;
    }

    public void setTv_add(TextView tv_add) {
        this.tv_add = tv_add;
    }

    public static void setBackgroundAlpha(Activity activity, float bgAlpha) {
        Window window = activity.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        window.setAttributes(lp);
    }

    public PasswordV2EditText getCet_user_old_password() {
        return cet_user_old_password;
    }

    public void setCet_user_old_password(PasswordV2EditText cet_user_old_password) {
        this.cet_user_old_password = cet_user_old_password;
    }

    public PasswordV2EditText getCet_user_new_password() {
        return cet_user_new_password;
    }

    public void setCet_user_new_password(PasswordV2EditText cet_user_new_password) {
        this.cet_user_new_password = cet_user_new_password;
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

    public LinearLayout getLinear_pop_change() {
        return linear_pop_change;
    }

    public void setMakeCancle(TextView makeCancle) {
        this.makeCancle = makeCancle;
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
