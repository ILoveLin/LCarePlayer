package com.yun.common.utils.popupwindow;

import android.widget.PopupWindow;

/**
 * Created by Lovelin on 2019/4/4
 * <p>
 * Describe:
 */
public class CustomPopWindow extends PopupWindow {
//    private PopupWindow popupView;
//    private PopupWindow popupWindow;
//
//    public void showUpPop() {
//        if (popupWindow == null) {
//            popupView = View.inflate(this, R.layout.popwindow_avatar, null);
//            // 参数2,3：指明popupwindow的宽度和高度
//            popupWindow = new PopupWindow(popupView, WindowManager.LayoutParams.MATCH_PARENT,
//                    WindowManager.LayoutParams.WRAP_CONTENT);
//
//            popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
//                @Override
//                public void onDismiss() {
//                    //隐藏遮罩
//                    v_mask.setVisibility(View.GONE);
//                }
//            });
//
//            // 设置背景图片， 必须设置，不然动画没作用
//            popupWindow.setBackgroundDrawable(new BitmapDrawable());
//            popupWindow.setFocusable(true);
//            // 设置点击popupwindow外屏幕其它地方消失
//            popupWindow.setOutsideTouchable(true);
//            // 平移动画相对于手机屏幕的底部开始，X轴不变，Y轴从1变0
//            animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT, 0,
//                    Animation.RELATIVE_TO_PARENT, 1, Animation.RELATIVE_TO_PARENT, 0);
//            animation.setInterpolator(new AccelerateInterpolator());
//            animation.setDuration(200);
//        }
//
//        //设置按钮点击监听
//        popupView.findViewById(R.id.close_popup).setOnClickListener(this);
//        popupView.findViewById(R.id.open_photos).setOnClickListener(this);
//        popupView.findViewById(R.id.open_camera).setOnClickListener(this);
//
//        // 设置popupWindow的显示位置，此处是在手机屏幕底部且水平居中的位置
//        popupWindow.showAtLocation(CustomPopWindow.this.findViewById(R.id.mo_info_layout), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
//        popupView.startAnimation(animation);
//        //显示遮罩
//        v_mask.setVisibility(View.VISIBLE);
//    }
}
