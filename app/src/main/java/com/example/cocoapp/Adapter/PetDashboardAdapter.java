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
import com.example.cocoapp.R;
import com.example.cocoapp.Object.Pet;

import java.util.List;

public class PetDashboardAdapter extends RecyclerView.Adapter<PetDashboardAdapter.PetDashboardViewHolder> {

	private final List<Pet> petList;
	private final Context context;

	public PetDashboardAdapter(Context context, List<Pet> petList) {
		this.context = context;
		this.petList = petList;
	}

	@NonNull
	@Override
	public PetDashboardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(context)
				.inflate(R.layout.item_pet_dashboard, parent, false);
		return new PetDashboardViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull PetDashboardViewHolder holder, int position) {
		Pet pet = petList.get(position);
		holder.bind(pet);
		holder.itemView.setOnClickListener(view -> {
			FragmentActivity activity = (FragmentActivity) context;
			FragmentManager fragmentManager = activity.getSupportFragmentManager();
			FragmentTransaction transaction = fragmentManager.beginTransaction();

			Bundle args = new Bundle();
			args.putSerializable("PET", pet);
			PetProfile profileFragment = PetProfile.newInstance(pet); // Assuming newInstance is defined
			profileFragment.setArguments(args);

			transaction.replace(R.id.fragment_container, profileFragment).addToBackStack(null).commit();
		});
	}

	@Override
	public int getItemCount() {
		return petList.size();
	}

	public static class PetDashboardViewHolder extends RecyclerView.ViewHolder {
		ImageView petImageView;
		TextView petName;

		public PetDashboardViewHolder(@NonNull View itemView) {
			super(itemView);
			petImageView = itemView.findViewById(R.id.pet_image); // Assuming you have an ImageView with this ID
			petName = itemView.findViewById(R.id.pet_name);
		}

		public void bind(Pet pet) {
			Glide.with(petImageView.getContext())
					.load(pet.getImage())
					.error(R.drawable.dog1)
					.into(petImageView);

			petName.setText(pet.getPetName());
		}
	}
}
