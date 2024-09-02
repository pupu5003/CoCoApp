package com.example.cocoapp.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cocoapp.Adapter.VeterinarianDashboardAdapter;
import com.example.cocoapp.Object.Veterinarian;
import com.example.cocoapp.R;

import java.util.ArrayList;
import java.util.List;

public class VisitVeterinarian extends Fragment {
	private RecyclerView recyclerView;
	private VeterinarianDashboardAdapter veterinarianAdapter;
	private List<Veterinarian> veterinarianList;
	private ImageButton backButton;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_veterinarian_visit, container, false);

		// Initialize the back button and set a click listener
		backButton = view.findViewById(R.id.back_button);
		backButton.setOnClickListener(v -> {
			getActivity().getSupportFragmentManager().popBackStack();
		});

		// Initialize RecyclerView
		recyclerView = view.findViewById(R.id.recyclerView);
		recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));


		// Initialize data
		veterinarianList = new ArrayList<>();
		veterinarianAdapter = new VeterinarianDashboardAdapter(getContext(), veterinarianList, true);
		recyclerView.setAdapter(veterinarianAdapter);

		// Load example data
		loadExampleData();

		return view;
	}

	private void loadExampleData() {

		String pic1 = "android.resource://" + getContext().getPackageName() + "/" + R.drawable.vet1;
		String pic2 = "android.resource://" + getContext().getPackageName() + "/" + R.drawable.vet2;

		veterinarianList.add(new Veterinarian("Dr. Brown", "Bachelor of Veterinary Science", 4.8f, 120, 12, "1.8 km", "$110", "Mon-Sat 8 AM - 4 PM", pic1, "2024-08-15"));
		veterinarianList.add(new Veterinarian("Dr. Johnson", "Doctor of Veterinary Medicine", 4.6f, 90, 9, "2.0 km", "$115", "Mon-Fri 10 AM - 6 PM", pic2, "2024-07-20"));
		veterinarianList.add(new Veterinarian("Dr. Brown", "Bachelor of Veterinary Science", 4.8f, 120, 12, "1.8 km", "$110", "Mon-Sat 8 AM - 4 PM", pic1, "2024-08-15"));
		veterinarianList.add(new Veterinarian("Dr. Johnson", "Doctor of Veterinary Medicine", 4.6f, 90, 9, "2.0 km", "$115", "Mon-Fri 10 AM - 6 PM", pic2, "2024-07-20"));

		veterinarianAdapter.notifyDataSetChanged();
	}
}
