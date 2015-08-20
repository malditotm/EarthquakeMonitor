package com.test.earthquakemonitor.entity;

public class Bbox {
    private Double minLng;
    private Double minLat;
    private Double minDepth;
    private Double maxLng;
    private Double maxLat;
    private Double maxDepth;
    
	public Double getMinLng() {
		return minLng;
	}
	public void setMinLng(Double minLng) {
		this.minLng = minLng;
	}
	public Double getMinLat() {
		return minLat;
	}
	public void setMinLat(Double minLat) {
		this.minLat = minLat;
	}
	public Double getMinDepth() {
		return minDepth;
	}
	public void setMinDepth(Double minDepth) {
		this.minDepth = minDepth;
	}
	public Double getMaxLng() {
		return maxLng;
	}
	public void setMaxLng(Double maxLng) {
		this.maxLng = maxLng;
	}
	public Double getMaxLat() {
		return maxLat;
	}
	public void setMaxLat(Double maxLat) {
		this.maxLat = maxLat;
	}
	public Double getMaxDepth() {
		return maxDepth;
	}
	public void setMaxDepth(Double maxDepth) {
		this.maxDepth = maxDepth;
	}
}
