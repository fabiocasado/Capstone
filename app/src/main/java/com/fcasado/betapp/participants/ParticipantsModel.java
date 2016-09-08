package com.fcasado.betapp.participants;

import com.fcasado.betapp.data.Constants;
import com.fcasado.betapp.data.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fcasado on 9/8/16.
 */
public class ParticipantsModel {
	private static final String TAG = "ParticipantsModel";

	public void loadParticipants(List<String> participantsIds, final LoadParticipantsListener listener) {
		convertFacebookIdsToUserIds(participantsIds, listener);
	}

	private void convertFacebookIdsToUserIds(List<String> participantsIds, final LoadParticipantsListener listener) {
		final List<User> participants = new ArrayList<>();
		final int friendsCount = participantsIds.size();
		if (friendsCount == 0) {
			listener.participantsLoaded(participants);
			return;
		}

		for (int i = 0; i < participantsIds.size(); i++) {
			getUserDataFromUserId(friendsCount, participants, participantsIds.get(i), listener);
		}
	}

	private void getUserDataFromUserId(final int totalUsers, final List<User> users, String userId, final LoadParticipantsListener listener) {
		DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child(Constants.CHILD_USERS).child(userId);
		usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				users.add(dataSnapshot.getValue(User.class));

				if (users.size() == totalUsers && listener != null) {
					listener.participantsLoaded(users);
				}
			}

			@Override
			public void onCancelled(DatabaseError databaseError) {
				if (listener != null) {
					listener.participantsLoadFailed();
				}
			}
		});
	}

	public interface LoadParticipantsListener {
		void participantsLoadFailed();

		void participantsLoaded(List<User> participants);
	}
}
