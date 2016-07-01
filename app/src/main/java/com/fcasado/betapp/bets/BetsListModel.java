package com.fcasado.betapp.bets;

import com.fcasado.betapp.data.Bet;
import com.fcasado.betapp.data.Constants;
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
 * Created by fcasado on 6/27/16.
 */
public class BetsListModel {
	private static final String TAG = "BetsListModel";

	public interface LoadBetsListener {
		void betsLoadFailed();

		void betsLoaded(List<Bet> betsList);
	}

	public void loadBets(final LoadBetsListener listener) {
		FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
		if (currentUser == null) {
			return;
		}

		DatabaseReference database = FirebaseDatabase.getInstance().getReference().child(Constants.CHILD_USERS).child(currentUser.getUid());
		database.child(Constants.CHILD_BETS).addListenerForSingleValueEvent(
				new ValueEventListener() {
					@Override
					public void onDataChange(DataSnapshot dataSnapshot) {
						final List<Bet> betsList = new ArrayList<>();
						final long totalBets = dataSnapshot.getChildrenCount();

						if (totalBets == 0) {
							listener.betsLoaded(betsList);
							return;
						}

						for (DataSnapshot data : dataSnapshot.getChildren()) {
							FirebaseDatabase.getInstance().getReference().child(Constants.CHILD_BETS).child(data.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
								@Override
								public void onDataChange(DataSnapshot dataSnapshot) {
									if (dataSnapshot == null) {
										listener.betsLoadFailed();
										return;
									}

									betsList.add(dataSnapshot.getValue(Bet.class));
									if (betsList.size() == totalBets) {
										listener.betsLoaded(betsList);
									}
								}

								@Override
								public void onCancelled(DatabaseError databaseError) {
									listener.betsLoadFailed();
								}
							});
						}
					}

					@Override
					public void onCancelled(DatabaseError databaseError) {
						listener.betsLoadFailed();
					}
				});
	}
}
