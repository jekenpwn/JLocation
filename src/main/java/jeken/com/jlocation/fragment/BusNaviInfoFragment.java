package jeken.com.jlocation.fragment;

import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteLine;
import com.baidu.mapapi.search.route.TransitRoutePlanOption;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;

import java.util.List;

import jeken.com.jlocation.R;
import jeken.com.jlocation.activity.BaiduMapActivity;
import jeken.com.jlocation.adapter.BusinfoAdapter;

/**
 * Created by jeken on 2017/11/26.
 */

public class BusNaviInfoFragment extends BaseFragment implements OnGetRoutePlanResultListener {
    private ListView busInfoRecycView = null;
    private EditText et_startcity,et_endcity;
    private EditText et_startAddr,et_endAddr;
    private Button btn_search;
    private BaiduMapActivity parentAc;

    private List<TransitRouteLine> data = null;
    private BusinfoAdapter adapter = null;

    // 搜索相关
    RoutePlanSearch mSearch = null;    // 搜索模块，也可去掉地图模块独立使用
    @Override
    public void onAttach(Activity ac) {
        parentAc = (BaiduMapActivity)ac;
        super.onAttach(ac);
    }

    @Override
    public Object getContentView() {
        return R.layout.fragment_busnviinfo;
    }
    @Override
    public void initView(View v) {
        busInfoRecycView = v.findViewById(R.id.fg_busnvi_recycview);
        et_startcity = v.findViewById(R.id.fg_businfo_etbgcity);
        et_startAddr = v.findViewById(R.id.fg_businfo_etbgaddr);
        et_endcity = v.findViewById(R.id.fg_businfo_etdscity);
        et_endAddr = v.findViewById(R.id.fg_businfo_etdsaddr);
        btn_search = v.findViewById(R.id.fg_businfo_btnsearch);
        initData();
    }

    private void initData() {
        mSearch = RoutePlanSearch.newInstance();
        mSearch.setOnGetRoutePlanResultListener(this);
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchDataLoad();
            }
        });
    }

    private void searchDataLoad(){
        String startAddr = et_startAddr.getText().toString();
        String startCity = et_startcity.getText().toString();
        String endAddr = et_endAddr.getText().toString();
        String endCity = et_endcity.getText().toString();
        if (startCity.equals("")) {
            TostShow("请输入目的地城市");
        }else if (startAddr.equals("")){
            TostShow("请输入目的地位置");
        }else if (endCity.equals("")){
            TostShow("请输入起始地城市");
        }else if (endAddr.equals("")){
            TostShow("请输入起始地位置");
        }else {
            PlanNode sNode = PlanNode.withCityNameAndPlaceName(startCity,startAddr);
            PlanNode eNode = PlanNode.withCityNameAndPlaceName(endCity,endAddr);
            mSearch.transitSearch((new TransitRoutePlanOption().from(sNode).city(startCity).
                    to(eNode)));
        }

    }

    private void TostShow(String txt){
        Toast.makeText(getActivity(),txt,Toast.LENGTH_LONG).show();
    }

    public void busInfoEntry(TransitRouteResult transitRouteResult){
        //Toast.makeText(getActivity(),transitRouteResult.error.name(),Toast.LENGTH_LONG).show();
        if (transitRouteResult!=null){
            if (transitRouteResult.getRouteLines()==null){
                        Toast.makeText(getActivity(),"size=null",Toast.LENGTH_LONG).show();
            }else {
                        Toast.makeText(getActivity(),"size="+transitRouteResult.getRouteLines().size(),Toast.LENGTH_LONG).show();
            }
        }
        //Log.i("TAG","size="+transitRouteResult.getRouteLines().size());
        if (data == null) {
            data = transitRouteResult.getRouteLines();
            adapter = new BusinfoAdapter(getContext(),data);
            busInfoRecycView.setAdapter(adapter);
        }else {
            data = transitRouteResult.getRouteLines();
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {

    }

    @Override
    public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {
        if (transitRouteResult == null||transitRouteResult.error != TransitRouteResult.ERRORNO.NO_ERROR){
            Toast.makeText(getActivity(),"未查询到可规划路线！！",Toast.LENGTH_LONG).show();
            return;
        }

            data = transitRouteResult.getRouteLines();
            adapter = new BusinfoAdapter(getContext(),data);
            busInfoRecycView.setAdapter(adapter);
            busInfoRecycView.setOnItemClickListener(itemClickListener);

    }

    private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            parentAc.showRouteOverlayout(data.get(position));
        }
    };
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

    }
}
