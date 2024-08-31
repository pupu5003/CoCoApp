package com.example.cocoapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cocoapp.R;
import com.example.cocoapp.Object.CartItem;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

	private final List<CartItem> cartItemList;

	public CartAdapter(List<CartItem> cartItemList) {
		this.cartItemList = cartItemList;
	}

	@NonNull
	@Override
	public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item, parent, false);
		return new CartViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
		CartItem cartItem = cartItemList.get(position);
		holder.bind(cartItem);
	}

	@Override
	public int getItemCount() {
		return cartItemList.size();
	}

	// Make CartViewHolder class public
	public class CartViewHolder extends RecyclerView.ViewHolder {
		private final TextView productName, productBrand, productWeight, quantityTextView;
		private final ImageView productImage, trashIcon;
		private final Button incrementButton, decrementButton;
		private final LinearLayout itemContent;

		public CartViewHolder(@NonNull View itemView) {
			super(itemView);
			productName = itemView.findViewById(R.id.product_name);
			productBrand = itemView.findViewById(R.id.product_brand);
			productWeight = itemView.findViewById(R.id.product_weight);
			productImage = itemView.findViewById(R.id.product_image);
			quantityTextView = itemView.findViewById(R.id.quantityTextView);
			incrementButton = itemView.findViewById(R.id.incrementButton);
			decrementButton = itemView.findViewById(R.id.decrementButton);
			trashIcon = itemView.findViewById(R.id.trash_icon);
			itemContent = itemView.findViewById(R.id.item_content);

			// Set up click listener for the trash icon inside the constructor
			trashIcon.setOnClickListener(v -> {
				// Remove the item when trash icon is clicked
				int position = getAdapterPosition();
				if (position != RecyclerView.NO_POSITION) {
					cartItemList.remove(position); // Corrected list reference
					notifyItemRemoved(position); // Correct method call within the adapter
				}
			});
		}

		// Make the revealTrashIcon method public
		public void revealTrashIcon() {
			itemContent.animate().translationX(-trashIcon.getWidth() - 20).setDuration(300).withEndAction(() -> {
				trashIcon.setVisibility(View.VISIBLE);
			}).start();
		}

		// Optional: Method to reset the view when needed (e.g., cancel swipe)
		public void resetView() {
			itemContent.animate().translationX(0).setDuration(300).start();
			trashIcon.setVisibility(View.GONE);
		}

		public void bind(CartItem cartItem) {
			productName.setText(cartItem.getProductName());
			productBrand.setText(cartItem.getProductBrand());
			productWeight.setText(cartItem.getProductWeight());
			productImage.setImageResource(cartItem.getProductImage());
			quantityTextView.setText(String.valueOf(cartItem.getQuantity()));

			incrementButton.setOnClickListener(v -> {
				cartItem.setQuantity(cartItem.getQuantity() + 1);
				quantityTextView.setText(String.valueOf(cartItem.getQuantity()));
			});

			decrementButton.setOnClickListener(v -> {
				if (cartItem.getQuantity() > 1) {
					cartItem.setQuantity(cartItem.getQuantity() - 1);
					quantityTextView.setText(String.valueOf(cartItem.getQuantity()));
				}
			});
		}
	}
}
