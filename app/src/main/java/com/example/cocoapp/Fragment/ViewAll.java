package com.example.cocoapp.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cocoapp.Adapter.GroomingAdapter;
import com.example.cocoapp.Adapter.VeterinarianAdapter;
import com.example.cocoapp.Object.Grooming;
import com.example.cocoapp.Object.Veterinarian;
import com.example.cocoapp.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ViewAll#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewAll extends Fragment {

	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";



	// TODO: Rename and change types of parameters
	private String mParam1;
	private String mParam2;
	private VeterinarianAdapter adapter;

	public ViewAll() {
		// Required empty public constructor
	}


	// TODO: Rename and change types and number of parameters
	public static ViewAll newInstance(String param1, String param2) {
		ViewAll fragment = new ViewAll();
		Bundle args = new Bundle();
		args.putString(ARG_PARAM1, param1);
		args.putString(ARG_PARAM2, param2);
		fragment.setArguments(args);
		return fragment;
	}
	public void setVeterinarianAdapter(VeterinarianAdapter adapter) {
		this.adapter = adapter;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			mParam1 = getArguments().getString(ARG_PARAM1);
			mParam2 = getArguments().getString(ARG_PARAM2);

		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_view_all_vet, container, false);
		TextView text = view.findViewById(R.id.tvHeader);
		Log.d("key 2 " , mParam2);
		Log.d("key 1" , mParam1);

		if (Objects.equals(mParam1, "1") && Objects.equals(mParam2,"1") || Objects.equals(mParam1, "1") && Objects.equals(mParam2,"2")) {
			RecyclerView nearbyRecyclerView = view.findViewById(R.id.recyclerView);
			if (Objects.equals(mParam1, "1") && Objects.equals(mParam2,"1")) text.setText("Nearby Veterinarians");
			else text.setText("All Veterinarians");
			nearbyRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
			nearbyRecyclerView.setAdapter(adapter);
		}
		if (Objects.equals(mParam1, "2") && Objects.equals(mParam2,"1")){
			text.setText("Nearby Grooming");
			// Initialize RecyclerView
			RecyclerView nearbyGrooming = view.findViewById(R.id.recyclerView);

			nearbyGrooming.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
			// Initialize data
			List<Grooming> groomingList = new ArrayList<>();
//			groomingList.add(new Grooming("Paws & Claws", 4.5f, 120, true, "1.2 km", "$50", "Mon-Sat 9 AM - 6 PM",pic1));
//			groomingList.add(new Grooming("Pet Pamper", 4.3f, 80, false, "2.5 km", "$60", "Mon-Fri 10 AM - 5 PM",pic2));
			// Set adapter
			GroomingAdapter NearByAdapter = new GroomingAdapter(groomingList,true, getContext(),true);
			nearbyGrooming.setAdapter(NearByAdapter);
		}
		if (Objects.equals(mParam1, "2") && Objects.equals(mParam2,"2")){
			text.setText("Grooming");
			// Initialize RecyclerView
			RecyclerView recommendedGrooming = view.findViewById(R.id.recyclerView);

			recommendedGrooming.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
			// Initialize data
			List<Grooming> groomingList = new ArrayList<>();
			// Set adapter
			GroomingAdapter recommendedAdapter = new GroomingAdapter(groomingList,true, getContext(),true);
			recommendedGrooming.setAdapter(recommendedAdapter);
		}
		if (Objects.equals(mParam1, "3") && Objects.equals(mParam2,"1")){
			text.setText("Nearby Boarding");
			// Initialize RecyclerView
			RecyclerView nearbyGrooming = view.findViewById(R.id.recyclerView);

			nearbyGrooming.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
			// Initialize data
			List<Grooming> groomingList = new ArrayList<>();
			// Set adapter
			GroomingAdapter NearByAdapter = new GroomingAdapter(groomingList,true, getContext(),false);
			nearbyGrooming.setAdapter(NearByAdapter);
		}
		if (Objects.equals(mParam1, "3") && Objects.equals(mParam2,"2")){
			text.setText("Boarding");
			// Initialize RecyclerView
			RecyclerView recommendedGrooming = view.findViewById(R.id.recyclerView);

			recommendedGrooming.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
			// Initialize data
			List<Grooming> groomingList = new ArrayList<>();
			// Set adapter
			GroomingAdapter recommendedAdapter = new GroomingAdapter(groomingList,true, getContext(),false);
			recommendedGrooming.setAdapter(recommendedAdapter);
		}

		ImageButton backButton = view.findViewById(R.id.back_button);
		backButton.setOnClickListener(v -> {
			getActivity().getSupportFragmentManager().popBackStack();
		});
		

		return view;
	}
}