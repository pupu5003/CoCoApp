package com.example.cocoapp.Fragment;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddPet#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddPet extends Fragment implements OnMapReadyCallback {
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";
	private String mParam1;
	private String mParam2;

	private GoogleMap mMap;
	private EditText locationEditText;

	public AddPet() {
		// Required empty public constructor
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
		if (getArguments() != null) {
			mParam1 = getArguments().getString(ARG_PARAM1);
			mParam2 = getArguments().getString(ARG_PARAM2);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_add_pet, container, false);

		// Initialize views
		locationEditText = view.findViewById(R.id.pet_location);
		Button btn = view.findViewById(R.id.btn);

		// Initialize the map
		SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
		if (mapFragment != null) {
			mapFragment.getMapAsync(this); // Pass 'this' since this class implements OnMapReadyCallback
		}

		// Set a listener for the Button
		btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
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
			}
		});

		return view;
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
				Log.d("Location", "Location: " + address.getLatitude() + ", " + address.getLongitude() + "");
			} else {
				Toast.makeText(getContext(), "Location not found", Toast.LENGTH_SHORT).show();
			}
		} catch (IOException e) {
			e.printStackTrace();
			Toast.makeText(getContext(), "Unable to get location", Toast.LENGTH_SHORT).show();
		}
	}
}
