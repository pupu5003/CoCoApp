package com.example.cocoapp.Fragment;

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

import com.example.cocoapp.Adapter.AllergyAdapter;
import com.example.cocoapp.Adapter.AppointmentAdapter;
import com.example.cocoapp.Adapter.VaccinationAdapter;
import com.example.cocoapp.Object.Allergy;
import com.example.cocoapp.Object.Appointment;
import com.example.cocoapp.Object.Vaccination;
import com.example.cocoapp.R;
import java.util.ArrayList;
import java.util.List;

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

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_wellness, container, false);
		seeAllVaccination = view.findViewById(R.id.see_all_vaccination);
		seeAllTreatment = view.findViewById(R.id.see_all_disease);

		// Initialize RecyclerViews
		vaccinationRecyclerView = view.findViewById(R.id.vaccination_recycler_view);
		allergyRecyclerView = view.findViewById(R.id.allergies_recycle_view);
		appointmentRecyclerView = view.findViewById(R.id.appointment_recycle_view);

		// Set up Vaccination RecyclerView
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

		loadExampleData();

		seeAllVaccination.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				requireActivity().getSupportFragmentManager().beginTransaction()
						.replace(R.id.fragment_container, new VaccinationWellness()) // R.id.fragment_container is the ID of your FrameLayout or FragmentContainerView
						.commit();
			}
		});

		seeAllTreatment.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				requireActivity().getSupportFragmentManager().beginTransaction()
						.replace(R.id.fragment_container, new DiseaseWellness()) // R.id.fragment_container is the ID of your FrameLayout or FragmentContainerView
						.commit();
			}
		});


		return view;
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

		appointmentList.add(new Appointment("Dr. Green", "2024-09-01"));
		appointmentList.add(new Appointment("Dr. Blue", "2024-09-15"));
		appointmentList.add(new Appointment("Dr. Red", "2024-10-01"));
		appointmentAdapter.notifyDataSetChanged();
	}
}
