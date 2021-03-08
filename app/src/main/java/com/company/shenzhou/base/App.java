package com.company.shenzhou.base;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.multidex.MultiDex;

import com.company.shenzhou.player.db.DaoMaster;
import com.company.shenzhou.player.db.DaoSession;
import com.company.shenzhou.utils.FileUtils;
import com.tencent.smtt.sdk.QbSdk;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.cookie.CookieJarImpl;
import com.zhy.http.okhttp.cookie.store.MemoryCookieStore;
import com.zhy.http.okhttp.https.HttpsUtils;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.OkHttpClient;

/**
 * Created by Lovelin on 2019/4/27
 * <p>
 * Describe:
 */
public class App extends Application {
    public static String FILE_DIR = "/sdcard/Downloads/test/";

    private static App app;
    public static String AppFilePath = "app";

    public App() {
        app = this;
    }

    public static synchronized App getInstance() {
        if (app == null) {
            app = new App();
        }
        return app;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        /**
         * 做一些sdk等等的init
         */
        // 初始化在线才看pdf(ppt，word都行这里只使用pdf)
        MultiDex.install(this);    // 主要是添加下面这句代码,解决Build bug
        initX5Web();
//        FILE_DIR = new File(getFilesDir(), "test").getAbsolutePath() + File.separator;
//        Log.e("QbSdk", "IOException:FILE_DIR======="+FILE_DIR );
//
//        try {
//            FileUtils.copyAssetsDir(this, "test", FILE_DIR);
//        } catch (IOException e) {
//            Log.e("QbSdk", "IOException:" );
//            e.printStackTrace();
//        }
        //数据库
        initGreenDao();
        //Okhttp请求头
        //请求工具的拦截器  ,可以设置证书,设置可访问所有的https网站,参考https://www.jianshu.com/p/64cc92c52650
        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null, null, null);
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder()
                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
                .cookieJar(new CookieJarImpl(new MemoryCookieStore()))                  //内存存储cookie
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .addInterceptor(new MyInterceptor(this))                      //拦截器,可以添加header 一些信息
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                .hostnameVerifier(new HostnameVerifier() {//允许访问https网站,并忽略证书
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                });

        OkHttpUtils.initClient(okHttpClientBuilder.build());



    }

    /**
     * 初始化GreenDao,直接在Application中进行初始化操作
     */
    private void initGreenDao() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "player.db");
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }

    private DaoSession daoSession;

    public DaoSession getDaoSession() {
        return daoSession;
    }



    private void initX5Web() {
        QbSdk.initX5Environment(getApplicationContext(), new QbSdk.PreInitCallback() {
            @Override
            public void onCoreInitFinished() {
                Log.d("QbSdk", "onCoreInitFinished");
            }

            @Override
            public void onViewInitFinished(boolean initResult) {
                Log.e("QbSdk", "onViewInitFinished:" + initResult);
            }
        });
    }
}










