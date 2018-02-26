package com.example.easytravelling;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.mapsforge.android.maps.MapView;
import org.mapsforge.android.maps.overlay.ArrayItemizedOverlay;
import org.mapsforge.android.maps.overlay.ArrayWayOverlay;
import org.mapsforge.android.maps.overlay.OverlayItem;
import org.mapsforge.android.maps.overlay.OverlayWay;
import org.mapsforge.core.GeoPoint;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.widget.Toast;

public class Shapes {
	static boolean firstTime = true;
	Paint wayDefaultPaintFill;
	Paint wayWalkFill;
	private ArrayItemizedOverlay itemizedOverlay;
	Paint wayDefaultPaintOutline;
	private static ArrayWayOverlay wayOverlay;
	private Context context;
	private MapView mapView;
	//private static boolean firstRoute = false;
	public Shapes(Context contextgi)
	{
		context = contextgi;
	}
	/**
	    * Constructs with shape file name that your want to draw.
	    *
	    * @param path
	    *            the direct path of file in asset folder.
	    */
	
	public Shapes(Context context, MapView mapView, Boolean bus)
	{
        itemizedOverlay = new ArrayItemizedOverlay(context.getResources().getDrawable(android.R.drawable.btn_star_big_off));
		this.context = context;
		this.mapView = mapView;
		wayDefaultPaintFill = new Paint(Paint.ANTI_ALIAS_FLAG);
        wayDefaultPaintFill.setStyle(Paint.Style.STROKE);
        
        wayDefaultPaintFill.setAlpha(160);
        wayDefaultPaintFill.setStrokeWidth(7);
        wayDefaultPaintFill.setStrokeJoin(Paint.Join.ROUND);
        wayDefaultPaintFill.setPathEffect(new DashPathEffect(new float[] { 20, 20 }, 0));
        wayDefaultPaintOutline = new Paint(Paint.ANTI_ALIAS_FLAG);
        wayDefaultPaintOutline.setStyle(Paint.Style.STROKE);
        
        if (bus)
        {
        wayDefaultPaintFill.setColor(Color.BLUE);
        wayDefaultPaintOutline.setColor(Color.BLUE);
        }
        else
        {
        	wayDefaultPaintFill.setColor(Color.RED);
            wayDefaultPaintOutline.setColor(Color.RED);
        }
        wayDefaultPaintOutline.setAlpha(128);
        wayDefaultPaintOutline.setStrokeWidth(7);
        wayDefaultPaintOutline.setStrokeJoin(Paint.Join.ROUND);
        wayOverlay = new ArrayWayOverlay(wayDefaultPaintFill, wayDefaultPaintOutline);
        
        if (firstTime)
        {
        mapView.getOverlays().add(wayOverlay);
        firstTime = false;
        }
	}
	/**
	    * method which read CSV file and return in string array list.
	    *
	    * @param Context
	    *            pass application context here.
	    */
	public final List<String[]> readCsv(String csv_Path) {
		  List<String[]> questionList = new ArrayList<String[]>();
		  AssetManager assetManager = context.getAssets();

		  try {
		    InputStream csvStream = assetManager.open(csv_Path);
		    InputStreamReader csvStreamReader = new InputStreamReader(csvStream);
		    CSVReader csvReader = new CSVReader(csvStreamReader);
		    String[] line;

		    // throw away the header
		    csvReader.readNext();

		    while ((line = csvReader.readNext()) != null) {
		      questionList.add(line);
		    }
		  } catch (IOException e) {
		    e.printStackTrace();
		  }
	
		  return questionList;
		}
	/**
	    * This method automatically read the file by using above method and draw it on map
	    *
	    * @param mapView
	    *            the mapView on which you want to draw path.
	    * @param context 
	    * 				Current application context.
	    */
	public void drawRoute(List<Vertex> points)
	{
		
		mapView.setCenter(new GeoPoint(points.get(0).lat, points.get(0).lon));
		for (int i=0; i < points.size(); i++)
		{
			while (i < points.size()-1)
			{
				Vertex rows = points.get(i);
				Vertex rowsNext = points.get(i+1);
				OverlayItem marker = new OverlayItem(new GeoPoint(rows.lat, rows.lon), "MOH","hjj");
				itemizedOverlay.addItem(marker);
				
				wayOverlay.addWay( new OverlayWay(new GeoPoint[][] 
						{ 
						{
							new GeoPoint((rows.lat),(rows.lon)), new GeoPoint((rowsNext.lat),(rowsNext.lon)) 
						}
						})
		     );
				i++;	        
			}
		//	if (firstRoute == false)
			{
			
			//	wayOverlay.requestRedraw();
			//	mapView.getOverlays().remove(wayOverlay);
			//	mapView.getOverlays().remove(itemizedOverlay);
			//	mapView.getOverlays().clear();
			//	 firstRoute = true;
			}
		}
		 mapView.getOverlays().add(wayOverlay);
		mapView.getOverlays().add(itemizedOverlay);
		//	wayOverlay.requestRedraw();
			Toast.makeText(context, ""+mapView.getOverlays().size(), Toast.LENGTH_SHORT).show();
		
	}
	public void showShapesOnMap(String path)
	{
		int i = 0;
		List<String[]> list = this.readCsv(path);
		
	//	
		// Toast.makeText(context, ""+rows[0], Toast.LENGTH_LONG).show();
		while (i < list.size()-1)
		{
			String[] rows = list.get(i);
			String[] rowsNext = list.get(i+1);
			
			wayOverlay.addWay( new OverlayWay(new GeoPoint[][] 
					{ 
					{
						new GeoPoint(Double.parseDouble(rows[1]),Double.parseDouble(rows[2])), new GeoPoint(Double.parseDouble(rowsNext[1]),Double.parseDouble(rowsNext[2])) 
					}
					})
	     );
			i++;	        
		}
        // create the WayOverlay and add the ways
       
        
        mapView.getOverlays().add(wayOverlay);
      //  mapView.getOverlays().remove(wayOverlay);
	}
}
