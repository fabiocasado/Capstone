package com.fcasado.betapp.data;

import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by fcasado on 7/14/16.
 */
public interface BetDataView extends MvpView {
	String getBetTitle();
	String getDescription();
	long getStartDate();
	long getEndDate();
	String getReward();
	String getPrediction();

	// displays start date
	void showStartDate(String date);

	// displays end date
	void showEndDate(String date);
}
