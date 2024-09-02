package com.example.cocoapp.Object;

import java.io.Serializable;

public class Veterinarian implements Serializable {
    private String name;
    private String qualification;
    private float rating;
    private int reviews;
    private int experience;
    private String distance;
    private String price;
    private String availability;
    private String lastVisit;
    private String pic; // Changed from ImageView to String

    public Veterinarian(String name, String qualification, float rating, int reviews,
                        int experience, String distance, String price, String availability, String pic, String lastVisit) {
        this.name = name;
        this.qualification = qualification;
        this.rating = rating;
        this.reviews = reviews;
        this.experience = experience;
        this.distance = distance;
        this.price = price;
        this.availability = availability;
        this.pic = pic; // Updated to use String
        this.lastVisit = lastVisit;
    }

    public String getName() {
        return name;
    }

    public String getQualification() {
        return qualification;
    }

    public float getRating() {
        return rating;
    }

    public int getReviews() {
        return reviews;
    }

    public int getExperience() {
        return experience;
    }

    public String getDistance() {
        return distance;
    }

    public String getPrice() {
        return price;
    }

    public String getAvailability() {
        return availability;
    }

    public String getProfileImage() { // Updated method name to reflect the change
        return pic;
    }

    public String getLastVisit() {
        return lastVisit;
    }

    public void setLastVisit(String lastVisit) {
        this.lastVisit = lastVisit;
    }
}
