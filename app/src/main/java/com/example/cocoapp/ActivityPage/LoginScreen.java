package com.example.cocoapp.ActivityPage;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.cocoapp.R;
import com.google.android.material.button.MaterialButton;

public class LoginScreen extends AppCompatActivity {
    private TextView loginTextView, registerTextView, titleTextView;
    private MaterialButton loginBtn;
    private View underlineView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_screen);
        loginTextView = findViewById(R.id.loginTextView);
        registerTextView = findViewById(R.id.registerTextView);
        titleTextView = findViewById(R.id.textViewLogin);
        loginBtn = findViewById(R.id.buttonLogin);
        underlineView = findViewById(R.id.underlineView);

        loginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setActiveTab(true);
            }
        });

        registerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setActiveTab(false);
            }
        });

    }



    private void setActiveTab(boolean isLogin) {
        if (isLogin) {
            loginTextView.setTextColor(Color.parseColor("#419C82"));
            loginTextView.setTextSize(18);
            loginTextView.setTypeface(null, Typeface.BOLD);
            registerTextView.setTextColor(Color.parseColor("#5F5F63"));
            registerTextView.setTextSize(16);
            registerTextView.setTypeface(null, Typeface.NORMAL);
            titleTextView.setText("Login");
            loginBtn.setText("Login");
            moveUnderline(loginTextView);

        } else {
            registerTextView.setTextColor(Color.parseColor("#419C82"));
            registerTextView.setTextSize(18);
            registerTextView.setTypeface(null, Typeface.BOLD);
            loginTextView.setTextColor(Color.parseColor("#5F5F63"));
            loginTextView.setTextSize(16);
            loginTextView.setTypeface(null, Typeface.NORMAL);
            titleTextView.setText("Register");
            loginBtn.setText("Register");
            moveUnderline(registerTextView);
        }
    }
    private void moveUnderline(final View targetView) {
        // Measure the start position and width of underlineView relative to its parent
        final int[] startLocation = new int[2];
        underlineView.getLocationOnScreen(startLocation);
        final float startX = startLocation[0];
        final int startWidth = underlineView.getMeasuredWidth();


        // Measure the target position and width relative to the parent
        final int[] targetLocation = new int[2];
        targetView.getLocationOnScreen(targetLocation);
        final float endX = targetLocation[0];
        final int endWidth = targetView.getMeasuredWidth();


        // Animate the position
        ValueAnimator positionAnimator = ValueAnimator.ofFloat(startX, endX);
        positionAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatedValue = (Float) animation.getAnimatedValue();
                underlineView.setX(animatedValue);
            }
        });

        //Animate the width
        ValueAnimator widthAnimator = ValueAnimator.ofInt(startWidth, endWidth);
        widthAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int animatedWidth = (Integer) animation.getAnimatedValue();
                ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) underlineView.getLayoutParams();
                params.width = animatedWidth;
                underlineView.setLayoutParams(params);
            }
        });

        // Combine both animators
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(positionAnimator, widthAnimator);
        animatorSet.setDuration(300);
        animatorSet.start();
    }

}
