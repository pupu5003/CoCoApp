package com.example.cocoapp.ActivityPage;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.cocoapp.Api.ApiClient;
import com.example.cocoapp.Api.ApiService;
import com.example.cocoapp.Api.Auth.AuthResponse;
import com.example.cocoapp.Api.Auth.LoginRequest;
import com.example.cocoapp.Api.Auth.RegisterRequest;
import com.example.cocoapp.Api.SendEmail;
import com.example.cocoapp.R;
import com.google.android.material.button.MaterialButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.Random;


public class LoginScreen extends AppCompatActivity {
	private TextView loginTextView, registerTextView, titleTextView, forgetPasswordTextView;
	private MaterialButton loginBtn, registerBtn;
	private View underlineView;
	private EditText editTextEmail, editTextPassword, username_res, password_res, name_res;
	private String generatedCode;
	private ConstraintLayout loginLayout, registerLayout;
	private ImageView imageViewGmail;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_screen);

		loginTextView = findViewById(R.id.loginTextView);
		registerTextView = findViewById(R.id.registerTextView);
		titleTextView = findViewById(R.id.textViewLogin);
		underlineView = findViewById(R.id.underlineView);
		loginLayout = findViewById(R.id.constraintLayoutLogin);
		registerLayout = findViewById(R.id.constraintLayoutRegister);
		forgetPasswordTextView = findViewById(R.id.forgetPasswordTextView);
		loginBtn = findViewById(R.id.buttonLogin);
		registerBtn = findViewById(R.id.buttonRegister);
		editTextEmail = findViewById(R.id.username);
		editTextPassword = findViewById(R.id.editTextPassword);
		username_res = findViewById(R.id.username_2);
		password_res = findViewById(R.id.editTextPassword_2);
		name_res = findViewById(R.id.editTextName_2);
		imageViewGmail = findViewById(R.id.imageViewGmail);


		loginTextView.setOnClickListener(v -> setActiveTab(true));
		registerTextView.setOnClickListener(v -> setActiveTab(false));
		registerBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ApiService apiService = ApiClient.getClient(getApplicationContext(), true).create(ApiService.class);
				String name = name_res.getText().toString().trim();
				String email = username_res.getText().toString().trim();
				String password = password_res.getText().toString().trim();
				RegisterRequest registerRequest = new RegisterRequest(email, password, name);
				Call<AuthResponse> call = apiService.registerUser(registerRequest);
				call.enqueue(new Callback<AuthResponse>() {
					@Override
					public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
						if (response.isSuccessful()) {
							AuthResponse authResponse = response.body();
							Toast.makeText(getApplicationContext(), "Registration successful!", Toast.LENGTH_SHORT).show();
							setActiveTab(true);
						} else {

						}
					}

					@Override
					public void onFailure(Call<AuthResponse> call, Throwable t) {

					}
				});

			}
		});
		imageViewGmail.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				hideKeyboard();
				String email = editTextEmail.getText().toString().trim();

				if (email.isEmpty()) {
					Toast.makeText(getApplicationContext(), "Please enter your email.", Toast.LENGTH_SHORT).show();
				} else {
					generatedCode = generateVerificationCode();
					sendVerificationEmail(email, generatedCode);
					showCodeInputDialog();
				}


				Intent intent = new Intent(LoginScreen.this, Bottom_Navigation.class);
				startActivity(intent);

			}
		});


		loginBtn.setOnClickListener(v -> {

			String email = editTextEmail.getText().toString().trim();
			String password = editTextPassword.getText().toString().trim();

			if (email.isEmpty() || password.isEmpty()) {
				Toast.makeText(this, "Please enter your email and password", Toast.LENGTH_SHORT).show();
			} else {
				performLogin(email, password);
			}
		});

		setupSocialButtons();
	}


	private void setActiveTab(boolean isLogin) {
		if (isLogin) {
			loginTextView.setTextColor(Color.parseColor("#419C82"));
			loginTextView.setTextSize(18);
			registerTextView.setTextColor(Color.parseColor("#5F5F63"));
			registerTextView.setTextSize(16);
			titleTextView.setText("Login");
			moveUnderline(loginTextView);
			loginLayout.setVisibility(View.VISIBLE);
			registerLayout.setVisibility(View.GONE);
		} else {
			registerTextView.setTextColor(Color.parseColor("#419C82"));
			registerTextView.setTextSize(18);
			loginTextView.setTextColor(Color.parseColor("#5F5F63"));
			loginTextView.setTextSize(16);
			titleTextView.setText("Register");
			moveUnderline(registerTextView);
			loginLayout.setVisibility(View.GONE);
			registerLayout.setVisibility(View.VISIBLE);
		}
	}

	private void moveUnderline(final View targetView) {
		final int[] startLocation = new int[2];
		underlineView.getLocationOnScreen(startLocation);
		final float startX = startLocation[0];
		final int startWidth = underlineView.getWidth();

		final int[] targetLocation = new int[2];
		targetView.getLocationOnScreen(targetLocation);
		final float endX = targetLocation[0];
		final int endWidth = targetView.getWidth();

		ValueAnimator positionAnimator = ValueAnimator.ofFloat(startX, endX);
		positionAnimator.addUpdateListener(animation -> {
			float animatedValue = (Float) animation.getAnimatedValue();
			underlineView.setX(animatedValue);
		});

		ValueAnimator widthAnimator = ValueAnimator.ofInt(startWidth, endWidth);
		widthAnimator.addUpdateListener(animation -> {
			int animatedWidth = (Integer) animation.getAnimatedValue();
			ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) underlineView.getLayoutParams();
			params.width = animatedWidth;
			underlineView.setLayoutParams(params);
		});

		AnimatorSet animatorSet = new AnimatorSet();
		animatorSet.playTogether(positionAnimator, widthAnimator);
		animatorSet.setDuration(300);
		animatorSet.start();
	}

	private void setupSocialButtons() {
		ViewGroup socialMediaLayout = findViewById(R.id.linearLayoutSocialMedia);
		if (socialMediaLayout != null && socialMediaLayout.getChildCount() > 0) {
			View firstButton = socialMediaLayout.getChildAt(0);
			firstButton.setOnClickListener(v -> showEmailInputDialog());
		} else {
			Toast.makeText(this, "Social media buttons layout not found or empty.", Toast.LENGTH_SHORT).show();
		}
	}

	private void performLogin(String email, String password) {
		ApiService apiService = ApiClient.getClient(this, true).create(ApiService.class);
		LoginRequest loginRequest = new LoginRequest(email, password);

		Call<AuthResponse> call = apiService.loginUser(loginRequest);
		call.enqueue(new Callback<AuthResponse>() {
			@Override
			public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
				if (response.isSuccessful() && response.body() != null) {
					AuthResponse authResponse = response.body();

					String accessToken = authResponse.getAccessToken();
					saveToken(accessToken);

					Intent intent = new Intent(LoginScreen.this, Bottom_Navigation.class);
					startActivity(intent);
					finish();
				} else {
					Toast.makeText(LoginScreen.this, "Login failed. Please check your credentials.", Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onFailure(Call<AuthResponse> call, Throwable t) {
				Toast.makeText(LoginScreen.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
			}
		});
	}

	private void saveToken(String token) {
		getSharedPreferences("app_prefs", MODE_PRIVATE)
				.edit()
				.putString("jwt_token", token)
				.apply();
	}

	private void showEmailInputDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Enter Your Email");

		final EditText emailInput = new EditText(this);
		emailInput.setHint("Enter your email");
		builder.setView(emailInput);

		builder.setPositiveButton("Send Code", (dialog, which) -> {
			hideKeyboard();
			String email = emailInput.getText().toString().trim();
			if (email.isEmpty()) {
				Toast.makeText(this, "Please enter your email.", Toast.LENGTH_SHORT).show();
			} else {
				String generatedCode = generateVerificationCode();
				sendVerificationEmail(email, generatedCode);
				showCodeInputDialog();
			}
		});

		builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
		builder.show();
	}

	private String generateVerificationCode() {
		Random random = new Random();
		int code = 100000 + random.nextInt(900000);
		String generated = String.valueOf(code);
		Log.d("generateVerificationCode", "Generated code: " + generated);
		return generated;
	}

	private void sendVerificationEmail(String email, String code) {
		if (code != null) {
			generatedCode = code;
			SendEmail.sendEmail(
					this,
					email,
					"Your Authentication Code",
					"Here is your authentication code: " + code
			);
			Toast.makeText(this, "Verification code sent to " + email, Toast.LENGTH_LONG).show();
		} else {
			Log.d("sendVerificationEmail", "Code was null, not sending email.");
		}
	}

	private void showCodeInputDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Enter Verification Code");

		final EditText codeInput = new EditText(this);
		codeInput.setHint("Enter Code");
		builder.setView(codeInput);

		builder.setPositiveButton("Verify", (dialog, which) -> {
			hideKeyboard();
			String code = codeInput.getText().toString().trim();
			Log.d("Verify Code Input", code);
			Log.d("Current Generated Code", generatedCode != null ? generatedCode : "null");

			if (verifyCode(code)) {
				Toast.makeText(this, "Verification successful!", Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(LoginScreen.this, Bottom_Navigation.class);
				startActivity(intent);
				finish();
			} else {
				Toast.makeText(this, "Invalid code, please try again.", Toast.LENGTH_SHORT).show();
			}
		});

		builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
		builder.show();
	}

	private boolean verifyCode(String inputCode) {
		Log.d("input: ", inputCode);
		if (generatedCode != null) {
			Log.d("generatedCode", generatedCode);
		} else {
			Log.d("generatedCode", "null");
		}
		return generatedCode != null && generatedCode.equals(inputCode);
	}

	private void hideKeyboard() {
		View view = this.getCurrentFocus();
		if (view != null) {
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
		}
	}

	private void performLoginAfterVerification(String email) {
		ApiService apiService = ApiClient.getClient(this, true).create(ApiService.class);
		LoginRequest loginRequest = new LoginRequest(email, "");

		Call<AuthResponse> call = apiService.loginUser(loginRequest);
		call.enqueue(new Callback<AuthResponse>() {
			@Override
			public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
				if (response.isSuccessful() && response.body() != null) {
					AuthResponse authResponse = response.body();

					saveToken(authResponse.getAccessToken());

					Intent intent = new Intent(LoginScreen.this, Bottom_Navigation.class);
					startActivity(intent);
					finish();
				} else {
					Toast.makeText(LoginScreen.this, "Login failed. Please check your credentials.", Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onFailure(Call<AuthResponse> call, Throwable t) {
				Toast.makeText(LoginScreen.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
			}
		});
	}

}