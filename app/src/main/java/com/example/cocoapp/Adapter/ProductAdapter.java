package com.example.cocoapp.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cocoapp.Api.ApiClient;
import com.example.cocoapp.Api.ApiService;
import com.example.cocoapp.Fragment.ProductProfile;
import com.example.cocoapp.Object.Product;
import com.example.cocoapp.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

	private Context context;
	private List<Product> productList;


	private boolean showAll;

	public ProductAdapter(Context context, List<Product> productList, boolean showAll) {
		this.context = context;
		this.productList = productList;
		this.showAll = showAll;
	}

	@NonNull
	@Override
	public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(context).inflate(R.layout.product_item, parent, false);
		return new ProductViewHolder(view);
	}

	@Override
	public void onBindViewHolder(ProductViewHolder holder, int position) {
		Product product = productList.get(position);
		holder.productNameTextView.setText(product.getName());
		String price = String.valueOf(product.getPrice()) + "VND";
		holder.priceTextView.setText(price);
		if (product.getDiscount() == 0) holder.discountTextView.setVisibility(View.GONE);
		else {
			holder.discountTextView.setVisibility(View.VISIBLE);
			holder.discountTextView.setText(String.valueOf(product.getDiscount()) + "%");
		}
		holder.discountTextView.setText(String.valueOf(product.getDiscount()) + "%");

		String baseUrl = "http://172.28.102.169:8080";
		Glide.with(context)
				.load(baseUrl + product.getProductImageUrl())
				.error(R.drawable.dog1)
				.into(holder.productImageView);



		if (product.getCurrentQuantity()>0) {
			int quantity = Integer.parseInt(holder.quantityTextView.getText().toString());
			holder.quantityTextView.setText(String.valueOf(quantity));
		}
		else holder.quantityTextView.setText("1");

		if (product.getSizeObject() != null) {
			String weight = String.valueOf(product.getSizeObject().getValue()) + " " + product.getSizeObject().getUnit();
			holder.weightTextView.setText(weight);
		}

		if(product.getPrice() != 0) holder.priceTextView.setText(String.valueOf(product.getPrice()) + " VND");

		holder.addToCartButton.setOnClickListener(v -> {
			holder.addToCartButton.setVisibility(View.GONE);
			holder.quantityLayout.setVisibility(View.VISIBLE);
			holder.quantityTextView.setText("1");
		});

		holder.incrementButton.setOnClickListener(v -> {
			int quantity = Integer.parseInt(holder.quantityTextView.getText().toString());
			holder.quantityTextView.setText(String.valueOf(++quantity));
			product.setCurrentQuantity(quantity);
		});

		// Handle decrement button click
		holder.decrementButton.setOnClickListener(v -> {
			int quantity = Integer.parseInt(holder.quantityTextView.getText().toString());
			if (quantity > 1) {
				holder.quantityTextView.setText(String.valueOf(--quantity));
				product.setCurrentQuantity(quantity);
			} else {// If quantity is 1, revert to showing the cart icon
				holder.quantityTextView.setText("1");
				holder.quantityLayout.setVisibility(View.GONE);
				holder.addToCartButton.setVisibility(View.VISIBLE);
				product.setCurrentQuantity(1);
			}
		});

		holder.itemView.setOnClickListener(v -> {
			FragmentActivity activity = (FragmentActivity) context;

			ProductProfile productProfileFragment = new ProductProfile();
			Bundle bundle = new Bundle();
			bundle.putString("product_id", product.getId());
			productProfileFragment.setArguments(bundle);

			FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
			transaction.replace(R.id.fragment_container, productProfileFragment)
					.addToBackStack(null)
					.commit();
		});
	}

	@Override
	public int getItemCount() {
		return showAll ? productList.size() : Math.min(productList.size(), 4); // Show all if showAll is true
	}

	public static class ProductViewHolder extends RecyclerView.ViewHolder {
		TextView productNameTextView, priceTextView, discountTextView, quantityTextView, weightTextView, productPrice;
		ImageView productImageView;
		Button addToCartButton;
		LinearLayout quantityLayout;
		Button incrementButton;
		Button decrementButton;


		public ProductViewHolder(View itemView) {
			super(itemView);
			productNameTextView = itemView.findViewById(R.id.productNameTextView);
			priceTextView = itemView.findViewById(R.id.priceTextView);
			productImageView = itemView.findViewById(R.id.productImageView);
			discountTextView = itemView.findViewById(R.id.discountTextView);
			addToCartButton = itemView.findViewById(R.id.addToCartButton);
			quantityLayout = itemView.findViewById(R.id.quantityLayout);
			quantityTextView = itemView.findViewById(R.id.quantityTextView);
			incrementButton = itemView.findViewById(R.id.incrementButton);
			decrementButton = itemView.findViewById(R.id.decrementButton);
			weightTextView = itemView.findViewById(R.id.productWeightTextView);
		}
	}


}
