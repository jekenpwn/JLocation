package jeken.com.jlocation.activity;

import android.content.Context;
import android.util.Log;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.navi.BaiduMapNavigation;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import jeken.com.jlocation.R;
import jeken.com.jlocation.event.LocationInfoEvent;

/**
 * Created by Administrator on 2017-09-16.
 */

public class BaiduMapActivity extends BaseActivity {

    private MapView mMapView;

    private BaiduMap mBaiduMap;
    // Restore previous Logitude and Latitude
    private static double  preLongitude = -1f;
    private static double  preLatitude = -1f;
    private static double  nowLongitude = 39.915071f;
    private static double  nowLatitude = 116.403907f;

    private BaiduMapNavigation bdNavi;
    @Override
    public void beforeSetContentView(Context context) {
        scriberRegister(this);
    }

    @Override
    public Object getContentView(Context context) {
        return R.layout.activity_baidumap;
    }

    @Override
    public void initView(Context context) {
        MapView.setMapCustomEnable(true);
        EventBus.getDefault().post(new LocationInfoEvent());
        mMapView = (MapView) findViewById(R.id.mpv_showmap);
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMyLocationEnabled(true);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLocationInfoEvent(LocationInfoEvent locInfoEvt){
        if (locInfoEvt != null){
            preLongitude = nowLongitude;
            preLatitude = nowLatitude;
            nowLongitude = locInfoEvt.getLocationInfo().getLongitude();
            nowLatitude = locInfoEvt.getLocationInfo().getLatitude();

            if(preLatitude != nowLatitude && preLongitude != nowLongitude ){
                location(nowLatitude,nowLongitude);
            }
        }

    }

    /**
     * show the map by loacation info
     * @param latitude
     * @param longitude
     */
    private void location(double latitude,double longitude){
       if (mBaiduMap == null) return;
        MyLocationData locationData=new MyLocationData.Builder()
                .direction(100).latitude(nowLatitude)
                .longitude(nowLongitude).build();
        mBaiduMap.setMyLocationData(locationData);

        LatLng ll = new LatLng(latitude,longitude);
        MapStatusUpdate msu = MapStatusUpdateFactory.newLatLngZoom(ll,19);
        mBaiduMap.animateMapStatus(msu);
        Log.e("MapLocation","Longtitude="+nowLongitude+"Latitude="+nowLatitude);
    }
    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        scriberRegister(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mMapView != null)
            mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mMapView != null)
            mMapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MapView.setMapCustomEnable(false);
        if (mMapView != null)
            mMapView.onDestroy();
    }

    /* register Subscriber */
    private void scriberRegister(Object obj){
        if (!EventBus.getDefault().isRegistered(obj)){
            EventBus.getDefault().register(obj);
        }
    }
    // 设置个性化地图config文件路径
    private void setMapCustomFile(Context context, String PATH) {
        FileOutputStream out = null;
        InputStream inputStream = null;
        String moduleName = null;
        try {
            inputStream = context.getAssets()
                    .open("customConfigdir/" + PATH);
            byte[] b = new byte[inputStream.available()];
            inputStream.read(b);

            moduleName = context.getFilesDir().getAbsolutePath();
            File f = new File(moduleName + "/" + PATH);
            if (f.exists()) {
                f.delete();
            }
            f.createNewFile();
            out = new FileOutputStream(f);
            out.write(b);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        MapView.setCustomMapStylePath(moduleName + "/" + PATH);

    }

}
