package com.fcasado.betapp.friends;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.fcasado.betapp.R;
import com.fcasado.betapp.data.Bet;
import com.fcasado.betapp.data.User;

import java.util.List;
import java.util.ListIterator;

public class AddParticipantsActivity extends FriendsActivity {
	private static final String TAG = "AddParticipantsActivity";

	private MenuItem saveMenuItem;

	@Override
	protected boolean shouldAllowSelection() {
		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.add_participants_activity, menu);

		saveMenuItem = menu.findItem(R.id.save);
		saveMenuItem.setVisible(false);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.save:
				Intent data = new Intent();
				data.putParcelableArrayListExtra(EXTRA_SELECTION_RESULTS, adapter.getSelectedUsers());
				setResult(RESULT_OK, data);
				finish();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
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

		if (saveMenuItem != null) {
			saveMenuItem.setVisible(friends != null && friends.size() != 0);
		}

		super.showFriends(friends);
	}
}
