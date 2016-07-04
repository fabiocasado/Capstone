package com.fcasado.betapp.friends;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.fcasado.betapp.LogUtils;
import com.fcasado.betapp.R;
import com.fcasado.betapp.data.Bet;
import com.fcasado.betapp.data.User;
import com.hannesdorfmann.mosby.mvp.MvpActivity;

import java.util.List;
import java.util.ListIterator;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FriendsActivity extends MvpActivity<FriendsView, FriendsPresenter> implements FriendsView, SwipeRefreshLayout.OnRefreshListener {
	private static final String TAG = "FriendsActivity";

	public static final int REQUEST_CODE_SELECTION = 10001;
	public static final String EXTRA_CURRENT_BET = "extra_current_bet";
	public static final String EXTRA_RETURN_SELECTION = "extra_return_selection";
	public static final String EXTRA_SELECTION_RESULTS = "extra_selection_results";

	@BindView(R.id.recyclerView)
	RecyclerView recyclerView;
	@BindView(R.id.swiperefresh)
	SwipeRefreshLayout swipeRefreshLayout;

	private FriendsAdapter adapter;
	private boolean shouldReturnSelection;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friends);
		ButterKnife.bind(this);

		shouldReturnSelection = getIntent().getBooleanExtra(EXTRA_RETURN_SELECTION, false);
		adapter = new FriendsAdapter(shouldReturnSelection);
		recyclerView.setLayoutManager(new LinearLayoutManager(this));
		recyclerView.setAdapter(adapter);
		swipeRefreshLayout.setOnRefreshListener(this);
	}

	@Override
	public void onBackPressed() {
		// TODO: This will go on a "SAVE" action button

		if (shouldReturnSelection) {
			Intent data = new Intent();
			data.putParcelableArrayListExtra(EXTRA_SELECTION_RESULTS, adapter.getSelectedUsers());
			setResult(RESULT_OK, data);
		}

		super.onBackPressed();
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
	public void showFriends(List<User> friends) {
		if (shouldReturnSelection) {
			if (getIntent().hasExtra(EXTRA_CURRENT_BET)) {
				Bet bet = getIntent().getParcelableExtra(EXTRA_CURRENT_BET);
				List<String> betParticipants = bet.getParticipants();
				ListIterator<User> iterator = friends.listIterator();
				while (iterator.hasNext()) {
					if (betParticipants.contains(iterator.next().getUid())) {
						iterator.remove();
					}
				}
			}
		}

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
