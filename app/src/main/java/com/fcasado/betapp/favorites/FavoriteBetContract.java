package com.fcasado.betapp.favorites;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by fcasado on 9/12/16.
 */
public class FavoriteBetContract {
	public static final String CONTENT_AUTHORITY = "com.fcasado.betapp";
	public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
	public static final String PATH_BET = "bet";

	public static final class BetEntry implements BaseColumns {
		public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_BET).build();
		public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_BET;

		public static final String TABLE_NAME = "bet";

		public static final String COLUMN_BET_ID = "bet_id";

		public static final String COLUMN_BET_TITLE = "bet_title";

		public static final String COLUMN_BET_DESCRIPTION = "bet_description";

		public static final String COLUMN_BET_HAS_FINISHED = "bet_has_finished";

		public static Uri buildBetUri(long id) {
			return ContentUris.withAppendedId(CONTENT_URI, id);
		}
	}
}
