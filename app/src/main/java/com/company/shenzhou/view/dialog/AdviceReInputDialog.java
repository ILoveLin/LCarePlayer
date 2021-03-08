package com.company.shenzhou.view.dialog;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.company.shenzhou.R;
import com.company.shenzhou.bean.dbbean.VideoDBBean;
import com.company.shenzhou.bean.dbbean.VideoDBBean01;
import com.company.shenzhou.utils.ClearEditText;
import com.hjq.base.BaseDialog;
import com.yun.common.utils.ToastUtil;

import java.util.HashMap;


/**
 * author : Android 轮子哥
 * github : https://github.com/getActivity/AndroidProject
 * time   : 2019/02/27
 * desc   : 输入对话框
 */
public final class AdviceReInputDialog {

    public static final class Builder
            extends UIDialog.Builder<Builder>
            implements BaseDialog.OnShowListener {

        private OnListener mListener;
        //        private final EditText mInputView;
        private ClearEditText cet_cme_account;
        private ClearEditText cet_cme_password;
        private ClearEditText cet_cme_title;
        private ClearEditText cet_cme_ip;
        private ClearEditText cet_cme_note_message;
        private ClearEditText cet_cme_port;
        private ClearEditText cet_cme_mic_port;
        private TextView cet_cme_start_type;
        private TextView makeSure;
        private TextView makeCancle;
        private Context context;
//        private LinearLayout linear_pop_change;
//
//        private interface onReInputTypeListener {
//            void oneInputTypeClick(TextView mType);
//        }

        public Builder(Context context, VideoDBBean01 bean) {
            super(context);
            setCustomView(R.layout.advice_input_dialog);
            this.context = context;
            cet_cme_account = (ClearEditText) findViewById(R.id.cet_cme_account);
            cet_cme_password = (ClearEditText) findViewById(R.id.cet_cme_password);
            cet_cme_title = (ClearEditText) findViewById(R.id.cet_cme_title);
            cet_cme_ip = (ClearEditText) findViewById(R.id.cet_cme_ip);
            cet_cme_note_message = (ClearEditText) findViewById(R.id.cet_cme_note_message);
            cet_cme_port = (ClearEditText) findViewById(R.id.cet_cme_port);
            cet_cme_mic_port = (ClearEditText) findViewById(R.id.cet_cme_mic_port);
            cet_cme_start_type = (TextView) findViewById(R.id.cet_cme_start_type);
            cet_cme_start_type.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onReInputTypeClick(cet_cme_start_type);
                }
            });
            if (null != bean) {
                cet_cme_account.setText("" + bean.getAccount());
                cet_cme_password.setText("" + bean.getPassword());
                cet_cme_title.setText("" + bean.getTitle());
                cet_cme_ip.setText("" + bean.getIp());
                cet_cme_note_message.setText("" + bean.getMakeMessage());
                cet_cme_port.setText("" + bean.getPort());
                cet_cme_start_type.setText("" + bean.getType());
                cet_cme_start_type.setText("" + bean.getType());
                cet_cme_mic_port.setText("" + bean.getMicport());
            }

            addOnShowListener(this);
        }


        public Builder setListener(OnListener listener) {
            mListener = listener;
            return this;
        }

        /**
         * {@link BaseDialog.OnShowListener}
         */
        @Override
        public void onShow(BaseDialog dialog) {
//            postDelayed(() -> getSystemService(InputMethodManager.class).showSoftInput(mInputView, 0), 500);
        }
        public TextView getTypeView() {
            return cet_cme_start_type;
        }
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_ui_confirm:
                    autoDismiss();
                    if (mListener != null) {
                        String account = cet_cme_account.getText().toString().trim();
                        String password = cet_cme_password.getText().toString().trim();
                        String title = cet_cme_title.getText().toString().trim();
                        String ip = cet_cme_ip.getText().toString().trim();
                        String makeMessage = cet_cme_note_message.getText().toString().trim();
                        String port = cet_cme_port.getText().toString().trim();
                        //获取当前类型  是2 的时候表示url链接 账号密码  端口可以为空
                        String type = cet_cme_start_type.getText().toString().trim();

                        HashMap<String, String> mMap = new HashMap<>();
                        mMap.put("account", account);
                        mMap.put("password", password);
                        mMap.put("title", title);
                        mMap.put("ip", ip);
                        mMap.put("makeMessage", makeMessage);
                        mMap.put("port", port);
                        mMap.put("type", type);
                        mListener.onConfirm(getDialog(), mMap);
                    }
                    break;
                case R.id.tv_ui_cancel:
                    autoDismiss();
                    if (mListener != null) {
                        mListener.onCancel(getDialog());
                    }
                    break;
                default:
                    break;
            }
        }
    }

    public void dismissDialog() {
    }

    public interface OnListener {

        /**
         * 点击确定时回调
         */
        void onConfirm(BaseDialog dialog, HashMap<String, String> mMap);
        /**
         * 点击确定时回调
         */
        void onReInputTypeClick(TextView mType);

        /**
         * 点击取消时回调
         */
        default void onCancel(BaseDialog dialog) {
        }
    }
}