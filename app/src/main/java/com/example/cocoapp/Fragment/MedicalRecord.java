package com.example.cocoapp.Fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.cocoapp.Api.ApiClient;
import com.example.cocoapp.Api.ApiService;
import com.example.cocoapp.Adapter.AllergyDetailAdapter;
import com.example.cocoapp.Adapter.VaccinationDetailAdapter;
import com.example.cocoapp.Object.AllergyDetail;
import com.example.cocoapp.Object.Appointment;
import com.example.cocoapp.Object.VaccinationDetail;
import com.example.cocoapp.Object.Veterinarian;
import com.example.cocoapp.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MedicalRecord extends Fragment {

	private RecyclerView vaccinationRecyclerView;
	private RecyclerView allergyRecyclerView;
	private VaccinationDetailAdapter vaccinationAdapter;
	private AllergyDetailAdapter allergyDetailAdapter;
	private List<VaccinationDetail> vaccinationsList;
	private List<AllergyDetail> allergiesList;
	private TextView seeAllVaccination;
	private TextView seeAllTreatment;

	private ApiService apiService;
	SharedPreferences prefs;
	private String token;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_medical_record, container, false);
		seeAllVaccination = view.findViewById(R.id.see_all_vaccination);
		seeAllTreatment = view.findViewById(R.id.see_all_treatment);
		vaccinationRecyclerView = view.findViewById(R.id.vaccination_recycler_view);
		allergyRecyclerView = view.findViewById(R.id.allergies_recycle_view);

		vaccinationsList = new ArrayList<>();
		allergiesList = new ArrayList<>();
		vaccinationAdapter = new VaccinationDetailAdapter(getContext(), vaccinationsList);
		vaccinationRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
		vaccinationRecyclerView.setAdapter(vaccinationAdapter);

		allergyDetailAdapter = new AllergyDetailAdapter(getContext(), allergiesList);
		allergyRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
		allergyRecyclerView.setAdapter(allergyDetailAdapter);

		apiService = ApiClient.getClient(requireActivity(), false).create(ApiService.class);
		prefs = requireActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
		token = prefs.getString("jwt_token", null);

		fetchAppointmentHistory();

		seeAllVaccination.setOnClickListener(v -> {
			requireActivity().getSupportFragmentManager().beginTransaction()
					.replace(R.id.fragment_container, new PastVaccination()).addToBackStack(null).commit();
		});

		seeAllTreatment.setOnClickListener(v -> {
			requireActivity().getSupportFragmentManager().beginTransaction()
					.replace(R.id.fragment_container, new PastTreatment()).addToBackStack(null)
					.commit();
		});

		return view;
	}

	private void fetchAppointmentHistory() {
		apiService.getAppointmentHistory("Bearer " + token).enqueue(new Callback<List<Appointment>>() {
			@Override
			public void onResponse(@NonNull Call<List<Appointment>> call, @NonNull Response<List<Appointment>> response) {
				if (response.isSuccessful() && response.body() != null) {
					List<Appointment> appointments = response.body();
					categorizeAppointments(appointments);
				} else {
					Toast.makeText(getContext(), "Failed to fetch appointment history", Toast.LENGTH_SHORT).show();
					Log.e("API Error", "Response code: " + response.code() + " Message: " + response.message());
				}
			}

			@Override
			public void onFailure(@NonNull Call<List<Appointment>> call, @NonNull Throwable t) {
				Toast.makeText(getContext(), "API call failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
				Log.e("API Error", t.getMessage(), t);
			}
		});
	}

	private void categorizeAppointments(List<Appointment> appointments) {
		vaccinationsList.clear();
		allergiesList.clear();

		for (Appointment appointment : appointments) {
			if ("Vaccinations".equalsIgnoreCase(appointment.getCategory())) {
				vaccinationsList.add(new VaccinationDetail(
						appointment.getType(),
						convertMillisecondsToDate(appointment.getTime()),
						"Fetching Name...",
						""
				));
			} else if ("Diseases".equalsIgnoreCase(appointment.getCategory())) {
				allergiesList.add(new AllergyDetail(
						appointment.getType(),
						"",
						"Fetching Name...",
						convertMillisecondsToDate(appointment.getTime())
				));
			}
		}

		vaccinationAdapter.notifyDataSetChanged();
		allergyDetailAdapter.notifyDataSetChanged();
	}

	private String convertMillisecondsToDate(long milliseconds) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
		Date date = new Date(milliseconds);
		return dateFormat.format(date);
	}
}
