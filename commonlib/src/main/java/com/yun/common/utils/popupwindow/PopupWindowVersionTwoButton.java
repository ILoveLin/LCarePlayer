package com.yun.common.utils.popupwindow;

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

import com.yun.common.R;
import com.yun.common.utils.DensityUtils;
import com.yun.common.utils.ScreenUtils;


/**
 * 作者：xujm on 2015/12/29
 * 备注：com.lzyc.framwork.widget.popupwindow
 */
public class PopupWindowVersionTwoButton extends PopupWindow {

    private Activity mContext;
    private TextView tv_title;
    private TextView tv_content;
    private TextView tv_ok;
    private View line_two;
    private final LinearLayout linear_pop_change;
    private final TextView tv_update_date;
    private final TextView tv_company;
    private final TextView tv_copyright;
    private final TextView tv_version;

    public TextView getTv_title() {
        return tv_title;
    }

    public void setTv_title(TextView tv_title) {
        this.tv_title = tv_title;
    }

    public TextView getTv_content() {
        return tv_content;
    }

    public void setTv_content(TextView tv_content) {
        this.tv_content = tv_content;
    }

    public TextView getTv_ok() {
        return tv_ok;
    }

    public View getLine_two() {
        return line_two;
    }

    public void setLine_two(View line_two) {
        this.line_two = line_two;
    }

    public void setTv_ok(TextView tv_ok) {
        this.tv_ok = tv_ok;
    }

    public Activity getmContext() {
        return mContext;
    }

    public void setmContext(Activity mContext) {
        this.mContext = mContext;
    }

    public TextView getTv_update_date() {
        return tv_update_date;
    }

    public TextView getTv_company() {
        return tv_company;
    }

    public TextView getTv_copyright() {
        return tv_copyright;
    }

    public TextView getTv_version() {
        return tv_version;
    }

    public PopupWindowVersionTwoButton(Activity context) {
        this.mContext = context;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View conentView = inflater.inflate(R.layout.popup_my_two_button, null);
        linear_pop_change = (LinearLayout) conentView.findViewById(R.id.linear_pop_change);
        tv_title = (TextView) conentView.findViewById(R.id.tv_two_bt_title);
//        tv_content = (TextView) conentView.findViewById(R.id.tv_two_bt_content);
//        tv_cancel = (TextView) conentView.findViewById(R.id.tv_two_bt_cancel);
        tv_ok = (TextView) conentView.findViewById(R.id.tv_two_bt_ok);

        tv_version = (TextView) conentView.findViewById(R.id.tv_version);
        tv_copyright = (TextView) conentView.findViewById(R.id.tv_copyright);
        tv_company = (TextView) conentView.findViewById(R.id.tv_company);
        tv_update_date = (TextView) conentView.findViewById(R.id.tv_update_date);


        line_two = conentView.findViewById(R.id.line_two);
        int w = ScreenUtils.getScreenWidth(mContext) - DensityUtils.dp2px(mContext, 60);
        int h = context.getWindowManager().getDefaultDisplay().getHeight();
        // 设置SelectPicPopupWindow的View
        this.setContentView(conentView);
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
}
