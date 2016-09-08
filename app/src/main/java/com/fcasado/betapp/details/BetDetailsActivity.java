package com.fcasado.betapp.details;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.fcasado.betapp.DateStringUtils;
import com.fcasado.betapp.R;
import com.fcasado.betapp.data.Bet;
import com.fcasado.betapp.data.User;
import com.fcasado.betapp.friends.AddParticipantsActivity;
import com.fcasado.betapp.participants.BetParticipantsActivity;
import com.fcasado.betapp.friends.FriendsActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hannesdorfmann.mosby.mvp.MvpActivity;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
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
	@BindView(R.id.button_add_participants)
	Button buttonAddParticipants;
	@BindView(R.id.editText_prediction)
	EditText editTextPrediction;
	private DatePickerDialog startDatePicker;
	private DatePickerDialog endDatePicker;

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

		FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
		setEditionEnabled(currentUser != null && currentUser.getUid().equalsIgnoreCase(bet.getAuthorId()));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.bet_details_activity, menu);

		MenuItem deleteMenuItem = menu.findItem(R.id.delete);
		FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
		deleteMenuItem.setVisible(currentUser != null && currentUser.getUid().equalsIgnoreCase(bet.getAuthorId()));

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.save:
				getPresenter().updateDetails(bet);
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

		if (requestCode == FriendsActivity.REQUEST_CODE_SELECTION) {
			if (resultCode == RESULT_OK) {
				if (data != null && data.hasExtra(FriendsActivity.EXTRA_SELECTION_RESULTS)) {
					List<User> selectedUsers = data.getParcelableArrayListExtra(FriendsActivity.EXTRA_SELECTION_RESULTS);
					for (User user : selectedUsers) {
						bet.getParticipants().add(user.getUid());
						bet.getPredictions().add("");
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
	public void showStartDate(String date) {
		buttonStartDate.setText(date);
	}

	@Override
	public void showEndDate(String date) {
		buttonEndDate.setText(date);
	}

	@Override
	public String getBetTitle() {
		return editTextTitle.getText().toString();
	}

	@Override
	public String getDescription() {
		return editTextDescription.getText().toString();
	}

	@Override
	public long getStartDate() {
		long startDate = Long.MIN_VALUE;
		if (startDatePicker != null) {
			DatePicker datePicker = startDatePicker.getDatePicker();
			GregorianCalendar calendar = new GregorianCalendar(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
			startDate = calendar.getTimeInMillis();
		}
		return startDate;
	}

	@Override
	public long getEndDate() {
		long endDate = Long.MIN_VALUE;
		if (endDatePicker != null) {
			DatePicker datePicker = endDatePicker.getDatePicker();
			GregorianCalendar calendar = new GregorianCalendar(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
			endDate = calendar.getTimeInMillis();
		}
		return endDate;
	}

	@Override
	public String getReward() {
		return editTextReward.getText().toString();
	}

	@Override
	public String getPrediction() {
		return editTextPrediction.getText().toString();
	}

	@OnClick(R.id.button_start_date)
	public void onStartDatePressed() {
		startDatePicker.show();
	}

	@OnClick(R.id.button_end_date)
	public void onEndDatePressed() {
		endDatePicker.show();
	}

	@Override
	public void showBetDeleted() {
		Toast.makeText(this, "Bet deleted", Toast.LENGTH_SHORT).show();
		finish();
	}

	@Override
	public void showDeleteFailed() {
		Toast.makeText(this, "Bet deletion failed. Please try again.", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void showUpdatedDetails(Bet bet) {
		Toast.makeText(this, "Bet updated", Toast.LENGTH_SHORT).show();
		this.bet = bet;
		showDetails();
	}

	@Override
	public void showUpdateFailed() {
		Toast.makeText(this, "Update failed. Please check your data and try again.", Toast.LENGTH_SHORT).show();
	}

	public void showDetails() {
		editTextTitle.setText(bet.getTitle());
		editTextDescription.setText(bet.getDescription());
		editTextReward.setText(bet.getReward());
		editTextPrediction.setText(bet.getPredictionForUser(FirebaseAuth.getInstance().getCurrentUser().getUid()));
		editTextPrediction.setEnabled(editTextPrediction.getText().length() == 0);
		buttonStartDate.setText(DateStringUtils.getDateString(bet.getStartDate()));
		buttonEndDate.setText(DateStringUtils.getDateString(bet.getEndDate()));
		initStartDatePicker();
		initEndDatePicker();
	}

	@OnClick(R.id.button_add_participants)
	@Override
	public void showAddParticipants() {
		Intent intent = new Intent(this, AddParticipantsActivity.class);
		intent.putExtra(FriendsActivity.EXTRA_CURRENT_BET, bet);
		startActivityForResult(intent, FriendsActivity.REQUEST_CODE_SELECTION);
	}

	@OnClick(R.id.button_show_participants)
	@Override
	public void showBetParticipants() {
		Intent intent = new Intent(this, BetParticipantsActivity.class);
		intent.putExtra(FriendsActivity.EXTRA_CURRENT_BET, bet);
		startActivity(intent);
	}

	private void setEditionEnabled(boolean isEnabled) {
		buttonStartDate.setClickable(isEnabled);
		buttonEndDate.setClickable(isEnabled);
		editTextTitle.setEnabled(isEnabled);
		editTextDescription.setEnabled(isEnabled);
		editTextReward.setEnabled(isEnabled);
		buttonAddParticipants.setVisibility(isEnabled ? View.VISIBLE : View.GONE);
	}

	private void initStartDatePicker() {
		Calendar c = GregorianCalendar.getInstance();
		c.setTimeInMillis(bet.getStartDate());
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);

		startDatePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				presenter.selectStartDate(year, monthOfYear, dayOfMonth);
			}
		}, year, month, day);

		DateFormat df = DateFormat.getDateInstance();
		showStartDate(df.format(c.getTime()));
	}

	private void initEndDatePicker() {
		Calendar c = GregorianCalendar.getInstance();
		c.setTimeInMillis(bet.getEndDate());
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);

		endDatePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				presenter.selectEndDate(year, monthOfYear, dayOfMonth);
			}
		}, year, month, day);

		DateFormat df = DateFormat.getDateInstance();
		showEndDate(df.format(c.getTime()));
	}
}
