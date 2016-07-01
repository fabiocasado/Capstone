package com.fcasado.betapp.friends;

import com.fcasado.betapp.data.Bet;
import com.hannesdorfmann.mosby.mvp.MvpView;

import java.util.List;

/**
 * Created by fcasado on 6/30/16.
 */
public interface FriendsView extends MvpView {
	// load friends
	void loadFriends();

	// show friends
	void showFriends(List<String> friends);

	// show load friends failed
	void showLoadFriendsFailed();
}
