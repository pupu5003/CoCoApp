package com.example.cocoapp.Object;

public class Allergy {
	private String id;
	private String name;
	private String description;
	private String veterinarian;

	public Allergy(String id, String name, String description, String veterinarian) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.veterinarian = veterinarian;
	}


	public Allergy(String name, String description, String veterinarian) {
		this.name = name;
		this.description = description;
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
