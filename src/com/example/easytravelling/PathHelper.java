package com.example.easytravelling;

import java.util.List;

public class PathHelper {
public List<Vertex> first;
public List<Vertex> last;
double path1Distance;
double path2Distance;
Vertex mStart;
Vertex mEnd;
public PathHelper(List<Vertex> first_Path, double path1Dis, List<Vertex> last_Path, 
		double path2Dis, Vertex minStart, Vertex minEnd)
{
	first = first_Path;
	last = last_Path;
	path1Distance = path1Dis;
	path2Distance = path2Dis;
	mStart = minStart;
	mEnd = minEnd;
}
}
