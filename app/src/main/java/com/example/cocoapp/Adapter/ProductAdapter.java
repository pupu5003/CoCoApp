package com.example.cocoapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.cocoapp.R;
import com.example.cocoapp.Object.Product;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

	private List<Product> productList;
	private Context context;


	public ProductAdapter(Context context, List<Product> productList) {
		this.productList = productList;
		this.context = context;
	}

	@Override
	public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, parent, false);
		return new ProductViewHolder(view);
	}

	@Override
	public void onBindViewHolder(ProductViewHolder holder, int position) {
		Product product = productList.get(position);
		holder.productNameTextView.setText(product.getName());
		holder.priceTextView.setText(product.getPrice());
		holder.productImageView.setImageDrawable(product.getProductImage().getDrawable());
		// Add more binding logic as needed
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
			discountTextView = itemView.findViewById(R.id.discountTextView);
		}
	}
}
