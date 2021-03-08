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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.company.shenzhou.R;
import com.company.shenzhou.bean.SwitchVideoModel;
import com.company.shenzhou.ui.activity.MainActivity;
import com.company.shenzhou.ui.broadcast.ConnectionChangeReceiver;
import com.company.shenzhou.ui.broadcast.PowerScreenReceiver;
import com.company.shenzhou.ui.pusher.ConstantUtils;
import com.company.shenzhou.utils.CommonUtil;
import com.company.shenzhou.utils.FileUtil;
import com.company.shenzhou.utils.VlcUtils;
import com.company.shenzhou.view.ENDownloadView;
import com.company.shenzhou.view.ENPlayView;
import com.company.shenzhou.view.gsyplayer.SwitchVideoTypeDialog;
import com.company.shenzhou.view.vlc.MyVlcVideoView;
import com.pedro.rtplibrary.rtmp.RtmpCamera3;
import com.vlc.lib.RecordEvent;
import com.vlc.lib.VlcVideoView;
import com.vlc.lib.listener.MediaListenerEvent;
import com.yun.common.utils.LogUtils;
import com.yun.common.utils.SharePreferenceUtil;
import com.yun.common.utils.StatusBarUtil;
import com.yun.common.utils.StatusBarUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import net.ossrs.rtmp.ConnectCheckerRtmp;

import org.videolan.libvlc.Media;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;


/**
 * LoveLin
 * <p>
 * Describe
 */
public class TestVlcPlayerActivity extends AppCompatActivity implements View.OnClickListener, ConnectCheckerRtmp {
    // public static final String path = "http://121.18.168.149/cache.ott.ystenlive.itv.cmvideo.cn:80/000000001000/1000000001000010606/1.m3u8?stbId=005301FF001589101611549359B92C46&channel-id=ystenlive&Contentid=1000000001000010606&mos=jbjhhzstsl&livemode=1&version=1.0&owaccmark=1000000001000010606&owchid=ystenlive&owsid=5474771579530255373&AuthInfo=2TOfGIahP4HrGWrHbpJXVOhAZZf%2B%2BRvFCOimr7PCGr%2Bu3lLj0NrV6tPDBIsVEpn3QZdNn969VxaznG4qedKIxPvWqo6nkyvxK0SnJLSEP%2FF4Wxm5gCchMH9VO%2BhWyofF";
    //public static final String path = "rtsp://wowzaec2demo.streamlock.net/vod/mp4:BigBuckBunny_115k.mov";
    //public static final String path = "http://ivi.bupt.edu.cn/hls/cctv1hd.0";
//    public static final String path = "rtmp://58.200.131.2:1935/livetv/jxhd";
    public String path = "rtmp://58.200.131.2:1935/livetv/jxhd";
    private final String tag = "VlcPlayer";

    //    public String path = "rtmp://ossrs.net/efe/eilfb";
    //public static final String path = "http://ivi.bupt.edu.cn/hls/cctv1hd.m3u8";
    //private String path = "http://9890.vod.myqcloud.com/9890_4e292f9a3dd011e6b4078980237cc3d3.f20.mp4";
    private VlcVideoView vlcVideoView;
    private TextView recordStart;
    private TextView change_live;
    private TextView mChangeFull;
    private LinearLayout layout_top, linear_contral;
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
    private File recordFile = new File(Environment.getExternalStorageDirectory(), "LCare");
    //private File videoFile = new File(Environment.getExternalStorageDirectory(), "LCare" + ".mp4");
    private String directory = recordFile.getAbsolutePath();
    //录屏文件的保存地址
    private String mRecordFilePath = CommonUtil.getSaveDirectory() + File.separator + System.currentTimeMillis() + ".mp4";
    //vlc截图文件地址
    private File takeSnapshotFile = new File(Environment.getExternalStorageDirectory(), "LCare");
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
    private boolean isStringRecord = false;
    private Configuration mConfiguration;
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
    private static final int Record_End = 104;
    private static final int Pusher_Error = 105;
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
                case Pusher_Start:
                    mPusher.setText("停止");
                    mPusher.setTextColor(getResources().getColor(R.color.color_007AFF));
                    Drawable topstart = getResources().getDrawable(R.drawable.icon_mic_pre);
                    mPusher.setCompoundDrawablesWithIntrinsicBounds(null, topstart, null, null);
                    String replace = mResponse.replace("", "");
                    int i = replace.indexOf("<body>");
                    int i1 = replace.lastIndexOf("</body>");
                    mPusherUrl = mResponse.substring(i + 6, i1);
                    Log.e("TAG", "rtmpCamera3.isStreaming()==response==mPusherUrl========" + mPusherUrl);
                    if (!"".equals(mPusherUrl)) {
                        if (rtmpCamera3.prepareAudio()) {
                            Log.e("TAG", "rtmpCamera3.isStreaming()==pusherStart==开始推流=" + mPusherUrl);
                            mPusher.setTag("startStream");
                            rtmpCamera3.startStream(mPusherUrl);
                        } else {
//                                Toast.makeText(this, "Error preparing stream, This device cant do it",
//                                        Toast.LENGTH_SHORT).show();
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
                    setTextColor(getResources().getColor(R.color.colorAccent), "录像", false);
                    Drawable record_start = getResources().getDrawable(R.drawable.icon_record_pre);
                    recordStart.setCompoundDrawablesWithIntrinsicBounds(null, record_start, null, null);
                    break;
                case Record_End:
                    setTextColor(getResources().getColor(R.color.white), "录像", true);
                    Toast.makeText(TestVlcPlayerActivity.this, "录像成功", Toast.LENGTH_SHORT).show();
                    Drawable record_end = getResources().getDrawable(R.drawable.icon_record_nore);
                    recordStart.setCompoundDrawablesWithIntrinsicBounds(null, record_end, null, null);
                    break;
                case Pusher_Error:
                    mPusher.setText("开始");
                    mPusher.setTextColor(getResources().getColor(R.color.white));
                    Drawable error = getResources().getDrawable(R.drawable.icon_mic_nor);
                    mPusher.setCompoundDrawablesWithIntrinsicBounds(null, error, null, null);
                    break;
                case FirstError:
                    LogUtils.e("path=====Start:=====" + "我是当前播放的url======FirstError======" + isPlayering);
                    startLive(path);
                    break;

            }
        }
    };

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
//        SwitchVideoModel switchVideoModel = new SwitchVideoModel(name, url01);
//        SwitchVideoModel switchVideoModel2 = new SwitchVideoModel(name2, url02);
                SwitchVideoModel switchVideoModel = new SwitchVideoModel(name, "rtmp://58.200.131.2:1935/livetv/jxhd");
        SwitchVideoModel switchVideoModel2 = new SwitchVideoModel(name2, "http://ivi.bupt.edu.cn/hls/cctv1hd.m3u8");
        mUrlList = new ArrayList<>();
        mUrlList.add(switchVideoModel);
        mUrlList.add(switchVideoModel2);
        mSourcePosition = 0;  //标清
        mTitle.setText("" + mTitleData);
        currentUserName = (String) SharePreferenceUtil.get(this, SharePreferenceUtil.Current_Username, "张三");

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
        mPusher.setOnClickListener(this);

        // 需要使用动态注册才能接收到广播,息屏的时候保存之前的录像
        registerPowerReceiver();
        registerConnectionReceiver();

        vlc_video_view = vlcVideoView.findViewById(R.id.vlc_video_view);
        vlc_video_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //控制功能按钮的显示和隐藏   点击屏幕
                if (lock_screen.getVisibility() == View.VISIBLE) {
                    lock_screen.setVisibility(View.INVISIBLE);
                    if (lock_screen.getTag().equals("Lock")) {
                        layout_top.setVisibility(View.INVISIBLE);
                        mChangeFull.setVisibility(View.INVISIBLE);
                        linear_contral.setVisibility(View.INVISIBLE);

                    } else {
                        layout_top.setVisibility(View.INVISIBLE);
                        mChangeFull.setVisibility(View.INVISIBLE);
                        linear_contral.setVisibility(View.INVISIBLE);
                    }
                } else {
                    lock_screen.setVisibility(View.VISIBLE);
                    if (lock_screen.getTag().equals("Lock")) {
                        layout_top.setVisibility(View.INVISIBLE);
                        mChangeFull.setVisibility(View.INVISIBLE);
                        linear_contral.setVisibility(View.INVISIBLE);
                    } else {
                        layout_top.setVisibility(View.VISIBLE);
                        mChangeFull.setVisibility(View.VISIBLE);
                        linear_contral.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
//        vlc_video_view.setLibVLCPath(url01);
        vlcVideoView.setMediaListenerEvent(new MediaListenerEvent() {
            @Override
            public void eventBuffing(int event, float buffing) {
                if (buffing < 100) {
                    loading.start();
                    loading.setVisibility(View.VISIBLE);
                } else if (buffing == 100) {
                    isPlayering = true;
                    loading.release();
                    loading.setVisibility(View.INVISIBLE);
                }


                LogUtils.e("path=====Start:=====" + "我是当前播放的url======eventBuffing======" + buffing);
//                if (buffing <3) {
//                    isPlayering = true;
//                    loading.release();
//                    loading.setVisibility(View.INVISIBLE);
//                }
            }

            @Override
            public void eventStop(boolean isPlayError) {
                LogUtils.e("path=====Start:=====" + "我是当前播放的url======eventStop======" + isPlayError);
//                if (isPlayError) {
//                    isPlayering = false;
//                    loading.setVisibility(View.INVISIBLE);
//                    error_text.setVisibility(View.VISIBLE);
//                }
            }

            @Override
            public void eventError(int event, boolean show) {
                LogUtils.e("path=====Start:=====" + "我是当前播放的url======eventError======" + isPlayering);

                if (isPlayering) {
//                    startLive(path);
                    if (isStringRecord) {
                        vlcRecordOver();
                    }
                    mHandler.sendEmptyMessage(FirstError);
                } else {
                    isPlayering = false;
                    startView.setVisibility(View.VISIBLE);
                    loading.setVisibility(View.INVISIBLE);
                }


            }

            @Override
            public void eventPlay(boolean isPlaying) {
                LogUtils.e("path=====Start:=====" + "我是当前播放的url======eventPlay======" + isPlaying);


            }

            @Override
            public void eventSystemEnd(String isStringed) {
                LogUtils.e("path=====Start:=====" + "我是当前播放的url======eventSystemEnd======" + isStringed);
                if ("EndReached".equals(isStringed)) {
                    if (isPlayering && isStringRecord) {
                        vlcRecordOver();
                    }
                }

            }

            @Override
            public void eventCurrentTime(String time) {
                currentTime = time;
                mHandler.sendEmptyMessage(0);
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
        path = "http://ivi.bupt.edu.cn/hls/cctv1hd.m3u8";
//        path = url01;
        loading.setVisibility(View.VISIBLE);
        rtmpCamera3 = new RtmpCamera3(this);

    }


    @SuppressLint("NewApi")
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start:
                startLive(path);
                break;
            case R.id.back:
                if (isStringRecord) {
                    vlcRecordOver();
                }
                vlcVideoView.setAddSlave(null);
                vlcVideoView.onStop();
                if (rtmpCamera3.isStreaming()) {
                    rtmpCamera3.stopStream();
                }
                pusherStop();
                finish();
                break;
            case R.id.pusher:
                if (!rtmpCamera3.isStreaming()) {
                    if (rtmpCamera3.prepareAudio()) {
                        if (CommonUtil.isFastClick()){
                            pusherStart();
                        }
                    } else {
                        Toast.makeText(this, "Error preparing stream, This device cant do it",
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (CommonUtil.isFastClick()){
                        pusherStop();
                    }
                }
                break;
            case R.id.photos:  //打开相册
                if (isPlayering) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivity(intent);
                } else {
                    Toast.makeText(TestVlcPlayerActivity.this, "只有在播放的时候才能打开相册", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.lock_screen:  //点击小锁
                LogUtils.e("TAG" + "===lock_screen==锁屏控制====");
                if (lock_screen.getTag().equals("unLock")) {
                    lock_screen.setTag("Lock");
                    lock_screen.setImageDrawable(getResources().getDrawable(R.drawable.video_lock_close_ic)); //不会变形
                    lock_screen.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                    layout_top.setVisibility(View.INVISIBLE);
                    mChangeFull.setVisibility(View.INVISIBLE);
                    linear_contral.setVisibility(View.INVISIBLE);
                } else {
                    lock_screen.setTag("unLock");
                    lock_screen.setImageDrawable(getResources().getDrawable(R.drawable.video_lock_open_ic));
                    lock_screen.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                    layout_top.setVisibility(View.VISIBLE);
                    mChangeFull.setVisibility(View.VISIBLE);
                    linear_contral.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.change:
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

            case R.id.change_live:
                if (!"02".equals(urlType)) {
                    showSwitchDialog();
                } else {
                    Toast.makeText(TestVlcPlayerActivity.this, "url只有高清模式噢", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.recordStart:
                if (isPlayering) {
                    if (isStarting && vlcVideoView.isPrepare()) {
                        isStringRecord = true;
                        mHandler.sendEmptyMessage(Record_Start);
                        vlcVideoView.getMediaPlayer().record(directory);
                        LogUtils.e("path=====录像--开始:=====" + directory); //   /storage/emulated/0/1604026573438.mp4
                        //recordEvent.startRecord(vlcVideoView.getMediaPlayer(), directory, "yyl.mp4");
                    } else {
                        vlcRecordOver();
                    }
                } else {
                    Toast.makeText(TestVlcPlayerActivity.this, "只有在播放的时候才能录像", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.snapShot:
                if (isPlayering) {
                    if (vlcVideoView.isPrepare()) {
                        Media.VideoTrack videoTrack = vlcVideoView.getVideoTrack();
                        if (videoTrack != null) {
                            //vlcVideoView.getMediaPlayer().updateVideoSurfaces();
                            Toast.makeText(this, "截图成功", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(TestVlcPlayerActivity.this, "只有在播放的时候才能截图", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @SuppressLint("NewApi")
    private void pusherStop() {
        OkHttpUtils.get()
//                .url("http://192.168.64.13:7789/stop")
                .url("http://" + mIp + ":" + mMicPort + "/stop")
                .addParams("username", currentUserName + "_安卓")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        mHandler.sendEmptyMessage(Pusher_Stop);
                    }
                });
    }

    //1.推流开始：http://ip:7789/start?username=xxx
//2.推流退出：http://ip:7789/stop?username=xxx
    @SuppressLint("NewApi")
    private void pusherStart() {
        OkHttpUtils.get()
                .url("http://" + mIp + ":" + mMicPort + "/start")
                .addParams("username", currentUserName + "_安卓")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        mResponse = response;
                        mHandler.sendEmptyMessage(Pusher_Start);
                    }
                });
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
        loading.setVisibility(View.VISIBLE);
        loading.start();

    }


    @Override
    public void onResume() {
        super.onResume();
        LogUtils.e("path=====录像--onResume:=====");
        LogUtils.e("path=====录像--onResume=path:=====" + path);
        isOnPauseExit = false;
        startLive(path);
    }

    @SuppressLint("NewApi")
    @Override
    public void onPause() {
        super.onPause();
        path = url01;
        vlcVideoView.pause();
        loading.setVisibility(View.INVISIBLE);
        loading.release();
        isOnPauseExit = true;
        //进入后台 停之推送!音频
        if (null != mPusher) {
            if ("startStream".equals(mPusher.getTag()))
                mHandler.sendEmptyMessage(Pusher_Stop);
            mPusher.setTag("stopStream");
            rtmpCamera3.stopStream();
        }
    }

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
//
    }

    @Override
    protected void onStop() {
        super.onStop();
        //手动清空字幕
        LogUtils.e("path=====录像--onStop:=====");
        vlcVideoView.setAddSlave(null);
        vlcVideoView.onStop();
        loading.setVisibility(View.INVISIBLE);
        loading.release();
    }


    private void vlcRecordOver() {
        isStringRecord = false;
        mHandler.sendEmptyMessage(Record_End);
        vlcVideoView.getMediaPlayer().record(null);
        FileUtil.scanFile(TestVlcPlayerActivity.this, directory);
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
                    change_live.setText(mTypeText + "");
                    if (isPlayering && isStringRecord) {
                        isStringRecord = false;
                        setTextColor(getResources().getColor(R.color.white), "录像", true);
                        Toast.makeText(TestVlcPlayerActivity.this, "录像成功", Toast.LENGTH_SHORT).show();
                        vlcVideoView.getMediaPlayer().record(null);
                        FileUtil.scanFile(TestVlcPlayerActivity.this, directory);
                    }
                    startLive(mUrlList.get(position).getUrl());
                    Toast.makeText(TestVlcPlayerActivity.this, "" + name, Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(TestVlcPlayerActivity.this, "已经是 " + name, Toast.LENGTH_LONG).show();
                }
            }
        });
        switchVideoTypeDialog.show();
    }

    @Override
    public void onConnectionSuccessRtmp() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.e("TAG", "rtmpCamera3.isStreaming()=====onConnectionSuccessRtmp");
                Toast.makeText(TestVlcPlayerActivity.this, "连接成功", Toast.LENGTH_SHORT)
                        .show();
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
                Toast.makeText(TestVlcPlayerActivity.this, "Connection failed. " + reason,
                        Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(TestVlcPlayerActivity.this, "断开麦克风链接", Toast.LENGTH_SHORT).show();
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

                Toast.makeText(TestVlcPlayerActivity.this, "Auth error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onAuthSuccessRtmp() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.e("TAG", "rtmpCamera3.isStreaming()=====onAuthSuccessRtmp");
                Toast.makeText(TestVlcPlayerActivity.this, "Auth success", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {//横屏
            Drawable record_end = getResources().getDrawable(R.drawable.nur_ic_fangda);
            mChangeFull.setCompoundDrawablesWithIntrinsicBounds(record_end, null, null, null);
        } else {
            Drawable record_end = getResources().getDrawable(R.drawable.nur_ic_fangxiao);
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
                if (isPlayering && isStringRecord) {
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
                if (isPlayering && isStringRecord) {
                    LogUtils.e("path=====录像--screenOff:=====");
                    vlcRecordOver();
                }
            }
        });
        registerReceiver(receiver, intentFilter);
    }

}
