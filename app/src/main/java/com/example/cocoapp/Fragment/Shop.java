package com.example.cocoapp.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cocoapp.Adapter.ProductAdapter;
import com.example.cocoapp.Adapter.ProductDashboardAdapter;
import com.example.cocoapp.Api.ApiClient;
import com.example.cocoapp.Api.ApiService;
import com.example.cocoapp.Object.CartItemDto;
import com.example.cocoapp.Object.Product;
import com.example.cocoapp.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Shop extends Fragment {

	private RecyclerView recyclerViewRecommend;
	private RecyclerView recyclerViewTopSelling;
	private ProductAdapter recommendAdapter;
	private ProductDashboardAdapter topSellingAdapter;
	private List<Product> productList,topsellingList;
	private ImageView cartButton;
	private ApiService apiService;
	private SharedPreferences prefs;
	private String token;

	public Shop() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_shop, container, false);
		prefs = requireActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
		token = prefs.getString("jwt_token", null);
		apiService = ApiClient.getClient(requireActivity(), false).create(ApiService.class);
		cartButton = view.findViewById(R.id.cart_ic);
		TextView seeAll = view.findViewById(R.id.see_all);
		cartButton.setOnClickListener(v -> openCart());

		seeAll.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				requireActivity().getSupportFragmentManager().beginTransaction()
						.replace(R.id.fragment_container,new ProductSeeAll()).addToBackStack(null)
						.commit();
			}
		});


		recyclerViewRecommend = view.findViewById(R.id.productRecommend_recycle_view);
		recyclerViewTopSelling = view.findViewById(R.id.productTopSelling_recycle_view);

		recyclerViewTopSelling.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
		recyclerViewRecommend.setLayoutManager(new GridLayoutManager(getContext(), 2));

		productList = new ArrayList<>();
		topsellingList = new ArrayList<>();


		recommendAdapter = new ProductAdapter(getContext(), productList,true);
		topSellingAdapter = new ProductDashboardAdapter(getContext(), topsellingList, true);
		recyclerViewRecommend.setAdapter(recommendAdapter);
		recyclerViewTopSelling.setAdapter(topSellingAdapter);

		fetchProducts();

		return view;
	}

	private void updateCartItem(CartItemDto cartItem) {
		// API call to update the cart
		ApiService apiService = ApiClient.getClient(requireContext(), false).create(ApiService.class);
		SharedPreferences prefs = requireActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
		String token = prefs.getString("jwt_token", null);

		// Convert CartItem to JSON string
		String cartItemJson = new Gson().toJson(cartItem);
		// Create RequestBody from the JSON string
		RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), cartItemJson);

		apiService.updateCartItem("Bearer " + token, requestBody).enqueue(new Callback<CartItemDto>() {
			@Override
			public void onResponse(Call<CartItemDto> call, Response<CartItemDto> response) {
				if (response.isSuccessful()) {
					// Handle success scenario
					Toast.makeText(getContext(), "Cart updated successfully", Toast.LENGTH_SHORT).show();
				} else {
					// Handle error response
					Toast.makeText(getContext(), "Failed to update cart: " + response.message(), Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onFailure(Call<CartItemDto> call, Throwable t) {
				// Handle failure
				Toast.makeText(getContext(), "Error updating cart: " + t.getMessage(), Toast.LENGTH_SHORT).show();
			}
		});
	}

	private void openCart() {
		for (Product item : productList) {
			//Log.d("product", item.getName()+" "+item.getCurrentQuantity());
			//Log.d("Item", item.toString());
			CartItemDto tmp = new CartItemDto();
			tmp.setQuantity(item.getCurrentQuantity());
			//Log.d("Item", tmp.getQuantity().toString());
			tmp.setItem(item);
			updateCart(tmp);
		}
		requireActivity().getSupportFragmentManager().beginTransaction()
				.replace(R.id.fragment_container, new ViewCart())
				.addToBackStack(null)
				.commit();
	}

	private void updateCart(CartItemDto item) {
		Gson gson = new Gson();
		String cartItemJson = gson.toJson(item);
		RequestBody cartItemBody = RequestBody.create(
				cartItemJson, MediaType.parse("application/json"));

		Call<CartItemDto> call = apiService.updateCartItem("Bearer" + token,cartItemBody);
		call.enqueue(new Callback<CartItemDto>() {
			@Override
			public void onResponse(Call<CartItemDto> call, Response<CartItemDto> response) {
				if (response.isSuccessful()) {
					// Handle successful response
					CartItemDto updatedCart = response.body();

					Log.d("API", "Cart updated successfully: " + updatedCart.toString());
					// Do something with the updated cart
				} else {
					Log.d("API", "Cart updated fail: ");
					// Handle error case
				}
			}

			@Override
			public void onFailure(Call<CartItemDto> call, Throwable t) {
				// Handle failure
			}
		});
	}


	private void fetchProducts() {
		ApiService apiService = ApiClient.getClient(requireActivity(), false).create(ApiService.class);
		SharedPreferences prefs = requireActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
		String token = prefs.getString("jwt_token", null);

		apiService.fetchAllShopItems("Bearer " + token).enqueue(new Callback<List<Product>>() {
			@Override
			public void onResponse(@NonNull Call<List<Product>> call, @NonNull Response<List<Product>> response) {
				if (response.isSuccessful() && response.body() != null) {
					List<Product> tmp = new ArrayList<>();
					tmp.addAll(response.body());
					productList.clear();
					productList.addAll(response.body());
					Collections.sort(tmp, new Comparator<Product>() {
						@Override
						public int compare(Product p1, Product p2) {
							return Integer.compare(p2.getQuantity(), p1.getQuantity()); // descending order
						}
					});
					for (int i = 0; i < Math.min(4, productList.size()); i++) {
						topsellingList.add(tmp.get(i));
					}
					recommendAdapter.notifyDataSetChanged();
					topSellingAdapter.notifyDataSetChanged();
				} else {
					Log.e("API Error", "Response code: " + response.code() + " Message: " + response.message());
					Toast.makeText(getContext(), "Failed to fetch products", Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onFailure(@NonNull Call<List<Product>> call, @NonNull Throwable t) {
				Log.e("API Error fetch product", t.getMessage());
				Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
			}
		});
	}

}
