package com.example.cocoapp.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.cocoapp.Adapter.AllergyDetailAdapter;
import com.example.cocoapp.Adapter.VaccinationDetailAdapter;
import com.example.cocoapp.Object.AllergyDetail;
import com.example.cocoapp.Object.VaccinationDetail;
import com.example.cocoapp.R;
import java.util.ArrayList;
import java.util.List;

public class MedicalRecord extends Fragment {

	private RecyclerView vaccinationRecyclerView;
	private RecyclerView allergyRecyclerView;
	private VaccinationDetailAdapter vaccinationAdapter;
	private AllergyDetailAdapter allergyDetailAdapter;
	private List<VaccinationDetail> vaccinationsList;
	private List<AllergyDetail> allergiesList;
	private TextView seeAllVaccination;
	private TextView seeAllTreatment;


	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_medical_record, container, false);
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

		allergyDetailAdapter = new AllergyDetailAdapter(getContext(), allergiesList);
		allergyRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
		allergyRecyclerView.setAdapter(allergyDetailAdapter);

		loadExampleData();

		seeAllVaccination.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				requireActivity().getSupportFragmentManager().beginTransaction()
						.replace(R.id.fragment_container, new PastVaccination()) // R.id.fragment_container is the ID of your FrameLayout or FragmentContainerView
						.commit();
			}
		});

		seeAllTreatment.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				requireActivity().getSupportFragmentManager().beginTransaction()
						.replace(R.id.fragment_container, new PastTreatment()) // R.id.fragment_container is the ID of your FrameLayout or FragmentContainerView
						.commit();
			}
		});

		return view;
	}

	private void loadExampleData() {
		// Add example data for vaccinations
		vaccinationsList.add(new VaccinationDetail("Rabies Vaccination", "2024-08-15", "Dr. Smith",""));
		vaccinationsList.add(new VaccinationDetail("Parvovirus Vaccination", "2024-05-20", "Dr. Brown",""));
		vaccinationAdapter.notifyDataSetChanged();

		allergiesList.add(new AllergyDetail("Pollen", "", "Dr. White", "2024-08-01"));
		allergiesList.add(new AllergyDetail("Peanuts", "", "Dr. Black", "2024-06-15"));
		allergyDetailAdapter.notifyDataSetChanged();
	}
}
