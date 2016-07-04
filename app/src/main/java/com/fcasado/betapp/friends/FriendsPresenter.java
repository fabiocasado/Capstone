package com.fcasado.betapp.friends;

import com.fcasado.betapp.data.User;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import java.util.List;

/**
 * Created by fcasado on 6/30/16.
 */
public class FriendsPresenter extends MvpBasePresenter<FriendsView> {
	private static final String TAG = "FriendsPresenter";

	private FriendsModel model;

	public FriendsPresenter() {
		model = new FriendsModel();
	}

	public void loadFriends() {
		model.loadFriends(new FriendsModel.LoadFriendsListener() {
			@Override
			public void friendsLoadFailed() {
				if (isViewAttached()) {
					getView().showLoadFriendsFailed();
				}
			}

			@Override
			public void friendsLoaded(List<User> friends) {
				if (isViewAttached()) {
					getView().showFriends(friends);
				}
			}
		});
	}
}
