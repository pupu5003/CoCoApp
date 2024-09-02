package com.example.cocoapp.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.cocoapp.Adapter.VaccinationDetailAdapter;
import com.example.cocoapp.Object.VaccinationDetail;
import com.example.cocoapp.R;
import java.util.ArrayList;
import java.util.List;

public class PastVaccination extends Fragment {

	private RecyclerView recyclerView;
	private VaccinationDetailAdapter vaccinationAdapter;
	private List<VaccinationDetail> vaccinationList;
	private ImageButton backButton;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_medical_record_vaccination, container, false);
		backButton = view.findViewById(R.id.back_button);
		// Initialize RecyclerView
		recyclerView = view.findViewById(R.id.recyclerView);

		// Set up the grid layout with 2 columns
		recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

		// Initialize data
		vaccinationList = new ArrayList<>();
		vaccinationAdapter = new VaccinationDetailAdapter(getContext(), vaccinationList);
		recyclerView.setAdapter(vaccinationAdapter);

		// Load example data
		loadExampleData();

		backButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				getActivity().getSupportFragmentManager().popBackStack();
			}
		});

		return view;
	}

	private void loadExampleData() {
		vaccinationList.add(new VaccinationDetail("Rabies Vaccination", "2024-08-15", "Dr. Smith", ""));
		vaccinationList.add(new VaccinationDetail("Parvovirus Vaccination", "2024-05-20", "Dr. Brown", ""));
		vaccinationList.add(new VaccinationDetail("Distemper Vaccination", "2024-07-10", "Dr. Grey", ""));
		vaccinationList.add(new VaccinationDetail("Hepatitis Vaccination", "2024-04-12", "Dr. White", ""));
		vaccinationAdapter.notifyDataSetChanged();
	}
}
