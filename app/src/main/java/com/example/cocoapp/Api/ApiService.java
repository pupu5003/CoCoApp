package com.example.cocoapp.Api;

import com.example.cocoapp.Api.Auth.LoginRequest;
import com.example.cocoapp.Api.Auth.RegisterRequest;
import com.example.cocoapp.Api.Auth.AuthResponse;
import com.example.cocoapp.Object.Appointment;
import com.example.cocoapp.Object.CartDto;
import com.example.cocoapp.Object.CartItemDto;
import com.example.cocoapp.Object.Grooming;
import com.example.cocoapp.Object.Pet;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import com.example.cocoapp.Object.Product;

import com.example.cocoapp.Object.ProfileData;
import com.example.cocoapp.Object.ReviewItem;
import com.example.cocoapp.Object.ShowcaseDto;
import com.example.cocoapp.Object.Veterinarian;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

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
	@GET("api/v1/user/getPets")
	Call<List<Pet>> fetchPets(@Header("Authorization") String token);

	//Shop item
	@GET("/api/v1/shopItem/getAll")
	Call<List<Product>> fetchAllShopItems(@Header("Authorization") String authHeader);

	@GET("/file/{fileName}")
	Call<Void> fetchImageFile(@Header("Authorization") String authHeader, @Path("fileName") String fileName);


	// Update user profile with image upload
	@Multipart
	@POST("/api/v1/user/update")
	Call<ProfileData> updateUserInfo(
			@Header("Authorization") String token,
			@Part MultipartBody.Part image,
			@Part("user") RequestBody user
	);

	// Fetch user profile information
	@GET("/api/v1/user/getInfo")
	Call<ProfileData> fetchProfile(@Header("Authorization") String token);

	// Fetch user reviews
	// @GET("/api/v1/user/getReviews")
	// Call<List<ReviewDto>> fetchReviews(@Header("Authorization") String token);

	// Fetch user appointments
	 @GET("/api/v1/user/getAppointments")
	 Call<List<Appointment>> fetchAppointments(@Header("Authorization") String token);

	// Fetch product by ID
	@GET("/api/v1/shopItem/get/{shopItemId}")
	Call<Product> fetchProductById(@Header("Authorization") String authHeader, @Path("shopItemId") String productId);

	//Fetch all Vet
	@GET("/api/v1/vet/getAll")
	Call<List<Veterinarian>> fetchVets(@Header("Authorization") String token);


	@Multipart
	@POST("/api/v1/pet/update")
	Call<Pet> updatePet(
			@Part MultipartBody.Part image,
			@Part("pet") RequestBody Pet,
			@Header("Authorization") String authHeader
	);

	@GET("api/v1/vet/get/{vetId}")
	Call<Veterinarian> fetchVetById(@Header("Authorization") String token, @Path("vetId") String vetId);

	@Multipart
	@POST("/api/v1/vet/addAppointment")
	Call<String> addAppointment(@Header("Authorization") String authToken, @Part("appointment") Appointment appointment);

	@GET("/api/v1/location/getAll")
	Call<List<Grooming>> fetchAllGrooming(@Header("Authorization") String token);

	@GET("/api/v1/showcase/getAll")
	Call<List<ShowcaseDto>> getAllShowcases(@Header("Authorization") String token);

	@GET("/api/v1/review/getAll")
	Call<List<ReviewItem>> getAllReviews();

	@Multipart
	@POST("/api/v1/review/add")
	Call<ReviewItem> addReview(
			@Header("Authorization") String token,
			@Part("reviewDto") RequestBody reviewDto
	);

	@GET("api/v1/user/getInfoId/{userId}")
	Call<ProfileData> fetchUserInfoId(
			@Path("userId") String userId,
			@Header("Authorization") String authToken
	);

	@Multipart
	@POST("/api/v1/user/addAppointmentHistory")
	Call<String> addAppointmentHistory(
			@Header("Authorization") String token,
			@Part("appointment") RequestBody appointment
	);

	@GET("/api/v1/user/getAppointmentHistory")
	Call<List<Appointment>> getAppointmentHistory(@Header("Authorization") String token);

	@GET("/api/v1/cart/get")
	Call<CartDto> getCart(@Header("Authorization") String token);

	@Multipart
	@POST("/api/v1/cart/updateItem")
	Call<CartDto> updateCartItem(
			@Header("Authorization") String token,
			@Part("cartItemDto") RequestBody cartItemDto
	);

	@Multipart
	@POST("/api/v1/cart/addItem")
	Call<CartDto> addCartItem(
			@Header("Authorization") String token,
			@Part("cartItemDto") RequestBody cartItemDto
	);

	@Multipart
	@POST("/api/v1/vet/deleteAppointment")
	Call<String> deleteAppointment(
			@Header("Authorization") String authToken,
			@Part("appointment") RequestBody appointment
	);


}

