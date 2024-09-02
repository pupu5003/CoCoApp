package com.example.cocoapp.Object;

public class ReviewItem {
	private String name;
	private String time;
	private float rating;
	private String comment;

	// Constructor
	public ReviewItem(String name, String time, float rating, String comment) {
		this.name = name;
		this.time = time;
		this.rating = rating;
		this.comment = comment;
	}

	// Getters and Setters
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public float getRating() {
		return rating;
	}

	public void setRating(float rating) {
		this.rating = rating;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
}
