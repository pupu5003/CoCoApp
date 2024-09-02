package com.example.cocoapp.ActivityPage;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.cocoapp.Api.SendEmail;
import com.example.cocoapp.R;
import com.google.android.material.button.MaterialButton;

import android.content.DialogInterface;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Random;
import android.util.Log;

public class LoginScreen extends AppCompatActivity {
    private TextView loginTextView, registerTextView, titleTextView, forgetPasswordTextView;
    private MaterialButton loginBtn;
    private View underlineView;
    private EditText editTextEmail, editTextPassword;
    private String generatedCode;

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
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        forgetPasswordTextView = findViewById(R.id.forgetPasswordTextView);

        loginTextView.setOnClickListener(v -> setActiveTab(true));
        registerTextView.setOnClickListener(v -> setActiveTab(false));

        loginBtn.setOnClickListener(v -> {
            hideKeyboard();
            String email = editTextEmail.getText().toString().trim();

            if (email.isEmpty()) {
                Toast.makeText(this, "Please enter your email.", Toast.LENGTH_SHORT).show();
            } else {
                generatedCode = generateVerificationCode();
                sendVerificationEmail(email, generatedCode);
                showCodeInputDialog();
            }
        });
        setupSocialButtons();
    }

    private void setActiveTab(boolean isLogin) {
        if (isLogin) {
            loginTextView.setTextColor(Color.parseColor("#419C82"));
            loginTextView.setTextSize(18);
            registerTextView.setTextColor(Color.parseColor("#5F5F63"));
            registerTextView.setTextSize(16);
            titleTextView.setText("Login");
            loginBtn.setText("Login");
            moveUnderline(loginTextView);
        } else {
            registerTextView.setTextColor(Color.parseColor("#419C82"));
            registerTextView.setTextSize(18);
            loginTextView.setTextColor(Color.parseColor("#5F5F63"));
            loginTextView.setTextSize(16);
            titleTextView.setText("Register");
            loginBtn.setText("Register");
            moveUnderline(registerTextView);
        }
    }

    private void moveUnderline(final View targetView) {
        final int[] startLocation = new int[2];
        underlineView.getLocationOnScreen(startLocation);
        final float startX = startLocation[0];
        final int startWidth = underlineView.getWidth();

        final int[] targetLocation = new int[2];
        targetView.getLocationOnScreen(targetLocation);
        final float endX = targetLocation[0];
        final int endWidth = targetView.getWidth();

        ValueAnimator positionAnimator = ValueAnimator.ofFloat(startX, endX);
        positionAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatedValue = (Float) animation.getAnimatedValue();
                underlineView.setX(animatedValue);
            }
        });

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

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(positionAnimator, widthAnimator);
        animatorSet.setDuration(300);
        animatorSet.start();
    }

    private void setupSocialButtons() {
        ViewGroup socialMediaLayout = findViewById(R.id.linearLayoutSocialMedia);

        if (socialMediaLayout != null && socialMediaLayout.getChildCount() > 0) {
            View firstButton = socialMediaLayout.getChildAt(0);
            firstButton.setOnClickListener(v -> showEmailInputDialog());
        } else {
            Toast.makeText(this, "Social media buttons layout not found or empty.", Toast.LENGTH_SHORT).show();
        }
    }

    private void showEmailInputDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Your Email");

        final EditText emailInput = new EditText(this);
        emailInput.setHint("Enter your email");
        builder.setView(emailInput);

        builder.setPositiveButton("Send Code", (dialog, which) -> {
            hideKeyboard();
            String email = emailInput.getText().toString().trim();
            if (email.isEmpty()) {
                Toast.makeText(this, "Please enter your email.", Toast.LENGTH_SHORT).show();
            } else {
                generatedCode = generateVerificationCode();
                sendVerificationEmail(email, generatedCode);
                showCodeInputDialog();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builder.show();
    }

    private String generateVerificationCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }

    private void sendVerificationEmail(String email, String code) {
        SendEmail.sendEmail(
                this,
                email,
                "Your Authentication Code",
                "Here is your authentication code: " + code
        );
        Toast.makeText(this, "Verification code sent to " + email, Toast.LENGTH_LONG).show();
    }

    private void showCodeInputDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Verification Code");

        final EditText codeInput = new EditText(this);
        codeInput.setHint("Enter Code");
        builder.setView(codeInput);

        builder.setPositiveButton("Verify", (dialog, which) -> {
            hideKeyboard();
            String code = codeInput.getText().toString().trim();
            if (verifyCode(code)) {
                Toast.makeText(this, "Verification successful!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginScreen.this, Bottom_Navigation.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Invalid code, please try again.", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builder.show();
    }

    private boolean verifyCode(String inputCode) {
        return generatedCode != null && generatedCode.equals(inputCode);
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}