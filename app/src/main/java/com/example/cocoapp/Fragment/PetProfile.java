package com.example.cocoapp.Fragment;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.cocoapp.Object.Pet;
import com.example.cocoapp.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class PetProfile extends Fragment implements OnMapReadyCallback {

	private static final String ARG_PET = "PET";
	private GoogleMap mMap;
	private Pet pet;

	private ActivityResultLauncher<Intent> pickImageLauncher;
	private ActivityResultLauncher<String> requestPermissionLauncher;
	private ImageView petImageView;

	public PetProfile() {
		// Required empty public constructor
	}

	public static PetProfile newInstance(Pet pet) {
		PetProfile fragment = new PetProfile();
		Bundle args = new Bundle();
		args.putSerializable("PET", pet);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			pet = (Pet) getArguments().getSerializable(ARG_PET);
		}

		pickImageLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
			if (result.getResultCode() == getActivity().RESULT_OK && result.getData() != null) {
				Uri imageUri = result.getData().getData();
				if (imageUri != null) {
					try {
						Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
						petImageView.setImageBitmap(bitmap);
					} catch (IOException e) {
						e.printStackTrace();
						Toast.makeText(getActivity(), "Failed to load image", Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(getActivity(), "No image selected", Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(getActivity(), "No image selected", Toast.LENGTH_SHORT).show();
			}
		});

		// Handle permissions based on Android version
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
			requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
				if (isGranted) {
					openGallery();
				} else {
					Toast.makeText(getActivity(), "Permission denied", Toast.LENGTH_SHORT).show();
				}
			});
		} else {
			requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
				if (isGranted) {
					openGallery();
				} else {
					Toast.makeText(getActivity(), "Permission denied", Toast.LENGTH_SHORT).show();
				}
			});
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
		TextView petNameTextView = view.findViewById(R.id.pet_name);
		TextView petBreedTextView = view.findViewById(R.id.type);
		ImageView petgenderImageView = view.findViewById(R.id.gender);
		petImageView = view.findViewById(R.id.imageDog);
		TextView petage = view.findViewById(R.id.age);
		TextView petweight = view.findViewById(R.id.weight);
		TextView petcolor = view.findViewById(R.id.color);
		TextView petheight = view.findViewById(R.id.height);


		ImageButton editButton = view.findViewById(R.id.edit_btn);
		View editFrame = view.findViewById(R.id.edit_frame);
		View informationFrame = view.findViewById(R.id.information_frame);
		Button doneButton = view.findViewById(R.id.done_btn);
		ImageButton imageButton = view.findViewById(R.id.camera_btn);


//		petBreedTextView.setText(pet.getBreed());
//		petage.setText(pet.getAge());
//		petNameTextView.setText(pet.getName());
//		petweight.setText(String.valueOf(pet.getWeight()));
//		petcolor.setText(pet.getColor());
//		petheight.setText(String.valueOf(pet.getHeight()));
//		Glide.with(this)
//				.load(pet.getImage())
//				.placeholder(R.drawable.dog1)
//				.into(petImageView);
//		if (pet.getGender().equals("Male")) {
//			petgenderImageView.setImageResource(R.drawable.male);
//		} else {
//			petgenderImageView.setImageResource(R.drawable.female);
//		}


		if (pet != null) {
			petNameTextView.setText(pet.getName());
			Glide.with(this)
					.load(pet.getImage())
					.placeholder(R.drawable.dog1)
					.into(petImageView);
		}

		editButton.setOnClickListener(v -> {
			informationFrame.setVisibility(View.GONE);
			editFrame.setVisibility(View.VISIBLE);
			editButton.setVisibility(View.GONE);
		});

		doneButton.setOnClickListener(v -> {
			editFrame.setVisibility(View.GONE);
			informationFrame.setVisibility(View.VISIBLE);
			editButton.setVisibility(View.VISIBLE);
			Toast.makeText(getActivity(), "Profile updated", Toast.LENGTH_SHORT).show();
			EditText petNameEditText = view.findViewById(R.id.pet_name_edit);
			EditText petBreedEditText = view.findViewById(R.id.pet_breed_name);
			EditText petGenderImageView = view.findViewById(R.id.pet_gender);
			EditText petAgeEditText = view.findViewById(R.id.pet_age);
			EditText petWeightEditText = view.findViewById(R.id.pet_weight);
			EditText petColorEditText = view.findViewById(R.id.pet_colour);
			EditText petHeightEditText = view.findViewById(R.id.pet_height);
			EditText petLocationEditText = view.findViewById(R.id.pet_location);
			if (pet.getGender().equals("Male")) {
			petgenderImageView.setImageResource(R.drawable.male);
			} else {
			petgenderImageView.setImageResource(R.drawable.gender);
			}

			if (!TextUtils.isEmpty(petNameEditText.getText())) {
				petNameTextView.setText(petNameEditText.getText().toString());
			}
			if (!TextUtils.isEmpty(petBreedEditText.getText()))
				petBreedTextView.setText(petBreedEditText.getText().toString());
			if (!TextUtils.isEmpty(petAgeEditText.getText()))
				petage.setText(petAgeEditText.getText().toString());
			if (!TextUtils.isEmpty(petWeightEditText.getText()))
				petweight.setText(petWeightEditText.getText().toString());
			if (!TextUtils.isEmpty(petColorEditText.getText()))
				petcolor.setText(petColorEditText.getText().toString());
			if (!TextUtils.isEmpty(petHeightEditText.getText()))
				petheight.setText(petHeightEditText.getText().toString());
			if (!TextUtils.isEmpty(petLocationEditText.getText())) {
				if (convertLocationToCoordinates(petLocationEditText.getText().toString()))
					petLocationEditText.setText(petLocationEditText.getText().toString());
			}
		});

		backButton.setOnClickListener(v ->
				getActivity().getSupportFragmentManager().popBackStack());

		imageButton.setOnClickListener(v -> {
			String permission = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU ? "android.permission.READ_MEDIA_IMAGES" : "android.permission.READ_EXTERNAL_STORAGE";
			if (ContextCompat.checkSelfPermission(getActivity(), permission) != PackageManager.PERMISSION_GRANTED) {
				requestPermissionLauncher.launch(permission);
			} else {
				openGallery();
			}
		});
		SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
		if (mapFragment != null) {
			mapFragment.getMapAsync(this); // Pass 'this' since this class implements OnMapReadyCallback
		}

	}

	private void openGallery() {
		Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		pickImageLauncher.launch(intent);
	}

	@Override
	public void onMapReady(GoogleMap googleMap) {
		mMap = googleMap;
		Log.d("MapFragment", "onMapReady called");
		// Set a default location (e.g., Sydney) until the user enters a location
		LatLng defaultLocation = new LatLng(-34, 151);
		mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 10));
	}

	private boolean convertLocationToCoordinates(String location) {
		Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
		try {
			List<Address> addresses = geocoder.getFromLocationName(location, 1);
			if (addresses != null && !addresses.isEmpty()) {
				Address address = addresses.get(0);
				LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());

				// Move the camera to the searched location
				mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));

				// Place a marker at the searched location
				mMap.addMarker(new MarkerOptions().position(latLng).title(address.getAddressLine(0)));

				// Show the coordinates in a Toast
				Toast.makeText(getContext(), "Location: " + address.getLatitude() + ", " + address.getLongitude(), Toast.LENGTH_LONG).show();
				Log.d("Location", "Location: " + address.getLatitude() + ", " + address.getLongitude());
				return true;
			} else {
				Toast.makeText(getContext(), "Location not found", Toast.LENGTH_SHORT).show();
				return false;
			}
		} catch (IOException e) {
			e.printStackTrace();
			Toast.makeText(getContext(), "Unable to get location", Toast.LENGTH_SHORT).show();
		}
		return true;
	}


}
