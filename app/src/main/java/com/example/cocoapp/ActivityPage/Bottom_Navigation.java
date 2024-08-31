package com.example.cocoapp.ActivityPage;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.example.cocoapp.Fragment.MapsFragment;
import com.example.cocoapp.Fragment.PetHealth;
import com.example.cocoapp.Fragment.VetVeterinarian;
import com.example.cocoapp.Fragment.ViewCart;
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
            Fragment selectedFragment = null;
            switch (model.getId()) {
                case home:
                    name = "Home";
                    break;
                case discover:
                    name = "Discover";
                    selectedFragment = new VetVeterinarian();
                    break;
                case explore:
                    name = "Explore";
                    selectedFragment = new PetHealth();
                    break;
                case manage:
                    name = "Manage";
                    selectedFragment = new MapsFragment();
                    break;
                case profile:
                    name = "Profile";
                    selectedFragment = new ViewCart();
                    break;
            }
            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment) // R.id.fragment_container is the ID of your FrameLayout or FragmentContainerView
                        .commit();
            }


            // bottomNavigation.setCount(home, "9");

            return null;
        });

        bottomNavigation.show(home, true);
    }
}
