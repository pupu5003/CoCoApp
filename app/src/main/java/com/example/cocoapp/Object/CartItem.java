package com.example.cocoapp.Object;

import android.widget.ImageView;

public class CartItem {
	private String productName;
	private String productBrand;
	private String productWeight;
	private int productImage;
	private int quantity;

	public CartItem(String productName, String productBrand, String productWeight, int productImage, int quantity) {
		this.productName = productName;
		this.productBrand = productBrand;
		this.productWeight = productWeight;
		this.productImage = productImage;
		this.quantity = quantity;
	}

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