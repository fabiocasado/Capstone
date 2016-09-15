package com.fcasado.betapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;
import android.widget.Toast;

/**
 * Implementation of App Widget functionality.
 */
public class FavoriteBetsWidgetProvider extends AppWidgetProvider {
	public static final String EXTRA_BET_ID = "com.fcasado.betapp.EXTRA_BET_ID";
	public static final String TOAST_ACTION = "com.fcasado.betapp.TOAST_ACTION";

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		// There may be multiple widgets active, so update all of them
		for (int appWidgetId : appWidgetIds) {
			updateAppWidget(context, appWidgetManager, appWidgetId);
		}

		super.onUpdate(context, appWidgetManager, appWidgetIds);
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		AppWidgetManager mgr = AppWidgetManager.getInstance(context);
		if (intent.getAction().equals(TOAST_ACTION)) {
			int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
					AppWidgetManager.INVALID_APPWIDGET_ID);
			int viewIndex = intent.getIntExtra(EXTRA_BET_ID, 0);
			Toast.makeText(context, "Touched view " + viewIndex, Toast.LENGTH_SHORT).show();
		}

		super.onReceive(context, intent);
	}

	@Override
	public void onEnabled(Context context) {
		// Enter relevant functionality for when the first widget is created
	}

	@Override
	public void onDisabled(Context context) {
		// Enter relevant functionality for when the last widget is disabled
	}

	static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
								int appWidgetId) {
		Intent intent = new Intent(context, FavoriteBetsService.class);
		intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
		intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

		RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_favorite_bets);
		views.setRemoteAdapter(R.id.listView_favorite_bets, intent);
		views.setEmptyView(R.id.listView_favorite_bets, R.id.textView_empty_favorite_bets);

		Intent toastIntent = new Intent(context, FavoriteBetsWidgetProvider.class);
		toastIntent.setAction(FavoriteBetsWidgetProvider.TOAST_ACTION);
		toastIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
		intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
		PendingIntent toastPendingIntent = PendingIntent.getBroadcast(context, 0, toastIntent, PendingIntent.FLAG_UPDATE_CURRENT);

		views.setPendingIntentTemplate(R.id.listView_favorite_bets, toastPendingIntent);

		appWidgetManager.updateAppWidget(appWidgetId, views);
	}
}

