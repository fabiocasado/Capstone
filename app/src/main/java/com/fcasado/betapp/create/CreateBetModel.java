package com.fcasado.betapp.create;

import com.fcasado.betapp.LogUtils;
import com.fcasado.betapp.data.Bet;
import com.fcasado.betapp.data.Constants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by fcasado on 6/22/16.
 */
public class CreateBetModel {
	private static final String TAG = "CreateBetModel";

	public interface CreateBetListener {
		void createBetFailed();

		void createBetDone();
	}

	public void createBet(final String authorId, String title, String description, long startDate, long endDate, String reward, final CreateBetListener listener) {
		FirebaseDatabase database = FirebaseDatabase.getInstance();
		DatabaseReference betsRef = database.getReference().child(Constants.CHILD_BETS);

		final String betId = betsRef.push().getKey();
		Bet bet = new Bet(betId, authorId, title, description, startDate, endDate, reward);

		Map<String, Object> betValues = bet.toMap();
		Map<String, Object> childUpdates = new HashMap<>();
		childUpdates.put(Constants.SEPARATOR + betId, betValues);

		betsRef.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				if (dataSnapshot != null) {
					addBetToUser(authorId, betId, listener);
					return;
				}

				listener.createBetFailed();
			}

			@Override
			public void onCancelled(DatabaseError databaseError) {
				LogUtils.d(TAG, databaseError.getMessage());
				listener.createBetFailed();
			}
		});

		betsRef.updateChildren(childUpdates);
	}

	private void addBetToUser(String authorId, String betId, final CreateBetListener listener) {
		FirebaseDatabase database = FirebaseDatabase.getInstance();
		DatabaseReference userBetsRef = database.getReference().child(Constants.CHILD_USERS).child(authorId).child(Constants.CHILD_BETS);
		userBetsRef.child(betId).setValue(betId, new DatabaseReference.CompletionListener() {
			@Override
			public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
				if (databaseError == null) {
					listener.createBetDone();
					return;
				}

				listener.createBetFailed();
			}
		});
	}
}
