package com.example.cocoapp.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cocoapp.Adapter.VaccinationDetailAdapter;
import com.example.cocoapp.Api.ApiClient;
import com.example.cocoapp.Api.ApiService;
import com.example.cocoapp.Object.Appointment;
import com.example.cocoapp.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PastVaccination extends Fragment {

	private RecyclerView recyclerView;
	private VaccinationDetailAdapter vaccinationAdapter;
	private List<Appointment> vaccinationList;
	private ImageButton backButton;
	private ApiService apiService;
	private String token;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_medical_record_vaccination, container, false);
		backButton = view.findViewById(R.id.back_button);

		recyclerView = view.findViewById(R.id.recyclerView);

		recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

		vaccinationList = new ArrayList<>();
		vaccinationAdapter = new VaccinationDetailAdapter(getContext(), vaccinationList);
		recyclerView.setAdapter(vaccinationAdapter);

		apiService = ApiClient.getClient(requireActivity(), false).create(ApiService.class);
		SharedPreferences prefs = requireActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
		token = prefs.getString("jwt_token", null);

		loadVaccinationData();

		backButton.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());

		return view;
	}

	private void loadVaccinationData() {
		apiService.getAppointmentHistory("Bearer " + token).enqueue(new Callback<List<Appointment>>() {
			@Override
			public void onResponse(@NonNull Call<List<Appointment>> call, @NonNull Response<List<Appointment>> response) {
				if (response.isSuccessful() && response.body() != null) {
					List<Appointment> allAppointments = response.body();
					for (Appointment appointment : allAppointments) {
						if ("Vaccinations".equalsIgnoreCase(appointment.getCatergory()) || "Vaccination".equalsIgnoreCase(appointment.getCatergory())) {
							vaccinationList.add(appointment);
						}
					}

					Collections.sort(vaccinationList, new Comparator<Appointment>() {
						@Override
						public int compare(Appointment a1, Appointment a2) {
							return Long.compare(a2.getTime(), a1.getTime());
						}
					});

					vaccinationAdapter.notifyDataSetChanged();
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
}
