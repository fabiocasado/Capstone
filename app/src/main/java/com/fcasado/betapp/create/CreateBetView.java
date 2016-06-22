package com.fcasado.betapp.create;

import com.hannesdorfmann.mosby.mvp.MvpView;

import java.util.Date;

/**
 * Created by fcasado on 6/14/16.
 */
public interface CreateBetView extends MvpView {
	// displays start date
	void showStartDate(String date);

	// displays end date
	void showEndDate(String date);

	// react to bet created
	void onBetCreated(String error);

	String getBetTitle();
	String getDescription();
	long getStartDate();
	long getEndDate();
	String getReward();

}
