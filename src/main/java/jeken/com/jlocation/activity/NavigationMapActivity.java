package jeken.com.jlocation.activity;

import android.content.Context;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;

import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.route.RoutePlanSearch;

import jeken.com.jlocation.R;

/**
 * Created by Administrator on 2017-10-10.
 */

public class NavigationMapActivity extends BaseActivity{
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private RouteLine mRouteLine;
    private RoutePlanSearch mRoutePlanSearch;


    private static boolean isError = false;
    @Override
    public Object getContentView(Context context) {
        return R.layout.activity_navigation;
    }

    @Override
    public void initView(Context context) {
        mMapView = (MapView) findViewById(R.id.mpv_navi);
        if (mMapView == null) {isError = true;return;} //safety checking
        mBaiduMap = mMapView.getMap();



    }

    @Override
    public void beforeSetContentView(Context context) {

    }
}
