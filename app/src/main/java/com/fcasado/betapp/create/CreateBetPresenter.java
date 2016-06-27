package com.fcasado.betapp.create;

import com.fcasado.betapp.LogUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import java.text.DateFormat;
import java.util.GregorianCalendar;

/**
 * Created by fcasado on 6/14/16.
 */
public class CreateBetPresenter extends MvpBasePresenter<CreateBetView> {
	private static final String TAG = "CreateBetPresenter";

	private CreateBetModel model;

	public CreateBetPresenter() {
		model = new CreateBetModel();
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

	public void createDatePressed() {
		if (!validateBetData()) {
			LogUtils.d(TAG, "Error validating bet");
			return;
		}

		model.createBet(FirebaseAuth.getInstance().getCurrentUser().getUid(),
					getView().getBetTitle(),
					getView().getDescription(),
					getView().getStartDate(),
					getView().getEndDate(),
					getView().getReward(), new CreateBetModel.CreateBetListener() {
						@Override
						public void createBetFailed() {
							getView().onBetCreated("Error");
						}

						@Override
						public void createBetDone() {
							getView().onBetCreated(null);
						}
					});
	}

	private boolean validateBetData() {
		return FirebaseAuth.getInstance().getCurrentUser() != null
				&& FirebaseAuth.getInstance().getCurrentUser().getUid() != null
				&& getView() != null
				&& getView().getBetTitle() != null
				&& getView().getDescription() != null
				&& getView().getStartDate() != Long.MIN_VALUE
				&& getView().getEndDate() != Long.MIN_VALUE
				&& getView().getReward() != null;
	}
}
