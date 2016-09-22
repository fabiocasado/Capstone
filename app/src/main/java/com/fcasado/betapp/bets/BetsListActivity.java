package com.fcasado.betapp.bets;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.fcasado.betapp.BetApp;
import com.fcasado.betapp.LoginActivity;
import com.fcasado.betapp.R;
import com.fcasado.betapp.create.CreateBetActivity;
import com.fcasado.betapp.custom.LineDividerItemDecoration;
import com.fcasado.betapp.data.Bet;
import com.fcasado.betapp.details.BetDetailsActivity;
import com.fcasado.betapp.favorites.FavoriteBetContract;
import com.fcasado.betapp.friends.FriendsActivity;
import com.fcasado.betapp.utils.Constants;
import com.fcasado.betapp.utils.FirebaseUtils;
import com.fcasado.betapp.utils.NetworkUtils;
import com.hannesdorfmann.mosby.mvp.MvpActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by fcasado on 6/24/16.
 */
public class BetsListActivity extends MvpActivity<BetsListView, BetsListPresenter> implements BetsListView,
		SwipeRefreshLayout.OnRefreshListener, BetsAdapter.OnItemClickListener, LoaderManager.LoaderCallbacks<Cursor> {
	private static final String TAG = "BetsListActivity";
	private static final int LOADER_FAVORITE_BET = 10001;

	@BindView(R.id.recyclerView)
	RecyclerView recyclerView;
	@BindView(R.id.swiperefresh)
	SwipeRefreshLayout swipeRefreshLayout;
	@BindView(R.id.floatingActionButton)
	FloatingActionButton fab;

	private BetsAdapter adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bets_list);
		ButterKnife.bind(this);

		FirebaseUtils.logEvent(this, FirebaseUtils.BET_LISTS_ACTIVITY, null);

		// We won't change the UI for now, so we can disregard the return value
		NetworkUtils.checkOnlineAndNotifyIfNeeded(this);

		adapter = new BetsAdapter(this);
		recyclerView.setLayoutManager(new LinearLayoutManager(this));
		recyclerView.setAdapter(adapter);
		recyclerView.addItemDecoration(new LineDividerItemDecoration(this, 0));
		swipeRefreshLayout.setOnRefreshListener(this);

		getLoaderManager().initLoader(LOADER_FAVORITE_BET, null, this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.bet_list_activity, menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.friends_list:
				startActivity(new Intent(BetsListActivity.this, FriendsActivity.class));
				return true;
			case R.id.profile:
				Intent profileIntent = new Intent(BetsListActivity.this, LoginActivity.class);
				profileIntent.putExtra(LoginActivity.EXTRA_SHOW_PROFILE, true);
				startActivityForResult(profileIntent, Constants.REQUEST_CODE_PROFILE);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
			case Constants.REQUEST_CODE_PROFILE:
				if (BetApp.currentUser == null) {
					finish();
				}
				break;
			default:
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
		loadBets();
	}

	@Override
	public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
		Uri uri = FavoriteBetContract.BetEntry.CONTENT_URI;
		return new CursorLoader(this, uri, null, null, null, null);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		if (cursor != null && adapter != null) {
			List<String> favoriteBetIds = new ArrayList<>(cursor.getCount());
			int columnIndex = cursor.getColumnIndex(FavoriteBetContract.BetEntry.COLUMN_BET_ID);
			while (cursor.moveToNext()) {
				favoriteBetIds.add(cursor.getString(columnIndex));
			}

			cursor.close();
			adapter.setFavoriteBets(favoriteBetIds);
		}
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		if (adapter != null) {
			adapter.setFavoriteBets(null);
		}
	}

	@NonNull
	@Override
	public BetsListPresenter createPresenter() {
		return new BetsListPresenter();
	}

	@Override
	public void loadBets() {
		getPresenter().loadBets();
	}

	@Override
	public void showBets(List<Bet> betList) {
		adapter.setBets(betList);
		swipeRefreshLayout.setRefreshing(false);

		// Use of a loader wasn't really used much in the application, since we can use Firebase
		// database features + MVP model, but we are using it here to demonstrate it's usage.
		getLoaderManager().restartLoader(LOADER_FAVORITE_BET, null, this);
	}

	@Override
	public void showLoadBetsFailed() {
		adapter.clearBets();
		Toast.makeText(this, R.string.load_bets_failed, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onRefresh() {
		loadBets();
	}

	@Override
	public void onItemClick(Bet bet) {
		Intent intent = new Intent(this, BetDetailsActivity.class);
		intent.putExtra(BetDetailsActivity.EXTRA_BET, bet);
		startActivity(intent);
	}

	@OnClick(R.id.floatingActionButton)
	public void onFabClicked() {
		startActivity(new Intent(BetsListActivity.this, CreateBetActivity.class));
	}
}
