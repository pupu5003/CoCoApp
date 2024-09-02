package com.example.cocoapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cocoapp.R;
import com.example.cocoapp.Object.Veterinarian;

import java.util.List;

public class VeterinarianAdapter extends RecyclerView.Adapter<VeterinarianAdapter.VeterinarianViewHolder> {

    private List<Veterinarian> veterinarianList;
    private Context context;
    private boolean showAll;

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
    }

    @Override
    public int getItemCount() {
        return showAll ? veterinarianList.size() : Math.min(veterinarianList.size(), 2);
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
            availabilityText = itemView.findViewById(R.id.availability);
        }

        public void bind(Veterinarian veterinarian) {
            doctorName.setText(veterinarian.getName());
            doctorQualification.setText(veterinarian.getQualification());
            ratingBar.setRating(veterinarian.getRating());
            reviewText.setText(String.format("%.1f {%d reviews}", veterinarian.getRating(), veterinarian.getReviews()));
            experienceText.setText(String.format("%d years of experience", veterinarian.getExperience()));
            distanceText.setText(veterinarian.getDistance());
            priceText.setText(veterinarian.getPrice());
            availabilityText.setText(veterinarian.getAvailability());

            // Load the image from the URL or file path using Glide
            Glide.with(context)
                    .load(veterinarian.getProfileImage())
                    .error(R.drawable.vet1)  // Error image if loading fails
                    .into(profileImage);  // Set the loaded image into the ImageView
        }
    }
}
