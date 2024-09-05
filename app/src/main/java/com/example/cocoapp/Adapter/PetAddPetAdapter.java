package com.example.cocoapp.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.example.cocoapp.Fragment.PetProfile;
import com.example.cocoapp.Object.Pet;
import com.example.cocoapp.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PetAddPetAdapter extends RecyclerView.Adapter<PetAddPetAdapter.PetViewHolder> {

	private List<Pet> petList;
	private final Context context;
	ApiService apiService;
	SharedPreferences prefs;
	String token;

	public PetAddPetAdapter(List<Pet> petList, Context context) {
		this.petList = petList;
		this.context = context;
	}

	@NonNull
	@Override
	public PetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pet_add_pet, parent, false);
		return new PetViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull PetViewHolder holder, int position) {
		Pet pet = petList.get(position);
		holder.tvPetName.setText(pet.getPetName());
		String baseUrl = "http://172.28.102.169:8080";
		String fileName = pet.getImage();
		String basePath = "/file/";
		fileName = fileName.substring(basePath.length());
		apiService = ApiClient.getClient(context, false).create(ApiService.class);
		SharedPreferences prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
		token = prefs.getString("jwt_token", null);
		apiService.fetchImageFile("Bearer " + token, fileName).enqueue(new Callback<Void>() {
			@Override
			public void onResponse(Call<Void> call, Response<Void> response) {
				if (response.isSuccessful()) {
					// Load image with Glide
					String fileName = pet.getImage();
					Glide.with(context)
							.load(baseUrl+fileName)
							.error(R.drawable.dog1)
							.into(holder.ivPetImage);

					Log.e("Full Image URL", baseUrl + fileName);
				} else {
					Log.e("API Error", "Response code: " + response.code() + " Message: " + response.message());
					Toast.makeText(context, "Failed to access image", Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onFailure(Call<Void> call, Throwable t) {
				Log.e("API Error", "Error accessing image: " + t.getMessage());
				Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
			}
		});

		holder.link.setOnClickListener(view -> {
			FragmentActivity activity = (FragmentActivity) context;
			FragmentManager fragmentManager = activity.getSupportFragmentManager();
			FragmentTransaction transaction = fragmentManager.beginTransaction();

			Bundle args = new Bundle();
			args.putSerializable("PET", pet);
			PetProfile profileFragment = new PetProfile();
			profileFragment.setArguments(args);

			transaction.replace(R.id.fragment_container, profileFragment).addToBackStack(null).commit();
		});
	}

	@Override
	public int getItemCount() {
		return petList.size();
	}

	static class PetViewHolder extends RecyclerView.ViewHolder {
		TextView tvPetName;
		ImageView ivPetImage;
		ImageButton link;

		PetViewHolder(@NonNull View itemView) {
			super(itemView);
			tvPetName = itemView.findViewById(R.id.pet_name);
			ivPetImage = itemView.findViewById(R.id.pet_image);
			link = itemView.findViewById(R.id.link_ic);
		}
	}
}
