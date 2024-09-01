package com.example.cocoapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cocoapp.Object.Product;
import com.example.cocoapp.R;

import java.util.List;
import java.util.Objects;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

	private Context context;
	private List<Product> productList;
	private OnAddToCartListener onAddToCartListener;

	public ProductAdapter(Context context, List<Product> productList, OnAddToCartListener onAddToCartListener) {
		this.context = context;
		this.productList = productList;
		this.onAddToCartListener = onAddToCartListener;
	}

	@NonNull
	@Override
	public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(context).inflate(R.layout.product_item, parent, false);
		return new ProductViewHolder(view);
	}

	@Override
	public void onBindViewHolder(ProductViewHolder holder, int position) {
		Product product = productList.get(position);
		holder.productNameTextView.setText(product.getName());
		holder.priceTextView.setText(product.getPrice());
		if (Objects.equals(product.getDiscount(), "")) holder.discountTextView.setVisibility(View.GONE);
		else {
			holder.discountTextView.setVisibility(View.VISIBLE);
			holder.discountTextView.setText(product.getDiscount());}
		holder.discountTextView.setText(product.getDiscount());
		holder.productImageView.setImageDrawable(product.getProductImage().getDrawable());
		if (product.getQuantity()>0) {
			int quantity = Integer.parseInt(holder.quantityTextView.getText().toString());
			holder.quantityTextView.setText(String.valueOf(quantity));
		}
		else holder.quantityTextView.setText("1");

		holder.addToCartButton.setOnClickListener(v -> {
			holder.addToCartButton.setVisibility(View.GONE);
			holder.quantityLayout.setVisibility(View.VISIBLE);
			holder.quantityTextView.setText("1");
			if (onAddToCartListener != null) {
				onAddToCartListener.onAddToCart(product);
				Toast.makeText(context, product.getName() + " added to cart", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(context, "Add to cart action not set", Toast.LENGTH_SHORT).show();
			}
		});

		holder.incrementButton.setOnClickListener(v -> {
			int quantity = Integer.parseInt(holder.quantityTextView.getText().toString());
			holder.quantityTextView.setText(String.valueOf(++quantity));
		});

		// Handle decrement button click
		holder.decrementButton.setOnClickListener(v -> {
			int quantity = Integer.parseInt(holder.quantityTextView.getText().toString());
			if (quantity > 1) {
				holder.quantityTextView.setText(String.valueOf(--quantity));
			} else {
				// If quantity is 1, revert to showing the cart icon
				holder.quantityTextView.setText("1");
				holder.quantityLayout.setVisibility(View.GONE);
				holder.addToCartButton.setVisibility(View.VISIBLE);
			}
		});
	}

	@Override
	public int getItemCount() {
		return productList.size();
	}

	public static class ProductViewHolder extends RecyclerView.ViewHolder {
		TextView productNameTextView, priceTextView, discountTextView, quantityTextView;
		ImageView productImageView;
		Button addToCartButton;
		LinearLayout quantityLayout;
		Button incrementButton;
		Button decrementButton;


		public ProductViewHolder(View itemView) {
			super(itemView);
			productNameTextView = itemView.findViewById(R.id.productNameTextView);
			priceTextView = itemView.findViewById(R.id.priceTextView);
			productImageView = itemView.findViewById(R.id.productImageView);
			discountTextView = itemView.findViewById(R.id.discountTextView);
			addToCartButton = itemView.findViewById(R.id.addToCartButton);
			quantityLayout = itemView.findViewById(R.id.quantityLayout);
			quantityTextView = itemView.findViewById(R.id.quantityTextView);
			incrementButton = itemView.findViewById(R.id.incrementButton);
			decrementButton = itemView.findViewById(R.id.decrementButton);
		}
	}

	public interface OnAddToCartListener {
		void onAddToCart(Product product);
	}
}
