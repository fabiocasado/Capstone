package com.fcasado.betapp.bets;

import android.content.Context;
import android.database.Cursor;

import com.fcasado.betapp.data.Bet;
import com.fcasado.betapp.favorites.FavoriteBetContract;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import java.util.List;

/**
 * Created by fcasado on 6/27/16.
 */
public class BetsListPresenter extends MvpBasePresenter<BetsListView> {
	private static final String TAG = "BetsListPresenter";

	private BetsListModel model;

	public BetsListPresenter() {
		model = new BetsListModel();
	}

	public void loadBets() {

		model.loadBets(new BetsListModel.LoadBetsListener() {
			@Override
			public void betsLoadFailed() {
				if (isViewAttached()) {
					getView().showLoadBetsFailed();
				}
			}

			@Override
			public void betsLoaded(List<Bet> betsList) {
				if (isViewAttached()) {
					getView().showBets(betsList);
				}
			}
		});
	}

	public List<String> loadFavoriteBets(Context context) {
		return model.loadFavoriteBets(context);
	}
}
