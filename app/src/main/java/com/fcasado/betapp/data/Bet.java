package com.fcasado.betapp.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fcasado on 6/22/16.
 */
public class Bet implements Parcelable {
	private String betId;
	private String authorId;
	private String title;
	private String description;
	private long startDate;
	private long endDate;
	private String reward;

	private List<String> participants;

	public Bet() {

	}

	public Bet(String betId, String authorId, String title, String description, long startDate, long endDate, String reward) {
		this.betId = betId;
		this.authorId = authorId;
		this.title = title;
		this.description = description;
		this.startDate = startDate;
		this.endDate = endDate;
		this.reward = reward;
		participants = new ArrayList<>();
		participants.add(authorId);
	}

	@Override
	public String toString() {
		return "Bet{" +
				"betId='" + betId + '\'' +
				", authorId='" + authorId + '\'' +
				", title='" + title + '\'' +
				", description='" + description + '\'' +
				", startDate=" + startDate +
				", endDate=" + endDate +
				", reward='" + reward + '\'' +
				", participants=" + participants +
				'}';
	}

	public String getBetId() {
		return betId;
	}

	public void setBetId(String betId) {
		this.betId = betId;
	}

	public String getAuthorId() {
		return authorId;
	}

	public void setAuthorId(String authorId) {
		this.authorId = authorId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public long getStartDate() {
		return startDate;
	}

	public void setStartDate(long startDate) {
		this.startDate = startDate;
	}

	public long getEndDate() {
		return endDate;
	}

	public void setEndDate(long endDate) {
		this.endDate = endDate;
	}

	public String getReward() {
		return reward;
	}

	public void setReward(String reward) {
		this.reward = reward;
	}

	public List<String> getParticipants() {
		return participants;
	}

	public void setParticipants(List<String> participants) {
		this.participants = participants;
	}

	@Exclude
	public Map<String, Object> toMap() {
		HashMap<String, Object> result = new HashMap<>();
		result.put("betId", betId);
		result.put("authorId", authorId);
		result.put("title", title);
		result.put("description", description);
		result.put("startDate", startDate);
		result.put("endDate", endDate);
		result.put("reward", reward);
		result.put("participants", participants);

		return result;
	}

	// Parcelable implementation
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(betId);
		dest.writeString(authorId);
		dest.writeString(title);
		dest.writeString(description);
		dest.writeLong(startDate);
		dest.writeLong(endDate);
		dest.writeString(reward);
		dest.writeList(participants);
	}

	public static final Parcelable.Creator<Bet> CREATOR
			= new Parcelable.Creator<Bet>() {
		public Bet createFromParcel(Parcel in) {
			return new Bet(in);
		}

		public Bet[] newArray(int size) {
			return new Bet[size];
		}
	};

	private Bet(Parcel in) {
		betId = in.readString();
		authorId = in.readString();
		title = in.readString();
		description = in.readString();
		startDate = in.readLong();
		endDate = in.readLong();
		reward = in.readString();
		participants = in.readArrayList(String.class.getClassLoader());
	}
}
