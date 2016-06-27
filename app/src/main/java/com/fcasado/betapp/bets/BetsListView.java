package com.fcasado.betapp.bets;

import com.fcasado.betapp.data.Bet;
import com.hannesdorfmann.mosby.mvp.MvpView;

import java.util.List;

/**
 * Created by fcasado on 6/27/16.
 */
public interface BetsListView extends MvpView {
	// load bets
	void loadBets();

	// show bets
	void showBets(List<Bet> betList);

	// show load bets failed
	void showLoadBetsFailed();
}
