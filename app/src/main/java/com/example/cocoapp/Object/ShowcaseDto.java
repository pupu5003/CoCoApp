package com.example.cocoapp.Object;

public class ShowcaseDto {
	private String id;

	private Veterinarian vet;

	private String category;

	private String type;

	private String description;

	public String getId() { return vet.getVetId();}
	public String getCategory(){
		return category;
	}
	public String getName(){
		return vet.getVetName();
	}
	public String getDescription(){
		return description;
	}
	public String getType() {
		return type;
	}
}
