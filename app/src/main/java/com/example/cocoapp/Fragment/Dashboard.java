package com.example.cocoapp.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cocoapp.Adapter.PetDashboardAdapter;
import com.example.cocoapp.Adapter.PetStatusAdapter;
import com.example.cocoapp.Adapter.ProductAdapter;
import com.example.cocoapp.Adapter.ProductDashboardAdapter;
import com.example.cocoapp.Adapter.VeterinarianDashboardAdapter;
import com.example.cocoapp.R;
import com.example.cocoapp.object.Pet;
import com.example.cocoapp.object.Product;
import com.example.cocoapp.object.Veterinarian;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Dashboard extends Fragment {

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

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

		recyclerViewPetStatus = view.findViewById(R.id.petStatus_recycle_view);
		recyclerViewProduct = view.findViewById(R.id.product_recycle_view);
		recyclerViewPet = view.findViewById(R.id.pet_recycle_view);
		recyclerViewVeterinarian = view.findViewById(R.id.veterinarian_recylce_view);

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
		pic1.setImageResource(R.drawable.vet1);
		ImageView pic2 = new ImageView(getContext());
		pic2.setImageResource(R.drawable.vet2);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date lastVisit1 = null;
		Date lastVisit2 = null;

		try {
			lastVisit1 = sdf.parse("2024-08-15"); // Explicit date
			lastVisit2 = sdf.parse("2024-07-20"); // Explicit date
		} catch (ParseException e) {
			e.printStackTrace();
		}

		veterinarianList.add(new Veterinarian("Dr. Brown", "Bachelor of Veterinary Science", 4.8f, 120, 12, "1.8 km", "$110", "Mon-Sat 8 AM - 4 PM", pic1, lastVisit1));
		veterinarianList.add(new Veterinarian("Dr. Johnson", "Doctor of Veterinary Medicine", 4.6f, 90, 9, "2.0 km", "$115", "Mon-Fri 10 AM - 6 PM", pic2, lastVisit2));

		petStatusAdapter = new PetStatusAdapter(getContext(), petList);
		productDashboardAdapter = new ProductDashboardAdapter(getContext(), productList);
		petDashboardAdapter = new PetDashboardAdapter(getContext(), petList);
		veterinarianDashboardAdapter = new VeterinarianDashboardAdapter(getContext(), veterinarianList);

		recyclerViewPetStatus.setAdapter(petStatusAdapter);
		recyclerViewProduct.setAdapter(productDashboardAdapter);
		recyclerViewPet.setAdapter(petDashboardAdapter);
		recyclerViewVeterinarian.setAdapter(veterinarianDashboardAdapter);

		return view;
	}
}
