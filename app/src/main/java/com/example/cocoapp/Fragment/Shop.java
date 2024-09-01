package com.example.cocoapp.Fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.example.cocoapp.Adapter.ProductAdapter;
import com.example.cocoapp.Adapter.ProductDashboardAdapter;
import com.example.cocoapp.Object.CartItem;
import com.example.cocoapp.Object.CartManager;
import com.example.cocoapp.Object.Product;
import com.example.cocoapp.R;

import java.util.ArrayList;
import java.util.List;

public class Shop extends Fragment implements ProductAdapter.OnAddToCartListener {

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
		cartButton.setOnClickListener(v -> openCart());

		recyclerViewRecommend = view.findViewById(R.id.productRecommend_recycle_view);
		recyclerViewTopSelling = view.findViewById(R.id.productTopSelling_recycle_view);

		recyclerViewTopSelling.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
		recyclerViewRecommend.setLayoutManager(new GridLayoutManager(getContext(), 2));

		productList = new ArrayList<>();
		cartList = new ArrayList<>();

		ImageView imageView1 = new ImageView(getContext());
		imageView1.setImageResource(R.drawable.product_img);

		ImageView imageView2 = new ImageView(getContext());
		imageView2.setImageResource(R.drawable.product_img2);

		ImageView imageView3 = new ImageView(getContext());
		imageView3.setImageResource(R.drawable.product_img3);

		ImageView imageView4 = new ImageView(getContext());
		imageView4.setImageResource(R.drawable.product_img3);

		productList.add(new Product("", imageView1, "$20", "Dog Food", "900g", "Brand A"));
		productList.add(new Product("", imageView2, "$15", "Cat Food", "500g", "Brand B"));
		productList.add(new Product("", imageView3, "$25", "Bird Food", "1kg", "Brand C"));
		productList.add(new Product("", imageView4, "$18", "Fish Food", "300g", "Brand D"));

		recommendAdapter = new ProductAdapter(getContext(), productList, this);
		topSellingAdapter = new ProductDashboardAdapter(getContext(), productList, true);
		recyclerViewRecommend.setAdapter(recommendAdapter);
		recyclerViewTopSelling.setAdapter(topSellingAdapter);

		return view;
	}

	@Override
	public void onAddToCart(Product product) {
		CartItem cartItem = new CartItem(
				product.getName(),
				product.getBrand(),
				product.getWeight(),
				R.drawable.product_img,
				1
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
}
