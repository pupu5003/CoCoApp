package com.example.cocoapp.object;

import android.widget.ImageView;

public class Product {

	private String discount;
	private ImageView productImage;
	private String price;
	private String name;
	private String weight;

	// Constructor
	public Product(String discount, ImageView productImage, String price, String name, String weight) {
		this.discount = discount;
		this.productImage = productImage;
		this.price = price;
		this.name = name;
		this.weight = weight;
	}

	// Getters and Setters
	public String getDiscount() {
		return discount;
	}

	public void setDiscount(String discount) {
		this.discount = discount;
	}

	public ImageView getProductImage() {
		return productImage;
	}

	public void setProductImage(ImageView productImage) {
		this.productImage = productImage;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}
}
