package com.fcasado.betapp.create;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Button;
import android.widget.DatePicker;

import com.fcasado.betapp.R;
import com.hannesdorfmann.mosby.mvp.MvpActivity;

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
	private DatePickerDialog startDatePicker;
	private DatePickerDialog endDatePicker;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_bet);
		ButterKnife.bind(this);
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


	@OnClick(R.id.button_start_date)
	public void onStartDatePressed() {
		if (startDatePicker == null) {
			Calendar c = GregorianCalendar.getInstance();
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH);
			int day = c.get(Calendar.DAY_OF_MONTH);

			startDatePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
				@Override
				public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
					presenter.selectStartDate(year, monthOfYear, dayOfMonth);
					startDatePicker = null;
				}
			}, year, month, day);
		}

		startDatePicker.show();
	}

	@OnClick(R.id.button_end_date)
	public void onEndDatePressed() {
		if (endDatePicker == null) {
			Calendar c = GregorianCalendar.getInstance();
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH);
			int day = c.get(Calendar.DAY_OF_MONTH);

			endDatePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
				@Override
				public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
					presenter.selectEndDate(year, monthOfYear, dayOfMonth);
					endDatePicker = null;
				}
			}, year, month, day);
		}

		endDatePicker.show();
	}
}