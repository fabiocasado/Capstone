package com.fcasado.betapp.bets;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.fcasado.betapp.R;
import com.fcasado.betapp.data.Bet;
import com.fcasado.betapp.data.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by fcasado on 6/24/16.
 */
public class BetsListActivity extends AppCompatActivity {
	private static final String TAG = "BetsListActivity";

	@BindView(R.id.recyclerView)
	RecyclerView recyclerView;

	private BetsAdapter adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bets_list);
		ButterKnife.bind(this);

		adapter = new BetsAdapter();
		recyclerView.setLayoutManager(new LinearLayoutManager(this));
		recyclerView.setAdapter(adapter);

		loadBets();
	}

	private void loadBets() {
		FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
		if (currentUser == null) {
			return;
		}

		DatabaseReference database = FirebaseDatabase.getInstance().getReference().child(Constants.CHILD_USERS).child(currentUser.getUid());
		database.child(Constants.CHILD_BETS).addListenerForSingleValueEvent(
				new ValueEventListener() {
					@Override
					public void onDataChange(DataSnapshot dataSnapshot) {
						final long totalBets = dataSnapshot.getChildrenCount();
						final List<Bet> bets = new ArrayList<>();
						for (DataSnapshot data : dataSnapshot.getChildren()) {
							FirebaseDatabase.getInstance().getReference().child(Constants.CHILD_BETS).child(data.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
								@Override
								public void onDataChange(DataSnapshot dataSnapshot) {
									bets.add(dataSnapshot.getValue(Bet.class));
									if (bets.size() == totalBets) {
										adapter.setBets(bets);
									}
								}

								@Override
								public void onCancelled(DatabaseError databaseError) {

								}
							});
						}
					}

					@Override
					public void onCancelled(DatabaseError databaseError) {
					}
				});
	}
}
