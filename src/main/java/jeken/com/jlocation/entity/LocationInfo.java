package jeken.com.jlocation.entity;

public class LocationInfo {

	private int locType;//��λ���� 
	private double latitude;//γ��
	private double longitude;//����
	private int userIndoorState;//�������ж� 1����
	private String addr;//��ַ
	private String locationDescribe;//��λ����
	private String city;
	private String where;

	public int getLocType() {
		return locType;
	}
	public void setLocType(int locType) {
		this.locType = locType;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public int getUserIndoorState() {
		return userIndoorState;
	}
	public void setUserIndoorState(int userIndoorState) {
		this.userIndoorState = userIndoorState;
	}
	public String getAddr() {
		return addr;
	}
	public void setAddr(String addr) {
		this.addr = addr;
	}
	public String getLocationDescribe() {
		return locationDescribe;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getWhere() {
		return where;
	}

	public void setWhere(String where) {
		this.where = where;
	}

	public void setLocationDescribe(String locationDescribe) {
		this.locationDescribe = locationDescribe;
	}
	
	
} 
