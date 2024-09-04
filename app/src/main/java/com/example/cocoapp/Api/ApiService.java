package com.example.cocoapp.Api;

import com.example.cocoapp.Api.Auth.LoginRequest;
import com.example.cocoapp.Api.Auth.RegisterRequest;
import com.example.cocoapp.Api.Auth.AuthResponse;
import com.example.cocoapp.Object.Pet;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiService
{

	// Login Endpoint
	@POST("/api/v1/auth/login")
	Call<AuthResponse> loginUser(@Body LoginRequest loginRequest);
	@POST("/api/v1/auth/register")
	Call<AuthResponse> registerUser(@Body RegisterRequest RegisterRequest );

	@Multipart
	@POST("api/v1/pet/add")
	Call<Pet> addPet(
			@Part MultipartBody.Part image,
			@Part("pet") RequestBody pet,
			@Header("Authorization") String authHeader
	);
}
