package com.example.cocoapp.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cocoapp.ActivityPage.Bottom_Navigation;
import com.example.cocoapp.Adapter.AllergyAdapter;
import com.example.cocoapp.Adapter.AppointmentAdapter;
import com.example.cocoapp.Adapter.VaccinationAdapter;
import com.example.cocoapp.Api.ApiClient;
import com.example.cocoapp.Api.ApiService;
import com.example.cocoapp.Object.Allergy;
import com.example.cocoapp.Object.Appointment;
import com.example.cocoapp.Object.Vaccination;
import com.example.cocoapp.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Wellness extends Fragment{

	private RecyclerView vaccinationRecyclerView;
	private RecyclerView allergyRecyclerView;
	private RecyclerView appointmentRecyclerView;
	private VaccinationAdapter vaccinationAdapter;
	private AllergyAdapter allergyAdapter;
	private AppointmentAdapter appointmentAdapter;
	private List<Vaccination> vaccinationsList;
	private List<Allergy> allergiesList;
	private List<Appointment> appointmentList;
	private TextView seeAllVaccination;
	private TextView seeAllTreatment;
	private Button startButton;
	private ApiService apiService;
	private String token;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_wellness, container, false);
		seeAllVaccination = view.findViewById(R.id.see_all_vaccination);
		seeAllTreatment = view.findViewById(R.id.see_all_disease);
		startButton = view.findViewById(R.id.buttonStart);

		// Initialize RecyclerViews
		vaccinationRecyclerView = view.findViewById(R.id.vaccination_recycler_view);
		allergyRecyclerView = view.findViewById(R.id.allergies_recycle_view);
		appointmentRecyclerView = view.findViewById(R.id.appointment_recycle_view);

		// Set up RecyclerViews
		vaccinationsList = new ArrayList<>();
		allergiesList = new ArrayList<>();
		appointmentList = new ArrayList<>();

		vaccinationAdapter = new VaccinationAdapter(getContext(), vaccinationsList);
		vaccinationRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
		vaccinationRecyclerView.setAdapter(vaccinationAdapter);

		allergyAdapter = new AllergyAdapter(getContext(), allergiesList);
		allergyRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
		allergyRecyclerView.setAdapter(allergyAdapter);

		appointmentAdapter = new AppointmentAdapter(getContext(), appointmentList);
		appointmentRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
		appointmentRecyclerView.setAdapter(appointmentAdapter);

		apiService = ApiClient.getClient(requireActivity(), false).create(ApiService.class);
		SharedPreferences prefs = requireActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
		token = prefs.getString("jwt_token", null);

		fetchAppointments();

		loadExampleData();

		seeAllVaccination.setOnClickListener(v -> {
			requireActivity().getSupportFragmentManager().beginTransaction()
					.replace(R.id.fragment_container, new VaccinationWellness()).addToBackStack(null).commit();
		});

		seeAllTreatment.setOnClickListener(v -> {
			requireActivity().getSupportFragmentManager().beginTransaction()
					.replace(R.id.fragment_container, new DiseaseWellness()).addToBackStack(null).commit();
		});

		startButton.setOnClickListener(v -> {
			requireActivity().getSupportFragmentManager().beginTransaction()
					.replace(R.id.fragment_container, new VetVeterinarian()).addToBackStack(null).commit();
			((Bottom_Navigation) getActivity()).setSelectedTab(2);
		});

		return view;
	}

	private void fetchAppointments() {
		apiService.fetchAppointments("Bearer " + token).enqueue(new Callback<List<Appointment>>() {
			@Override
			public void onResponse(@NonNull Call<List<Appointment>> call, Response<List<Appointment>> response) {
				if (response.isSuccessful() && response.body() != null) {
					appointmentList.clear();
					appointmentList.addAll(response.body());
					appointmentAdapter.notifyDataSetChanged();
				} else {
					Log.e("API Error", "Response code: " + response.code() + " Message: " + response.message());
					if (getContext()!= null)
						Toast.makeText(getContext(), "Failed to fetch appointments", Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onFailure(@NonNull Call<List<Appointment>> call, @NonNull Throwable t) {
				Log.e("API Error", t.getMessage());
				if (getContext()!= null)
					Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
			}
		});
	}

	private void loadExampleData() {
		// Add example data for vaccinations
		vaccinationsList.add(new Vaccination("Rabies Vaccination", "2024-08-15", "Dr. Smith"));
		vaccinationsList.add(new Vaccination("Parvovirus Vaccination", "2024-05-20", "Dr. Brown"));
		vaccinationAdapter.notifyDataSetChanged();

		// Add example data for allergies
		allergiesList.add(new Allergy("Pollen", "abc", "Dr. White"));
		allergiesList.add(new Allergy("Peanuts", "abc", "Dr. Black"));
		allergyAdapter.notifyDataSetChanged();
	}





}
