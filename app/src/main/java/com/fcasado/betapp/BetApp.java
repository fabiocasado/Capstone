package com.fcasado.betapp;

import android.app.Application;

import com.facebook.FacebookSdk;


/**
 * Created by fcasado on 6/4/16.
 */
public class BetApp extends Application {
	@Override
	public void onCreate() {
		super.onCreate();

		FacebookSdk.sdkInitialize(getApplicationContext());
	}
}
