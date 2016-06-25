package com.fcasado.betapp.bets;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fcasado.betapp.R;
import com.fcasado.betapp.data.Bet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fcasado on 6/25/16.
 */
public class BetsAdapter extends RecyclerView.Adapter<BetsAdapter.BetsHolder> {
	private List<Bet> bets;

	public BetsAdapter() {
		bets = new ArrayList<>();
	}

	public void setBets(List<Bet> bets) {
		this.bets = bets;
		notifyDataSetChanged();
	}

	@Override
	public BetsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		return new BetsHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_bet_item, parent, false));
	}

	@Override
	public void onBindViewHolder(BetsHolder holder, int position) {
		holder.title.setText(bets.get(position).getTitle());
	}

	@Override
	public int getItemCount() {
		return bets == null ? 0 : bets.size();
	}

	static class BetsHolder extends RecyclerView.ViewHolder {
		public TextView title;

		public BetsHolder(View itemView) {
			super(itemView);
			title = (TextView) itemView.findViewById(R.id.title_textView);
		}
	}
}
