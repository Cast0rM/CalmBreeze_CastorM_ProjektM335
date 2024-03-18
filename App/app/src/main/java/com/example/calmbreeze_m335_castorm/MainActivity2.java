package com.example.calmbreeze_m335_castorm;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity2 extends AppCompatActivity {
    private TextView fadeTextView;
    private int count = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button startExperience = findViewById(R.id.startExperienceButton);
        startExperience.setOnClickListener(view -> {
            Intent experience = new Intent(this, Experience.class );
            startActivity(experience);
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        fadeTextView = findViewById(R.id.fadeText);
        fadeTextIn();
    }
    private void fadeTextIn(){

        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(fadeTextView, "alpha", 0f, 1f);
        fadeIn.setDuration(1000);
        fadeIn.setInterpolator(new AccelerateInterpolator());
        fadeIn.start();

        switch (count){
            case 1:
                fadeTextView.setText(getString(R.string.instruction1));
                count++;
                fadeTextView.setTextSize(40);

                break;
            case 2:
                fadeTextView.setText(getString(R.string.instruction2));
                fadeTextView.setTextSize(40);
                count++;
                break;
            case 3:
                fadeTextView.setText(getString(R.string.instruction3));
                fadeTextView.setTextSize(35);
                count++;
                break;
        }
        fadeIn.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                fadeOutText();
            }
        });
        if (count > 3) {
            count = 1; // Reset count to start over
        }
    }
    private void fadeTextOut(){
        long delay = 1000;
        fadeTextView.postDelayed(this::fadeOutText, delay);
    }
    private void fadeOutText(){
        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(fadeTextView, "alpha", 1f,0f);
        fadeOut.setDuration(1000);
        fadeOut.setInterpolator(new AccelerateInterpolator());
        fadeOut.start();

        fadeOut.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                fadeTextIn();
            }
        });
    }
}