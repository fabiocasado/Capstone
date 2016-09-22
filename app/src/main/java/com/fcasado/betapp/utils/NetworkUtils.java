package com.fcasado.betapp.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.fcasado.betapp.R;

/**
 * Created by fcasado on 9/22/16.
 */

public class NetworkUtils {
	public static boolean isOnline(Context context) {
		ConnectivityManager cm =
				(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		return activeNetwork != null &&
				activeNetwork.isConnectedOrConnecting();

	}

	public static boolean checkOnlineAndNotifyIfNeeded(Context context) {
		if (isOnline(context)) {
			return true;
		}

		Toast.makeText(context, R.string.no_connectivity, Toast.LENGTH_SHORT).show();
		return false;
	}
}
