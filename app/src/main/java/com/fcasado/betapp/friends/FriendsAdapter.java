package com.fcasado.betapp.friends;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.ArraySet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fcasado.betapp.R;
import com.fcasado.betapp.data.Bet;
import com.fcasado.betapp.data.User;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by fcasado on 6/30/16.
 */
public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.FriendsHolder> {
	public interface OnItemClickListener {
		void onItemClick(User user);
	}

	private List<User> friends;
	private boolean isSelectable;
	private Set<User> selectedFriends;
	private OnItemClickListener onItemClickListener;

	public FriendsAdapter(boolean isListSelectable) {
		isSelectable = isListSelectable;
		friends = new ArrayList<>();
		selectedFriends = new HashSet<>();
		onItemClickListener = new OnItemClickListener() {
			@Override
			public void onItemClick(User user) {
				if (isSelectable) {
					if (selectedFriends.contains(user)) {
						selectedFriends.remove(user);
					} else {
						selectedFriends.add(user);
					}

					notifyDataSetChanged();
				}
			}
		};
	}

	public ArrayList<User> getSelectedUsers() {
		ArrayList<User> selectedUsers = new ArrayList<>(selectedFriends);
		return selectedUsers;
	}

	public void clearFriends() {
		friends.clear();
		selectedFriends.clear();
		notifyDataSetChanged();
	}

	public void setFriends(List<User> friends) {
		this.friends = friends;
		selectedFriends.clear();
		notifyDataSetChanged();
	}

	@Override
	public FriendsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		return new FriendsHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_friend_item, parent, false));
	}

	@Override
	public void onBindViewHolder(FriendsHolder holder, int position) {
		User user = friends.get(position);
		holder.name.setText(user.getName());
		holder.name.setTextColor(selectedFriends.contains(user) ? Color.RED : Color.BLACK);
		holder.bindListener(user, onItemClickListener);
	}

	@Override
	public int getItemCount() {
		return friends == null ? 0 : friends.size();
	}

	static class FriendsHolder extends RecyclerView.ViewHolder {
		@BindView(R.id.textView_name)
		TextView name;
		@BindView(R.id.imageView_avatar)
		ImageView avatar;

		public FriendsHolder(View itemView) {
			super(itemView);
			ButterKnife.bind(this, itemView);
		}

		public void bindListener(final User user, final OnItemClickListener listener) {
			itemView.setOnClickListener(new View.OnClickListener() {
				@Override public void onClick(View v) {
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
