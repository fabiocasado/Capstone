package com.fcasado.betapp.create;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.fcasado.betapp.R;
import com.hannesdorfmann.mosby.mvp.MvpActivity;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by fcasado on 6/14/16.
 */
public class CreateBetActivity extends MvpActivity<CreateBetView, CreateBetPresenter> implements CreateBetView {
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
	private DatePickerDialog startDatePicker;
	private DatePickerDialog endDatePicker;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_bet);
		ButterKnife.bind(this);

		initStartDatePicker();
		initEndDatePicker();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		startDatePicker = null;
		endDatePicker = null;
	}

	@NonNull
	@Override
	public CreateBetPresenter createPresenter() {
		return new CreateBetPresenter();
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
		return null;
	}

	@OnClick(R.id.button_start_date)
	public void onStartDatePressed() {
		startDatePicker.show();
	}

	@OnClick(R.id.button_end_date)
	public void onEndDatePressed() {
		endDatePicker.show();
	}

	@OnClick(R.id.button_create)
	public void onCreatePressed() {
		findViewById(R.id.button_create).setClickable(false);
		presenter.createDatePressed();
	}

	@Override
	public void onBetCreated(String error) {
		findViewById(R.id.button_create).setClickable(true);
		if (error != null) {
			Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
		} else {
			finish();
		}
	}

	private void initStartDatePicker() {
		Calendar c = GregorianCalendar.getInstance();
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
		c.add(Calendar.DAY_OF_MONTH, 1);
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