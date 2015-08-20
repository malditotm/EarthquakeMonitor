package com.test.earthquakemonitor.entity;

import java.util.List;

public class FeatureCollection{
	private String type;
	private Metadata metadata;
	//private Bbox bbox;
	private List<Double> bbox;
	private List<Feature> features;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Metadata getMetadata() {
		return metadata;
	}
	public void setMetadata(Metadata metadata) {
		this.metadata = metadata;
	}
	public List<Double> getBbox() {
		return bbox;
	}
	public void setBbox(List<Double> bbox) {
		this.bbox = bbox;
	}
	public List<Feature> getFeatures() {
		return features;
	}
	public void setFeatures(List<Feature> features) {
		this.features = features;
	}
}