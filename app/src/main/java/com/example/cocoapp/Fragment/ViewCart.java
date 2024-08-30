package com.example.cocoapp.Fragment;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.GestureDetector;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cocoapp.object.CartItem;
import com.example.cocoapp.R;

import java.util.ArrayList;
import java.util.List;

public class ViewCart extends Fragment {

	private RecyclerView recyclerView;
	private CartAdapter adapter;
	private List<CartItem> itemList; // Update to match the CartItem list

	public ViewCart() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_view_cart, container, false);
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		// Initialize RecyclerView
		recyclerView = view.findViewById(R.id.recyclerView);
		recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

		// Create sample product data
		itemList = new ArrayList<>(); // Ensure this is a List<CartItem>
		itemList.add(new CartItem("Rottweiler Puppy", "Royal Canin", "3kg", R.drawable.product_img, 3));
		itemList.add(new CartItem("Lactol Puppy Milk", "Beaphar", "250g", R.drawable.product_img, 2));
		itemList.add(new CartItem("Mini Deluxe", "Josera", "900g", R.drawable.product_img, 5));

		// Set up the adapter
		adapter = new CartAdapter(itemList);
		recyclerView.setAdapter(adapter);

		// Set up swipe-to-show-trash functionality
		setUpSwipeToShowTrash(recyclerView);
	}

	private void setUpSwipeToShowTrash(RecyclerView recyclerView) {
		ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
			private final ColorDrawable background = new ColorDrawable(Color.WHITE);
			private final Drawable icon = ContextCompat.getDrawable(getContext(), R.drawable.trash);
			private final int iconWidth = icon.getIntrinsicWidth();
			private final int maxSwipeDistance = iconWidth + convertDpToPx(20);
			private GestureDetector gestureDetector;

			@Override
			public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
				return false;
			}

			@Override
			public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
				if (direction == ItemTouchHelper.RIGHT) {
					adapter.notifyItemChanged(viewHolder.getAdapterPosition());
				} else if (direction == ItemTouchHelper.LEFT){
					adjustToTrashButtonState(viewHolder);
				}
			}

			@Override
			public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
				float limitedDX = Math.min(Math.abs(dX), maxSwipeDistance) * Math.signum(dX);

				super.onChildDraw(c, recyclerView, viewHolder, limitedDX, dY, actionState, isCurrentlyActive);

				View itemView = viewHolder.itemView;
				int iconMargin = (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
				int iconTop = itemView.getTop() + iconMargin;
				int iconBottom = iconTop + icon.getIntrinsicHeight();
				int iconLeft = itemView.getRight() - iconMargin - icon.getIntrinsicWidth();
				int iconRight = itemView.getRight() - iconMargin;

				if (dX < 0) {
					background.setBounds(itemView.getRight() - (int) maxSwipeDistance, itemView.getTop(), itemView.getRight(), itemView.getBottom());
					background.draw(c);

					icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
					icon.draw(c);

					if (gestureDetector == null) {
						gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
							@Override
							public boolean onSingleTapConfirmed(MotionEvent e) {
								if (e.getX() >= iconLeft && e.getX() <= iconRight && e.getY() >= iconTop && e.getY() <= iconBottom) {
									int position = viewHolder.getAdapterPosition();
									itemList.remove(position);
									adapter.notifyItemRemoved(position);
									return true;
								}
								return false;
							}
						});
					}

					itemView.setOnTouchListener((v, event) -> gestureDetector.onTouchEvent(event));
				} else if (dX > 0) {
					background.setBounds(0, 0, 0, 0);
					icon.setBounds(0, 0, 0, 0);
				}
			}

			@Override
			public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
				super.clearView(recyclerView, viewHolder);
				adjustToTrashButtonState(viewHolder);
			}

			private void adjustToTrashButtonState(@NonNull RecyclerView.ViewHolder viewHolder) {
				View itemView = viewHolder.itemView;
				itemView.animate()
						.translationX(-maxSwipeDistance)
						.setDuration(200)
						.start();
			}
		};

		new ItemTouchHelper(simpleCallback).attachToRecyclerView(recyclerView);

		recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
				if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
					for (int i = 0; i < recyclerView.getChildCount(); i++) {
						View child = recyclerView.getChildAt(i);
						if (child.getTranslationX() != 0) {
							child.animate().translationX(0).setDuration(200).start();
						}
					}
				}
			}
		});
	}
	private int convertDpToPx(int dp) {
		return (int) (dp * getResources().getDisplayMetrics().density);
	}




	// Custom Adapter for Cart Items
	private static class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
		private final List<CartItem> items;

		public CartAdapter(List<CartItem> items) {
			this.items = items;
		}

		@NonNull
		@Override
		public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
			// Inflate the cart_item layout
			View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item, parent, false);
			return new ViewHolder(view);
		}

		@Override
		public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
			// Bind the data to the views
			CartItem item = items.get(position);
			holder.productName.setText(item.getProductName());
			holder.productBrand.setText(item.getProductBrand());
			holder.productWeight.setText(item.getProductWeight());
			holder.productImage.setImageResource(item.getProductImage());
			holder.quantityTextView.setText(String.valueOf(item.getQuantity()));

			// Handle quantity increment
			holder.incrementButton.setOnClickListener(v -> {
				item.setQuantity(item.getQuantity() + 1);
				holder.quantityTextView.setText(String.valueOf(item.getQuantity()));
			});

			// Handle quantity decrement
			holder.decrementButton.setOnClickListener(v -> {
				if (item.getQuantity() > 1) {
					item.setQuantity(item.getQuantity() - 1);
					holder.quantityTextView.setText(String.valueOf(item.getQuantity()));
				}
			});
		}

		@Override
		public int getItemCount() {
			return items.size();
		}

		public static class ViewHolder extends RecyclerView.ViewHolder {
			private final TextView productName, productBrand, productWeight, quantityTextView;
			private final ImageView productImage;
			private final Button incrementButton, decrementButton;

			public ViewHolder(@NonNull View itemView) {
				super(itemView);
				productName = itemView.findViewById(R.id.product_name);
				productBrand = itemView.findViewById(R.id.product_brand);
				productWeight = itemView.findViewById(R.id.product_weight);
				productImage = itemView.findViewById(R.id.product_image);
				quantityTextView = itemView.findViewById(R.id.quantityTextView);
				incrementButton = itemView.findViewById(R.id.incrementButton);
				decrementButton = itemView.findViewById(R.id.decrementButton);
			}
		}
	}
}