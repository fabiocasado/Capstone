package com.fcasado.betapp.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by fcasado on 9/14/16.
 */
public class FavoriteBetsService extends RemoteViewsService {
	@Override
	public RemoteViewsFactory onGetViewFactory(Intent intent) {
		return new FavoriteBetsFactory(getApplicationContext());
	}
}
