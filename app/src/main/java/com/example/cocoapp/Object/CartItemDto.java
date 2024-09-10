// CartItemDto.java
package com.example.cocoapp.Object;

public class CartItemDto {
	private ShopItemDto item; // Assuming you have a ShopItemDto or equivalent class
	private Integer quantity;

	// Getters and Setters
	public ShopItemDto getItem() {
		return item;
	}

	public void setItem(ShopItemDto item) {
		this.item = item;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
}
