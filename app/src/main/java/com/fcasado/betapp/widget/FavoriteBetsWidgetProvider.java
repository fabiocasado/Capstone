package com.fcasado.betapp.widget;

import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.fcasado.betapp.R;
import com.fcasado.betapp.details.BetDetailsActivity;

/**
 * Implementation of App Widget functionality.
 */
public class FavoriteBetsWidgetProvider extends AppWidgetProvider {
	public static final String EXTRA_BET_ID = "com.fcasado.betapp.EXTRA_BET_ID";
	public static final String OPEN_BET_ACTION = "com.fcasado.betapp.OPEN_BET_ACTION";

	static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
								int appWidgetId) {
		Intent intent = new Intent(context, FavoriteBetsService.class);
		intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);

		RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_favorite_bets);
		views.setRemoteAdapter(R.id.listView_favorite_bets, intent);
		views.setEmptyView(R.id.listView_favorite_bets, R.id.textView_empty_favorite_bets);

		Intent toastIntent = new Intent(context, FavoriteBetsWidgetProvider.class);
		toastIntent.setAction(FavoriteBetsWidgetProvider.OPEN_BET_ACTION);
		toastIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
		PendingIntent toastPendingIntent = PendingIntent.getBroadcast(context, 0, toastIntent, PendingIntent.FLAG_UPDATE_CURRENT);

		views.setPendingIntentTemplate(R.id.listView_favorite_bets, toastPendingIntent);

		appWidgetManager.updateAppWidget(appWidgetId, views);
	}

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
		if (intent.getAction().equals(OPEN_BET_ACTION)) {
			String betId = intent.getStringExtra(EXTRA_BET_ID);
			if (betId == null) {
				Toast.makeText(context, R.string.invalid_favorite_bet, Toast.LENGTH_SHORT).show();
				return;
			}

			Intent activityIntent = new Intent(context, BetDetailsActivity.class);
			activityIntent.putExtra(BetDetailsActivity.EXTRA_BET_ID, betId);
			activityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

			TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
			stackBuilder.addParentStack(BetDetailsActivity.class);
			stackBuilder.addNextIntent(activityIntent);

			stackBuilder.startActivities();
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
}

