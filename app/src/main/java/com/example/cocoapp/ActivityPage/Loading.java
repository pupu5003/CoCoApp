package com.example.cocoapp.ActivityPage;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.cocoapp.R;

public class Loading extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loading);

		// Find the ImageView for dog_loading and loading
		ImageView dogLoading = findViewById(R.id.dog_loading);
		ImageView loading = findViewById(R.id.loading);

		// Load the dog_loading GIF
		Glide.with(this)
				.asGif()
				.load(R.drawable.dog_loading)  // Replace with your actual GIF drawable resource name
				.into(dogLoading);

		// Load the loading GIF
		Glide.with(this)
				.asGif()
				.load(R.drawable.loading)  // Replace with your actual GIF drawable resource name
				.into(loading);

		// Delay for 5 seconds before navigating to the GettingStarted activity
		new Handler().postDelayed(() -> {
			Intent intent = new Intent(Loading.this, Home.class);
			startActivity(intent);
			finish();  // Close the Loading activity so that the user can't go back to it
		}, 5000);  // 5000 milliseconds equals 5 seconds
	}
}
