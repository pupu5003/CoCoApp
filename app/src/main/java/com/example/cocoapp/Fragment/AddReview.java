package com.example.cocoapp.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.cocoapp.Api.ApiClient;
import com.example.cocoapp.Api.ApiService;
import com.example.cocoapp.Object.Pet;
import com.example.cocoapp.Object.ProfileData;
import com.example.cocoapp.Object.ReviewItem;
import com.example.cocoapp.R;
import com.google.gson.Gson;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddReview#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddReview extends Fragment {

	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";

	// TODO: Rename and change types of parameters
	private String mParam1;
	private String mParam2;
	private ImageButton backButton;
	private RatingBar ratingBar;
	private EditText reviewText;
	private ReviewItem cmt;
	private ApiService apiService;
	private SharedPreferences prefs;
	private String token;
	private ProfileData profile;
	private ImageView ava;
	private TextView name;

	private Button postBtn;
	public AddReview() {
		// Required empty public constructor
	}
	public static AddReview newInstance(String param1,String param2) {
		AddReview fragment = new AddReview();
		Bundle args = new Bundle();
		args.putString(ARG_PARAM1, param1);
		args.putString(ARG_PARAM2, param2);
		fragment.setArguments(args);
		return fragment;
	}


	// TODO: Rename and change types and number of parameters
	public static AddReview newInstance(String param1) {
		AddReview fragment = new AddReview();
		Bundle args = new Bundle();
		args.putString(ARG_PARAM1, param1);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			mParam1 = getArguments().getString(ARG_PARAM1);
			mParam2 = getArguments().getString(ARG_PARAM2);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_add_review, container, false);
		apiService = ApiClient.getClient(getActivity(), false).create(ApiService.class);
		prefs = requireActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
		token = prefs.getString("jwt_token", null);
		backButton = view.findViewById(R.id.back_button);
		ratingBar  = view.findViewById(R.id.ratingBar2);
		reviewText = view.findViewById(R.id.editTextTextMultiLine);
		ava = view.findViewById(R.id.ivAva);
		name = view.findViewById(R.id.tvName);
		postBtn = view.findViewById(R.id.btn_post);

		loadProfileData();
		postBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				cmt = new ReviewItem();
				cmt.setDetail(reviewText.getText().toString());
				cmt.setType(mParam2);
				cmt.setTargetId(mParam1);
				cmt.setUserId(profile.getUserId());
				cmt.setRating(ratingBar.getRating());
				addReview(token);
			}
		});


		backButton.setOnClickListener(v -> {
			getActivity().getSupportFragmentManager().popBackStack();
		});
		return view;
	}
	private void loadProfileData() {
		Call<ProfileData> call = apiService.fetchProfile("Bearer " + token);
		call.enqueue(new Callback<ProfileData>() {
			@Override
			public void onResponse(Call<ProfileData> call, Response<ProfileData> response) {

				if (response.isSuccessful() && response.body() != null) {
					profile = response.body();
					if (!TextUtils.isEmpty(profile.getImageUrl())) {
						String baseUrl = "http://172.28.102.169:8080";
						Glide.with(requireContext())
								.load(baseUrl + profile.getImageUrl())
								.error(R.drawable.ava)
								.into(ava);
						name.setText(profile.getName());
					}
				} else {
					Toast.makeText(getActivity(), "Failed to load profile data", Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onFailure(Call<ProfileData> call, Throwable t) {
				Toast.makeText(getActivity(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
			}
		});

	}
	public void addReview(String token) {
		// Convert ReviewDto to JSON format and create RequestBody
		Gson gson = new Gson();
		String jsonReview = gson.toJson(cmt);
		RequestBody requestBody = RequestBody.create(jsonReview, MediaType.parse("application/json"));
		// Call the API
		Call<ReviewItem> call = apiService.addReview("Bearer " + token,requestBody);
		call.enqueue(new retrofit2.Callback<ReviewItem>() {
			@Override
			public void onResponse(Call<ReviewItem> call, retrofit2.Response<ReviewItem> response) {
				if (response.isSuccessful()) {
					// Handle the successful response
					Toast.makeText(getActivity(), "Review added successfully!", Toast.LENGTH_SHORT).show();
				} else {
					Log.e("API Error", "Response code: " + response.code() + " Message: " + response.message());
				}
			}

			@Override
			public void onFailure(Call<ReviewItem> call, Throwable t) {
				// Handle the failure
				System.out.println("Error: " + t.getMessage());
			}
		});
	}
}