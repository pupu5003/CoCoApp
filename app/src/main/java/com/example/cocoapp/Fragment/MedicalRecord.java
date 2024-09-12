package com.example.cocoapp.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.cocoapp.Adapter.AllergyDetailAdapter;
import com.example.cocoapp.Adapter.VaccinationDetailAdapter;
import com.example.cocoapp.Api.ApiClient;
import com.example.cocoapp.Api.ApiService;
import com.example.cocoapp.Object.AllergyDetail;
import com.example.cocoapp.Object.Appointment;
import com.example.cocoapp.Object.VaccinationDetail;
import com.example.cocoapp.R;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MedicalRecord extends Fragment {

	private RecyclerView vaccinationRecyclerView;
	private RecyclerView allergyRecyclerView;
	private VaccinationDetailAdapter vaccinationAdapter;
	private VaccinationDetailAdapter allergyDetailAdapter;
	private List<Appointment> vaccinationsList;
	private List<Appointment> allergiesList;
	private TextView seeAllVaccination;
	private TextView seeAllTreatment;
	private ApiService apiService;
	private String token;


	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_medical_record, container, false);
		apiService = ApiClient.getClient(requireActivity(), false).create(ApiService.class);
		SharedPreferences prefs = requireActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
		token = prefs.getString("jwt_token", null);
		seeAllVaccination = view.findViewById(R.id.see_all_vaccination);
		seeAllTreatment = view.findViewById(R.id.see_all_treatment);
		// Initialize RecyclerViews
		vaccinationRecyclerView = view.findViewById(R.id.vaccination_recycler_view);
		allergyRecyclerView = view.findViewById(R.id.allergies_recycle_view);

		// Set up Vaccination RecyclerView
		vaccinationsList = new ArrayList<>();
		allergiesList = new ArrayList<>();
		vaccinationAdapter = new VaccinationDetailAdapter(getContext(), vaccinationsList);
		vaccinationRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
		vaccinationRecyclerView.setAdapter(vaccinationAdapter);

		allergyDetailAdapter = new VaccinationDetailAdapter(getContext(), allergiesList);
		allergyRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
		allergyRecyclerView.setAdapter(allergyDetailAdapter);

		seeAllVaccination.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				requireActivity().getSupportFragmentManager().beginTransaction()
						.replace(R.id.fragment_container, new PastVaccination()).addToBackStack(null).commit();
			}
		});

		seeAllTreatment.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				requireActivity().getSupportFragmentManager().beginTransaction()
						.replace(R.id.fragment_container, new PastTreatment()).addToBackStack(null)
						.commit();
			}
		});

		fetchAppointmentHistory();
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
		for (Appointment app : appointments){
			if (Objects.equals(app.getCatergory(), "Vaccination")){
				vaccinationsList.add(app);
			}
			else allergiesList.add(app);
		}
		vaccinationAdapter.notifyDataSetChanged();
		allergyDetailAdapter.notifyDataSetChanged();
	}
}
