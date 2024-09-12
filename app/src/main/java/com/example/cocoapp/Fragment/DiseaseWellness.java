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

import com.example.cocoapp.Adapter.AllergyAdapter;
import com.example.cocoapp.Api.ApiClient;
import com.example.cocoapp.Api.ApiService;
import com.example.cocoapp.Object.Allergy;
import com.example.cocoapp.Object.ShowcaseDto;
import com.example.cocoapp.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DiseaseWellness extends Fragment {

	private RecyclerView recyclerView;
	private AllergyAdapter allergyAdapter;
	private List<Allergy> allergyList;
	private ImageButton backButton;
	private ApiService apiService;
	private String token;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_wellness_diseases, container, false);
		backButton = view.findViewById(R.id.back_button);

		// Initialize RecyclerView
		recyclerView = view.findViewById(R.id.recyclerView);
		recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

		allergyList = new ArrayList<>();
		allergyAdapter = new AllergyAdapter(getContext(), allergyList);
		recyclerView.setAdapter(allergyAdapter);

		// Initialize API service and token
		apiService = ApiClient.getClient(requireActivity(), false).create(ApiService.class);
		SharedPreferences prefs = requireActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
		token = prefs.getString("jwt_token", null);

		// Fetch disease data
		fetchDiseaseData();

		// Set back button click listener
		backButton.setOnClickListener(v -> getActivity().getSupportFragmentManager().popBackStack());

		return view;
	}

	private void fetchDiseaseData() {
		apiService.getAllShowcases("Bearer " + token).enqueue(new Callback<List<ShowcaseDto>>() {
			@Override
			public void onResponse(@NonNull Call<List<ShowcaseDto>> call, @NonNull Response<List<ShowcaseDto>> response) {
				if (response.isSuccessful() && response.body() != null) {
					List<ShowcaseDto> showcaseList = response.body();
					allergyList.clear();

					// Filter for "Diseases" and "Disease" categories
					for (ShowcaseDto showcase : showcaseList) {
						if ("Diseases".equalsIgnoreCase(showcase.getCategory()) || "Disease".equalsIgnoreCase(showcase.getCategory())) {
							Allergy tmp = new Allergy(showcase.getId(), showcase.getType(), showcase.getDescription(), showcase.getName());
							tmp.setId(showcase.getId());
							allergyList.add(tmp);
						}
					}

					allergyAdapter.notifyDataSetChanged();
				} else {
					Toast.makeText(getContext(), "Failed to retrieve disease data", Toast.LENGTH_SHORT).show();
					Log.e("API Error", "Response code: " + response.code() + " Message: " + response.message());
				}
			}

			@Override
			public void onFailure(@NonNull Call<List<ShowcaseDto>> call, @NonNull Throwable t) {
				Toast.makeText(getContext(), "API call failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
				Log.e("API Error", "Error: " + t.getMessage(), t);
			}
		});
	}
}
