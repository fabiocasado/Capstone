package com.fcasado.betapp.participants;

import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fcasado.betapp.R;
import com.fcasado.betapp.data.User;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by fcasado on 9/8/16.
 */
public class ParticipantsAdapter extends RecyclerView.Adapter<ParticipantsAdapter.ParticipantsHolder> {

	private List<Pair<User, String>> predictions;

	public ParticipantsAdapter() {
		predictions = new ArrayList<>();
	}

	public void setPredictions(List<Pair<User, String>> predictions) {
		this.predictions = predictions;
		notifyDataSetChanged();
	}

	public void clearPredictions() {
		if (predictions != null) {
			predictions.clear();
		}
		notifyDataSetChanged();
	}

	@Override
	public ParticipantsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		return new ParticipantsHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_participant_item, parent, false));
	}

	@Override
	public void onBindViewHolder(ParticipantsHolder holder, int position) {
		Pair<User, String> prediction = predictions.get(position);
		holder.name.setText(prediction.first.getName());
		holder.prediction.setText(prediction.second);
	}

	@Override
	public int getItemCount() {
		return predictions == null ? 0 : predictions.size();
	}

	static class ParticipantsHolder extends RecyclerView.ViewHolder {
		@BindView(R.id.textView_name)
		TextView name;
		@BindView(R.id.textView_prediction)
		TextView prediction;

		public ParticipantsHolder(View itemView) {
			super(itemView);
			ButterKnife.bind(this, itemView);
		}
	}
}
