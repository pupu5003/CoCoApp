package com.example.cocoapp.Object;

public class NotificationItem {
	private String doctorName;
	private String message;
	private String time;
	private String UrlImage;

	public NotificationItem(String doctorName, String message, String time, String UrlImage) {
		this.doctorName = doctorName;
		this.message = message;
		this.time = time;
		this.UrlImage = UrlImage;
	}

	public String getDoctorName() {
		return doctorName;
	}

	public String getMessage() {
		return message;
	}

	public String getTime() {
		return time;
	}

	public String getIconResId() {
		return UrlImage;
	}
}

