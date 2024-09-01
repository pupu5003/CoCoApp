package com.example.cocoapp.Fragment;

import android.os.Bundle;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cocoapp.Object.Veterinarian;
import com.example.cocoapp.R;
import com.example.cocoapp.Object.Pet;

public class PetProfile extends Fragment {

	private static final String ARG_PET = "PET";
	private Pet pet;

	public PetProfile() {
		// Required empty public constructor
	}

	public static PetProfile newInstance(Pet pet) {
		PetProfile fragment = new PetProfile();
		Bundle args = new Bundle();
		args.putSerializable(ARG_PET, pet); // Pass the Pet object
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			pet = (Pet) getArguments().getSerializable(ARG_PET);
		}
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_pet_profile, container, false);
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		ImageButton backButton = view.findViewById(R.id.back_button);
		Button checkVet = view.findViewById(R.id.contactVet_btn);
		Button checkShop = view.findViewById(R.id.check_shop_btn);
		Button checkStatus = view.findViewById(R.id.check_status_btn);
		TextView petNameTextView = view.findViewById(R.id.pet_name);
		ImageView petImageView = view.findViewById(R.id.imageDog);

		if (pet != null) {
			petNameTextView.setText(pet.getName());
			petImageView.setImageDrawable(pet.getImage().getDrawable());
		}

		backButton.setOnClickListener(v -> getActivity().getSupportFragmentManager().popBackStack());

		checkVet.setOnClickListener(v -> {
			requireActivity().getSupportFragmentManager().beginTransaction()
					.replace(R.id.fragment_container, new VetVeterinarian()).addToBackStack(null).commit();
		});

		checkShop.setOnClickListener(v -> {
			requireActivity().getSupportFragmentManager().beginTransaction()
					.replace(R.id.fragment_container, new Shop()).addToBackStack(null).commit();
		});

		checkStatus.setOnClickListener(v -> {
			requireActivity().getSupportFragmentManager().beginTransaction()
					.replace(R.id.fragment_container, new PetHealth()).addToBackStack(null).commit();
		});
	}
}
