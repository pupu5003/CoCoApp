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
import androidx.fragment.app.FragmentTransaction;
import com.example.cocoapp.R;

public class Review extends Fragment {

	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";

	private String mParam1;
	private String mParam2;

	public Review() {
		// Required empty public constructor
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
		}
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_review, container, false);

		ImageView addButton = view.findViewById(R.id.addbtn);
		ImageButton backButton = view.findViewById(R.id.back_button);
		addButton.setOnClickListener(v -> {
			FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
			transaction.replace(R.id.fragment_container, new AddReview()).addToBackStack(null).commit();
		});

		backButton.setOnClickListener(v -> {
			getActivity().getSupportFragmentManager().popBackStack();
		});

		return view;
	}
}
