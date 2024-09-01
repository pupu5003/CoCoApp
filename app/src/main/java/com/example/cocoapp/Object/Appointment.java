package com.example.cocoapp.Object;

public class Appointment {

	private String veterinarian;
	private String date;
	private String hour;

	// Constructor
	public Appointment(String veterinarian, String date, String hour) {
		this.veterinarian = veterinarian;
		this.date = date;
		this.hour = hour;
	}

	// Getter and Setter for veterinarian
	public String getVeterinarian() {
		return veterinarian;
	}

	public void setVeterinarian(String veterinarian) {
		this.veterinarian = veterinarian;
	}

	// Getter and Setter for date
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}


	public String getHour() {
		return hour;
	}

	public void setHour(String hour) {
		this.hour = hour;
	}
}
