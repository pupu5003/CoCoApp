package com.example.cocoapp.Object;

import java.io.Serializable;
import java.util.List;

public class Veterinarian implements Serializable {
    private String vetId;
    private String vetName;
    private String degree;
    private Grooming location;
    private List<Appointment> appointments;
    private String description;
    private float price;
    private String workTime;
    private float rating;
    private float votes;
    private float distance;
    private List<ReviewItem> reviews;
    private String imageUrl;
    private String address;
    private String experience;

    public Veterinarian(String name, String qualification, float rating, int reviews,
                        int experience, String distance, String price, String availability, String pic, String lastVisit) {
        this.vetName = name;
        this.degree = qualification;
        this.rating = rating;
        this.votes = reviews;
        this.description = lastVisit;
        this.distance = Float.parseFloat(distance);
        this.price = Float.parseFloat(price);
        this.workTime = availability;
        this.imageUrl = pic;
    }

    // Getters and Setters
    public String getVetId() {
        return vetId;
    }

    public void setVetId(String vetId) {
        this.vetId = vetId;
    }

    public String getVetName() {
        return vetName;
    }

    public void setVetName(String vetName) {
        this.vetName = vetName;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public Grooming getLocation() {
        return location;
    }

    public void setLocation(Grooming location) {
        this.location = location;
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getPrice() {
        return location.getPrice();
    }

    public void setPrice(float price) {
        this.price = price;
        this.location.setPrice(price);
    }

    public String getWorkTime() {
        return location.getWorkTime();
    }

    public void setWorkTime(String workTime) {
        this.workTime = workTime;
        this.location.setWorkTime(workTime);
    }

    public float getRating() {
        return location.getRating();
    }

    public void setRating(float rating) {
        this.rating = rating;
        this.location.setRating(rating);
    }

    public float getVotes() {
        return location.getVotes();
    }

    public void setVotes(float votes) {
        this.votes = votes;
        this.location.setVotes(votes);
    }

    public float getDistance() {
        return location.getDistance();
    }

    public void setDistance(float distance) {
        this.distance = distance;
        this.location.setDistance(distance);
    }

    public List<ReviewItem> getReviews() {
        return location.getReviews();
    }

    public void setReviews(List<ReviewItem> reviews) {
        this.reviews = reviews;
        this.location.setReviews(reviews);
    }

    public String getImageUrl() {
        return location.getPic();
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        this.location.setPic(imageUrl);
    }

    public String getAddress() {
        return location.getAddress();
    }

    public void setAddress(String address) {
        this.address = address;
        this.location.setAddress(address);
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getLocationType(){
        return location.getType();
    }

}
