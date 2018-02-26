package com.example.easytravelling;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.AssetManager;

public class Stops {
	
	private static final String CSV_PATH ="stops.csv";
	public Stops()
	{
		
	}

	public final static List<String[]> readCsv(Context context) {
		  List<String[]> questionList = new ArrayList<String[]>();
		  AssetManager assetManager = context.getAssets();

		  try {
		    InputStream csvStream = assetManager.open(CSV_PATH);
		    InputStreamReader csvStreamReader = new InputStreamReader(csvStream);
		    CSVReader csvReader = new CSVReader(csvStreamReader);
		    String[] line;

		    // throw away the header
		 //   csvReader.readNext();

		    while ((line = csvReader.readNext()) != null) {
		      questionList.add(line);
		    }
		  } catch (IOException e) {
		    e.printStackTrace();
		  }
	 // Toast.makeText(context, ""+questionList.size(), Toast.LENGTH_LONG).show();
		  return questionList;
		}
}
