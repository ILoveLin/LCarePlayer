package com.yun.common.widgets;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.yun.common.R;


public class ProgressDialog {

    private Dialog _Dialog = null;
    private static ProgressDialog _progressDialog;

    //获取该类实例对象
    public static ProgressDialog getInstance() {
        if (_progressDialog == null) {
            _progressDialog = new ProgressDialog();
        }
        return _progressDialog;
    }

    //显示对话框
    public void showProgressDialog(Context context) {
        if (null == _Dialog) {
            View view = View.inflate(context, R.layout.base_loading, null);
            LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.layloading);
            _Dialog = new Dialog(context, R.style.progressdialog);
            _Dialog.setContentView(view);
            _Dialog.setCancelable(false);
            linearLayout.setFocusable(true);
            linearLayout.setFocusableInTouchMode(true);
            linearLayout.requestFocus();
            linearLayout.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {


                    if (_Dialog != null) {

                        _Dialog.dismiss();
                        _Dialog = null;
                    }
                }

            });

            _Dialog.show();

            _Dialog.setOnKeyListener(new OnKeyListener() {

                @Override
                public boolean onKey(DialogInterface Dialog, int keyCode, KeyEvent event) {
                    if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK) {
                        closeProgressDialog();
                    }
                    return false;
                }
            });
        }
    }

    //关闭进度对话框
    public void closeProgressDialog() {
        if (_Dialog != null) {
            _Dialog.dismiss();
            _Dialog = null;
        }
    }
}
