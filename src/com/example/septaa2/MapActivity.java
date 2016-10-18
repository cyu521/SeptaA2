package com.example.septaa2;



import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import com.example.septaa2.MainActivity.getTrainTime;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.ImageView;

//the activity that shows the map and creates the markers
public class MapActivity extends Activity  {
	private GoogleMap mMap;  
	@SuppressLint("NewApi")
	@Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.map);
	        //this code is commented out because the emmunlator could not get the current location
	        //however if we un comment this code and put it on an android, it will work
	        
	       /*LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE); 
			Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			double lng = location.getLongitude();
			double lat = location.getLatitude();
		*/
	        mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
			        .getMap();
			//mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 15));
			mMap.setMyLocationEnabled(true);
			  String url = "http://www3.septa.org/hackathon/TrainView/";
			  //we call on an async class that parse out the json and creates markers on the map
  			   new getMapInfo().execute(url);
  			   	

	}

class getMapInfo extends AsyncTask<String, Void, String> {
	//Making a network call to get the json string
	//we return the json string 
@Override
protected String doInBackground(String... uri) {
    HttpClient httpclient = new DefaultHttpClient();
    HttpResponse response;
    String responseString = null;
    try {
        response = httpclient.execute(new HttpGet(uri[0]));               
        StatusLine statusLine = response.getStatusLine();
        if(statusLine.getStatusCode() == HttpStatus.SC_OK){
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            response.getEntity().writeTo(out);
            out.close();
            responseString = out.toString();
        } else{
            //Closes the connection.
            response.getEntity().getContent().close();
            throw new IOException(statusLine.getReasonPhrase());
        }
    } catch (Exception e){
    
    }
    
    	return responseString;
}

//here we parse the json string and put mparts with the weather info
@Override
protected void onPostExecute(String result) {
    super.onPostExecute(result);
    try {
    	
    	JSONArray trains = new JSONArray(result);
		for (int i = 0; i < trains.length(); i++) {
			JSONObject train = trains.getJSONObject(i);
			Double lat =  Double.parseDouble(train.getString("lat"));
			Double lon =  Double.parseDouble(train.getString("lon"));
			String trainNo = train.getString("trainno");
			  mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lon))
				        .title(trainNo));
			  //for each item, we get the long and lat and train no and creates a marker for it
		}
		//here we delete the time and json table and we write it
	
	
		
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} 

    
}
}
}