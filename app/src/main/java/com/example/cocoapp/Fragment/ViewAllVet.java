package com.example.cocoapp.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.cocoapp.Adapter.VeterinarianAdapter;
import com.example.cocoapp.Object.Veterinarian;
import com.example.cocoapp.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ViewAllVet#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewAllVet extends Fragment {

	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";

	// TODO: Rename and change types of parameters
	private String mParam1;
	private String mParam2;

	public ViewAllVet() {
		// Required empty public constructor
	}


	// TODO: Rename and change types and number of parameters
	public static ViewAllVet newInstance(String param1) {
		ViewAllVet fragment = new ViewAllVet();
		Bundle args = new Bundle();
		args.putString(ARG_PARAM1, param1);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			mParam1 = getArguments().getString(ARG_PARAM1);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_view_all_vet, container, false);
		ImageView pic1 = new ImageView(getContext());
		pic1.setImageResource(R.drawable.product_img);
		ImageView pic2 = new ImageView(getContext());
		pic2.setImageResource(R.drawable.product_img2);
		if (Objects.equals(mParam1, "1")) {
			RecyclerView nearbyRecyclerView = view.findViewById(R.id.recyclerView);


			// Set up LayoutManager for the RecyclerView
			nearbyRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
			//recommendedRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));


			List<Veterinarian> nearbyVets = new ArrayList<>();
			nearbyVets.add(new Veterinarian("Dr. Smith", "Bachelor of Veterinary Science", 4.5f, 100, 10, "2.5 km", "$100", "Mon-Fri 8 AM - 5 PM", pic1, "2024-08-15"));
			nearbyVets.add(new Veterinarian("Dr. Jones", "Doctor of Veterinary Medicine", 4.2f, 80, 8, "3.0 km", "$120", "Mon-Fri 9 AM - 6 PM", pic2, "2024-07-20"));
			nearbyVets.add(new Veterinarian("Dr. Smith", "Bachelor of Veterinary Science", 4.5f, 100, 10, "2.5 km", "$100", "Mon-Fri 8 AM - 5 PM", pic1, "2024-08-15"));
			nearbyVets.add(new Veterinarian("Dr. Jones", "Doctor of Veterinary Medicine", 4.2f, 80, 8, "3.0 km", "$120", "Mon-Fri 9 AM - 6 PM", pic2, "2024-07-20"));
			VeterinarianAdapter nearbyAdapter = new VeterinarianAdapter(getContext(), nearbyVets);
			//VeterinarianAdapter recommendedAdapter = new VeterinarianAdapter(getContext(), recommendedVets);

			nearbyRecyclerView.setAdapter(nearbyAdapter);
			//recommendedRecyclerView.setAdapter(recommendedAdapter);
		}else{
			RecyclerView nearbyRecyclerView = view.findViewById(R.id.recyclerView);
			nearbyRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
			List<Veterinarian> recommendedVets = new ArrayList<>();
			recommendedVets.add(new Veterinarian("Dr. Brown", "Bachelor of Veterinary Science", 4.8f, 120, 12, "1.8 km", "$110", "Mon-Sat 8 AM - 4 PM", pic1, "2024-08-15"));
			recommendedVets.add(new Veterinarian("Dr. Johnson", "Doctor of Veterinary Medicine", 4.6f, 90, 9, "2.0 km", "$115", "Mon-Fri 10 AM - 6 PM", pic2, "2024-07-20"));
			recommendedVets.add(new Veterinarian("Dr. Smith", "Bachelor of Veterinary Science", 4.5f, 100, 10, "2.5 km", "$100", "Mon-Fri 8 AM - 5 PM",pic1, "2024-08-15"));
			recommendedVets.add(new Veterinarian("Dr. Jones", "Doctor of Veterinary Medicine", 4.2f, 80, 8, "3.0 km", "$120", "Mon-Fri 9 AM - 6 PM",pic2, "2024-07-20"));
			VeterinarianAdapter recommendedAdapter = new VeterinarianAdapter(getContext(), recommendedVets);
			//VeterinarianAdapter recommendedAdapter = new VeterinarianAdapter(getContext(), recommendedVets);

			nearbyRecyclerView.setAdapter(recommendedAdapter);
		}
		return view;
	}
}