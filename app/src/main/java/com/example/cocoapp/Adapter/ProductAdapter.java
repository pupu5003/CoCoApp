package com.example.cocoapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cocoapp.Object.Product;
import com.example.cocoapp.R;

import java.util.List;

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
		holder.productImageView.setImageDrawable(product.getProductImage().getDrawable());
		holder.addToCartButton.setOnClickListener(v -> {
			if (onAddToCartListener != null) {
				onAddToCartListener.onAddToCart(product);
				Toast.makeText(context, product.getName() + " added to cart", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(context, "Add to cart action not set", Toast.LENGTH_SHORT).show();
			}
		});
	}

	@Override
	public int getItemCount() {
		return productList.size();
	}

	public static class ProductViewHolder extends RecyclerView.ViewHolder {
		TextView productNameTextView, priceTextView, discountTextView;
		ImageView productImageView;
		Button addToCartButton;


		public ProductViewHolder(View itemView) {
			super(itemView);
			productNameTextView = itemView.findViewById(R.id.productNameTextView);
			priceTextView = itemView.findViewById(R.id.priceTextView);
			productImageView = itemView.findViewById(R.id.productImageView);
			addToCartButton = itemView.findViewById(R.id.addToCartButton);
		}
	}

	public interface OnAddToCartListener {
		void onAddToCart(Product product);
	}
}
