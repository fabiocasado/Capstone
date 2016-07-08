package com.fcasado.betapp.details;

import com.fcasado.betapp.data.Bet;
import com.hannesdorfmann.mosby.mvp.MvpView;

import java.util.List;

/**
 * Created by fcasado on 7/1/16.
 */
public interface BetDetailsView extends MvpView {
	// update details
	void updateDetails();

	// show updated bet
	void showUpdatedDetails(Bet bet);

	// show bet details
	void showDetails();

	// show bet details failed
	void showDetailsFailed();

	// show add participants
	void showAddParticipants();

	// show bet participants
	void showBetParticipants();
}
