package com.example.cocoapp.Object;

import android.widget.ImageView;

public class CartItem {
	private String productId;
	private String productName;
	private String productBrand;
	private String productWeight;
	private int productImage;
	private int quantity;

	public CartItem(String productId, String productName, String productBrand, String productWeight, int productImage, int quantity) {
		this.productId = productId;
		this.productName = productName;
		this.productBrand = productBrand;
		this.productWeight = productWeight;
		this.productImage = productImage;
		this.quantity = quantity;
	}

	public CartItem(String productId, String productName, String productBrand, String productWeight, int quantity) {
		this.productId = productId;
		this.productName = productName;
		this.productBrand = productBrand;
		this.productWeight = productWeight;
		this.productImage = 0; // Default image or update as necessary
		this.quantity = quantity;
	}


	public CartItem(String productName, String productBrand, String productWeight, int productImage) {
		this.productName = productName;
		this.productBrand = productBrand;
		this.productWeight = productWeight;
		this.productImage = productImage;
		this.quantity = 1; // Default to 1 if no quantity is specified
	}

	public String getProductId() { return productId; }

	public void setProductId(String productId) { this.productId = productId; }

	public String getProductName() {
		return productName;
	}

	public String getProductBrand() {
		return productBrand;
	}

	public String getProductWeight() {
		return productWeight;
	}

	public int getProductImage() {
		return productImage;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

}