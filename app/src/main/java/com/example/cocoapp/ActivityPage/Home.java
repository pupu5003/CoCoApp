package com.example.cocoapp.ActivityPage;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.cocoapp.R;

public class Home extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        Button getstart = findViewById(R.id.getStartedBtn);
        getstart.setOnClickListener(v -> {
            Intent intent = new Intent(Home.this, GettingStarted.class);
            startActivity(intent);
        });
        TextView login = findViewById(R.id.loginText);
        login.setOnClickListener(v -> {
            Intent intent = new Intent(Home.this, LoginScreen.class);
            startActivity(intent);
        });
        }

    }
