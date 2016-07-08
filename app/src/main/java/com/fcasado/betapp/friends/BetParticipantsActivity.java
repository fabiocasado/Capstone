package com.fcasado.betapp.friends;

import com.fcasado.betapp.data.Bet;
import com.fcasado.betapp.data.User;

import java.util.List;
import java.util.ListIterator;

public class BetParticipantsActivity extends FriendsActivity {
	private static final String TAG = "BetParticipantsActivity";

	@Override
	public void showFriends(List<User> friends) {
		if (getIntent().hasExtra(EXTRA_CURRENT_BET)) {
			Bet bet = getIntent().getParcelableExtra(EXTRA_CURRENT_BET);
			List<String> betParticipants = bet.getParticipants();
			ListIterator<User> iterator = friends.listIterator();
			while (iterator.hasNext()) {
				if (!betParticipants.contains(iterator.next().getUid())) {
					iterator.remove();
				}
			}
		}

		super.showFriends(friends);
	}
}
