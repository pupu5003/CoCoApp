package com.example.cocoapp.Fragment;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.cocoapp.Adapter.CartAdapter;
import com.example.cocoapp.Object.CartItem;
import com.example.cocoapp.R;

import java.util.ArrayList;
import java.util.List;
import androidx.recyclerview.widget.ItemTouchHelper;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ViewCart#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewCart extends Fragment {

	// Parameter arguments for fragment initialization
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";

	// Parameters
	private String mParam1;
	private String mParam2;

	private RecyclerView recyclerView;
	private CartAdapter cartAdapter;
	private List<CartItem> cartItemList;

	public ViewCart() {
		// Required empty public constructor
	}

	/**
	 * Factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @param param1 Parameter 1.
	 * @param param2 Parameter 2.
	 * @return A new instance of fragment ViewCart.
	 */
	public static ViewCart newInstance(String param1, String param2) {
		ViewCart fragment = new ViewCart();
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

	private TextView subtotalValue, shippingValue, totalValue;
	private Button checkoutButton;
	private ImageButton backButton, shoppingCartPlusButton;


	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_view_cart, container, false);

		// Initialize views
		recyclerView = view.findViewById(R.id.recyclerView);
		subtotalValue = view.findViewById(R.id.subtotal_value);
		shippingValue = view.findViewById(R.id.shipping_value);
		totalValue = view.findViewById(R.id.total_value);
		checkoutButton = view.findViewById(R.id.checkout_button);
		backButton = view.findViewById(R.id.back_button);
		shoppingCartPlusButton = view.findViewById(R.id.shopping_cart_plus_button);

		// Setup RecyclerView
		recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
		cartItemList = getSampleCartItems(); // Replace with actual data fetching logic
		cartAdapter = new CartAdapter(cartItemList);
		recyclerView.setAdapter(cartAdapter);

		// Setup RecyclerView
		recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
		cartItemList = getSampleCartItems(); // Replace with actual data fetching logic
		cartAdapter = new CartAdapter(cartItemList);
		recyclerView.setAdapter(cartAdapter);

		// Attach ItemTouchHelper to the RecyclerView
		ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
			@Override
			public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
				// Not implementing drag functionality currently; return false
				return false;
			}

			@Override
			public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
				// Get the position of the swiped item
				int position = viewHolder.getAdapterPosition();

				if (direction == ItemTouchHelper.LEFT) {

					new AlertDialog.Builder(getContext())
							.setTitle("Delete Item")
							.setMessage("Do you want to delete this item?")
							.setPositiveButton("Yes", (dialog, which) -> {
								// Remove the item if user confirms
								cartItemList.remove(position);
								cartAdapter.notifyItemRemoved(position);
							})
							.setNegativeButton("No", (dialog, which) -> {
								// Reset the item if user cancels
								cartAdapter.notifyItemChanged(position);
								dialog.dismiss();
							})
							.setCancelable(false)
							.show();
				} else if (direction == ItemTouchHelper.RIGHT) {
					cartAdapter.notifyItemChanged(position);
				}
			}
		});

		itemTouchHelper.attachToRecyclerView(recyclerView);

		backButton.setOnClickListener(v -> getActivity().onBackPressed());
		shoppingCartPlusButton.setOnClickListener(v -> {
		});
		checkoutButton.setOnClickListener(v -> {
		});

		return view;
	}

	private List<CartItem> getSampleCartItems() {
		List<CartItem> sampleItems = new ArrayList<>();
		sampleItems.add(new CartItem("Product A", "Brand A", "500g", R.drawable.product_img, 1));
		sampleItems.add(new CartItem("Product B", "Brand B", "1kg", R.drawable.product_img2, 2));
		sampleItems.add(new CartItem("Product B", "Brand B", "1kg", R.drawable.product_img, 4));
		sampleItems.add(new CartItem("Product B", "Brand B", "1kg", R.drawable.product_img3, 3));
		sampleItems.add(new CartItem("Product B", "Brand B", "1kg", R.drawable.product_img2, 1));
		sampleItems.add(new CartItem("Product B", "Brand B", "1kg", R.drawable.product_img3, 5));
		return sampleItems;
	}
}
