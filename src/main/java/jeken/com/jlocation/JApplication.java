package jeken.com.jlocation;

import android.app.Application;
import android.content.Intent;

import com.baidu.mapapi.SDKInitializer;

import jeken.com.jlocation.service.CoreSerivce;

/**
 * Created by jeken on 2017/10/24.
 */

public class JApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
        SDKInitializer.initialize(getApplicationContext());
        //start core service when app startup
        startService(new Intent(getApplicationContext(),CoreSerivce.class));
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        //stop core service when app is terminated
        stopService(new Intent(getApplicationContext(),CoreSerivce.class));
    }
}
