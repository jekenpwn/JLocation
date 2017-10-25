package jeken.com.jlocation.navi;

import android.app.Activity;

import com.baidu.mapapi.model.LatLng;
import com.baidu.navisdk.adapter.BNRoutePlanNode;

/**
 * Created by jeken on 2017/10/19.
 */

public class NaviCfgBuilder {
    public Activity activity;
    public BNRoutePlanNode startRoutePlan;
    public BNRoutePlanNode endRoutePlan;
    public Class<?> clazz;
    public NaviCfgBuilder(Activity activity){
        this.activity = activity;
    }
    public static NaviCfgBuilder build(Activity activity){
        return new NaviCfgBuilder(activity);
    }
    public NaviCfgBuilder setRoute(LatLng startLatLng,String startDescri,LatLng endLatLng,String endDescri,
                                    BNRoutePlanNode.CoordinateType coordinateType){
        startRoutePlan = new BNRoutePlanNode(startLatLng.longitude,startLatLng.latitude,startDescri,null,coordinateType);
        endRoutePlan = new BNRoutePlanNode(endLatLng.longitude,endLatLng.latitude,endDescri,null,coordinateType);
        return this;
    }
    public NaviCfgBuilder naviActivity(Class<?> clazz){
        this.clazz = clazz;
        return this;
    }
    public boolean hasBaseCfg(){
        if (startRoutePlan.getLatitude() == 0||startRoutePlan.getLongitude()==0||
                endRoutePlan.getLatitude() == 0||endRoutePlan.getLongitude()==0){
            return false;
        }
        if (this.activity == null) return false;
        if (this.clazz == null) return false;
        return true;
    }
}
