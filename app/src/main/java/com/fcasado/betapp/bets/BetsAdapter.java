package com.fcasado.betapp.bets;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
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
	public interface OnItemClickListener {
		void onItemClick(Bet bet);
	}

	private List<Bet> bets;
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

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_bet_item, parent, false));
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		if (bets.size() > position) {
			holder.bind(bets.get(position), onItemClickListener);
		}
	}

	@Override
	public int getItemCount() {
		return bets == null ? 0 : bets.size();
	}

	static class ViewHolder extends RecyclerView.ViewHolder {
		@BindView(R.id.textView_title)
		TextView title;
		@BindView(R.id.textView_description)
		TextView description;

		public ViewHolder(View itemView) {
			super(itemView);
			ButterKnife.bind(this, itemView);
		}

		public void bind(final Bet bet, final OnItemClickListener listener) {
			title.setText(bet.getTitle());
			description.setText(bet.getDescription());
			itemView.setOnClickListener(new View.OnClickListener() {
				@Override public void onClick(View v) {
					listener.onItemClick(bet);
				}
			});
		}
	}
}