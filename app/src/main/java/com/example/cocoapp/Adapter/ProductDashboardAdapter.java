package com.example.cocoapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cocoapp.R;
import com.example.cocoapp.object.Product;

import java.util.List;

public class ProductDashboardAdapter extends RecyclerView.Adapter<ProductDashboardAdapter.ProductViewHolder> {

	private Context context;
	private List<Product> productList;

	public ProductDashboardAdapter(Context context, List<Product> productList) {
		this.context = context;
		this.productList = productList;
	}

	@NonNull
	@Override
	public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(context).inflate(R.layout.item_product_dashboard, parent, false);
		return new ProductViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
		Product product = productList.get(position);

		// Bind product data to views
		holder.productName.setText(product.getName());
		holder.productBrand.setText(product.getBrand());
		holder.productWeight.setText(product.getWeight());
		holder.productImage.setImageDrawable(product.getProductImage().getDrawable()); // Assuming ImageView has drawable set
	}

	@Override
	public int getItemCount() {
		return productList.size();
	}

	public static class ProductViewHolder extends RecyclerView.ViewHolder {

		ImageView productImage;
		TextView productName;
		TextView productBrand;
		TextView productWeight;

		public ProductViewHolder(@NonNull View itemView) {
			super(itemView);
			productImage = itemView.findViewById(R.id.food_image);
			productName = itemView.findViewById(R.id.food_name);
			productBrand = itemView.findViewById(R.id.company_name);
			productWeight = itemView.findViewById(R.id.food_weight);
		}
	}
}
