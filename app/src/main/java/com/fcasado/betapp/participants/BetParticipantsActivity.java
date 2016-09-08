package com.fcasado.betapp.participants;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.util.Pair;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.fcasado.betapp.R;
import com.fcasado.betapp.data.Bet;
import com.fcasado.betapp.data.User;
import com.fcasado.betapp.friends.FriendsPresenter;
import com.fcasado.betapp.friends.FriendsView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hannesdorfmann.mosby.mvp.MvpActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BetParticipantsActivity extends MvpActivity<ParticipantsView, ParticipantsPresenter>implements ParticipantsView, SwipeRefreshLayout.OnRefreshListener {
	private static final String TAG = "BetParticipantsActivity";
	public static final String EXTRA_CURRENT_BET = "extra_current_bet";

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

		if (bet == null && getIntent().hasExtra(EXTRA_CURRENT_BET)) {
			bet = getIntent().getParcelableExtra(EXTRA_CURRENT_BET);
		}

		adapter = new ParticipantsAdapter();
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
		Toast.makeText(this, "Error loading participants", Toast.LENGTH_SHORT).show();
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
}
