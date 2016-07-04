package com.fcasado.betapp.details;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fcasado.betapp.DateStringUtils;
import com.fcasado.betapp.R;
import com.fcasado.betapp.data.Bet;
import com.fcasado.betapp.data.Constants;
import com.fcasado.betapp.data.User;
import com.fcasado.betapp.friends.FriendsActivity;
import com.hannesdorfmann.mosby.mvp.MvpActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by fcasado on 7/1/16.
 */
public class BetDetailsActivity extends MvpActivity<BetDetailsView, BetDetailsPresenter> implements BetDetailsView {
	public static final String EXTRA_BET = "extra_bet";
	public static final String EXTRA_BET_ID = "extra_bet_id";
	private static final String TAG = "BetsListActivity";

	@BindView(R.id.button_start_date)
	Button buttonStartDate;
	@BindView(R.id.button_end_date)
	Button buttonEndDate;
	@BindView(R.id.editText_title)
	EditText editTextTitle;
	@BindView(R.id.editText_description)
	EditText editTextDescription;
	@BindView(R.id.editText_reward)
	EditText editTextReward;

	private Bet bet;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bet_details);
		ButterKnife.bind(this);

		bet = getIntent().getParcelableExtra(EXTRA_BET);
		if (bet == null) {
			Toast.makeText(this, "Bet data corrupted. Please refresh bet list.", Toast.LENGTH_SHORT).show();
			finish();
		} else {
			showDetails();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.bet_details_activity, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.save:
				updateDetails();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == FriendsActivity.REQUEST_CODE_SELECTION) {
			if (resultCode == RESULT_OK) {
				if (data != null && data.hasExtra(FriendsActivity.EXTRA_SELECTION_RESULTS)) {
					List<User> selectedUsers = data.getParcelableArrayListExtra(FriendsActivity.EXTRA_SELECTION_RESULTS);
					for (User user : selectedUsers) {
						bet.getParticipants().add(user.getUid());
					}
				}
			}
		}
	}

	@NonNull
	@Override
	public BetDetailsPresenter createPresenter() {
		return new BetDetailsPresenter();
	}

	@Override
	public void updateDetails() {
		getPresenter().updateDetails(bet);
	}

	@Override
	public void showUpdatedDetails(Bet bet) {
		Toast.makeText(this, "Bet updated", Toast.LENGTH_SHORT).show();
		this.bet = bet;
		showDetails();
	}

	public void showDetails() {
		editTextTitle.setText(bet.getTitle());
		editTextDescription.setText(bet.getDescription());
		editTextReward.setText(bet.getReward());
		buttonStartDate.setText(DateStringUtils.getDateString(bet.getStartDate()));
		buttonEndDate.setText(DateStringUtils.getDateString(bet.getEndDate()));
	}

	@Override
	public void showDetailsFailed() {
		Toast.makeText(this, "Bet details couldn't be loaded.  Please refresh bet list.", Toast.LENGTH_SHORT).show();
		finish();
	}

	@OnClick(R.id.button_add_participants)
	@Override
	public void onAddParticipantsClicked() {
		Intent intent = new Intent(this, FriendsActivity.class);
		intent.putExtra(FriendsActivity.EXTRA_RETURN_SELECTION, true);
		intent.putExtra(FriendsActivity.EXTRA_CURRENT_BET, bet);
		startActivityForResult(intent, FriendsActivity.REQUEST_CODE_SELECTION);
	}
}
