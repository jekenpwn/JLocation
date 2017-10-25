package jeken.com.jlocation.navi;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.baidu.navisdk.adapter.BNCommonSettingParam;
import com.baidu.navisdk.adapter.BNOuterLogUtil;
import com.baidu.navisdk.adapter.BNOuterTTSPlayerCallback;
import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.baidu.navisdk.adapter.BNaviSettingManager;
import com.baidu.navisdk.adapter.BaiduNaviManager;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import jeken.com.jlocation.activity.BNEventHandler;

import static jeken.com.jlocation.activity.BNDemoMainActivity.ROUTE_PLAN_NODE;

/**
 * Created by jeken on 2017/10/18.
 */

public class NavInitManager {
    private static NavInitManager navInitManager;

    private static final String APP_FOLDER_NAME = "JLocationPWN";

    private String mSDCardPath = null;//sd卡路径

    private final static String authBaseArr[] =
            { Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION };
    private final static String authComArr[] = { Manifest.permission.READ_PHONE_STATE };

    private final static int authBaseRequestCode = 1;
    private final static int authComRequestCode = 2;

    private boolean hasInitSuccess = false;
    private boolean hasRequestComAuth = false;

    private NaviCallback mNaviCallback;

    //存储Activity
    public static List<Activity> activityList = new LinkedList<Activity>();

    private NavInitManager(){}
    public static NavInitManager getInstance() {
        if (navInitManager == null) {
            synchronized (NavInitManager.class) {
                if (navInitManager == null) {
                    navInitManager = new NavInitManager();
                }
            }
        }
        return  navInitManager;
    }

    public void startDrivingNavi(Activity activity, BNRoutePlanNode startWhere,
                                 BNRoutePlanNode endWhere, Class naviActivity){

        routeplanToNavi(activity,startWhere,endWhere,naviActivity);

    }
    public void initAll(Activity activity){
        BNOuterLogUtil.setLogSwitcher(true);
        if (initDirs()) {
            initNavi(activity);
        }
    }

    /**
     * Set Callback listener. Init Start stop ,wiil be called back
     * @param mNaviCallback
     */
    public void setmNaviCallback(NaviCallback mNaviCallback){
        this.mNaviCallback = mNaviCallback;
    }
    private void sendMessage(String msg){
        if (mNaviCallback != null){
            mNaviCallback.message(msg);
        }
    }
    /**
     * 初始化SD卡路径，没有路径则创建
     * @return
     */
     private boolean initDirs() {
        mSDCardPath = getSdcardDir();
        if (mSDCardPath == null) {
            return false;
        }
        File f = new File(mSDCardPath, APP_FOLDER_NAME);
        if (!f.exists()) {
            try {
                f.mkdir();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    /**
     * 检查是否有基本的手机权限
     * @param activity
     * @return
     */
    private boolean hasBasePhoneAuth(Activity activity) {
        // TODO Auto-generated method stub

        PackageManager pm = activity.getPackageManager();
        for (String auth : authBaseArr) {
            if (pm.checkPermission(auth, activity.getPackageName()) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * 检查是否有全部需要用到的手机权限
     * @param activity
     * @return
     */
    private boolean hasCompletePhoneAuth(Activity activity) {
        // TODO Auto-generated method stub

        PackageManager pm = activity.getPackageManager();
        for (String auth : authComArr) {
            if (pm.checkPermission(auth, activity.getPackageName()) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * sd卡路径获取
     * @return
     */
    private String getSdcardDir() {
        if (Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED)) {
            return Environment.getExternalStorageDirectory().toString();
        }
        return null;
    }

    /**
     * Init Navigation
     * @param activity
     */
    private void initNavi(final Activity activity) {

        BNOuterTTSPlayerCallback ttsCallback = null;

        // 申请权限
        if (android.os.Build.VERSION.SDK_INT >= 23) {

            if (!hasBasePhoneAuth(activity)) {
                //权限请求
                activity.requestPermissions(authBaseArr, authBaseRequestCode);
                return;

            }
        }

        BaiduNaviManager.getInstance().init(activity, mSDCardPath, APP_FOLDER_NAME, new BaiduNaviManager.NaviInitListener() {
            @Override
            public void onAuthResult(int status, String msg) {

                  if (0 == status){
                      sendMessage("key校验成功!");
                  }else {
                      sendMessage("key校验失败, " + msg);
                  }

            }

            public void initSuccess() {
               Toast.makeText(activity, "百度导航引擎初始化成功", Toast.LENGTH_SHORT).show();
                hasInitSuccess = true;
                if (mNaviCallback != null){
                    mNaviCallback.initSuccess();
                }
                initSetting();
            }

            public void initStart() {
                sendMessage("百度导航引擎初始化开始");
            }

            public void initFailed() {
                sendMessage("百度导航引擎初始化失败");
            }

        }, null, null, null);

    }
    private void routeplanToNavi(final Activity activity, final BNRoutePlanNode startWhere,
                                 BNRoutePlanNode endWhere, final Class<?> naviActivity) {

        if (!hasInitSuccess) {
            sendMessage("还未初始化!");
        }
        // 权限申请
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            // 保证导航功能完备
            if (!hasCompletePhoneAuth(activity)) {
                if (!hasRequestComAuth) {
                    hasRequestComAuth = true;
                    activity.requestPermissions(authComArr, authComRequestCode);
                    return;
                } else {
                    Toast.makeText(activity, "没有完备的权限!", Toast.LENGTH_SHORT).show();
                }
            }

        }

        if (startWhere != null && endWhere != null) {
            List<BNRoutePlanNode> list = new ArrayList<BNRoutePlanNode>();
            list.add(startWhere);
            list.add(endWhere);

            // 开发者可以使用旧的算路接口，也可以使用新的算路接口,可以接收诱导信息等
            // BaiduNaviManager.getInstance().launchNavigator(this, list, 1, true, new DemoRoutePlanListener(sNode));
            BaiduNaviManager.getInstance().launchNavigator(activity,list,1,true,
                    new BaiduNaviManager.RoutePlanListener(){

                        @Override
                        public void onJumpToNavigator() {
                            //针对version>= 23，需要代码请求权限时将两次进行调用routeplanToNavi（）方法
                            for (Activity ac : activityList) {
                                if (ac.getClass().getSimpleName().equals(naviActivity.getSimpleName())) {
                                    return;
                                }
                            }
                            Intent intent = new Intent(activity, naviActivity);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable(ROUTE_PLAN_NODE, startWhere);
                            intent.putExtras(bundle);
                            activity.startActivity(intent);
//                            Toast.makeText(activity,"lauch now",Toast.LENGTH_LONG).show();
                            sendMessage("开启导航");
                            //回调成功开始导航了
                            if (null != mNaviCallback){
                                mNaviCallback.startResult(NaviCallback.STARTSUCCESS);
                            }
                        }

                        @Override
                        public void onRoutePlanFailed() {
//                            Toast.makeText(activity,"lauch error",Toast.LENGTH_LONG).show();
                            sendMessage("导航开启失败");
                            //回调成功开始导航了
                            if (null != mNaviCallback){
                                mNaviCallback.startResult(NaviCallback.STARTERROR);
                            }
                        }
            },eventListerner);
        }
    }

    private BaiduNaviManager.NavEventListener eventListerner = new BaiduNaviManager.NavEventListener() {

        @Override
        public void onCommonEventCall(int what, int arg1, int arg2, Bundle bundle) {
            BNEventHandler.getInstance().handleNaviEvent(what, arg1, arg2, bundle);
        }
    };

    private void initSetting() {
        // BNaviSettingManager.setDayNightMode(BNaviSettingManager.DayNightMode.DAY_NIGHT_MODE_DAY);
        BNaviSettingManager
                .setShowTotalRoadConditionBar(BNaviSettingManager.PreViewRoadCondition.ROAD_CONDITION_BAR_SHOW_ON);
        BNaviSettingManager.setVoiceMode(BNaviSettingManager.VoiceMode.Veteran);
        // BNaviSettingManager.setPowerSaveMode(BNaviSettingManager.PowerSaveMode.DISABLE_MODE);
        BNaviSettingManager.setRealRoadCondition(BNaviSettingManager.RealRoadCondition.NAVI_ITS_ON);
        BNaviSettingManager.setIsAutoQuitWhenArrived(true);
        Bundle bundle = new Bundle();
        // 必须设置APPID，否则会静音
        bundle.putString(BNCommonSettingParam.TTS_APP_ID, "9354030");
        BNaviSettingManager.setNaviSdkParam(bundle);
    }

    private BNOuterTTSPlayerCallback mTTSCallback = new BNOuterTTSPlayerCallback() {

        @Override
        public void stopTTS() {
            // TODO Auto-generated method stub
            Log.e("test_TTS", "stopTTS");
        }

        @Override
        public void resumeTTS() {
            // TODO Auto-generated method stub
            Log.e("test_TTS", "resumeTTS");
        }

        @Override
        public void releaseTTSPlayer() {
            // TODO Auto-generated method stub
            Log.e("test_TTS", "releaseTTSPlayer");
        }

        @Override
        public int playTTSText(String speech, int bPreempt) {
            // TODO Auto-generated method stub
            Log.e("test_TTS", "playTTSText" + "_" + speech + "_" + bPreempt);

            return 1;
        }

        @Override
        public void phoneHangUp() {
            // TODO Auto-generated method stub
            Log.e("test_TTS", "phoneHangUp");
        }

        @Override
        public void phoneCalling() {
            // TODO Auto-generated method stub
            Log.e("test_TTS", "phoneCalling");
        }

        @Override
        public void pauseTTS() {
            // TODO Auto-generated method stub
            Log.e("test_TTS", "pauseTTS");
        }

        @Override
        public void initTTSPlayer() {
            // TODO Auto-generated method stub
            Log.e("test_TTS", "initTTSPlayer");
        }

        @Override
        public int getTTSState() {
            // TODO Auto-generated method stub
            Log.e("test_TTS", "getTTSState");
            return 1;
        }
    };
}
