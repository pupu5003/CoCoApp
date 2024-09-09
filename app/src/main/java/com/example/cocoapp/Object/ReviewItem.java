package com.example.cocoapp.Object;

import com.google.gson.annotations.SerializedName;

public class ReviewItem {

	@SerializedName("id")
	private String id;

	@SerializedName("type")
	private String type;

	@SerializedName("targetId")
	private String targetId;

	@SerializedName("detail")
	private String detail;

	@SerializedName("rating")
	private float rating;

	@SerializedName("userId")
	private String userId;

	@SerializedName("name")
	private String name;

	@SerializedName("time")
	private String time;

	@SerializedName("comment")
	private String comment;

	public String getId() { return id; }

	public void setId(String id) { this.id = id; }

	public String getType() { return type; }

	public void setType(String type) { this.type = type; }

	public String getTargetId() { return targetId; }

	public void setTargetId(String targetId) { this.targetId = targetId; }

	public String getDetail() { return detail; }

	public void setDetail(String detail) { this.detail = detail; }

	public float getRating() { return rating; }

	public void setRating(float rating) { this.rating = rating; }

	public String getUserId() { return userId; }

	public void setUserId(String userId) { this.userId = userId; }

	public String getName() { return name; }

	public void setName(String name) { this.name = name; }

	public String getTime() { return time; }

	public void setTime(String time) { this.time = time; }

	public String getComment() { return detail; }

	public void setComment(String comment) { this.detail = comment; }
}
