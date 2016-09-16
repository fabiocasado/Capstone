package com.fcasado.betapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.fcasado.betapp.favorites.FavoriteBetContract;

/**
 * Created by fcasado on 9/14/16.
 */
public class FavoriteBetsFactory implements RemoteViewsService.RemoteViewsFactory {
	private Context context;
	private Cursor cursor;
	private int columnId;
	private int columnBetId;
	private int columnTitle;
	private int columnDescription;

	public FavoriteBetsFactory(Context context) {
		this.context = context;
	}

	private Cursor queryFavoriteBets(Context context) {
		if (context != null) {
			return context.getContentResolver().query(
					FavoriteBetContract.BetEntry.CONTENT_URI,
					null,
					null,
					null,
					null);
		}

		return null;
	}

	@Override
	public void onCreate() {
		cursor = queryFavoriteBets(context);
		if (cursor != null) {
			columnId = cursor.getColumnIndex(FavoriteBetContract.BetEntry._ID);
			columnBetId = cursor.getColumnIndex(FavoriteBetContract.BetEntry.COLUMN_BET_ID);
			columnTitle = cursor.getColumnIndex(FavoriteBetContract.BetEntry.COLUMN_BET_TITLE);
			columnDescription = cursor.getColumnIndex(FavoriteBetContract.BetEntry.COLUMN_BET_DESCRIPTION);
		}
	}

	@Override
	public void onDataSetChanged() {
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}

		cursor = queryFavoriteBets(context);
	}

	@Override
	public void onDestroy() {
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
	}

	@Override
	public int getCount() {
		if (cursor == null || cursor.isClosed()) {
			return 0;
		}

		return cursor.getCount();
	}

	@Override
	public RemoteViews getViewAt(int position) {
		cursor.moveToPosition(position);

		RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_item);
		rv.setTextViewText(R.id.textView_title, cursor.getString(columnTitle));
		rv.setTextViewText(R.id.textView_description, cursor.getString(columnDescription));

		Bundle extras = new Bundle();
		extras.putString(FavoriteBetsWidgetProvider.EXTRA_BET_ID, cursor.getString(columnBetId));
		Intent fillInIntent = new Intent();
		fillInIntent.putExtras(extras);

		rv.setOnClickFillInIntent(R.id.widget_item, fillInIntent);

		return rv;
	}

	@Override
	public RemoteViews getLoadingView() {
		return null;
	}

	@Override
	public int getViewTypeCount() {
		return 1;
	}

	@Override
	public long getItemId(int i) {
		if (cursor != null && !cursor.isClosed()) {
			cursor.moveToPosition(i);
			return cursor.getLong(columnId);
		}

		return 0;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}
}
