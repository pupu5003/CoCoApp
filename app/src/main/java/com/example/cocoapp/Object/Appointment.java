package com.example.cocoapp.Object;

public class Appointment {

	private long time;
	private String vetId;
	private String category;
	private String id;
	private String type;
	

	// Constructor
	public Appointment(long time, String vetId, String catergory) {
		this.time = time;
		this.vetId = vetId;
		this.category = catergory;
	}
	

	// Getter and Setter for id
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getType() {return type;}

	public void setType(String type) { this.type = type;}
}
