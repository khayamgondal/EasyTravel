package com.example.easytravelling;

import java.util.ArrayList;
import java.util.List;

import org.mapsforge.android.maps.MapView;
import org.mapsforge.core.GeoPoint;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class TextInfo {
	static TextView startFrom;
	static TextView endAt ;
	static TextView busChange ;
	static boolean firstTime = true;
	static TextView routeText;
	private static TextView startFrom2;
	private static TextView endAt2;
	private static TextView busChange2;
	private static TextView stopat;
	
public static void textInfo (Context context, final List<Vertex> list, LinearLayout layout, final MapView mapView)
{
	
	List<String> lists = new ArrayList<String>() ;
	for (int i=0; i < list.size(); i++) 
	{
		lists.add(list.get(i).type);
	}
	//if (CalcPath.direct)
	{
	//new Shapes(context, mapView, true).drawRoute(list);
	 ////////////////// providing damn navigations
	
	 String tempType;
	 String[] parts1;
	 String[] parts2;
	 String delims = "+";
	 final List<Vertex> change = new ArrayList<Vertex>();
	 for (int i=0; i <list.size()-1; i++)
	 {
		 parts1 = list.get(i).type.split("&") ;
		 parts2 = list.get(i+1).type.split("&") ;
		 if (parts1[0].compareTo(parts2[0]) !=0 )
		 {
			 if (list.get(i).type.contains("&"))
			 change.add(list.get(i));
			 else
				 change.add(list.get(i+1));
			 
		 }
	 }
	 
	  startFrom = new TextView(context);
		 endAt = new TextView(context);
		 busChange = new TextView(context);
		 
		 int boxCount = 0; 
		 List<String[]> routeData =  new Shapes(context).readCsv("routes.csv");
	
	layout.addView(startFrom); boxCount ++;
	String startRoute = null;
	String midRoute = null;
	String endRoute = null ;
	
	
	 startFrom.setText("Start from "+list.get(0).name+" On "+startRoute);
		endAt.setText("End Trip at "+list.get(list.size()-1).name);
	//Toast.makeText(context, startRoute+endRoute, Toast.LENGTH_LONG).show();
	if (change.size() > 0)
	 {
		for (int i = 0; i < routeData.size(); i++)
		{
			//Toast.makeText(context, routeData.get(i)[0], Toast.LENGTH_SHORT).show();	
			if (routeData.get(i)[0] == change.get(0).type)
			{
			midRoute = routeData.get(i)[1];
			break;
			}
		}
		 busChange.setText("Change Bus at"+change.get(0).name+" On "+midRoute);
			layout.addView(busChange); boxCount ++;

		 busChange.setOnClickListener(new OnClickListener() {
			    @Override
			    public void onClick(View v) {
			    	mapView.setCenter(new GeoPoint(change.get(0).lat, change.get(0).lon));
			    }
			});
	 }
	layout.addView(endAt); 

 //Toast.makeText(context, ""+change, Toast.LENGTH_LONG).show();
	
	startFrom.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
	    	mapView.setCenter(new GeoPoint(list.get(0).lat, list.get(0).lon));
	    }
	});
	endAt.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
	    	mapView.setCenter(new GeoPoint(list.get(list.size()-1).lat, list.get(list.size()-1).lon));
	    }
	    
	});
	}
	/*
	ToggleButton tb = new ToggleButton(context);
    tb.setTextOn("Hide");
    tb.setTextOff("Show");
    tb.setChecked(true);
    tb.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
    layout.addView(tb);*/
}

public static void textInfoMulti(Context context,
		PathHelper pathHelper, LinearLayout layout, final MapView mapView) {
	
	List<String> list1 = new ArrayList<String>() ;
	List<String> list2 = new ArrayList<String>() ;
	final List<Vertex> path1 = pathHelper.first;
	final List<Vertex> path2 = pathHelper.last;
	for (int i=0; i < path1.size(); i++) 
	{
		list1.add(path1.get(i).type);
	}
	for (int i=0; i < path2.size(); i++) 
	{
		list2.add(path2.get(i).type);
	}
	
	 new Shapes(context, mapView, true).drawRoute(path1);
	 new Shapes(context, mapView, true).drawRoute(path2);
	 List<Vertex> walk = new ArrayList<Vertex>();
	 walk.add(pathHelper.mStart);
	 walk.add(pathHelper.mEnd);
	new Shapes(context, mapView, false).drawRoute(walk);
	
		
	 startFrom = new TextView(context);
	 endAt = new TextView(context);
	 busChange = new TextView(context);
	 startFrom2 = new TextView(context);
	 busChange2 = new TextView(context);
	 endAt2  = new TextView(context);
	 stopat = new TextView(context);
	 
	 String tempType;
	 String[] parts1;
	 String[] parts2;
	 String delims = "+";
	 final List<Vertex> change = new ArrayList<Vertex>();
	 for (int i=0; i <path1.size()-1; i++)
	 {
		 parts1 = path1.get(i).type.split("&") ;
		 parts2 = path1.get(i+1).type.split("&") ;
		 if (parts1[0].compareTo(parts2[0]) !=0 )
		 {
			 
			 if (path1.get(i).type.contains("&"))
			 change.add(path1.get(i));
			 else
				 change.add(path1.get(i+1));
			 
		 }
	 }
	 List<String[]> routeData =  new Shapes(context).readCsv("routes.csv");

	 String startRoute = null;
	for (int i = 0; i < routeData.size(); i++)
		{
			//Toast.makeText(context, routeData.get(i)[0], Toast.LENGTH_SHORT).show();	
			if (path1.get(0).type.contains(routeData.get(i)[0]));
			{
		//		Toast.makeText(context, routeData.get(i)[0]+path1.get(0).type, Toast.LENGTH_LONG).show();
			startRoute = routeData.get(i)[1];
			break;
			}
		}
		
		 startFrom.setText("Start from "+path1.get(0)+" On "+startRoute);
startFrom.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			mapView.setCenter(new GeoPoint(path1.get(0).lat, path1.get(0).lon));
			
		}
	});
	layout.addView(startFrom);
	if (change.size() > 0)
	 {
		if (change.get(0).name != path1.get(0).name)
		{
		 busChange.setText("Change Bus at "+change.get(0).name);
		 busChange.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					mapView.setCenter(new GeoPoint(change.get(0).lat, change.get(0).lon));
					
				}
			});
		 layout.addView(busChange);
		}
	 }
	endAt.setText("Stop at "+path1.get(path1.size()-1)+" and walk from "+path1.get(path1.size()-1)+" to "+path2.get(0));
	layout.addView(endAt);
endAt.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			mapView.setCenter(new GeoPoint(path1.get(path1.size()-1).lat, path1.get(path1.size()-1).lon));
			
		}
	});
	startFrom2.setText("Again Start from "+path2.get(0).name);
	layout.addView(startFrom2);
startFrom2.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			mapView.setCenter(new GeoPoint(path2.get(0).lat, path2.get(0).lon));
			
		}
	});
	 List<Vertex> change2 = new ArrayList<Vertex>();
	for (int i=0; i <path2.size()-1; i++)
	 {
		 parts1 = path2.get(i).type.split("&") ;
		 parts2 = path2.get(i+1).type.split("&") ;
		 if (parts1[0].compareTo(parts2[0]) !=0 )
		 {
			 
			 if (path2.get(i).type.contains("&"))
			 change2.add(path2.get(i));
			 else
				 change2.add(path2.get(i+1));
			 
		 }
	 }
	 if (change2.size() > 0)
	 {
		 busChange2.setText("Change Bus at "+change2.get(0).name);
		 layout.addView(busChange2);
		 busChange2.setOnClickListener(new OnClickListener() {
			    @Override
			    public void onClick(View v) {
			    	mapView.setCenter(new GeoPoint(change.get(0).lat, change.get(0).lon));
			    }
			});
	 }
	 if (path2.get(path2.size()-1).name != path2.get(0).name)
	 {
	 endAt2.setText("Stop at "+path2.get(path2.size()-1));
		layout.addView(endAt2);
	//	layout.removeView(startFrom2);
		endAt2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				mapView.setCenter(new GeoPoint(path2.get(path2.size()-1).lat, path2.get(path2.size()-1).lon));
				
			}
		});
	 }
	/* ToggleButton tb = new ToggleButton(context);
	    tb.setTextOn("Hide");
	    tb.setTextOff("Show");
	    tb.setChecked(true);
	    tb.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
	    layout.addView(tb);*/
}
}
