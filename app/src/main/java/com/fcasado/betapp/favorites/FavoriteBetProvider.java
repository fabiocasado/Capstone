package com.fcasado.betapp.favorites;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class FavoriteBetProvider extends ContentProvider {
	private static final int BET = 1000;
	private static final UriMatcher uriMatcher = buildUriMatcher();
	private FavoriteBetDbHelper mHelper;

	private static UriMatcher buildUriMatcher() {
		final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
		String authority = FavoriteBetContract.CONTENT_AUTHORITY;

		matcher.addURI(authority, FavoriteBetContract.PATH_BET, BET);
		return matcher;
	}

	@Override
	public boolean onCreate() {
		mHelper = new FavoriteBetDbHelper(getContext());
		return true;
	}

	@Override
	public String getType(Uri uri) {
		int match = uriMatcher.match(uri);
		switch (match) {
			case BET:
				return FavoriteBetContract.BetEntry.CONTENT_TYPE;
			default:
				throw new UnsupportedOperationException("Unknown uri: " + uri);
		}
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
						String[] selectionArgs, String sortOrder) {
		Cursor retCursor;
		String tableName;
		switch (uriMatcher.match(uri)) {
			case BET:
				tableName = FavoriteBetContract.BetEntry.TABLE_NAME;
				break;
			default:
				throw new UnsupportedOperationException("Unknown uri: " + uri);
		}

		retCursor = mHelper.getReadableDatabase().query(tableName, projection, selection, selectionArgs, null, null, sortOrder);
		retCursor.setNotificationUri(getContext().getContentResolver(), uri);
		return retCursor;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		SQLiteDatabase db = mHelper.getWritableDatabase();
		int match = uriMatcher.match(uri);
		Uri returnUri;

		switch (match) {
			case BET:
				long id = db.insertWithOnConflict(FavoriteBetContract.BetEntry.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
				if (id > 0) {
					returnUri = FavoriteBetContract.BetEntry.buildBetUri(id);
				} else {
					throw new android.database.SQLException("Failed to insert row into " + uri);
				}
				break;
			default:
				throw new UnsupportedOperationException("Unknown uri: " + uri);
		}

		getContext().getContentResolver().notifyChange(uri, null);
		return returnUri;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		SQLiteDatabase db = mHelper.getWritableDatabase();
		int match = uriMatcher.match(uri);
		String tableName;
		int rowsDeleted;

		switch (match) {
			case BET:
				tableName = FavoriteBetContract.BetEntry.TABLE_NAME;
				break;
			default:
				throw new UnsupportedOperationException("Unknown uri: " + uri);
		}

		rowsDeleted = db.delete(tableName, selection, selectionArgs);
		if (rowsDeleted != 0) {
			getContext().getContentResolver().notifyChange(uri, null);
		}

		return rowsDeleted;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
					  String[] selectionArgs) {
		SQLiteDatabase db = mHelper.getWritableDatabase();
		int match = uriMatcher.match(uri);
		String tableName;
		int rowsUpdated;

		switch (match) {
			case BET:
				tableName = FavoriteBetContract.BetEntry.TABLE_NAME;
				break;
			default:
				throw new UnsupportedOperationException("Unknown uri: " + uri);
		}

		rowsUpdated = db.update(tableName, values, selection, selectionArgs);
		if (rowsUpdated != 0) {
			getContext().getContentResolver().notifyChange(uri, null);
		}

		return rowsUpdated;
	}
}
