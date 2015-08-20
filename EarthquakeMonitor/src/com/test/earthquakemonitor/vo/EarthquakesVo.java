package com.test.earthquakemonitor.vo;

import java.util.ArrayList;
import java.util.List;

public class EarthquakesVo {
	List<EarthquakeItemVo> earthquakes;
	
	public void add(EarthquakeItemVo earthquake){
		if(earthquakes == null){
			earthquakes = new ArrayList<EarthquakeItemVo>();
		}
		earthquakes.add(earthquake);
	}

	public List<EarthquakeItemVo> getEarthquakes() {
		return earthquakes;
	}
	public void setEarthquakes(List<EarthquakeItemVo> earthquakes) {
		this.earthquakes = earthquakes;
	}
}
