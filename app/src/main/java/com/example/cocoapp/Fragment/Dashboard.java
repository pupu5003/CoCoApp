package com.example.cocoapp.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cocoapp.ActivityPage.Bottom_Navigation;
import com.example.cocoapp.Adapter.PetDashboardAdapter;
import com.example.cocoapp.Adapter.PetStatusAdapter;
import com.example.cocoapp.Adapter.ProductDashboardAdapter;
import com.example.cocoapp.Adapter.VeterinarianDashboardAdapter;
import com.example.cocoapp.Object.CartItem;
import com.example.cocoapp.Object.CartManager;
import com.example.cocoapp.R;
import com.example.cocoapp.Object.Pet;
import com.example.cocoapp.Object.Product;
import com.example.cocoapp.Object.Veterinarian;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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


	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

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

		ImageView imageView1 = new ImageView(getContext());
		imageView1.setImageResource(R.drawable.dog1);
		ImageView imageView2 = new ImageView(getContext());
		imageView2.setImageResource(R.drawable.dog2);

		// Add sample data for pets
		petList.add(new Pet("Buddy", imageView1, "Golden Retriever", 5, "Male", "Golden", 60.0f, 30.0f, 75, 50, 80));
		petList.add(new Pet("Lucy", imageView2, "Labrador", 4, "Female", "Black", 55.0f, 25.0f, 60, 40, 70));

		// Add sample data for products
		ImageView imageView3 = new ImageView(getContext());
		imageView3.setImageResource(R.drawable.product_img);
		ImageView imageView4 = new ImageView(getContext());
		imageView4.setImageResource(R.drawable.product_img2);

		productList.add(new Product("", imageView3, "$20", "Dog Food", "900g", "Brand A"));
		productList.add(new Product("", imageView4, "$15", "Cat Food", "500g", "Brand B"));

		ImageView pic1 = new ImageView(getContext());
		pic1.setImageResource(R.drawable.doctor_ava);
		ImageView pic2 = new ImageView(getContext());
		pic2.setImageResource(R.drawable.vet2);

		veterinarianList.add(new Veterinarian("Dr. Brown", "Bachelor of Veterinary Science", 4.8f, 120, 12, "1.8 km", "$110", "Mon-Sat 8 AM - 4 PM", pic1, "2024-08-15"));
		veterinarianList.add(new Veterinarian("Dr. Johnson", "Doctor of Veterinary Medicine", 4.6f, 90, 9, "2.0 km", "$115", "Mon-Fri 10 AM - 6 PM", pic2, "2024-07-20"));
		veterinarianList.add(new Veterinarian("Dr. Brown", "Bachelor of Veterinary Science", 4.8f, 120, 12, "1.8 km", "$110", "Mon-Sat 8 AM - 4 PM", pic1, "2024-08-15"));
		veterinarianList.add(new Veterinarian("Dr. Johnson", "Doctor of Veterinary Medicine", 4.6f, 90, 9, "2.0 km", "$115", "Mon-Fri 10 AM - 6 PM", pic2, "2024-07-20"));

		petStatusAdapter = new PetStatusAdapter(getContext(), petList);
		productDashboardAdapter = new ProductDashboardAdapter(getContext(), productList, false, this);
		petDashboardAdapter = new PetDashboardAdapter(getContext(), petList);
		veterinarianDashboardAdapter = new VeterinarianDashboardAdapter(getContext(), veterinarianList, false);

		recyclerViewPetStatus.setAdapter(petStatusAdapter);
		recyclerViewProduct.setAdapter(productDashboardAdapter);
		recyclerViewPet.setAdapter(petDashboardAdapter);
		recyclerViewVeterinarian.setAdapter(veterinarianDashboardAdapter);

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
				requireActivity().getSupportFragmentManager().beginTransaction()
						.replace(R.id.fragment_container, new MapsFragment()).addToBackStack(null).commit();
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
				product.getName(),
				product.getBrand(),
				product.getWeight(),
				R.drawable.product_img,
				product.getQuantity()
		);
		CartManager.getInstance().addItem(cartItem);
		Toast.makeText(getContext(), product.getName() + " added to cart", Toast.LENGTH_SHORT).show();
	}
}

