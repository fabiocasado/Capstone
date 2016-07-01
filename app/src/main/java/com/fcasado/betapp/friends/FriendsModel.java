package com.fcasado.betapp.friends;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.fcasado.betapp.data.Bet;
import com.fcasado.betapp.data.Constants;
import com.fcasado.betapp.data.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fcasado on 6/30/16.
 */
public class FriendsModel {
	private static final String TAG = "FriendsModel";

	public void loadFriends(final LoadFriendsListener listener) {
		new GraphRequest(
				AccessToken.getCurrentAccessToken(),
				"/me/friends",
				null,
				HttpMethod.GET,
				new GraphRequest.Callback() {
					public void onCompleted(GraphResponse response) {
						convertFacebookIdsToUserIds(response, listener);
					}
				}
		).executeAsync();
	}

	private void convertFacebookIdsToUserIds(GraphResponse response, final LoadFriendsListener listener) {
		try {
			JSONArray data = response.getJSONObject().getJSONArray("data");

			final List<String> friends = new ArrayList<>();
			final int friendsCount = data.length();
			if (friendsCount == 0) {
				listener.friendsLoaded(friends);
				return;
			}

			for (int i = 0; i < data.length(); i++) {
				String friendId = data.getJSONObject(i).optString("id", null);
				DatabaseReference fbIdsRef = FirebaseDatabase.getInstance().getReference().child(Constants.CHILD_FACEBOOK).child(friendId);
				fbIdsRef.child(Constants.CHILD_USER_ID).addListenerForSingleValueEvent(new ValueEventListener() {
					@Override
					public void onDataChange(DataSnapshot dataSnapshot) {
						getUserDataFromUserId(friendsCount, friends, dataSnapshot.getValue().toString(), listener);
					}

					@Override
					public void onCancelled(DatabaseError databaseError) {
						listener.friendsLoadFailed();
					}
				});
			}
		} catch (JSONException e) {
			e.printStackTrace();
			if (listener != null) {
				listener.friendsLoadFailed();
			}
		}
	}

	private void getUserDataFromUserId(final int totalUsers, final List<String> users, String userId, final LoadFriendsListener listener) {
		DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child(Constants.CHILD_USERS).child(userId);
		usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				User user = dataSnapshot.getValue(User.class);
				users.add(user.getName());

				if (users.size() == totalUsers && listener != null) {
					listener.friendsLoaded(users);
				}
			}

			@Override
			public void onCancelled(DatabaseError databaseError) {
				if (listener != null) {
					listener.friendsLoaded(null);
				}
			}
		});
	}

	public interface LoadFriendsListener {
		void friendsLoadFailed();

		void friendsLoaded(List<String> friends);
	}
}
