package com.example.cocoapp.Object;

import java.util.ArrayList;
import java.util.List;

public class CartManager
{

	private static CartManager instance;
	private final List<CartItem> cartItemList;

	private CartManager() { cartItemList = new ArrayList<>(); }

	public static synchronized CartManager getInstance()
	{
		if (instance == null) { instance = new CartManager(); }
		return instance;
	}

	public List<CartItem> getCartItemList() { return cartItemList; }

	public void addItem(CartItem item)
	{
		for (CartItem cartItem : cartItemList)
		{
			if (cartItem.getProductName().equals(item.getProductName()) && cartItem.getProductBrand().equals(item.getProductBrand()))
			{
				cartItem.setQuantity(cartItem.getQuantity() + item.getQuantity());
				return;
			}
		}
		cartItemList.add(item);
	}

	public void removeItem(int position)
	{
		if (position >= 0 && position < cartItemList.size()) { cartItemList.remove(position); }
	}

	public void clearCart() { cartItemList.clear(); }
}
