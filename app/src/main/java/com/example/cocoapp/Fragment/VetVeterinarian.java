package com.example.cocoapp.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.cocoapp.Adapter.VeterinarianAdapter;
import com.example.cocoapp.R;
import com.example.cocoapp.object.veterinarian;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VetVeterinarian#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VetVeterinarian extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public VetVeterinarian() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Vet.
     */
    // TODO: Rename and change types and number of parameters
    public static VetVeterinarian newInstance(String param1, String param2) {
        VetVeterinarian fragment = new VetVeterinarian();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
        View view = inflater.inflate(R.layout.fragment_vet, container, false);
        ImageButton btnVeterinary = view.findViewById(R.id.btn_veterinary);
        btnVeterinary.setSelected(true);
        ImageButton btnGrooming = view.findViewById(R.id.btn_grooming);
        ImageButton btnBoarding = view.findViewById(R.id.btn_boarding);
        btnGrooming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new VetGrooming()) // Replace with your actual fragment
                        .commit();
            }
        });
        btnBoarding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new VetBoarding()) // Replace with your actual fragment
                        .commit();
            }
        });


        // Initialize the RecyclerView
        RecyclerView nearbyRecyclerView = view.findViewById(R.id.nearby_recycler_view);
        RecyclerView recommendedRecyclerView = view.findViewById(R.id.recommend_recycler_view);

        // Set up LayoutManager for the RecyclerView
        nearbyRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recommendedRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        // Initialize the data
        List<veterinarian> nearbyVets = new ArrayList<>();
        nearbyVets.add(new veterinarian("Dr. Smith", "Bachelor of Veterinary Science", 4.5f, 100, 10, "2.5 km", "$100", "Mon-Fri 8 AM - 5 PM"));
        nearbyVets.add(new veterinarian("Dr. Jones", "Doctor of Veterinary Medicine", 4.2f, 80, 8, "3.0 km", "$120", "Mon-Fri 9 AM - 6 PM"));

        List<veterinarian> recommendedVets = new ArrayList<>();
        recommendedVets.add(new veterinarian("Dr. Brown", "Bachelor of Veterinary Science", 4.8f, 120, 12, "1.8 km", "$110", "Mon-Sat 8 AM - 4 PM"));
        recommendedVets.add(new veterinarian("Dr. Johnson", "Doctor of Veterinary Medicine", 4.6f, 90, 9, "2.0 km", "$115", "Mon-Fri 10 AM - 6 PM"));

        // Create the adapter and set it to the RecyclerView
        VeterinarianAdapter nearbyAdapter = new VeterinarianAdapter(nearbyVets);
        VeterinarianAdapter recommendedAdapter = new VeterinarianAdapter(recommendedVets);

        nearbyRecyclerView.setAdapter(nearbyAdapter);
        recommendedRecyclerView.setAdapter(recommendedAdapter);

        return view;
    }
}