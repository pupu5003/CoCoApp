package com.example.cocoapp.Fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cocoapp.Adapter.VaccinationAdapter;
import com.example.cocoapp.Object.Vaccination;
import com.example.cocoapp.Object.VaccinationDetail;
import com.example.cocoapp.R;
import java.util.ArrayList;
import java.util.List;

public class VaccinationWellness extends Fragment {

	private RecyclerView recyclerView;
	private VaccinationAdapter vaccinationAdapter;
	private List<Vaccination> vaccinationList;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_medical_record_vaccination, container, false);

		// Initialize RecyclerView
		recyclerView = view.findViewById(R.id.recyclerView);


		// Set up the grid layout with 2 columns
		recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

		// Initialize data
		vaccinationList = new ArrayList<>();
		vaccinationAdapter = new VaccinationAdapter(getContext(), vaccinationList);
		recyclerView.setAdapter(vaccinationAdapter);

		// Load example data
		loadExampleData();

		return view;
	}

	private void loadExampleData() {
		vaccinationList.add(new Vaccination("Rabies Vaccination", "2024-08-15", "Dr. Smith"));
		vaccinationList.add(new Vaccination("Parvovirus Vaccination", "2024-05-20", "Dr. Brown"));
		vaccinationList.add(new Vaccination("Distemper Vaccination", "2024-07-10", "Dr. Grey"));
		vaccinationList.add(new Vaccination("Hepatitis Vaccination", "2024-04-12", "Dr. White"));
		vaccinationAdapter.notifyDataSetChanged();
	}
}
