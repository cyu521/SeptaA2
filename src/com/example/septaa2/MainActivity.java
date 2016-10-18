package com.example.septaa2;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;



import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends ListActivity implements OnItemLongClickListener {

	// declare class variables
		private ArrayList<Train>m_parts = new ArrayList<Train>();
		private Runnable viewParts;
		private TrainAdapter m_adapter;
		private Spinner dest;
		private Spinner src;
		private String[] stops = {"30th Street Station", "49th St", "Airport Terminal A", "Airport Terminal B", "Airport Terminal C-D", "Airport Terminal E-F", "Allegheny", "Allen Lane", "Ambler", "Angora", "Ardmore", "Ardsley", "Bala", "Berwyn", "Bethayres", "Bridesburg", "Bristol", "Bryn Mawr", "Carpenter", "Chalfont", "Chelten Avenue", "Cheltenham", "Chester TC", "Chestnut Hill East", "Chestnut Hill West", "Claymont", "Clifton-Aldan", "Colmar", "Conshohocken", "Cornwells Heights", "Crestmont", "Croydon", "Crum Lynne", "Curtis Park", "Cynwyd", "Daylesford", "Darby", "Delaware Valley College", "Devon", "Downingtown", "Doylestown", "East Falls", "Eastwick Station", "Eddington", "Eddystone", "Elkins Park", "Elm St", "Elwyn Station", "Exton", "Fern Rock TC", "Fernwood", "Folcroft", "Forest Hills", "Ft Washington", "Fortuna", "Fox Chase", "Germantown", "Gladstone", "Glenolden", "Glenside", "Gravers", "Gwynedd Valley", "Hatboro", "Haverford", "Highland Ave", "Highland", "Holmesburg Jct", "Ivy Ridge", "Jenkintown-Wyncote", "Langhorne", "Lansdale", "Lansdowne", "Lawndale", "Levittown", "Link Belt", "Main St", "Malvern", "Manayunk East", "Marcus Hook", "Market East", "Meadowbrook", "Media", "Melrose Park", "Merion", "Miquon", "Morton", "Moylan-Rose Valley", "Mt Airy", "Narberth", "Neshaminy Falls", "New Britain", "Newark", "Norristown TC", "North Broad St", "North Hills", "North Philadelphia", "North Wales", "Norwood", "Olney", "Oreland", "Overbrook", "Paoli", "Penllyn", "Pennbrook", "Philmont", "Primos", "Prospect Park", "Queen Lane", "Radnor", "Ridley Park", "Rosemont", "Roslyn", "Rydal", "Ryers", "Secane", "Sharon Hill", "Somerton", "Spring Mill", "St. Davids", "St. Martins", "Stenton", "Strafford", "Suburban Station", "Swarthmore", "Tacony", "Temple U", "Thorndale", "Torresdale", "Trenton", "Trevose", "Tulpehocken", "University City", "Upsal", "Villanova", "Wallingford", "Warminster", "Washington Lane", "Wayne Jct", "Wayne", "West Trenton", "Whitford", "Willow Grove", "Wilmington", "Wissahickon", "Wister", "Woodbourne", "Wyndmoor", "Wynnefield Avenue", "Wynnewood, Yardley"};
		private String dest1="30th Street Station";
		private String src1="30th Street Station";
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//get the spinner info and set the spinner data 
		this.dest = (Spinner)this.findViewById(R.id.destSpin);
		this.src = (Spinner)this.findViewById(R.id.sourceSpin);
		ArrayAdapter<String> adapter_stops = new ArrayAdapter<String>(this,  android.R.layout.simple_spinner_item, stops);
		dest.setAdapter(adapter_stops);
		src.setAdapter(adapter_stops);
		
		//create on item select listener for the two spinners (code on buttom of the class)
		dest.setOnItemSelectedListener(new descSelect()); 
		src.setOnItemSelectedListener(new srcSelect()); 
		
		 // instantiate our ItemAdapter class
	     m_adapter = new TrainAdapter(this, R.layout.item_list, m_parts);
        setListAdapter(m_adapter);
     
        //list view on item long click listener, that displays train's last stop, sch time, and depart time.
        ListView listView = (ListView) findViewById(android.R.id.list);
        listView.setOnItemLongClickListener(new OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                    int pos, long id) {
                // TODO Auto-generated method stub

   			  String trainNo= m_parts.get(pos).getTrainno();
   			   String url = "http://www3.septa.org/hackathon/RRSchedules/"+trainNo;
   			   //using async class to execute the url above and displays toast
   			   new getTrainTime().execute(url);

                return true;
            }
        }); 
       
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	//creating a new activity for the map. check MapActivity for more info
	public void openMap(View view){
		  Intent intent = new Intent(MainActivity.this, MapActivity.class);
	      	startActivity(intent); 
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	private static String convertInputStreamToString(InputStream inputStream) throws IOException{
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

	//class that gets the train info using asynctask
class getTrainInfo extends AsyncTask<String, Void, String> {

@Override
protected String doInBackground(String... uri) {
	InputStream inputStream = null;
	String result = "";
	try {

		// create HttpClient
		HttpClient httpclient = new DefaultHttpClient();

		// make GET request to the given URL
		HttpResponse httpResponse = httpclient.execute(new HttpGet(uri[0]));

		// receive response as inputStream
		inputStream = httpResponse.getEntity().getContent();

		// convert inputstream to string
		if(inputStream != null)
			result = convertInputStreamToString(inputStream);
		else
			result = "Did not work!";

	} catch (Exception e) {
		Log.d("InputStream", e.getLocalizedMessage());
	}

	return result;
}


//here we parse the json string and put mparts with the train info
@Override
protected void onPostExecute(String result) {
    super.onPostExecute(result);
    try {
    
    	JSONArray septa = new JSONArray(result);
    	//if there's no train info, we display the toast there are no train running
		if(septa.length()==0)
			Toast.makeText( getApplicationContext(), "There are no train running", Toast.LENGTH_SHORT).show();
		m_parts=new ArrayList<Train>();
		m_parts.add(new Train("Departure","Arrival","Train"));
		   
		for (int i = 0; i < septa.length(); i++) {
			JSONObject train = septa.getJSONObject(i);
			String trainNum = train.getString("orig_train");
			String dptime = train.getString("orig_departure_time");
			String avtime = train.getString("orig_arrival_time");
			m_parts.add(new Train("  "+dptime+"  ",avtime,trainNum));
		}
	    m_adapter = new TrainAdapter(MainActivity.this, R.layout.item_list, m_parts);
        setListAdapter(m_adapter);
		
       
		
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} 

    
}
}

//class that gets the train time info using async task
class getTrainTime extends AsyncTask<String, Void, String> {

@Override
protected String doInBackground(String... uri) {
	InputStream inputStream = null;
	String result = "";
	try {

		// create HttpClient
		HttpClient httpclient = new DefaultHttpClient();

		// make GET request to the given URL
		HttpResponse httpResponse = httpclient.execute(new HttpGet(uri[0]));

		// receive response as inputStream
		inputStream = httpResponse.getEntity().getContent();

		// convert inputstream to string
		if(inputStream != null)
			result = convertInputStreamToString(inputStream);
		else
			result = "Did not work!";

	} catch (Exception e) {
		Log.d("InputStream", e.getLocalizedMessage());
	}

	return result;
}


//here we parse the json string and put mparts with the train time info
@Override
protected void onPostExecute(String result) {
    super.onPostExecute(result);
    try {
    
    	JSONArray septa = new JSONArray(result);
    	String actime=null;
		String schtime=null;
		String station=null;
		for (int i = 0; i < septa.length(); i++) {
			JSONObject train = septa.getJSONObject(i);
			String act_tm = train.getString("act_tm");
			String sch_tm = train.getString("sched_tm");
			String stat =train.getString("station");
			if(act_tm.equals("na")){
				break;
			}
			actime = act_tm;
			schtime =sch_tm;
			station=stat;
		}
	   
		//if the first item is na. it means that the train did not start running
		//else we display the last stop that the train at and the time info
		   if(actime==null)
		   {
			   Toast.makeText( getApplicationContext(), "This train did not start running", Toast.LENGTH_SHORT).show();
		   }
		   else
			   Toast.makeText( getApplicationContext(), "The train was scheduled to leave the station "+station+" at "+schtime+" and it was there at "+actime, Toast.LENGTH_LONG).show();
	
		
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} 

    
}
}

//here's the code for item listner for the destination spinner
public class descSelect implements OnItemSelectedListener{

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
     
        dest1  = arg0.getItemAtPosition(arg2).toString();
     
        viewParts = new Runnable(){
        	public void run(){
        		handler.sendEmptyMessage(0);
        	}
        };

        // here we call the thread we just defined - it is sent to the handler below.
        Thread thread =  new Thread(null, viewParts, "MagentoBackground");
        thread.start();
    }

    private Handler handler = new Handler()
	 {
		public void handleMessage(Message msg)
		{
			 m_adapter = new TrainAdapter(MainActivity.this, R.layout.item_list, m_parts);
			src1= src1.replaceAll(" ", "%20");
			dest1= dest1.replaceAll(" ", "%20");
		        String url ="http://www3.septa.org/hackathon/NextToArrive/"+src1+"/"+dest1+"/30";
		        //this calls the get train info and execute the url and parse out the json and display it on list view
		    	new getTrainInfo().execute(url);

		        m_adapter = new TrainAdapter(MainActivity.this, R.layout.item_list, m_parts);
		        setListAdapter(m_adapter);
	}
	 };

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }

}

//here's the listner class for source spinner
public class srcSelect implements OnItemSelectedListener{

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
        src1  = arg0.getItemAtPosition(arg2).toString();
       
        // here we are defining our runnable thread.
        viewParts = new Runnable(){
        	public void run(){
        		handler.sendEmptyMessage(0);
        	}
        };

        // here we call the thread we just defined - it is sent to the handler below.
        Thread thread =  new Thread(null, viewParts, "MagentoBackground");
        thread.start();
    }

    private Handler handler = new Handler()
	 {
		public void handleMessage(Message msg)
		{
			 m_adapter = new TrainAdapter(MainActivity.this, R.layout.item_list, m_parts);
			src1= src1.replaceAll(" ", "%20");
			dest1= dest1.replaceAll(" ", "%20");
		        String url ="http://www3.septa.org/hackathon/NextToArrive/"+src1+"/"+dest1+"/30";
		        Log.v("url", url);
		    	new getTrainInfo().execute(url);
		    	
		        m_adapter = new TrainAdapter(MainActivity.this, R.layout.item_list, m_parts);
		        setListAdapter(m_adapter);
	}
	 };
	 
    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }

}
@Override
public boolean onItemLongClick(AdapterView<?> parent, View view, int position,
		long id) {
	// TODO Auto-generated method stub
	return false;
}

}
