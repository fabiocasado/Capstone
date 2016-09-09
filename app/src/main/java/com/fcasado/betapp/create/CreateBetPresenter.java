package com.fcasado.betapp.create;

import android.text.TextUtils;

import com.fcasado.betapp.LogUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import java.text.DateFormat;
import java.util.Calendar;
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

	public void createBetPressed() {
		if (!validateBetData()) {
			LogUtils.d(TAG, "Error validating bet");
			if (isViewAttached()) {
				getView().onBetCreated("Error validating bet");
			}
			return;
		}

		model.createBet(FirebaseAuth.getInstance().getCurrentUser().getUid(),
					getView().getBetTitle(),
					getView().getDescription(),
					getView().getReward(), new CreateBetModel.CreateBetListener() {
						@Override
						public void createBetFailed() {
							getView().onBetCreated("Error");
						}

						@Override
						public void createBetDone() {
							if (isViewAttached()) {
								getView().onBetCreated(null);
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
				&& !TextUtils.isEmpty(getView().getReward());
	}
}
