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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cocoapp.Api.ApiClient;
import com.example.cocoapp.Api.ApiService;
import com.example.cocoapp.Object.ProfileData;
import com.example.cocoapp.Object.ReviewItem;
import com.example.cocoapp.R; // Update with your actual package name

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

	private List<ReviewItem> reviewList;
	private Context context;
	private ProfileData profile;
	private ApiService apiService;
	private SharedPreferences prefs;
	private String token;

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
		profile = new ProfileData();
		loadProfileData(reviewItem);

		if (profile.getImageUrl() != null) {
			String baseUrl = "http://172.28.102.169:8080";
			Glide.with(context)
					.load(baseUrl + profile.getImageUrl())
					.error(R.drawable.ava)
					.into(holder.iv_ava);
		}else{
			holder.iv_ava.setImageResource(R.drawable.ava);
		}

		if (profile.getName() != null) {holder.nameTextView.setText(profile.getName());}
		else {holder.nameTextView.setText("No name");}
		holder.ratingTextView.setText(String.valueOf(reviewItem.getRating()));
		holder.commentTextView.setText(reviewItem.getComment());
		holder.ratingBar.setRating(reviewItem.getRating());
	}

	@Override
	public int getItemCount() {
		return reviewList.size();
	}
	private void loadProfileData(ReviewItem reviewItem) {
		apiService = ApiClient.getClient(context, false).create(ApiService.class);
		prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
		token = prefs.getString("jwt_token", null);

		// Make the API call
		Call<ProfileData> call = apiService.fetchUserInfoId(reviewItem.getUserId(), "Bearer " + token);

		call.enqueue(new Callback<ProfileData>() {
			@Override
			public void onResponse(Call<ProfileData> call, Response<ProfileData> response) {
				if (response.isSuccessful()) {
					// Fetch successful, handle the response data
					profile = response.body();
					//Toast.makeText(context,"fetch success", Toast.LENGTH_SHORT).show();

				} else {
					// Handle the error response (e.g., show error message)
					//Toast.makeText(context,"fetch fail", Toast.LENGTH_SHORT).show();
					Log.d("fetch fail", "fetch fail");
				}
			}

			@Override
			public void onFailure(Call<ProfileData> call, Throwable t) {
				//Toast.makeText(context,"fetch fail", Toast.LENGTH_SHORT).show();
				// Handle the failure (e.g., show a Toast or log the error)
			}
		});
	}

	public static class ReviewViewHolder extends RecyclerView.ViewHolder {
		TextView nameTextView;
		TextView timeTextView;
		TextView ratingTextView;
		TextView commentTextView;
		RatingBar ratingBar;
		ImageView iv_ava;

		public ReviewViewHolder(@NonNull View itemView) {
			super(itemView);
			nameTextView = itemView.findViewById(R.id.tv_name);
			timeTextView = itemView.findViewById(R.id.tv_time);
			ratingTextView = itemView.findViewById(R.id.tv_rating);
			commentTextView = itemView.findViewById(R.id.tv_comment);
			ratingBar = itemView.findViewById(R.id.rating_bar);
			iv_ava = itemView.findViewById(R.id.iv_ava);
		}
	}
}
