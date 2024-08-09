package com.example.cocoapp.ActivityPage;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.example.cocoapp.R;

public class Bottom_Navigation extends AppCompatActivity {

    private final int home = 1;
    private final int discover = 2;
    private final int explore = 3;
    private final int manage = 4;
    private final int profile = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bottom_navigation);

        MeowBottomNavigation bottomNavigation = findViewById(R.id.bottomNavigation);

        bottomNavigation.add(new MeowBottomNavigation.Model(home, R.drawable.ic_home));
        bottomNavigation.add(new MeowBottomNavigation.Model(discover, R.drawable.ic_discover));
        bottomNavigation.add(new MeowBottomNavigation.Model(explore, R.drawable.ic_explore));
        bottomNavigation.add(new MeowBottomNavigation.Model(manage, R.drawable.ic_manage));
        bottomNavigation.add(new MeowBottomNavigation.Model(profile, R.drawable.ic_profile));

        bottomNavigation.setOnClickMenuListener(model -> {
            Toast.makeText(Bottom_Navigation.this, "Item click: " + model.getId(), Toast.LENGTH_SHORT).show();
            return null;
        });

        bottomNavigation.setOnShowListener(model -> {
            String name = "";
            switch (model.getId()) {
                case home:
                    name = "Home";
                    break;
                case discover:
                    name = "Discover";
                    break;
                case explore:
                    name = "Explore";
                    break;
                case manage:
                    name = "Manage";
                    break;
                case profile:
                    name = "Profile";
                    break;
            }
            Toast.makeText(Bottom_Navigation.this, "Showing: " + name, Toast.LENGTH_SHORT).show();

            // bottomNavigation.setCount(home, "9");

            return null;
        });

        bottomNavigation.show(home, true);
    }
}
