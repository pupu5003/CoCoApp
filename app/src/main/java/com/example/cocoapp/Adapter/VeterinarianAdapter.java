package com.example.cocoapp.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cocoapp.Api.ApiClient;
import com.example.cocoapp.Api.ApiService;
import com.example.cocoapp.Fragment.VeterinarianProfile;
import com.example.cocoapp.R;
import com.example.cocoapp.Object.Veterinarian;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VeterinarianAdapter extends RecyclerView.Adapter<VeterinarianAdapter.VeterinarianViewHolder> {

    private List<Veterinarian> veterinarianList;
    private Context context;
    private boolean showAll;
    ApiService apiService;
    SharedPreferences prefs;
    String token;

    public VeterinarianAdapter(Context context, List<Veterinarian> veterinarianList, boolean showAll) {
        this.context = context;
        this.veterinarianList = veterinarianList;
        this.showAll = showAll;
    }

    @NonNull
    @Override
    public VeterinarianViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.veterinarian_item, parent, false);
        return new VeterinarianViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VeterinarianViewHolder holder, int position) {
        Veterinarian veterinarian = veterinarianList.get(position);
        holder.bind(veterinarian);

        holder.itemView.setOnClickListener(view -> {
            FragmentActivity activity = (FragmentActivity) context;
            activity.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, VeterinarianProfile.newInstance(veterinarian))
                    .addToBackStack(null).commit();
        });
    }

    @Override
    public int getItemCount() {
        return showAll ? veterinarianList.size() : Math.min(veterinarianList.size(), 5);
    }

    public class VeterinarianViewHolder extends RecyclerView.ViewHolder {
        private ImageView profileImage;
        private TextView doctorName;
        private TextView doctorQualification;
        private RatingBar ratingBar;
        private TextView reviewText;
        private TextView experienceText;
        private ImageView distanceIcon;
        private TextView distanceText;
        private ImageView priceIcon;
        private TextView priceText;
        private ImageView clockIcon;
        private TextView availabilityText;

        public VeterinarianViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.profile_image);
            doctorName = itemView.findViewById(R.id.doctor_name);
            doctorQualification = itemView.findViewById(R.id.doctor_qualification);
            ratingBar = itemView.findViewById(R.id.rating_layout);
            reviewText = itemView.findViewById(R.id.textView);
            experienceText = itemView.findViewById(R.id.experience);
            distanceIcon = itemView.findViewById(R.id.distance_icon);
            distanceText = itemView.findViewById(R.id.distance);
            priceIcon = itemView.findViewById(R.id.price_icon);
            priceText = itemView.findViewById(R.id.price);
            clockIcon = itemView.findViewById(R.id.clock_icon);
            availabilityText = itemView.findViewById(R.id.availability);
        }

        public void bind(Veterinarian veterinarian) {
            doctorName.setText(veterinarian.getVetName());
            doctorQualification.setText(veterinarian.getDegree());
            ratingBar.setRating(veterinarian.getRating());
            reviewText.setText(String.valueOf(veterinarian.getRating()) + "{" +String.valueOf(veterinarian.getReviews().size() + " reviews}"));
            experienceText.setText(veterinarian.getExperience());
            distanceText.setText(String.valueOf(veterinarian.getDistance())+" km");
            priceText.setText(String.valueOf(veterinarian.getPrice()) + " VND");
            availabilityText.setText(veterinarian.getWorkTime());

            String baseUrl = "http://172.28.102.169:8080";
            String fileName = veterinarian.getImageUrl();
            String basePath = "/file/";
            if (fileName != null)
                fileName = fileName.substring(basePath.length());

            apiService = ApiClient.getClient(context, false).create(ApiService.class);
            SharedPreferences prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
            String token = prefs.getString("jwt_token", null);

            apiService.fetchImageFile("Bearer " + token, fileName).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        String fileName = veterinarian.getImageUrl();
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
