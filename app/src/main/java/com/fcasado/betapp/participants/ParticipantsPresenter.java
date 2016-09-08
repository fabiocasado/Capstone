package com.fcasado.betapp.participants;

import com.fcasado.betapp.data.User;
import com.fcasado.betapp.friends.FriendsModel;
import com.fcasado.betapp.friends.FriendsView;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import java.util.List;

/**
 * Created by fcasado on 9/8/16.
 */
public class ParticipantsPresenter extends MvpBasePresenter<ParticipantsView> {
	private static final String TAG = "ParticipantsPresenter";

	private ParticipantsModel model;

	public ParticipantsPresenter() {
		model = new ParticipantsModel();
	}

	public void loadParticipants(List<String> participantIds) {
		model.loadParticipants(participantIds, new ParticipantsModel.LoadParticipantsListener() {
			@Override
			public void participantsLoadFailed() {
				if (isViewAttached()) {
					getView().showLoadParticipantsFailed();
				}
			}

			@Override
			public void participantsLoaded(List<User> participants) {
				if (isViewAttached()) {
					getView().showParticipants(participants);
				}
			}
		});
	}
}
