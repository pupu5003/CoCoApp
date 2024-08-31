package com.example.cocoapp.Object;

public class AllergyDetail {
	private String name;
	private String petName; // This is the description in your context
	private String veterinarian;
	private String date;

	public AllergyDetail(String name, String petName, String veterinarian, String date) {
		this.name = name;
		this.petName = petName;
		this.veterinarian = veterinarian;
		this.date = date;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPetName() {
		return petName;
	}

	public void setPetName(String petName) {
		this.petName = petName;
	}

	public String getVeterinarian() {
		return veterinarian;
	}

	public void setVeterinarian(String veterinarian) {
		this.veterinarian = veterinarian;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
}
