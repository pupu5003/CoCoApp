package com.example.cocoapp.Adapter;

import static java.security.AccessController.getContext;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cocoapp.Api.ApiClient;
import com.example.cocoapp.Api.ApiService;
import com.example.cocoapp.Object.Appointment;
import com.example.cocoapp.Object.Veterinarian;
import com.example.cocoapp.R;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.AppointmentViewHolder> {

	private final List<Appointment> appointments;
	private final Context context;
	private String token;
	private ApiService apiService;
	Veterinarian vet;

	public AppointmentAdapter(Context context, List<Appointment> appointments) {
		this.context = context;
		this.appointments = appointments;
	}

	@NonNull
	@Override
	public AppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.item_appointment, parent, false);
		apiService = ApiClient.getClient(context, false).create(ApiService.class);
		SharedPreferences prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
		token = prefs.getString("jwt_token", null);

		return new AppointmentViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull AppointmentViewHolder holder, int position) {
		Appointment appointment = appointments.get(position);
		apiService.fetchVetById("Bearer " + token, appointment.getVetId()).enqueue(new Callback<Veterinarian>() {
			@Override
			public void onResponse(@NonNull Call<Veterinarian> call, @NonNull Response<Veterinarian> response) {
				if (response.isSuccessful() && response.body() != null) {
					vet = response.body();
					holder.veterinarianTextView.setText(vet.getVetName());
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
		String timeString = dateTimeStrings[1];

		holder.dateTextView.setText(dateString);
		holder.hourTextView.setText(timeString);

		holder.doneBtn.setOnClickListener(v -> {
			addHistoryAppoiment(appointment, position);
		});

	}

	@Override
	public int getItemCount() {
		return appointments.size();
	}

	static class AppointmentViewHolder extends RecyclerView.ViewHolder{
		TextView veterinarianTextView;
		TextView dateTextView;
		TextView hourTextView;
		ImageButton doneBtn;
		TextView typeTextView;

		public AppointmentViewHolder(@NonNull View itemView) {
			super(itemView);
			veterinarianTextView = itemView.findViewById(R.id.appointment_veterinarian);
			dateTextView = itemView.findViewById(R.id.appointment_date);
			hourTextView = itemView.findViewById(R.id.appointment_hour);
			doneBtn = itemView.findViewById(R.id.done_btn);
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

	public void addHistoryAppoiment(Appointment appointment, int position) {
		Gson gson = new Gson();
		String jsonAppointment = gson.toJson(appointment);
		RequestBody appointmentJson = RequestBody.create(jsonAppointment, MediaType.parse("application/json"));
		Log.e("Json", "error: " +jsonAppointment);
		apiService.addAppointmentHistory("Bearer " + token, appointmentJson).enqueue(new Callback<String>() {
			@Override
			public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
				if (response.isSuccessful() && response.body() != null) {
					Toast.makeText(context, "Appointment added to history", Toast.LENGTH_SHORT).show();
					notifyItemRemoved(position);
				} else {
					Log.e("API Error", "Failed to add appointment to history. Code: " + response.code());
				}
			}

			@Override
			public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
				Log.e("API Error", "Error: " + t.getMessage());
			}
		});
	}
}
