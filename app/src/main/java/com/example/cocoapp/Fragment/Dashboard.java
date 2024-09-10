package com.example.cocoapp.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cocoapp.ActivityPage.Bottom_Navigation;
import com.example.cocoapp.Adapter.PetDashboardAdapter;
import com.example.cocoapp.Adapter.PetStatusAdapter;
import com.example.cocoapp.Adapter.ProductDashboardAdapter;
import com.example.cocoapp.Adapter.VeterinarianDashboardAdapter;
import com.example.cocoapp.Api.ApiClient;
import com.example.cocoapp.Api.ApiService;
import com.example.cocoapp.Object.CartDto;
import com.example.cocoapp.Object.CartItem;
import com.example.cocoapp.Object.CartManager;
import com.example.cocoapp.R;
import com.example.cocoapp.Object.Pet;
import com.example.cocoapp.Object.Product;
import com.example.cocoapp.Object.Veterinarian;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Dashboard extends Fragment implements OnMapReadyCallback, ProductDashboardAdapter.OnAddToCartListener {

	private RecyclerView recyclerViewPetStatus;
	private RecyclerView recyclerViewProduct;
	private RecyclerView recyclerViewPet;
	private RecyclerView recyclerViewVeterinarian;
	private PetStatusAdapter petStatusAdapter;
	private ProductDashboardAdapter productDashboardAdapter;
	private PetDashboardAdapter petDashboardAdapter;
	private VeterinarianDashboardAdapter veterinarianDashboardAdapter;
	private List<Pet> petList;
	private List<Product> productList;
	private List<Veterinarian> veterinarianList;
	private TextView seeAllFood;
	private TextView seeAllVet;
	private ImageView tracksPet;
	private ImageView checksPet;
	private MapView mapView;
	private GoogleMap googleMap;
	private String token;
	private ApiService apiService;



	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
		apiService = ApiClient.getClient(requireActivity(), false).create(ApiService.class);
		SharedPreferences prefs = requireActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
		token = prefs.getString("jwt_token", null);

		recyclerViewPetStatus = view.findViewById(R.id.petStatus_recycle_view);
		recyclerViewProduct = view.findViewById(R.id.product_recycle_view);
		recyclerViewPet = view.findViewById(R.id.pet_recycle_view);
		recyclerViewVeterinarian = view.findViewById(R.id.veterinarian_recylce_view);
		seeAllVet = view.findViewById(R.id.see_all_veterinarian);
		seeAllFood = view.findViewById(R.id.see_all_petfood);
		tracksPet = view.findViewById(R.id.trackspet);
		checksPet = view.findViewById(R.id.checkspet);

		mapView = view.findViewById(R.id.map_view);

		// Initialize MapView
		if (mapView != null) {
			mapView.onCreate(savedInstanceState);
			mapView.getMapAsync(this);
		}

		recyclerViewPetStatus.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
		recyclerViewProduct.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
		recyclerViewPet.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
		recyclerViewVeterinarian.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

		petList = new ArrayList<>();
		productList = new ArrayList<>();
		veterinarianList = new ArrayList<>();


		petStatusAdapter = new PetStatusAdapter(getContext(), petList);
		productDashboardAdapter = new ProductDashboardAdapter(getContext(), productList, false, this);
		petDashboardAdapter = new PetDashboardAdapter(getContext(), petList);
		veterinarianDashboardAdapter = new VeterinarianDashboardAdapter(getContext(), veterinarianList, false);

		recyclerViewPetStatus.setAdapter(petStatusAdapter);
		recyclerViewProduct.setAdapter(productDashboardAdapter);
		recyclerViewPet.setAdapter(petDashboardAdapter);
		recyclerViewVeterinarian.setAdapter(veterinarianDashboardAdapter);
		fetchPets(token);
		fetchProducts();

		seeAllVet.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				requireActivity().getSupportFragmentManager().beginTransaction()
						.replace(R.id.fragment_container, new VisitVeterinarian()).commit();

				((Bottom_Navigation) getActivity()).setSelectedTab(2);
			}
		});

		seeAllFood.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				requireActivity().getSupportFragmentManager().beginTransaction()
						.replace(R.id.fragment_container, new Shop()).commit();

				((Bottom_Navigation) getActivity()).setSelectedTab(3);
			}
		});

		tracksPet.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ArrayList<LatLng> coordinate = new ArrayList<LatLng>();
				for (int i = 0; i < petList.size(); i++){
					LatLng latLng = new LatLng(petList.get(i).getNorthCoordinate(), petList.get(i).getEastCoordinate());
					coordinate.add(latLng);
				}


				requireActivity().getSupportFragmentManager().beginTransaction()
						.replace(R.id.fragment_container,MapsFragment.newInstance(coordinate)).addToBackStack(null).commit();
			}
		});

		checksPet.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				requireActivity().getSupportFragmentManager().beginTransaction()
						.replace(R.id.fragment_container, new PetHealth()).addToBackStack(null).commit();
			}
		});

		return view;
	}

	public void fetchPets(String token) {
		Call<List<Pet>> call = apiService.fetchPets("Bearer " + token);
		call.enqueue(new Callback<List<Pet>>() {
			@Override
			public void onResponse(Call<List<Pet>> call, Response<List<Pet>> response) {
				if (response.isSuccessful() && response.body() != null) {
					List<Pet> pets = response.body();
					petList.clear();
					petList.addAll(pets);
					petDashboardAdapter.notifyDataSetChanged();
					// Process the list of pets
				} else {
					//Toast.makeText(getContext(), "Failed to fetch pets", Toast.LENGTH_SHORT).show();
					// Handle unsuccessful response
				}
			}

			@Override
			public void onFailure(Call<List<Pet>> call, Throwable t) {
				// Handle failure
			}

		});
	}


	private void fetchProducts() {
		apiService.fetchAllShopItems("Bearer " + token).enqueue(new Callback<List<Product>>() {
			@Override
			public void onResponse(@NonNull Call<List<Product>> call, Response<List<Product>> response) {
				if (response.isSuccessful() && response.body() != null) {
					productList.clear();
					productList.addAll(response.body());
					productDashboardAdapter.notifyDataSetChanged();
				} else {
					Log.e("API Error", "Response code: " + response.code() + " Message: " + response.message());
					Toast.makeText(getContext(), "Failed to fetch products", Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onFailure(@NonNull Call<List<Product>> call, @NonNull Throwable t) {
				Log.e("API Error fetch product", t.getMessage());
				if (getContext() != null) {
					Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
				}
			}
		});
	}


	@Override
	public void onMapReady(@NonNull GoogleMap googleMap) {
		this.googleMap = googleMap;

		LatLng location = new LatLng(-34, 151);
		googleMap.addMarker(new MarkerOptions().position(location).title("You are here"));
		googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
	}

	@Override
	public void onResume() {
		super.onResume();
		if (mapView != null) {
			mapView.onResume();
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		if (mapView != null) {
			mapView.onPause();
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mapView != null) {
			mapView.onDestroy();
		}
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
		if (mapView != null) {
			mapView.onLowMemory();
		}
	}

	@Override
	public void onAddToCart(Product product) {
		CartItem cartItem = new CartItem(
				product.getId(), // Make sure to pass the product ID
				product.getName(),
				product.getBrand(),
				product.getSize(),
				R.drawable.product_img,
				1
		);
		CartManager.getInstance().addItem(cartItem);
		updateCartItem(cartItem);
		Toast.makeText(getContext(), product.getName() + " added to cart", Toast.LENGTH_SHORT).show();
	}

	private void updateCartItem(CartItem cartItem) {
		// API call to update the cart
		ApiService apiService = ApiClient.getClient(requireContext(), false).create(ApiService.class);
		SharedPreferences prefs = requireActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
		String token = prefs.getString("jwt_token", null);

		String cartItemJson = new Gson().toJson(cartItem);
		RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), cartItemJson);

		apiService.updateCartItem("Bearer " + token, requestBody).enqueue(new Callback<CartDto>() {
			@Override
			public void onResponse(Call<CartDto> call, Response<CartDto> response) {
				if (response.isSuccessful()) {
					// Handle success scenario
					Toast.makeText(getContext(), "Cart updated successfully", Toast.LENGTH_SHORT).show();
				} else {
					// Handle error response
					Toast.makeText(getContext(), "Failed to update cart: " + response.message(), Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onFailure(Call<CartDto> call, Throwable t) {
				// Handle failure
				Toast.makeText(getContext(), "Error updating cart: " + t.getMessage(), Toast.LENGTH_SHORT).show();
			}
		});
	}
}