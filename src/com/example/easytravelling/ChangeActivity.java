package com.example.easytravelling;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.ByteArrayBuffer;
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class ChangeActivity extends Activity {

	RelativeLayout R1;
	RelativeLayout R2;
	RelativeLayout R3;
	String text;
	EditText et1;
	EditText et2;
	EditText et3;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.change, menu);
		return true;
	}
	public void firstPageNextButton(View v) {
		R1 = (RelativeLayout) findViewById(R.id.firstPage);
		R2 = (RelativeLayout) findViewById(R.id.secondPage);
		RadioButton rb = (RadioButton)(findViewById(R.id.newPlace));
		if (rb.isChecked()) {
		R1.setVisibility(View.GONE);
		R2.setVisibility(View.VISIBLE);
		}
	}
	public void secondPageBackButton (View v) {
		R1 = (RelativeLayout) findViewById(R.id.firstPage);
		R2 = (RelativeLayout) findViewById(R.id.secondPage);
		R2.setVisibility(View.GONE);
		R1.setVisibility(View.VISIBLE);
	}
	public void firstPageCancelButton (View v) {
		System.exit(0);
	}
	public void sendRequest(View v) {
		 et1 = (EditText) findViewById(R.id.newPlaceText);
		 et2 = (EditText) findViewById(R.id.newPlaceLat);
		 et3 = (EditText) findViewById(R.id.newPlaceLon);
		 
		if (et1.getText().length() == 0)
			Toast.makeText(getApplicationContext(), "Name can't be null", Toast.LENGTH_SHORT).show();
		else if (et2.getText().length()==0)
			Toast.makeText(getApplicationContext(), "Latitute can't be null", Toast.LENGTH_SHORT).show();
		else  if (et3.getText().length()==0)
			Toast.makeText(getApplicationContext(), "Longitute can't be null", Toast.LENGTH_SHORT).show();
		else {
			final Button b = (Button) findViewById(R.id.secondSubmit);
		new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute()
            {
                //Log.d(TAG, "onPreExecute()");
            	b.setEnabled(false);
            }

            @Override
            protected Void doInBackground(Void... params)
            {
            	//else
            	{
        		// Creating HTTP client
        		HttpClient httpClient = new DefaultHttpClient();
        		 
        		// Creating HTTP Post
        		HttpPost httpPost = new HttpPost("http://submitchanges.ap01.aws.af.cm/change.php");
        			
                try {
                    // Add your data
                	List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(3);
            		nameValuePair.add(new BasicNameValuePair("name", et1.getText().toString()));
            		nameValuePair.add(new BasicNameValuePair("lat", et2.getText().toString()));
            		nameValuePair.add(new BasicNameValuePair("lon", et3.getText().toString()));
            		httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair)); 
                    // Execute HTTP Post Request
                    HttpResponse response = httpClient.execute(httpPost);
         
                    InputStream is = response.getEntity().getContent();
                    BufferedInputStream bis = new BufferedInputStream(is);
                    ByteArrayBuffer baf = new ByteArrayBuffer(20);
         
                    int current = 0;
                     
                    while((current = bis.read()) != -1){
                        baf.append((byte)current);
                    }  
         
                    /* Convert the Bytes read to a String. */
                     text = new String(baf.toByteArray());
                   
                } catch (ClientProtocolException e) {
                    // TODO Auto-generated catch block
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                }
            }
               // Log.d(TAG, "doInBackground() -- Here is the download");
                // downloadBitmap("http://mydomain.com/image.jpg")
                return null;
            }

            @Override
            protected void onPostExecute(Void res)
            {
            //    Log.d(TAG, "onPostExecute()");
            	b.setEnabled(true);

                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();

                if(isCancelled()){
                    return;
                }
            }
        }.execute();
		}
	
	}
} 
