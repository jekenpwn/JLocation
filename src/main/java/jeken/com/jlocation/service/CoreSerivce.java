package jeken.com.jlocation.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;

import org.greenrobot.eventbus.EventBus;

import jeken.com.jlocation.entity.LocationInfo;
import jeken.com.jlocation.event.LocationInfoEvent;
import jeken.com.jlocation.location.BDLocationCallback;
import jeken.com.jlocation.location.BDLocationListenerImpl;
import jeken.com.jlocation.location.LocationImpl;

public class CoreSerivce extends Service {

    /* PS:Initialize Baidu Location interface will spend a number of times and resource,Don't do it in construction*/
    private LocationImpl locationImpl;//Implemenet the interface of baidu location
    private BDLocationListenerImpl bdLocationListener;//Baidu Location Listener
    private LocationInfo locationInfo;//Entity after getting location info
    private int counter = 0;//Checking if right

    private static int START = 1;
    private static int PAUSE = 2;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == START){
//                Log.e("TAG","restart");
                if (locationImpl!=null)
                   locationImpl.start();
            }else if (msg.what == PAUSE){
                if (locationImpl!=null)
                    locationImpl.stop();
            }
        }
    };
    public CoreSerivce() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private PowerManager.WakeLock wakeLock;
    /**
     * 获取电源锁，保持服务在锁屏下仍能获取到CPU时，可运行
     */
    private void acquireWakeLock(){
        if(null == wakeLock){
            PowerManager pm = (PowerManager)getSystemService(Context.POWER_SERVICE);
            wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK |
                    PowerManager.ON_AFTER_RELEASE, getClass().getCanonicalName());
            if(null != wakeLock)
                wakeLock.acquire();
        }
    }
    private void releaseWakeLock(){
        if(null!=wakeLock&&wakeLock.isHeld()){
            wakeLock.release();
            wakeLock=null;
        }
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        acquireWakeLock();//Get CUP when phone are locked
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        initBaiduLocation();//Baidu Location initilization
        return super.onStartCommand(intent, flags, startId);
    }

    private void initBaiduLocation(){
        //Location interface
        locationImpl = new LocationImpl(getApplicationContext());
        //baidu location listener
        locationInfo = new LocationInfo();
        bdLocationListener = new BDLocationListenerImpl(locationInfo,bdLocCallback);

        locationImpl.registerListener(bdLocationListener);
        locationImpl.setLocationOption(locationImpl.getDefaultLocationClientOption());
        locationImpl.start();
    }

    public void locationRequest(int startTime,int stopTime){
        if (handler==null) return;
        if ((Math.abs(startTime-stopTime))<700) return;//|starttime - stoptime|>=700ms
        if (startTime < 0 && stopTime < 0) return;
        if (startTime <= 0){
            handler.sendEmptyMessageDelayed(START,300);
        }else if (stopTime <= 0){
            handler.sendEmptyMessageDelayed(START,300);
        }else {
            handler.sendEmptyMessageDelayed(START,startTime);
            handler.sendEmptyMessageDelayed(PAUSE,stopTime);
        }
    }

    private BDLocationCallback bdLocCallback = new BDLocationCallback() {
        @Override
        public void locationSuccess() {
            LocationInfoEvent event = new LocationInfoEvent();
            event.setLocationInfo(locationInfo);
            EventBus.getDefault().post(event);
//            if(locationInfo != null){
//                String addr = locationInfo.getAddr();
//                Log.e("TAG", "location"+addr);
//            }
        }

        @Override
        public void locationError(int locType) {

        }
    };
    @Override
    public void onDestroy() {
        releaseWakeLock();//Release CPU when service is destroyed
        if (locationImpl != null && bdLocationListener != null) {
            locationImpl.stop();
            locationImpl.unregisterListener(bdLocationListener);
            locationImpl = null;//for gc
            bdLocationListener = null;
        }
        super.onDestroy();
    }
}
