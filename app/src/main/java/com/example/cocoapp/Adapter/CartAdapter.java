package com.example.cocoapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cocoapp.Fragment.ViewCart;
import com.example.cocoapp.Object.CartDto;
import com.example.cocoapp.R;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

	private final List<CartDto> cartItemList;
	private final Context context;

	public CartAdapter(List<CartDto> cartItemList, Context context) {
		this.cartItemList = cartItemList;
		this.context = context;
	}

	@NonNull
	@Override
	public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
		return new CartViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
		CartDto cartItem = cartItemList.get(position);
		holder.bind(cartItem, position);
	}

	@Override
	public int getItemCount() {
		return cartItemList.size();
	}

	public class CartViewHolder extends RecyclerView.ViewHolder {
		private final TextView productName, productBrand, productWeight, quantityTextView;
		private final ImageView productImage;
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
			itemContent = itemView.findViewById(R.id.item_content);
		}

		public void bind(CartDto cartItem, int position) {
			productName.setText(cartItem.getItems().get(position).getItem().getName());
			productBrand.setText(cartItem.getItems().get(position).getItem().getBrand());
			productWeight.setText(cartItem.getItems().get(position).getItem().getSizeObject().getValue() + " " + cartItem.getItems().get(position).getItem().getSizeObject().getUnit());
			String baseUrl = "http://172.28.102.169:8080";
			String fileName = cartItem.getItems().get(position).getItem().getImageUrl();
			Glide.with(context)
					.load(baseUrl+fileName)
					.error(R.drawable.dog1)
					.into(productImage);
			quantityTextView.setText(String.valueOf(cartItem.getItems().get(position).getQuantity()));

			incrementButton.setOnClickListener(v -> {
				cartItem.getItems().get(position).setQuantity(cartItem.getItems().get(position).getQuantity() + 1);
				quantityTextView.setText(String.valueOf(cartItem.getItems().get(position).getQuantity()));

				((ViewCart) ((FragmentActivity) context).getSupportFragmentManager().findFragmentById(R.id.fragment_container))
						.updateCartItem(cartItem.getItems().get(position), position);
			});

			decrementButton.setOnClickListener(v -> {
				if (cartItem.getItems().get(position).getQuantity() > 1) {
					cartItem.getItems().get(position).setQuantity(cartItem.getItems().get(position).getQuantity() - 1);
					quantityTextView.setText(String.valueOf(cartItem.getItems().get(position).getQuantity()));
					((ViewCart) ((FragmentActivity) context).getSupportFragmentManager().findFragmentById(R.id.fragment_container))
							.updateCartItem(cartItem.getItems().get(position), position);
				}
			});
		}
	}
}
