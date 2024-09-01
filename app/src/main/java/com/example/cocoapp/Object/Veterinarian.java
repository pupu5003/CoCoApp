package com.example.cocoapp.Object;

import android.widget.ImageView;

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
    private ImageView pic;

    public Veterinarian(String name, String qualification, float rating, int reviews,
                        int experience, String distance, String price, String availability, ImageView pic, String lastVisit) {
        this.name = name;
        this.qualification = qualification;
        this.rating = rating;
        this.reviews = reviews;
        this.experience = experience;
        this.distance = distance;
        this.price = price;
        this.availability = availability;
        this.pic=pic;
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

    public ImageView getProfileImage() {
        return pic;
    }

    public String getLastVisit() {
        return lastVisit;
    }

    public void setLastVisit(String lastVisit) {
        this.lastVisit = lastVisit;
    }
}

