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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cocoapp.Adapter.ProductAdapter;
import com.example.cocoapp.Adapter.ProductDashboardAdapter;
import com.example.cocoapp.Api.ApiClient;
import com.example.cocoapp.Api.ApiService;
import com.example.cocoapp.Object.CartItem;
import com.example.cocoapp.Object.CartManager;
import com.example.cocoapp.Object.Product;
import com.example.cocoapp.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Shop extends Fragment implements ProductAdapter.OnAddToCartListener, ProductDashboardAdapter.OnAddToCartListener {

	private RecyclerView recyclerViewRecommend;
	private RecyclerView recyclerViewTopSelling;
	private ProductAdapter recommendAdapter;
	private ProductDashboardAdapter topSellingAdapter;
	private List<Product> productList;
	private List<Product> cartList;
	private ImageView cartButton;

	public Shop() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_shop, container, false);
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
		cartList = new ArrayList<>();

//		String imageView3 = "android.resource://" + getContext().getPackageName() + "/" + R.drawable.product_img;
//		String imageView4 = "android.resource://" + getContext().getPackageName() + "/" + R.drawable.product_img;
//		String imageView2 = "android.resource://" + getContext().getPackageName() + "/" + R.drawable.product_img;
//		String imageView1 = "android.resource://" + getContext().getPackageName() + "/" + R.drawable.product_img;
//
//
//		productList.add(new Product(10, imageView1, 20F, "Dog Food", "900g", "Brand A", 100));
//		productList.add(new Product(20, imageView2, 43F, "Cat Food", "500g", "Brand B", 100));
//		productList.add(new Product(0, imageView3, 30F, "Bird Food", "1kg", "Brand C", 100));
//		productList.add(new Product(0, imageView4, 13F, "Fish Food", "300g", "Brand D", 100));
//
		recommendAdapter = new ProductAdapter(getContext(), productList, this,true);
		topSellingAdapter = new ProductDashboardAdapter(getContext(), productList, false, this);
		recyclerViewRecommend.setAdapter(recommendAdapter);
		recyclerViewTopSelling.setAdapter(topSellingAdapter);

		fetchProducts();

		return view;
	}

	@Override
	public void onAddToCart(Product product) {
		CartItem cartItem = new CartItem(
				product.getName(),
				product.getBrand(),
				product.getSize(),
				R.drawable.product_img,
				product.getQuantity()
		);
		CartManager.getInstance().addItem(cartItem);
	}

	private void openCart() {
		ViewCart viewCartFragment = new ViewCart();
		Bundle bundle = new Bundle();
		bundle.putSerializable("cartList", new ArrayList<>(cartList));
		viewCartFragment.setArguments(bundle);
		requireActivity().getSupportFragmentManager().beginTransaction()
				.replace(R.id.fragment_container, viewCartFragment)
				.addToBackStack(null)
				.commit();
	}


	private void fetchProducts() {
		ApiService apiService = ApiClient.getClient(requireActivity(), false).create(ApiService.class);
		SharedPreferences prefs = requireActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
		String token = prefs.getString("jwt_token", null);

		apiService.fetchAllShopItems("Bearer " + token).enqueue(new Callback<List<Product>>() {
			@Override
			public void onResponse(@NonNull Call<List<Product>> call, @NonNull Response<List<Product>> response) {
				if (response.isSuccessful() && response.body() != null) {
					productList.clear();
					productList.addAll(response.body());
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
