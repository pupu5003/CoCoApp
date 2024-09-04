package com.example.cocoapp.Api;

import com.example.cocoapp.Api.Auth.LoginRequest;
import com.example.cocoapp.Api.Auth.RegisterRequest;
import com.example.cocoapp.Api.Auth.AuthResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService
{

	// Login Endpoint
	@POST("/api/v1/auth/login")
	Call<AuthResponse> loginUser(@Body LoginRequest loginRequest);
	@POST("/api/v1/auth/register")
	Call<AuthResponse> registerUser(@Body RegisterRequest RegisterRequest );
}
