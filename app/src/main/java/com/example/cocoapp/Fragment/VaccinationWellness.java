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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cocoapp.Adapter.VaccinationAdapter;
import com.example.cocoapp.Api.ApiClient;
import com.example.cocoapp.Api.ApiService;
import com.example.cocoapp.Object.ShowcaseDto;
import com.example.cocoapp.Object.Vaccination;
import com.example.cocoapp.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import androidx.recyclerview.widget.GridLayoutManager;

public class VaccinationWellness extends Fragment {

	private RecyclerView recyclerView;
	private VaccinationAdapter vaccinationAdapter;
	private List<Vaccination> vaccinationList;
	private ImageButton backButton;
	private ApiService apiService;
	private String token;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_medical_record_vaccination, container, false);
		backButton = view.findViewById(R.id.back_button);

		// Initialize RecyclerView
		recyclerView = view.findViewById(R.id.recyclerView);

		// Set up the layout with a grid of 2 columns
		recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

		// Initialize data
		vaccinationList = new ArrayList<>();
		vaccinationAdapter = new VaccinationAdapter(getContext(), vaccinationList);
		recyclerView.setAdapter(vaccinationAdapter);

		// Initialize API service and fetch data
		apiService = ApiClient.getClient(requireActivity(), false).create(ApiService.class);
		SharedPreferences prefs = requireActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
		token = prefs.getString("jwt_token", null);

		// Fetch data from the server
		fetchVaccinationData();

		backButton.setOnClickListener(v -> getActivity().getSupportFragmentManager().popBackStack());

		return view;
	}

	private void fetchVaccinationData() {
		Call<List<ShowcaseDto>> call = apiService.getAllShowcases(token);
		call.enqueue(new Callback<List<ShowcaseDto>>() {
			@Override
			public void onResponse(Call<List<ShowcaseDto>> call, Response<List<ShowcaseDto>> response) {
				if (response.isSuccessful() && response.body() != null) {
					List<ShowcaseDto> showcaseList = response.body();
					vaccinationList.clear();

					for (ShowcaseDto showcase : showcaseList) {
						// Check for both "Vaccination" and "Vaccinations"
						if ("Vaccinations".equalsIgnoreCase(showcase.getCategory()) || "Vaccination".equalsIgnoreCase(showcase.getCategory())) {
							Vaccination tmp = new Vaccination(showcase.getId(), showcase.getType(), showcase.getName());
							tmp.setId(showcase.getId());
							vaccinationList.add(tmp);
						}
					}

					vaccinationAdapter.notifyDataSetChanged();

				} else {
					Toast.makeText(getContext(), "Failed to retrieve data", Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onFailure(Call<List<ShowcaseDto>> call, Throwable t) {
				Toast.makeText(getContext(), "API call failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
				Log.e("API Error", t.getMessage(), t);
			}
		});
	}
}
