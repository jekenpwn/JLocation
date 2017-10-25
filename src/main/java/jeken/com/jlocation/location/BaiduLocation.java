package jeken.com.jlocation.location;

import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClientOption;

public interface BaiduLocation {

	boolean registerListener(BDLocationListener listener);
	void unregisterListener(BDLocationListener listener);
	boolean setLocationOption(LocationClientOption option);
	LocationClientOption getOption();
	LocationClientOption getDefaultLocationClientOption();
	void start();
	void stop();
	boolean requestHotSpotState();
}
