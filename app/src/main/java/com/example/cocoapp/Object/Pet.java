package com.example.cocoapp.Object;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;

public class Pet implements Serializable {
	private String id;
	private String petName;
	private String breedName;
	private Character gender;
	private int age;
	private String color;
	private float height;
	private float weight;
	private String imageUrl;   // The URL of the image
	private String ownerId;
	private float northCoordinate;
	private float eastCoordinate;
	private int healthValue;
	private int foodValue;
	private int moodValue;

	private String address;

	private byte[] byteArray;

	// Constructor
	public Pet(String petName, String imageUrl, String breedName, int age, Character gender, String color,
	           float height, float weight) {
		this.petName = petName;
		this.imageUrl = imageUrl;
		this.breedName = breedName;
		this.age = age;
		this.gender = gender;
		this.color = color;
		this.height = height;
		this.weight = weight;
	}


	public byte[] getByteArray() {
		return byteArray;
	}

	public void setByteArray(byte[] byteArray) {
		this.byteArray = byteArray;
	}

	public String getLocation() {
		return address;
	}

	public void setLocation(String location) {
		this.address = location;
	}


	// Getters and Setters
	public String getId() { return id; }
	public void setId(String id) { this.id = id; }

	public String getPetName() { return petName; }
	public void setPetName(String petName) { this.petName = petName; }

	public String getBreedName() { return breedName; }
	public void setBreedName(String breedName) { this.breedName = breedName; }

	public Character getGender() { return gender; }
	public void setGender(Character gender) { this.gender = gender; }

	public int getAge() { return age; }
	public void setAge(int age) { this.age = age; }

	public String getColor() { return color; }
	public void setColor(String color) { this.color = color; }

	public float getHeight() { return height; }
	public void setHeight(float height) { this.height = height; }

	public float getWeight() { return weight; }
	public void setWeight(float weight) { this.weight = weight; }


	public String getImage() { return imageUrl; }
	public void setImage(String imageUrl) { this.imageUrl = imageUrl; }

	public String getOwnerId() { return ownerId; }
	public void setOwnerId(String ownerId) { this.ownerId = ownerId; }

	public float getNorthCoordinate() { return northCoordinate; }
	public void setNorthCoordinate(float northCoordinate) { this.northCoordinate = northCoordinate; }

	public float getEastCoordinate() { return eastCoordinate; }
	public void setEastCoordinate(float eastCoordinate) { this.eastCoordinate = eastCoordinate; }

	public int getHealthValue() { return healthValue; }
	public void setHealthValue(int healthValue) { this.healthValue = healthValue; }

	public int getFoodValue() { return foodValue; }
	public void setFoodValue(int foodValue) { this.foodValue = foodValue; }

	public int getMoodValue() { return moodValue; }
	public void setMoodValue(int moodValue) { this.moodValue = moodValue; }



}
