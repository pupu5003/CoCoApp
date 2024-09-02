package com.example.cocoapp.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cocoapp.Fragment.PetProfile;
import com.example.cocoapp.Object.Pet;
import com.example.cocoapp.R;

import java.util.List;

public class PetAddPetAdapter extends RecyclerView.Adapter<PetAddPetAdapter.PetViewHolder> {

	private List<Pet> petList;
	private final Context context;

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
		holder.tvPetName.setText(pet.getName());

		// Load image from URL using Glide
		Glide.with(context)
				.load(pet.getImage())
				.error(R.drawable.dog1)
				.into(holder.ivPetImage);

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
