package com.example.cocoapp.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.cocoapp.Api.ApiClient;
import com.example.cocoapp.Api.ApiService;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PetProfile extends Fragment implements OnMapReadyCallback {

	private static final String ARG_PET = "PET";
	private GoogleMap mMap;
	private Pet pet;

	private ActivityResultLauncher<Intent> pickImageLauncher;
	private ActivityResultLauncher<String> requestPermissionLauncher;
	private ImageView petImageView;

	private ApiService apiService;
	private SharedPreferences prefs;
	private String token;
	public PetProfile() {
		// Required empty public constructor
	}

	public static PetProfile newInstance(Pet pet) {
		PetProfile fragment = new PetProfile();
		Bundle args = new Bundle();
		args.putSerializable(ARG_PET, pet);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			pet = (Pet) getArguments().getSerializable(ARG_PET);  // Retrieve the Pet object
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
		TextView petLocation = view.findViewById(R.id.location);
		TextView header = view.findViewById(R.id.tvHeader);
		TextView name2 = view.findViewById(R.id.name2);
		TextView name3 = view.findViewById(R.id.name3);


		ImageButton editButton = view.findViewById(R.id.edit_btn);
		View editFrame = view.findViewById(R.id.edit_frame);
		View informationFrame = view.findViewById(R.id.information_frame);
		Button doneButton = view.findViewById(R.id.done_btn);
		ImageButton imageButton = view.findViewById(R.id.camera_btn);


		if (pet != null) {
			name2.setText(pet.getPetName());
			name3.setText(pet.getPetName());
			header.setText(pet.getPetName());
			petBreedTextView.setText(pet.getBreedName());
			petage.setText(String.valueOf(pet.getAge()));
			petNameTextView.setText(pet.getPetName());
			petheight.setText((String.valueOf(pet.getHeight())) + "cm");
			petcolor.setText(pet.getColor());
			petweight.setText((String.valueOf(pet.getWeight())) + "kg");
			petLocation.setText(pet.getLocation());
			fetchPNG(pet.getImage());
			petLocation.setText(pet.getLocation());
			if (pet.getGender().equals("M")) {
				petgenderImageView.setImageResource(R.drawable.male);
			} else {
				petgenderImageView.setImageResource(R.drawable.female);
			}
		}else{
			petNameTextView.setText("No pet found");
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
			EditText petAgeEditText = view.findViewById(R.id.pet_age);
			EditText petWeightEditText = view.findViewById(R.id.pet_weight);
			EditText petColorEditText = view.findViewById(R.id.pet_colour);
			EditText petHeightEditText = view.findViewById(R.id.pet_height);
			EditText petLocationEditText = view.findViewById(R.id.pet_location);
			name2.setText(pet.getPetName());
			name3.setText(pet.getPetName());
			header.setText(pet.getPetName());
			if (pet.getGender().equals("M")) {
				petgenderImageView.setImageResource(R.drawable.male);
			} else {
				petgenderImageView.setImageResource(R.drawable.female);
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

	private void fetchPNG(String image) {
		String baseUrl = "http://172.28.102.169:8080";
		String basePath = "/file/";
		String fileName = image.substring(basePath.length());
		apiService = ApiClient.getClient(requireActivity(), false).create(ApiService.class);
		prefs = requireActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
		token = prefs.getString("jwt_token", null);
		apiService.fetchImageFile("Bearer " + token, fileName).enqueue(new Callback<Void>() {
			@Override
			public void onResponse(Call<Void> call, Response<Void> response) {
				if (response.isSuccessful()) {
					// Load image with Glide
					String fileName = pet.getImage();
					Glide.with(requireActivity())
							.load(baseUrl+fileName)
							.error(R.drawable.dog1)
							.into(petImageView);
				} else {
					Log.e("API Error", "Response code: " + response.code() + " Message: " + response.message());

				}
			}

			@Override
			public void onFailure(Call<Void> call, Throwable t) {
				Log.e("API Error", "Error accessing image: " + t.getMessage());
			}
		});
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
