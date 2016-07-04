package com.fcasado.betapp.friends;

import com.fcasado.betapp.data.Bet;
import com.fcasado.betapp.data.User;
import com.hannesdorfmann.mosby.mvp.MvpView;

import java.util.List;

/**
 * Created by fcasado on 6/30/16.
 */
public interface FriendsView extends MvpView {
	// load friends
	void loadFriends();

	// show friends
	void showFriends(List<User> friends);

	// show load friends failed
	void showLoadFriendsFailed();
}
