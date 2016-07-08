package com.fcasado.betapp.friends;

import android.content.Intent;

import com.fcasado.betapp.data.Bet;
import com.fcasado.betapp.data.User;

import java.util.List;
import java.util.ListIterator;

public class AddParticipantsActivity extends FriendsActivity {
	private static final String TAG = "AddParticipantsActivity";

	@Override
	protected boolean shouldAllowSelection() {
		return true;
	}

	@Override
	public void onBackPressed() {
		// TODO: This will go on a "SAVE" action button
		Intent data = new Intent();
		data.putParcelableArrayListExtra(EXTRA_SELECTION_RESULTS, adapter.getSelectedUsers());
		setResult(RESULT_OK, data);

		super.onBackPressed();
	}

	@Override
	public void showFriends(List<User> friends) {
		if (getIntent().hasExtra(EXTRA_CURRENT_BET)) {
			Bet bet = getIntent().getParcelableExtra(EXTRA_CURRENT_BET);
			List<String> betParticipants = bet.getParticipants();
			ListIterator<User> iterator = friends.listIterator();
			while (iterator.hasNext()) {
				if (betParticipants.contains(iterator.next().getUid())) {
					iterator.remove();
				}
			}
		}

		super.showFriends(friends);
	}
}
