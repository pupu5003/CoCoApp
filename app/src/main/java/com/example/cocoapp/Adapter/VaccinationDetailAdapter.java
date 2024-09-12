package com.example.cocoapp.Adapter;

import static java.security.AccessController.getContext;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cocoapp.Api.ApiClient;
import com.example.cocoapp.Api.ApiService;
import com.example.cocoapp.Object.Appointment;
import com.example.cocoapp.Object.VaccinationDetail;
import com.example.cocoapp.Object.Veterinarian;
import com.example.cocoapp.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VaccinationDetailAdapter extends RecyclerView.Adapter<VaccinationDetailAdapter.VaccinationDetailViewHolder> {

	private final List<Appointment> vaccinations;
	private Context context;
	private ApiService apiService;
	private String token;

	public VaccinationDetailAdapter(Context context, List<Appointment> vaccinations) {
		this.vaccinations = vaccinations;
		this.context = context;
	}

	@NonNull
	@Override
	public VaccinationDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.item_medicalrecord, parent, false);

		apiService = ApiClient.getClient(context, false).create(ApiService.class);
		SharedPreferences prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
		token = prefs.getString("jwt_token", null);
		return new VaccinationDetailViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull VaccinationDetailViewHolder holder, int position) {
		Appointment appointment = vaccinations.get(position);
		holder.nameTextView.setText(appointment.getType());

		apiService.fetchVetById("Bearer " + token, appointment.getVetId()).enqueue(new Callback<Veterinarian>() {
			@Override
			public void onResponse(@NonNull Call<Veterinarian> call, @NonNull Response<Veterinarian> response) {
				if (response.isSuccessful() && response.body() != null) {
					holder.veterinarianTextView.setText(response.body().getVetName());
				} else {
					Log.e("API Error", "Response code: " + response.code() + " Message: " + response.message());
					if (context != null)
						Toast.makeText(context, "Failed to fetch vet", Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onFailure(@NonNull Call<Veterinarian> call, @NonNull Throwable t) {
				Log.e("API Error", t.getMessage());
				if (getContext() != null) {
					Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
				}
			}
		});

		long timestampInMillis = appointment.getTime();
		String[] dateTimeStrings = convertMillisecondsToDateTime(timestampInMillis);

		String dateString = dateTimeStrings[0];
		holder.dateTextView.setText(dateString);
	}

	@Override
	public int getItemCount() {
		return vaccinations.size();
	}

	static class VaccinationDetailViewHolder extends RecyclerView.ViewHolder {
		TextView nameTextView;
		TextView dateTextView;
		TextView veterinarianTextView;
		TextView petNameTextView;


		public VaccinationDetailViewHolder(@NonNull View itemView) {
			super(itemView);
			nameTextView = itemView.findViewById(R.id.vaccination_name);
			dateTextView = itemView.findViewById(R.id.vaccination_date);
			veterinarianTextView = itemView.findViewById(R.id.vaccination_veterinarian);
		}
	}

	public static String[] convertMillisecondsToDateTime(long milliseconds) {
		// Ensure that the date and time format uses the correct time zone
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

		// Set the time zone (e.g., Vietnam)
		dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
		timeFormat.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));

		// Create a Date object from milliseconds
		Date date = new Date(milliseconds);

		// Format the date and time
		String dateString = dateFormat.format(date);
		String timeString = timeFormat.format(date);

		return new String[] { dateString, timeString };
	}
}
