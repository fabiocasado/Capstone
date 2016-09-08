package com.fcasado.betapp.participants;

import com.fcasado.betapp.data.User;
import com.hannesdorfmann.mosby.mvp.MvpView;

import java.util.List;

/**
 * Created by fcasado on 9/8/16.
 */
public interface ParticipantsView extends MvpView {
	void loadParticipants();

	void showParticipants(List<User> participants);

	void showLoadParticipantsFailed();
}
