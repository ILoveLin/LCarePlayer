package com.company.shenzhou.ui.activity.vlc;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.company.shenzhou.R;
import com.company.shenzhou.bean.SwitchVideoModel;
import com.company.shenzhou.ui.broadcast.ConnectionChangeReceiver;
import com.company.shenzhou.ui.broadcast.PowerScreenReceiver;
import com.company.shenzhou.utils.CommonUtil;
import com.company.shenzhou.utils.FileUtil;
import com.company.shenzhou.utils.VlcUtils;
import com.company.shenzhou.view.ENDownloadView;
import com.company.shenzhou.view.ENPlayView;
import com.company.shenzhou.view.dialog.WaitDialog;
import com.company.shenzhou.view.gsyplayer.SwitchVideoTypeDialog;
import com.company.shenzhou.view.vlc.MyVlcVideoView;
import com.hjq.base.BaseDialog;
import com.pedro.rtplibrary.rtmp.RtmpCamera3;
import com.vlc.lib.RecordEvent;
import com.vlc.lib.VlcVideoView;
import com.vlc.lib.listener.MediaListenerEvent;
import com.yun.common.utils.LogUtils;
import com.yun.common.utils.SharePreferenceUtil;
import com.yun.common.utils.StatusBarUtil;
import com.yun.common.utils.StatusBarUtils;
import com.yun.common.utils.ToastUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zhy.http.okhttp.utils.L;

import net.ossrs.rtmp.ConnectCheckerRtmp;

import org.videolan.libvlc.Media;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;


/**
 * LoveLin
 * <p>  VLC 直播界面
 * Describe
 */
public class VlcPlayerActivity extends AppCompatActivity implements View.OnClickListener, ConnectCheckerRtmp {
    // public static final String path = "http://121.18.168.149/cache.ott.ystenlive.itv.cmvideo.cn:80/000000001000/1000000001000010606/1.m3u8?stbId=005301FF001589101611549359B92C46&channel-id=ystenlive&Contentid=1000000001000010606&mos=jbjhhzstsl&livemode=1&version=1.0&owaccmark=1000000001000010606&owchid=ystenlive&owsid=5474771579530255373&AuthInfo=2TOfGIahP4HrGWrHbpJXVOhAZZf%2B%2BRvFCOimr7PCGr%2Bu3lLj0NrV6tPDBIsVEpn3QZdNn969VxaznG4qedKIxPvWqo6nkyvxK0SnJLSEP%2FF4Wxm5gCchMH9VO%2BhWyofF";
    //public static final String path = "rtsp://wowzaec2demo.streamlock.net/vod/mp4:BigBuckBunny_115k.mov";
    //public static final String path = "http://ivi.bupt.edu.cn/hls/cctv1hd.0 ";
    // public static final String path = "rtmp://58.200.131.2:1935/livetv/jxhd";
    public String path = "rtmp://58.200.131.2:1935/livetv/jxhd";
    private final String tag = "VlcPlayer";
    //public String path = "rtmp://ossrs.net/efe/eilfb";
    //public static final String path = "http://ivi.bupt.edu.cn/hls/cctv1hd.m3u8";
    //private String path = "http://9890.vod.myqcloud.com/9890_4e292f9a3dd011e6b4078980237cc3d3.f20.mp4";
    private VlcVideoView vlcVideoView;
    private TextView recordStart;
    private TextView change_live;
    private TextView mChangeFull;
    private LinearLayout layout_top, linear_contral;
    private BaseDialog mPusherLoading;
    private RelativeLayout mRelativeStatue;
    private ImageView lock_screen;
    private TextView error_text;
    private ENPlayView startView;
    private ENDownloadView loading;
    private boolean isFirstLoading = true;
    private List<SwitchVideoModel> mUrlList = new ArrayList<>();
    public boolean isFullscreen = true;
    private boolean isStarting = true;
    private RecordEvent recordEvent = new RecordEvent();
    //数据源
    private int mSourcePosition = 0;
    private String mTypeText = "高清";
    //录像
    private File recordFile = new File(Environment.getExternalStorageDirectory(), "CME");   //老徐手机 录像地址-内部存储/Pictures/
    //private File videoFile = new File(Environment.getExternalStorageDirectory(), "LCare" + ".mp4");
    private String directory = recordFile.getAbsolutePath();
    //录屏文件的保存地址getExternalStorageDirectory
    private String mRecordFilePath = CommonUtil.getSaveDirectory() + File.separator + System.currentTimeMillis() + ".mp4";
    //vlc截图文件地址
    private File takeSnapshotFile = new File(Environment.getExternalStorageDirectory(), "CME");
    private boolean isPlayering = false;   //视频是否播放的标识符
    private TextView snapShot;
    private MyVlcVideoView player;
    private ImageView back;
    private String url01;
    private String url02;
    public String mResponse;
    private TextView photos;
    private PowerScreenReceiver receiver;
    private ConnectionChangeReceiver mConnectionReceiver;
    private VlcVideoView vlc_video_view;
    private boolean isOnPauseExit = false;
    private boolean mFlag_Record = false;
    private boolean mFlag_MicOnLine = false;
    private String urlType;
    private TextView tv_current_time;
    private RtmpCamera3 rtmpCamera3;
    private TextView mPusher;
    private TextView mTitle;
    private String mTitleData;
    private String mPusherUrl;
    private String mIp;
    private String mMicPort;
    private String currentUserName;
    private static final int FirstError = 100;
    private static final int Pusher_Start = 101;
    private static final int Pusher_Stop = 102;
    private static final int Record_Start = 103;
    private static final int Record_Stop = 104;
    private static final int Pusher_Error = 105;
    private static final int Send_Toast = 106;
    private static final int Send_UrlType = 107;
    private static final int Type_Loading_Visible = 108;
    private static final int Type_Loading_InVisible = 109;
    private static final int Show_UrL_Type = 110;
    private static final int Show_Lock = 111;
    private static final int Show_Unlock = 112;
    private static final int Show_Control_InVisible = 113;
    private static final int Show_Control_Visible = 114;
    private String currentTime = "0";
    private Handler mHandler = new Handler() {
        @SuppressLint("NewApi")
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    tv_current_time.setText("" + VlcUtils.stringForTime(Integer.parseInt(currentTime)));
                    break;
                case Send_Toast:
                    ToastUtil.showToastCenter(VlcPlayerActivity.this, (String) msg.obj);
                    break;
                case Pusher_Start:
                    mPusher.setText("停止");
                    mPusher.setTextColor(getResources().getColor(R.color.color_007AFF));
                    Drawable topstart = getResources().getDrawable(R.drawable.icon_mic_pre);

                    mPusher.setCompoundDrawablesWithIntrinsicBounds(null, topstart, null, null);
//                    String replace = mResponse.replace("", "");
//                    int i = replace.indexOf("<body>");
//                    int i1 = replace.lastIndexOf("</body>");
//                    mPusherUrl = mResponse.substring(i + 6, i1);
                    Log.e("TAG", "rtmpCamera3.isStreaming()==response==mPusherUrl========" + mPusherUrl);
                    if (!"".equals(mPusherUrl)) {
                        if (rtmpCamera3.prepareAudio()) {
                            Log.e("TAG", "rtmpCamera3.isStreaming()==pusherStart==开始推流=" + mPusherUrl);
                            mPusher.setTag("startStream");
                            rtmpCamera3.startStream(mPusherUrl);
                        } else {
                            startSendToast("Error preparing stream, This device cant do it");
                        }
                    }
                    break;
                case Pusher_Stop:
                    mPusher.setTag("stopStream");
                    rtmpCamera3.stopStream();
                    mPusher.setText("开始");
                    mPusher.setTextColor(getResources().getColor(R.color.white));
                    Drawable topend = getResources().getDrawable(R.drawable.icon_mic_nor);
                    mPusher.setCompoundDrawablesWithIntrinsicBounds(null, topend, null, null);
                    break;
                case Record_Start:
                    mFlag_MicOnLine = true;
                    setTextColor(getResources().getColor(R.color.colorAccent), "录像", false);
                    Drawable record_start = getResources().getDrawable(R.drawable.icon_record_pre);
                    recordStart.setCompoundDrawablesWithIntrinsicBounds(null, record_start, null, null);
                    break;
                case Record_Stop:
                    mFlag_MicOnLine = false;
                    setTextColor(getResources().getColor(R.color.white), "录像", true);
                    ToastUtil.showToastCenter(VlcPlayerActivity.this, "录像成功");
                    Drawable record_end = getResources().getDrawable(R.drawable.icon_record_nore);
                    recordStart.setCompoundDrawablesWithIntrinsicBounds(null, record_end, null, null);
                    break;
                case Pusher_Error:
                    mPusher.setText("开始");
                    mPusher.setTextColor(getResources().getColor(R.color.white));
                    Drawable error = getResources().getDrawable(R.drawable.icon_mic_nor);
                    mPusher.setCompoundDrawablesWithIntrinsicBounds(null, error, null, null);
                    break;
                case Send_UrlType:
                    change_live.setText(mTypeText + "");
                    Drawable urlTypeSD = getResources().getDrawable(R.drawable.icon_url_type_sd);
                    Drawable urlTypeHD = getResources().getDrawable(R.drawable.icon_url_type_hd);
                    if ("高清".equals(mTypeText)) {
                        change_live.setCompoundDrawablesWithIntrinsicBounds(null, urlTypeHD, null, null);
                    } else if ("标清".equals(mTypeText)) {
                        change_live.setCompoundDrawablesWithIntrinsicBounds(null, urlTypeSD, null, null);
                    }
                    break;
                case Show_UrL_Type:   //切换清晰度
                    showSwitchDialog();
                    break;
                case Type_Loading_Visible:   //加载框 可见
                    loading.setVisibility(View.VISIBLE);
                    break;
                case Type_Loading_InVisible: //隐藏 加载框
                    loading.setVisibility(View.INVISIBLE);
                    break;
                case Show_Lock: //设置锁屏显示
                    lock_screen.setImageDrawable(getResources().getDrawable(R.drawable.video_lock_close_ic));
                    lock_screen.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                    layout_top.setVisibility(View.INVISIBLE);
                    mChangeFull.setVisibility(View.INVISIBLE);
                    linear_contral.setVisibility(View.INVISIBLE);
                    tv_current_time.setVisibility(View.INVISIBLE);
                    break;
                case Show_Unlock: //设置锁屏隐藏
                    lock_screen.setImageDrawable(getResources().getDrawable(R.drawable.video_lock_open_ic));
                    lock_screen.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                    layout_top.setVisibility(View.VISIBLE);
                    mChangeFull.setVisibility(View.VISIBLE);
                    linear_contral.setVisibility(View.VISIBLE);
                    tv_current_time.setVisibility(View.VISIBLE);
                    break;
                case Show_Control_InVisible: //控制布局，锁屏显示
                    layout_top.setVisibility(View.INVISIBLE);
                    mChangeFull.setVisibility(View.INVISIBLE);
                    linear_contral.setVisibility(View.INVISIBLE);
                    tv_current_time.setVisibility(View.INVISIBLE);
                    break;
                case Show_Control_Visible: //控制布局，锁屏隐藏
                    layout_top.setVisibility(View.VISIBLE);
                    mChangeFull.setVisibility(View.VISIBLE);
                    linear_contral.setVisibility(View.VISIBLE);
                    tv_current_time.setVisibility(View.VISIBLE);
                    break;


            }
        }
    };
    private ImageView mBackStatue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置沉浸式观影模式体验
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //永远不息屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_vlc_player);
        initView();
        initData();
        responseListener();

    }

    private void initData() {
        String name = "高清";
        String name2 = "标清";
        SwitchVideoModel switchVideoModel = new SwitchVideoModel(name, url01);
        SwitchVideoModel switchVideoModel2 = new SwitchVideoModel(name2, url02);
        mUrlList = new ArrayList<>();
        mUrlList.add(switchVideoModel);
        mUrlList.add(switchVideoModel2);
        mSourcePosition = 0;  //标清
        mTitle.setText("" + mTitleData);

    }

    private void responseListener() {
        mChangeFull.setOnClickListener(this);
        back.setOnClickListener(this);
        recordStart.setOnClickListener(this);
        change_live.setOnClickListener(this);
        lock_screen.setOnClickListener(this);
        player.setOnClickListener(this);
        snapShot.setOnClickListener(this);
        photos.setOnClickListener(this);
        startView.setOnClickListener(this);
        mBackStatue.setOnClickListener(this);
        mPusher.setOnClickListener(this);

        // 需要使用动态注册才能接收到广播,息屏的时候保存之前的录像
        registerPowerReceiver();
        registerConnectionReceiver();

        vlc_video_view = vlcVideoView.findViewById(R.id.vlc_video_view);
        vlc_video_view.setOnClickListener((View v) -> {
            //控制功能按钮的显示和隐藏   点击屏幕
            if (lock_screen.getVisibility() == View.VISIBLE) {
                lock_screen.setVisibility(View.INVISIBLE);
                if (lock_screen.getTag().equals("Lock")) {
                    mHandler.sendEmptyMessage(Show_Control_InVisible);
                } else {
                    mHandler.sendEmptyMessage(Show_Control_InVisible);
                }
            } else {
                lock_screen.setVisibility(View.VISIBLE);
                if (lock_screen.getTag().equals("Lock")) {
                    mHandler.sendEmptyMessage(Show_Control_InVisible);

                } else {
                    mHandler.sendEmptyMessage(Show_Control_Visible);

                }
            }
        });
        vlcVideoView.setMediaListenerEvent(new MediaListenerEvent() {
            @Override
            public void eventBuffing(int event, float buffing) {
                if (buffing < 100) {
                    loading.start();
                    mHandler.sendEmptyMessage(Type_Loading_Visible);
                } else if (buffing == 100) {
                    isPlayering = true;
                    loading.release();
                    mHandler.sendEmptyMessage(Type_Loading_InVisible);
                }


                LogUtils.e("path=====Start:=====" + "我是当前播放的url======eventBuffing======" + buffing);

            }

            @Override
            public void eventStop(boolean isPlayError) {
                LogUtils.e("path=====Start:=====" + "我是当前播放的url======eventStop======" + isPlayError);
                if (isPlayError) {

                    if (mFlag_Record) { //如果在录像，断开录像
                        vlcRecordOver();
                        LogUtils.e("path=====Start:=====" + "我是当前播放的url===eventStop===视频流断开连接====断开录像==");

                    }
//                    if (isPlayering && mFlag_Record) {
//                        vlcRecordOver();
//                    }

                    if (mFlag_MicOnLine) {//如果在连麦，断开连麦
                        pusherStop("Common");
                        LogUtils.e("path=====Start:=====" + "我是当前播放的url===eventStop===视频流断开连接====断开连麦==");

                    }
                    mRelativeStatue.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void eventError(int event, boolean show) {
                LogUtils.e("path=====Start:=====" + "我是当前播放的url======eventError======" + isPlayering);

                if (isPlayering) {
//                    startLive(path);
                    if (mFlag_Record) {
                        vlcRecordOver();
                    }
                    mHandler.sendEmptyMessage(FirstError);
                } else {
                    isPlayering = false;
                    startView.setVisibility(View.VISIBLE);
                    mHandler.sendEmptyMessage(Type_Loading_InVisible);
//                    loading.setVisibility(View.INVISIBLE);
                }


            }

            @Override
            public void eventPlay(boolean isPlaying) {
                LogUtils.e("path=====Start:=====" + "我是当前播放的url======eventPlay======" + isPlaying);


            }

            @Override
            public void eventSystemEnd(String isStringed) {  //视频流断开连接
                LogUtils.e("path=====Start:=====" + "我是当前播放的url======eventSystemEnd======" + isStringed);
                if ("EndReached".equals(isStringed)) {
                    if (mFlag_Record) { //如果在录像，断开录像
                        vlcRecordOver();
                        LogUtils.e("path=====Start:=====" + "我是当前播放的url======视频流断开连接====断开录像==");

                    }
//                    if (isPlayering && mFlag_Record) {
//                        vlcRecordOver();
//                    }

                    if (mFlag_MicOnLine) {//如果在连麦，断开连麦
                        pusherStop("Common");
                        LogUtils.e("path=====Start:=====" + "我是当前播放的url======视频流断开连接====断开连麦==");

                    }

                }

            }

            @Override
            public void eventCurrentTime(String time) {
                currentTime = time;
                mHandler.sendEmptyMessageDelayed(0, 1000);
//                mHandler.sendEmptyMessage(0);

            }

            @Override
            public void eventPlayInit(boolean openClose) {
                startView.setVisibility(View.INVISIBLE);
                LogUtils.e("path=====Start:=====" + "我是当前播放的url======eventPlayInit======" + openClose);
                LogUtils.e("path=====Start:=====" + "我是当前播放的url======eventPlayInit===url===" + path);

            }

        });


    }


    @SuppressLint("NewApi")
    private void initView() {
        StatusBarUtils.setColor(this, getResources().getColor(R.color.black), 0);
        StatusBarUtil.darkMode(this, false);  //设置了状态栏文字的颜色
        recordFile.mkdirs();
        player = findViewById(R.id.player);
        mPusher = findViewById(R.id.pusher);
        mRelativeStatue = findViewById(R.id.relative_statue);
        mRelativeStatue.setVisibility(View.INVISIBLE);
        mBackStatue = mRelativeStatue.findViewById(R.id.back_statue);

        mTitle = findViewById(R.id.tv_top_title);
        tv_current_time = findViewById(R.id.tv_current_time);
        vlcVideoView = findViewById(R.id.vlc_video_view);
        mChangeFull = findViewById(R.id.change);
        lock_screen = findViewById(R.id.lock_screen);
        photos = findViewById(R.id.photos);
        recordStart = findViewById(R.id.recordStart);
        change_live = findViewById(R.id.change_live);
        layout_top = findViewById(R.id.layout_top);
        linear_contral = findViewById(R.id.linear_contral);
        error_text = findViewById(R.id.error_text);
        snapShot = findViewById(R.id.snapShot);
        startView = findViewById(R.id.start);
        loading = findViewById(R.id.loading);
        back = findViewById(R.id.back);
        lock_screen.setTag("unLock");
        url01 = getIntent().getStringExtra("url01");
        url02 = getIntent().getStringExtra("url02");
        urlType = getIntent().getStringExtra("urlType");
        mTitleData = getIntent().getStringExtra("mTitle");
        mIp = getIntent().getStringExtra("ip");
        mMicPort = getIntent().getStringExtra("micport");
        LogUtils.e("url==01===" + url01);
        LogUtils.e("url==02===" + url02);
        path = url01;
        mHandler.sendEmptyMessage(Type_Loading_Visible);
//        loading.setVisibility(View.VISIBLE);
        rtmpCamera3 = new RtmpCamera3(this, this);

    }

    @SuppressLint("NewApi")
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start://开始播放
                startLive(path);
                break;
            case R.id.back_statue://返回
                finish();
                break;
            case R.id.back://返回
                if (mFlag_Record) {
                    vlcRecordOver();
                }
                vlcVideoView.setAddSlave(null);
                vlcVideoView.onStop();
                if (rtmpCamera3.isStreaming()) {
                    rtmpCamera3.stopStream();
                }
                pusherStop("Back");
                finish();
                break;
            case R.id.pusher:  //推流
                LogUtils.e("pusherStart====111===" + rtmpCamera3.isStreaming());    //true   断开的时候
                LogUtils.e("pusherStart====222===" + rtmpCamera3.prepareAudio());   //true
                if (!rtmpCamera3.isStreaming()) {
                    if (rtmpCamera3.prepareAudio()) {
                        if (CommonUtil.isFastClick()) {
                            if ("一体机".equals(mTitleData)) {
                                if (isPlayering) {
                                    pusherStart();
                                } else {
                                    startSendToast("只有在直播开启的时候,才能使用语音功能!");
                                }
                            } else {
                                startSendToast("当前直播没有语音功能!");
                            }
                        }
                    } else {
                        startSendToast("Error preparing stream, This device cant do it");
                    }
                } else {
                    if (CommonUtil.isFastClick()) {
                        pusherStop("Common");
                    }
                }
                break;
            case R.id.photos:  //打开相册
                if (isPlayering) {
                    if (mFlag_Record) { //如果录像则关闭录像
                        vlcRecordOver();
                    }
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivity(intent);
                } else {
                    startSendToast("只有在播放的时候才能打开相册");
                }
                break;
            case R.id.lock_screen:  //锁屏
                LogUtils.e("TAG" + "===lock_screen==锁屏控制====");
                if (lock_screen.getTag().equals("unLock")) {
                    lock_screen.setTag("Lock");
                    mHandler.sendEmptyMessage(Show_Lock);
                } else {
                    lock_screen.setTag("unLock");
                    mHandler.sendEmptyMessage(Show_Unlock);

                }
                break;
            case R.id.change: //切换全屏
                //ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE,//指定横屏
                //ActivityInfo.SCREEN_ORIENTATION_PORTRAIT,//指定竖屏
                //ActivityInfo.SCREEN_ORIENTATION_USER,//根据用户朝向
                //ActivityInfo.SCREEN_ORIENTATION_NOSENSOR,//不受重力影响
                //ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE,//横屏动态转换
                //ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT,//竖屏动态转换
                //ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR,//根据重力变换朝向
                isFullscreen = !isFullscreen;
                if (isFullscreen) {
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE); //横屏动态转换
                } else {
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//竖屏动态转换
                }
                break;

            case R.id.change_live: //切换清晰度
                if (!"02".equals(urlType)) {
                    mHandler.sendEmptyMessage(Show_UrL_Type);
                } else {
                    startSendToast("url类型只有高清模式噢!");
                }
                break;
            case R.id.recordStart: //录像
                if (isPlayering) {
                    if (isStarting && vlcVideoView.isPrepare()) {
                        mFlag_Record = true;
                        mHandler.sendEmptyMessage(Record_Start);
//                        vlcVideoView.getMediaPlayer().record(directory);
                        LogUtils.e("path=====录像--开始:=====" + directory); //   /storage/emulated/0/1604026573438.mp4
                        recordEvent.startRecord(vlcVideoView.getMediaPlayer(), directory, "cme.mp4");
                    } else {
                        vlcRecordOver();
                    }
                } else {
                    startSendToast("只有在播放的时候才能录像!");
                }
                break;
            case R.id.snapShot://截图
                if (isPlayering) {
                    if (vlcVideoView.isPrepare()) {
                        Media.VideoTrack videoTrack = vlcVideoView.getVideoTrack();
                        if (videoTrack != null) {
                            //vlcVideoView.getMediaPlayer().updateVideoSurfaces();
                            startSendToast("截图成功");
                            //原图
                            LogUtils.e("path=====截图地址:=====" + takeSnapshotFile.getAbsolutePath()); //   /storage/emulated/0/1604026573438.mp4
                            File localFile = new File(takeSnapshotFile.getAbsolutePath());
                            if (!localFile.exists()) {
                                localFile.mkdir();
                            }
                            recordEvent.takeSnapshot(vlcVideoView.getMediaPlayer(), takeSnapshotFile.getAbsolutePath(), 0, 0);
                            //插入相册 解决了华为截图显示问题
                            MediaStore.Images.Media.insertImage(getContentResolver(), vlcVideoView.getBitmap(), "", "");
                            //原图的一半
                            //recordEvent.takeSnapshot(vlcVideoView.getMediaPlayer(), takeSnapshotFile.getAbsolutePath(), videoTrack.width / 2, 0);
                        }
                    }
                    //这个就是截图 保存Bitmap就行了
                    //thumbnail.setImageBitmap(vlcVideoView.getBitmap());
                    //Bitmap bitmap = vlcVideoView.getBitmap();
                    //saveBitmap("", bitmap);
                } else {
                    startSendToast("只有在播放的时候才能截图!");
                }
                break;
        }
    }

    private void startSendToast(String toastStr) {
        Message tempMsg = mHandler.obtainMessage();
        tempMsg.what = Send_Toast;
        tempMsg.obj = toastStr;
        mHandler.sendMessage(tempMsg);
    }


    private void pusherStop(String Type) {
        try {
            mPusherLoading = new WaitDialog.Builder(this)
                    // 消息文本可以不用填写
                    .setMessage(getString(R.string.common_stop_loading))
                    .show();
            LogUtils.e("pusher====pusherStop===" + "http://" + mIp + ":" + mMicPort + "/stop");
            OkHttpUtils.get()
//                .url("http://192.168.64.13:7789/stop")
                    .url("http://" + mIp + ":" + mMicPort + "/stop")
                    .addParams("username", currentUserName + "_安卓")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            mPusherLoading.dismiss();
                            if (!"Back".equals(Type)) {
                                startSendToast("语音断开失败: 500");
                            }
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            mPusherLoading.dismiss();
                            LogUtils.e("pusher====pusherStop===" + "response===" + response);
                            mHandler.sendEmptyMessage(Pusher_Stop);
                        }
                    });
        } catch (Exception e) {
        }
    }

    //1.推流开始：http://ip:7789/start?username=xxx
    //2.推流退出：http://ip:7789/stop?username=xxx
    @SuppressLint("NewApi")
    private void pusherStart() {
        try {
            LogUtils.e("pusher====pusherStart===" + "http://" + mIp + ":" + mMicPort + "/start");
            /**
             * 开始推流
             * 语音连接失败: 300  html数据解析失败
             * 语音连接失败: 500  未连接上服务器
             * 结束推流
             * 语音断开失败: 500 未连接上服务器
             */
            currentUserName = (String) SharePreferenceUtil.get(this, SharePreferenceUtil.Current_Username, "张三");
            mPusherLoading = new WaitDialog.Builder(this)
                    // 消息文本可以不用填写
                    .setMessage(getString(R.string.common_loading))
                    .show();
            OkHttpUtils.get()
                    .url("http://" + mIp + ":" + mMicPort + "/start")
                    .addParams("username", currentUserName + "_安卓")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            mPusherLoading.dismiss();
                            startSendToast("语音连接失败: 500");
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            LogUtils.e("pusherStart====response===" + response);
//                            <html><head><title></title></head><body>rtmp://192.168.128.134:8350/live/94726</body></html>
                            String replace = response.replace("", "");
                            int i = replace.indexOf("<body>");
                            int i1 = replace.lastIndexOf("</body>");
                            mPusherUrl = response.substring(i + 6, i1);
                            mPusherLoading.dismiss();
                            if (-1 != mPusherUrl.indexOf("null")) { //说明是完整的url
                                startSendToast("语音连接失败: 300");
                            } else {
                                mHandler.sendEmptyMessage(Pusher_Start);
                            }
                        }
                    });
        } catch (Exception e) {
            startSendToast("获取推流地址失败!");
        }
    }

    /**
     * 开始直播
     *
     * @param path
     */
    private void startLive(String path) {
        LogUtils.e("path=====--startLive:=====" + path);
        vlcVideoView.setPath(path);
        vlcVideoView.startPlay();
        error_text.setVisibility(View.INVISIBLE);
        mHandler.sendEmptyMessage(Type_Loading_Visible);
        loading.start();

    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtils.e("path=====录像--onResume:=====");
        LogUtils.e("path=====录像--onResume=path:=====" + path);
        isOnPauseExit = false;
        startLive(path);
        vlcVideoView.startPlay();
    }

    @SuppressLint("NewApi")
    @Override
    public void onPause() {
        super.onPause();
        path = url01;
        //直接调用stop 不然回ANR
        vlcVideoView.onStop();
        mHandler.sendEmptyMessage(Type_Loading_InVisible);
        loading.release();
        isOnPauseExit = true;

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtils.e("path=====录像--onDestroy:=====");
        if (receiver != null) {
            unregisterReceiver(receiver);
        }
        if (mConnectionReceiver != null) {
            unregisterReceiver(mConnectionReceiver);
        }
        //手动清空字幕
        vlcVideoView.setAddSlave(null);
        vlcVideoView.onDestroy();

        // 停之推送!音频
        if (null != mPusher) {
            if ("startStream".equals(mPusher.getTag())) {
                mHandler.sendEmptyMessage(Pusher_Stop);
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        //手动清空字幕
        LogUtils.e("path=====录像--onStop:=====");
        vlcVideoView.setAddSlave(null);
        vlcVideoView.onStop();
        mHandler.sendEmptyMessage(Type_Loading_InVisible);
        loading.release();
    }

    private void vlcRecordOver() {
        mFlag_Record = false;
        mHandler.sendEmptyMessage(Record_Stop);
        vlcVideoView.getMediaPlayer().record(null);
//        FileUtil.scanFile(VlcPlayerActivity.this, directory);
        FileUtil.RefreshAlbum(directory, true, this);
    }

    public void setTextColor(int color, String message, boolean isStarting) {
        recordStart.setText(message);
        recordStart.setTextColor(color);
        this.isStarting = isStarting;
    }

    /**
     * 扫描文件
     */
    public final MediaScannerConnection msc = new MediaScannerConnection(this, new MediaScannerConnection.MediaScannerConnectionClient() {
        public void onMediaScannerConnected() {
            msc.scanFile("/sdcard/image.jpg", "image/jpeg");
        }

        public void onScanCompleted(String path, Uri uri) {
            Log.v("TAG", "scan completed");
            msc.disconnect();
        }
    });

    /**
     * 弹出切换清晰度
     */
    private void showSwitchDialog() {
        SwitchVideoTypeDialog switchVideoTypeDialog = new SwitchVideoTypeDialog(this);
        switchVideoTypeDialog.initList(mUrlList, new SwitchVideoTypeDialog.OnListItemClickListener() {
            @Override
            public void onItemClick(int position) {
                final String name = mUrlList.get(position).getName();
                if (mSourcePosition != position) {
                    mTypeText = name;
                    mSourcePosition = position;
                    mHandler.sendEmptyMessage(Send_UrlType);
                    mHandler.sendEmptyMessage(Pusher_Stop);
                    //要断开麦克风连接
                    pusherStop("Common");
                    if (isPlayering && mFlag_Record) {
                        mFlag_Record = false;
                        mHandler.sendEmptyMessage(Record_Stop);
//                        setTextColor(getResources().getColor(R.color.colorAccent), "录像", false);
//                        Drawable record_start = getResources().getDrawable(R.drawable.icon_record_pre);
//                        recordStart.setCompoundDrawablesWithIntrinsicBounds(null, record_start, null, null);
                        startSendToast("录像成功!");
                        vlcVideoView.getMediaPlayer().record(null);
                        FileUtil.scanFile(VlcPlayerActivity.this, directory);
                    }
                    startLive(mUrlList.get(position).getUrl());
                    startSendToast("" + name);
                } else {
                    startSendToast("已经是 " + name);
                }
            }
        });
        switchVideoTypeDialog.show();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {//横屏
            Drawable record_end = getResources().getDrawable(R.drawable.nur_ic_fangxiao);
            mChangeFull.setCompoundDrawablesWithIntrinsicBounds(record_end, null, null, null);
        } else {
            Drawable record_end = getResources().getDrawable(R.drawable.nur_ic_fangda);
            mChangeFull.setCompoundDrawablesWithIntrinsicBounds(record_end, null, null, null);//竖屏
        }
    }

    //wifi不好的时候的广播监听
    private void registerConnectionReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        mConnectionReceiver = new ConnectionChangeReceiver();
        mConnectionReceiver.setOnWifiListener(new ConnectionChangeReceiver.onWifiListener() {
            @Override
            public void wifiOff() {
                if (isPlayering && mFlag_Record) {
                    vlcRecordOver();
                }
            }

            @Override
            public void wifiOn() {

            }
        });
        this.registerReceiver(mConnectionReceiver, filter);
    }

    //息屏的广播监听
    private void registerPowerReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        intentFilter.addAction(Intent.ACTION_SCREEN_ON);
        receiver = new PowerScreenReceiver();
        receiver.setOnScreenListener(new PowerScreenReceiver.onScreenListener() {
            @Override
            public void screenOff() {
                if (isPlayering && mFlag_Record) {
                    LogUtils.e("path=====录像--screenOff:=====");
                    vlcRecordOver();
                }
            }
        });
        registerReceiver(receiver, intentFilter);
    }

    @Override
    public void onConnectionSuccessRtmp() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.e("TAG", "rtmpCamera3.isStreaming()=====onConnectionSuccessRtmp");
                startSendToast("连接成功 ");
            }
        });
    }

    @Override
    public void onConnectionFailedRtmp(final String reason) {
        runOnUiThread(new Runnable() {
            @SuppressLint("NewApi")
            @Override
            public void run() {
                Log.e("TAG", "rtmpCamera3.isStreaming()=====" + reason);
                Log.e("TAG", "rtmpCamera3.isStreaming()=====onConnectionFailedRtmp");
                startSendToast("Connection failed. " + reason);
                rtmpCamera3.stopStream();
            }
        });
    }

    @Override
    public void onDisconnectRtmp() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.e("TAG", "rtmpCamera3.isStreaming()=====onDisconnectRtmp");
                if (!isOnPauseExit) {
                    startSendToast("断开麦克风链接");

                }
            }
        });
    }

    @Override
    public void onAuthErrorRtmp() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.e("TAG", "rtmpCamera3.isStreaming()=====onAuthErrorRtmp");
                startSendToast("Auth error");
            }
        });
    }

    @Override
    public void onAuthSuccessRtmp() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.e("TAG", "rtmpCamera3.isStreaming()=====onAuthSuccessRtmp");
                startSendToast("Auth success");
            }
        });
    }

}
