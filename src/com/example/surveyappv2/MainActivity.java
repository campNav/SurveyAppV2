package com.example.surveyappv2;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity 
{
	LinearLayout mainLayout,leftLayout,rightLayout;
	EditText pointName;
	EditText scanTime;
	Button scan10, scan1, timedScan;
	TextView msg;
	LayoutParams paramsL,paramsR;
	static final String EXTRA_pntNameStr = "com.example.surveyappv2.pntNameStr";
	static final String EXTRA_scanTimeMin = "com.example.surveyappv2.scanTimeMin";
	static final String EXTRA_numScans = "com.example.surveyappv2.numScans";

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
		mainLayout = new LinearLayout(this);
		mainLayout.setOrientation(LinearLayout.HORIZONTAL);
		
		paramsL = new LayoutParams(500,LayoutParams.MATCH_PARENT, 1);
		paramsR = new LayoutParams(500,LayoutParams.MATCH_PARENT, 1);
				
		leftLayout = new LinearLayout(this);
		leftLayout.setOrientation(LinearLayout.VERTICAL);
		leftLayout.setLayoutParams(paramsL);
		leftLayout.setGravity(Gravity.CENTER_HORIZONTAL);
		leftLayout.setBackgroundColor(Color.YELLOW);
		
		rightLayout = new LinearLayout(this);
		rightLayout.setOrientation(LinearLayout.VERTICAL);
		rightLayout.setLayoutParams(paramsR);
		rightLayout.setGravity(Gravity.CENTER_HORIZONTAL);
		rightLayout.setBackgroundColor(Color.BLUE);
		
		scanTime = new EditText(this);
		scanTime.setHint("Scan Duration (min)");
		scanTime.setInputType(InputType.TYPE_CLASS_NUMBER);		
		pointName = new EditText(this);
		pointName.setHint("Enter Point Name");
		scan10 = new Button(this);
		scan1 = new Button(this);
		timedScan = new Button(this);
		msg = new TextView(this);
		
		scan10.setText("Do 10 Scans");
		scan1.setText("Do a Single Scan");
		timedScan.setText("Timed Scan");
		msg.setText("Alec Sucks");
		
		
		leftLayout.addView(pointName);
		leftLayout.addView(scanTime);
		rightLayout.addView(msg);
		rightLayout.addView(scan10);
		rightLayout.addView(scan1);
		rightLayout.addView(timedScan);
		
		mainLayout.addView(leftLayout);
		mainLayout.addView(rightLayout);
		mainLayout.setBackgroundColor(Color.RED);
		setContentView(mainLayout);
		setButtonClickListener();

	}

	private void setButtonClickListener()
	{
		scan10.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v) 
			{
				String message2 = "Button: \n" + scan10.getText();
				msg.setText(message2);
				startScan(mainLayout,10);

			}
		});
		
		scan1.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v) 
			{
				String message2 = "Button: \n" + scan1.toString();
				msg.setText(message2);
				startScan(mainLayout,1);
			}
		});
		
		timedScan.setOnClickListener(new View.OnClickListener() 
		{
			public void onClick(View v) 
			{
				String message2 = "Button: \n" + timedScan.toString();
				msg.setText(message2);
				startScan(mainLayout,Integer.parseInt(scanTime.getText().toString()));
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void startScan(View view, int numScans)
	{
		Intent intent = new Intent(this, nWiFiScans.class);
		String pntNameStr = pointName.getText().toString();
		intent.putExtra(EXTRA_pntNameStr, pntNameStr);
		intent.putExtra(EXTRA_numScans, numScans);
		startActivity(intent);
	}
}