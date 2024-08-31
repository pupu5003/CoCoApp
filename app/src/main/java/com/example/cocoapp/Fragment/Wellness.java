package com.example.cocoapp.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.cocoapp.Adapter.AllergyDetailAdapter;
import com.example.cocoapp.Adapter.AppointmentAdapter;
import com.example.cocoapp.Adapter.VaccinationDetailAdapter;
import com.example.cocoapp.Object.AllergyDetail;
import com.example.cocoapp.Object.Appointment;
import com.example.cocoapp.Object.VaccinationDetail;
import com.example.cocoapp.R;
import java.util.ArrayList;
import java.util.List;

public class Wellness extends Fragment {

	private RecyclerView vaccinationRecyclerView;
	private RecyclerView allergyRecyclerView;
	private RecyclerView appointmentRecyclerView;
	private VaccinationDetailAdapter vaccinationAdapter;
	private AllergyDetailAdapter allergyDetailAdapter;
	private AppointmentAdapter appointmentAdapter;
	List<VaccinationDetail> vaccinationsList;
	List<AllergyDetail> allergiesList;
	List<Appointment> appointmentList;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_wellness, container, false);

		// Initialize RecyclerViews
		vaccinationRecyclerView = view.findViewById(R.id.vaccination_recyle_view);
		allergyRecyclerView = view.findViewById(R.id.allergies_recycle_view);
		appointmentRecyclerView = view.findViewById(R.id.appointment_recycle_view);

		// Set up Vaccination RecyclerView
		vaccinationsList = new ArrayList<>();
		allergiesList = new ArrayList<>();
		appointmentList = new ArrayList<>();

		vaccinationAdapter = new VaccinationDetailAdapter(getContext(), vaccinationsList);
		vaccinationRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
		vaccinationRecyclerView.setAdapter(vaccinationAdapter);

		allergyDetailAdapter = new AllergyDetailAdapter(getContext(), allergiesList);
		allergyRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
		allergyRecyclerView.setAdapter(allergyDetailAdapter);

		appointmentAdapter = new AppointmentAdapter(getContext(), appointmentList);
		appointmentRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
		appointmentRecyclerView.setAdapter(appointmentAdapter);

		loadExampleData();

		return view;
	}

	private void loadExampleData() {
		// Add example data for vaccinations
		vaccinationsList.add(new VaccinationDetail("Rabies Vaccination", "2024-08-15", "Dr. Smith", ""));
		vaccinationsList.add(new VaccinationDetail("Parvovirus Vaccination", "2024-05-20", "Dr. Brown", ""));
		vaccinationAdapter.notifyDataSetChanged();

		// Add example data for allergies
		allergiesList.add(new AllergyDetail("Pollen", "", "Dr. White", "2024-08-01"));
		allergiesList.add(new AllergyDetail("Peanuts", "", "Dr. Black", "2024-06-15"));
		allergyDetailAdapter.notifyDataSetChanged();

		appointmentList.add(new Appointment("Dr. Green", "2024-09-01"));
		appointmentList.add(new Appointment("Dr. Blue", "2024-09-15"));
		appointmentList.add(new Appointment("Dr. Red", "2024-10-01"));
		appointmentAdapter.notifyDataSetChanged();
	}
}
