package com.fcasado.betapp.details;

import com.fcasado.betapp.bets.BetsListModel;
import com.fcasado.betapp.bets.BetsListView;
import com.fcasado.betapp.data.Bet;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by fcasado on 7/1/16.
 */
public class BetDetailsPresenter extends MvpBasePresenter<BetDetailsView> {
	private static final String TAG = "BetDetailsPresenter";

	private BetDetailsModel model;

	public BetDetailsPresenter() {
		model = new BetDetailsModel();
	}

	public void updateDetails(Bet bet) {
		model.updateDetails(bet, new BetDetailsModel.LoadDetailsListener() {
			@Override
			public void updateDetailsFailed() {
			}

			@Override
			public void detailsUpdated(Bet bet) {
				if (isViewAttached()) {
					getView().showUpdatedDetails(bet);
				}
			}
		});
	}
}
