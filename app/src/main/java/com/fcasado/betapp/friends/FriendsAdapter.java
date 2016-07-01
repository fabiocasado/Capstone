package com.fcasado.betapp.friends;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fcasado.betapp.R;
import com.fcasado.betapp.data.Bet;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by fcasado on 6/30/16.
 */
public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.FriendsHolder> {
	private List<String> friends;

	public FriendsAdapter() {
		friends = new ArrayList<>();
	}

	public void clearFriends() {
		this.friends.clear();
		notifyDataSetChanged();
	}

	public void setFriends(List<String> friends) {
		this.friends = friends;
		notifyDataSetChanged();
	}

	@Override
	public FriendsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		return new FriendsHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_friend_item, parent, false));
	}

	@Override
	public void onBindViewHolder(FriendsHolder holder, int position) {
		holder.name.setText(friends.get(position));
	}

	@Override
	public int getItemCount() {
		return friends == null ? 0 : friends.size();
	}

	static class FriendsHolder extends RecyclerView.ViewHolder {
		@BindView(R.id.textView_name)
		TextView name;

		public FriendsHolder(View itemView) {
			super(itemView);
			ButterKnife.bind(this, itemView);
		}
	}
}
