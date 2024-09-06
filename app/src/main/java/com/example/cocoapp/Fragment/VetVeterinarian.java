package com.example.cocoapp.Fragment;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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
import android.widget.Toast;

import com.example.cocoapp.Adapter.VeterinarianAdapter;
import com.example.cocoapp.Api.ApiClient;
import com.example.cocoapp.Api.ApiService;
import com.example.cocoapp.R;
import com.example.cocoapp.Object.Veterinarian;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VetVeterinarian#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VetVeterinarian extends Fragment {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private String token;
    private ApiService apiService;
    private List<Veterinarian> nearbyVets;
    private List<Veterinarian> recommendedVets;
    private List<Veterinarian> allVets;
    private VeterinarianAdapter nearbyAdapter;
    private VeterinarianAdapter recommendedAdapter;
    private FusedLocationProviderClient fusedLocationClient;
    private Float currentLatitude;
    private Float currentLongitude;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView nearbyRecyclerView,recommendedRecyclerView;

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
        View view = inflater.inflate(R.layout.fragment_vet, container, false);
        apiService = ApiClient.getClient(requireActivity(), false).create(ApiService.class);
        SharedPreferences prefs = requireActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        token = prefs.getString("jwt_token", null);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        if (checkLocationPermission()) {
            getCurrentLocation();
        } else {
            requestLocationPermission();
        }

        nearbyRecyclerView = view.findViewById(R.id.nearby_recycler_view);
        recommendedRecyclerView = view.findViewById(R.id.recommend_recycler_view);
        ImageButton btnVeterinary = view.findViewById(R.id.btn_veterinary);
        btnVeterinary.setSelected(true);
        ImageButton btnGrooming = view.findViewById(R.id.btn_grooming);
        ImageButton btnBoarding = view.findViewById(R.id.btn_boarding);
        TextView seeAll1 = view.findViewById(R.id.see_all);
        TextView seeAll2 = view.findViewById(R.id.see_all_recommended);

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
        seeAll1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewAll viewAllFragment = ViewAll.newInstance("1", "1");
                nearbyAdapter.setShowAll(true);
                nearbyAdapter.notifyDataSetChanged();
                viewAllFragment.setVeterinarianAdapter(nearbyAdapter);

                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, viewAllFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
        seeAll2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewAll viewAllFragment = ViewAll.newInstance("1", "2");
                recommendedAdapter.setShowAll(true);
                recommendedAdapter.notifyDataSetChanged();
                viewAllFragment.setVeterinarianAdapter(recommendedAdapter);

                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, viewAllFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        return view;
    }
    private boolean checkLocationPermission() {
        return ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
    }

    private void getCurrentLocation() {
        if (checkLocationPermission()) {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(getActivity(), location -> {
                        if (location != null) {
                            this.currentLatitude = (float) location.getLatitude();
                            this.currentLongitude = (float) location.getLongitude();
                            // Set up LayoutManager for the RecyclerView
                             nearbyRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                             recommendedRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));


                             nearbyVets = new ArrayList<>();
                             allVets = new ArrayList<>();

                            nearbyAdapter = new VeterinarianAdapter(getContext(), nearbyVets,false, currentLatitude, currentLongitude);
                            recommendedAdapter = new VeterinarianAdapter(getContext(), allVets,false, currentLatitude, currentLongitude);

                            nearbyRecyclerView.setAdapter(nearbyAdapter);
                            recommendedRecyclerView.setAdapter(recommendedAdapter);
                            fetchVets();
                            Log.d("Location", "Latitude: " + currentLatitude + ", Longitude: " + currentLongitude);
                        } else {
                            Toast.makeText(getContext(), "Location not available", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(getActivity(), e -> {
                        Log.e("Location Error", e.getMessage());
                        Toast.makeText(getContext(), "Error fetching location", Toast.LENGTH_SHORT).show();
                    });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
                getCurrentLocation();
            } else {
                // Permission denied
                Toast.makeText(getContext(), "Location permission is required", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void fetchVets() {
        apiService.fetchVets("Bearer " + token).enqueue(new Callback<List<Veterinarian>>() {
            @Override
            public void onResponse(@NonNull Call<List<Veterinarian>> call, Response<List<Veterinarian>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Veterinarian> Vets = response.body();
                    List<Veterinarian> filteredVets = new ArrayList<>();
                    for (Veterinarian vet : Vets) {
                        if (vet.getLocation().getType() == null) {
                            filteredVets.add(vet);
                        }
                    }
                    allVets.clear();
                    allVets.addAll(filteredVets);
                    Veterinarian.sortByDistance(filteredVets);
                    nearbyVets.clear();
                    nearbyVets.addAll(filteredVets);


                    nearbyAdapter.notifyDataSetChanged();
                    recommendedAdapter.notifyDataSetChanged();
                } else {
                    Log.e("API Error", "Response code: " + response.code() + " Message: " + response.message());
                    if (getContext()!= null) {
                        Toast.makeText(getContext(), "Failed to fetch vets", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Veterinarian>> call, @NonNull Throwable t) {
                Log.e("API Error fetch vets", t.getMessage());
                if (getContext() != null) {
                    Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}