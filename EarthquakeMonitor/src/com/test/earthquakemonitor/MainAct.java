package com.test.earthquakemonitor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.test.earthquakemonitor.entity.Feature;
import com.test.earthquakemonitor.entity.FeatureCollection;
import com.test.earthquakemonitor.entity.InfoRetrieved;
import com.test.earthquakemonitor.map.DetailAct;
import com.test.earthquakemonitor.map.MapSummaryAct;
import com.test.earthquakemonitor.util.Constants;
import com.test.earthquakemonitor.util.GsonUtil;
import com.test.earthquakemonitor.util.sqlite.ds.InfoRetievedDS;
import com.test.earthquakemonitor.vo.EarthquakeItemVo;
import com.test.earthquakemonitor.vo.EarthquakesVo;

public class MainAct extends Activity {
	MainAct thisRef = this;
	SimpleDateFormat dateFormat = new SimpleDateFormat("EEE dd MMM yyyy, HH:mm:ss a", Locale.ENGLISH);
	private InfoRetievedDS datasource;
	
	TextView generatedTimeTV;
	ListView quakeList;
	List<EarthquakeItemVo> earthquakes;
	boolean hasEarthquakes;
	
	String dataFromServer;
	boolean dataRetrieved;
	ArrayAdapter<EarthquakeItemVo> adapter;
	
	InfoRetrieved infoRetrieved;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_main);
		initialize();
	}
	
	private void initialize(){
		dataRetrieved = false;
		datasource = new InfoRetievedDS(thisRef);
	    datasource.open();
		
		generatedTimeTV = (TextView) findViewById(R.id.generatedTimeTextView);
		quakeList = (ListView) findViewById(R.id.quakeList);
		
		fetchInfo();
	    
	    quakeList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View v, int position, long arg3) {
			    @SuppressWarnings("unchecked")
				ArrayAdapter<EarthquakeItemVo> adapter = (ArrayAdapter<EarthquakeItemVo>) quakeList.getAdapter();
			    EarthquakeItemVo item = adapter.getItem(position);
			    
				Intent intent = new Intent();
				intent.setClass(thisRef, DetailAct.class);
				intent.putExtra("earthquake", GsonUtil.objectToGsonString(item));
				startActivity(intent);
				overridePendingTransition(R.anim.grow_from_middle,R.anim.shrink_to_middle);
			}
		});
	}
	
	public void fetchInfo(){
		new RequestTask().execute();		
	}
	
	void saveInfo(){
		infoRetrieved = datasource.saveInfo(infoRetrieved);
	}
	
	void getSavedInfo(){
	    List<InfoRetrieved> values = datasource.getInfo();
	    String latest = values.get(0).getInfoFromServer();
	    if(latest != null && !latest.equals("")){
	    	dataFromServer = latest;
	    }
	}
	
	void readInfo(){
		hasEarthquakes = false;
		FeatureCollection fetchData = (FeatureCollection) GsonUtil.gsonStringToObject(dataFromServer, new FeatureCollection());
		Log.v("readedInfo", dataFromServer);
		Date generatedDate = new Date(fetchData.getMetadata().getGenerated());
		generatedTimeTV.setText(dateFormat.format(generatedDate));
		
		List<EarthquakeItemVo> eartquakes = new ArrayList<EarthquakeItemVo>();
		EarthquakeItemVo eartquake;
		for(Feature feature: fetchData.getFeatures()){
			eartquake = new EarthquakeItemVo();
			eartquake.setMag(feature.getProperties().getMag());
			eartquake.setPlace(feature.getProperties().getPlace());
			String alert; 
			if(feature.getProperties().getAlert() == null){
				if(feature.getProperties().getMag()< 4){
					alert = "green";
				}
				else if(feature.getProperties().getMag() <6){
					alert = "yellow";
				}
				else if(feature.getProperties().getMag() <6){
					alert = "orange";
				}
				else {
					alert = "red";
				}
				eartquake.setAlert(alert);
			}
			else{
				eartquake.setAlert(feature.getProperties().getAlert());
			}
			eartquake.setTime(feature.getProperties().getTime());
			eartquake.setFelt(feature.getProperties().getFelt());
			eartquake.setUpdated(feature.getProperties().getUpdated());
			double lat = feature.getGeometry().getCoordinates().get(1);
			double lng = feature.getGeometry().getCoordinates().get(0);
			double depth = feature.getGeometry().getCoordinates().get(2);
			eartquake.setDepth(depth);
			eartquake.setLatLng(lat, lng);
			eartquakes.add(eartquake);
			hasEarthquakes = true;
		}
		thisRef.earthquakes = eartquakes;
		createArrayAdapter();
	}
	
	public void createArrayAdapter(){
		System.out.println("Setting the adapter");
		if(earthquakes == null){
			earthquakes = new ArrayList<EarthquakeItemVo>();
		}
		adapter = new ArrayAdapter<EarthquakeItemVo>(thisRef, android.R.layout.simple_list_item_1, earthquakes){
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
                View row = super.getView(position, convertView, parent);
                TextView text = (TextView) row;
                int mag = earthquakes.get(position).getMag().intValue();
                setColor(text, mag);
				return row;
			}
		};
		quakeList.setAdapter(adapter);
	}
	
	public void setColor(TextView textView, int mag){
		switch (mag) {
			case 0:
				textView.setTextColor(getResources().getColor(R.color.range_0));
				textView.setBackgroundResource(R.drawable.info);
				break;
				
			case 1:
				textView.setTextColor(getResources().getColor(R.color.range_1));
				textView.setBackgroundResource(R.drawable.info);
				break;
				
			case 2:
				textView.setTextColor(getResources().getColor(R.color.range_2));
				textView.setBackgroundResource(R.drawable.info);
				break;
	
			case 3:
				textView.setTextColor(getResources().getColor(R.color.range_3));
				textView.setBackgroundResource(R.drawable.info);
				break;
				
			case 4:
				textView.setTextColor(Color.YELLOW);
				textView.setBackgroundResource(R.drawable.info);
				break;
				
			case 5:
				textView.setTextColor(getResources().getColor(R.color.range_5));
				textView.setBackgroundResource(R.drawable.info);
				break;
				
			case 6:
				textView.setTextColor(getResources().getColor(R.color.range_6));
				textView.setBackgroundResource(R.drawable.info);
				break;
	
			case 7:
				textView.setTextColor(getResources().getColor(R.color.range_7));
				textView.setBackgroundResource(R.drawable.info);
				break;
	
			case 8:
				textView.setTextColor(Color.RED);
				textView.setBackgroundResource(R.drawable.info);
				break;
				
			case 9:
			default:
				textView.setTextColor(getResources().getColor(R.color.range_9));
				textView.setBackgroundResource(R.drawable.info);
				break;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_refresh) {
			refreshData();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void sumMapBtnOnClick(View view){
		if(hasEarthquakes){
			Intent intent = new Intent();
			intent.setClass(thisRef, MapSummaryAct.class);
			EarthquakesVo earthquakesVo = new EarthquakesVo();
			earthquakesVo.setEarthquakes(earthquakes);
			intent.putExtra("earthquakes", GsonUtil.objectToGsonString(earthquakesVo));
			startActivity(intent);
			overridePendingTransition(R.anim.grow_from_middle,R.anim.shrink_to_middle);
			
		}
		else{
			Toast.makeText(thisRef, "The information has not been retrieved", Toast.LENGTH_SHORT).show();
		}
	}
	
	public void refreshBtnOnClick(View view){
		refreshData();
	}
	
	public void refreshData(){
		new RequestTask().execute();
		Toast.makeText(thisRef, "Retrieving Info", Toast.LENGTH_SHORT).show();
	}
	
	private class RequestTask extends AsyncTask<Void, Void, Integer> {
	
		protected void onPreExecute() {
			// display progress dialog
		}
	
		protected Integer doInBackground(Void... params) {
			int result = 0;

			InputStream responseInputStream = null;
			try{
				HttpURLConnection httpURLConnection = null;
				URL url = new URL(Constants.URL);
	
				httpURLConnection = (HttpURLConnection)url.openConnection();
				httpURLConnection.setDoOutput(true);
				httpURLConnection.setConnectTimeout(Constants.HTTPCONN_TIMEOUT);
				httpURLConnection.setReadTimeout(Constants.HTTPCONN_TIMEOUT);
				httpURLConnection.setRequestProperty("Accept-Charset", Constants.CHARSET_ISO_8859_1);
				httpURLConnection.setRequestProperty("Content-Type", Constants.CONTENT_TYPE);

				// Receive response
				responseInputStream = httpURLConnection.getInputStream();
				
				// Convert responseInputStream to String
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(responseInputStream));
				StringBuilder stringBuilder = new StringBuilder();
				String line = null;
				
				while ((line = bufferedReader.readLine()) != null) {
					stringBuilder.append(line);
				}
				
				dataFromServer = stringBuilder.toString();

				//Log.v("obtained json:", dataFromServer);
				result = 1;
				dataRetrieved = true;
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch(SocketTimeoutException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {if (responseInputStream != null) {responseInputStream.close();}} catch (IOException e) {e.printStackTrace();}
			}
			return result;
		}
	
		protected void onPostExecute(Integer result) {
			switch(result){
				case 1:
					if(infoRetrieved == null){
						infoRetrieved = new InfoRetrieved();
					}
					infoRetrieved.setInfoFromServer(dataFromServer);
					saveInfo();
					readInfo();
					Toast.makeText(thisRef, "Info Updated", Toast.LENGTH_SHORT).show();
					//DismissDyalog and load data; 
					break;
				
				case 0:
				default:
					getSavedInfo();
					readInfo();
					break;
			}
				
		}
	}
	
}
