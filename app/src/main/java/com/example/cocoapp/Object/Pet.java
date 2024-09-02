package com.example.cocoapp.Object;

import android.net.Uri;

import java.io.Serializable;

public class Pet implements Serializable {
	private String name;
	private String image;
	private String breed;
	private int age;
	private String gender;
	private String color;
	private float height;
	private float weight;
	private int healthValue;
	private int foodValue;
	private int moodValue;

	// Constructor
	public Pet(String name, String image, String breed, int age, String gender, String color,
	           float height, float weight, int healthValue, int foodValue, int moodValue) {
		this.name = name;
		this.image = image;
		this.breed = breed;
		this.age = age;
		this.gender = gender;
		this.color = color;
		this.height = height;
		this.weight = weight;
		this.healthValue = healthValue;
		this.foodValue = foodValue;
		this.moodValue = moodValue;
	}

	// Getters and Setters
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }

	public String getImage() { return image; }  // Updated to Uri
	public void setImage(String image) { this.image = image; }  // Updated to Uri

	public String getBreed() { return breed; }
	public void setBreed(String breed) { this.breed = breed; }

	public int getAge() { return age; }
	public void setAge(int age) { this.age = age; }

	public String getGender() { return gender; }
	public void setGender(String gender) { this.gender = gender; }

	public String getColor() { return color; }
	public void setColor(String color) { this.color = color; }

	public float getHeight() { return height; }
	public void setHeight(float height) { this.height = height; }

	public float getWeight() { return weight; }
	public void setWeight(float weight) { this.weight = weight; }

	public int getHealthValue() { return healthValue; }
	public void setHealthValue(int healthValue) { this.healthValue = healthValue; }

	public int getFoodValue() { return foodValue; }
	public void setFoodValue(int foodValue) { this.foodValue = foodValue; }

	public int getMoodValue() { return moodValue; }
	public void setMoodValue(int moodValue) { this.moodValue = moodValue; }
}
