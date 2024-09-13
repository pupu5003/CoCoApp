package com.example.cocoapp.Fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;


import com.example.cocoapp.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

public class MapsFragment extends Fragment {
	private GoogleMap mMap;
	private List<LatLng> coordinates;
	private FusedLocationProviderClient mFusedLocationClient;
	private LatLng currentLocationLatLng;
	private final static int LOCATION_PERMISSION_REQUEST_CODE = 100;
	private int position;

	public static MapsFragment newInstance(ArrayList<LatLng> coordinates, int position) {
		MapsFragment fragment = new MapsFragment();
		Bundle args = new Bundle();
		args.putParcelableArrayList("coordinates", coordinates);  // Store the vector in the bundle
		args.putInt("position", position);
		fragment.setArguments(args);
		return fragment;
	}
	private OnMapReadyCallback callback = new OnMapReadyCallback() {
		@Override
		public void onMapReady(GoogleMap googleMap) {
			// Example: Add markers for retail shops
			mMap = googleMap;
			try {
				boolean success = googleMap.setMapStyle(
						MapStyleOptions.loadRawResourceStyle(
								requireContext(), R.raw.custom_map_style));

				if (!success) {
					Log.e("MapsActivity", "Style parsing failed.");
				}
			} catch (Resources.NotFoundException e) {
				Log.e("MapsActivity", "Can't find style. Error: ", e);
			}
			mMap.getUiSettings().setZoomControlsEnabled(true); // Adds zoom controls (+/- buttons)
			mMap.getUiSettings().setZoomGesturesEnabled(true); // Enables pinch-to-zoom gestures
			int num = 0;
			for (LatLng coordinate : coordinates) {
				num++;
				Log.d("Coordinate", coordinate.toString());
				// Add a marker at the current location and move the camera
				if (position == 1) mMap.addMarker(new MarkerOptions().position(coordinate).title("Here is the location"));
				else mMap.addMarker(new MarkerOptions().position(coordinate).title("Here is your "+ num +" Dog"));
				mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinate, 15));
			}
		}
	};


	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater,
	                         @Nullable ViewGroup container,
	                         @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_maps, container, false);
		return view;
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		if (getArguments() != null) {
			coordinates = getArguments().getParcelableArrayList("coordinates"); // Retrieve the ArrayList of LatLng
			position = getArguments().getInt("position");
		}
		SupportMapFragment mapFragment =
				(SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
		if (mapFragment != null) {
			mapFragment.getMapAsync(callback);
		}
		Log.d("Location","Location");
		//mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext());
		//getLastLocation();

		ImageButton backButton = view.findViewById(R.id.back_button);
		backButton.setOnClickListener(v -> {
			getActivity().getSupportFragmentManager().popBackStack();
		});
	}

//	private void getLastLocation() {
//		if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//			mFusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
//				@Override
//				public void onSuccess(Location location) {
//					if (location != null){
//						Geocoder geocoder = new Geocoder(requireContext(), Locale.getDefault());
//						List<Address> addressList= null;
//						try {
//							addressList = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
//							double latitude = addressList.get(0).getLatitude();
//							double longitude = addressList.get(0).getLongitude();
//							Log.d("getLatitude",String.valueOf(addressList.get(0).getLatitude()));
//							Log.d("getLongitude",String.valueOf(addressList.get(0).getLongitude()));
//							Log.d("getAddressLine",String.valueOf(addressList.get(0).getAddressLine(0)));
//							Log.d("getLocality",String.valueOf(addressList.get(0).getLocality()));
//							Log.d("getPostalCode",String.valueOf(addressList.get(0).getPostalCode()));
//							currentLocationLatLng = new LatLng(latitude, longitude);
//
//							// Check if the map is ready and update the UI
////							if (mMap != null) {
////								mMap.addMarker(new MarkerOptions().position(currentLocationLatLng).title("You are here"));
////								mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocationLatLng, 15));
////							}
//						} catch (IOException e) {
//							Log.d("Exception","Ngu");
//							throw new RuntimeException(e);
//						}
//
//					}
//
//				}
//			});
//		} else {
//			askPermission();
//		}
//	}

//	private void askPermission() {
//		if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
//				!= PackageManager.PERMISSION_GRANTED) {
//			// Request the permission
//			requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
//		}
//	}
	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
			if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				// Permission granted, proceed with location operations
				//getLastLocation();
			} else {
				Log.d("Permission","Denied");
			}
		}
	}
}