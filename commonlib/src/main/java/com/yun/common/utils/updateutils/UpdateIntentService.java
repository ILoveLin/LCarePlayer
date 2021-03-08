package com.yun.common.utils.updateutils;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.RemoteViews;


import androidx.core.app.NotificationCompat;

import com.yun.common.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 三个地方更改包名
 * 第一，
 * 清单文件的 FileProvider和UriUtil类中的getUriForFile(参数1，必须同名)
 * uri = FileProvider.getUriForFile(context.getApplicationContext(), "com.company.yun.fileProvider", file);
 * <p>
 * 的FileProvider必须一样
 * 第二，
 * UpdateIntentService----ACTION_UPDATE   字段
 * 第三，
 * UriUtil-----getUriForFile   方法里面
 */
public class UpdateIntentService extends IntentService {
    public static final String ACTION_UPDATE = "com.company.forturetelling.fileProvider";
    private NotificationManager mNotificationManager;
    private RemoteViews mRemoteViews;
    private Notification mNotification;
    private Handler mUpdateHandler;
    private String mChannelId = "updateChannel";


    public UpdateIntentService() {
        super("MyIntentService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            switch (action) {
                case UpdateIntentService.ACTION_UPDATE:
                    handleActionUpdate(intent);
                    break;
                default:
                    break;
            }
        }
    }


    private void handleActionUpdate(Intent intent) {
        getUpdateHandler();
        beforeUpdateMessage();
        File file = updateIo(intent);
        finishUpdateMessage(file);
    }

    private void getUpdateHandler() {
        mUpdateHandler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.arg1) {
                    case 0:
                        createNotification();
                        //start
                        break;
                    case 1:
                        updateNotification(msg);
                        //updateingMessage
                        break;
                    case 2:
                        installApk(msg);
                        //finish
                    case 3:
                        //error
                        installApk(msg);
                        break;
                }
                return true;
            }
        });
    }


    private File updateIo(Intent intent) {
        File updateFile = FileUtil.getDiskCacheDir(getApplicationContext(), intent.getStringExtra("name") + System.currentTimeMillis() + ".apk");
        try {
            URL url = new URL(intent.getStringExtra("downUrl"));
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(10000);
            conn.setRequestProperty("Accept-Encoding", "identity");
            conn.connect();
            int length = conn.getContentLength();
            InputStream inputStream = conn.getInputStream();
            FileOutputStream fos = new FileOutputStream(updateFile, true);
            int oldProgress = 0;
            byte buf[] = new byte[1024 * 8];
            int currentLength = 0;
            while (true) {
                int num = inputStream.read(buf);
                currentLength += num;
                // 计算进度条位置
                int progress = (int) ((currentLength / (float) length) * 100);
                if (progress > oldProgress) {
                    updatingMessage(progress);
                    oldProgress = progress;
                }
                if (num <= 0) {
                    break;
                }
                fos.write(buf, 0, num);
                fos.flush();
            }
            fos.flush();
            fos.close();
            inputStream.close();
        } catch (Exception e) {
            Log.i("updateException", e.toString());
            return null;
        }
        return updateFile;
    }


    private void beforeUpdateMessage() {
        Message message = mUpdateHandler.obtainMessage();
        message.arg1 = 0;
        mUpdateHandler.sendMessage(message);
    }

    private void updatingMessage(int progress) {
        Message message = mUpdateHandler.obtainMessage();
        message.arg1 = 1;
        message.obj = progress;
        mUpdateHandler.sendMessage(message);
    }

    private void finishUpdateMessage(File file) {
        Message message = mUpdateHandler.obtainMessage();
        message.arg1 = 2;
        message.obj = file;
        mUpdateHandler.sendMessage(message);
    }

    public void createNotification() {
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = null;
        //notification channel work
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            builder = new NotificationCompat.Builder(getApplicationContext(), mChannelId);
            builder = new NotificationCompat.Builder(getApplicationContext());
            NotificationChannel channel = new NotificationChannel(mChannelId,
                    getString(R.string.app_name), NotificationManager.IMPORTANCE_DEFAULT);
            mNotificationManager.createNotificationChannel(channel);
        } else {
            builder = new NotificationCompat.Builder(getApplicationContext());
        }
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setTicker("开始下载");
        mRemoteViews = new RemoteViews(getPackageName(), R.layout.notification_update);
        mRemoteViews.setProgressBar(R.id.notificationProgress, 100, 0, false);
        builder.setCustomContentView(mRemoteViews);
        mNotification = builder.build();
        //Notification.FLAG_ONLY_ALERT_ONCE 避免8.0在进度更新时候(notify)中多次响铃
        //Notification.FLAG_NO_CLEAR 下载过程中无法关闭通知,失败或者完成会切换到可以关闭
        mNotification.flags = Notification.FLAG_NO_CLEAR | Notification.FLAG_ONLY_ALERT_ONCE;
        mNotification.icon = R.mipmap.ic_launcher;
        mNotificationManager.notify(0, mNotification);
    }

    private void updateNotification(Message msg) {
        Integer aFloat = (Integer) msg.obj;
        mRemoteViews.setProgressBar(R.id.notificationProgress, 100, aFloat, false);
        mRemoteViews.setTextViewText(R.id.notification_note_tv, "正在下载  " + aFloat + "%");
        mNotificationManager.notify(0, mNotification);
    }

    // 下载完成后打开安装apk界面
    public void installApk(Message msg) {
        File file = (File) msg.obj;
        if (file == null || file.length() == 0) {
            mRemoteViews.setTextViewText(R.id.notification_note_tv, "下载失败  ");
            //下载失败,flags设置为可关闭
            mNotification.flags = Notification.FLAG_AUTO_CANCEL;
            mNotificationManager.notify(0, mNotification);
            return;
        }
        //关闭之前的通知,为了兼容某些手机在mNotification.contentIntent后不更新的bug
        //,保证PendingIntent正确执行
        mNotificationManager.cancel(0);
        mRemoteViews.setProgressBar(R.id.notificationProgress, 100, 100, false);
        mRemoteViews.setTextViewText(R.id.notification_note_tv, "下载完毕  ");
        //下载完成,flags设置为可关闭
        mNotification.flags = Notification.FLAG_AUTO_CANCEL | Notification.FLAG_ONLY_ALERT_ONCE;
        Intent openFile = getFileIntent(file);
        mNotification.contentIntent = PendingIntent.getActivity(this, 0, openFile, 0);
        mNotificationManager.notify(1, mNotification);
        startActivity(openFile);
    }

    public Intent getFileIntent(File file) {
        Uri uri = UriUtil.getUriForFile(getBaseContext(), file);
        String type = getMIMEType(file);
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(uri, type);
        return intent;
    }

    public String getMIMEType(File file) {
        String type = null;
        String suffix = file.getName().substring(file.getName().lastIndexOf(".") + 1, file.getName().length());
        if (suffix.equals("apk")) {
            type = "application/vnd.android.package-archive";
        } else {
            // /*如果无法直接打开，就跳出软件列表给用户选择 */
            type = "*/*";
        }
        return type;
    }


}
