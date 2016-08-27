package com.example.smarthome_client;

import android.os.AsyncTask;

public class AsyncrhonousReadSubscribe  extends AsyncTask<Object, Object, Object> {
  
	@Override
	protected Object doInBackground(Object... params) {
		// TODO Auto-generated method stub
		MainActivity.createMQTTDefaultClients();
		return null;
	}
}

