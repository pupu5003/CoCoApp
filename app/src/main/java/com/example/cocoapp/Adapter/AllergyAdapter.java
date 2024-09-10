package com.example.cocoapp.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cocoapp.Api.ApiClient;
import com.example.cocoapp.Api.ApiService;
import com.example.cocoapp.Fragment.VeterinarianProfile;
import com.example.cocoapp.Object.Allergy;
import com.example.cocoapp.Object.Veterinarian;
import com.example.cocoapp.R;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllergyAdapter extends RecyclerView.Adapter<AllergyAdapter.AllergyViewHolder> {

	private final List<Allergy> allergies;
	private Context context;

	public AllergyAdapter(Context context, List<Allergy> allergies) {
		this.context =context;
		this.allergies = allergies;
	}

	@NonNull
	@Override
	public AllergyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.item_allergy_wellness, parent, false);
		return new AllergyViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull AllergyViewHolder holder, int position) {
		Allergy allergy = allergies.get(position);
		holder.nameTextView.setText(allergy.getName());
		holder.descriptionTextView.setText(allergy.getDescription());
		holder.veterinarianTextView.setText(allergy.getVeterinarian());

		holder.itemView.setOnClickListener(v -> {
			fetchVeterinarianDetails(allergy.getId());
		});
	}

	@Override
	public int getItemCount() {
		return allergies.size();
	}

	private void fetchVeterinarianDetails(String veterinarianId) {
		// API service call to fetch veterinarian details

		SharedPreferences prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
		String token = prefs.getString("jwt_token", null);
		ApiService apiService = ApiClient.getClient(context, false).create(ApiService.class);
		apiService.fetchVetById("Bearer " + token, veterinarianId).enqueue(new Callback<Veterinarian>() {
			@Override
			public void onResponse(@NonNull Call<Veterinarian> call, @NonNull Response<Veterinarian> response) {
				if (response.isSuccessful() && response.body() != null) {
					// Navigate to VeterinarianProfile with the retrieved data
					Veterinarian veterinarian = response.body();
					VeterinarianProfile profileFragment = VeterinarianProfile.newInstance(veterinarian);
					((FragmentActivity) context).getSupportFragmentManager().beginTransaction()
							.replace(R.id.fragment_container, profileFragment)
							.addToBackStack(null)
							.commit();
				} else {
					// Handle the error response
					Toast.makeText(context, "Failed to load veterinarian details.", Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onFailure(@NonNull Call<Veterinarian> call, @NonNull Throwable t) {
				// Handle the failure scenario
				Log.e("API Error", t.getMessage());
				Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
			}
		});
	}

	static class AllergyViewHolder extends RecyclerView.ViewHolder {
		TextView nameTextView;
		TextView descriptionTextView;
		TextView veterinarianTextView;

		public AllergyViewHolder(@NonNull View itemView) {
			super(itemView);
			nameTextView = itemView.findViewById(R.id.allergy_name);
			descriptionTextView = itemView.findViewById(R.id.allergy_description);
			veterinarianTextView = itemView.findViewById(R.id.allergy_veterinarian);
		}
	}
}
