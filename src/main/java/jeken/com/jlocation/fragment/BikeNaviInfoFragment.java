package jeken.com.jlocation.fragment;

import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;

import java.util.List;

import jeken.com.jlocation.R;
import jeken.com.jlocation.activity.BaiduMapActivity;
import jeken.com.jlocation.adapter.BikeinfoAdapter;

/**
 * Created by jeken on 2017/12/3.
 */

public class BikeNaviInfoFragment extends BaseFragment implements OnGetRoutePlanResultListener {
    private BaiduMapActivity parentAc;

    private TextView tv_result;
    private ListView bikeInfoLV;

    private BikeinfoAdapter adapter;
    private List<BikingRouteLine> data;

    // 搜索相关
    private RoutePlanSearch mSearch = null;    // 搜索模块，也可去掉地图模块独立使用
    @Override
    public Object getContentView() {
        return R.layout.fragment_bikenaviinfo;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        parentAc  = (BaiduMapActivity) activity;
    }

    @Override
    public void initView(View view) {

        tv_result = view.findViewById(R.id.fgbike_tvnoresult);
        bikeInfoLV = view.findViewById(R.id.fgbike_lsvresult);

        initData();
    }
    /*
    *  Init data
    */
    private void initData(){
        mSearch = RoutePlanSearch.newInstance();
        mSearch.setOnGetRoutePlanResultListener(this);

    }

    public void searchBikeRoute(LatLng start,LatLng end){
//        PlanNode startNode = PlanNode.withCityNameAndPlaceName(sCity,sDist);
//        PlanNode endNode = PlanNode.withCityNameAndPlaceName(eCity,eDist);
        final PlanNode startNode = PlanNode.withCityNameAndPlaceName("广州","天河客运站");
        final PlanNode endNode = PlanNode.withCityNameAndPlaceName("广州","马瑞利");
        final PlanNode stNode = PlanNode.withLocation(start);
        final PlanNode enNode = PlanNode.withLocation(end);
//        Log.e("TAG","s="+startNode.getCity()+sDist+ " e=" + endNode.getCity()+eDist);
        if (mSearch != null) {
            mSearch.bikingSearch((new BikingRoutePlanOption()).from(stNode).to(enNode));
        }else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.currentThread().sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (mSearch != null)
                        mSearch.bikingSearch((new BikingRoutePlanOption()).from(stNode).to(enNode));
                }
            }).start();
        }
    }

    private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            parentAc.showRouteOverlayout(data.get(position));
        }
    };

    @Override
    public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {
        if (bikingRouteResult == null) Toast.makeText(getActivity(),"null！！",Toast.LENGTH_LONG).show();
        Toast.makeText(getActivity(),bikingRouteResult.error.name(),Toast.LENGTH_LONG).show();
        if (bikingRouteResult == null||bikingRouteResult.error != BikingRouteResult.ERRORNO.NO_ERROR){
            Toast.makeText(getActivity(),"未查询到可规划路线！！",Toast.LENGTH_LONG).show();
            return;
        }



        Toast.makeText(getActivity(),"size="+bikingRouteResult.getRouteLines().size(),Toast.LENGTH_LONG).show();
        data = bikingRouteResult.getRouteLines();
        adapter = new BikeinfoAdapter(getContext(),data);
        bikeInfoLV.setAdapter(adapter);
        bikeInfoLV.setOnItemClickListener(itemClickListener);
    }


    @Override
    public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {

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


}
