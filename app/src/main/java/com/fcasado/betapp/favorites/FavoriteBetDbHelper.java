package com.fcasado.betapp.favorites;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.fcasado.betapp.favorites.FavoriteBetContract.BetEntry;

/**
 * Created by fcasado on 9/12/16.
 */
public class FavoriteBetDbHelper extends SQLiteOpenHelper {
	static final String DATABASE_NAME = "bets.db";
	private static final int DATABASE_VERSION = 1;

	public FavoriteBetDbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		final String SQL_CREATE_FAVORITE_BET_TABLE = "CREATE TABLE " + BetEntry.TABLE_NAME + " ("
				+ BetEntry._ID + " INTEGER PRIMARY KEY," + BetEntry.COLUMN_BET_ID
				+ " TEXT NOT NULL, " + BetEntry.COLUMN_BET_TITLE + " TEXT NOT NULL, "
				+ BetEntry.COLUMN_BET_DESCRIPTION + " TEXT, "
				+ BetEntry.COLUMN_BET_HAS_FINISHED + " INTEGER " + ");";

		db.execSQL(SQL_CREATE_FAVORITE_BET_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
		// We are not implementing an upgrade mechanism right now, nor are we foreseeing a DB upgrade right now or DB schema change.
	}
}
