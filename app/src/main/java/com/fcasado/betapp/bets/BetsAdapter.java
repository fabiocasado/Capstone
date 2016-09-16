package com.fcasado.betapp.bets;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fcasado.betapp.R;
import com.fcasado.betapp.data.Bet;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by fcasado on 6/25/16.
 */
public class BetsAdapter extends RecyclerView.Adapter<BetsAdapter.ViewHolder> {
	private List<Bet> bets;
	private List<String> favoriteBetIds;
	private OnItemClickListener onItemClickListener;

	public BetsAdapter(OnItemClickListener onItemClickListener) {
		bets = new ArrayList<>();
		this.onItemClickListener = onItemClickListener;
	}

	public void clearBets() {
		this.bets.clear();
		notifyDataSetChanged();
	}

	public void setBets(List<Bet> bets) {
		this.bets = bets;
		notifyDataSetChanged();
	}

	public void setFavoriteBets(List<String> favoriteBetIds) {
		this.favoriteBetIds = favoriteBetIds;
		notifyDataSetChanged();
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_bet_item, parent, false));
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		if (bets.size() > position) {
			boolean isFavorite = favoriteBetIds != null && favoriteBetIds.contains(bets.get(position).getBetId());
			holder.bind(bets.get(position), onItemClickListener, isFavorite);
		}
	}

	@Override
	public int getItemCount() {
		return bets == null ? 0 : bets.size();
	}

	public interface OnItemClickListener {
		void onItemClick(Bet bet);
	}

	static class ViewHolder extends RecyclerView.ViewHolder {
		@BindView(R.id.textView_title)
		TextView title;
		@BindView(R.id.textView_description)
		TextView description;
		@BindView(R.id.imageView_favorite)
		ImageView favorite;

		public ViewHolder(View itemView) {
			super(itemView);
			ButterKnife.bind(this, itemView);
		}

		public void bind(final Bet bet, final OnItemClickListener listener, boolean isFavoriteBet) {
			title.setText(bet.getTitle());
			description.setText(bet.getDescription());
			favorite.setVisibility(isFavoriteBet ? View.VISIBLE : View.GONE);
			itemView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					listener.onItemClick(bet);
				}
			});
		}
	}
}
