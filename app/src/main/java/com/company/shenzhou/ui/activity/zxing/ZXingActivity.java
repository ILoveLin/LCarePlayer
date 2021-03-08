package com.company.shenzhou.ui.activity.zxing;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.company.shenzhou.R;
import com.company.shenzhou.base.BaseActivity;
import com.company.shenzhou.bean.RefreshEvent;
import com.company.shenzhou.bean.ZXingBean;
import com.company.shenzhou.bean.dbbean.VideoDBBean;
import com.company.shenzhou.bean.dbbean.VideoDBBean01;
import com.company.shenzhou.ui.activity.MainActivity;
import com.company.shenzhou.ui.activity.vlc.VlcPlayerActivity;
import com.company.shenzhou.utils.db.VideoDB01Utils;
import com.company.shenzhou.utils.db.VideoDBUtils;
import com.company.shenzhou.view.ListPopup;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.yun.common.utils.StatusBarUtil;
import com.yun.common.utils.StatusBarUtils;
import com.yun.common.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

import butterknife.BindView;
import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerActivity;
import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.ZXingView;

/**
 * LoveLin
 * <p>
 * Describe  二维码扫描
 */
public class ZXingActivity extends BaseActivity implements QRCodeView.Delegate, View.OnClickListener {
    private static final String TAG = ZXingActivity.class.getSimpleName();
    private static final int REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY = 666;
    @BindView(R.id.myzxingview)
    ZXingView mZXingView;
    @BindView(R.id.open_light)
    TextView open_light;
    @BindView(R.id.text111)
    TextView text111;
    @BindView(R.id.close_light)
    TextView close_light;
    @BindView(R.id.zx_back)
    ImageView zx_back;
    @BindView(R.id.choose_picture)
    TextView choose_picture;
    private String currentUsername;

    private Boolean isFirstIn = true;

    @Override
    public int getContentViewId() {
        return R.layout.activity_zxing;
    }

    @Override
    protected void onResume() {
        super.onResume();
        isFirstIn = true;
    }

    @Override
    public void init() {
        StatusBarUtils.setColor(this, getResources().getColor(R.color.transparent), 1);
        StatusBarUtil.darkMode(this, false);  //设置了状态栏文字的颜色
        setTitleBarVisibility(View.GONE);
        setTitleLeftBtnVisibility(View.VISIBLE);
        setTitleRightBtnVisibility(View.VISIBLE);
        setTitleRightBtnResources(R.drawable.add_ic);
        setTitleName("二维码扫描");
        setPageStateView();
        //这个是查询数据库的tag
        currentUsername = getIntent().getStringExtra("currentUsername");
        responseListener();


    }

    private void responseListener() {
        mZXingView.setDelegate(this);
        open_light.setOnClickListener(this);
        close_light.setOnClickListener(this);
        choose_picture.setOnClickListener(this);
        zx_back.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        mZXingView.startCamera(); // 打开后置摄像头开始预览，但是并未开始识别
//        mZXingView.startCamera(Camera.CameraInfo.CAMERA_FACING_FRONT); // 打开前置摄像头开始预览，但是并未开始识别

        mZXingView.startSpotAndShowRect(); // 显示扫描框，并开始识别
    }

    @Override
    protected void onStop() {
        mZXingView.stopCamera(); // 关闭摄像头预览，并且隐藏扫描框
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mZXingView.onDestroy(); // 销毁二维码扫描控件
        super.onDestroy();
    }

    @SuppressLint("MissingPermission")
    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }

    @Override
    public void onScanQRCodeSuccess(String result) {
        Log.i(TAG, "result:" + result);
        setTitle("扫描结果为：" + result);
        Log.e("扫描结果为：", "result===" + result);
        //取消震动
//        vibrate();
        text111.setText("" + result);
        if (!"".equals(result) && isFirstIn) {
            isFirstIn = false;
            if (isGoodJson(result)) {  //是json数据 HD3  或者一体机的格式
                Log.e("扫描结果为：", "result===" + result);

                getJsonData(result);
            } else {//暂时认定为自定义url链接
                getCustomUrl(result);
            }
        }
        mZXingView.startSpot(); // 开始识别
//        ToastUtil.showToastCenter(ZXingActivity.this, "扫码成功!");
        finish();
    }

    private void getCustomUrl(String result) {
        VideoDBBean01 videoDBBean = new VideoDBBean01();
        videoDBBean.setAccount("root");
        videoDBBean.setPassword("root");
        videoDBBean.setTitle("自定义URL");
        videoDBBean.setIp(result.trim());
        videoDBBean.setMakeMessage("自定义URL");
        videoDBBean.setPort("自定义URL");
        videoDBBean.setType("自定义URL");
        videoDBBean.setTag(currentUsername);
        videoDBBean.setMicport("7789");
        VideoDB01Utils.insertOrReplaceData(videoDBBean);
        EventBus.getDefault().post(new RefreshEvent("refresh"));
    }

    private void getJsonData(String result) {
        try {
            Gson gson = new Gson();
            Type type = new TypeToken<ZXingBean>() {
            }.getType();
            ZXingBean mBean = gson.fromJson(result, type);
            VideoDBBean01 videoDBBean = new VideoDBBean01();
            videoDBBean.setAccount(mBean.getUsername());
            videoDBBean.setPassword(mBean.getPassword());
            videoDBBean.setTitle(mBean.getTitle());
            videoDBBean.setIp(mBean.getIp());
            videoDBBean.setMakeMessage(mBean.getMakemsg());
            videoDBBean.setPort(mBean.getPort());
            String type1 =mBean.getType() ;
            if ("0".equals(type1)) {
                videoDBBean.setType("HD3");
            } else if ("1".equals(type1)) {
                videoDBBean.setType("一体机");
            }
            videoDBBean.setTag(currentUsername);
            if ("".equals(mBean.getMicport())||null==mBean.getMicport()) {
                videoDBBean.setMicport("7789");
            } else {
                videoDBBean.setMicport(mBean.getMicport());
            }
            VideoDB01Utils.insertOrReplaceData(videoDBBean);
            EventBus.getDefault().post(new RefreshEvent("refresh"));
//            Toast.makeText(this, "扫码成功!", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(this, "请选择有用的二维码图片!", Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean isGoodJson(String json) {
        if (TextUtils.isEmpty(json)) {
            return false;
        }
        try {
            new JsonParser().parse(json);
            return true;
        } catch (JsonSyntaxException e) {
            return false;
        } catch (JsonParseException e) {
            return false;
        }
    }

    @Override
    public void onCameraAmbientBrightnessChanged(boolean isDark) {
        // 这里是通过修改提示文案来展示环境是否过暗的状态，接入方也可以根据 isDark 的值来实现其他交互效果
        String tipText = mZXingView.getScanBoxView().getTipText();
        String ambientBrightnessTip = "\n环境过暗，请打开闪光灯";
        if (isDark) {
            if (!tipText.contains(ambientBrightnessTip)) {
                mZXingView.getScanBoxView().setTipText(tipText + ambientBrightnessTip);
            }
        } else {
            if (tipText.contains(ambientBrightnessTip)) {
                tipText = tipText.substring(0, tipText.indexOf(ambientBrightnessTip));
                mZXingView.getScanBoxView().setTipText(tipText);
            }
        }
    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        showToast("打开相机出错");
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mZXingView.startSpotAndShowRect(); // 显示扫描框，并开始识别
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY) {
            final String picturePath = BGAPhotoPickerActivity.getSelectedPhotos(data).get(0);
            // 本来就用到 QRCodeView 时可直接调 QRCodeView 的方法，走通用的回调
            mZXingView.decodeQRCode(picturePath);

            /*
            没有用到 QRCodeView 时可以调用 QRCodeDecoder 的 syncDecodeQRCode 方法

            这里为了偷懒，就没有处理匿名 AsyncTask 内部类导致 Activity 泄漏的问题
            请开发在使用时自行处理匿名内部类导致Activity内存泄漏的问题，处理方式可参考 https://github
            .com/GeniusVJR/LearningNotes/blob/master/Part1/Android/Android%E5%86%85%E5%AD%98%E6%B3%84%E6%BC%8F%E6%80%BB%E7%BB%93.md
             */
//            new AsyncTask<Void, Void, String>() {
//                @Override
//                protected String doInBackground(Void... params) {
//                    return QRCodeDecoder.syncDecodeQRCode(picturePath);
//                }
//
//                @Override
//                protected void onPostExecute(String result) {
//                    if (TextUtils.isEmpty(result)) {
//                        Toast.makeText(TestScanActivity.this, "未发现二维码", Toast.LENGTH_SHORT).show();
//                    } else {
//                        Toast.makeText(TestScanActivity.this, result, Toast.LENGTH_SHORT).show();
//                    }
//                }
//            }.execute();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.zx_back:  //退出
                finish();
                break;
            case R.id.open_light:  //打开闪光灯
                mZXingView.openFlashlight();
                break;
            case R.id.close_light: //关闭闪光灯
                mZXingView.closeFlashlight();
                break;
            case R.id.choose_picture://打开相册
                Intent photoPickerIntent = new BGAPhotoPickerActivity.IntentBuilder(this)
                        .cameraFileDir(null)
                        .maxChooseCount(1)
                        .selectedPhotos(null)
                        .pauseOnScroll(false)
                        .build();
                startActivityForResult(photoPickerIntent, REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY);
                break;

        }
    }
}
