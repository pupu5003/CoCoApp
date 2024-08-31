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
import com.example.cocoapp.Adapter.AllergyDetailAdapter;
import com.example.cocoapp.Object.AllergyDetail;
import com.example.cocoapp.R;
import java.util.ArrayList;
import java.util.List;

public class PastTreatment extends Fragment {

	private RecyclerView recyclerView;
	private AllergyDetailAdapter allergyAdapter;
	private List<AllergyDetail> allergyList;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_medical_record_treatment, container, false);

		// Initialize RecyclerView
		recyclerView = view.findViewById(R.id.recyclerView);

		// Set up the grid layout with 2 columns
		recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

		// Initialize data
		allergyList = new ArrayList<>();
		allergyAdapter = new AllergyDetailAdapter(getContext(), allergyList);
		recyclerView.setAdapter(allergyAdapter);

		// Load example data
		loadExampleData();

		return view;
	}

	private void loadExampleData() {
		allergyList.add(new AllergyDetail("Pollen", "", "Dr. White", "2024-08-01"));
		allergyList.add(new AllergyDetail("Peanuts", "", "Dr. Black", "2024-06-15"));
		allergyList.add(new AllergyDetail("Dust Mites", "", "Dr. Green", "2024-04-10"));
		allergyList.add(new AllergyDetail("Mold", "", "Dr. Blue", "2024-03-22"));
		allergyAdapter.notifyDataSetChanged();
	}
}
