package com.example.cocoapp.Object;

public class VaccinationDetail {

	private String name;
	private String date;
	private String veterinarian;
	private String petName;

	// Constructor
	public VaccinationDetail(String name, String date, String veterinarian, String petName) {
		this.name = name;
		this.date = date;
		this.veterinarian = veterinarian;
		this.petName = petName;
	}

	// Getters and Setters
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getVeterinarian() {
		return veterinarian;
	}

	public void setVeterinarian(String veterinarian) {
		this.veterinarian = veterinarian;
	}
	public String getPetName() {
		return petName;
	}

	public void setPetName(String petName) {
		this.petName = petName;
	}
}
