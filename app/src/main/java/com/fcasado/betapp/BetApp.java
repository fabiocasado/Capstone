package com.fcasado.betapp;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.fcasado.betapp.data.User;


/**
 * Created by fcasado on 6/4/16.
 */
public class BetApp extends Application {
	public static User currentUser;

	@Override
	public void onCreate() {
		super.onCreate();

		FacebookSdk.sdkInitialize(getApplicationContext());
	}
}
