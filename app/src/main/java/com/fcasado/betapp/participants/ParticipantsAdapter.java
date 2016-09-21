package com.fcasado.betapp.participants;

import android.graphics.Color;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fcasado.betapp.R;
import com.fcasado.betapp.data.User;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by fcasado on 9/8/16.
 */
public class ParticipantsAdapter extends RecyclerView.Adapter<ParticipantsAdapter.ParticipantsHolder> {
	private List<Pair<User, String>> predictions;
	// Only useful is bet is already finished and winners have been declared
	private List<String> betWinners;
	private boolean canSelectWinner;
	private Set<String> selectedWinners;
	private OnItemClickListener onItemClickListener;
	public ParticipantsAdapter(List<String> betWinners) {
		predictions = new ArrayList<>();
		selectedWinners = new HashSet<>();
		setCanSelectWinner(false);

		if (betWinners == null) {
			betWinners = new ArrayList<>();
		}
		this.betWinners = betWinners;
	}

	public void setPredictions(List<Pair<User, String>> predictions) {
		this.predictions = predictions;
		notifyDataSetChanged();
	}

	public void clearPredictions() {
		predictions.clear();
		selectedWinners.clear();
		notifyDataSetChanged();
	}

	public boolean canSelectWinner() {
		return canSelectWinner;
	}

	public void setCanSelectWinner(boolean canSelectWinner) {
		this.canSelectWinner = canSelectWinner;
		onItemClickListener = new OnItemClickListener() {
			@Override
			public void onItemClick(User user) {
				if (ParticipantsAdapter.this.canSelectWinner) {
					if (selectedWinners.contains(user.getUid())) {
						selectedWinners.remove(user.getUid());
					} else {
						selectedWinners.add(user.getUid());
					}

					notifyDataSetChanged();
				}
			}
		};
	}

	public ArrayList<String> getSelectedWinners() {
		ArrayList<String> winners = new ArrayList<>(selectedWinners);
		return winners;
	}

	@Override
	public ParticipantsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		return new ParticipantsHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_participant_item, parent, false));
	}

	@Override
	public void onBindViewHolder(ParticipantsHolder holder, int position) {
		User user = predictions.get(position).first;
		String userPrediction = predictions.get(position).second;
		holder.name.setText(user.getName());
		holder.prediction.setText(userPrediction);
		holder.name.setTextColor(betWinners.contains(user.getUid()) || selectedWinners.contains(user.getUid()) ? Color.RED : Color.BLACK);
		holder.bindListener(user, onItemClickListener);
	}

	@Override
	public int getItemCount() {
		return predictions == null ? 0 : predictions.size();
	}

	public interface OnItemClickListener {
		void onItemClick(User user);
	}

	static class ParticipantsHolder extends RecyclerView.ViewHolder {
		@BindView(R.id.textView_name)
		TextView name;
		@BindView(R.id.textView_prediction)
		TextView prediction;
		@BindView(R.id.imageView_avatar)
		ImageView avatar;

		public ParticipantsHolder(View itemView) {
			super(itemView);
			ButterKnife.bind(this, itemView);
		}

		public void bindListener(final User user, final OnItemClickListener listener) {
			itemView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					listener.onItemClick(user);
				}
			});
			Glide.with(itemView.getContext())
					.load("https://graph.facebook.com/" + user.getFacebookId() + "/picture?type=large")
					.placeholder(R.drawable.ic_portrait_black)
					.crossFade()
					.fitCenter()
					.into(avatar);
		}
	}
}
