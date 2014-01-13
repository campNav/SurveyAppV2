package com.example.surveyappv2;


import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActionBar.LayoutParams;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.wifi.*;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.CalendarContract.Calendars;
import android.support.v4.app.NavUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class nWiFiScans extends Activity 
{
	//declarations
	public static String pntNameStr;
	public static int numScans;
	public static int scansMin;
	LinearLayout 	layout1;
	ScrollView		scrollLayout;
	TextView 		label1;	
	EditText		n1Text;
	EditText		n2Text;
	LayoutParams	params_layout1;

	WifiManager mainWifi = null;
	WifiReceiver receiverWifi;
	List<ScanResult> wifiList;
	StringBuilder sb = new StringBuilder();
	Intent intent;
	SimpleDateFormat time;

	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		scrollLayout = new ScrollView(this);
		scrollLayout.setBackgroundColor(Color.BLUE);
		//Create new layout in "this" activity
		params_layout1 = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT, 1);
		layout1 = new LinearLayout(this);
		layout1.setBackgroundColor(Color.RED);
		layout1.setLayoutParams(params_layout1);
		//Create TextView in "this" activity
		label1 = new TextView(this);
		label1.setBackgroundColor(Color.YELLOW);
		//Put some text in the TextView

		// Get the message from the intent
		intent = getIntent();
		pntNameStr = intent.getStringExtra(MainActivity.EXTRA_pntNameStr);
		numScans = intent.getIntExtra(MainActivity.EXTRA_numScans, 1);

		label1.setText(pntNameStr);


		//Place the TextView inside the Layout
		layout1.addView(label1);
		layout1.setOrientation(LinearLayout.VERTICAL);
		//scrollLayout.addView(label1);
		//layout1.addView(n1Text);
		//layout1.addView(n2Text);

		//By default the layout is set to HOR, so we change it to VERT orientation:

		// Display layout1 when the activity is created:
		setContentView(layout1);

		mainWifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		checkIfWifiIsOn(mainWifi);

		receiverWifi = new WifiReceiver();
		registerReceiver(receiverWifi, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
		mainWifi.startScan();
		label1.setText("Starting Scan..."+String.valueOf(numScans));

		/*Toast.makeText(getApplicationContext(), "IM MOTHERFUCKING DONE" ,
				Toast.LENGTH_SHORT).show();
		//Log.d("Scan Results",sb.toString());
		Intent i=new Intent(nWiFiScans.this, MainActivity.class);
		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(i);
		finish();*/
	}

	public void checkIfWifiIsOn(WifiManager mainWifi)
	{ 
		if (mainWifi.isWifiEnabled() == false)
		{  
			// If wifi disabled then enable it
			Toast.makeText(getApplicationContext(), "wifi is disabled..making it enabled",
					Toast.LENGTH_LONG).show();

			mainWifi.setWifiEnabled(true);
		} 
		else {Toast.makeText(getApplicationContext(), "wifi is enabled... thats good...",
				Toast.LENGTH_SHORT).show();}
	}

	
	public class WifiReceiver  extends BroadcastReceiver
	{
		// This method call when number of wifi connections changed
		long fisrtScanTS;
		private String FILENAME = pntNameStr;
		public FileOutputStream outputStream;

		@SuppressLint("NewApi")
		public void onReceive(Context c, Intent intent) 
		{
			time = new SimpleDateFormat("ddMMyyyyhhmmss");
			int ScanTime = numScans;
			if(ScanTime==1 | ScanTime==10)
			{

				for (int scanNum = 1; scanNum<ScanTime+1; scanNum++)
				{
					wifiList = mainWifi.getScanResults();
					for(int i = 0; i < wifiList.size(); i++)
					{            	
						sb.append(((wifiList.get(i)).BSSID + " " +  (wifiList.get(i)).level + " "  + (wifiList.get(i)).frequency + " "+ (wifiList.get(i)).timestamp + " "+ (wifiList.get(i)).SSID +" "+ time.format(new Date()) + "\n" ) );
						fisrtScanTS = (wifiList.get(1)).timestamp;
					} 

					do
					{
						mainWifi.startScan();
						wifiList = mainWifi.getScanResults();
						try{Thread.currentThread().sleep(200);}
						catch(InterruptedException ie){
							//If this thread was intrrupted by nother thread 
						}
					} while(fisrtScanTS == (wifiList.get(1)).timestamp);

					Toast.makeText(getApplicationContext(), "Scan number " + scanNum ,
							Toast.LENGTH_SHORT).show();
				}
			}
			else if (ScanTime!=0)
			{
				Calendar timeNow = Calendar.getInstance();
				Calendar timeEnd = Calendar.getInstance();
				timeEnd.add(Calendar.MINUTE, numScans);
				
				while(!(timeNow.after(timeEnd)))
				{
					wifiList = mainWifi.getScanResults();
					for(int i = 0; i < wifiList.size(); i++){            	
						sb.append(((wifiList.get(i)).BSSID + " " +  (wifiList.get(i)).level + " "  + (wifiList.get(i)).frequency + " "+ (wifiList.get(i)).timestamp + " "+ (wifiList.get(i)).SSID +" "+ time.format(new Date()) + "\n" ) );
						fisrtScanTS = (wifiList.get(1)).timestamp;
					}       
					do {
						mainWifi.startScan();
						wifiList = mainWifi.getScanResults();
						try{Thread.currentThread().sleep(200);}
						catch(InterruptedException ie){
							//If this thread was intrrupted by nother thread 
						}} while(fisrtScanTS == (wifiList.get(1)).timestamp);
					timeNow = Calendar.getInstance();
					Log.d("TimeNow",timeNow.getTime().toString());
					Log.d("TimeEnd",timeEnd.getTime().toString());
					Log.d("BOOLbitch",String.valueOf((timeNow.after(timeEnd))));
					
					Toast.makeText(getApplicationContext(), timeNow.getTime().toString() + " :: " + timeEnd.getTime().toString() ,
							Toast.LENGTH_SHORT).show();
				}

			}

			generateNoteOnSD(FILENAME, sb.toString());
			FILENAME="x";
			ScanTime=0;
			Toast.makeText(getApplicationContext(), "IM MOTHERFUCKING DONE" ,
					Toast.LENGTH_SHORT).show();
			//Log.d("Scan Results",sb.toString());
			/*Intent i=new Intent(nWiFiScans.this, MainActivity.class);
			i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(i);*/
			unregisterReceiver(receiverWifi);
			finish();
		}

	}	
	
	public void generateNoteOnSD(String sFileName, String sBody)
	{
		
		try
		{
			File root = new File(Environment.getExternalStoragePublicDirectory("SurveyAppData"), "ScanResults");
			if (!root.exists()) 
			{
				root.mkdirs();
			}
			File gpxfile = new File(root, sFileName);
			FileWriter writer = new FileWriter(gpxfile);
			writer.append(sBody);
			writer.flush();
			writer.close();
			Toast.makeText(this, "Saved" + "/n" +gpxfile.toURI(), Toast.LENGTH_LONG).show();
					 
		}
		catch(IOException e)
		{
			e.printStackTrace();
			

		}
	}  


}
