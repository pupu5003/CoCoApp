package com.example.cocoapp.Api;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class ApiClient
{
	private static Retrofit retrofit = null;
	private static final String BASE_URL = "http://172.28.102.169:8080"; // Backend URL - Zerotier

	public static Retrofit getClient(Context context, boolean useBasicAuth)
	{
		retrofit = new Retrofit.Builder()
				.baseUrl(BASE_URL)
				.addConverterFactory(GsonConverterFactory.create())
				.client(getOkHttpClient(context, useBasicAuth))
				.build();

		return retrofit;
	}

	private static OkHttpClient getOkHttpClient(Context context, boolean useBasicAuth)
	{

		HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
		logging.setLevel(HttpLoggingInterceptor.Level.BODY); // Set the level of logging

		Interceptor authInterceptor = chain -> {
			Request originalRequest = chain.request();
			Request.Builder requestBuilder = originalRequest.newBuilder();

			if (useBasicAuth)
			{
				// Username: admin, Password: pass to connect to server
				String credentials = Credentials.basic("admin", "pass");
				requestBuilder.header("Authorization", credentials);
			} else
			{
				// Use JWT token for requests
				SharedPreferences prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
				String token = prefs.getString("jwt_token", null);
				if (token != null) { requestBuilder.header("Authorization", "Bearer " + token); }
			}

			Request authenticatedRequest = requestBuilder.build();

			return chain.proceed(authenticatedRequest);
		};

		return new OkHttpClient.Builder()
				.addInterceptor(logging)
				.addInterceptor(authInterceptor)
				.connectTimeout(30, TimeUnit.SECONDS)
				.readTimeout(30, TimeUnit.SECONDS)
				.writeTimeout(30, TimeUnit.SECONDS)
				.build();
	}

	// Create a method in ApiClient to get OkHttpClient instance
	public static OkHttpClient getOkHttpClient(Context context) {
		return new OkHttpClient.Builder()
				.addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
				.addInterceptor(new Interceptor() {
					@Override
					public Response intercept(Chain chain) throws IOException {
						Request originalRequest = chain.request();
						Request.Builder requestBuilder = originalRequest.newBuilder();
						SharedPreferences prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
						String token = prefs.getString("jwt_token", null);
						if (token != null) {
							requestBuilder.header("Authorization", "Bearer " + token);
						}
						Request authenticatedRequest = requestBuilder.build();
						return chain.proceed(authenticatedRequest);
					}
				})
				.connectTimeout(30, TimeUnit.SECONDS)
				.readTimeout(30, TimeUnit.SECONDS)
				.writeTimeout(30, TimeUnit.SECONDS)
				.build();
	}


}

