package com.example.cocoapp.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cocoapp.Adapter.CartAdapter;
import com.example.cocoapp.Adapter.ProductAdapter;
import com.example.cocoapp.Api.ApiClient;
import com.example.cocoapp.Api.ApiService;
import com.example.cocoapp.Object.CartDto;
import com.example.cocoapp.Object.CartItemDto;
import com.example.cocoapp.Object.Pet;
import com.example.cocoapp.Object.Product;
import com.example.cocoapp.R;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewCart extends Fragment implements CartAdapter.OnQuantityChangeListener {
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";

	private RecyclerView recyclerView;
	private CartAdapter cartAdapter;
	private List<CartItemDto> cartItemList;
	private ImageButton shoppingButton;
	private Button checkoutButton;
	private SharedPreferences prefs;
	private String token;
	private ApiService apiService;
	TextView subtotalTextView, discountTextView, totalTextView;

	public ViewCart() {
	}

	public static ViewCart newInstance(List<CartItemDto> param1) {
		ViewCart fragment = new ViewCart();
		Bundle args = new Bundle();
		args.putSerializable(ARG_PARAM1, (Serializable) param1);
		fragment.setArguments(args);
		return fragment;
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			cartItemList = new ArrayList<>();
			cartItemList = (List<CartItemDto>) getArguments().getSerializable(ARG_PARAM1);  // Retrieve the Pet object
		}
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_view_cart, container, false);
		apiService = ApiClient.getClient(requireActivity(), false).create(ApiService.class);
		prefs = requireActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
		token = prefs.getString("jwt_token", null);
		subtotalTextView = view.findViewById(R.id.subtotal_value);
		discountTextView = view.findViewById(R.id.Discount_value);
		totalTextView = view.findViewById(R.id.total_value);
		recyclerView = view.findViewById(R.id.recyclerView);
		shoppingButton = view.findViewById(R.id.shoppping_ic);
		checkoutButton = view.findViewById(R.id.checkout_button);
		recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
		cartAdapter = new CartAdapter(cartItemList,getContext(),this);


		recyclerView.setAdapter(cartAdapter);
		calculatePrice();

		ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
			@Override
			public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
				return false;
			}

			@Override
			public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
				int position = viewHolder.getAdapterPosition();
				CartItemDto cartItem = cartItemList.get(position);

				if (direction == ItemTouchHelper.LEFT) {
					new AlertDialog.Builder(getContext())
							.setTitle("Delete Item")
							.setMessage("Do you want to delete this item?")
							.setPositiveButton("Yes", (dialog, which) -> {
								cartItemList.remove(position);
								cartItem.getItem().setCurrentQuantity(0);
								cartItem.setQuantity(0);
								updateCartItem(cartItem, position);
								cartAdapter.notifyItemRemoved(position);
								calculatePrice();

							})
							.setNegativeButton("No", (dialog, which) -> {
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

		shoppingButton.setOnClickListener(v -> {
			requireActivity().getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,new Shop()).addToBackStack(null).commit();
		});

		checkoutButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String totalText = totalTextView.getText().toString();
				Integer totalAmount = Integer.parseInt(totalText);
				requireActivity().getSupportFragmentManager().beginTransaction()
						.replace(R.id.fragment_container, Payment.newInstance(totalAmount)).addToBackStack(null)
						.commit();
			}
		});
		return view;
	}

	public void calculatePrice() {
		double subtotal = 0.0;
		double discount = 0.0;  // You can calculate discounts if necessary
		double total = 0.0;

		// Loop through cart items to calculate subtotal
		for (CartItemDto cartItem : cartItemList) {
			if (cartItem.getQuantity() > 0) {  // Ensure only items with quantity > 0 are counted
				float itemPrice = cartItem.getItem().getPrice();  // Assume price is a field in the item object
				subtotal += itemPrice * cartItem.getQuantity();
				discount += cartItem.getItem().getDiscount()*0.01*itemPrice;
			}
		}

		// Assuming no discount for now, you can modify this if needed
		total = subtotal - discount;
		int roundedTotal = (int) Math.round(total);

		subtotalTextView.setText(String.valueOf(subtotal));
		discountTextView.setText(String.valueOf(discount));
		totalTextView.setText(String.valueOf(roundedTotal));
	}

	private void showError(String message) {
		new AlertDialog.Builder(getContext())
				.setTitle("Error")
				.setMessage(message)
				.setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
				.show();
	}

	public void updateCartItem(CartItemDto cartItem, int position) {
		String cartItemJson = new Gson().toJson(cartItem);
		RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), cartItemJson);

		apiService.updateCartItem("Bearer " + token, requestBody).enqueue(new Callback<CartDto>() {
			@Override
			public void onResponse(Call<CartDto> call, Response<CartDto> response) {
				if (response.isSuccessful() && response.body() != null) {
					Toast.makeText(getContext(), "Item updated successfully", Toast.LENGTH_SHORT).show();
					cartAdapter.notifyItemChanged(position);
				} else {
					Toast.makeText(getContext(), "Failed to update item: " + response.message(), Toast.LENGTH_SHORT).show();
					cartAdapter.notifyItemChanged(position);
				}
			}

			@Override
			public void onFailure(Call<CartDto> call, Throwable t) {
				Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
				Toast.makeText(getContext(), "Error updating cart: " + t.getMessage(), Toast.LENGTH_SHORT).show();
				cartAdapter.notifyItemChanged(position);
			}
		});

	}
	@Override
	public void onQuantityChanged() {
		calculatePrice();
	}


}
