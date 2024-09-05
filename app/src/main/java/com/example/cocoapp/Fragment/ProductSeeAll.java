package com.example.cocoapp.Fragment;

import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.example.cocoapp.Adapter.ProductAdapter;
import com.example.cocoapp.Object.CartItem;
import com.example.cocoapp.Object.CartManager;
import com.example.cocoapp.Object.Product;
import com.example.cocoapp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProductSeeAll#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductSeeAll extends Fragment implements ProductAdapter.OnAddToCartListener{

	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";

	// TODO: Rename and change types of parameters
	private String mParam1;
	private String mParam2;

	public ProductSeeAll() {
		// Required empty public constructor
	}

	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @param param1 Parameter 1.
	 * @param param2 Parameter 2.
	 * @return A new instance of fragment ProductSeeAll.
	 */
	// TODO: Rename and change types and number of parameters
	public static ProductSeeAll newInstance(String param1, String param2) {
		ProductSeeAll fragment = new ProductSeeAll();
		Bundle args = new Bundle();
		args.putString(ARG_PARAM1, param1);
		args.putString(ARG_PARAM2, param2);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			mParam1 = getArguments().getString(ARG_PARAM1);
			mParam2 = getArguments().getString(ARG_PARAM2);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_product_see_all, container, false);
		RecyclerView recyclerViewRecommend = view.findViewById(R.id.recyclerView);
		recyclerViewRecommend.setLayoutManager(new GridLayoutManager(getContext(), 2));
		List<Product> productList = new ArrayList<>();
		ImageButton backbtn = view.findViewById(R.id.back_button);
		ImageButton filterButton = view.findViewById(R.id.filter_btn);

		backbtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				getActivity().getSupportFragmentManager().popBackStack();
			}
		});

		filterButton.setOnClickListener(v -> showSortDialog());

		String imageView3 = "android.resource://" + getContext().getPackageName() + "/" + R.drawable.product_img;
		String imageView4 = "android.resource://" + getContext().getPackageName() + "/" + R.drawable.product_img;
		String imageView2 = "android.resource://" + getContext().getPackageName() + "/" + R.drawable.product_img;
		String imageView1 = "android.resource://" + getContext().getPackageName() + "/" + R.drawable.product_img;

		productList.add(new Product(10, imageView1, 20F, "Dog Food", "900g", "Brand A", 100));
		productList.add(new Product(0, imageView2, 30F, "Cat Food", "500g", "Brand B", 100));
		productList.add(new Product(20, imageView3, 30F, "Bird Food", "1kg", "Brand C", 100));
		productList.add(new Product(10, imageView4, 30F, "Fish Food", "300g", "Brand D", 100));
		productList.add(new Product(0, imageView1, 30F, "Dog Food", "900g", "Brand A", 100));
		productList.add(new Product(0, imageView2, 30F, "Cat Food", "500g", "Brand B", 100));
		productList.add(new Product(20, imageView3, 30F, "Bird Food", "1kg", "Brand C", 100));
		productList.add(new Product(10, imageView4, 30F, "Fish Food", "300g", "Brand D", 100));

		ProductAdapter recommendAdapter = new ProductAdapter(getContext(), productList, this,true);

		recyclerViewRecommend.setAdapter(recommendAdapter);
		return view;
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
			}
			else sortByDefault();

			dialog.dismiss();
		});

		dialog.show();
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

	private void sortByWeight() {
		// Implement your sorting logic here
	}

	private void sortByPrice() {
		// Implement your sorting logic here
	}

	private void sortByDefault() {
	}
}