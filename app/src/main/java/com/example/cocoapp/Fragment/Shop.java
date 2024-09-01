package com.example.cocoapp.Fragment;

import android.media.Image;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import com.example.cocoapp.Adapter.ProductAdapter;
import com.example.cocoapp.Adapter.ProductDashboardAdapter;
import com.example.cocoapp.R;
import com.example.cocoapp.Object.Product;

import java.util.ArrayList;
import java.util.List;

public class Shop extends Fragment {

	private RecyclerView recyclerViewRecommend;
	private RecyclerView recyclerViewTopSelling;
	private ProductAdapter recommendAdapter;
	private ProductDashboardAdapter topSellingAdapter;
	private List<Product> productList;
	private ImageView cartButton;

	public Shop() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_shop, container, false);
		cartButton = view.findViewById(R.id.cart_ic);
		cartButton.setOnClickListener(v -> {
			requireActivity().getSupportFragmentManager().beginTransaction()
					.replace(R.id.fragment_container, new ViewCart()).addToBackStack(null).commit(); // R.id.fragment_container is the ID of your FrameLayout or FragmentContainerView

		});


		recyclerViewRecommend = view.findViewById(R.id.productRecommend_recycle_view);
		recyclerViewTopSelling = view.findViewById(R.id.productTopSelling_recycle_view);

		recyclerViewTopSelling.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

		recyclerViewRecommend.setHasFixedSize(true);
		GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
		recyclerViewRecommend.setLayoutManager(gridLayoutManager);

		// Initialize the product list and adapter
		productList = new ArrayList<>();

		// Add sample data to the product list
		ImageView imageView1 = new ImageView(getContext());
		imageView1.setImageResource(R.drawable.product_img);

		ImageView imageView2 = new ImageView(getContext());
		imageView2.setImageResource(R.drawable.product_img2);

		ImageView imageView3 = new ImageView(getContext());
		imageView3.setImageResource(R.drawable.product_img3);

		ImageView imageView4 = new ImageView(getContext());
		imageView4.setImageResource(R.drawable.product_img3);

		// Each product should have its own unique ImageView
		productList.add(new Product("", imageView1, "$20", "Dog Food", "900g", "Brand A"));
		productList.add(new Product("", imageView2, "$15", "Cat Food", "500g", "Brand B"));
		productList.add(new Product("", imageView3, "$25", "Bird Food", "1kg", "Brand C"));
		productList.add(new Product("", imageView4, "$18", "Fish Food", "300g", "Brand D"));

		recommendAdapter = new ProductAdapter(getContext(), productList);
		topSellingAdapter = new ProductDashboardAdapter(getContext(),productList, true);
		recyclerViewRecommend.setAdapter(recommendAdapter);
		recyclerViewTopSelling.setAdapter(topSellingAdapter);
		// Notify the adapter of data changes
		recommendAdapter.notifyDataSetChanged();
		topSellingAdapter.notifyDataSetChanged();

		return view;
	}
}
