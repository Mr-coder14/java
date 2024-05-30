package com.example.java;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

public class suceesanimation extends AppCompatActivity {

    private static final int DELAY_TIME_MS = 3000;
    private static final int SUCCESS_GIF_DISPLAY_TIME_MS = 2000; // Adjust this value if needed

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suceesanimation);
        final ImageView imageView = findViewById(R.id.gif);

        loadGif(R.raw.load, imageView);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loadGif(R.raw.payment_successfully, imageView);

                // Delay to show the success animation before transitioning to MainActivity
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(suceesanimation.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);
                        finish();
                    }
                }, SUCCESS_GIF_DISPLAY_TIME_MS);
            }
        }, DELAY_TIME_MS);
    }

    private void loadGif(int gifResource, ImageView imageView) {
        Glide.with(this)
                .asGif()
                .load(gifResource)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView);
    }
}
