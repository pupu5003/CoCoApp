// CartItemDto.java
package com.example.cocoapp.Object;

public class CartItemDto {
	private Product item; // Assuming you have a ShopItemDto or equivalent class
	private Integer quantity;

	// Getters and Setters
	public Product getItem() {
		return item;
	}

	public void setItem(Product item) {
		this.item = item;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
}
