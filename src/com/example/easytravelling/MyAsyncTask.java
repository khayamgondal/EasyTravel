package com.example.easytravelling;

import android.os.AsyncTask;

public abstract class MyAsyncTask<A, B, C> extends AsyncTask<A, B, C> {
	private Throwable error;

	protected abstract C saveDoInBackground(A... params) throws Exception;
		
	protected C doInBackground(A... params) {
		try {
			return saveDoInBackground(params);
		} catch (Throwable t) {
			error = t;
			return null;
		}
	};

	public boolean hasError() {
		return error != null;
	}
	
	public String getErrorMessage() {
		if(hasError()) 
			return error.getMessage();
		return "No Error";			
	}
}
