package com.fcasado.betapp.details;

import android.support.annotation.NonNull;

import com.fcasado.betapp.data.Bet;
import com.fcasado.betapp.data.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fcasado on 7/1/16.
 */
public class BetDetailsModel {
	private static final String TAG = "BetDetailsModel";

	public interface LoadDetailsListener {
		void updateDetailsFailed();

		void detailsUpdated(Bet bet);
	}

	public void updateDetails(final Bet bet, final LoadDetailsListener listener) {
		final FirebaseDatabase database = FirebaseDatabase.getInstance();
		DatabaseReference betsRef = database.getReference().child(Constants.CHILD_BETS);
		betsRef.child(bet.getBetId()).setValue(bet, new DatabaseReference.CompletionListener() {
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

}
