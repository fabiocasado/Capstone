package com.fcasado.betapp.details;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.fcasado.betapp.LogUtils;
import com.fcasado.betapp.bets.BetsListModel;
import com.fcasado.betapp.bets.BetsListView;
import com.fcasado.betapp.data.Bet;
import com.google.firebase.auth.FirebaseAuth;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import java.text.DateFormat;
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

	public void updateDetails(@NonNull Bet bet) {
		if (!validateBetData()) {
			LogUtils.d(TAG, "Error validating bet");
			if (isViewAttached()) {
				getView().showUpdateFailed();
			}
			return;
		}

		bet.setTitle(getView().getBetTitle());
		bet.setDescription(getView().getDescription());
		bet.setReward(getView().getReward());
		bet.setStartDate(bet.getStartDate());
		bet.setEndDate(bet.getEndDate());

		int indexOfPlayer = bet.getParticipants().indexOf(FirebaseAuth.getInstance().getCurrentUser().getUid());
		if (indexOfPlayer >= 0 && getView().getPrediction() != null) {
			bet.getPredictions().set(indexOfPlayer, getView().getPrediction());
		}

		model.updateDetails(bet, new BetDetailsModel.LoadDetailsListener() {
			@Override
			public void updateDetailsFailed() {
				if (isViewAttached()) {
					getView().showUpdateFailed();
				}
			}

			@Override
			public void detailsUpdated(Bet bet) {
				if (isViewAttached()) {
					getView().showUpdatedDetails(bet);
				}
			}
		});
	}

	public void deleteBet(@NonNull Bet bet) {
		model.deleteBet(bet, new BetDetailsModel.DeleteBetListener() {
			@Override
			public void deleteBetFailed() {
				if (isViewAttached()) {
					getView().showDeleteFailed();
				}
			}

			@Override
			public void betDeleted() {
				if (isViewAttached()) {
					getView().showBetDeleted();
				}
			}
		});
	}

	private boolean validateBetData() {
		return FirebaseAuth.getInstance().getCurrentUser() != null
				&& FirebaseAuth.getInstance().getCurrentUser().getUid() != null
				&& isViewAttached()
				&& !TextUtils.isEmpty(getView().getBetTitle())
				&& !TextUtils.isEmpty(getView().getDescription())
				&& getView().getStartDate() != Long.MIN_VALUE
				&& getView().getEndDate() != Long.MIN_VALUE
				&& !TextUtils.isEmpty(getView().getReward());
	}
}
