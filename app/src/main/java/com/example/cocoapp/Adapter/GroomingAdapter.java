package com.example.cocoapp.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cocoapp.Api.ApiClient;
import com.example.cocoapp.Api.ApiService;
import com.example.cocoapp.Fragment.Review;
import com.example.cocoapp.Fragment.VeterinarianProfile;
import com.example.cocoapp.R;
import com.example.cocoapp.Object.Grooming;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GroomingAdapter extends RecyclerView.Adapter<GroomingAdapter.GroomingViewHolder> {

    private List<Grooming> groomingList;
    private boolean showAll;
    private float lat, lont;
    private static Context context;
    private boolean isGrooming;

    public GroomingAdapter(List<Grooming> groomingList, boolean showAll, Context context, boolean isGrooming,Float lat,Float lont) {
        this.groomingList = groomingList;
        this.showAll = showAll;
        this.context = context;
        this.isGrooming = isGrooming;
        this.lat = lat;
        this.lont = lont;
    }
    public void setShowAll(boolean showAll) {
        this.showAll = showAll;
    }

    @NonNull
    @Override
    public GroomingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grooming_item, parent, false);
        return new GroomingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroomingViewHolder holder, int position) {
        Grooming grooming = groomingList.get(position);
        Pair<Float, Float> coordinates = convertLocationToCoordinates(grooming.getAddress());
        grooming.setDistance(calculateDistance(lat, lont, coordinates.first, coordinates.second));
        holder.bind(grooming);
        holder.bind(grooming);

        holder.itemView.setOnClickListener(view -> {
            if (isGrooming) {
                FragmentActivity activity = (FragmentActivity) context;
                activity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, Review.newInstance(grooming.getName(),"1"))
                        .addToBackStack(null).commit();
            }else{
                FragmentActivity activity = (FragmentActivity) context;
                activity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, Review.newInstance(grooming.getName(),"2"))
                        .addToBackStack(null).commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return showAll ? groomingList.size() : Math.min(groomingList.size(), 2);
    }
    private float calculateDistance(float lat1, float lon1, float lat2, float lon2) {
        final int R = 6371; // Radius of the Earth in kilometers

        // Convert latitude and longitude from degrees to radians
        float latDistance = (float) Math.toRadians(lat2 - lat1);
        float lonDistance = (float) Math.toRadians(lon2 - lon1);

        // Apply the Haversine formula
        float a = (float) (Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2));
        float c = (float) (2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a)));

        return R * c; // Distance in kilometers
    }

    private Pair<Float, Float> convertLocationToCoordinates(String location) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        Float latitude = null;
        Float longitude = null;

        try {
            List<Address> addresses = geocoder.getFromLocationName(location, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                latitude = (float) address.getLatitude();
                longitude = (float) address.getLongitude();
            } else {
                Log.d("Location", location);
                Toast.makeText(context, "Location not found", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Unable to get location", Toast.LENGTH_SHORT).show();
        }

        return new Pair<>(latitude, longitude);
    }


    public void setGroomingList(List<Grooming> filteredGroomings) {
        this.groomingList.clear();
        this.groomingList.addAll(filteredGroomings);
    }

    static class GroomingViewHolder extends RecyclerView.ViewHolder {
        private TextView groomingName;
        private RatingBar ratingBar;
        private TextView reviewText;
        private TextView isOpenText;
        private TextView distanceText;
        private TextView priceText;
        private TextView availabilityText;
        private ImageView profileImage;

        public GroomingViewHolder(@NonNull View itemView) {
            super(itemView);
            groomingName = itemView.findViewById(R.id.grooming_name);
            ratingBar = itemView.findViewById(R.id.rating_layout);
            reviewText = itemView.findViewById(R.id.textView);
            isOpenText = itemView.findViewById(R.id.open_close);
            distanceText = itemView.findViewById(R.id.distance);
            priceText = itemView.findViewById(R.id.price);
            availabilityText = itemView.findViewById(R.id.availability);
            profileImage = itemView.findViewById(R.id.profile_image);
        }

        public void bind(Grooming grooming) {
            groomingName.setText(grooming.getName());
            ratingBar.setRating(grooming.getRating());
            reviewText.setText(String.valueOf(grooming.getRating()) + " {" +String.valueOf(grooming.getReviews().size() + " reviews}"));
            boolean isOpen = grooming.isOpen();
            isOpenText.setText(isOpen ? "Open" : "Closed");
            distanceText.setText(String.valueOf(grooming.getDistance()) + " km");
            priceText.setText(String.valueOf(grooming.getPrice()) + " VND");
            availabilityText.setText(grooming.getWorkTime());

            String baseUrl = "http://172.28.102.169:8080";
            String fileName = grooming.getImageUrl();
            String basePath = "/file/";
            if (fileName != null)
                fileName = fileName.substring(basePath.length());

            ApiService apiService = ApiClient.getClient(context, false).create(ApiService.class);
            SharedPreferences prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
            String token = prefs.getString("jwt_token", null);

            apiService.fetchImageFile("Bearer " + token, fileName).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        String fileName = grooming.getImageUrl();
                        Glide.with(context)
                                .load(baseUrl+fileName)
                                .error(R.drawable.dog1)
                                .into(profileImage);

                        Log.e("Full Image URL", baseUrl + fileName);
                    } else {
                        Log.e("API Error", "Response code: " + response.code() + " Message: " + response.message());
                        Toast.makeText(context, "Failed to access image", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Log.e("API Error", "Error accessing image: " + t.getMessage());
                    Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
