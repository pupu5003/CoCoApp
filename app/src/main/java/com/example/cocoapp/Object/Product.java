package com.example.cocoapp.Object;

import android.widget.ImageView;

import java.io.Serializable;

public class Product implements Serializable
{

	private String discount;
	private ImageView productImage;
	private String price;
	private String name;
	private String weight;
	private String brand;
	private int quantity;

	public Product(String discount, ImageView productImage, String price, String name, String weight, String brand)
	{
		this.discount = discount;
		this.productImage = productImage;
		this.price = price;
		this.name = name;
		this.weight = weight;
		this.brand = brand;
		this.quantity = 0;
	}

	public Product(String discount, ImageView productImage, String price, String name, String weight, String brand, int quantity)
	{
		this.discount = discount;
		this.productImage = productImage;
		this.price = price;
		this.name = name;
		this.weight = weight;
		this.brand = brand;
		this.quantity = quantity;
	}

	public String getDiscount() { return discount; }

	public void setDiscount(String discount) { this.discount = discount; }

	public ImageView getProductImage() { return productImage; }

	public void setProductImage(ImageView productImage) { this.productImage = productImage; }

	public String getPrice() { return price; }

	public void setPrice(String price) { this.price = price; }

	public String getName() { return name; }

	public void setName(String name) { this.name = name; }

	public String getWeight() { return weight; }

	public void setWeight(String weight) { this.weight = weight; }

	public String getBrand() { return brand; }

	public void setBrand(String brand) { this.brand = brand; }

	public int getQuantity() { return quantity; }

	public void setQuantity(int quantity) { this.quantity = quantity; }

	public void incrementQuantity() { this.quantity++; }

	public void decrementQuantity()
	{
		if (this.quantity > 1) { this.quantity--; }
	}
}