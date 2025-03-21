package com.example.cocoapp.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.cocoapp.R;

public class PetHealth extends Fragment{

	private View tabWellness, tabMedicalRecords;
	private View lineWellness, lineMedicalRecord;
	private FragmentManager fragmentManager;
	private ImageButton backButton;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_pet_health, container, false);
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		backButton = view.findViewById(R.id.back_button);
		backButton.setOnClickListener(v -> {
			requireActivity().getSupportFragmentManager().beginTransaction()
					.replace(R.id.fragment_container, new Dashboard()).addToBackStack(null).commit(); // R.id.fragment_container is the ID of your FrameLayout or FragmentContainerView

		});

		fragmentManager = getChildFragmentManager();

		tabWellness = view.findViewById(R.id.tabWellness);
		tabMedicalRecords = view.findViewById(R.id.tabMedicalRecords);
		lineWellness = view.findViewById(R.id.lineWellness);
		lineMedicalRecord = view.findViewById(R.id.lineMedicalRecord);

		// Set default fragment
		if (savedInstanceState == null) {
			showFragment(new Wellness());
			updateTabUI(true);
		}

		tabWellness.setOnClickListener(v -> {
			showFragment(new Wellness());
			updateTabUI(true);
		});

		tabMedicalRecords.setOnClickListener(v -> {
			showFragment(new MedicalRecord());
			updateTabUI(false);
		});
	}

	private void showFragment(Fragment fragment) {
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		transaction.replace(R.id.fragment_container_small, fragment).commit();
	}

	private void updateTabUI(boolean isWellness) {
		lineWellness.setVisibility(isWellness ? View.VISIBLE : View.GONE);
		lineMedicalRecord.setVisibility(isWellness ? View.GONE : View.VISIBLE);
	}
}
