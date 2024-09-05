package com.example.cocoapp.Object;

import java.io.Serializable;
import java.util.List;

public class Grooming implements Serializable {
    private String id;
    private String name;
    private float price;
    private String workTime;
    private float rating;
    private float votes;
    private float distance;
    private String type; // "Grooming" or "Boarding" (if it's veterinarian, it's null)
    private List<ReviewItem> reviews;
    private String imageUrl;
    private float northCoordinate;
    private float eastCoordinate;
    private String address;
    private boolean isOpen;

    // Constructor
    public Grooming(String name, float rating) {
        this.name = name;
        this.rating = rating;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getWorkTime() {
        return workTime;
    }

    public void setWorkTime(String workTime) {
        this.workTime = workTime;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public float getVotes() {
        return votes;
    }

    public void setVotes(float votes) {
        this.votes = votes;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPic() {
        return imageUrl;
    }

    public void setPic(String pic) {
        this.imageUrl = pic;
    }

    public float getNorthCoordinate() {
        return northCoordinate;
    }

    public void setNorthCoordinate(float northCoordinate) {
        this.northCoordinate = northCoordinate;
    }

    public float getEastCoordinate() {
        return eastCoordinate;
    }

    public void setEastCoordinate(float eastCoordinate) {
        this.eastCoordinate = eastCoordinate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public List<ReviewItem> getReviews() {
        return reviews;
    }

    public void setReviews(List<ReviewItem> reviews) {
        this.reviews = reviews;
    }


}
