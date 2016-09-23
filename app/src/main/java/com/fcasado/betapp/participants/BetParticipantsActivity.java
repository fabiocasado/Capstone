package com.fcasado.betapp.participants;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.util.Pair;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.fcasado.betapp.utils.FirebaseUtils;
import com.fcasado.betapp.R;
import com.fcasado.betapp.data.Bet;
import com.fcasado.betapp.data.User;
import com.hannesdorfmann.mosby.mvp.MvpActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BetParticipantsActivity extends MvpActivity<ParticipantsView, ParticipantsPresenter>implements ParticipantsView, SwipeRefreshLayout.OnRefreshListener {
	private static final String TAG = "BetParticipantsActivity";
	public static final String EXTRA_CURRENT_BET = "extra_current_bet";
	public static final String EXTRA_BET_WINNERS = "extra_bet_winners";

	protected ParticipantsAdapter adapter;

	@BindView(R.id.recyclerView)
	RecyclerView recyclerView;
	@BindView(R.id.swiperefresh)
	SwipeRefreshLayout swipeRefreshLayout;

	private Bet bet;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friends);
		ButterKnife.bind(this);

		FirebaseUtils.logEvent(this, FirebaseUtils.PARTICIPANTS_SHOW_ACTIVITY, null);

		if (bet == null && getIntent().hasExtra(EXTRA_CURRENT_BET)) {
			bet = getIntent().getParcelableExtra(EXTRA_CURRENT_BET);
		}

		adapter = new ParticipantsAdapter(bet.getWinners());
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
		loadParticipants();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.participants_activity, menu);

		if (bet.getWinners() != null && bet.getWinners().size() > 0) {
			menu.findItem(R.id.winners).setVisible(false);
		}

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.winners:
				if (!adapter.canSelectWinner()) {
					adapter.setCanSelectWinner(true);
					item.setIcon(R.drawable.ic_save_white);
				} else {
					Intent data = new Intent();
					data.putStringArrayListExtra(EXTRA_BET_WINNERS, adapter.getSelectedWinners());
					setResult(RESULT_OK, data);
					finish();
				}
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@NonNull
	@Override
	public ParticipantsPresenter createPresenter() {
		return new ParticipantsPresenter();
	}

	@Override
	public void loadParticipants() {
		if (bet != null) {
			getPresenter().loadParticipants(bet.getParticipants());
		}
	}

	@Override
	public void showLoadParticipantsFailed() {
		adapter.clearPredictions();
		Toast.makeText(this, R.string.load_participants_failed, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onRefresh() {
		loadParticipants();
	}

	@Override
	public void showParticipants(List<User> participants) {
		List<Pair<User, String>> predictions = new ArrayList<>();

		if (bet != null) {
			List<String> betParticipants = bet.getParticipants();
			List<String> betPredictions = bet.getPredictions();

			// Add friends predictions
			for (int i = 0; i < participants.size(); i++) {
				int participantIndex = betParticipants.indexOf(participants.get(i).getUid());
				if (participantIndex >= 0) {
					predictions.add(Pair.create(participants.get(i), betPredictions.get(participantIndex)));
				}
			}
		}

		adapter.setPredictions(predictions);
		swipeRefreshLayout.setRefreshing(false);
	}

	@Override
	public Intent getParentActivityIntent() {
		return super.getParentActivityIntent().addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	}
}
