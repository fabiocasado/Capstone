package com.fcasado.betapp.details;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fcasado.betapp.FirebaseUtils;
import com.fcasado.betapp.R;
import com.fcasado.betapp.data.Bet;
import com.fcasado.betapp.data.Constants;
import com.fcasado.betapp.data.User;
import com.fcasado.betapp.favorites.FavoriteBetContract;
import com.fcasado.betapp.friends.AddParticipantsActivity;
import com.fcasado.betapp.participants.BetParticipantsActivity;
import com.fcasado.betapp.friends.FriendsActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

	@BindView(R.id.textView_title)
	TextView textViewTitle;
	@BindView(R.id.textView_description)
	TextView textViewDescription;
	@BindView(R.id.textView_reward)
	TextView textViewReward;
	@BindView(R.id.button_add_participants)
	Button buttonAddParticipants;
	@BindView(R.id.editText_prediction)
	EditText editTextPrediction;

	private Bet bet;
	private boolean isFavorite;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bet_details);
		ButterKnife.bind(this);

		FirebaseUtils.logEvent(this, FirebaseUtils.BET_DETAILS_ACTIVITY, null);

		bet = getIntent().getParcelableExtra(EXTRA_BET);
		if (bet == null) {
			Toast.makeText(this, R.string.bet_data_corrupted, Toast.LENGTH_SHORT).show();
			finish();
		} else {
			showDetails();
		}

		setFavoriteStatus();
		setEditionEnabledStatus();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.bet_details_activity, menu);

		MenuItem deleteMenuItem = menu.findItem(R.id.delete);
		FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
		deleteMenuItem.setVisible(currentUser != null && currentUser.getUid().equalsIgnoreCase(bet.getAuthorId()));

		if (bet.hasFinished()) {
			MenuItem saveMenuItem = menu.findItem(R.id.save);
			saveMenuItem.setVisible(false);
		}

		MenuItem favoriteMenuItem = menu.findItem(R.id.favorite);
		if (isFavorite) {
			favoriteMenuItem.setIcon(R.drawable.ic_star_white);
		}

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.save:
				getPresenter().updateDetails(bet);
				return true;
			case R.id.favorite:
				if (!isFavorite) {
					ContentValues values = new ContentValues();
					values.put(FavoriteBetContract.BetEntry.COLUMN_BET_ID, bet.getBetId());
					values.put(FavoriteBetContract.BetEntry.COLUMN_BET_TITLE, bet.getTitle());
					values.put(FavoriteBetContract.BetEntry.COLUMN_BET_DESCRIPTION, bet.getDescription());
					values.put(FavoriteBetContract.BetEntry.COLUMN_BET_HAS_FINISHED, bet.hasFinished());
					getContentResolver().insert(
							FavoriteBetContract.BetEntry.CONTENT_URI,
							values);
				} else {
					getContentResolver().delete(
							FavoriteBetContract.BetEntry.CONTENT_URI,
							FavoriteBetContract.BetEntry.COLUMN_BET_ID + " = ?",
							new String[] {bet.getBetId()});
				}

				isFavorite = !isFavorite;
				item.setIcon(isFavorite ? R.drawable.ic_star_white : R.drawable.ic_star_border_white);
				return true;
			case R.id.delete:
				getPresenter().deleteBet(bet);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
			case Constants.REQUEST_CODE_SELECTION:
				if (resultCode == RESULT_OK) {
					if (data != null && data.hasExtra(FriendsActivity.EXTRA_SELECTION_RESULTS)) {
						List<User> selectedUsers = data.getParcelableArrayListExtra(FriendsActivity.EXTRA_SELECTION_RESULTS);
						for (User user : selectedUsers) {
							bet.getParticipants().add(user.getUid());
							bet.getPredictions().add("");
						}

						getPresenter().updateDetails(bet);
					}
				}
				break;
			case Constants.REQUEST_CODE_PARTICIPANTS:
				if (resultCode == RESULT_OK) {
					if (data != null && data.hasExtra(BetParticipantsActivity.EXTRA_BET_WINNERS)) {
						List<String> betWinners = data.getStringArrayListExtra(BetParticipantsActivity.EXTRA_BET_WINNERS);
						bet.setWinners(betWinners);
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
	public String getBetTitle() {
		return textViewTitle.getText().toString();
	}

	@Override
	public String getDescription() {
		return textViewDescription.getText().toString();
	}

	@Override
	public String getReward() {
		return textViewReward.getText().toString();
	}

	@Override
	public String getPrediction() {
		return editTextPrediction.getText().toString();
	}

	@Override
	public void showBetDeleted() {
		Toast.makeText(this, R.string.bet_deleted, Toast.LENGTH_SHORT).show();
		finish();
	}

	@Override
	public void showDeleteFailed() {
		Toast.makeText(this, R.string.bet_deletion_failed, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void showUpdatedDetails(Bet bet) {
		Toast.makeText(this, R.string.bet_updated, Toast.LENGTH_SHORT).show();
		this.bet = bet;
		showDetails();
		setEditionEnabledStatus();
	}

	@Override
	public void showUpdateFailed() {
		Toast.makeText(this, R.string.bet_update_failed, Toast.LENGTH_SHORT).show();
	}

	public void showDetails() {
		textViewTitle.setText(bet.getTitle());
		textViewDescription.setText(bet.getDescription());
		textViewReward.setText(bet.getReward());
		editTextPrediction.setText(bet.getPredictionForUser(FirebaseAuth.getInstance().getCurrentUser().getUid()));
		editTextPrediction.setEnabled(editTextPrediction.getText().length() == 0 && !bet.hasFinished());
	}

	@OnClick(R.id.button_add_participants)
	@Override
	public void showAddParticipants() {
		Intent intent = new Intent(this, AddParticipantsActivity.class);
		intent.putExtra(FriendsActivity.EXTRA_CURRENT_BET, bet);
		startActivityForResult(intent, Constants.REQUEST_CODE_SELECTION);
	}

	@OnClick(R.id.button_show_participants)
	@Override
	public void showBetParticipants() {
		Intent intent = new Intent(this, BetParticipantsActivity.class);
		intent.putExtra(FriendsActivity.EXTRA_CURRENT_BET, bet);
		startActivityForResult(intent, Constants.REQUEST_CODE_PARTICIPANTS);
	}

	private void setFavoriteStatus() {
		Cursor cursor = getContentResolver().query(
				FavoriteBetContract.BetEntry.CONTENT_URI,
				null,
				null,
				null,
				null);
		int columnIndex = cursor.getColumnIndex(FavoriteBetContract.BetEntry.COLUMN_BET_ID);
		while (cursor.moveToNext()) {
			if (cursor.getString(columnIndex).equalsIgnoreCase(bet.getBetId())) {
				isFavorite = true;
				break;
			}
		}

		cursor.close();
	}

	private void setEditionEnabledStatus() {
		FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
		boolean isEnabled = !bet.hasFinished() && currentUser != null && currentUser.getUid().equalsIgnoreCase(bet.getAuthorId());
		buttonAddParticipants.setVisibility(isEnabled ? View.VISIBLE : View.GONE);
	}
}
