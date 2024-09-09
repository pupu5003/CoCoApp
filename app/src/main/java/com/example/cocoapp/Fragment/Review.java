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
	private String targetId;
	private RecyclerView recyclerView;
	private ReviewAdapter reviewAdapter;
	private List<ReviewItem> reviewList;
	private ApiService apiService;

	public Review() {

	}

	public static Review newInstance(String targetId) {
		Review fragment = new Review();
		Bundle args = new Bundle();
		args.putString(ARG_TARGET_ID, targetId);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			targetId = getArguments().getString(ARG_TARGET_ID);
		}
		apiService = ApiClient.getClient(getContext(), false).create(ApiService.class);
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_review, container, false);
		ImageView addButton = view.findViewById(R.id.addbtn);
		ImageButton backButton = view.findViewById(R.id.back_button);
		recyclerView = view.findViewById(R.id.comment_recycle_view);
		recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

		reviewList = new ArrayList<>();
		reviewAdapter = new ReviewAdapter(reviewList, getContext());
		recyclerView.setAdapter(reviewAdapter);

		fetchReviews();

		addButton.setOnClickListener(v -> {
			requireActivity().getSupportFragmentManager().beginTransaction()
					.replace(R.id.fragment_container, AddReview.newInstance("Review"))
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

		String targetId = getArguments().getString("targetId");

		call.enqueue(new Callback<List<ReviewItem>>() {
			@Override
			public void onResponse(Call<List<ReviewItem>> call, Response<List<ReviewItem>> response) {
				if (response.isSuccessful() && response.body() != null) {
					List<ReviewItem> allReviews = response.body();
					filterReviewsByTargetId(allReviews);
				} else {
					Toast.makeText(getContext(), "Failed to fetch reviews", Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onFailure(Call<List<ReviewItem>> call, Throwable t) {
				Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
			}
			private void filterReviewsByTargetId(List<ReviewItem> allReviews) {
				List<ReviewItem> filteredReviews = new ArrayList<>();
				for (ReviewItem review : allReviews) {
					if (targetId.equals(review.getTargetId())) {
						filteredReviews.add(review);
					}
				}
				reviewList.clear();
				reviewList.addAll(filteredReviews);
				reviewAdapter.notifyDataSetChanged();
			}
		});
	}
}
