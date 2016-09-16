package com.fcasado.betapp.details;

import com.fcasado.betapp.data.Bet;
import com.fcasado.betapp.data.BetDataView;
import com.hannesdorfmann.mosby.mvp.MvpView;

import java.util.List;

/**
 * Created by fcasado on 7/1/16.
 */
public interface BetDetailsView extends BetDataView {
	// show bet deleted
	void showBetDeleted();

	// show delete failed
	void showDeleteFailed();

	// show updated bet
	void showUpdatedDetails(Bet bet);

	// show loaded bet details
	void showLoadedBetDetails(Bet bet);

	// show load bet failed
	void showLoadBetFailed();

	// show update failed
	void showUpdateFailed();

	// show bet details
	void showDetails();

	// show add participants
	void showAddParticipants();

	// show bet participants
	void showBetParticipants();
}
