package com.example.cocoapp.Object;

import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class CartManager
{

	private static CartManager instance;
	private final List<CartItem> cartItemList;

	private final List<Pair<String, Integer>> cartItems;

	private CartManager() {
		cartItemList = new ArrayList<>();
		cartItems = new ArrayList<>();
	}

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
			if (cartItem.getProductId().equals(item.getProductId()))
			{
				cartItem.setQuantity(cartItem.getQuantity() + item.getQuantity());
				updateCartItemsList(cartItem.getProductId(), cartItem.getQuantity());
				return;
			}
		}
		cartItemList.add(item);
		updateCartItemsList(item.getProductId(), item.getQuantity());
	}

	private void updateCartItemsList(String productId, int quantity) {
		for (int i = 0; i < cartItems.size(); i++) {
			Pair<String, Integer> pair = cartItems.get(i);
			if (pair.first.equals(productId)) {
				cartItems.set(i, new Pair<>(productId, quantity));
				return;
			}
		}
		cartItems.add(new Pair<>(productId, quantity));
	}

	public void removeItem(int position) {
		if (position >= 0 && position < cartItemList.size()) {
			CartItem removedItem = cartItemList.remove(position);
			removeItemFromCartItemsList(removedItem.getProductId());
		}
	}

	private void removeItemFromCartItemsList(String productId) {
		cartItems.removeIf(pair -> pair.first.equals(productId));
	}

	public void clearCart() {
		cartItemList.clear();
		cartItems.clear();
	}

	public List<Pair<String, Integer>> getCartItems() {
		return cartItems;
	}
}
