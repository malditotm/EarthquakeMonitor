package com.test.earthquakemonitor.map;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.test.earthquakemonitor.R;
import com.test.earthquakemonitor.util.GsonUtil;
import com.test.earthquakemonitor.vo.EarthquakeItemVo;
import com.test.earthquakemonitor.vo.EarthquakesVo;

public class MapSummaryAct extends FragmentActivity {
	MapSummaryAct thisRef = this;
	
	private GoogleMap googleMap;
	private SupportMapFragment mapFragment;
	private int mapType;
	
	List<EarthquakeItemVo> earthquakes;
	private List<Marker> markers;
	private LatLng centerLatLng;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_map_summary);
		//setTitle(" ");
		initialize();
    }
    
    private void initialize(){
    	Intent fromIntent = getIntent();
		mapType = GoogleMap.MAP_TYPE_NORMAL;
        
        mapFragment = (SupportMapFragment) this.getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        googleMap = mapFragment.getMap();
        
        setMapConfiguration();
        readValues(fromIntent);
        setMarkers();
    	
    }
    
    public void setMapConfiguration(){
        googleMap.setMyLocationEnabled(false);
        googleMap.getUiSettings().setMyLocationButtonEnabled(false);
        googleMap.getUiSettings().setCompassEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
    }
    
    public void setMarkers(){
    	markers = new ArrayList<Marker>();
    	Marker marker;
    	LatLngBounds.Builder builder = new LatLngBounds.Builder();
    	System.out.println("#Earthqks: " + earthquakes.size());
    	for(EarthquakeItemVo earthquake: earthquakes){
	    	marker = googleMap.addMarker(new MarkerOptions()
		        .flat(true)
		        .title(earthquake.getPlace())
		        .snippet("Magnitude: " + earthquake.getMag())
		        .icon(getBitmap(earthquake.getMag().intValue()))
		        .anchor(0.5f, 1.0f)
		        .position(earthquake.getLatLng()));
	    	builder.include(earthquake.getLatLng());
	    	markers.add(marker);
	    	System.out.println("Marker: " + earthquake.getPlace() + " LatLng: " + GsonUtil.objectToGsonString(earthquake.getLatLng()));
    	}
    	LatLngBounds bounds = builder.build();
    	centerLatLng = bounds.getCenter();
    	adjustBounds();
    }
    
    public BitmapDescriptor getBitmap(int mag){
    	BitmapDescriptor sprite;
		switch (mag) {
			case 0:
				sprite = BitmapDescriptorFactory.fromResource(R.drawable.pointer_darker_green);
				break;
				
			case 1:
				sprite = BitmapDescriptorFactory.fromResource(R.drawable.pointer_dark_green);
				break;
				
			case 2:
				sprite = BitmapDescriptorFactory.fromResource(R.drawable.pointer_green);
				break;
	
			case 3:
			case 4:
				sprite = BitmapDescriptorFactory.fromResource(R.drawable.pointer_yellow);
				break;
				
			case 5:
			case 6:
				sprite = BitmapDescriptorFactory.fromResource(R.drawable.pointer_orange);
				break;
				
			case 7:
			case 8:
				sprite = BitmapDescriptorFactory.fromResource(R.drawable.pointer_red);
				break;
				
			case 9:
			default:
				sprite = BitmapDescriptorFactory.fromResource(R.drawable.pointer_dark_red);
				break;
		}
		return sprite;
    }
    
    public void adjustBounds(){
    	googleMap.animateCamera(CameraUpdateFactory.newLatLng(centerLatLng));
    }
    
    public void readValues(Intent fromIntent){
    	System.out.println("read: " + fromIntent.getExtras().get("earthquakes"));
    	EarthquakesVo earthquakesVo  = (EarthquakesVo) GsonUtil.gsonStringToObject(fromIntent.getExtras().get("earthquakes").toString(), new EarthquakesVo());
    	earthquakes = earthquakesVo.getEarthquakes();
    }

    public void centerMapBtnOnClick(View view){
    	adjustBounds();
    }
    
    public void showStreetWorldToggleBtnOnClick(View view){
    	if(mapType == GoogleMap.MAP_TYPE_NORMAL){
    		googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE); 
    		mapType = GoogleMap.MAP_TYPE_SATELLITE;
    	}
    	else if(mapType == GoogleMap.MAP_TYPE_SATELLITE){
    		googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
    		mapType = GoogleMap.MAP_TYPE_HYBRID;
    	}
    	else {
    		googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL); 
    		mapType = GoogleMap.MAP_TYPE_NORMAL;
    	}
    }
}
