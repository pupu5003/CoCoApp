package com.example.cocoapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cocoapp.R;
import com.example.cocoapp.Object.Pet;

import java.util.List;

public class PetStatusAdapter extends RecyclerView.Adapter<PetStatusAdapter.PetViewHolder> {

	private final List<Pet> petList;
	private final Context context;

	public PetStatusAdapter(Context context, List<Pet> petList) {
		this.context = context;
		this.petList = petList;
	}

	@NonNull
	@Override
	public PetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(context).inflate(R.layout.item_pet_health, parent, false);
		return new PetViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull PetViewHolder holder, int position) {
		Pet pet = petList.get(position);

		// Load image from URL using Glide
		Glide.with(context)
				.load(pet.getImage())
				.error(R.drawable.dog1)
				.into(holder.petImageView);

		holder.healthLabel.setText("Health");
		holder.healthProgressBar.setProgress(pet.getHealthValue());
		holder.healthPercentage.setText(pet.getHealthValue() + "%");

		holder.foodLabel.setText("Food");
		holder.foodProgressBar.setProgress(pet.getFoodValue());
		holder.foodPercentage.setText(pet.getFoodValue() + "%");

		holder.moodLabel.setText("Mood");
		holder.moodProgressBar.setProgress(pet.getMoodValue());
		holder.moodPercentage.setText(pet.getMoodValue() + "%");
	}

	@Override
	public int getItemCount() {
		return petList.size();
	}

	public static class PetViewHolder extends RecyclerView.ViewHolder {
		ImageView petImageView;
		TextView healthLabel, foodLabel, moodLabel, healthPercentage, foodPercentage, moodPercentage;
		ProgressBar healthProgressBar, foodProgressBar, moodProgressBar;

		public PetViewHolder(@NonNull View itemView) {
			super(itemView);
			petImageView = itemView.findViewById(R.id.pet_image); // Assuming you have an ImageView with this ID

			healthLabel = itemView.findViewById(R.id.health_label);
			healthProgressBar = itemView.findViewById(R.id.health_progress);
			healthPercentage = itemView.findViewById(R.id.health_percentage);

			foodLabel = itemView.findViewById(R.id.food_label);
			foodProgressBar = itemView.findViewById(R.id.food_progress);
			foodPercentage = itemView.findViewById(R.id.food_percentage);

			moodLabel = itemView.findViewById(R.id.mood_label);
			moodProgressBar = itemView.findViewById(R.id.mood_progress);
			moodPercentage = itemView.findViewById(R.id.mood_percentage);
		}
	}
}
