package com.fcasado.betapp.create;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fcasado.betapp.R;
import com.hannesdorfmann.mosby.mvp.MvpActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by fcasado on 6/14/16.
 */
public class CreateBetActivity extends MvpActivity<CreateBetView, CreateBetPresenter> implements CreateBetView {
	@BindView(R.id.editText_title)
	EditText editTextTitle;
	@BindView(R.id.editText_description)
	EditText editTextDescription;
	@BindView(R.id.editText_reward)
	EditText editTextReward;

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
	public String getBetTitle() {
		return editTextTitle.getText().toString();
	}

	@Override
	public String getDescription() {
		return editTextDescription.getText().toString();
	}

	@Override
	public String getReward() {
		return editTextReward.getText().toString();
	}

	@Override
	public String getPrediction() {
		return null;
	}

	@OnClick(R.id.button_create)
	public void onCreatePressed() {
		findViewById(R.id.button_create).setClickable(false);
		presenter.createBetPressed();
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
}