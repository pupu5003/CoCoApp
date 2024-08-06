package com.example.cocoapp.ActivityPage;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cocoapp.Fragment.GetStarted1;
import com.example.cocoapp.R;

public class GettingStarted extends AppCompatActivity {

    private int currentFragment = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_getting_started);

        if (currentFragment == 1){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainerView,new GetStarted1(),null)
                    .commit();
        }


    }


}



