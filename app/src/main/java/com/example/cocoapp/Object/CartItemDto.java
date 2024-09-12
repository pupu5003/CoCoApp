// CartItemDto.java
package com.example.cocoapp.Object;

import java.io.Serializable;

public class CartItemDto implements Serializable {
	private String id;
	private Product item; // Assuming you have a ShopItemDto or equivalent class
	private Integer quantity;
	public String getId(){
		return id;
	}
	public void setId(String id){
		this.id = id;
	}

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
