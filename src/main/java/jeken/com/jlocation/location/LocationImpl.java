package jeken.com.jlocation.location;

import android.content.Context;

import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;

public class LocationImpl implements BaiduLocation{

	private LocationClient client = null;
	private LocationClientOption mOption,DIYoption;
	private Object  objLock = new Object();

	public  LocationImpl(Context locationContext) {
		synchronized (objLock) {
			if(client == null){
				client = new LocationClient(locationContext);
				client.setLocOption(getDefaultLocationClientOption());
			}
		}
	}
	@Override
	public boolean registerListener(BDLocationListener listener) {
		boolean isSuccess = false;
		if(listener != null){
			client.registerLocationListener(listener);
			isSuccess = true;
		}
		return  isSuccess;
		
	}

	@Override
	public void unregisterListener(BDLocationListener listener) {
		if(listener != null){
			client.unRegisterLocationListener(listener);
		}
	}

	@Override
	public boolean setLocationOption(LocationClientOption option) {
		boolean isSuccess = false;
		if(option != null){
			if(client.isStarted())
				client.stop();
			DIYoption = option;
			client.setLocOption(option);
		}
		return isSuccess;
	}

	@Override
	public LocationClientOption getOption() {
		return DIYoption;
	}

	@Override
	public LocationClientOption getDefaultLocationClientOption() {
		if(mOption == null){
			mOption = new LocationClientOption();
			mOption.setLocationMode(LocationMode.Hight_Accuracy);
			mOption.setCoorType("bd09ll");
			mOption.setScanSpan(3000);
		    mOption.setIsNeedAddress(true);
		    mOption.setIsNeedLocationDescribe(true);
		    mOption.setNeedDeviceDirect(false);
		    mOption.setLocationNotify(false);
		    mOption.setIgnoreKillProcess(true);
		    mOption.setIsNeedLocationDescribe(true);
		    mOption.setIsNeedLocationPoiList(true);
		    mOption.SetIgnoreCacheException(false);
		    mOption.setOpenGps(true);
		    mOption.setIsNeedAltitude(false);
		}
		return mOption;
	}

	@Override
	public void start() {
		synchronized (objLock) {
			if(client != null && !client.isStarted()){
				client.start();
			}
		}
	}

	@Override
	public void stop() {
		synchronized (objLock) {
			if(client != null && client.isStarted()){
				client.stop();
				
			}
		}
	}


	@Override
	public boolean requestHotSpotState() {
		return client.requestHotSpotState();
	}

}
