package jeken.com.jlocation.activity;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.route.BikingRouteLine;
import com.baidu.mapapi.search.route.BikingRoutePlanOption;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteLine;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteLine;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.baidu.navisdk.adapter.BNRoutePlanNode;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import jeken.com.jlocation.R;
import jeken.com.jlocation.adapter.SugListAdapter;
import jeken.com.jlocation.event.LocationInfoEvent;
import jeken.com.jlocation.fragment.BusNaviInfoFragment;
import jeken.com.jlocation.map.BikingRouteOverlay;
import jeken.com.jlocation.map.OverlayManager;
import jeken.com.jlocation.map.TransitRouteOverlay;
import jeken.com.jlocation.map.WalkingRouteOverlay;
import jeken.com.jlocation.navi.NavInitManager;
import jeken.com.jlocation.tools.ActionBarTools;

/**
 * Created by Administrator on 2017-09-16.
 */

public class BaiduMapActivity extends BaseActivity implements OnGetSuggestionResultListener, TextWatcher {

    private static float ZOOM_MAX = 19f;//The max zoom for baidumap
    private static float ZOOM_PREVIEW = 16f;
    private float zoom_param = ZOOM_MAX;

    private MapView mMapView;
    private EditText sugSearch;
    private BaiduMap mBaiduMap;
    private ViewPager naviFrame;
    private ListView lv_searchlist;//SugRearsh list detail
    private SugListAdapter sug_adapter;
    private List<SuggestionResult.SuggestionInfo> sug_data;//put sugRearch in list
    // Restore previous Logitude and Latitude
    private static double  preLongitude = -1f;
    private static double  preLatitude = -1f;
    private static double  nowLongitude = 39.915071f;
    private static double  nowLatitude = 116.403907f;
    private String startCity = "";
    private String startWhere = "";
    private String endCity = "";
    private String endWhere = "";


    private boolean okNavi = false; // The flag if meet need of jumping Navigation
    private String nowAddr; //Remember the address where you are
    private LatLng goLatLng = null;//The destination location info

    private boolean SUGITEM_CLICK = false;

    private SuggestionSearch suggestionSearch;

    private FragmentPagerAdapter fragmentPagerAdapter;
    private List<Fragment> fragmentsDatas;

    private OverlayManager routeOverlay = null;

    private RoutePlanSearch mSearch;

    @Override
    public void beforeSetContentView(Context context) {
        scriberRegister(this);
        suggestionSearch =  SuggestionSearch.newInstance();
        suggestionSearch.setOnGetSuggestionResultListener(this);
        ActionBarTools.hintXX(this,ActionBarTools.HINT_TITLE);
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
        sugSearch = (EditText) findViewById(R.id.et_bdmap_search);
        lv_searchlist = (ListView) findViewById(R.id.lv_map_searchlist);

        sugSearch.addTextChangedListener(this);
        //stand by driving navigation
        NavInitManager.getInstance().initAll(this);

        naviFrame  = (ViewPager) findViewById(R.id.vp_bdmap_bottom);




        showOrhintBottomFragemnt(false);//Start up hint navigation fragment fistly
        initFragment();

        initData(context);

        initListener();
    }

    private void initData(Context context){
        mSearch = RoutePlanSearch.newInstance();
        sug_data = new LinkedList<SuggestionResult.SuggestionInfo>();
        sug_adapter = new SugListAdapter(context, sug_data);
        lv_searchlist.setAdapter(sug_adapter);

    }

    private void initListener(){
        lv_searchlist.setOnItemClickListener(lv_item_listener);
        mSearch.setOnGetRoutePlanResultListener(routItemListener);

    }
    private AdapterView.OnItemClickListener lv_item_listener = new AdapterView.OnItemClickListener(){

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            SuggestionResult.SuggestionInfo suginfo = sug_data.get(position);
            if (suginfo == null) return;
            if (sugSearch != null) {
                String addr = suginfo.city + suginfo.district + suginfo.key;
                sugSearch.setText(addr);
                endCity = suginfo.city;
                endWhere = suginfo.key;
                SUGITEM_CLICK = true;
            }
            goLatLng = suginfo.pt;
            okNavi = true;
            clearSug();
            lvOn_Off(false);
        }
    };

    private OnGetRoutePlanResultListener routItemListener = new OnGetRoutePlanResultListener() {
        @Override
        public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {
            if (walkingRouteResult == null||walkingRouteResult.error != BikingRouteResult.ERRORNO.NO_ERROR){
                Toast.makeText(BaiduMapActivity.this,"未查询到可规划路线！！",Toast.LENGTH_LONG).show();
                return;
            }
            WalkingRouteLine walkingRouteLine = walkingRouteResult.getRouteLines().get(0);
            if (walkingRouteLine!=null){
                showRouteOverlayout(walkingRouteLine);
            }
        }

        @Override
        public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {

        }

        @Override
        public void onGetMassTransitRouteResult(MassTransitRouteResult massTransitRouteResult) {

        }

        @Override
        public void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult) {

        }

        @Override
        public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {

        }

        @Override
        public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {
            if (bikingRouteResult == null||bikingRouteResult.error != BikingRouteResult.ERRORNO.NO_ERROR){
                Toast.makeText(BaiduMapActivity.this,"未查询到可规划路线！！",Toast.LENGTH_LONG).show();
                return;
            }
            BikingRouteLine bikingRouteLine = bikingRouteResult.getRouteLines().get(0);
            if (bikingRouteLine!=null){
                showRouteOverlayout(bikingRouteLine);
            }
        }
    };

    private void bikeRouteNavi(){
        if (nowLatitude == 0 || goLatLng == null) return;
        LatLng start = new LatLng(nowLatitude,nowLongitude);
        final PlanNode stNode = PlanNode.withLocation(start);
        final PlanNode enNode = PlanNode.withLocation(goLatLng);
        mSearch.bikingSearch((new BikingRoutePlanOption()).from(stNode).to(enNode));

    }

    private void walkRouteNavi(){
        if (nowLatitude == 0 || goLatLng == null) return;
        LatLng start = new LatLng(nowLatitude,nowLongitude);
        final PlanNode stNode = PlanNode.withLocation(start);
        final PlanNode enNode = PlanNode.withLocation(goLatLng);
        mSearch.walkingSearch((new WalkingRoutePlanOption()).from(stNode).to(enNode));
    }
    /**
     * Add a suggestion result information
     * @param sugInfo
     */
    private void addSugItem(SuggestionResult.SuggestionInfo sugInfo){
        if (sug_data != null){
            sug_data.add(sugInfo);
        }
    }
    /**
     * Clear all suggestion result information
     */
    private void clearSug(){
        if (sug_data!=null){
            sug_data.clear();
        }
        lvOn_Off(false);
    }

    /**
     * 隐藏或者显示ListView  for suggestRearch
     * @param on_off
     */
    private void lvOn_Off(boolean on_off){
        if (lv_searchlist == null) return;
        if (on_off){
            lv_searchlist.setVisibility(View.VISIBLE);
        }else {
            lv_searchlist.setVisibility(View.GONE);
        }
    }

    private void initFragment(){
        fragmentsDatas = new ArrayList<>();
        //TODO add fragments
        fragmentsDatas.add( new BusNaviInfoFragment());

        fragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragmentsDatas.get(position);
            }

            @Override
            public int getCount() {
                return fragmentsDatas.size();
            }
        };
        naviFrame.setAdapter(fragmentPagerAdapter);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLocationInfoEvent(LocationInfoEvent locInfoEvt){
        if (locInfoEvt != null){
            preLongitude = nowLongitude;
            preLatitude = nowLatitude;
            nowLongitude = locInfoEvt.getLocationInfo().getLongitude();
            nowLatitude = locInfoEvt.getLocationInfo().getLatitude();
            nowAddr = locInfoEvt.getLocationInfo().getLocationDescribe();

            startCity = locInfoEvt.getLocationInfo().getCity();
            startWhere = locInfoEvt.getLocationInfo().getWhere();
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
        MapStatusUpdate msu = MapStatusUpdateFactory.newLatLngZoom(ll,zoom_param);
        mBaiduMap.animateMapStatus(msu);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.bdmap_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.bdmap_map:
                naviFrame.setVisibility(View.GONE);
                break;
            case R.id.bdmap_driving:
                DrivingNaviStart();
                SUGITEM_CLICK = false;
                break;
            case R.id.bdmp_bus:
                naviFrame.setCurrentItem(0);
                showOrhintBottomFragemnt(true);
                break;
            case R.id.bdmap_biking:
                bikeRouteNavi();
                naviFrame.setVisibility(View.GONE);
                SUGITEM_CLICK = false;
                break;
            case R.id.bdmap_walking:
                walkRouteNavi();
                naviFrame.setVisibility(View.GONE);
                SUGITEM_CLICK = false;
                break;
        }
        lvOn_Off(false);
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        scriberRegister(this);
        restart_clean();
    }
    @Override
    protected void onResume() {
        super.onResume();
        lvOn_Off(false);
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
        exit_release();

    }

    /* register Subscriber */
    private void scriberRegister(Object obj){
        if (!EventBus.getDefault().isRegistered(obj)){
            EventBus.getDefault().register(obj);
        }
    }

    private void  DrivingNaviStart(){
         if (okNavi) {
             BNRoutePlanNode sNode = new BNRoutePlanNode(nowLongitude, nowLatitude,
                     nowAddr, null, BNRoutePlanNode.CoordinateType.BD09LL);
             BNRoutePlanNode eNode = new BNRoutePlanNode(goLatLng.longitude, goLatLng.latitude,
                     sugSearch.getText().toString(), null, BNRoutePlanNode.CoordinateType.BD09LL);
             NavInitManager.getInstance().activityList.clear();
             NavInitManager.getInstance().startDrivingNavi(this, sNode, eNode, BNDemoGuideActivity.class);
         }else {
             Toast.makeText(this,"请在建议下拉菜单选择地点",Toast.LENGTH_LONG).show();
         }
         okNavi = false;
    }

    /**
     * The callback of SuggestionSearch, the suggestion information
     * @param suggestionResult
     */
    @Override
    public void onGetSuggestionResult(SuggestionResult suggestionResult) {
        if (suggestionResult == null || suggestionResult.getAllSuggestions() == null) {
            return;
        }else
        {
            List<SuggestionResult.SuggestionInfo> resl=suggestionResult.getAllSuggestions();
            clearSug();
            for(int i=0;i<resl.size();i++)
            {
//                String addr = resl.get(i).city+resl.get(i).district+resl.get(i).key;
                addSugItem(resl.get(i));
            }
            lvOn_Off(true);

        }
    }
    /******************************************************************************************/
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        //suggestionSearch.requestSuggestion(new SuggestionSearchOption().city("中国").keyword(s.toString()));
        lvOn_Off(true);
        String tmp = s.toString();
//        String city = null;
//        String detail = "";
//        if (tmp.contains("市")&&tmp.length()>2){
//            city = tmp.substring(0,tmp.indexOf("市")-1);
//            detail = tmp.substring(tmp.indexOf("市")+1,tmp.length()-1);
//        }else if (tmp.contains("省")&&tmp.length()>2){
//            city = tmp.substring(0,tmp.indexOf("省")-1);
//            detail = tmp.substring(tmp.indexOf("省")+1,tmp.length()-1);
//        }
        suggestionSearch.requestSuggestion(new SuggestionSearchOption().city("中国").keyword(tmp));
//        if (city==null){
//            suggestionSearch.requestSuggestion(new SuggestionSearchOption().city("中国").keyword(tmp));
//        }else {
//            suggestionSearch.requestSuggestion(new SuggestionSearchOption().city(city).keyword(detail));
//        }

    }
    @Override
    public void afterTextChanged(Editable s) {

    }
    /******************************************************************************************/
    private void restart_clean(){
        if (sugSearch != null)
            sugSearch.setText("");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // TODO Auto-generated method stub
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            for (int ret : grantResults) {
                if (ret == 0) {
                    continue;
                } else {
                    Toast.makeText(BaiduMapActivity.this, "缺少导航基本的权限!", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        } else if (requestCode == 2) {
            for (int ret : grantResults) {
                if (ret == 0) {
                    continue;
                }
            }
            DrivingNaviStart();
        }
    }

    /**
     * Release resource before exit activity
     *
     */
    private void exit_release(){
        if (suggestionSearch!=null)
           suggestionSearch.destroy();
        NavInitManager.DrivingNavi_clean();//Driving navigation release

        /* Speed up recycle memory*/
        System.gc();
        System.runFinalization();
    }

    public void showOrhintBottomFragemnt(boolean NO_OFF){
        if (naviFrame != null){
            if (NO_OFF){
                naviFrame.setVisibility(View.VISIBLE);
            }else {
                naviFrame.setVisibility(View.GONE);
            }
        }
    }

    public void showRouteOverlayout(Object obj){
        mBaiduMap.clear();
        zoom_param = ZOOM_PREVIEW;
        if (obj instanceof TransitRouteLine){
            TransitRouteOverlay overlay = new TransitRouteOverlay(mBaiduMap);
            mBaiduMap.setOnMarkerClickListener(overlay);
            routeOverlay = overlay;
            overlay.setData((TransitRouteLine) obj);
            overlay.addToMap();
            overlay.zoomToSpan();
            routeOverlay.removeFromMap();
            routeOverlay.addToMap();
            showOrhintBottomFragemnt(false);
        }else if (obj instanceof WalkingRouteLine){
            WalkingRouteOverlay overlay = new WalkingRouteOverlay(mBaiduMap);
            mBaiduMap.setOnMarkerClickListener(overlay);
            routeOverlay = overlay;
            overlay.setData((WalkingRouteLine) obj);
            overlay.addToMap();
            overlay.zoomToSpan();
            routeOverlay.removeFromMap();
            routeOverlay.addToMap();
            showOrhintBottomFragemnt(false);
        }else if (obj instanceof BikingRouteLine){
            BikingRouteOverlay overlay = new BikingRouteOverlay(mBaiduMap);
            mBaiduMap.setOnMarkerClickListener(overlay);
            routeOverlay = overlay;
            overlay.setData((BikingRouteLine) obj);
            overlay.addToMap();
            overlay.zoomToSpan();
            routeOverlay.removeFromMap();
            routeOverlay.addToMap();
            showOrhintBottomFragemnt(false);
        }
    }
}
