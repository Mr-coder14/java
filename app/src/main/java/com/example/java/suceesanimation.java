package com.example.java;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieProperty;
import com.airbnb.lottie.model.KeyPath;
import com.airbnb.lottie.value.LottieValueCallback;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

public class suceesanimation extends AppCompatActivity {

    private static final int DELAY_TIME_MS = 3000;
    private static final int SUCCESS_GIF_DISPLAY_TIME_MS = 2000;
    private TextView textView;
    private LottieAnimationView lottieAnimationView1,lottieAnimationView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suceesanimation);
        lottieAnimationView2 = findViewById(R.id.gif2);
        lottieAnimationView1=findViewById(R.id.gif1);
        textView=findViewById(R.id.ordertxt);

        textView.setVisibility(View.GONE);


        lottieAnimationView1.setAnimation(R.raw.loading);
        int color = getResources().getColor(R.color.green);
        ColorFilter colorFilter = new LightingColorFilter(color, color);


        lottieAnimationView1.addValueCallback(
                new KeyPath("**"),
                LottieProperty.COLOR_FILTER,
                new LottieValueCallback<ColorFilter>(colorFilter)
        );
        lottieAnimationView1.playAnimation();




        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                lottieAnimationView1.cancelAnimation();
                lottieAnimationView1.setVisibility(View.GONE);

                lottieAnimationView2.setAnimation(R.raw.paymentdone);
                lottieAnimationView2.setVisibility(View.VISIBLE);
                lottieAnimationView2.playAnimation();
                textView.setVisibility(View.VISIBLE);

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


}
