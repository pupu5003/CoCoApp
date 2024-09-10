package com.example.cocoapp.Object;

import java.util.Date;

public class Vaccination {
	private String id;
	private String name;
	private String date;
	private String veterinarian;

	// Constructor
	public Vaccination(String id, String name, String date, String veterinarian) {
		this.id = id;
		this.name = name;
		this.date = date;
		this.veterinarian = veterinarian;
	}
	public Vaccination(String id, String name, String veterinarian) {
		this.name = name;
		this.date = date;
		this.veterinarian = veterinarian;
	}
	public Vaccination(String name,  String veterinarian) {
		this.name = name;
		this.veterinarian = veterinarian;
	}

	public String getId() { return id;}

	public void setId(String id) { this.id = id;}

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
}
