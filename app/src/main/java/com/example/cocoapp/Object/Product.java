package com.example.cocoapp.Object;

import android.content.Context;

import java.io.Serializable;

public class Product implements Serializable {
	private Size size;
	private String id;
	private int discount;
	private String imageUrl; // Change this to String
	private float price;
	private String name;
	private String size1;
	private String brand;
	private int quantitySold;
	private int stock;
	private String description;

	public Product(int discount, String productImageUrl, float price, String name, String weight, String brand, int stock) {
		this.discount = discount;
		this.imageUrl = productImageUrl;
		this.price = price;
		this.name = name;
		this.size1 = weight;
		this.brand = brand;
		this.quantitySold = 0;
		this.stock = stock;
	}

	public Product() {

	}

	public int getDiscount() { return discount; }

	public String getProductImageUrl() { return imageUrl; }

	public float getPrice() { return price; }

	public String getName() { return name; }

	public String getSize() {
		return size1;
	}

	public String getBrand() { return brand; }

	public int getQuantity() { return quantitySold; }

	public void incrementQuantity() { this.quantitySold++; }

	public void decrementQuantity() {
		if (this.quantitySold > 1) { this.quantitySold--; }
	}

	public int getStock(){
		return stock;
	}

	public  void setStock(int stock){
		this.stock = stock;
	}

	public void setId(String id){
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}


	public void setPrice(float price) {
	}

	public void setDescription(String description) {
	}

	public void setQuantitySold(int quantitySold) {
		this.quantitySold = quantitySold;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public void setWeight(String size) {
		this.size1 = size;
	}

//	public void setSizeFromApi(Size size) {
//		if (size != null) {
//			this.size1 = size.getValue() + " " + size.getUnit();
//		} else {
//			this.size1 = "";
//		}
//	}

	public Size getSizeObject() {
		return size;
	}

	public void setSizeObject(Size size) {
		this.size = size;
	}

	public void setSize(String size) {
		this.size1 = size;
	}
}

