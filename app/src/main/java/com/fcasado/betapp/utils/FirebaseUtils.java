package com.fcasado.betapp.utils;

import android.content.Context;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;

/**
 * Created by fcasado on 9/12/16.
 */
public class FirebaseUtils {
	public static final String LOGIN_ACTIVITY = "login_activity";
	public static final String BET_LISTS_ACTIVITY = "bet_lists_activity";
	public static final String BET_CREATE_ACTIVITY = "bet_create_activity";
	public static final String BET_DETAILS_ACTIVITY = "bet_details_activity";
	public static final String PARTICIPANTS_SHOW_ACTIVITY = "participants_show_activity";
	public static final String PARTICIPANTS_ADD_ACTIVITY = "participants_add_activity";

	public static void logEvent(Context context, String event, Bundle bundle) {
		if (context == null || event == null) {
			return;
		}

		FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(context);
		firebaseAnalytics.logEvent(event, bundle);
	}
}
