package com.zezoo.emergency;

public class Item {
	private String locationName;
	private String longitude;
	private String latitude;
	private String time;

	public Item(String locationN, String locationX, String locationY, String locationT) {
		this.locationName = locationN;
		this.longitude = locationX;
		this.latitude = locationY;
		this.time = locationT;
	}

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
}
