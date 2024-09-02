package com.example.cocoapp.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cocoapp.Adapter.ReviewAdapter;
import com.example.cocoapp.Object.ReviewItem;
import com.example.cocoapp.R;

import java.util.ArrayList;
import java.util.List;

public class Review extends Fragment {

	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";

	private String mParam1;
	private String mParam2;
	private RecyclerView recyclerView;
	private ReviewAdapter reviewAdapter;
	private List<ReviewItem> reviewList;

	public Review() {
		// Required empty public constructor
	}
	public static Review newInstance(String param1, String param2) {
		Review fragment = new Review();
		Bundle args = new Bundle();
		args.putString(ARG_PARAM1, param1);
		args.putString(ARG_PARAM2, param2);
		fragment.setArguments(args);
		return fragment;
	}

	public static Review newInstance(String param1) {
		Review fragment = new Review();
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
			mParam2 = getArguments().getString(ARG_PARAM2);
		}
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_review, container, false);
		ImageView addButton = view.findViewById(R.id.addbtn);
		ImageButton backButton = view.findViewById(R.id.back_button);
		recyclerView = view.findViewById(R.id.comment_recycle_view);
		recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

		reviewList = new ArrayList<>();
		reviewList.add(new ReviewItem("Haylie Aminoff", "Just now", 4.5f, "The thing I like best about COCO is the amount of time it has saved while trying to manage my three pets."));
		// Add more items as needed

		reviewAdapter = new ReviewAdapter(reviewList, getContext());
		recyclerView.setAdapter(reviewAdapter);

		addButton.setOnClickListener(v -> {
			requireActivity().getSupportFragmentManager().beginTransaction()
					.replace(R.id.fragment_container, AddReview.newInstance(mParam1, mParam2))
					.addToBackStack(null)
					.commit();
		});

		backButton.setOnClickListener(v -> {
			getActivity().getSupportFragmentManager().popBackStack();
		});


		return view;
	}

}
