package com.example.cocoapp.Object;

public class Allergy {
	private String name;
	private String description;
	private String veterinarian;

	public Allergy(String name, String description, String veterinarian) {
		this.name = name;
		this.description = description;
		this.veterinarian = veterinarian;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getVeterinarian() {
		return veterinarian;
	}

	public void setVeterinarian(String veterinarian) {
		this.veterinarian = veterinarian;
	}

}
