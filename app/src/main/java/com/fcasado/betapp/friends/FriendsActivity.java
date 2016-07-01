package com.fcasado.betapp.friends;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.fcasado.betapp.LogUtils;
import com.fcasado.betapp.R;
import com.hannesdorfmann.mosby.mvp.MvpActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FriendsActivity extends MvpActivity<FriendsView, FriendsPresenter> implements FriendsView, SwipeRefreshLayout.OnRefreshListener {
	private static final String TAG = "FriendsActivity";

	@BindView(R.id.recyclerView)
	RecyclerView recyclerView;
	@BindView(R.id.swiperefresh)
	SwipeRefreshLayout swipeRefreshLayout;

	private FriendsAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friends);
		ButterKnife.bind(this);

		adapter = new FriendsAdapter();
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
	public void showFriends(List<String> friends) {
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
