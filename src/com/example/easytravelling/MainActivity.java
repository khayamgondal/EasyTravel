package com.example.easytravelling;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Display;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.OnGestureListener;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


import org.mapsforge.android.maps.MapView;
import org.mapsforge.android.maps.MapActivity;
import org.mapsforge.android.maps.overlay.ArrayItemizedOverlay;
import org.mapsforge.android.maps.overlay.ArrayWayOverlay;
import org.mapsforge.android.maps.overlay.OverlayItem;
import org.mapsforge.core.GeoPoint;

import com.graphhopper.GHRequest;
import com.graphhopper.GHResponse;
import com.graphhopper.GraphHopper;
import com.graphhopper.GraphHopperAPI;
import com.graphhopper.util.Constants;
import com.graphhopper.util.PointList;
import com.graphhopper.util.StopWatch;


public class MainActivity extends MapActivity   {
	//public static ArrayWayOverlay wayOverlay;
	protected CharSequence[] _options = { "Search", "From & TO", "Routing Info", "Buttons", "Map" };
	protected boolean[] _selections =  new boolean[ _options.length ];
	private RibbonMenuView rbmView;
	private String mapsFolder;
	private String currentArea = "pakistan"; 
	private GraphHopperAPI hopper;
	double coords1[] = {0,0};
	double coords2[] = {0,0};
	static boolean firstTime = true;
	LinearLayout layout;
	LinearLayout routeLayout;
	ScrollView routeScrollView;
	LinearLayout buttonLayout;
	AutoCompleteTextView textView;
	AutoCompleteTextView from;
	AutoCompleteTextView to;
	Button hideShow;
	public MapView mapView;
	private LocationManager locationManager;
	
	TextView datails;
	TextView navigation;
	private double currentLat;
	private double currentLon;
	private float currentSpeed;
	private float currentAccuracy;
	private double currenetAltitude;
	
	public String fromLoc;
	public String toLoc;
	ImageButton tranRouteButtton ;
	InputMethodManager imm;
	TextView showInfo;
	
	private ArrayItemizedOverlay itemizedOverlay;
	

	ArrayWayOverlay walkWay;
	ArrayWayOverlay busWay;
	ArrayItemizedOverlay startMarker;
	ArrayItemizedOverlay endMarker;
	ArrayItemizedOverlay walkMarkers;
	ArrayItemizedOverlay busMarkers;
	
	 boolean checkFlag = true;
	 GeoPoint tmpPoint;
	 
	private SimpleOnGestureListener gestureListener = new SimpleOnGestureListener() {
		
        // why does this fail? public boolean onDoubleTap(MotionEvent e) {};
		private static final int SWIPE_MIN_DISTANCE = 100;
	    private static final int SWIPE_THRESHOLD_VELOCITY = 100;
	   
		@Override
	    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
	            float velocityY) {
	        /* on scroll to the next page */
	        if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
	                && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY
	                ) {
	            //Ur code goes here
	        }
	        /* on scroll to the previous page  */
	        else if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
	                && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY
	                ) {
	            //ur code goes here.
	        }
	        Toast.makeText(getApplicationContext(), "fling", Toast.LENGTH_SHORT).show();
	        return false;
	    }
		
public void onLongPress(MotionEvent motionEvent) {
    float x = motionEvent.getX();
float y = motionEvent.getY();
org.mapsforge.android.maps.Projection p = mapView.getProjection();
 tmpPoint = p.fromPixels((int) x, (int) y);

	openContextMenu (mapView);
	
}
       
    };
    
    private GestureDetector gestureDetector = new GestureDetector(gestureListener);

    void initWays()

    {
    	Paint wayDefaultPaintOutline1 = new Paint(Paint.ANTI_ALIAS_FLAG);
        wayDefaultPaintOutline1.setStyle(Paint.Style.STROKE);
        wayDefaultPaintOutline1.setColor(Color.BLUE);
        wayDefaultPaintOutline1.setStrokeWidth(7);
        
        Paint wayDefaultPaintOutline2 = new Paint(Paint.ANTI_ALIAS_FLAG);
        wayDefaultPaintOutline2.setStyle(Paint.Style.STROKE);
        wayDefaultPaintOutline2.setColor(Color.RED);
        wayDefaultPaintOutline2.setStrokeWidth(7);
        
    	walkWay = new ArrayWayOverlay(null, wayDefaultPaintOutline2);
    	busWay = new ArrayWayOverlay(null, wayDefaultPaintOutline1);
    	mapView.getOverlays().add(busWay);
    	mapView.getOverlays().add(walkWay);
    }
    void initMarkers()
    {
    	walkMarkers = new ArrayItemizedOverlay(getResources().getDrawable(R.drawable.walk));
    	busMarkers = new ArrayItemizedOverlay(getResources().getDrawable(R.drawable.busmarker));
    	startMarker =  new ArrayItemizedOverlay(getResources().getDrawable(R.drawable.flag_green));
    	endMarker = new ArrayItemizedOverlay(getResources().getDrawable(R.drawable.flag_red));
    	mapView.getOverlays().add(walkMarkers);
    	mapView.getOverlays().add(busMarkers);
    	mapView.getOverlays().add(startMarker);
    	mapView.getOverlays().add(endMarker);
    }
	private ImageButton carRouteButton;
	
    @Override  
    public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) {  
 super.onCreateContextMenu(menu, v, menuInfo);  
     menu.setHeaderTitle("Get Directions");  
     menu.add(0, v.getId(), 0, "From Current Location");
     menu.add(0, v.getId(), 0, "From This Point");  
     menu.add(0, v.getId(), 0, "To This Point"); 
     menu.add(0,v.getId(), 0, "Clear Map");
     menu.add(0, v.getId(), 0, "Toggle Windows");
     
 }  
    @Override  
    public boolean onContextItemSelected(MenuItem item) {  
    	
    	if (item.getTitle() == "From Current Location") {
    		if (currentLat !=0 && currentLon !=0) {
    		startMarker.clear();
    		startMarker.addItem(new OverlayItem(new GeoPoint(currentLat, currentLon), "Start", "snip"));
    		startMarker.requestRedraw();
            coords1[1] = currentLat;
            coords1[0] = currentLon;
            from.setText(coords1[1]+","+coords1[0]);
           
            checkFlag = false;
            from.setVisibility(View.VISIBLE);
    		to.setVisibility(View.VISIBLE);
    		buttonLayout.setVisibility(View.VISIBLE);  
    	//	routeLayout.setVisibility(View.VISIBLE);
    //		routeScrollView.setVisibility(View.VISIBLE);
   // 		routeScrollView.setVisibility(View.VISIBLE);
    		from.setBackgroundColor(getResources().getColor(android.R.color.secondary_text_dark));
    		to.setBackgroundColor(getResources().getColor(android.R.color.transparent));
    	}
    		else
        		Toast.makeText(getApplicationContext(), "Current Location not Avaiable", Toast.LENGTH_SHORT).show();
        	
    	}
    	
    else if(item.getTitle()=="From This Point"){
		    	startMarker.clear();
				startMarker.addItem(new OverlayItem(tmpPoint, "Start", "snip"));
				startMarker.requestRedraw();
		        coords1[1] = tmpPoint.getLatitude();
                coords1[0] = tmpPoint.getLongitude();
                from.setText(coords1[1]+","+coords1[0]);
             
                checkFlag = false;
                from.setVisibility(View.VISIBLE);
	    		to.setVisibility(View.VISIBLE);
	    		buttonLayout.setVisibility(View.VISIBLE);  
	    		//routeLayout.setVisibility(View.VISIBLE);
	    	//	routeScrollView.setVisibility(View.VISIBLE);
	    //		routeScrollView.setVisibility(View.VISIBLE);
	    		from.setBackgroundColor(getResources().getColor(android.R.color.secondary_text_dark));
	    		to.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            }  
        else if(item.getTitle()=="To This Point"){
        
        	endMarker.clear();
        	endMarker.addItem(new OverlayItem(tmpPoint, "END", "ss"));
        	coords2[1] = tmpPoint.getLatitude();
        	coords2[0] = tmpPoint.getLongitude();
        	
        	to.setText(coords2[1]+","+coords2[0]);
        	to.setBackgroundColor(getResources().getColor(android.R.color.secondary_text_dark));
    		from.setBackgroundColor(getResources().getColor(android.R.color.transparent));
    		from.setVisibility(View.VISIBLE);
    		to.setVisibility(View.VISIBLE);
    		buttonLayout.setVisibility(View.VISIBLE);  
    	//	routeLayout.setVisibility(View.VISIBLE);
    	//	routeScrollView.setVisibility(View.VISIBLE);
    		checkFlag = true;
        	}  
        else if (item.getTitle() == "Clear Map") {
        	walkWay.clear();
        	busWay.clear();
        	walkMarkers.clear();
        	busMarkers.clear();
        	startMarker.clear();
        	endMarker.clear();
        	        }
        else if (item.getTitle() == "Toggle Windows") {
        
        	if (buttonLayout.getVisibility() == View.GONE) {
        		from.setVisibility(View.VISIBLE);
        		to.setVisibility(View.VISIBLE);
        		buttonLayout.setVisibility(View.VISIBLE);
        	//	routeLayout.setVisibility(View.VISIBLE);
        		routeScrollView.setVisibility(View.VISIBLE);
        		
        	}
        	else {
        		from.setVisibility(View.GONE);
        		to.setVisibility(View.GONE);
        		buttonLayout.setVisibility(View.GONE);
        	//	routeLayout.setVisibility(View.GONE);
        		routeScrollView.setVisibility(View.GONE);
        		textView.setVisibility(View.GONE);

        	}
        	
        }
        else {return false;}  
    return true;  
    }
      @Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       /* Intent intent = new Intent(this, SplashActivity.class);

        startActivity(intent);      
*/ 
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        
        currentLat = 0;
        currentLon = 0;
        fromLoc = null;
        toLoc = null;
        locationManager  = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new MyLocationListener();
        locationManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 10000, 10, locationListener);
        itemizedOverlay = new ArrayItemizedOverlay(getResources().getDrawable(android.R.drawable.ic_menu_mylocation));
        setContentView(R.layout.activity_main);
      
        navigation = (TextView) findViewById(R.id.navigationP);
        
        mapView = new MapView(this) {
            @Override
            public boolean onTouchEvent(MotionEvent event) {
                if (gestureDetector.onTouchEvent(event)) {
                    return true;
                }
                return super.onTouchEvent(event);
            }
        };
        mapView.zoom((byte) 12, 5);
      //  mapView.setMapViewLimits(33.756315, 33.615191, 72.940292, 73.175812);
        mapView.setClickable(true);
        mapView.setCenter(new GeoPoint(33.698351,73.063889));
        mapView.setBuiltInZoomControls(true);
        mapView.setMapFile(new File(Environment.getExternalStorageDirectory()+"/pakistan.map"));
        Display display = getWindowManager().getDefaultDisplay();
        
        int width = display.getWidth();
        int height = display.getHeight();
        if (width >=720 && height >=1280) {
        mapView.setLayoutParams(new ViewGroup.LayoutParams(
        		  720,
        		  1110));
        }
        //////////////////////////////
        
       
       
        
        ////////////////////////////////
        textView = (AutoCompleteTextView) findViewById(R.id.autocompleteCountry);
        
  
        routeLayout = (LinearLayout) findViewById(R.id.showInfoLayout);
        routeScrollView = (ScrollView) findViewById(R.id.scrollID);
        final GestureDetector gestureDetector2;
        View.OnTouchListener gestureListener2;
        gestureDetector2 = new GestureDetector(new GestureDetector.SimpleOnGestureListener() {
        	 private boolean trr = true;

			@Override
             public boolean onDoubleTap(MotionEvent e) {
        		 if (trr ) {
        		 routeScrollView.setLayoutParams(new LinearLayout.LayoutParams(
        				 LinearLayout.LayoutParams.MATCH_PARENT,
        				 LinearLayout.LayoutParams.WRAP_CONTENT));
        		 from.setVisibility(View.GONE);
        		 to.setVisibility(View.GONE);
        		 buttonLayout.setVisibility(View.GONE);
        		 trr = false;
        		 }
        		 else {
        			 routeScrollView.setLayoutParams(new LinearLayout.LayoutParams(
            				 LinearLayout.LayoutParams.MATCH_PARENT,100));
        			 trr = true;
        		 }
                 return true;
             }
			@Override
			public boolean onSingleTapUp(MotionEvent e) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public void onShowPress(MotionEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
					float distanceY) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public void onLongPress(MotionEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
					float velocityY) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean onDown(MotionEvent e) {
				// TODO Auto-generated method stub
				return false;
			}
		});
        
        gestureListener2 = new View.OnTouchListener() {
        public boolean onTouch(View v, MotionEvent event) {
        if (gestureDetector2.onTouchEvent(event)) {
        return true;
        }
        return false;
        }
        };
        routeScrollView.setOnTouchListener(gestureListener2);

        datails = (TextView) findViewById(R.id.detailer);
		textView.setVisibility(View.GONE);
        layout = (LinearLayout) findViewById(R.id.layout);
        layout.addView(mapView);
        final DatabaseConnector db = new DatabaseConnector(getApplicationContext());
        String[] all = db.allList();
        ArrayAdapter<String>adapter= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, all);
        
       Toast.makeText(getApplicationContext(), ""+adapter.getCount(), Toast.LENGTH_SHORT).show();

        textView.setAdapter(adapter);        
        from = (AutoCompleteTextView)findViewById(R.id.locationAutoCompleteFrom);
        to = (AutoCompleteTextView)findViewById(R.id.locationAutoCompleteTO);
        buttonLayout = (LinearLayout) findViewById(R.id.buttonLayout);
        new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, CalcPath.listStop(getApplicationContext()));
       from.setAdapter(adapter);
       to.setAdapter(adapter);
       
        mapView.getOverlays().add(itemizedOverlay);
        carRouteButton = (ImageButton) findViewById(R.id.carRoute);
        carRouteButton.setEnabled(false);
        tranRouteButtton = (ImageButton) findViewById(R.id.buttonRoute);
        carRouteButton.setOnClickListener(new Button.OnClickListener() {
        	     public void onClick(View v) {
        	    	 Location aLoc = new Location("reverseGeocoded");
                 	Location bLoc = new Location("reverseGeocoded");
                 	aLoc.setLatitude(coords1[1]);
                 	aLoc.setLongitude(coords1[0]);
                 	bLoc.setLatitude(coords2[1]);
                 	bLoc.setLongitude(coords2[0]);
                 	float dist = bLoc.distanceTo(aLoc);
                 	if (dist < 800) 
                     	Toast.makeText(getApplicationContext(), "From and To are too Close", Toast.LENGTH_SHORT).show();

                 	else{
        	    	 if (coords1[1] == 0 || coords2[1] == 0 || coords1[0] == 0 || coords2[0] == 0 ||
                 			coords1[1] == coords2[1] && coords1[0] == coords2[0] )
             	    	Toast.makeText(getApplicationContext(), "To and From cant be same or empty", Toast.LENGTH_SHORT).show();
        	    	 else
        	    	 {
        	    		 routeScrollView.setVisibility(View.GONE);
                     calcPath(coords1[1],coords1[0],coords2[1],coords2[0]);
        	    	 }
            }
        	     }
        });
        tranRouteButtton.setOnClickListener(new Button.OnClickListener() {
        	
            public void onClick(View v) {
            	Location aLoc = new Location("reverseGeocoded");
            	Location bLoc = new Location("reverseGeocoded");
            	aLoc.setLatitude(coords1[1]);
            	aLoc.setLongitude(coords1[0]);
            	bLoc.setLatitude(coords2[1]);
            	bLoc.setLongitude(coords2[0]);
            	float dist = bLoc.distanceTo(aLoc);
            	if (dist < 800) 
                	Toast.makeText(getApplicationContext(), "From and To are too Close", Toast.LENGTH_SHORT).show();

            	else{
            	if (coords1[1] == 0 || coords2[1] == 0 || coords1[0] == 0 || coords2[0] == 0 ||
            			coords1[1] == coords2[1] && coords1[0] == coords2[0] )
        	    	Toast.makeText(getApplicationContext(), "To and From cant be same or empty", Toast.LENGTH_SHORT).show();
            	else
            	{
            	//	if (coords1[1] !=0 && coords2[1]!=0 && coords1[0] !=0 && coords2[0]!=0)
    			{
   	    		 routeScrollView.setVisibility(View.VISIBLE);

            		Vertex fro = CalcPath.getNear(getApplicationContext(), coords1);
            		
            	
            		Vertex to = CalcPath.getNear(getApplicationContext(), coords2);
          
            		
               	 final List<Vertex> list = CalcPath.CalculatedPath(getApplicationContext(), fro.name, to.name);
               	 List<Vertex> startWalk = new ArrayList<Vertex>();
               	 List<Vertex> endWalk = new ArrayList<Vertex>();
               	 startWalk.add(new Vertex(fromLoc, coords1[1], coords1[0], "raw"));
               	 startWalk.add(fro);
               	 endWalk.add(new Vertex(toLoc, coords2[1], coords2[0], "raw"));
               	 endWalk.add(to);
               	if (list != null) 
               	{
               		Toast.makeText(getApplicationContext(), "SR", Toast.LENGTH_SHORT).show();
               		if (firstTime == false)
               		routeLayout.removeAllViewsInLayout();
               		firstTime = false;
           	    //	TextInfo.textInfo(getApplicationContext(), list, routeLayout, mapView);
           	    	//new Shapes(getApplicationContext(), mapView, false).drawRoute(startWalk);
               		walkWay.clear();
               		busWay.clear();
               		walkMarkers.clear();
               		busMarkers.clear();
               		routeLayout.removeAllViews();
               		RouteDraw.drawRoute(getApplicationContext(), list, mapView, busWay);
           	    	RouteDraw.drawRoute(getApplicationContext(), startWalk, mapView, walkWay);
           	    	RouteDraw.drawRoute(getApplicationContext(), endWalk, mapView, walkWay);
           	    	RouteDraw.drawWalkMarker(getApplicationContext(), startWalk, mapView, walkMarkers);
           	    	RouteDraw.drawWalkMarker(getApplicationContext(), endWalk, mapView, walkMarkers);
           	    	List<Vertex> changeBusList = getchangeBusList(list);
           	    	RouteDraw.showRouteInfo(getApplicationContext(), list.get(0),
           	    			list.get(list.size()-1) , changeBusList, routeLayout,  datails);
           	    	Toast.makeText(getApplicationContext(), ""+list, Toast.LENGTH_LONG).show();

           	    	/////////////
           	    	RouteDraw.drawWalkMarker(getApplicationContext(), changeBusList, mapView, busMarkers);

           	    	Toast.makeText(getApplicationContext(), ""+changeBusList, Toast.LENGTH_LONG).show();

           	    	/////////////
           	    //	new Shapes(getApplicationContext(), mapView, false).drawRoute(endWalk);

               			}
               	else // Now we will get a walker
               	{
               		// big guns here
               Toast.makeText(getApplicationContext(), "MR", Toast.LENGTH_SHORT).show();
               	final PathHelper pathHelper = CalcPath.multipleRoute(getApplicationContext(), fro.name, to.name);
               	if (firstTime == false)
           	   		routeLayout.removeAllViewsInLayout();
           	    	firstTime = false;
           		    TextInfo.textInfoMulti(getApplicationContext(), pathHelper, routeLayout, mapView);
           		 new Shapes(getApplicationContext(), mapView, false).drawRoute(startWalk);
        	    	new Shapes(getApplicationContext(), mapView, false).drawRoute(endWalk);
               	}

               			
    			}      
            		}
            	}
            }});
      from.setOnItemClickListener(new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			imm.hideSoftInputFromWindow(from.getWindowToken(), 0);
			// TODO Auto-generated method stub
			fromLoc = arg0.getItemAtPosition(arg2).toString();
			coords1 = db.getNames(fromLoc);
			startMarker.clear();
    		startMarker.addItem(new OverlayItem(new GeoPoint(coords1[1], coords1[0]), "Start", "snip"));
			mapView.setCenter(new GeoPoint(coords1[1], coords1[0]));
			
			
		}
	});
      to.setOnItemClickListener(new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			imm.hideSoftInputFromWindow(to.getWindowToken(), 0);
			// TODO Auto-generated method stub
			toLoc = arg0.getItemAtPosition(arg2).toString();
			coords2 = db.getNames(toLoc);
			mapView.setCenter(new GeoPoint(coords2[1], coords2[0]));
			endMarker.clear();
    		endMarker.addItem(new OverlayItem(new GeoPoint(coords2[1], coords2[0]), "Start", "snip"));
			
		}
	});
        textView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
					imm.hideSoftInputFromWindow(textView.getWindowToken(), 0);
				// TODO Auto-generated method stub			
			double result[] = db.getNames(arg0.getItemAtPosition(0).toString());
				mapView.setCenter(new GeoPoint ( result[1], result[0]));
			
			}
		});
        initWays();
        initMarkers();
        registerForContextMenu(mapView);
        mapsFolder = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/graphhopper/maps/";
        if (!new File(mapsFolder).exists())
			new File(mapsFolder).mkdirs();
        loadMap();

		/** Menu **/
	//	rbmView = (RibbonMenuView) findViewById(R.id.ribbonMenuView1);
        findViewById(R.id.sample_button).setOnClickListener( new ButtonClickHandler() );	
        findViewById(R.id.shareButton).setOnClickListener(new Button.OnClickListener() {
			
			private CharSequence[] items = {"My Location"};

			@Override
			public void onClick(View arg0) {
				AlertDialog.Builder builderSingle = new AlertDialog.Builder(
	                    MainActivity.this);
	            builderSingle.setIcon(getResources().getDrawable(android.R.drawable.ic_menu_share));
	            builderSingle.setTitle("Share");
	            builderSingle.setItems(items , new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int which) {
	                    // The 'which' argument contains the index position
	                    // of the selected item
	                	if (which == 0) 
	                		if (currentLat != 0 && currentLon !=0) {
	                			String message = "Your Current Location is "+currentLat+" N and " +
	                					currentLon + " E";
	                			Intent sendIntent = new Intent(Intent.ACTION_VIEW);
	                			sendIntent.putExtra("sms_body", message); 
	                			sendIntent.setType("vnd.android-dir/mms-sms");
	                			startActivity(sendIntent);
	                		}
	                		else
	                			Toast.makeText(getApplicationContext(), "Current Location Not Avaiable", Toast.LENGTH_SHORT).show();
	                	
	                		
	                } 
	         });
	            AlertDialog dialog = builderSingle.create();
	            dialog.show();
	            dialog.setCanceledOnTouchOutside(true);
	            
			}
		});
      }
      void loadMap() {
          logUser("loading map");
          // mapView.getOverlays().add(pathOverlay);
          prepareGraph();
      }
      private void logUser(String str) {
  		Toast.makeText(this, str, Toast.LENGTH_LONG).show();
  	}
  	void prepareGraph() {
        logUser("loading graph (" + Constants.VERSION + "|" + Constants.VERSION_FILE
                + ") ... ");
        new GHAsyncTask<Void, Void, Path>() {
            protected Path saveDoInBackground(Void... v) throws Exception {
                GraphHopper tmpHopp = new GraphHopper().forMobile();
               // tmpHopp.chShortcuts(true, true);
                
                tmpHopp.load(mapsFolder + currentArea);
            //    logUser("found graph with " + tmpHopp.graph().nodes() + " nodes");
                hopper = tmpHopp;
                return null;
            }

            protected void onPostExecute(Path o) {
                if (hasError()) {
                    logUser("An error happend while creating graph:"
                            + getErrorMessage());
                } else {
                    logUser("Finished loading graph. Touch to route.");   
                    carRouteButton.setEnabled(true);

                }

                finishPrepare();
            }
        }.execute();
    }
  	private void finishPrepare() {
		//prepareInProgress = false;
	}
  	public void calcPath(final double fromLat, final double fromLon,
			final double toLat, final double toLon) {

        logUser("calculating path ...");
        new AsyncTask<Void, Void, GHResponse>() {
            float time;

            protected GHResponse doInBackground(Void... v) {
                StopWatch sw = new StopWatch().start();
                GHRequest req = new GHRequest(fromLat, fromLon, toLat, toLon)
                        .algorithm("dijkstrabi").putHint("douglas.minprecision", 1);
                GHResponse resp = hopper.route(req);
                time = sw.stop().getSeconds();
                return resp;
            }

            protected void onPostExecute(GHResponse resp) {
                if (!resp.hasError()) {
                   /* logUser("from:" + fromLat + "," + fromLon + " to:" + toLat + ","
                            + toLon + " found path with distance:" + resp.distance()
                            / 1000f + ", nodes:" + resp.points().size() + ", time:"
                            + time + " " + resp.debugInfo());
                    logUser("the route is " + (int) (resp.distance() / 100) / 10f
                            + "km long, time:" + resp.time() / 60f + "min, debug:" + time);
                   */
                	createDesiredResponse(resp);
                  //  pathOverlay.getOverlayItems().add(createPolyline(resp));
                   // mapView.redraw();
                } else {
                    logUser("Error:" + resp.errors());
                }
            //    shortestPathRunning = false;
            }
        }.execute();
    }
	protected void createDesiredResponse(GHResponse resp) {
		// TODO Auto-generated method stub
		 int points = resp.points().size();
	        List<Vertex> geoPoints = new ArrayList<Vertex>(points);
	        PointList tmp = resp.points();
	        for (int i = 0; i < resp.points().size(); i++) {
	            geoPoints.add(new Vertex(null,tmp.latitude(i), tmp.longitude(i),null));
	        }
	        walkWay.clear();
       		busWay.clear();
       		walkMarkers.clear();
       		busMarkers.clear();
       		routeLayout.removeAllViews();
	        float dis = (int) (resp.distance() / 100) / 10f;
	        datails.setText("Total Distance = "+dis+" km");
       		RouteDraw.drawRoute(getApplicationContext(), geoPoints, mapView, busWay);

	}
	protected List<Vertex> getchangeBusList(List<Vertex> list) {
		// TODO Auto-generated method stub
		List<Vertex> stopList = new ArrayList<Vertex>();
		String[] temps;
	//	stopList.add(list.get(0));
		for (int i =1; i < list.size()-1; i ++) {
			temps = list.get(i).type.split("&") ;
			if (temps.length > 1) {
				if (list.get(i-1).type != list.get(i+1).type)
				{
					stopList.add(new Vertex(list.get(i).name, list.get(i).lat,
							list.get(i).lon, list.get(i+1).type));
				}
			}
		}
	//	stopList.add(list.get(list.size()-1));
		return stopList;
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	@Override
	   public boolean onOptionsItemSelected(MenuItem item) {
	       switch (item.getItemId())
	       {
	       
	       case R.id.showhide_auto:
	    	   from.setVisibility(View.GONE);
	    	   to.setVisibility(View.GONE);
	    	  if (textView.getVisibility()==View.GONE)
	    	  {
	    	     textView.setVisibility(View.VISIBLE);
	    	     textView.setFocusable(true);
	    	  }
	    	  else
	    		  textView.setVisibility(View.GONE);	    	   
	    	   break;
	     /*  case R.id.showRouteButton:
	    	   Shapes shape = new Shapes(getApplicationContext(), mapView);
		       shape.showShapesOnMap("shapes.csv");
		       
		       break;*/
	       case R.id.currentLoc:
	    	   if (currentLat!=0 && currentLon!=0)
	    	   {
	    	   GeoPoint geoPoint = new GeoPoint(currentLat, currentLon);
	    	   mapView.setCenter(geoPoint);		  
	    	   OverlayItem marker = new OverlayItem(geoPoint, "","");
	    	   itemizedOverlay.clear();
	    	   itemizedOverlay.addItem(marker);
	    	   }
	    	   else
	    		   Toast.makeText(getApplicationContext(), "Still Locating Your Location", Toast.LENGTH_SHORT).show();
	    	   break;
	       case R.id.directions:
	    	   textView.setVisibility(View.GONE);
	    	 if (from.getVisibility()==View.GONE)
	    	 {
		    	     from.setVisibility(View.VISIBLE);
	    	 		 to.setVisibility(View.VISIBLE);
	    	 		buttonLayout.setVisibility(View.VISIBLE);
	    	 }
		    	  else
		    	  {
		    		  from.setVisibility(View.GONE);
		    		  to.setVisibility(View.GONE);
		    		  buttonLayout.setVisibility(View.GONE);
		    	  }
	    	   break;
	       case R.id.changePageMenu:
	    	   Intent intent = new Intent(this, ChangeActivity.class);
	    	   startActivity(intent);
	    	   break;
	       case R.id.navigation:
	    	//   rbmView.toggleMenu();
	    	   from.setVisibility(View.GONE);
	    	   navigation.setVisibility(View.VISIBLE);
	    	   to.setVisibility(View.GONE);
	    	   buttonLayout.setVisibility(View.GONE);
	    	   textView.setVisibility(View.GONE);
	    	   String s = "<b> Latitute = </b>"+currentLat+	"<b> Longitude = </b>"+currentLon+"<br>"+
	    	   "<b>Speed = </b>"+currentSpeed+"<b> Altitude = </b>"+currenetAltitude  
	    	   ;
	    			   
	    	   navigation.setText(Html.fromHtml(s));
	    	   
	    	  
	    	   break;
	       }
	     
	       return true;
	   }
	public class MyLocationListener implements LocationListener
	{

	 

	@Override
	  public void onLocationChanged(Location loc)
	  {
		  currentLat = loc.getLatitude();
		  currentLon = loc.getLongitude();
		  currentSpeed = loc.getSpeed();
		  currentAccuracy = loc.getAccuracy();
		  currenetAltitude =  loc.getAltitude();
		 
	  }

	  @Override
	  public void onProviderDisabled(String provider)
	  {
	    Toast.makeText( getApplicationContext(), "GPS Disabled", Toast.LENGTH_SHORT ).show();
	  }

	  @Override
	  public void onProviderEnabled(String provider)
	  {
	    Toast.makeText( getApplicationContext(), "GPS Enabled", Toast.LENGTH_SHORT).show();
	  }

	  @Override
	  public void onStatusChanged(String provider, int status, Bundle extras)
	  {

	  }
	}
	boolean vistoBool (int vis) 
    {
    	if (vis ==View.VISIBLE) return true;
    	else return false;
    }
	 public class ButtonClickHandler implements View.OnClickListener {
			public void onClick( View view ) {
				

				AlertDialog.Builder builderSingle = new AlertDialog.Builder(
	                    MainActivity.this);
	            builderSingle.setIcon(getResources().getDrawable(android.R.drawable.ic_menu_view));
	            builderSingle.setTitle("Windows");
	            
	            boolean [] visList = {vistoBool(textView.getVisibility()), vistoBool(from.getVisibility()),
	            		vistoBool(routeScrollView.getVisibility()), vistoBool(buttonLayout.getVisibility()), vistoBool(mapView.getVisibility())};
	            builderSingle.setMultiChoiceItems(_options, visList, new DialogInterface.OnMultiChoiceClickListener() {
					
					@Override
					public void onClick(DialogInterface arg0, int clicked, boolean selected) {
						// TODO Auto-generated method stub
						switch (clicked) {
						case 0: 
							if (selected)
								textView.setVisibility(View.VISIBLE);
							else
								textView.setVisibility(View.GONE);
							break;
						case 1: 
							if (selected) {
								from.setVisibility(View.VISIBLE);
								to.setVisibility(View.VISIBLE);}
							else
							{
								from.setVisibility(View.GONE);
								to.setVisibility(View.GONE);
							}
							break;
						case 2: 
							if (selected)
								//routeLayout.setVisibility(View.VISIBLE);
								routeScrollView.setVisibility(View.VISIBLE);
							else
							//	routeLayout.setVisibility(View.GONE);
								routeScrollView.setVisibility(View.GONE);
							break;
						case 3: 
							if (selected)
								buttonLayout.setVisibility(View.VISIBLE);
							else
								buttonLayout.setVisibility(View.GONE);
							break;
						case 4: 
							if (selected)
								mapView.setVisibility(View.VISIBLE);
							else
								mapView.setVisibility(View.GONE);
							break;
						}
					}
				});
	            AlertDialog dialog = builderSingle.create();
	            dialog.show();
	            dialog.setCanceledOnTouchOutside(true);
	        
			
				
			}
		}
	    
	 
	 @Override
	 public void onBackPressed() {
	     new AlertDialog.Builder(this)
	         .setIcon(android.R.drawable.ic_dialog_alert)
	         .setTitle("Exit Easy Travel")
	         .setMessage("Are you sure you want to Close Easy Travel ?")
	         .setPositiveButton("Yes", new DialogInterface.OnClickListener()
	     {
	         @Override
	         public void onClick(DialogInterface dialog, int which) {
	             finish();    
	         }

	     })
	     .setNegativeButton("No", null)
	     .show();
	 }

}


