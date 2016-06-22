package com.fcasado.betapp.create;

import com.fcasado.betapp.LogUtils;
import com.fcasado.betapp.data.Bet;
import com.google.firebase.auth.FirebaseAuth;
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

	public void createBet(String authorId, String title, String description, long startDate, long endDate, String reward, final CreateBetListener listener) {
		// Write a message to the database
		FirebaseDatabase database = FirebaseDatabase.getInstance();
		DatabaseReference myRef = database.getReference();

		String betId = myRef.child("bets").push().getKey();
		Bet bet = new Bet(betId, authorId, title, description, startDate, endDate, reward);

		Map<String, Object> betValues = bet.toMap();
		Map<String, Object> childUpdates = new HashMap<>();
		childUpdates.put("/bets/" + betId, betValues);

		myRef.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				if (dataSnapshot != null) {
					listener.createBetDone();
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

		myRef.updateChildren(childUpdates);
	}
}
