package com.fcasado.betapp.data;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by fcasado on 6/22/16.
 */
public class Bet {
	private String betId;
	private String authorId;
	private String title;
	private String description;
	private long startDate;
	private long endDate;
	private String reward;

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
	}

	@Override
	public String toString() {
		return "Bet{" +
				"betId='" + betId + '\'' +
				"authorId='" + authorId + '\'' +
				", title='" + title + '\'' +
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

		return result;
	}
}
