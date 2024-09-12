package com.example.cocoapp.Object;

public class Appointment {
	private long time;
	private String vetId;
	private String userId;
	private String category;
	private String type;
	

	// Constructor
	public Appointment(long time, String vetId, String catergory, String type) {
		this.time = time;
		this.vetId = vetId;
		this.category = catergory;
		this.type = type;
	}

	public long getTime() {
		return time;
	}

	public void setTime(Integer time) {
		this.time = time;
	}

	public String getVetId() {
		return vetId;
	}

	public void setVetId(String vetId) {
		this.vetId = vetId;
	}

	public String getCatergory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
