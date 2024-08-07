package com.example.cocoapp.ActivityPage;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.cocoapp.Adapter.GettingStartedAdapter;
import com.example.cocoapp.R;

public class GettingStarted extends AppCompatActivity {
    ViewPager slideViewPager;
    ViewPager imageViewPager;
    LinearLayout dotIndicator;
    Button nextButton;
    TextView[] dots;

    GettingStartedAdapter slideAdapter;
    GettingStartedAdapter imageAdapter;

    ViewPager.OnPageChangeListener viewPagerListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            setDotIndicator(position);
            if (position == 2) {
                nextButton.setText("Let's start!");
            } else {
                nextButton.setText("Next");
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getting_started);

        slideViewPager = findViewById(R.id.slideViewPager);
        imageViewPager = findViewById(R.id.imageViewPager);
        dotIndicator = findViewById(R.id.dotIndicator);
        nextButton = findViewById(R.id.nextButton);

        slideAdapter = new GettingStartedAdapter(this, GettingStartedAdapter.TYPE_FRAGMENT);
        imageAdapter = new GettingStartedAdapter(this, GettingStartedAdapter.TYPE_IMAGE);

        slideViewPager.setAdapter(slideAdapter);
        imageViewPager.setAdapter(imageAdapter);
        setDotIndicator(0);
        slideViewPager.addOnPageChangeListener(viewPagerListener);
        imageViewPager.addOnPageChangeListener(viewPagerListener);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getItem(0) < 2) {
                    slideViewPager.setCurrentItem(getItem(1), true);
                    imageViewPager.setCurrentItem(slideViewPager.getCurrentItem());
                } else {
                    Intent intent = new Intent(GettingStarted.this, LoginScreen.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    public void setDotIndicator(int position) {
        dots = new TextView[3];
        dotIndicator.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8211;", Html.FROM_HTML_MODE_LEGACY));
            dots[i].setTextSize(35);
            dots[i].setTextColor(getResources().getColor(R.color.Grey, getApplicationContext().getTheme()));
            dotIndicator.addView(dots[i]);
        }
        if (dots.length > 0) {
            dots[position].setTextColor(getResources().getColor(R.color.Primary_color, getApplicationContext().getTheme()));
        }
    }

    private int getItem(int i) {
        return slideViewPager.getCurrentItem() + i;
    }
}
