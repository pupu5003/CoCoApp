package com.example.cocoapp.Object;

public class ProfileData {
	private String userId;
	private String name;
	private String email;
	private String phoneNumber;
	private String imageUrl;

	public ProfileData() {
		this.userId = userId;
		this.name = name;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.imageUrl = imageUrl;
	}

	public String getUserId() { return userId; }

	public void setUserId(String userId) { this.userId = userId; }

	public String getName() { return name; }

	public void setName(String name) { this.name = name; }

	public String getEmail() { return email; }

	public void setEmail(String email) { this.email = email; }

	public String getPhone() { return phoneNumber; }

	public void setPhone(String phone) { this.phoneNumber = phone; }

	public String getImageUrl() { return imageUrl; }

	public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

	@Override
	public String toString() {
		return "ProfileData{" +
				"name='" + name + '\'' +
				", email='" + email + '\'' +
				", phone='" + phoneNumber + '\'' +
				'}';
	}
}
