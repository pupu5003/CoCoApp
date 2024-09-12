package com.example.cocoapp.ActivityPage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.example.cocoapp.Fragment.Dashboard;
import com.example.cocoapp.Fragment.MapsFragment;


import com.example.cocoapp.Fragment.Notification;
import com.example.cocoapp.Fragment.Payment;
import com.example.cocoapp.Fragment.PetHealth;

import com.example.cocoapp.Fragment.Profile;
import com.example.cocoapp.Fragment.Shop;
import com.example.cocoapp.Fragment.VetVeterinarian;
import com.example.cocoapp.Fragment.ViewCart;
import com.example.cocoapp.R;



public class Bottom_Navigation extends AppCompatActivity {

    private final int home = 1;
    private final int discover = 2;
    private final int explore = 3;
    private final int manage = 4;
    private final int profile = 5;
    private MeowBottomNavigation bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bottom_navigation);

        bottomNavigation = findViewById(R.id.bottomNavigation);
        bottomNavigation.add(new MeowBottomNavigation.Model(home, R.drawable.ic_home));
        bottomNavigation.add(new MeowBottomNavigation.Model(discover, R.drawable.ic_discover));
        bottomNavigation.add(new MeowBottomNavigation.Model(explore, R.drawable.shop_ic));
        bottomNavigation.add(new MeowBottomNavigation.Model(manage, R.drawable.ic_manage));
        bottomNavigation.add(new MeowBottomNavigation.Model(profile, R.drawable.ic_profile));

        bottomNavigation.setOnClickMenuListener(model -> {
            Toast.makeText(Bottom_Navigation.this, "Item click: " + model.getId(), Toast.LENGTH_SHORT).show();
            return null;
        });

        bottomNavigation.setOnShowListener(model -> {
            Fragment selectedFragment = null;
            switch (model.getId()) {
                case home:
                    selectedFragment = new Dashboard();
                    break;
                case discover:
                    selectedFragment = new VetVeterinarian();
                    break;
                case explore:
                    selectedFragment = new Shop();
                    break;
                case manage:
                    selectedFragment = new Notification();
                    break;
                case profile:
                    selectedFragment = new Profile();
                    break;
            }
            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .commit();
            }
            return null;
        });

        // Set default tab
        bottomNavigation.show(home, true);
    }

    // Method to set the selected tab programmatically
    public void setSelectedTab(int tabId) {
        MeowBottomNavigation bottomNavigation = findViewById(R.id.bottomNavigation);
        bottomNavigation.show(tabId, true);  // Programmatically update the selected tab
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        //ZaloPaySDK.getInstance().onResult(intent);
        notifyPaymentFragment(intent);
    }
    private void notifyPaymentFragment(Intent intent) {
        // Get the fragment manager and find your Payment fragment by its tag
        Payment paymentFragment = (Payment) getSupportFragmentManager().findFragmentByTag("PaymentFragmentTag");
        if (paymentFragment != null) {
            paymentFragment.handleNewIntent(intent);
        }
    }
}
