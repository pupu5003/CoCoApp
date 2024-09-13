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

import com.example.cocoapp.Adapter.AllergyDetailAdapter;
import com.example.cocoapp.Api.ApiClient;
import com.example.cocoapp.Api.ApiService;
import com.example.cocoapp.Object.AllergyDetail;
import com.example.cocoapp.Object.Appointment;
import com.example.cocoapp.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PastTreatment extends Fragment {

	private RecyclerView recyclerView;
	private AllergyDetailAdapter allergyAdapter;
	private List<AllergyDetail> allergyList;
	private ImageButton backButton;
	private ApiService apiService;
	private String token;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_medical_record_treatment, container, false);
		backButton = view.findViewById(R.id.back_button);

		recyclerView = view.findViewById(R.id.recyclerView);
		recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

		allergyList = new ArrayList<>();
		allergyAdapter = new AllergyDetailAdapter(getContext(), allergyList);
		recyclerView.setAdapter(allergyAdapter);

		apiService = ApiClient.getClient(requireActivity(), false).create(ApiService.class);
		SharedPreferences prefs = requireActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
		token = prefs.getString("jwt_token", null);

		loadDiseaseData();

		backButton.setOnClickListener(v -> getActivity().getSupportFragmentManager().popBackStack());

		return view;
	}

	private void loadDiseaseData() {
		apiService.getAppointmentHistory("Bearer " + token).enqueue(new Callback<List<Appointment>>() {
			@Override
			public void onResponse(@NonNull Call<List<Appointment>> call, @NonNull Response<List<Appointment>> response) {
				if (response.isSuccessful() && response.body() != null) {
					allergyList.clear();

					for (Appointment appointment : response.body()) {
						if ("Diseases".equalsIgnoreCase(appointment.getCatergory()) || "Disease".equalsIgnoreCase(appointment.getCatergory())) {
							String formattedDate = convertMillisecondsToDate(appointment.getTime());
							AllergyDetail allergyDetail = new AllergyDetail(
									appointment.getType(),
									"Description placeholder",
									"Fetching Name...",
									formattedDate
							);
							allergyList.add(allergyDetail);
						}
					}

					Collections.sort(allergyList, new Comparator<AllergyDetail>() {
						@Override
						public int compare(AllergyDetail o1, AllergyDetail o2) {
							return o2.getDate().compareTo(o1.getDate());
						}
					});

					allergyAdapter.notifyDataSetChanged();
				} else {
					Toast.makeText(getContext(), "Failed to fetch disease history", Toast.LENGTH_SHORT).show();
					Log.e("API Error", "Response code: " + response.code() + " Message: " + response.message());
				}
			}

			@Override
			public void onFailure(@NonNull Call<List<Appointment>> call, @NonNull Throwable t) {
				Toast.makeText(getContext(), "API call failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
				Log.e("API Error", "Error: " + t.getMessage(), t);
			}
		});
	}

	private String convertMillisecondsToDate(long milliseconds) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
		dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
		Date date = new Date(milliseconds);
		return dateFormat.format(date);
	}
}
