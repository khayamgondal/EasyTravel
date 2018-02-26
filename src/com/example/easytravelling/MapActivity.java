package com.example.easytravelling;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MapActivity extends Activity {
	public static final int DIALOG_DOWNLOAD_PROGRESS = 0;
    private Button startBtn;
    private ProgressDialog mProgressDialog;
	private Button exitBtn;
 
   

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		File map =new File(Environment.getExternalStorageDirectory()+"/pakistan.map");
		if (! map.exists()) {
			
		startBtn = (Button)findViewById(R.id.startBtn);
		exitBtn = (Button) findViewById(R.id.exit);
		exitBtn.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                finish();
                System.exit(0);
            }
        });
        startBtn.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                startDownload();
                
            }
        });
		}
        else
        {
		Intent myIntent = new Intent(MapActivity.this, MainActivity.class);
		MapActivity.this.startActivity(myIntent);
		finish();
        }
	
	}
	 private void startDownload() {
	        String url = "http://download.mapsforge.org/maps/asia/pakistan.map";
	        new DownloadFileAsync().execute(url);
	    }
	 /* dont know where to call this function kindly fucking check where to call it. */
	 private void downloadSucceed() {
		 if ((new File(Environment.getExternalStorageDirectory()+"/pakistan.map").exists())) {
			 Toast.makeText(getApplicationContext(), "Map Found", Toast.LENGTH_SHORT).show();
			 Intent myIntent = new Intent(MapActivity.this, MainActivity.class);
				MapActivity.this.startActivity(myIntent);
				finish();
		 }
	 }
	 @Override
	    protected Dialog onCreateDialog(int id) {
	        switch (id) {
	            case DIALOG_DOWNLOAD_PROGRESS:
	                mProgressDialog = new ProgressDialog(this);
	                mProgressDialog.setMessage("Downloading Map . . .");
	                mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
	                mProgressDialog.setCancelable(false);
	                mProgressDialog.show();
	                return mProgressDialog;
	            default:
	                return null;
	        }
	    }
	 class DownloadFileAsync extends AsyncTask<String, String, String> {
	        
	        @Override
	        protected void onPreExecute() {
	            super.onPreExecute();
	            showDialog(DIALOG_DOWNLOAD_PROGRESS);
	        }

	        @Override
	        protected String doInBackground(String... aurl) {
	            int count;

	            try {
	                URL url = new URL(aurl[0]);
	                URLConnection conexion = url.openConnection();
	                conexion.connect();

	                int lenghtOfFile = conexion.getContentLength();
	                Log.d("ANDRO_ASYNC", "Lenght of file: " + lenghtOfFile);

	                InputStream input = new BufferedInputStream(url.openStream());
	                OutputStream output = new FileOutputStream("/sdcard/pakistan.map");

	                byte data[] = new byte[1024];

	                long total = 0;

	                while ((count = input.read(data)) != -1) {
	                    total += count;
	                    publishProgress(""+(int)((total*100)/lenghtOfFile));
	                    output.write(data, 0, count);
	                }

	                output.flush();
	                output.close();
	                input.close();
	                downloadSucceed(); // fucking try to call
	            } catch (Exception e) {}
	            return null;

	        }
	        protected void onProgressUpdate(String... progress) {
	             Log.d("ANDRO_ASYNC",progress[0]);
	             mProgressDialog.setProgress(Integer.parseInt(progress[0]));
	        }

	        @Override
	        protected void onPostExecute(String unused) {
	            dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
	        }
	    }
	}

