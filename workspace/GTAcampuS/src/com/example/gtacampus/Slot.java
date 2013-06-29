package com.example.gtacampus;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

public class Slot extends Activity {
	
	final int COURSE_DET=0;
	String coursedet,coursetitle;
	
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
	//	setContentView(R.layout.slots);
	LinearLayout.LayoutParams dims = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
	LinearLayout.LayoutParams dim = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT,(float)1.0);
	LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT,(float)1.0);
	LayoutParams param = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
	
	HorizontalScrollView timetable = new HorizontalScrollView(this);
	timetable.setHorizontalScrollBarEnabled(true);
	timetable.setLayoutParams(dims);
		
	LinearLayout timetablemain = new LinearLayout(this);
	timetablemain.setLayoutParams(dims);
	timetablemain.setOrientation(LinearLayout.VERTICAL);
	
	TextView mainheading = new TextView(this);
	mainheading.setText("TIMETABLE");
	mainheading.setTextAppearance(this, R.style.mytext);
	mainheading.setTextSize(30);
	mainheading.setGravity(Gravity.CENTER);
	mainheading.setTextColor(Color.RED);
	timetablemain.addView(mainheading);
	
	View dividr = new View(this);
	LayoutParams divd = new LayoutParams(LayoutParams.MATCH_PARENT,1);
	dividr.setLayoutParams(divd);
	dividr.setBackgroundResource(android.R.drawable.divider_horizontal_dark);
	timetablemain.addView(dividr);
	
	LinearLayout subtimetable = new LinearLayout(this);
	subtimetable.setLayoutParams(dims);
	subtimetable.setOrientation(LinearLayout.HORIZONTAL);
	
	String week[] = {"DAY","Monday","Tuesday","Wednesday","Thursday","Friday"};
	LinearLayout days = new LinearLayout(this);
	days.setLayoutParams(param);
	days.setOrientation(LinearLayout.VERTICAL);
	
	for(int i=0;i<6;i++)
	{
		TextView c_days = new TextView(this);
		c_days.setWidth(100);
		c_days.setTextAppearance(getBaseContext(), R.style.mytext);
		c_days.setTextColor(Color.GREEN);
		c_days.setLayoutParams(dim);
		c_days.setText(week[i]);
		days.addView(c_days);
	}
	
	subtimetable.addView(days);
	
	LinearLayout tabletime  = new LinearLayout(this);
	tabletime.setLayoutParams(params);
	tabletime.setOrientation(LinearLayout.HORIZONTAL);
	
		DataManipulator db = new DataManipulator(this);
		String times[] = db.gettimings();
		


		for(int i=0;i<times.length;i++)
		{
			View divider = new View(this);
			LayoutParams div = new LayoutParams(1,LayoutParams.WRAP_CONTENT);
			divider.setLayoutParams(div);
			divider.setBackgroundResource(android.R.drawable.divider_horizontal_dark);
			
			TextView headings = new TextView(this);
			headings.setWidth(143);
			headings.setGravity(Gravity.CENTER);
			headings.setTextAppearance(getBaseContext(), R.style.mytext);
			headings.setTextColor(Color.GREEN);
			headings.setText(times[i]);
			tabletime.addView(divider);
			tabletime.addView(headings);
		}
		
		
		LinearLayout courses = new LinearLayout(this);
		courses.setLayoutParams(dims);
		courses.setOrientation(LinearLayout.VERTICAL);
		
		courses.addView(tabletime);
		Cursor slotstat = db.slotstat();
		slotstat.moveToFirst();
		int count = slotstat.getColumnCount();
		for(int i=0;i<5;i++)
		{
		LinearLayout slots = new LinearLayout(this);
		slots.setLayoutParams(dim);
		slots.setOrientation(LinearLayout.HORIZONTAL);
		for(int j=1;j<count;j++)
		{
			View divider = new View(this);
			LayoutParams div = new LayoutParams(1,LayoutParams.WRAP_CONTENT);
			divider.setLayoutParams(div);
			divider.setBackgroundResource(android.R.drawable.divider_horizontal_dark);
			
			TextView c_slot = new TextView(this);
			c_slot.setOnLongClickListener(clicked);
			c_slot.setWidth(143);
			c_slot.setGravity(Gravity.CENTER);
			c_slot.setTextAppearance(getBaseContext(), R.style.mytext);
			c_slot.setTextColor(Color.WHITE);
			c_slot.setText(slotstat.getString(j));
			c_slot.setTag(slotstat.getString(j));
			slots.addView(divider);
			slots.addView(c_slot);
		}
		slotstat.moveToNext();
		courses.addView(slots);
		}
		slotstat.close();
		timetable.addView(courses);
		subtimetable.addView(timetable);
		timetablemain.addView(subtimetable);
		setContentView(timetablemain);
		db.close();
	}
	
	View.OnLongClickListener clicked = new View.OnLongClickListener() {

		@Override
		public boolean onLongClick(View v) {
			// TODO Auto-generated method stub
			DataManipulator db = new DataManipulator(Slot.this);
			Cursor c_det = db.coursedetails(v.getTag().toString());
			if(c_det.moveToFirst()){
				coursetitle = c_det.getString(1);
				coursedet = "\nCOURSE CODE\t\t:\t" + c_det.getString(2) + "\n";
				coursedet+= "\nTEACHER\t\t\t\t:\t" + c_det.getString(0) + "\n";
				coursedet+= "\nBUNKS\t\t\t\t\t:\t" + c_det.getInt(3) + "\n";
				showDialog(COURSE_DET);
			}
			c_det.close();
			db.close();
			return false;	}	};
	
	protected final Dialog onCreateDialog(final int id){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		switch(id){
		case COURSE_DET : builder.setMessage(coursedet)
		.setTitle(coursetitle)
		.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dismissDialog(COURSE_DET);
			}
		});
		}		return builder.create();	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.campus, menu);
		return true;
	}
}