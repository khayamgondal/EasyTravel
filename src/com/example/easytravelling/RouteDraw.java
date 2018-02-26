package com.example.easytravelling;

import java.util.List;

import org.mapsforge.android.maps.MapView;
import org.mapsforge.android.maps.overlay.ArrayItemizedOverlay;
import org.mapsforge.android.maps.overlay.ArrayWayOverlay;
import org.mapsforge.android.maps.overlay.OverlayItem;
import org.mapsforge.android.maps.overlay.OverlayWay;
import org.mapsforge.core.GeoPoint;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class RouteDraw {
public static void drawRoute(Context context,List<Vertex> points, MapView mapView, ArrayWayOverlay overlay ) {
	
	mapView.setCenter(new GeoPoint(points.get(0).lat, points.get(0).lon));
	for (int i=0; i < points.size(); i++)
	{
		while (i < points.size()-1)
		{
			Vertex rows = points.get(i);
			Vertex rowsNext = points.get(i+1);
			
			
			overlay.addWay( new OverlayWay(new GeoPoint[][] 
					{ 
					{
						new GeoPoint((rows.lat),(rows.lon)), new GeoPoint((rowsNext.lat),(rowsNext.lon)) 
					}
					})
	     );
			i++;	        
		}
	}
	overlay.requestRedraw();
}
public static void drawWalkMarker(Context context, List<Vertex> points, MapView mapView, ArrayItemizedOverlay overlay) {
	for (int i =0; i < points.size(); i ++) {
		OverlayItem marker = new OverlayItem(new GeoPoint(points.get(i).lat, points.get(i).lon),
				points.get(i).name, "hjj");
		overlay.addItem(marker);
		overlay.requestRedraw();
	}
}
public static String routeName(String routeType)
{
	String output = null;
	if (routeType == "R1")
		output = "Raja Bazaar to Darbaar Bari";
	else if (routeType == "R2")
		output = "Hajh Complex to Bari Imam";
	else if (routeType == "R3")
		output = "Pirwadhi to Faisal Masjid";
	else if (routeType == "R4")
		output = "Pak Sectrate to Tarnol";
	else if (routeType == "R5")
		output = "Pak Sectrate to Tarnol";
	else if (routeType == "R6")
		output = "G-11 to Pak Sectrate";
	else if (routeType == "R7")
		output = "Pirwadhi to Chattar";
	else if (routeType == "R8")
		output = "Rawat to F-8 Markaz";
		
	return output;
}
public static void showRouteInfo(Context context ,Vertex start, 
	Vertex end,List<Vertex> stopList, LinearLayout layout, TextView detailer) {
	TextView startBox = new TextView(context);
	startBox.setTextColor(Color.GREEN);
	startBox.setTextSize(20);
	TextView endBox = new TextView(context);
	endBox.setTextColor(Color.RED);
	endBox.setTextSize(20);
	detailer.setText(Html.fromHtml("&nbsp Total Distance "+CalcPath.minDis+" km<br> &nbsp  "
	+stopList.size()+" bus changes"));
	startBox.setText("Walk to "+start.name);
	layout.addView(startBox);
	TextView getOn = new TextView(context);
	getOn.setTextSize(18);
	TextView getOff = new TextView(context);
	getOff.setTextSize(18);
	TextView getOn1 = new TextView(context);
	getOn1.setTextSize(18);
	TextView getOff1 = new TextView(context);
	getOff1.setTextSize(18);
	TextView getOff2 = new TextView(context);
	getOff2.setTextSize(18);
	//Toast.makeText(context, ""+stopList.size(), Toast.LENGTH_SHORT).show();
	if (stopList.size() == 0)
	{
		getOn.setText("Start from "+start.name+" to "+end.name);
		layout.addView(getOn);
		
	}
	else if (stopList.size() == 1)
	{
		getOn.setText("Start form "+ start.name+" to "+ stopList.get(0).name);
		layout.addView(getOn);
		getOff.setText("Again Start from "+ stopList.get(0)+" and end at "+end.name);
		layout.addView(getOff);
	}
	else if (stopList.size() == 2)
	{
		getOn.setText("Start form "+ start.name+" to "+ stopList.get(0).name);
		layout.addView(getOn);
		getOn1.setText("again start form "+ stopList.get(0).name+" to "+ stopList.get(1).name);
		layout.addView(getOn1);
		getOff1.setText("again start form "+ stopList.get(1).name+" to "+ end.name);
		layout.addView(getOff1);
	}
	else if (stopList.size() == 3)
	{
		getOn.setText("Start form "+ start.name+" to "+ stopList.get(0).name);
		layout.addView(getOn);
		getOn1.setText("again start form "+ stopList.get(0).name+" to "+ stopList.get(1).name);
		layout.addView(getOn1);
		getOff1.setText("again start form "+ stopList.get(1).name+" to "+ stopList.get(2).name);
		layout.addView(getOff1);
		
		getOff2.setText("again start form "+ stopList.get(2).name+" to "+ end.name);
		layout.addView(getOff2);
	}
	endBox.setText("Walk from "+ end.name+" to your distination");
	layout.addView(endBox);
}
}
