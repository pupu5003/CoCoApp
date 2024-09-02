package com.example.cocoapp.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.cocoapp.Object.Veterinarian;
import com.example.cocoapp.R;

public class VeterinarianProfile extends Fragment {

	private static final String ARG_VETERINARIAN = "veterinarian";
	private Veterinarian veterinarian;

	public static VeterinarianProfile newInstance(Veterinarian veterinarian) {
		VeterinarianProfile fragment = new VeterinarianProfile();
		Bundle args = new Bundle();
		args.putSerializable(ARG_VETERINARIAN, veterinarian);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			veterinarian = (Veterinarian) getArguments().getSerializable(ARG_VETERINARIAN);
		}
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_veterinarian_detail, container, false);

		// Initialize UI elements
		TextView tvHeader = view.findViewById(R.id.tvHeader);
		ImageView imageDoctor = view.findViewById(R.id.imageDoctor);
		TextView doctorName = view.findViewById(R.id.doctor_name);
		TextView doctorQualification = view.findViewById(R.id.doctor_qualification);
		TextView ratingText = view.findViewById(R.id.ratingTextview);
		TextView time = view.findViewById(R.id.time);
		TextView distance = view.findViewById(R.id.distance);
		TextView price = view.findViewById(R.id.price);
		TextView bookingAppointment = view.findViewById(R.id.bookAppointment_btn);

		// Bind the veterinarian data to the UI elements
		if (veterinarian != null) {
			tvHeader.setText(veterinarian.getName());

			// Load image using Glide
			Glide.with(this)
					.load(veterinarian.getProfileImage()) // Load from URL or file path
					.placeholder(R.drawable.vet1) // Optional: placeholder image
					.error(R.drawable.vet2) // Optional: error image
					.into(imageDoctor);

			doctorName.setText(veterinarian.getName());
			doctorQualification.setText(veterinarian.getQualification());
			ratingText.setText(String.format("%s (%d reviews)", veterinarian.getRating(), veterinarian.getReviews()));
			time.setText(veterinarian.getAvailability());
			distance.setText(veterinarian.getDistance());
			price.setText(veterinarian.getPrice());
		}

		// Handle back button click
		view.findViewById(R.id.back_button).setOnClickListener(v -> getParentFragmentManager().popBackStack());

		// Handle book appointment button click
		bookingAppointment.setOnClickListener(v ->
				requireActivity().getSupportFragmentManager().beginTransaction()
						.replace(R.id.fragment_container, BookingAppoinment.newInstance(veterinarian.getName()))
						.addToBackStack(null)
						.commit()
		);

		// Handle rating text click
		ratingText.setOnClickListener(v ->
				requireActivity().getSupportFragmentManager().beginTransaction()
						.replace(R.id.fragment_container, Review.newInstance(veterinarian.getName()))
						.addToBackStack(null)
						.commit()
		);

		return view;
	}
}
