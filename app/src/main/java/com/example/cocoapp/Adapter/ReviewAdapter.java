package com.example.cocoapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cocoapp.Object.ReviewItem;
import com.example.cocoapp.R; // Update with your actual package name

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

	private List<ReviewItem> reviewList;
	private Context context;

	public ReviewAdapter(List<ReviewItem> reviewList, Context context) {
		this.reviewList = reviewList;
		this.context = context;
	}

	@NonNull
	@Override
	public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.commentcard, parent, false); // Replace with your actual layout name
		return new ReviewViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
		ReviewItem reviewItem = reviewList.get(position);
		holder.nameTextView.setText(reviewItem.getName());
		holder.timeTextView.setText(reviewItem.getTime());
		holder.ratingTextView.setText(String.valueOf(reviewItem.getRating()));
		holder.commentTextView.setText(reviewItem.getComment());
		holder.ratingBar.setRating(reviewItem.getRating());
	}

	@Override
	public int getItemCount() {
		return reviewList.size();
	}

	public static class ReviewViewHolder extends RecyclerView.ViewHolder {
		TextView nameTextView;
		TextView timeTextView;
		TextView ratingTextView;
		TextView commentTextView;
		RatingBar ratingBar;

		public ReviewViewHolder(@NonNull View itemView) {
			super(itemView);
			nameTextView = itemView.findViewById(R.id.tv_name);
			timeTextView = itemView.findViewById(R.id.tv_time);
			ratingTextView = itemView.findViewById(R.id.tv_rating);
			commentTextView = itemView.findViewById(R.id.tv_comment);
			ratingBar = itemView.findViewById(R.id.rating_bar);
		}
	}
}
