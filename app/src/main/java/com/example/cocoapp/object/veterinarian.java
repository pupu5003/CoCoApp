package com.example.cocoapp.object;

import android.widget.ImageView;

public class veterinarian {
    private String name;
    private String qualification;
    private float rating;
    private int reviews;
    private int experience;
    private String distance;
    private String price;
    private String availability;

    private ImageView pic;

    public veterinarian(String name, String qualification, float rating, int reviews,
                        int experience, String distance, String price, String availability,ImageView pic) {
        this.name = name;
        this.qualification = qualification;
        this.rating = rating;
        this.reviews = reviews;
        this.experience = experience;
        this.distance = distance;
        this.price = price;
        this.availability = availability;
        this.pic=pic;
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
    public ImageView getProfileImage(){
        return pic;
    }

}
