package com.example.cocoapp.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cocoapp.Adapter.ReviewAdapter;
import com.example.cocoapp.Api.ApiClient;
import com.example.cocoapp.Api.ApiService;
import com.example.cocoapp.Object.ReviewItem;
import com.example.cocoapp.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Review extends Fragment {

	private static final String ARG_NAME = "name";
	private static final String ARG_TARGET_ID = "targetId";

	private static final String PREF_NAME = "user_prefs";
	private static final String KEY_AUTH_TOKEN = "auth_token";
	private static final String TYPE = "type";

	private String targetId;
	private RecyclerView recyclerView;
	private ReviewAdapter reviewAdapter;
	private List<ReviewItem> reviewList;
	private ApiService apiService;
	private String type;
	private TextView avrate,numberView;
	private RatingBar ratingBar;
	private ProgressBar p1,p2,p3,p4,p5;

	public Review() {

	}

	public static Review newInstance(String targetId,String type) {
		Review fragment = new Review();
		Bundle args = new Bundle();
		args.putString(ARG_TARGET_ID, targetId);
		args.putString(TYPE,type);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			targetId = getArguments().getString(ARG_TARGET_ID);
			type = getArguments().getString(TYPE);
		}
		apiService = ApiClient.getClient(getContext(), false).create(ApiService.class);
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_review, container, false);
		ImageView addButton = view.findViewById(R.id.addbtn);
		ImageButton backButton = view.findViewById(R.id.back_button);
		avrate = view.findViewById(R.id.avg_rate);
		numberView = view.findViewById(R.id.number_of_view);
		p1 = view.findViewById(R.id.progress1star);
		p2 = view.findViewById(R.id.progress2star);
		p3 = view.findViewById(R.id.progress3star);
		p4 = view.findViewById(R.id.progress4star);
		p5 = view.findViewById(R.id.progress5star);
		ratingBar = view.findViewById(R.id.ratingBar);
		recyclerView = view.findViewById(R.id.comment_recycle_view);
		recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

		reviewList = new ArrayList<>();
		reviewAdapter = new ReviewAdapter(reviewList, getContext());
		recyclerView.setAdapter(reviewAdapter);

		fetchReviews();

		addButton.setOnClickListener(v -> {
			requireActivity().getSupportFragmentManager().beginTransaction()
					.replace(R.id.fragment_container, AddReview.newInstance(targetId,type))
					.addToBackStack(null)
					.commit();
		});

		backButton.setOnClickListener(v -> {
			getActivity().getSupportFragmentManager().popBackStack();
		});



		return view;
	}

	private void fetchReviews() {
		Call<List<ReviewItem>> call = apiService.getAllReviews();

		call.enqueue(new Callback<List<ReviewItem>>() {
			@Override
			public void onResponse(Call<List<ReviewItem>> call, Response<List<ReviewItem>> response) {
				if (response.isSuccessful() && response.body() != null) {
					List<ReviewItem> allReviews = response.body();
					filterReviewsByTargetId(allReviews,targetId);
				} else {
					Toast.makeText(getContext(), "Failed to fetch reviews", Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onFailure(Call<List<ReviewItem>> call, Throwable t) {
				Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
			}
			private void filterReviewsByTargetId(List<ReviewItem> allReviews, String targetId) {
				List<ReviewItem> filteredReviews = new ArrayList<>();
				Float rating = 0f;
				int cnt = 0;
				int cnt_1=0,cnt_2=0,cnt_3=0,cnt_4=0,cnt_5=0;
				for (ReviewItem review : allReviews) {
					if (targetId.equals(review.getTargetId())) {
						filteredReviews.add(review);
						cnt++;
						if (review.getRating() == 1){
							cnt_1++;
						}
						else if (review.getRating() == 2){
							cnt_2++;
						}
						else if (review.getRating() == 3) {
							cnt_3++;
						}
						else if (review.getRating() == 4) {
							cnt_4++;
						}
						else if (review.getRating() == 5) {
							cnt_5++;
						}
						rating += review.getRating();
					}
				}
				if(cnt != 0) {
					avrate.setText(String.format("%.1f",rating/cnt));
					ratingBar.setRating(rating/cnt);
					p1.setProgress(cnt_1*100/cnt);
					p2.setProgress(cnt_2*100/cnt);
					p3.setProgress(cnt_3*100/cnt);
					p4.setProgress(cnt_4*100/cnt);
					p5.setProgress(cnt_5*100/cnt);
				}
				numberView.setText("Based on "+ String.valueOf(cnt)+" reviews");


				reviewList.clear();
				reviewList.addAll(filteredReviews);
				reviewAdapter.notifyDataSetChanged();
			}
		});
	}
}
