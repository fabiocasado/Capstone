package com.fcasado.betapp.bets;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.fcasado.betapp.LogUtils;
import com.fcasado.betapp.R;
import com.fcasado.betapp.data.Bet;
import com.hannesdorfmann.mosby.mvp.MvpActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by fcasado on 6/24/16.
 */
public class BetsListActivity extends MvpActivity<BetsListView, BetsListPresenter> implements BetsListView, SwipeRefreshLayout.OnRefreshListener {
	private static final String TAG = "BetsListActivity";

	@BindView(R.id.recyclerView)
	RecyclerView recyclerView;
	@BindView(R.id.swiperefresh)
	SwipeRefreshLayout swipeRefreshLayout;

	private BetsAdapter adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bets_list);
		ButterKnife.bind(this);

		adapter = new BetsAdapter();
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
		loadBets();
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
	}

	@Override
	public void showLoadBetsFailed() {
		adapter.clearBets();
		Toast.makeText(this, "Error loading bets", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onRefresh() {
		LogUtils.d(TAG, "onRefresh called from SwipeRefreshLayout");
		loadBets();
	}
}
