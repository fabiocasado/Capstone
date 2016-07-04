package com.fcasado.betapp;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by fcasado on 7/1/16.
 */
public class DateStringUtils {
	public static String getDateString(long date) {
		DateFormat df = DateFormat.getDateInstance();
		return df.format(new Date(date));
	}
}
