package com.example.cocoapp.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.cocoapp.Adapter.GroomingAdapter;
import com.example.cocoapp.R;
import com.example.cocoapp.Object.Grooming;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VetBoarding#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VetBoarding extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public VetBoarding() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment VetBoarding.
     */
    // TODO: Rename and change types and number of parameters
    public static VetBoarding newInstance(String param1, String param2) {
        VetBoarding fragment = new VetBoarding();
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
        View view = inflater.inflate(R.layout.fragment_vet_boarding, container, false);
        ImageButton btnBoarding = view.findViewById(R.id.btn_boarding);
        btnBoarding.setSelected(true);
        ImageButton btnVet = view.findViewById(R.id.btn_veterinary);
        ImageButton btnGrooming = view.findViewById(R.id.btn_grooming);
        ImageView pic1 = new ImageView(getContext());
        pic1.setImageResource(R.drawable.product_img);
        ImageView pic2 = new ImageView(getContext());
        pic2.setImageResource(R.drawable.product_img2);
        btnVet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new VetVeterinarian()) // Replace with your actual fragment
                        .commit();
            }
        });
        btnGrooming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new VetGrooming()) // Replace with your actual fragment
                        .commit();
            }
        });

        // Initialize RecyclerView
        RecyclerView nearbyGrooming = view.findViewById(R.id.nearby_recycler_view);
        RecyclerView recommendGrooming = view.findViewById(R.id.recommend_recycler_view);
        nearbyGrooming.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recommendGrooming.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        // Initialize data
        List<Grooming> groomingList = new ArrayList<>();
        groomingList.add(new Grooming("Paws & Claws", 4.5f, 120, true, "1.2 km", "$50", "Mon-Sat 9 AM - 6 PM",pic1));
        groomingList.add(new Grooming("Pet Pamper", 4.3f, 80, false, "2.5 km", "$60", "Mon-Fri 10 AM - 5 PM",pic2));

        // Set adapter
        GroomingAdapter NearByAdapter = new GroomingAdapter(groomingList);
        GroomingAdapter RecommendAdapter = new GroomingAdapter(groomingList);
        nearbyGrooming.setAdapter(NearByAdapter);
        recommendGrooming.setAdapter(RecommendAdapter);
        return view;
    }
}