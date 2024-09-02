package com.example.cocoapp.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.cocoapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddReview#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddReview extends Fragment {

	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";

	// TODO: Rename and change types of parameters
	private String mParam1;
	private String mParam2;

	public AddReview() {
		// Required empty public constructor
	}
	public static AddReview newInstance(String param1,String param2) {
		AddReview fragment = new AddReview();
		Bundle args = new Bundle();
		args.putString(ARG_PARAM1, param1);
		args.putString(ARG_PARAM2, param2);
		fragment.setArguments(args);
		return fragment;
	}


	// TODO: Rename and change types and number of parameters
	public static AddReview newInstance(String param1) {
		AddReview fragment = new AddReview();
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
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_add_review, container, false);

		ImageButton backButton = view.findViewById(R.id.back_button);
		TextView header = view.findViewById(R.id.tvHeader);
		header.setText("Add Review for " + mParam1);

		backButton.setOnClickListener(v -> {
			getActivity().getSupportFragmentManager().popBackStack();
		});
		return view;
	}
}