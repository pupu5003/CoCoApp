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
import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PetProfile extends Fragment {

	private static final String ARG_PET = "PET";
	private static final String PREF_NAME = "user_prefs";
	private static final String KEY_AUTH_TOKEN = "auth_token";

	private Pet pet;
	private Uri selectedImageUri;

	private ActivityResultLauncher<Intent> pickImageLauncher;
	private ActivityResultLauncher<String> requestPermissionLauncher;
	private ImageView petImageView,petgenderImageView;

	// private ProgressBar progressBar;

	private ApiService apiService;
	private SharedPreferences prefs;
	TextView petNameTextView, petBreedTextView, petage, petweight, petcolor, petheight,petLocation,header,name2,name3;

	View editFrame,informationFrame;
	public PetProfile() {
		// Default constructor
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
		setupPermissionLaunchers();
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_pet_profile, container, false);
		initializeViews(view);
		loadPetProfile();
		setupButtons(view);
		return view;
	}

	private void setupPermissionLaunchers() {
		requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
			if (isGranted) {
				openGallery();
			} else {
				Toast.makeText(getActivity(), "Permission denied", Toast.LENGTH_SHORT).show();
			}
		});
		pickImageLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
			if (result.getResultCode() == getActivity().RESULT_OK && result.getData() != null) {
				selectedImageUri = result.getData().getData();
				if (selectedImageUri != null) {
					try {
						Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImageUri);
						petImageView.setImageBitmap(bitmap);

						uploadPetImage();

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
//	@Override
//	public void onMapReady(GoogleMap googleMap) {
//		mMap = googleMap;
//		Log.d("MapFragment", "onMapReady called");
//		// Set a default location (e.g., Sydney) until the user enters a location
//		LatLng defaultLocation = new LatLng(-34, 151);
//		mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 10));
//	}

	private boolean convertLocationToCoordinates(String location) {
		Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
		try {
			List<Address> addresses = geocoder.getFromLocationName(location, 1);
			if (addresses != null && !addresses.isEmpty()) {
				Address address = addresses.get(0);
				LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());

				// Move the camera to the searched location
//				mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
//
//				// Place a marker at the searched location
//				mMap.addMarker(new MarkerOptions().position(latLng).title(address.getAddressLine(0)));

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


	private void initializeViews(View view){
		petNameTextView = view.findViewById(R.id.pet_name); // TextView
		petBreedTextView = view.findViewById(R.id.type); // TextView
		petgenderImageView = view.findViewById(R.id.gender); // ImageView
		petImageView = view.findViewById(R.id.imageDog); // ImageView
		petage = view.findViewById(R.id.age); // TextView
		petweight = view.findViewById(R.id.weight); // TextView
		petcolor = view.findViewById(R.id.color); // TextView
		petheight = view.findViewById(R.id.height); // TextView
		petLocation = view.findViewById(R.id.location); // TextView
		header = view.findViewById(R.id.tvHeader); // TextView
		name2 = view.findViewById(R.id.name2); // TextView
		name3 = view.findViewById(R.id.name3); // TextView
		editFrame = view.findViewById(R.id.edit_frame); // View or appropriate layout type
		informationFrame = view.findViewById(R.id.information_frame); // View or appropriate layout type

		// Correct type casting for ImageButton

		// progressBar = view.findViewById(R.id.progressBar);
		loadCachedPetProfile();
	}

	private void loadCachedPetProfile() {
		SharedPreferences prefs = requireActivity().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

		String petName = prefs.getString("pet_name", "Loading...");
		String breedName = prefs.getString("breed_name", "Loading...");
		int age = prefs.getInt("age", 0); // default is 0 if not found
		float weight = prefs.getFloat("weight", 0.0f); // default is 0.0 if not found
		String color = prefs.getString("color", "Unknown");
		float height = prefs.getFloat("height", 0.0f); // default is 0.0 if not found
		String gender = prefs.getString("gender", "U"); // default is 'U' for unspecified
		String imageUrl = prefs.getString("image_url", "");
	}

	private void setupButtons(View view) {
		ImageButton backButton = (ImageButton) view.findViewById(R.id.back_button); // ImageButton
		ImageButton editButton = (ImageButton) view.findViewById(R.id.edit_btn);    // ImageButton
		ImageButton imageButton = (ImageButton) view.findViewById(R.id.camera_btn); // ImageButton
		Button doneButton = (Button) view.findViewById(R.id.done_btn);         // Button

		editButton.setOnClickListener(v -> {
			informationFrame.setVisibility(View.GONE);
			editFrame.setVisibility(View.VISIBLE);
			editButton.setVisibility(View.GONE);
		});

		doneButton.setOnClickListener(v -> {
			// Hide the edit frame and show information frame
			editFrame.setVisibility(View.GONE);
			informationFrame.setVisibility(View.VISIBLE);
			editButton.setVisibility(View.VISIBLE);
			Toast.makeText(getActivity(), "Profile updated", Toast.LENGTH_SHORT).show();
			// Get references to the EditText fields
			EditText petNameEditText = view.findViewById(R.id.pet_name_edit);
			EditText petBreedEditText = view.findViewById(R.id.pet_breed_name);
			EditText petAgeEditText = view.findViewById(R.id.pet_age);
			EditText petWeightEditText = view.findViewById(R.id.pet_weight);
			EditText petColorEditText = view.findViewById(R.id.pet_colour);
			EditText petHeightEditText = view.findViewById(R.id.pet_height);
			EditText petLocationEditText = view.findViewById(R.id.pet_location);
			EditText genderEditText = view.findViewById(R.id.pet_gender);
			// Update the pet object with the new values from EditText
			if (!TextUtils.isEmpty(genderEditText.getText())) {
				pet.setGender(genderEditText.getText().charAt(0));
			}
			if (!TextUtils.isEmpty(petNameEditText.getText())) {
				pet.setPetName(petNameEditText.getText().toString());
			}
			if (!TextUtils.isEmpty(petBreedEditText.getText())) {
				pet.setBreedName(petBreedEditText.getText().toString());
			}
			if (!TextUtils.isEmpty(petAgeEditText.getText())) {
				pet.setAge(Integer.parseInt(petAgeEditText.getText().toString()));
			}
			if (!TextUtils.isEmpty(petWeightEditText.getText())) {
				pet.setWeight(Float.parseFloat(petWeightEditText.getText().toString()));
			}
			if (!TextUtils.isEmpty(petColorEditText.getText())) {
				pet.setColor(petColorEditText.getText().toString());
			}
			if (!TextUtils.isEmpty(petHeightEditText.getText())) {
				pet.setHeight(Float.parseFloat(petHeightEditText.getText().toString()));
			}
			if (!TextUtils.isEmpty(petLocationEditText.getText())) {
				pet.setLocation(petLocationEditText.getText().toString());
			}
			// Set gender image based on updated pet object
			if (pet.getGender().equals('M')) {
				petgenderImageView.setImageResource(R.drawable.male);
			} else {
				petgenderImageView.setImageResource(R.drawable.female);
			}
			// Update UI fields with new pet values
			name2.setText(pet.getPetName());
			name3.setText(pet.getPetName());
			header.setText(pet.getPetName());
			petNameTextView.setText(pet.getPetName());
			petBreedTextView.setText(pet.getBreedName());
			petage.setText(String.valueOf(pet.getAge()));
			petweight.setText(String.valueOf(pet.getWeight()));
			petcolor.setText(pet.getColor());
			petheight.setText(String.valueOf(pet.getHeight()));
			petLocationEditText.setText(pet.getLocation());
			// Call the method to update the pet in the backend
			uploadPetImage();
		});

		backButton.setOnClickListener(v ->
				getActivity().getSupportFragmentManager().popBackStack());

		imageButton.setOnClickListener(v -> {
			requireActivity().getSupportFragmentManager().beginTransaction()
					.replace(R.id.fragment_container, PetProfile.newInstance(pet))
					.commit();
			String permission = android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU ? "android.permission.READ_MEDIA_IMAGES" : "android.permission.READ_EXTERNAL_STORAGE";
			if (ContextCompat.checkSelfPermission(getActivity(), permission) != PackageManager.PERMISSION_GRANTED) {
				requestPermissionLauncher.launch(permission);
			} else {
				openGallery();
			}
		});
	}

	private void loadPetProfile() {
		// progressBar.setVisibility(View.VISIBLE);

		ApiService apiService = ApiClient.getClient(getActivity(), false).create(ApiService.class);
		String token = "Bearer " + getToken();
		Call<PetProfile> call = apiService.fetchPetProfile(token, pet.getId());

		call.enqueue(new Callback<PetProfile>() {
			@Override
			public void onResponse(Call<PetProfile> call, Response<PetProfile> response) {
				if (response.isSuccessful() && response.body() != null) {
					PetProfile profile = response.body();

					// Correct usage: accessing the methods from the 'pet' object
					name2.setText(pet.getPetName());
					name3.setText(pet.getPetName());
					header.setText(pet.getPetName());
					petNameTextView.setText(pet.getPetName());
					petBreedTextView.setText(pet.getBreedName());
					petImageView.setImageResource(R.drawable.dog1);
					petage.setText(String.valueOf(pet.getAge()));
					petheight.setText(String.valueOf(pet.getHeight()) + " cm");
					petweight.setText(String.valueOf(pet.getWeight()) + " kg");
					petcolor.setText(pet.getColor());
					petLocation.setText(pet.getLocation());

					if (pet.getGender().equals("M")) {
						petgenderImageView.setImageResource(R.drawable.male);
					} else {
						petgenderImageView.setImageResource(R.drawable.female);
					}

					// Cache the pet profile
					cachePetProfile(pet);

					// Load the pet image using Glide if the URL is not empty
					if (!TextUtils.isEmpty(pet.getImageUrl())) {
						String baseUrl = "http://172.28.102.169:8080";
						Glide.with(requireContext()).clear(petImageView);
						Glide.with(requireContext())
								.load(baseUrl + pet.getImageUrl())
								.error(R.drawable.dog1)  // Fallback image if loading fails
								.into(petImageView);
					}
				} else {
					Log.e("PetProfile", "Failed to load pet profile data: " + response.message());
					Toast.makeText(getActivity(), "Failed to load pet profile data", Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onFailure(Call<PetProfile> call, Throwable t) {
				// progressBar.setVisibility(View.GONE);
				Log.e("PetProfile", "Error loading profile: " + t.getMessage());
				Toast.makeText(getActivity(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
			}
		});
	}

	private void cachePetProfile(Pet pet) {
		SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();

		// Save pet details into SharedPreferences
		editor.putString("pet_id", pet.getId());  // Assuming the Pet object has an ID
		editor.putString("pet_name", pet.getPetName());
		editor.putString("breed_name", pet.getBreedName());
		editor.putInt("age", pet.getAge());
		editor.putFloat("weight", pet.getWeight());
		editor.putString("color", pet.getColor());
		editor.putFloat("height", pet.getHeight());
		editor.putString("gender", String.valueOf(pet.getGender()));
		editor.putString("image_url", pet.getImageUrl());
		editor.putString("address", pet.getLocation()); // Save address if available

		editor.apply(); // Commit the changes
	}

	private void uploadPetImage() {
		Pet petData = new Pet();
		petData.setId(pet.getId());
		petData.setPetName(petNameTextView.getText().toString().trim());
		petData.setBreedName(petBreedTextView.getText().toString().trim());
		petData.setAge(Integer.parseInt(petage.getText().toString().trim()));
		petData.setWeight(Float.parseFloat(petweight.getText().toString().trim()));
		petData.setColor(petcolor.getText().toString().trim());
		petData.setHeight(Float.parseFloat(petheight.getText().toString().trim()));
		petData.setGender(pet.getGender());
		petData.setLocation(petLocation.getText().toString().trim());

		MultipartBody.Part imagePart = null;
		if (selectedImageUri == null) {
			Toast.makeText(getActivity(), "No image selected", Toast.LENGTH_SHORT).show();
			return;
		}
		File imageFile = getImageFileFromImageView(petImageView);
		if (imageFile == null) {
			Toast.makeText(getActivity(), "Failed to convert image", Toast.LENGTH_SHORT).show();
			return;
		}
		if (selectedImageUri != null) {
			// Convert the selected image to a file and create a multipart body part
			imagePart = createMultipartBodyPartFromFile(imageFile);
		}


		// Convert the Pet object to JSON and create a request body
		RequestBody petPart = RequestBody.create(MediaType.parse("application/json"), new Gson().toJson(petData));

		// Setup the API call
		ApiService apiService = ApiClient.getClient(getActivity(), false).create(ApiService.class);
		String token = "Bearer " + getToken(); // Get the JWT token for authorization
		Call<Pet> call = apiService.updatePetInfo(imagePart, petPart, token);

		call.enqueue(new Callback<Pet>() {
			@Override
			public void onResponse(Call<Pet> call, Response<Pet> response) {
				if (response.isSuccessful()) {
					Toast.makeText(getActivity(), "Pet image updated successfully", Toast.LENGTH_SHORT).show();
					loadPetProfile();
				} else {
					Log.e("PetProfile", "Failed to update pet profile data: " + response.message());
					Toast.makeText(getActivity(), "Failed to update pet: " + response.message(), Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onFailure(Call<Pet> call, Throwable t) {
				Toast.makeText(getActivity(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
			}
		});
	}



	private String getPetId() {
		SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
		return sharedPreferences.getString("pet_id", "");
	}

	private String getToken() {
		SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
		String token = sharedPreferences.getString(KEY_AUTH_TOKEN, "");
		Log.d("Token", "Token retrieved: " + token);
		return token;
	}

	private File getImageFileFromImageView(ImageView imageView) {
		imageView.setDrawingCacheEnabled(true);
		Bitmap bitmap = Bitmap.createBitmap(imageView.getDrawingCache());
		imageView.setDrawingCacheEnabled(false);

		File file = new File(getContext().getCacheDir(), "pet_image.png");
		try (FileOutputStream fos = new FileOutputStream(file)) {
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return file;
	}

	private MultipartBody.Part createMultipartBodyPartFromFile(File file) {
		RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
		return MultipartBody.Part.createFormData("image", file.getName(), requestFile);
	}

	private void openGallery() {
		Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		pickImageLauncher.launch(intent);
	}
}
