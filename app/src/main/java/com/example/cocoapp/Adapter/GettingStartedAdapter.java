package com.example.cocoapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.cocoapp.R;

public class GettingStartedAdapter extends PagerAdapter {
    public static final int TYPE_FRAGMENT = 0;
    public static final int TYPE_IMAGE = 1;

    private Context context;
    private int[] sliderAllFragments = {
            R.layout.fragment_get_started1,
            R.layout.fragment_get_started2,
            R.layout.fragment_get_started3,
    };
    private int[] sliderAllImages = {
            R.drawable.getstarted1,
            R.drawable.getstarted2,
            R.drawable.getstarted3,
    };
    private int type;

    public GettingStartedAdapter(Context context, int type) {
        this.context = context;
        this.type = type;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (View) object;
    }

    @Override
    public int getCount() {
        return type == TYPE_FRAGMENT ? sliderAllFragments.length : sliderAllImages.length;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = null;

        if (type == TYPE_FRAGMENT) {
            view = layoutInflater.inflate(sliderAllFragments[position], container, false);
        } else if (type == TYPE_IMAGE) {
            view = layoutInflater.inflate(R.layout.fragment_get_started_image, container, false);
            ImageView imageView = view.findViewById(R.id.imageView);
            imageView.setImageResource(sliderAllImages[position]);
        }
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
