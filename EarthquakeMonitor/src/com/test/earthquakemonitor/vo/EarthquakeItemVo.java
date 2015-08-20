package com.test.earthquakemonitor.vo;

import com.google.android.gms.maps.model.LatLng;

public class EarthquakeItemVo {
	private Integer id;
	private Double mag;
	private String place;
	private String alert;
	private Long time;
	private Double depth;
	private Integer felt;
	private Long updated;
	private LatLng latLng;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Double getMag() {
		return mag;
	}
	public void setMag(Double mag) {
		this.mag = mag;
	}
	public String getPlace() {
		return place;
	}
	public void setPlace(String place) {
		this.place = place;
	}
	public String getAlert() {
		return alert;
	}
	public void setAlert(String alert) {
		this.alert = alert;
	}
	public Long getTime() {
		return time;
	}
	public void setTime(Long time) {
		this.time = time;
	}
	public Double getDepth() {
		return depth;
	}
	public void setDepth(Double depth) {
		this.depth = depth;
	}
	public Integer getFelt() {
		return felt;
	}
	public void setFelt(Integer felt) {
		this.felt = felt;
	}
	public Long getUpdated() {
		return updated;
	}
	public void setUpdated(Long updated) {
		this.updated = updated;
	}
	public LatLng getLatLng() {
		return latLng;
	}
	public void setLatLng(LatLng latLng) {
		this.latLng = latLng;
	}
	public void setLatLng(double lat, double lng) {
		this.latLng = new LatLng(lat, lng);
	}
	public String toString(){
		String text = mag + " - " + place;
		return text;
	}
}
