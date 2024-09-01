package com.example.cocoapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cocoapp.R;
import com.example.cocoapp.Object.Grooming;

import java.util.List;

public class GroomingAdapter extends RecyclerView.Adapter<GroomingAdapter.GroomingViewHolder> {

    private List<Grooming> groomingList;

    private boolean showAll;

    public GroomingAdapter(List<Grooming> groomingList,boolean showAll) {
        this.groomingList = groomingList;
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
        holder.bind(grooming);
    }

    @Override
    public int getItemCount() {
        return showAll ? groomingList.size() : Math.min(groomingList.size(), 2);
    }

    static class GroomingViewHolder extends RecyclerView.ViewHolder {
        private TextView Grooming_name;
        private RatingBar ratingBar;
        private TextView reviewText;
        private TextView isOpenText;
        private TextView distanceText;
        private TextView priceText;
        private TextView availabilityText;
        private ImageView profileImage;

        public GroomingViewHolder(@NonNull View itemView) {
            super(itemView);
            Grooming_name = itemView.findViewById(R.id.grooming_name);
            ratingBar = itemView.findViewById(R.id.rating_layout);
            reviewText = itemView.findViewById(R.id.textView);
            isOpenText = itemView.findViewById(R.id.open_close);
            distanceText = itemView.findViewById(R.id.distance);
            priceText = itemView.findViewById(R.id.price);
            availabilityText = itemView.findViewById(R.id.availability);
            profileImage = itemView.findViewById(R.id.profile_image);
        }
        public void bind(Grooming grooming) {
            Grooming_name.setText(grooming.getName());
            ratingBar.setRating(grooming.getRating());
            reviewText.setText(String.format("%.1f {%d reviews}", grooming.getRating(), grooming.getReviews()));
            boolean ok = grooming.isOpen();
            isOpenText.setText(ok ? "Open" : "Closed");
            distanceText.setText(grooming.getDistance());
            priceText.setText(grooming.getPrice());
            availabilityText.setText(grooming.getAvailability());
            profileImage.setImageDrawable(grooming.getPic().getDrawable());
        }
    }
}