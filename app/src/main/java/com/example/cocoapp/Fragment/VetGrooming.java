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
import android.widget.TextView;

import com.example.cocoapp.Adapter.GroomingAdapter;
import com.example.cocoapp.R;
import com.example.cocoapp.Object.Grooming;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VetGrooming#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VetGrooming extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public VetGrooming() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Vet_Grooming.
     */
    // TODO: Rename and change types and number of parameters
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_vet_grooming, container, false);
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
        RecyclerView nearbyGrooming = view.findViewById(R.id.nearby_recycler_view);
        RecyclerView recommendGrooming = view.findViewById(R.id.recommend_recycler_view);
        nearbyGrooming.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recommendGrooming.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        // Initialize data
        List<Grooming> groomingList = new ArrayList<>();

        // Set adapter
        GroomingAdapter NearByAdapter = new GroomingAdapter(groomingList,false, getContext(),true);
        GroomingAdapter RecommendAdapter = new GroomingAdapter(groomingList,false, getContext(),true);
        nearbyGrooming.setAdapter(NearByAdapter);
        recommendGrooming.setAdapter(RecommendAdapter);



        return view;
    }
}