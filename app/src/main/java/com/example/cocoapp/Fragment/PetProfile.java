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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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

	private Pet pet;

	private ActivityResultLauncher<Intent> pickImageLauncher;
	private ActivityResultLauncher<String> requestPermissionLauncher;
	private ImageView petImageView,petgenderImageView;

	private ApiService apiService;
	private SharedPreferences prefs;
	private String token;
	ImageButton backButton,editButton,imageButton;
	TextView petNameTextView, petBreedTextView, petage, petweight, petcolor, petheight,petLocation,header,name2,name3;

	View editFrame,informationFrame;
	Button doneButton ;
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
						//Glide.with(this).clear(petImageView);
						updatePet(token);
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
		apiService = ApiClient.getClient(requireActivity(), false).create(ApiService.class);
		prefs = requireActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
		token = prefs.getString("jwt_token", null);
		init(view);


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

			String baseUrl = "http://172.28.102.169:8080";
			Glide.with(requireActivity())
					.load(baseUrl+pet.getImage())
					.error(R.drawable.dog1)
					.into(petImageView);
			petLocation.setText(pet.getLocation());
			if (pet.getGender().equals('M')) {
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
			EditText petNameEditText = view.findViewById(R.id.pet_name_edit);
			EditText petBreedEditText = view.findViewById(R.id.pet_breed_name);
			EditText petAgeEditText = view.findViewById(R.id.pet_age);
			EditText petWeightEditText = view.findViewById(R.id.pet_weight);
			EditText petColorEditText = view.findViewById(R.id.pet_colour);
			EditText petHeightEditText = view.findViewById(R.id.pet_height);
			EditText petLocationEditText = view.findViewById(R.id.pet_location);
			EditText genderEditText = view.findViewById(R.id.pet_gender);

			petNameEditText.setText(pet.getPetName());
			petBreedEditText.setText(pet.getBreedName());
			petAgeEditText.setText(String.valueOf(pet.getAge()));
			petHeightEditText.setText((String.valueOf(pet.getHeight())));
			petColorEditText.setText(pet.getColor());
			petWeightEditText.setText((String.valueOf(pet.getWeight())));
			petLocationEditText.setText(pet.getLocation());
			genderEditText.setText(String.valueOf(pet.getGender()));
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
			petLocation.setText(pet.getLocation());
			//Glide.with(this).clear(petImageView);

			// Call the method to update the pet in the backend
			updatePet(token);
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


	}

	private void openGallery() {
		Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
			pickImageLauncher.launch(intent);
		} else {
			Log.w("ProfileFragment", "ACTION_PICK failed. Falling back to ACTION_GET_CONTENT.");

			// Fallback to ACTION_GET_CONTENT
			Intent fallbackIntent = new Intent(Intent.ACTION_GET_CONTENT);
			fallbackIntent.setType("image/*");
			if (fallbackIntent.resolveActivity(getActivity().getPackageManager()) != null) {
				pickImageLauncher.launch(fallbackIntent);
			} else {
				Log.e("ProfileFragment", "No activity available to handle either ACTION_PICK or ACTION_GET_CONTENT.");
				Toast.makeText(getActivity(), "No app available to pick images", Toast.LENGTH_SHORT).show();
			}
		}
	}


	private boolean convertLocationToCoordinates(String location) {
		Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
		try {
			List<Address> addresses = geocoder.getFromLocationName(location, 1);
			if (addresses != null && !addresses.isEmpty()) {
				Address address = addresses.get(0);
				LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
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
	private void init(View view){
		backButton = view.findViewById(R.id.back_button);
		petNameTextView = view.findViewById(R.id.pet_name);
		petBreedTextView = view.findViewById(R.id.type);
		petgenderImageView = view.findViewById(R.id.gender);
		petImageView = view.findViewById(R.id.imageDog);
		petage = view.findViewById(R.id.age);
		petweight = view.findViewById(R.id.weight);
		petcolor = view.findViewById(R.id.color);
		petheight = view.findViewById(R.id.height);
		petLocation = view.findViewById(R.id.location);
		header = view.findViewById(R.id.tvHeader);
		name2 = view.findViewById(R.id.name2);
		name3 = view.findViewById(R.id.name3);
		editButton = view.findViewById(R.id.edit_btn);
	    editFrame = view.findViewById(R.id.edit_frame);
		informationFrame = view.findViewById(R.id.information_frame);
		doneButton = view.findViewById(R.id.done_btn);
		imageButton = view.findViewById(R.id.camera_btn);

	}
	private File getImageFileFromImageView(ImageView imageView) {
		// Enable drawing cache
		imageView.setDrawingCacheEnabled(true);
		Bitmap bitmap = Bitmap.createBitmap(imageView.getDrawingCache());
		imageView.setDrawingCacheEnabled(false);

		// Create a file in cache directory
		File file = new File(getContext().getCacheDir(), "image.png");
		try (FileOutputStream fos = new FileOutputStream(file)) {
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos); // Save the bitmap to file
		} catch (IOException e) {
			e.printStackTrace();
		}
		return file;
	}
	private MultipartBody.Part createMultipartBodyPartFromFile(File file) {
		RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
		return MultipartBody.Part.createFormData("image", file.getName(), requestFile);
	}

	private void updatePet(String token){
		Log.d("Pet", "Updating pet: " + pet.getPetName());
		// Convert the Pet object to JSON
		Gson gson = new Gson();
		String petJson = gson.toJson(pet);
		Log.d("Pet", "Pet JSON: " + petJson);

		// Create RequestBody for the pet object
		RequestBody petRequestBody = RequestBody.create(MediaType.parse("application/json"), petJson);

		// Convert ImageView to File
		File file = getImageFileFromImageView(petImageView);
		// Create MultipartBody.Part from File
		MultipartBody.Part body = createMultipartBodyPartFromFile(file);


		Call<Pet> call = apiService.updatePet(body, petRequestBody,"Bearer " + token);
		call.enqueue(new Callback<Pet>() {
			@Override
			public void onResponse(Call<Pet> call, Response<Pet> response) {
				if (response.isSuccessful()) {
					Toast.makeText(getActivity(), "Pet updated successfully", Toast.LENGTH_SHORT).show();
				} else {
					if (getActivity() != null)
						Toast.makeText(getActivity(), "Failed to update pet", Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onFailure(Call<Pet> call, Throwable t) {
				if (getActivity() != null)
					Toast.makeText(getActivity(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
			}
		});

	}


}
