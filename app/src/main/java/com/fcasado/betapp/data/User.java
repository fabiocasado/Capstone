package com.fcasado.betapp.data;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by fcasado on 7/1/16.
 */
public class User {
	private String uid;
	private String facebookId;
	private String name;

	public User() {

	}

	public User(String uid, String facebookId, String name) {
		this.uid = uid;
		this.facebookId = facebookId;
		this.name = name;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getFacebookId() {
		return facebookId;
	}

	public void setFacebookId(String facebookId) {
		this.facebookId = facebookId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Exclude
	public Map<String, Object> toMap() {
		HashMap<String, Object> result = new HashMap<>();
		result.put("uid", uid);
		result.put("facebookId", facebookId);
		result.put("name", name);

		return result;
	}
}
