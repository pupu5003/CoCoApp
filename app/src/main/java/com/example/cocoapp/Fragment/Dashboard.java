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
import com.example.cocoapp.Adapter.CartAdapter;
import com.example.cocoapp.Adapter.PetDashboardAdapter;
import com.example.cocoapp.Adapter.PetStatusAdapter;
import com.example.cocoapp.Adapter.ProductDashboardAdapter;
import com.example.cocoapp.Adapter.VeterinarianDashboardAdapter;
import com.example.cocoapp.Api.ApiClient;
import com.example.cocoapp.Api.ApiService;
import com.example.cocoapp.Object.CartDto;
import com.example.cocoapp.Object.CartItemDto;
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
import java.util.Objects;

import com.google.gson.Gson;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Dashboard extends Fragment implements OnMapReadyCallback {

	private RecyclerView recyclerViewPetStatus;
	private RecyclerView recyclerViewPet;
	private RecyclerView recyclerViewVeterinarian;
	private RecyclerView recyclerViewCart;
	private PetStatusAdapter petStatusAdapter;
	private ProductDashboardAdapter productDashboardAdapter;
	private PetDashboardAdapter petDashboardAdapter;
	private VeterinarianDashboardAdapter veterinarianDashboardAdapter;
	private List<Pet> petList;
	private List<Veterinarian> veterinarianList;
	private List<CartItemDto> cartItemsList;
	private CartAdapter cartAdapter;
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
		recyclerViewCart = view.findViewById(R.id.product_recycle_view);
		recyclerViewPet = view.findViewById(R.id.pet_recycle_view);
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
		recyclerViewPet.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
		recyclerViewCart.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

		petList = new ArrayList<>();
		cartItemsList = new ArrayList<>();

		petStatusAdapter = new PetStatusAdapter(getContext(), petList);
		petDashboardAdapter = new PetDashboardAdapter(getContext(), petList);
		cartAdapter = new CartAdapter(cartItemsList, getContext(), false);

		recyclerViewPetStatus.setAdapter(petStatusAdapter);
		recyclerViewPet.setAdapter(petDashboardAdapter);
		recyclerViewCart.setAdapter(cartAdapter);
		fetchPets(token);
		fetchCart();

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
						.replace(R.id.fragment_container,MapsFragment.newInstance(coordinate,0)).addToBackStack(null).commit();
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

	private void fetchCart() {
		Call<CartDto> call = apiService.getCart("Bearer " + token);
		call.enqueue(new Callback<CartDto>() {
			@Override
			public void onResponse(Call<CartDto> call, Response<CartDto> response) {
				if (response.isSuccessful()) {
					CartDto cart = response.body();
					cartItemsList.clear();
					cartItemsList.addAll(cart.getItems());
					cartAdapter.notifyDataSetChanged();
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
}