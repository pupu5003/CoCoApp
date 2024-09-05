package com.example.cocoapp.Fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import com.example.cocoapp.Adapter.CartAdapter;
import com.example.cocoapp.Object.CartItem;
import com.example.cocoapp.Object.CartManager;
import com.example.cocoapp.Object.Product;
import com.example.cocoapp.R;
import java.util.List;

public class ViewCart extends Fragment {

	private RecyclerView recyclerView;
	private CartAdapter cartAdapter;
	private List<CartItem> cartItemList;
	private ImageButton shoppingButton;
	private Button checkoutButton;

	public ViewCart() {
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_view_cart, container, false);

		recyclerView = view.findViewById(R.id.recyclerView);
		recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
		shoppingButton = view.findViewById(R.id.shoppping_ic);
		checkoutButton = view.findViewById(R.id.checkout_button);


		cartItemList = CartManager.getInstance().getCartItemList();

		Bundle args = getArguments();
		if (args != null) {
			List<Product> products = (List<Product>) args.getSerializable("cartList");
			if (products != null) {
				for (Product product : products) {
					cartItemList.add(new CartItem(
							product.getName(),
							product.getBrand(),
							product.getSize(),
							R.drawable.product_img,
							product.getQuantity()
					));
				}
			} else {
				showError("Error: Cart data is missing.");
			}
		} else {
			showError("Error: No data received.");
		}

		recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
		cartAdapter = new CartAdapter(cartItemList);
		recyclerView.setAdapter(cartAdapter);

		ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
			@Override
			public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
				return false;
			}

			@Override
			public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
				int position = viewHolder.getAdapterPosition();

				if (direction == ItemTouchHelper.LEFT) {
					new AlertDialog.Builder(getContext())
							.setTitle("Delete Item")
							.setMessage("Do you want to delete this item?")
							.setPositiveButton("Yes", (dialog, which) -> {
								cartItemList.remove(position);
								cartAdapter.notifyItemRemoved(position);
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
			getActivity().getSupportFragmentManager().popBackStack();
		});

		checkoutButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				requireActivity().getSupportFragmentManager().beginTransaction()
						.replace(R.id.fragment_container,new Payment()).addToBackStack(null)
						.commit();
			}
		});




		return view;
	}

	private void showError(String message) {
		new AlertDialog.Builder(getContext())
				.setTitle("Error")
				.setMessage(message)
				.setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
				.show();
	}

}
