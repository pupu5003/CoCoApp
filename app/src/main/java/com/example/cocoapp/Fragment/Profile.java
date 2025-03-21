package com.example.cocoapp.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.cocoapp.ActivityPage.LoginScreen;
import com.example.cocoapp.Api.ApiClient;
import com.example.cocoapp.Api.ApiService;
import com.example.cocoapp.Object.ProfileData;
import com.example.cocoapp.R;
import com.google.gson.Gson;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.prefs.Preferences;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Profile extends Fragment {

    private static final String PREF_NAME = "user_prefs";
    private static final String KEY_AUTH_TOKEN = "auth_token";

    private ImageView ava;
    private TextView ownerName, ownerEmail, ownerPhone, addPetbtn, logoutText;
    private EditText ownerNameEdit, ownerEmailEdit, ownerPhoneEdit;
    private View editFrame, informationFrame;
    private Uri selectedImageUri;
    private ProgressBar progressBar;
    private ActivityResultLauncher<String> requestPermissionLauncher;
    private ActivityResultLauncher<Intent> pickImageLauncher;
    private ProfileData profile;
    private ApiService apiService;
    private SharedPreferences prefs;
    private String token;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupPermissionLaunchers();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        apiService = ApiClient.getClient(requireActivity(), false).create(ApiService.class);
        prefs = requireActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        token = prefs.getString("jwt_token", null);
        initializeViews(view);
        loadProfileData();

        ImageButton editButton = view.findViewById(R.id.edit_btn);
        ImageButton doneButton = view.findViewById(R.id.done_btn);
        ImageButton imageButton = view.findViewById(R.id.camera_btn);
        logoutText = view.findViewById(R.id.log_out_text);

        editButton.setOnClickListener(v -> {
            informationFrame.setVisibility(View.GONE);
            editFrame.setVisibility(View.VISIBLE);
            ownerNameEdit.setText(ownerName.getText());
            ownerEmailEdit.setText(ownerEmail.getText());
            ownerPhoneEdit.setText(ownerPhone.getText());
        });

        doneButton.setOnClickListener(v -> {
            if (!validateInputs()) {
                Toast.makeText(getActivity(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }
            ownerName.setText(ownerNameEdit.getText());
            ownerEmail.setText(ownerEmailEdit.getText());
            ownerPhone.setText(ownerPhoneEdit.getText());
            profile.setName(ownerNameEdit.getText().toString());
            profile.setEmail(ownerEmailEdit.getText().toString());
            profile.setPhone(ownerPhoneEdit.getText().toString());
            editFrame.setVisibility(View.GONE);
            informationFrame.setVisibility(View.VISIBLE);
            uploadImage();
        });

        imageButton.setOnClickListener(v -> {
            String permission = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU ?
                    "android.permission.READ_MEDIA_IMAGES" : "android.permission.READ_EXTERNAL_STORAGE";

            if (ContextCompat.checkSelfPermission(getActivity(), permission) != PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(permission);
            } else {
                openGallery();
            }
        });
        addPetbtn.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new AddPet()).addToBackStack(null).commit();
        });
        // Set up the logout click listener
        logoutText.setOnClickListener(v -> logout());
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
            Log.d("ProfileFragment", "Result Code: " + result.getResultCode());
            Log.d("ProfileFragment", "Result Data: " + result.getData());

            if (getActivity() != null && result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                selectedImageUri = result.getData().getData();

                if (selectedImageUri != null) {
                    Log.d("ProfileFragment", "Selected Image Uri: " + selectedImageUri.toString());
                    try {
                        // Display the selected image
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImageUri);
                        ava.setImageBitmap(bitmap);

                        // Proceed with image upload
                        uploadImage();

                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(), "Failed to load image", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e("ProfileFragment", "No image selected or URI is null");
                    Toast.makeText(getActivity(), "No image selected", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getActivity(), "No image selected", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void initializeViews(View view) {
        ava = view.findViewById(R.id.imageOwner);
        ownerName = view.findViewById(R.id.owner_name);
        ownerEmail = view.findViewById(R.id.owner_email);
        ownerPhone = view.findViewById(R.id.owner_phone);
        ownerNameEdit = view.findViewById(R.id.owner_name_edit);
        ownerEmailEdit = view.findViewById(R.id.owner_email_edit);
        ownerPhoneEdit = view.findViewById(R.id.owner_phone_edit);
        editFrame = view.findViewById(R.id.information_frame_edit);
        informationFrame = view.findViewById(R.id.information_frame);
        addPetbtn = view.findViewById(R.id.add_pet_text);
        progressBar = view.findViewById(R.id.progressBar);
    }

    private boolean validateInputs() {
        return !TextUtils.isEmpty(ownerNameEdit.getText()) &&
                !TextUtils.isEmpty(ownerEmailEdit.getText()) &&
                !TextUtils.isEmpty(ownerPhoneEdit.getText());
    }

    private void loadProfileData() {
        progressBar.setVisibility(View.VISIBLE);
        ApiService apiService = ApiClient.getClient(getActivity(), false).create(ApiService.class);
        String token = "Bearer " + getToken();
        Call<ProfileData> call = apiService.fetchProfile(token);

        call.enqueue(new Callback<ProfileData>() {
            @Override
            public void onResponse(Call<ProfileData> call, Response<ProfileData> response) {
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    ProfileData profile1 = response.body();
                    profile = profile1;
                    ownerName.setText(profile.getName());
                    ownerEmail.setText(profile.getEmail());
                    ownerPhone.setText(profile.getPhone());

                    if (!TextUtils.isEmpty(profile.getImageUrl())) {
                        String baseUrl = "http://172.28.102.169:8080";
                        Glide.with(requireContext())
                                .load(baseUrl + profile.getImageUrl())
                                .error(R.drawable.ava)
                                .into(ava);
                    }
                } else {
                    Toast.makeText(getActivity(), "Failed to load profile data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ProfileData> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void uploadImage() {
        File file = getImageFileFromImageView(ava);
        MultipartBody.Part body = createMultipartBodyPartFromFile(file);
        RequestBody profilePart = RequestBody.create(MediaType.parse("application/json"), new Gson().toJson(profile));

        ApiService apiService = ApiClient.getClient(getActivity(), false).create(ApiService.class);
        String token = "Bearer " + getToken();
        Call<ProfileData> call = apiService.updateUserInfo(token, body, profilePart);

        call.enqueue(new Callback<ProfileData>() {
            @Override
            public void onResponse(Call<ProfileData> call, Response<ProfileData> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getActivity(), "Profile image updated successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Failed to update profile: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ProfileData> call, Throwable t) {
                Toast.makeText(getActivity(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getToken() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String token = sharedPreferences.getString(KEY_AUTH_TOKEN, "");
        Log.d("Token", "Token retrieved: " + token);
        return token;
    }

    private void logout() {
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(KEY_AUTH_TOKEN);
        editor.apply();

        Intent intent = new Intent(getActivity(), LoginScreen.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

        Toast.makeText(getActivity(), "Logged out successfully", Toast.LENGTH_SHORT).show();
    }

    private File getImageFileFromImageView(ImageView imageView) {
        imageView.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(imageView.getDrawingCache());
        imageView.setDrawingCacheEnabled(false);

        File file = new File(getContext().getCacheDir(), "profile_image.png");
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
}
