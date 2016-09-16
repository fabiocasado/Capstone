package com.fcasado.betapp.details;

import com.fcasado.betapp.data.Bet;
import com.fcasado.betapp.utils.Constants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fcasado on 7/1/16.
 */
public class BetDetailsModel {
	private static final String TAG = "BetDetailsModel";

	public interface LoadBetListener {
		void loadBetFailed();
		void loadedBet(Bet bet);
	}

	public interface UpdateDetailsListener {
		void updateDetailsFailed();
		void detailsUpdated(Bet bet);
	}

	public interface DeleteBetListener {
		void deleteBetFailed();
		void betDeleted();
	}

	public void loadBet(String betId, final LoadBetListener listener) {
		FirebaseDatabase database = FirebaseDatabase.getInstance();
		DatabaseReference betRef = database.getReference().child(Constants.CHILD_BETS).child(betId);
		betRef.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				Bet bet = dataSnapshot.getValue(Bet.class);
				listener.loadedBet(bet);
			}

			@Override
			public void onCancelled(DatabaseError databaseError) {
				listener.loadBetFailed();
			}
		});
	}

	public void deleteBet(Bet bet, final DeleteBetListener listener) {
		FirebaseDatabase database = FirebaseDatabase.getInstance();
		DatabaseReference betRef = database.getReference().child(Constants.CHILD_BETS).child(bet.getBetId());
		// Go through participants and delete bet from their list.
		for (String userId : bet.getParticipants()) {
			removeBetFromParticipants(userId, bet.getBetId());
		}

		// Delete bet
		betRef.removeValue(new DatabaseReference.CompletionListener() {
			@Override
			public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
				if (databaseError != null) {
					listener.deleteBetFailed();
					return;
				}

				listener.betDeleted();
			}
		});
	}

	public void updateDetails(final Bet bet, final UpdateDetailsListener listener) {
		FirebaseDatabase database = FirebaseDatabase.getInstance();
		DatabaseReference betsRef = database.getReference().child(Constants.CHILD_BETS);
		betsRef.child(bet.getBetId()).setValue(bet, new DatabaseReference.CompletionListener() {
			@Override
			public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
				if (databaseError == null) {
					updateBetParticipants(bet, listener);
					return;
				}

				listener.updateDetailsFailed();
			}
		});
	}

	private void updateBetParticipants(final Bet bet, final UpdateDetailsListener listener) {
		DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child(Constants.CHILD_USERS);
		List<String> participantsIds = bet.getParticipants();
		Map newPost = new HashMap();
		for (String userId : participantsIds) {
			newPost.put(userId + Constants.SEPARATOR + Constants.CHILD_BETS + Constants.SEPARATOR + bet.getBetId(), bet.getBetId());
		}
		usersRef.updateChildren(newPost, new DatabaseReference.CompletionListener() {
			@Override
			public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
				if (databaseError == null) {
					listener.detailsUpdated(bet);
					return;
				}

				listener.updateDetailsFailed();
			}
		});
	}

	private void removeBetFromParticipants(String userId, String betId) {
		DatabaseReference userBetsRef = FirebaseDatabase.getInstance().getReference().child(Constants.CHILD_USERS).child(userId).child(Constants.CHILD_BETS);
		userBetsRef.child(betId).removeValue();
	}

}
