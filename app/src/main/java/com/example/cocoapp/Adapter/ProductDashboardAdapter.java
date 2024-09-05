package com.example.cocoapp.Adapter;

import static java.lang.Float.NaN;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cocoapp.Api.ApiClient;
import com.example.cocoapp.Api.ApiService;
import com.example.cocoapp.Fragment.ProductProfile;
import com.example.cocoapp.R;
import com.example.cocoapp.Object.Product;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

	public void updateProductList(List<Product> products) {
		this.productList.clear();
		this.productList.addAll(products);
		notifyDataSetChanged();  // Notify the adapter that the data has changed
	}


	@Override
	public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
		Product product = productList.get(position);

		holder.productName.setText(product.getName());
		holder.productBrand.setText(product.getBrand());
		holder.productWeight.setText(product.getSize());

		if (product.getDiscount() == 0) holder.productDiscount.setVisibility(View.GONE);
		else {
			holder.productDiscount.setVisibility(View.VISIBLE);
			holder.productDiscount.setText(String.valueOf(product.getDiscount()) + "%");}
		holder.productDiscount.setText(String.valueOf(product.getDiscount()) + "%");

		if (product.getQuantity()>0) {
			int quantity = Integer.parseInt(holder.quantityTextView.getText().toString());
			holder.quantityTextView.setText(String.valueOf(quantity));
		}
		else holder.quantityTextView.setText("1");

		if(product.getPrice() != 0) holder.productPrice.setText(String.valueOf(product.getPrice()) + " VND");

		if (product.getSizeObject() != null) {
			String weight = String.valueOf(product.getSizeObject().getValue()) + " " + product.getSizeObject().getUnit();
			holder.productWeight.setText(weight);
		}

		ApiService apiService = ApiClient.getClient(context, false).create(ApiService.class);
		SharedPreferences prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
		String token = prefs.getString("jwt_token", null);
		String baseUrl = "http://172.28.102.169:8080";
		String fileName = product.getProductImageUrl();
		String basePath = "/file/";
		fileName = fileName.substring(basePath.length());

		apiService.fetchImageFile("Bearer " + token, fileName).enqueue(new Callback<Void>() {
			@Override
			public void onResponse(Call<Void> call, Response<Void> response) {
				if (response.isSuccessful()) {
					// Load image with Glide
					String fileName = product.getProductImageUrl();
					Glide.with(context)
							.load(baseUrl+fileName)
							.error(R.drawable.dog1)
							.into(holder.productImage);

					Log.e("Full Image URL", baseUrl + fileName);
				} else {
					Log.e("API Error", "Response code: " + response.code() + " Message: " + response.message());
					Toast.makeText(context, "Failed to access image", Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onFailure(Call<Void> call, Throwable t) {
				Log.e("API Error", "Error accessing image: " + t.getMessage());
				Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
			}
		});

		// Handle cart icon click to show quantity layout
		holder.cartIcon.setOnClickListener(v -> {
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
			}
		});

		holder.itemView.setOnClickListener(v -> {
			FragmentActivity activity = (FragmentActivity) context;

			ProductProfile productProfileFragment = new ProductProfile();
			Bundle bundle = new Bundle();
			bundle.putString("product_id", product.getId());
			productProfileFragment.setArguments(bundle);

			FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
			transaction.replace(R.id.fragment_container, productProfileFragment)
					.addToBackStack(null)
					.commit();
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
		TextView productDiscount;
		TextView productPrice;
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
			productDiscount = itemView.findViewById(R.id.discountTextView);
			productPrice = itemView.findViewById(R.id.food_price);
		}
	}

	public interface OnAddToCartListener {
		void onAddToCart(Product product);
	}

}
