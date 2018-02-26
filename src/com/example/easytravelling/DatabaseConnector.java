package com.example.easytravelling;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class DatabaseConnector extends SQLiteAssetHelper {

	private static final String DATABASE_NAME = "pakistan";
	private static final int DATABASE_VERSION = 2;
	private SQLiteDatabase sqliteDBInstance = null;
	public DatabaseConnector(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);	
		 sqliteDBInstance = getReadableDatabase();
		
		
	}
	public String[] allList()
	{
		///Cursor cursor = this.sqliteDBInstance.query(DB_TABLE_NAME, new String[] {DB_COLUMN_1_NAME, DB_COLUMN_3_NAME}, null, null, null, null, null);
	Cursor cursor =	this.sqliteDBInstance.rawQuery("select v from node_tags", null);
	//sqliteDBInstance.close();
		if(cursor.getCount() >0)
        {
            String[] str = new String[cursor.getCount()];
            int i = 0;
 
            while (cursor.moveToNext())
            {
                 str[i] = cursor.getString(0);
                 i++;
             }
            cursor.close();
            return str;
        }
        else
        {
        	cursor.close();
            return new String[] {};
        }
	}
	public double[] getNames(String arg0) {
		Cursor cursor =	this.sqliteDBInstance.rawQuery("select node_id from node_tags where v = '"+arg0+"' ;", null);
		cursor.moveToFirst();
		String string = cursor.getString(0);
		cursor = this.sqliteDBInstance.rawQuery("select * from nodes where nodes_id = '"+string+"' ;" , null);
		cursor.moveToFirst();
		
		double lat = cursor.getDouble(3);
		double lon = cursor.getDouble(2);
		double result[] = {lon, lat};
		cursor.close();
		return result;	
	}
}
