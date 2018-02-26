package com.example.easytravelling;

import java.util.ArrayList;
import java.util.List;

import org.mapsforge.core.GeoPoint;

import android.content.Context;
import android.location.Location;
import android.widget.Toast;

import com.example.easytravelling.Dijkstra;

public class CalcPath {

	static Vertex minStart ;
    static Vertex minEnd; 	
    static boolean direct = true;
    static double minDis;
    static Vertex last;
    static Vertex first;
    static List<Vertex> verList;
public CalcPath()
	{     
	    
	}
public static Vertex redSt()
{
	return minStart;
}
public static Vertex redSo()
{
	return minEnd;
}
public static String[] listStop(Context context)
{
	List<Vertex> lists = createGraph(context);
	String[] names = new String[lists.size()];
	for (int i =0; i < lists.size(); i++)
	{
		names[i] = lists.get(i).name;
	}
	return names;
}
public static List<Vertex> createGraph(Context context)
{
	List<String[]> list = Stops.readCsv(context);
	//Toast.makeText(context, list.get(0)[0], Toast.LENGTH_LONG).show();
	List<Vertex> vertices = new ArrayList<Vertex>() ;
	
	for (int i=0; i < list.size(); i++)
	{
	Vertex v = new Vertex(list.get(i)[0], Double.parseDouble(list.get(i)[1]), Double.parseDouble(list.get(i)[2]) , list.get(i)[3]);
	vertices.add(v);
	}
	//raja bazaar
	vertices.get(0).adjacencies = new Edge[]{ 
		new Edge(vertices.get(1), 1.1),  // committe chowk
								  			};
// commite chowk
	vertices.get(1).adjacencies = new Edge[]{ 
		new Edge(vertices.get(0), 1.1),  // raja baazaar
		 new Edge(vertices.get(2), 0.4)  // waris khan
			};
// waris khan
	vertices.get(2).adjacencies = new Edge[]{ 
		new Edge(vertices.get(1), 0.4),  // committe chowk
		 new Edge(vertices.get(3), 3.2)  // naz cinema
			};
	// naz cinema
	vertices.get(3).adjacencies = new Edge[]{ 
		new Edge(vertices.get(2), 3.2), // waris khan
		 new Edge(vertices.get(4), 1.40)  // central hospital
			};
	// central hospital
	vertices.get(4).adjacencies = new Edge[]{ 
		new Edge(vertices.get(3), 1.40), // naz cineama
		new Edge(vertices.get(5), 1.2)  // chandani chowk
			};
	// chandni chowk
	vertices.get(5).adjacencies = new Edge[]{ 
		new Edge(vertices.get(4), 1.2), // central hospital
		new Edge(vertices.get(6), 1.1) // rehmanabad
			};
	// rehmanabad
	vertices.get(6).adjacencies = new Edge[]{ 
		new Edge(vertices.get(5), 1.1), //chandani chowk
		new Edge(vertices.get(7), 1.40) // passport office
			};
	// passport office
	vertices.get(7).adjacencies = new Edge[]{ 
		new Edge(vertices.get(6), 1.4), // rehmabad
		new Edge(vertices.get(8), 1.40)  // shamsabad
			};
	// shamsabad
	vertices.get(8).adjacencies = new Edge[]{ 
		new Edge(vertices.get(7), 1.4), // passport
		new Edge(vertices.get(9), 3.0) // fazabad
			};
	// fazabad
	vertices.get(9).adjacencies = new Edge[]{ 
		new Edge(vertices.get(8), 3.0),  // shamsabad
		new Edge(vertices.get(10), 4.6), //zero point
		new Edge(vertices.get(20), 9.2),  // perwidha
		new Edge(vertices.get(13), 7.6),  // abapara
		new Edge(vertices.get(39), 6.2),  // penora
		new Edge(vertices.get(40), 13.3),// malpor
		new Edge(vertices.get(45), 6.5),  // khana
		new Edge(vertices.get(46), 4.2)  // al shifa
			};
	// zero point
	vertices.get(10).adjacencies = new Edge[]{ 
		new Edge(vertices.get(9), 4.6), //fazabad
		new Edge(vertices.get(11), 1.5) // firebridde
			};
	// fire brid
	vertices.get(11).adjacencies = new Edge[]{ 
		new Edge(vertices.get(10), 1.5), //fazabad
		new Edge(vertices.get(12), 1.40)  // store stop
			};
	// store stop
	vertices.get(12).adjacencies = new Edge[]{ 
		new Edge(vertices.get(11), 1.4), // fire brid
		new Edge(vertices.get(13), 0.800)  // abpara
			};
	// abpara
	vertices.get(13).adjacencies = new Edge[]{ 
		new Edge(vertices.get(12), 0.800), // store stop
		new Edge(vertices.get(14), 2.3), // m n a hostel
		new Edge(vertices.get(21), 4.7), // super market
		new Edge(vertices.get(37), 2.5),   // poly clinic
		new Edge(vertices.get(38), 2.6) // foreign office
			};
	//m n a hostel
	vertices.get(14).adjacencies = new Edge[]{ 
		new Edge(vertices.get(13), 2.3), //abpara
		new Edge(vertices.get(15), 2.7)  // convention cen
			};
	// convenation center
	vertices.get(15).adjacencies = new Edge[]{ 
		new Edge(vertices.get(14), 2.7), // m n a hostel
		new Edge(vertices.get(16), 5.6) // rawal jheel
			};
	// rawal jheel
	vertices.get(16).adjacencies = new Edge[]{ 
		new Edge(vertices.get(15), 5.6), // conve center
		new Edge(vertices.get(17), 4.0) // uni mor
			};
	// uni mor
	vertices.get(17).adjacencies = new Edge[]{ 
		new Edge(vertices.get(16), 4.0), // rawal jheel
		new Edge(vertices.get(18), 0.7) // quaid uni
			};
	// quaid e azam uni
	vertices.get(18).adjacencies = new Edge[]{ 
		new Edge(vertices.get(17), 0.7), // uni mor
		new Edge(vertices.get(19), 1.5) // bari imam
			};
	// darbar bari imam
	vertices.get(19).adjacencies = new Edge[]{ 
		new Edge(vertices.get(18), 1.5) // quaid e azam uni
			};
	///////////////////////////////////////////////
	// perwidhai
	vertices.get(20).adjacencies = new Edge[]{ 
		new Edge(vertices.get(9), 8.0), //fazabad
		new Edge(vertices.get(39), 4.9) // pendora
			};
	// super market
	vertices.get(21).adjacencies = new Edge[]{ 
		new Edge(vertices.get(22), 5.3), // faisal masjid
		new Edge(vertices.get(13), 5.0) // abpara
			};
	// faisal masjid
	vertices.get(22).adjacencies = new Edge[]{ 
		new Edge(vertices.get(21), 5.3) // super market
	};
	
	///////////////////////////////////////////
	// pak sec
	vertices.get(23).adjacencies = new Edge[]{ 
		new Edge(vertices.get(29), 2.4)  // parlement
	};	
	// g 11 1
	vertices.get(24).adjacencies = new Edge[]{ 
		new Edge(vertices.get(30), 12.2),  // tarnol
		new Edge(vertices.get(25), 7.5), // peshawar mor
		new Edge(vertices.get(31), 4.2), // g10 double road
		new Edge(vertices.get(33), 1.3) //g 11/ 2
	};
	// peshawar mor
	vertices.get(25).adjacencies = new Edge[]{ 
		new Edge(vertices.get(24), 6.6), // g 11 1
		new Edge(vertices.get(26), 2.5)  // g 8 markaz
	};
	// g-8 mark
	vertices.get(26).adjacencies = new Edge[]{ 
		new Edge(vertices.get(25), 2.5), // pesh mor
		new Edge(vertices.get(27), 1.4) // pims
	};
	// pims
	vertices.get(27).adjacencies = new Edge[]{ 
		new Edge(vertices.get(26), 1.4), // g-8 mark
		new Edge(vertices.get(28), 1.0), // blue area
		new Edge(vertices.get(47), 4.9),  // allama iqbal
		new Edge(vertices.get(48), 5.4)   // f-8 markaz
	};
	// blue area
	vertices.get(28).adjacencies = new Edge[]{ 
		new Edge(vertices.get(27), 4.60), // pims
		new Edge(vertices.get(29), 4.60), // parliment
		new Edge(vertices.get(23), 5.0), // sectrate
	};
	/// parilmanet house
	
	vertices.get(29).adjacencies = new Edge[]{ 
		new Edge(vertices.get(23), 5.0), // pak sec
		new Edge(vertices.get(28), 4.60) // blue area
	};
	// tarnol
	vertices.get(30).adjacencies = new Edge[]{ 
		new Edge(vertices.get(24), 4.60) // g -11
	};
	//////////////////////////////////////// <TODO>
	// g 10 double road
	vertices.get(31).adjacencies = new Edge[]{ 
		new Edge(vertices.get(24), 4.60),
		new Edge(vertices.get(32), 4.60),
	};
	// g 9 markaz
	vertices.get(32).adjacencies = new Edge[]{ 
		new Edge(vertices.get(31), 4.60),
		new Edge(vertices.get(27), 4.60),
		new Edge(vertices.get(35), 4.60), // g 7 markaz
		new Edge(vertices.get(34), 4.60), // g 10 2/3
		
	};
	// g 11 /2-3
	vertices.get(33).adjacencies = new Edge[]{ 
		new Edge(vertices.get(24), 4.60),
		new Edge(vertices.get(32), 4.60) // g 9 markaz
	};
	
	// g 10 2/3
	vertices.get(34).adjacencies = new Edge[]{ 
		new Edge(vertices.get(33), 4.60),
		new Edge(vertices.get(32), 4.60)
	};
	// g 7 markaz
	vertices.get(35).adjacencies = new Edge[]{ 
		new Edge(vertices.get(32), 4.60), // g 9 markaz
		new Edge(vertices.get(36), 4.60) // lal masjid
	};
	// lal masjid
	vertices.get(36).adjacencies = new Edge[]{ 
		new Edge(vertices.get(35), 4.60), // g 9 markaz
		new Edge(vertices.get(37), 4.60) // lal masjid
	};
	// poly clinic 
	vertices.get(37).adjacencies = new Edge[]{ 
		new Edge(vertices.get(36), 4.60), // lal
		new Edge(vertices.get(13), 4.60) // aabpara
	};
	// foreign office
	vertices.get(38).adjacencies = new Edge[]{ 
		new Edge(vertices.get(13), 4.60), // aabpara
		new Edge(vertices.get(23), 4.60) // sect
	};
/////////////////////////////// ROUTE 7 STARTS
	
	// penora
	vertices.get(39).adjacencies = new Edge[]{ 
		new Edge(vertices.get(20), 4.60), // penora
		new Edge(vertices.get(9), 4.60) // faizabad
	};
	// malpor
	vertices.get(40).adjacencies = new Edge[]{ 
		new Edge(vertices.get(41), 4.60), // bhara khau
		new Edge(vertices.get(9), 4.60) // faizabad
	};
	// bhara khao
	vertices.get(41).adjacencies = new Edge[]{ 
		new Edge(vertices.get(40), 4.60), // malpor
		};
	/////////////////// ROUTE 8 STARTS RAWAT TO F8
	// rawat
	vertices.get(42).adjacencies = new Edge[]{ 
		new Edge(vertices.get(43), 4.60), // high way
		
		};
	// islamabad highway
	vertices.get(43).adjacencies = new Edge[]{ 
		new Edge(vertices.get(42), 4.60), // rawat
		new Edge(vertices.get(44), 4.60) // kak pul
		
		};
	// kak put
	vertices.get(44).adjacencies = new Edge[]{ 
		new Edge(vertices.get(43), 4.60), // high way
		new Edge(vertices.get(45), 4.60) // khana
		
		};
	// khana
	vertices.get(45).adjacencies = new Edge[]{ 
		new Edge(vertices.get(44), 4.60), // kak pul
		new Edge(vertices.get(9), 4.60) // faiazabad
				};
	// al shifa
	vertices.get(46).adjacencies = new Edge[]{ 
		new Edge(vertices.get(9), 4.60), // fazabad
		new Edge(vertices.get(47), 4.60) // allama iqbal
		};
	// allama iqbal
	vertices.get(47).adjacencies = new Edge[]{ 
		new Edge(vertices.get(46), 4.60), // al shifa
		new Edge(vertices.get(27), 4.60) // PIMS
		};
	// F-8 markaz
	vertices.get(48).adjacencies = new Edge[]{ 
		new Edge(vertices.get(27), 4.60), // pims
		
		};
	return vertices;
}
public static List<Vertex> CalculatedPath(Context context, String from, String to)
{
	
	 last = new Vertex(null, 0, 0, null) ;
	 first = new Vertex(null, 0, 0, null);
	 verList = createGraph(context);
	direct = true;
	//Toast.makeText(context, from+to, Toast.LENGTH_LONG).show();
	for (int i =0; i <verList.size(); i++)
	{
		if (verList.get(i).name.compareTo(from)==0)
		{
		first = verList.get(i);
		Dijkstra.computePaths(verList.get(i));
		
		break;
		}
	}
	List<Vertex> path = null;
	for (int i =0; i <verList.size(); i++)
	{
		if (verList.get(i).name.compareTo(to)==0)
		{
			last = verList.get(i);
			path = Dijkstra.getShortestPathTo(verList.get(i));
			minDis = verList.get(i).minDistance;
			break;
		}
		
	}
	Toast.makeText(context, first.name+" to "+ last.name, Toast.LENGTH_SHORT).show();
    if (path.get(0).compareTo(path.get(path.size()-1)) == 0) 
    	return null;
    
    else return path;
}
public static PathHelper multipleRoute(Context context, String from, String to)
{
	direct = false;
	//Toast.makeText(context,"No direct route", Toast.LENGTH_SHORT).show();
//////////////////// GETTING NEW PATH ///////////////////////////////
String startTrip = first.type;
String endTrip = last.type;
//   
List<Vertex> startTripList = new ArrayList<Vertex>();
List<Vertex> endTripList = new ArrayList<Vertex>();
for (int i=0 ; i < verList.size() ; i++)
{
	if (verList.get(i).type.contains(startTrip))
		startTripList.add(verList.get(i));
}
for (int i=0 ; i < verList.size() ; i++)
{
	if (verList.get(i).type.contains(endTrip))
		endTripList.add(verList.get(i));
}

Location aLoc = new Location("reverseGeocoded");
Location bLoc = new Location("reverseGeocoded");
 minStart = new Vertex(null, 0, 0, null);
 minEnd = new Vertex(null, 0, 0, null);

float min = 1000000 ;
float distance = 0;
for (int i =0 ; i < startTripList.size(); i++) 
{
	for (int j =0; j < endTripList.size(); j++)
	{
		aLoc.setLatitude(startTripList.get(i).lat);
		aLoc.setLongitude(startTripList.get(i).lon);
		bLoc.setLatitude(endTripList.get(j).lat);
		bLoc.setLongitude(endTripList.get(j).lon);    		
	 distance = aLoc.distanceTo(bLoc);
	 if (distance < min)
	 {
		 min = distance;
		 minStart = startTripList.get(i);
		 minEnd = endTripList.get(j);
		 
	 }
		    	}
}

List<Vertex> path1 = new ArrayList<Vertex>();
List<Vertex> path2 = new ArrayList<Vertex>();
double path1Dis;
Dijkstra.computePaths(first);
path1Dis = minStart.minDistance;
path1 = Dijkstra.getShortestPathTo(minStart);
Toast.makeText(context, first.name+" to "+minStart.name+"&&&"+
minEnd+" to "+last.name, Toast.LENGTH_SHORT).show();
Toast.makeText(context, path1+"&&"+path2, Toast.LENGTH_SHORT).show();
double path2Dis;
Dijkstra.computePaths(minEnd);
path2Dis =  minStart.minDistance;
path2 = Dijkstra.getShortestPathTo(last);


return new PathHelper(path1,path1Dis, path2, path2Dis, minStart, minEnd);
	
}
public static Vertex getNear(Context context, double coords[])
{
	double  min = 1000000;
	float distance = 0 ;
	Location aLoc = new Location("reverseGeocoded");
	Location bLoc = new Location("reverseGeocoded");
	List<Vertex> allStops = createGraph(context);
	Vertex nearUs = null ;//= new Vertex(null, 0, 0, null);
	bLoc.setLatitude(coords[1]);
	bLoc.setLongitude(coords[0]);
	for (int i =0 ; i < allStops.size(); i++)
	{

		aLoc.setLatitude(allStops.get(i).lat);
		aLoc.setLongitude(allStops.get(i).lon);
		
		distance = aLoc.distanceTo(bLoc);
		 if (distance < min)
		 {
			 min = distance;
			 nearUs = new Vertex(allStops.get(i).name, allStops.get(i).lat,
					 allStops.get(i).lon, allStops.get(i).type);

		 }
	}
	//Toast.makeText(context, , Toast.LENGTH_SHORT).show();

	return nearUs;
	
}
}
