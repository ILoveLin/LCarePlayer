package com.yun.common.utils.popupwindow;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.yun.common.R;


/**
 * BasePopupWindow
 * 作者：xujm on 2015/12/29
 * 备注：com.lzyc.framwork.widget.popupwindow
 */
public class BasePopupWindow extends PopupWindow {

    private Activity mContext;
    private View conentView;

    public View getConentView() {
        return conentView;
    }

    public void setConentView(View conentView) {
        this.conentView = conentView;
    }

    public BasePopupWindow(Activity context, int itemLayoutId, int w, int h) {
        this.mContext = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        conentView = inflater.inflate(itemLayoutId, null);
//        int h = context.getWindowManager().getDefaultDisplay().getHeight();
//        int w = context.getWindowManager().getDefaultDisplay().getWidth();
        // 设置SelectPicPopupWindow的View
        this.setContentView(conentView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(w);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(h);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setOutsideTouchable(false);
        this.setFocusable(true);
        this.setBackgroundDrawable(new BitmapDrawable());
        this.setTouchable(true);
        this.update();
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        // mPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
        // 设置SelectPicPopupWindow弹出窗体动画效果
    }


    /**
     * 显示popupWindow
     * @param parent
     */
    public void showPopupWindow(View parent, int gravity) {
        if (!this.isShowing()) {
            // 以下拉方式显示popupwindow
            setBackgroundAlpha(mContext, 0.5f);
            setAnimationStyle(R.style.popup_anim_style);
            this.showAtLocation(parent, gravity, 0, 0);
        } else {
            this.dismiss();
        }

    }


    /**
     * 显示popupWindow
     *
     * @param parent
     */
    public void showDropDownPopupWindow(View parent, int x, int y) {
        if (!this.isShowing()) {
            // 以下拉方式显示popupwindow
            setBackgroundAlpha(mContext, 0.5f);
            setAnimationStyle(R.style.popup_anim_style);
            this.showAsDropDown(parent, x, y);
        } else {
            this.dismiss();
        }

    }

    /**
     * 右侧下拉popupWindow
     *
     * @param parent
     */
    public void showDropPopRight(View parent, int gravity) {
        if (!this.isShowing()) {
            // 以下拉方式显示popupwindow
            setBackgroundAlpha(mContext, 0.5f);
            setAnimationStyle(R.style.popup_anim_style_bootom_right);
            this.showAtLocation(parent, gravity, 0, 0);
        } else {
            this.dismiss();
        }
    }
    public static void setBackgroundAlpha(Activity activity, float bgAlpha) {
        Window window = activity.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        window.setAttributes(lp);
    }

    /**
     * 左侧下拉popupWindow
     *
     * @param parent
     */
    public void showDropPopLeft(View parent, int gravity) {
        if (!this.isShowing()) {
            setBackgroundAlpha(mContext, 0.5f);
            setAnimationStyle(R.style.popup_anim_style_bootom_left);
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

}
