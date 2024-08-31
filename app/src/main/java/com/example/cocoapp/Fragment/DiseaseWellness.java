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

import com.example.cocoapp.Adapter.AllergyAdapter;
import com.example.cocoapp.Object.Allergy;
import com.example.cocoapp.R;
import java.util.ArrayList;
import java.util.List;

public class DiseaseWellness extends Fragment {

	private RecyclerView recyclerView;
	private AllergyAdapter allergyAdapter;
	private List<Allergy> allergyList;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_wellness_diseases, container, false);

		// Initialize RecyclerView
		recyclerView = view.findViewById(R.id.recyclerView);
		recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

		allergyList = new ArrayList<>();
		allergyAdapter = new AllergyAdapter(getContext(), allergyList);
		recyclerView.setAdapter(allergyAdapter);

		// Load example data
		loadExampleData();

		return view;
	}

	private void loadExampleData() {
		allergyList.add(new Allergy("Pollen", "abc fh jhjsfd iuydkhask awhdjsdnkjsadn wdjdwjdbjwqd hbdjasdjad", "Dr. White"));
		allergyList.add(new Allergy("Peanuts", "abc fh jhjsfd iuydkhask awhdjsdnkjsadn wdjdwjdbjwqd hbdjasdjad", "Dr. Black"));
		allergyList.add(new Allergy("Dust Mites", "abc fh jhjsfd iuydkhask awhdjsdnkjsadn wdjdwjdbjwqd hbdjasdjad", "Dr. Green"));
		allergyList.add(new Allergy("Mold", "abc fh jhjsfd iuydkhask awhdjsdnkjsadn wdjdwjdbjwqd hbdjasdjad", "Dr. Blue"));
		allergyAdapter.notifyDataSetChanged();
	}
}
