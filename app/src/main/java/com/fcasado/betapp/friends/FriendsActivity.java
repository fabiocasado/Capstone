package com.fcasado.betapp.friends;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.share.model.AppInviteContent;
import com.facebook.share.widget.AppInviteDialog;
import com.fcasado.betapp.R;
import com.fcasado.betapp.custom.LineDividerItemDecoration;
import com.fcasado.betapp.data.User;
import com.fcasado.betapp.utils.Constants;
import com.hannesdorfmann.mosby.mvp.MvpActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FriendsActivity extends MvpActivity<FriendsView, FriendsPresenter> implements FriendsView, SwipeRefreshLayout.OnRefreshListener {
	public static final String EXTRA_CURRENT_BET = "extra_current_bet";
	public static final String EXTRA_SELECTION_RESULTS = "extra_selection_results";
	private static final String TAG = "FriendsActivity";

	protected FriendsAdapter adapter;

	@BindView(R.id.recyclerView)
	RecyclerView recyclerView;
	@BindView(R.id.swiperefresh)
	SwipeRefreshLayout swipeRefreshLayout;
	@BindView(R.id.textView_empty)
	TextView emptyTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friends);
		ButterKnife.bind(this);

		adapter = new FriendsAdapter(shouldAllowSelection());
		recyclerView.setLayoutManager(new LinearLayoutManager(this));
		recyclerView.setAdapter(adapter);
		recyclerView.addItemDecoration(new LineDividerItemDecoration(this, 0));
		swipeRefreshLayout.setOnRefreshListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.friends_list_activity, menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.invite_friends:
				String appLinkUrl = Constants.FB_APP_INVITE;

				if (AppInviteDialog.canShow()) {
					AppInviteContent content = new AppInviteContent.Builder()
							.setApplinkUrl(appLinkUrl)
							.build();
					AppInviteDialog.show(this, content);
				}
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
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

		emptyTextView.setVisibility(friends.size() == 0 ? View.VISIBLE : View.GONE);
	}

	@Override
	public void showLoadFriendsFailed() {
		adapter.clearFriends();
		Toast.makeText(this, R.string.load_friends_failed, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onRefresh() {
		loadFriends();
	}
}
