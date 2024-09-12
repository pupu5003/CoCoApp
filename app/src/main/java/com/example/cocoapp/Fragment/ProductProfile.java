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
import com.example.cocoapp.Object.CartDto;
import com.example.cocoapp.Object.CartItemDto;
import com.example.cocoapp.Object.Product;
import com.example.cocoapp.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductProfile extends Fragment {
	private String productId;
	private String token;
	private ApiService apiService;
	private SharedPreferences prefs;
	private List<CartItemDto> cartItemsList = new ArrayList<>();
	private Product product;
	private TextView quantityTextView;

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
		prefs = requireActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
		token = prefs.getString("jwt_token", null);
		fetchCartInit();
		ImageButton backButton = view.findViewById(R.id.back_button);
		Button decreaseButton = view.findViewById(R.id.decrementButton);
		Button increaseButton = view.findViewById(R.id.incrementButton);
		quantityTextView = view.findViewById(R.id.quantityTextView);
		Button addToCartButton = view.findViewById(R.id.cart_button);
		ImageButton shoppingCartButton = view.findViewById(R.id.shopping_cart_plus_button);
		ImageView shop = view.findViewById(R.id.shopping_cart_plus_button);



		backButton.setOnClickListener(v ->{
				getActivity().getSupportFragmentManager().popBackStack();
			}
		);

		addToCartButton.setOnClickListener(v -> {
			fetchCart();
			Toast.makeText(requireContext(), "Product added to cart", Toast.LENGTH_SHORT).show();
		});
		shop.setOnClickListener(v -> {
			Log.d("hihi", String.valueOf(cartItemsList.size()));
			requireActivity().getSupportFragmentManager().beginTransaction()
					.replace(R.id.fragment_container, ViewCart.newInstance(cartItemsList))
					.addToBackStack(null)
					.commit();

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
			transaction.replace(R.id.fragment_container, ViewCart.newInstance(cartItemsList))
					.addToBackStack(null)
					.commit();
		});

		fetchProductById(productId);
		return view;
	}
	private void fetchCart() {

		Call<CartDto> call = apiService.getCart("Bearer " + token);
		call.enqueue(new Callback<CartDto>() {
			@Override
			public void onResponse(Call<CartDto> call, Response<CartDto> response) {
				if (response.isSuccessful()) {
					// Handle successful response
					CartDto cart = response.body();
					cartItemsList.clear();
					cartItemsList.addAll(cart.getItems());
					Boolean isExist = false;
					for (CartItemDto cartItem : cartItemsList)
						if (Objects.equals(cartItem.getItem().getId(), productId)) {
							int tmp = quantityTextView.getText().toString().equals("") ? 0 : Integer.parseInt(quantityTextView.getText().toString());
							cartItem.setQuantity(product.getCurrentQuantity() + tmp);
							product.setCurrentQuantity(product.getCurrentQuantity() + tmp);
							updateCart(cartItem);
							isExist = true;
							break;
						}
					if (!isExist) {
						CartItemDto newCartItem = new CartItemDto();
						newCartItem.setItem(product);
						newCartItem.setQuantity(Integer.parseInt(quantityTextView.getText().toString()));
						cartItemsList.add(newCartItem);
						addCartItem(newCartItem);
					}
				} else {
					Log.d("API", "Fetched cart unsuccessfully: ");
				}
			}

			@Override
			public void onFailure(Call<CartDto> call, Throwable t) {
				// Handle failure (e.g., network errors)
				Log.e("API", "Fetch cart request failed: " + t.getMessage());
			}
		});
	}
	private void fetchCartInit() {

		Call<CartDto> call = apiService.getCart("Bearer " + token);
		call.enqueue(new Callback<CartDto>() {
			@Override
			public void onResponse(Call<CartDto> call, Response<CartDto> response) {
				if (response.isSuccessful()) {
					// Handle successful response
					CartDto cart = response.body();
					cartItemsList.clear();
					cartItemsList.addAll(cart.getItems());
					for (CartItemDto cartItem : cartItemsList){
						if (Objects.equals(cartItem.getItem().getId(), productId)) {
							product.setCurrentQuantity(cartItem.getQuantity());
							break;
						}
					}
				} else {
					Log.d("API", "Fetched cart unsuccessfully: ");
				}
			}

			@Override
			public void onFailure(Call<CartDto> call, Throwable t) {
				// Handle failure (e.g., network errors)
				Log.e("API", "Fetch cart request failed: " + t.getMessage());
			}
		});
	}
	private void addCartItem(CartItemDto cartItemDto) {
		apiService = ApiClient.getClient(requireActivity(), false).create(ApiService.class);
		prefs = requireActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
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
	private void updateCart(CartItemDto item) {
		Gson gson = new Gson();
		String cartItemJson = gson.toJson(item);
		RequestBody cartItemBody = RequestBody.create(
				cartItemJson, MediaType.parse("application/json"));

		Call<CartDto> call = apiService.updateCartItem("Bearer " + token,cartItemBody);
		call.enqueue(new Callback<CartDto>() {
			@Override
			public void onResponse(Call<CartDto> call, Response<CartDto> response) {
				if (response.isSuccessful()) {
					// Handle successful response
					CartDto updatedCart = response.body();

					Log.d("huhuhu", "Cart updated successfully: " + updatedCart.toString());
					// Do something with the updated cart
				} else {
					Log.d("huhuhu", "Cart updated fail: ");
					// Handle error case
				}
			}

			@Override
			public void onFailure(Call<CartDto> call, Throwable t) {
				// Handle failure
			}
		});
	}


	private void fetchProductById(String productId) {
		apiService.fetchProductById("Bearer " + token, productId).enqueue(new Callback<Product>() {
			@Override
			public void onResponse(Call<Product> call, Response<Product> response) {
				if (response.isSuccessful()) {
					product = response.body();
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
		RatingBar ratingBar = getView().findViewById(R.id.rating_layout);
		TextView rate = getView().findViewById(R.id.rating_text);

		// Set the product data to views
		productNameTextView.setText(product.getName());
		brandTextView.setText(product.getBrand());
		priceTextView.setText(String.valueOf(product.getPrice()+ " VND"));
		descriptionTextView.setText(product.getDescription()); // Assuming getDescription()

		String baseUrl = "http://172.28.102.169:8080";
		Glide.with(getContext())
							.load(baseUrl+product.getProductImageUrl())
							.error(R.drawable.dog1)
							.into(productImageView);
		ratingBar.setRating(product.getRating());
		rate.setText(String.format("%.2f", product.getRating()));



//
//		String baseUrl = "http://172.28.102.169:8080";
//		String fileName = product.getProductImageUrl();
//		String basePath = "/file/";
//		fileName = fileName.substring(basePath.length());
//		apiService.fetchImageFile("Bearer " + token, fileName).enqueue(new Callback<Void>() {
//			@Override
//			public void onResponse(Call<Void> call, Response<Void> response) {
//				if (response.isSuccessful()) {
//					// Load image with Glide
//					String fileName = product.getProductImageUrl();
//					Glide.with(getContext())
//							.load(baseUrl+fileName)
//							.error(R.drawable.dog1)
//							.into(productImageView);
//
//					Log.e("Full Image URL", baseUrl + fileName);
//				} else {
//					Log.e("API Error", "Response code: " + response.code() + " Message: " + response.message());
//					Toast.makeText(getContext(), "Failed to access image", Toast.LENGTH_SHORT).show();
//				}
//			}
//
//			@Override
//			public void onFailure(Call<Void> call, Throwable t) {
//				Log.e("API Error", "Error accessing image: " + t.getMessage());
//				Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
//			}
//		});
	}



}