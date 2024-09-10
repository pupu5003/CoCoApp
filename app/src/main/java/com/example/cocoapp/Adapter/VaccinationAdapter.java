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
import com.example.cocoapp.Object.Vaccination;
import com.example.cocoapp.Object.Veterinarian;
import com.example.cocoapp.R;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VaccinationAdapter extends RecyclerView.Adapter<VaccinationAdapter.VaccinationViewHolder> {

	private final List<Vaccination> vaccinations;
	private Context context;


	public VaccinationAdapter(Context context, List<Vaccination> vaccinations) {
		this.vaccinations = vaccinations;
		this.context = context;
	}

	@NonNull
	@Override
	public VaccinationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.item_vaccination, parent, false);
		return new VaccinationViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull VaccinationViewHolder holder, int position) {
		Vaccination vaccination = vaccinations.get(position);
		holder.nameTextView.setText(vaccination.getName());
		holder.dateTextView.setText(vaccination.getDate());
		holder.veterinarianTextView.setText(vaccination.getVeterinarian());

		holder.itemView.setOnClickListener(v -> {
			fetchVeterinarianDetails(vaccination.getId());
		});
	}

	@Override
	public int getItemCount() {
		return vaccinations.size();
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

	static class VaccinationViewHolder extends RecyclerView.ViewHolder {
		TextView nameTextView;
		TextView dateTextView;
		TextView veterinarianTextView;

		public VaccinationViewHolder(@NonNull View itemView) {
			super(itemView);
			nameTextView = itemView.findViewById(R.id.vaccination_name);
			dateTextView = itemView.findViewById(R.id.vaccination_date);
			veterinarianTextView = itemView.findViewById(R.id.vaccination_veterinarian);
		}
	}
}
