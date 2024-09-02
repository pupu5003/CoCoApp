package com.example.cocoapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cocoapp.Fragment.Review;
import com.example.cocoapp.Fragment.VeterinarianProfile;
import com.example.cocoapp.R;
import com.example.cocoapp.Object.Grooming;

import java.util.List;

public class GroomingAdapter extends RecyclerView.Adapter<GroomingAdapter.GroomingViewHolder> {

    private List<Grooming> groomingList;
    private boolean showAll;
    private Context context;
    private boolean isGrooming;

    public GroomingAdapter(List<Grooming> groomingList, boolean showAll, Context context, boolean isGrooming) {
        this.groomingList = groomingList;
        this.showAll = showAll;
        this.context = context;
        this.isGrooming = isGrooming;
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
            reviewText.setText(String.format("%.1f (%d reviews)", grooming.getRating(), grooming.getReviews()));
            boolean isOpen = grooming.isOpen();
            isOpenText.setText(isOpen ? "Open" : "Closed");
            distanceText.setText(grooming.getDistance());
            priceText.setText(grooming.getPrice());
            availabilityText.setText(grooming.getAvailability());

            // Load image using Glide
            Glide.with(profileImage.getContext())
                    .load(grooming.getPic()) // Load image from the URL or file path stored in the string
                    .placeholder(R.drawable.vet1) // Placeholder image while loading
                    .error(R.drawable.vet2) // Error image if loading fails
                    .into(profileImage); // Set the loaded image into the ImageView
        }
    }
}
