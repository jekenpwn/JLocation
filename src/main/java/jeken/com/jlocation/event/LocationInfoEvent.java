package jeken.com.jlocation.event;

import jeken.com.jlocation.entity.LocationInfo;

/**
 * Created by Administrator on 2017-09-18.
 */

public class LocationInfoEvent {
    private LocationInfo locInfoEvt;
    public void setLocationInfo(LocationInfo locInfoEvt){
        this.locInfoEvt = locInfoEvt;
    }
    public LocationInfo getLocationInfo(){
        return this.locInfoEvt;
    }

}
