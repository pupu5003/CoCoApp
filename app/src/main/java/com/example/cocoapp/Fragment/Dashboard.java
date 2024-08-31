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

import com.example.cocoapp.Adapter.PetStatusAdapter;
import com.example.cocoapp.Adapter.ProductAdapter;
import com.example.cocoapp.Adapter.ProductDashboardAdapter;
import com.example.cocoapp.R;
import com.example.cocoapp.object.Pet;
import com.example.cocoapp.object.Product;

import java.util.ArrayList;
import java.util.List;

public class Dashboard extends Fragment {

	private RecyclerView recyclerViewPetStatus;
	private RecyclerView recyclerViewProduct;
	private PetStatusAdapter petStatusAdapter;
	private ProductDashboardAdapter productDashboardAdapter;
	private List<Pet> petList;
	private List<Product> productList;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

		recyclerViewPetStatus = view.findViewById(R.id.petStatus_recycle_view);
		recyclerViewProduct = view.findViewById(R.id.product_recycle_view);

		recyclerViewPetStatus.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
		recyclerViewProduct.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

		petList = new ArrayList<>();
		productList = new ArrayList<>();

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

		petStatusAdapter = new PetStatusAdapter(getContext(), petList);
		productDashboardAdapter = new ProductDashboardAdapter(getContext(), productList);

		petStatusAdapter = new PetStatusAdapter(getContext(), petList);
		productDashboardAdapter = new ProductDashboardAdapter(getContext(), productList);

		recyclerViewPetStatus.setAdapter(petStatusAdapter);
		recyclerViewProduct.setAdapter(productDashboardAdapter);

		return view;
	}
}
