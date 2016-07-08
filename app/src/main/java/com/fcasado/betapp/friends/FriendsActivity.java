package com.fcasado.betapp.friends;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.fcasado.betapp.R;
import com.fcasado.betapp.data.User;
import com.hannesdorfmann.mosby.mvp.MvpActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FriendsActivity extends MvpActivity<FriendsView, FriendsPresenter> implements FriendsView, SwipeRefreshLayout.OnRefreshListener {
	public static final int REQUEST_CODE_SELECTION = 10001;
	public static final String EXTRA_CURRENT_BET = "extra_current_bet";
	public static final String EXTRA_SELECTION_RESULTS = "extra_selection_results";
	private static final String TAG = "FriendsActivity";

	protected FriendsAdapter adapter;
	@BindView(R.id.recyclerView)
	RecyclerView recyclerView;
	@BindView(R.id.swiperefresh)
	SwipeRefreshLayout swipeRefreshLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friends);
		ButterKnife.bind(this);

		adapter = new FriendsAdapter(shouldAllowSelection());
		recyclerView.setLayoutManager(new LinearLayoutManager(this));
		recyclerView.setAdapter(adapter);
		swipeRefreshLayout.setOnRefreshListener(this);
	}

	@Override
	protected void onStart() {
		super.onStart();
		swipeRefreshLayout.post(new Runnable() {
			@Override
			public void run() {
				// Workaround since indicator is not showing properly
				swipeRefreshLayout.setRefreshing(true);
			}
		});
		loadFriends();
		getPresenter().loadFriends();
	}

	protected boolean shouldAllowSelection() {
		return false;
	}

	@NonNull
	@Override
	public FriendsPresenter createPresenter() {
		return new FriendsPresenter();
	}

	@Override
	public void loadFriends() {
		getPresenter().loadFriends();
	}

	@Override
	public void showFriends(List<User> friends) {
		adapter.setFriends(friends);
		swipeRefreshLayout.setRefreshing(false);
	}

	@Override
	public void showLoadFriendsFailed() {
		adapter.clearFriends();
		Toast.makeText(this, "Error loading friends", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onRefresh() {
		loadFriends();
	}
}
