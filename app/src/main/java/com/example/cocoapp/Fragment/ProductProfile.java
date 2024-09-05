package com.example.cocoapp.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.cocoapp.Api.ApiClient;
import com.example.cocoapp.Api.ApiService;
import com.example.cocoapp.Object.Product;
import com.example.cocoapp.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductProfile extends Fragment {
	private String productId;
	private String token;
	private ApiService apiService;

	public ProductProfile() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_view_food, container, false);
		if (getArguments() != null) {
			productId = getArguments().getString("product_id");
		}
		apiService = ApiClient.getClient(requireActivity(), false).create(ApiService.class);
		SharedPreferences prefs = requireActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
		token = prefs.getString("jwt_token", null);
		ImageButton backButton = view.findViewById(R.id.back_button);
		Button decreaseButton = view.findViewById(R.id.decrementButton);
		Button increaseButton = view.findViewById(R.id.incrementButton);
		TextView quantityTextView = view.findViewById(R.id.quantityTextView);
		Button addToCartButton = view.findViewById(R.id.cart_button);
		ImageButton shoppingCartButton = view.findViewById(R.id.shopping_cart_plus_button);

		backButton.setOnClickListener(v ->{
				getActivity().getSupportFragmentManager().popBackStack();
			}
		);

		addToCartButton.setOnClickListener(v -> {
			int quantity = Integer.parseInt(quantityTextView.getText().toString());
			quantityTextView.setText(String.valueOf(++quantity));
			Toast.makeText(requireContext(), "Product added to cart", Toast.LENGTH_SHORT).show();
		});

		increaseButton.setOnClickListener(v -> {
			int quantity = Integer.parseInt(quantityTextView.getText().toString());
			quantityTextView.setText(String.valueOf(++quantity));
		});

		decreaseButton.setOnClickListener(v -> {
			int quantity = Integer.parseInt(quantityTextView.getText().toString());
			if (quantity > 0) {
				quantityTextView.setText(String.valueOf(--quantity));
			} else {
				quantityTextView.setText("0");
			}
		});

		shoppingCartButton.setOnClickListener(v -> {
			FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
			transaction.replace(R.id.fragment_container, new ViewCart())
					.addToBackStack(null)
					.commit();
		});

		fetchProductById(productId);
		return view;
	}

	private void fetchProductById(String productId) {
		apiService.fetchProductById("Bearer " + token, productId).enqueue(new Callback<Product>() {
			@Override
			public void onResponse(Call<Product> call, Response<Product> response) {
				if (response.isSuccessful()) {
					Product product = response.body();
					if (product != null) {
						displayProductDetails(product);
					}
				} else {
					Log.e("API Error fetch product by id", "Response code: " + response.code() + " Message: " + response.message());
				}
			}

			@Override
			public void onFailure(Call<Product> call, Throwable t) {
				Log.e("API Error fetch product by id", t.getMessage());
			}
		});
	}

	private void displayProductDetails(Product product) {
		// Initialize the views
		TextView productNameTextView = getView().findViewById(R.id.dogfoodname);
		TextView brandTextView = getView().findViewById(R.id.brandname);
		TextView priceTextView = getView().findViewById(R.id.price);
		TextView descriptionTextView = getView().findViewById(R.id.dogfooddescription);
		ImageView productImageView = getView().findViewById(R.id.imagedogfood);

		// Set the product data to views
		productNameTextView.setText(product.getName());
		brandTextView.setText(product.getBrand());
		priceTextView.setText(String.valueOf(product.getPrice()+ " VND"));
		descriptionTextView.setText(product.getDescription()); // Assuming getDescription()

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
					Glide.with(getContext())
							.load(baseUrl+fileName)
							.error(R.drawable.dog1)
							.into(productImageView);

					Log.e("Full Image URL", baseUrl + fileName);
				} else {
					Log.e("API Error", "Response code: " + response.code() + " Message: " + response.message());
					Toast.makeText(getContext(), "Failed to access image", Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onFailure(Call<Void> call, Throwable t) {
				Log.e("API Error", "Error accessing image: " + t.getMessage());
				Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
			}
		});
	}



}