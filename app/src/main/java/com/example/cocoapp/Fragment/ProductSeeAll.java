package com.example.cocoapp.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.cocoapp.Adapter.ProductAdapter;
import com.example.cocoapp.Api.ApiClient;
import com.example.cocoapp.Api.ApiService;
import com.example.cocoapp.Object.CartDto;
import com.example.cocoapp.Object.CartItemDto;
import com.example.cocoapp.Object.Product;
import com.example.cocoapp.R;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProductSeeAll#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductSeeAll extends Fragment{

	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";

	// TODO: Rename and change types of parameters
	private String mParam1;
	private String mParam2;
	private ProductAdapter recommendAdapter;
	private RecyclerView recyclerViewRecommend;
	private List<Product> productList;
	private List<CartItemDto> cartItemList;
	private ApiService apiService;
	private SharedPreferences prefs;
	private String token;



	public ProductSeeAll() {
		// Required empty public constructor
	}

	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @param param1 Parameter 1.

	 * @return A new instance of fragment ProductSeeAll.
	 */
	// TODO: Rename and change types and number of parameters
	public static ProductSeeAll newInstance(List<Product> param1) {
		ProductSeeAll fragment = new ProductSeeAll();
		Bundle args = new Bundle();
		args.putSerializable(ARG_PARAM1, (Serializable) param1);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			productList = new ArrayList<>();
			productList = (List<Product>) getArguments().getSerializable(ARG_PARAM1);  // Retrieve the Pet object
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_product_see_all, container, false);
		prefs = requireActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
		token = prefs.getString("jwt_token", null);
		apiService = ApiClient.getClient(requireActivity(), false).create(ApiService.class);
		recyclerViewRecommend = view.findViewById(R.id.recyclerView);
		recyclerViewRecommend.setLayoutManager(new GridLayoutManager(getContext(), 2));
		ImageButton backbtn = view.findViewById(R.id.back_button);
		ImageButton filterButton = view.findViewById(R.id.filter_btn);
		ImageButton shop_btn = view.findViewById(R.id.cart_btn);
		cartItemList = new ArrayList<>();

		shop_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				fetchCart(2);
			}
		});
		backbtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				fetchCart(1);
			}
		});

		filterButton.setOnClickListener(v -> showSortDialog());

		recommendAdapter = new ProductAdapter(getContext(), productList, true);

		recyclerViewRecommend.setAdapter(recommendAdapter);
		//fetchProducts();
		return view;
	}

	private void fetchCart (int i) {


			Call<CartDto> call = apiService.getCart("Bearer " + token);
			call.enqueue(new Callback<CartDto>() {
				@Override
				public void onResponse(Call<CartDto> call, Response<CartDto> response) {
					if (response.isSuccessful()) {
						// Handle successful response
						CartDto cart = response.body();
						cartItemList.clear();
						cartItemList.addAll(cart.getItems());
						for (Product item : productList) {
							for (CartItemDto cartItem : cartItemList) {
								if (Objects.equals(cartItem.getItem().getId(), item.getId())) {
									cartItem.setQuantity(item.getCurrentQuantity());
									cartItem.setItem(item);
									//Log.d("hihi", String.valueOf(item.getCurrentQuantity()));
									updateCart(cartItem);
									break;
								}

							}
						}
						if (i == 1){
							getActivity().getSupportFragmentManager().popBackStack();
						}else{
							requireActivity().getSupportFragmentManager().beginTransaction()
									.replace(R.id.fragment_container, ViewCart.newInstance(cartItemList))
									.addToBackStack(null)
									.commit();
						}
						Log.d("API", "Fetched cart successfully: " + cart.toString());
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


	private void showSortDialog() {
		Dialog dialog = new Dialog(getContext());
		dialog.setContentView(R.layout.dialog_sort_option_product);

		RadioGroup radioGroupSort = dialog.findViewById(R.id.radioGroupSort);
		Button btnApplySort = dialog.findViewById(R.id.btnApplySort);

		btnApplySort.setOnClickListener(v -> {
			int selectedId = radioGroupSort.getCheckedRadioButtonId();

			if (selectedId == R.id.radioSortByWeight) {
				sortByWeight();
			} else if (selectedId == R.id.radioSortByPrice) {
				sortByPrice();
			} else sortByDefault();

			dialog.dismiss();
		});

		dialog.show();
	}
	private void updateCart(CartItemDto item) {
		Gson gson = new Gson();
		String cartItemJson = gson.toJson(item);
		RequestBody cartItemBody = RequestBody.create(
				cartItemJson, MediaType.parse("application/json"));

		Call<CartDto> call = apiService.updateCartItem("Bearer " + token, cartItemBody);
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
					for (Product p : productList) {
						if (p.getSizeObject().getUnit().equals("g"))
							p.setSize(String.valueOf(p.getSizeObject().getValue() / 1000));
						else p.setSize(String.valueOf(p.getSizeObject().getValue()));
					}
					recommendAdapter.notifyDataSetChanged();

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

	private void sortByWeight() {
		List<Product> tmp = new ArrayList<>();
		tmp.addAll(productList);
		Collections.sort(tmp, new Comparator<Product>() {
			@Override
			public int compare(Product p1, Product p2) {

				return Integer.compare(Integer.parseInt(p2.getSize()), Integer.parseInt(p1.getSize())); // descending order
			}
		});
		recommendAdapter = new ProductAdapter(getContext(), tmp, true);

		recyclerViewRecommend.setAdapter(recommendAdapter);

	}

	private void sortByPrice() {
		List<Product> tmp = new ArrayList<>();
		tmp.addAll(productList);
		Collections.sort(tmp, new Comparator<Product>() {
			@Override
			public int compare(Product p1, Product p2) {
				return Float.compare(p2.getPrice(), p1.getPrice()); // descending order
			}
		});
		recommendAdapter = new ProductAdapter(getContext(), tmp, true);

		recyclerViewRecommend.setAdapter(recommendAdapter);
	}

	private void sortByDefault() {
		List<Product> tmp = new ArrayList<>();
		tmp.addAll(productList);
		recommendAdapter = new ProductAdapter(getContext(), tmp, true);
	}


//	private void updateCartItem(CartItem cartItem) {
//		// API call to update the cart
//		ApiService apiService = ApiClient.getClient(requireContext(), false).create(ApiService.class);
//		SharedPreferences prefs = requireActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
//		String token = prefs.getString("jwt_token", null);
//
//		// Convert CartItem to JSON string
//		String cartItemJson = new Gson().toJson(cartItem);
//		// Create RequestBody from the JSON string
//		RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), cartItemJson);
//
//		apiService.updateCartItem("Bearer " + token, requestBody).enqueue(new Callback<CartDto>() {
//			@Override
//			public void onResponse(Call<CartDto> call, Response<CartDto> response) {
//				if (response.isSuccessful()) {
//					// Handle successful update
//					Toast.makeText(getContext(), "Cart updated successfully", Toast.LENGTH_SHORT).show();
//				} else {
//					// Handle error response
//					Toast.makeText(getContext(), "Failed to update cart: " + response.message(), Toast.LENGTH_SHORT).show();
//				}
//			}
//
//			@Override
//			public void onFailure(Call<CartDto> call, Throwable t) {
//				// Handle failure
//				Toast.makeText(getContext(), "Error updating cart: " + t.getMessage(), Toast.LENGTH_SHORT).show();
//			}
//		});
//}

}