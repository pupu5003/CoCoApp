package com.example.cocoapp.Adapter;

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
import com.example.cocoapp.Fragment.ViewCart;
import com.example.cocoapp.Object.CartDto;
import com.example.cocoapp.Object.CartItemDto;
import com.example.cocoapp.Object.Product;
import com.example.cocoapp.R;
import com.google.gson.Gson;

import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

	private Context context;
	private List<Product> productList;


	private boolean showAll;
	private SharedPreferences prefs;
	private String token;
	private ApiService apiService;

	public ProductAdapter(Context context, List<Product> productList, boolean showAll) {
		this.context = context;
		this.productList = productList;
		this.showAll = showAll;
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
		String price = String.valueOf(product.getPrice()) + "VND";
		holder.priceTextView.setText(price);
		if (product.getDiscount() == 0) holder.discountTextView.setVisibility(View.GONE);
		else {
			holder.discountTextView.setVisibility(View.VISIBLE);
			holder.discountTextView.setText(String.valueOf(product.getDiscount()) + "%");
		}
		holder.discountTextView.setText(String.valueOf(product.getDiscount()) + "%");

		String baseUrl = "http://172.28.102.169:8080";
		Glide.with(context)
				.load(baseUrl + product.getProductImageUrl())
				.error(R.drawable.dog1)
				.into(holder.productImageView);



		if (product.getCurrentQuantity()>0) {
			holder.quantityTextView.setText(String.valueOf(product.getCurrentQuantity()));
			holder.addToCartButton.setVisibility(View.GONE);
			holder.quantityLayout.setVisibility(View.VISIBLE);
		}
		else {
			holder.addToCartButton.setVisibility(View.VISIBLE);
			holder.quantityLayout.setVisibility(View.GONE);
		}

		if (product.getSizeObject() != null) {
			String weight = String.valueOf(product.getSizeObject().getValue()) + " " + product.getSizeObject().getUnit();
			holder.weightTextView.setText(weight);
		}

		if(product.getPrice() != 0) holder.priceTextView.setText(String.valueOf(product.getPrice()) + " VND");

		holder.addToCartButton.setOnClickListener(v -> {
			holder.addToCartButton.setVisibility(View.GONE);
			holder.quantityLayout.setVisibility(View.VISIBLE);
			holder.quantityTextView.setText("1");
			product.setCurrentQuantity(1);
			CartItemDto tmp = new CartItemDto();
			tmp.setItem(product);
			tmp.setQuantity(1);
			addCartItem(tmp);
		});

		holder.incrementButton.setOnClickListener(v -> {
			int quantity = Integer.parseInt(holder.quantityTextView.getText().toString());
			holder.quantityTextView.setText(String.valueOf(++quantity));
			product.setCurrentQuantity(quantity);
		});

		// Handle decrement button click
		holder.decrementButton.setOnClickListener(v -> {
			int quantity = Integer.parseInt(holder.quantityTextView.getText().toString());
			if (quantity > 1) {
				holder.quantityTextView.setText(String.valueOf(--quantity));
				product.setCurrentQuantity(quantity);
			} else {// If quantity is 1, revert to showing the cart icon
				holder.quantityTextView.setText("1");
				holder.quantityLayout.setVisibility(View.GONE);
				holder.addToCartButton.setVisibility(View.VISIBLE);
				product.setCurrentQuantity(0);
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
		return showAll ? productList.size() : Math.min(productList.size(), 4); // Show all if showAll is true
	}
	private void addCartItem(CartItemDto cartItemDto) {
		apiService = ApiClient.getClient(context, false).create(ApiService.class);
		prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
		token = prefs.getString("jwt_token", null);
		// Convert CartItemDto to JSON and then to RequestBody
		Gson gson = new Gson();
		String cartItemJson = gson.toJson(cartItemDto);
		Log.d("API", "JSON: " + cartItemJson);
		RequestBody cartItemBody = RequestBody.create(cartItemJson, MediaType.parse("application/json"));


		// Make the API call
		Call<CartDto> call = apiService.addCartItem("Bearer " + token, cartItemBody);
		call.enqueue(new Callback<CartDto>() {
			@Override
			public void onResponse(Call<CartDto> call, Response<CartDto> response) {
				if (response.isSuccessful()) {
					// Handle successful response
					CartDto updatedCart = response.body();
					Log.d("huhuhu", "Cart item added successfully: " + updatedCart.toString());
				} else {
					Log.d("huhuhu", "Cart item fail added ");

				}
			}

			@Override
			public void onFailure(Call<CartDto> call, Throwable t) {
				// Handle failure (e.g., network errors)
				Log.e("API", "Add cart item request failed: " + t.getMessage());
			}
		});
	}

	public static class ProductViewHolder extends RecyclerView.ViewHolder {
		TextView productNameTextView, priceTextView, discountTextView, quantityTextView, weightTextView, productPrice;
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
			weightTextView = itemView.findViewById(R.id.productWeightTextView);
		}
	}


}
