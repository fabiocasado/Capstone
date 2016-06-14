package com.fcasado.betapp.create;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import java.text.DateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by fcasado on 6/14/16.
 */
public class CreateBetPresenter extends MvpBasePresenter<CreateBetView> {
	public void selectStartDate(int year, int monthOfYear, int dayOfMonth) {
		if (isViewAttached()) {
			GregorianCalendar calendar = new GregorianCalendar(year, monthOfYear, dayOfMonth);
			DateFormat df = DateFormat.getDateInstance();
			getView().showStartDate(df.format(calendar.getTime()));
		}
	}

	public void selectEndDate(int year, int monthOfYear, int dayOfMonth) {
		if (isViewAttached()) {
			GregorianCalendar calendar = new GregorianCalendar(year, monthOfYear, dayOfMonth);
			DateFormat df = DateFormat.getDateInstance();
			getView().showEndDate(df.format(calendar.getTime()));
		}
	}
}
