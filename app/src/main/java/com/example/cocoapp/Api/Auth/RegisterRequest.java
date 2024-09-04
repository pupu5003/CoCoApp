package com.example.cocoapp.Api.Auth;

public class RegisterRequest {
	private String name;
	private String email;
	private String password;
	public RegisterRequest(String email, String password, String name)
	{
		this.email = email;
		this.password = password;
		this.name = name;
	}

}
