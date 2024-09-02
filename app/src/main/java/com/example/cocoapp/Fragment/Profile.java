package com.example.cocoapp.Fragment;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.cocoapp.R;

import java.io.IOException;

public class Profile extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private ImageView ava;

    // ActivityResultLaunchers for handling permission requests and image picking
    private ActivityResultLauncher<String> requestPermissionLauncher;
    private ActivityResultLauncher<Intent> pickImageLauncher;


    public Profile() {
        // Required empty public constructor
    }

    public static Profile newInstance(String param1, String param2) {
        Profile fragment = new Profile();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
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

        // Registering for permission result callback
        requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (isGranted) {
                openGallery();
            } else {
                Toast.makeText(getActivity(), "Permission denied", Toast.LENGTH_SHORT).show();
            }
        });

        // Registering for image picking result callback
        pickImageLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == getActivity().RESULT_OK && result.getData() != null) {
                Uri imageUri = result.getData().getData();
                if (imageUri != null) {
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                        ava.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(), "Failed to load image", Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                Toast.makeText(getActivity(), "No image selected", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        TextView addPetbtn = view.findViewById(R.id.add_pet_text);
        ImageButton editButton = view.findViewById(R.id.edit_btn);
        ImageButton doneButton = view.findViewById(R.id.done_btn);
        ImageButton imageButton = view.findViewById(R.id.camera_btn);
        ava = view.findViewById(R.id.imageOwner);

        TextView ownerName = view.findViewById(R.id.owner_name);
        TextView ownerEmail = view.findViewById(R.id.owner_email);
        TextView ownerPhone = view.findViewById(R.id.owner_phone);

        View editFrame = view.findViewById(R.id.information_frame_edit);
        View informationFrame = view.findViewById(R.id.information_frame);

        editButton.setOnClickListener(v -> {
            informationFrame.setVisibility(View.GONE);
            editFrame.setVisibility(View.VISIBLE);

        });

        doneButton.setOnClickListener(v -> {
            editFrame.setVisibility(View.GONE);
            informationFrame.setVisibility(View.VISIBLE);
            TextView ownerNameEdit = view.findViewById(R.id.owner_name_edit);
            TextView ownerEmailEdit = view.findViewById(R.id.owner_email_edit);
            TextView ownerPhoneEdit = view.findViewById(R.id.owner_phone_edit);
            if (!TextUtils.isEmpty(ownerNameEdit.getText())) {
                ownerName.setText(ownerName.getText().toString());
            }
            if (!TextUtils.isEmpty(ownerEmailEdit.getText())) {
                ownerEmail.setText(ownerEmailEdit.getText().toString());
            }
            if (!TextUtils.isEmpty(ownerPhoneEdit.getText())) {
                ownerPhone.setText(ownerPhoneEdit.getText().toString());
            }

        });

        addPetbtn.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new AddPet()).addToBackStack(null).commit();
        });

        imageButton.setOnClickListener(v -> {
            String permission = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU ?
                    "android.permission.READ_MEDIA_IMAGES" : "android.permission.READ_EXTERNAL_STORAGE";
            if (ContextCompat.checkSelfPermission(getActivity(), permission) != PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(permission);
            } else {
                openGallery();
            }
        });

        return view;
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickImageLauncher.launch(intent);
    }
}
