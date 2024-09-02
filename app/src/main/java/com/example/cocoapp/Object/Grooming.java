package com.example.cocoapp.Object;

import java.io.Serializable;

public class Grooming implements Serializable {

    private String name;
    private float rating;
    private int reviews;
    private boolean isOpen;
    private String distance;
    private String price;
    private String availability;
    private String pic;  // Changed from ImageView to String

    // Constructor
    public Grooming(String name, float rating, int reviews, boolean isOpen, String distance, String price, String availability, String pic) {
        this.name = name;
        this.rating = rating;
        this.reviews = reviews;
        this.isOpen = isOpen;
        this.distance = distance;
        this.price = price;
        this.availability = availability;
        this.pic = pic;
    }

    // Getters and Setters
    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public int getReviews() {
        return reviews;
    }

    public void setReviews(int reviews) {
        this.reviews = reviews;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }
}
