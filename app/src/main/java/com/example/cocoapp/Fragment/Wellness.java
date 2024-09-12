package com.example.cocoapp.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import com.example.cocoapp.Object.ShowcaseDto;
import com.example.cocoapp.Object.Vaccination;
import com.example.cocoapp.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import androidx.recyclerview.widget.RecyclerView;
import com.example.cocoapp.Object.RecyclerItemClickListener;

public class Wellness extends Fragment {

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

		// Set up lists and adapters
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

		setupAppointmentClickListener();

		// Set up API client and token
		apiService = ApiClient.getClient(requireActivity(), false).create(ApiService.class);
		SharedPreferences prefs = requireActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
		token = prefs.getString("jwt_token", null);

		// Fetch data
		fetchAppointments();
		fetchShowcaseData();

		// Set button click listeners
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

	private void setupAppointmentClickListener() {
		appointmentRecyclerView.addOnItemTouchListener(
				new RecyclerItemClickListener(getContext(), appointmentRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
					@Override
					public void onItemClick(View view, int position) {
						Appointment appointment = appointmentList.get(position);
						boolean isVaccination = "Vaccinations".equalsIgnoreCase(appointment.getCategory());
						showAppointmentDialog(appointment, isVaccination);
					}

					@Override
					public void onLongItemClick(View view, int position) {
						// Optional: handle long click if needed
					}
				})
		);
	}

	private void handleAppointmentClick(Appointment appointment) {
		// Perform action when an appointment is clicked
		AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
		builder.setTitle("Appointment Clicked")
				.setMessage("Details: " + appointment.getType() + "\nDate: " + convertMillisecondsToDateTime(appointment.getTime())[0])
				.setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
				.show();
	}

	private String[] convertMillisecondsToDateTime(long milliseconds) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
		Date date = new Date(milliseconds);
		return new String[]{dateFormat.format(date)};
	}

	private void fetchAppointments() {
		apiService.fetchAppointments("Bearer " + token).enqueue(new Callback<List<Appointment>>() {
			@Override
			public void onResponse(@NonNull Call<List<Appointment>> call, @NonNull Response<List<Appointment>> response) {
				if (response.isSuccessful() && response.body() != null) {
					appointmentList.clear();
					appointmentList.addAll(response.body());
					appointmentAdapter.notifyDataSetChanged();
				} else {
					Log.e("API Error", "Response code: " + response.code() + " Message: " + response.message());
					Toast.makeText(getContext(), "Failed to fetch appointments", Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onFailure(@NonNull Call<List<Appointment>> call, @NonNull Throwable t) {
				Log.e("API Error", t.getMessage());
				Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
			}
		});
	}

	private void fetchShowcaseData() {
		// Fetch showcase data (vaccinations and allergies)
		apiService.getAllShowcases(token).enqueue(new Callback<List<ShowcaseDto>>() {
			@Override
			public void onResponse(@NonNull Call<List<ShowcaseDto>> call, @NonNull Response<List<ShowcaseDto>> response) {
				if (response.isSuccessful() && response.body() != null) {
					List<ShowcaseDto> showcaseList = response.body();
					allergiesList.clear();
					vaccinationsList.clear();

					for (ShowcaseDto showcase : showcaseList) {
						if (showcase.getCategory().equals("Vaccinations")) {
							Vaccination tmp = new Vaccination(showcase.getId(), showcase.getType(), showcase.getName());
							tmp.setId(showcase.getId());
							vaccinationsList.add(tmp);
						} else {
							Allergy tmp = new Allergy(showcase.getId(), showcase.getType(), showcase.getDescription(), showcase.getName());
							tmp.setId(showcase.getId());
							allergiesList.add(tmp);
						}
					}
					vaccinationAdapter.notifyDataSetChanged();
					allergyAdapter.notifyDataSetChanged();
				} else {
					Toast.makeText(getContext(), "Failed to retrieve data", Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onFailure(@NonNull Call<List<ShowcaseDto>> call, @NonNull Throwable t) {
				Toast.makeText(getContext(), "API call failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
				Log.e("API Error", t.getMessage(), t);
			}
		});
	}

	// Show appointment dialog for confirmations and modifications
	private void showAppointmentDialog(Appointment appointment, boolean isVaccination) {
		EditText input = new EditText(getContext());
		input.setHint("Enter type of appointment");
		input.setText(appointment.getType());

		AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
		builder.setTitle("Confirm Appointment")
				.setMessage("Enter or confirm the type of the appointment.")
				.setView(input)
				.setPositiveButton("Enter", (dialog, which) -> {
					String inputType = input.getText().toString().trim();

					if (inputType.isEmpty()) {
						Toast.makeText(getContext(), "Type cannot be empty.", Toast.LENGTH_SHORT).show();
						return;
					}

					appointment.setType(inputType);

					if (isVaccination) {
						addVaccinationRecord(appointment);
					} else {
						addTreatmentRecord(appointment);
					}
					Toast.makeText(getContext(), "Appointment moved to history.", Toast.LENGTH_SHORT).show();
				})
				.setNegativeButton("Cancel", (dialog, which) -> {
					dialog.dismiss();
					Toast.makeText(getContext(), "Appointment not added.", Toast.LENGTH_SHORT).show();
				})
				.show();
	}

	// Method to add a vaccination appointment to the medical record
	private void addVaccinationRecord(Appointment appointment) {
		Vaccination vaccinationDetail = new Vaccination(
				appointment.getId(),
				appointment.getType(),
				"Fetched Name or Details"
		);

		vaccinationsList.add(vaccinationDetail);
		vaccinationAdapter.notifyDataSetChanged();
		Toast.makeText(getContext(), "Vaccination record up dated.", Toast.LENGTH_SHORT).show();
	}

	// Method to add a treatment appointment to the medical record
	private void addTreatmentRecord(Appointment appointment) {
		Allergy treatmentDetail = new Allergy(
				appointment.getId(),
				appointment.getType(),
				"Description or Additional Info",
				"Fetched Vet Name or Details"
		);

		allergiesList.add(treatmentDetail);
		allergyAdapter.notifyDataSetChanged();
		Toast.makeText(getContext(), "Treatment record updated.", Toast.LENGTH_SHORT).show();
	}
}
