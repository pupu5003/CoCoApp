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

import com.example.cocoapp.R;
import com.example.cocoapp.Object.Product;

import java.util.List;

public class ProductDashboardAdapter extends RecyclerView.Adapter<ProductDashboardAdapter.ProductViewHolder> {

	private Context context;
	private List<Product> productList;
	private boolean showAll;
	private ProductDashboardAdapter.OnAddToCartListener onAddToCartListener;

	public ProductDashboardAdapter(Context context, List<Product> productList, boolean showAll, ProductDashboardAdapter.OnAddToCartListener onAddToCartListener) {
		this.context = context;
		this.productList = productList;
		this.showAll = showAll;
		this.onAddToCartListener = onAddToCartListener;
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

		// Initially show cart icon and hide quantity layout
		holder.cartIcon.setVisibility(View.VISIBLE);
		holder.quantityLayout.setVisibility(View.GONE);

		if (product.getQuantity()>0) {
			holder.cartIcon.setVisibility(View.GONE);
			holder.quantityLayout.setVisibility(View.VISIBLE);
			int quantity = Integer.parseInt(holder.quantityTextView.getText().toString());
			holder.quantityTextView.setText(String.valueOf(quantity));
		}
		else{
			holder.quantityTextView.setText("1");
		}

		// Handle cart icon click to show quantity layout
		holder.cartIcon.setOnClickListener(v -> {
			holder.cartIcon.setVisibility(View.GONE);
			holder.quantityLayout.setVisibility(View.VISIBLE);
			holder.quantityTextView.setText("1");
			if (onAddToCartListener != null) {
				onAddToCartListener.onAddToCart(product);
				Toast.makeText(context, product.getName() + " added to cart", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(context, "Add to cart action not set", Toast.LENGTH_SHORT).show();
			}
		});

		// Handle increment button click
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
				holder.quantityTextView.setText("1");
				holder.quantityLayout.setVisibility(View.GONE);
				holder.cartIcon.setVisibility(View.VISIBLE);
			}
		});
	}



	@Override
	public int getItemCount() {
		return showAll ? productList.size() : Math.min(productList.size(), 3); // Show all if showAll is true
	}
	public static class ProductViewHolder extends RecyclerView.ViewHolder {

		ImageView productImage;
		TextView productName;
		TextView productBrand;
		TextView productWeight;
		ImageView cartIcon;
		LinearLayout quantityLayout;
		TextView quantityTextView;
		Button incrementButton;
		Button decrementButton;

		public ProductViewHolder(@NonNull View itemView) {
			super(itemView);
			productImage = itemView.findViewById(R.id.food_image);
			productName = itemView.findViewById(R.id.food_name);
			productBrand = itemView.findViewById(R.id.company_name);
			productWeight = itemView.findViewById(R.id.food_weight);
			cartIcon = itemView.findViewById(R.id.cart_ic);
			quantityLayout = itemView.findViewById(R.id.linearlayout_quantity);
			quantityTextView = itemView.findViewById(R.id.quantityTextView);
			incrementButton = itemView.findViewById(R.id.incrementButton);
			decrementButton = itemView.findViewById(R.id.decrementButton);
		}
	}

	public interface OnAddToCartListener {
		void onAddToCart(Product product);
	}

}
