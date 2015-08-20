package com.test.earthquakemonitor.map;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.test.earthquakemonitor.R;
import com.test.earthquakemonitor.util.GsonUtil;
import com.test.earthquakemonitor.vo.EarthquakeItemVo;

public class DetailAct extends FragmentActivity {
	DetailAct thisRef = this;
	SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE dd MMM yyyy, HH:mm:ss a", Locale.ENGLISH);
	
	private GoogleMap googleMap;
	private SupportMapFragment mapFragment;
	private int mapType;
	
	TextView placeVl;
	TextView magVl;
	TextView alertVl;
	TextView timeVl;
	TextView depthVl;
	TextView feltVl;
	TextView updateVl;
	
	EarthquakeItemVo earthquake;
	@SuppressWarnings("unused")
	private Marker originMarker;
	private LatLng originLatLng;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_detail);
		//setTitle(" ");
		initialize();
    }
    
    private void initialize(){
    	Intent fromIntent = getIntent();
		mapType = GoogleMap.MAP_TYPE_NORMAL;
        
        mapFragment = (SupportMapFragment) this.getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        googleMap = mapFragment.getMap();
        
    	placeVl = (TextView) findViewById(R.id.placeVl);
    	magVl = (TextView) findViewById(R.id.magVl);
    	alertVl = (TextView) findViewById(R.id.alertVl);
    	timeVl = (TextView) findViewById(R.id.timeVl);
    	depthVl = (TextView) findViewById(R.id.depthVl);
    	feltVl = (TextView) findViewById(R.id.feltVl);
    	updateVl = (TextView) findViewById(R.id.updateVl);
        
        setMapConfiguration();
        readValues(fromIntent);
        setMarker();
    	
        placeVl.setText(earthquake.getPlace());
        magVl.setText(earthquake.getMag() + "°Richter");
        alertVl.setText(earthquake.getAlert());
        timeVl.setText(dateFormat.format(new Date(earthquake.getTime())));
        depthVl.setText(earthquake.getDepth() + " km");
        feltVl.setText(earthquake.getFelt() + " reports submitted");
        updateVl.setText(dateFormat.format(new Date(earthquake.getUpdated())));
    }
    
    public void setMapConfiguration(){
        googleMap.setMyLocationEnabled(false);
        googleMap.getUiSettings().setMyLocationButtonEnabled(false);
        googleMap.getUiSettings().setCompassEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);

        originLatLng = new LatLng(39.028881, -101.574981);
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(originLatLng, 3));
        System.out.println("set in position");
    }
    
    public void setMarker(){
    	originLatLng = earthquake.getLatLng();
    	originMarker = googleMap.addMarker(new MarkerOptions()
	        .flat(true)
	        .title(earthquake.getPlace())
	        .snippet("Magnitude: " + earthquake.getMag())
	        .icon(getBitmap(earthquake.getMag().intValue()))
	        .anchor(0.5f, 1.0f)
	        .position(originLatLng));
    	adjustBounds();
    }
    
    public void adjustBounds(){
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(originLatLng, 7));
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
    
    public void readValues(Intent fromIntent){
    	System.out.println("read: " + fromIntent.getExtras().get("earthquake"));
    	earthquake = (EarthquakeItemVo) GsonUtil.gsonStringToObject(fromIntent.getExtras().get("earthquake").toString(), new EarthquakeItemVo());
    	originLatLng = earthquake.getLatLng();
    }

    public void centerPinBtnOnClick(View view){
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
