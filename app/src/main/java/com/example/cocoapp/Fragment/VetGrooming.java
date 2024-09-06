package com.example.cocoapp.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
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

import com.example.cocoapp.Adapter.GroomingAdapter;
import com.example.cocoapp.Api.ApiClient;
import com.example.cocoapp.Api.ApiService;
import com.example.cocoapp.R;
import com.example.cocoapp.Object.Grooming;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VetGrooming extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    SharedPreferences prefs;
    String token;
    ApiService apiService;
    GroomingAdapter NearByAdapter;
    GroomingAdapter RecommendAdapter;
    List<Grooming> groomingList;
    RecyclerView nearbyGrooming;
    RecyclerView recommendGrooming;

    public VetGrooming() {
        // Required empty public constructor
    }
    public static VetGrooming newInstance(String param1, String param2) {
        VetGrooming fragment = new VetGrooming();
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

        View view = inflater.inflate(R.layout.fragment_vet_grooming, container, false);
        apiService = ApiClient.getClient(view.getContext(), false).create(ApiService.class);
    	prefs = view.getContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        token = prefs.getString("jwt_token", null);
        ImageButton btnGrooming = view.findViewById(R.id.btn_grooming);
        btnGrooming.setSelected(true);
        ImageButton btnVet = view.findViewById(R.id.btn_veterinary);
        ImageButton btnBoarding = view.findViewById(R.id.btn_boarding);
        // Create an ImageView programmatically

        String pic1 = "android.resource://" + getContext().getPackageName() + "/" + R.drawable.vet1;
        String pic2 = "android.resource://" + getContext().getPackageName() + "/" + R.drawable.vet2;
        TextView seeAll1 = view.findViewById(R.id.see_all);
        TextView seeAll2 = view.findViewById(R.id.see_all_recommended);

        seeAll1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, ViewAll.newInstance("2","1")).addToBackStack(null) // Replace with your actual fragment
                        .commit();
            }
        });
        seeAll2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, ViewAll.newInstance("2","2")).addToBackStack(null) // Replace with your actual fragment
                        .commit();
            }
        });

        btnVet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new VetVeterinarian()) // Replace with your actual fragment
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

        // Initialize RecyclerView
        nearbyGrooming = view.findViewById(R.id.nearby_recycler_view);
        recommendGrooming = view.findViewById(R.id.recommend_recycler_view);
        nearbyGrooming.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recommendGrooming.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        groomingList = new ArrayList<>();

        // Set adapter
        NearByAdapter = new GroomingAdapter(groomingList,false, getContext(),true);
        RecommendAdapter = new GroomingAdapter(groomingList,false, getContext(),true);
        nearbyGrooming.setAdapter(NearByAdapter);
        recommendGrooming.setAdapter(RecommendAdapter);
        
        fetchAllGrooming();

        return view;
    }

    private void fetchAllGrooming() {
        apiService.fetchAllGrooming("Bearer " + token).enqueue(new Callback<List<Grooming>>() {
            @Override
            public void onResponse(@NonNull Call<List<Grooming>> call, @NonNull Response<List<Grooming>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Grooming> groomings = response.body();
                    List<Grooming> filteredGroomings = new ArrayList<>();
                    for (Grooming grooming : groomings) {
                        if (grooming.getType() != null && grooming.getType().equals("Grooming")) {
                            filteredGroomings.add(grooming);
                        }
                    }

                    NearByAdapter.setGroomingList(filteredGroomings);
                    RecommendAdapter.setGroomingList(filteredGroomings);

                    NearByAdapter.notifyDataSetChanged();
                    RecommendAdapter.notifyDataSetChanged();
                } else {
                    Log.e("API Error", "Response code: " + response.code() + " Message: " + response.message());
                    if (getContext() != null) {
                        Toast.makeText(getContext(), "Failed to fetch grooming services", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Grooming>> call, @NonNull Throwable t) {
                Log.e("API Error", t.getMessage());
                if (getContext() != null) {
                    Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}