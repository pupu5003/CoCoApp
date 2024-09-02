package com.example.cocoapp.Fragment;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Build;
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
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cocoapp.Adapter.PetAddPetAdapter;
import com.example.cocoapp.Object.Pet;
import com.example.cocoapp.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AddPet extends Fragment implements OnMapReadyCallback {
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";
	private String mParam1;
	private ActivityResultLauncher<Intent> resultLauncher;
	private ActivityResultLauncher<String> requestPermissionLauncher;
	private ImageView imageView;
	private GoogleMap mMap;
	private EditText locationEditText;
	private List<Pet> petList;
	private PetAddPetAdapter petAddPetAdapter;

	public AddPet() {
		
	}

	public static AddPet newInstance(String param1, String param2) {
		AddPet fragment = new AddPet();
		Bundle args = new Bundle();
		args.putString(ARG_PARAM1, param1);
		args.putString(ARG_PARAM2, param2);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
			requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
				if (isGranted) {
					openGallery();
				} else {
					Toast.makeText(getContext(), "Permission denied", Toast.LENGTH_SHORT).show();
				}
			});
		} else {
			requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
				if (isGranted) {
					openGallery();
				} else {
					Toast.makeText(getContext(), "Permission denied", Toast.LENGTH_SHORT).show();
				}
			});
		}

		resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
			if (result.getResultCode() == getActivity().RESULT_OK && result.getData() != null) {
				Uri imageUri = result.getData().getData();
				if (imageUri != null) {
					try {
						Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
						imageView.setImageBitmap(bitmap);

						// Store the URI as a tag in the ImageView
						imageView.setTag(imageUri.toString());
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

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_add_pet, container, false);

		// Initialize views
		locationEditText = view.findViewById(R.id.pet_location);
		Button addButton = view.findViewById(R.id.btn);
		Button uploadImage = view.findViewById(R.id.upload_btn);
		imageView = view.findViewById(R.id.imageView);
		ImageButton backButton = view.findViewById(R.id.back_button);
		EditText name = view.findViewById(R.id.pet_name);
		EditText breed = view.findViewById(R.id.pet_breed_name);
		EditText age = view.findViewById(R.id.pet_age);
		EditText gender = view.findViewById(R.id.pet_gender);
		EditText color = view.findViewById(R.id.pet_colour);
		EditText weight = view.findViewById(R.id.pet_weight);
		EditText height = view.findViewById(R.id.pet_height);
		petList = new ArrayList<>();



		backButton.setOnClickListener(view1 -> {
			getActivity().getSupportFragmentManager().popBackStack();
		});

		// Initialize the map
		SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
		if (mapFragment != null) {
			mapFragment.getMapAsync(this); // Pass 'this' since this class implements OnMapReadyCallback
		}

		// Set a listener for the Button
		addButton.setOnClickListener(v -> {
			String location = locationEditText.getText().toString().trim();
			if (!TextUtils.isEmpty(location)) {
				if (mMap != null) {
					convertLocationToCoordinates(location);
				} else {
					Toast.makeText(getContext(), "Map is not ready yet", Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(getContext(), "Please enter a location", Toast.LENGTH_SHORT).show();
			}
			String url = (String) imageView.getTag();
			Log.d("PetImageURL", "URL: " + url);

			Pet pet = new Pet(name.getText().toString(),url,breed.getText().toString(),Integer.parseInt(age.getText().toString()),gender.getText().toString(),color.getText().toString(),
					Float.parseFloat(height.getText().toString()),Float.parseFloat(weight.getText().toString()),0,0,0);
			petList.add(pet);
			petAddPetAdapter.notifyDataSetChanged();
		});

		// Handle image upload
		uploadImage.setOnClickListener(v -> {
			String permission = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU ? "android.permission.READ_MEDIA_IMAGES" : "android.permission.READ_EXTERNAL_STORAGE";
			if (ContextCompat.checkSelfPermission(getContext(), permission) != PackageManager.PERMISSION_GRANTED) {
				requestPermissionLauncher.launch(permission);
			} else {
				openGallery();
			}
		});
		RecyclerView recyclerView = view.findViewById(R.id.recycler_view_pets);
		recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

		String imageUrl1 = "android.resource://" + getContext().getPackageName() + "/" + R.drawable.dog1;
		String imageUrl2 = "android.resource://" + getContext().getPackageName() + "/" + R.drawable.dog2;

		petList.add(new Pet("Buddy", imageUrl1, "Golden Retriever", 5, "Male", "Golden", 60.0f, 30.0f, 75, 50, 80));
		petList.add(new Pet("Lucy", imageUrl2, "Labrador", 4, "Female", "Black", 55.0f, 25.0f, 60, 40, 70));

		petAddPetAdapter = new PetAddPetAdapter(petList, getContext());
		recyclerView.setAdapter(petAddPetAdapter);

		return view;
	}

	private void openGallery() {
		Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		resultLauncher.launch(intent);
	}

	@Override
	public void onMapReady(GoogleMap googleMap) {
		mMap = googleMap;
		Log.d("MapFragment", "onMapReady called");
		// Set a default location (e.g., Sydney) until the user enters a location
		LatLng defaultLocation = new LatLng(-34, 151);
		mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 10));
	}

	private void convertLocationToCoordinates(String location) {
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
			} else {
				Toast.makeText(getContext(), "Location not found", Toast.LENGTH_SHORT).show();
			}
		} catch (IOException e) {
			e.printStackTrace();
			Toast.makeText(getContext(), "Unable to get location", Toast.LENGTH_SHORT).show();
		}
	}

}
