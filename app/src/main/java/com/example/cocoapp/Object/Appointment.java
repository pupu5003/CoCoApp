package com.example.cocoapp.Object;

public class Appointment {
	private long time;
	private String vetId;
	private String catergory;

	// Constructor
	public Appointment(long time, String vetId, String catergory) {
		this.time = time;
		this.vetId = vetId;
		this.catergory = catergory;
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
		return catergory;
	}

	public void setCatergory(String catergory) {
		this.catergory = catergory;
	}
}
