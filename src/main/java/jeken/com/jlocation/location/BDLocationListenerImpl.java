package jeken.com.jlocation.location;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import jeken.com.jlocation.entity.LocationInfo;


public class BDLocationListenerImpl implements BDLocationListener{

	private LocationInfo locationInfo;
	private BDLocationCallback callBack;
	public BDLocationListenerImpl(LocationInfo locationInfo, BDLocationCallback callBack){
		this.locationInfo = locationInfo;
		this.callBack = callBack;
	}


	@Override
	public void onReceiveLocation(BDLocation dbLcn) {
		this.locationInfo.setLocType(dbLcn.getLocType());
		this.locationInfo.setLatitude(dbLcn.getLatitude());
		this.locationInfo.setLongitude(dbLcn.getLongitude());
		this.locationInfo.setUserIndoorState(dbLcn.getUserIndoorState());
		this.locationInfo.setAddr(dbLcn.getAddrStr());
		this.locationInfo.setLocationDescribe(dbLcn.getLocationDescribe());
		this.locationInfo.setCity(dbLcn.getCity());
		this.locationInfo.setWhere(dbLcn.getAddrStr());
	    if(dbLcn.getLocType() == BDLocation.TypeServerError || 
	       dbLcn.getLocType() == BDLocation.TypeNetWorkException ||
	       dbLcn.getLocType() == BDLocation.TypeCriteriaException){
	    	callBack.locationError(dbLcn.getLocType());
	    }else{
	    	callBack.locationSuccess();
	    }
	}
	public LocationInfo getLocationInfo(){
		return this.locationInfo;
	}
}
