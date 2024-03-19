package com.example.calmbreeze_m335_castorm;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Experience extends AppCompatActivity {
    private TextView countdownText;
    private long timeMs;
    private final Handler handler = new Handler();
    private final long Countdown_Duration_MS = 1 * 60 * 1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_experience);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        countdownText = findViewById(R.id.countdown);
        timeMs = System.currentTimeMillis();
        startCountdown();
    }

    private void startCountdown() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsedTimeMs = System.currentTimeMillis() - timeMs;
                long remainingTimeMs = Countdown_Duration_MS - elapsedTimeMs;
                updateCountdown(remainingTimeMs);
                if (remainingTimeMs > 0) {
                    handler.postDelayed(this, 1000); // Update countdown every second
                } else {
                    // Benachrichtigung || Popup
                    startVibration();
                }
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("timeMs", timeMs);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        timeMs = savedInstanceState.getLong("timeMs");
        startCountdown();
    }

    private void startVibration() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        if (vibrator != null && vibrator.hasVibrator()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                long vibration_Duration_MS = 3000;
                vibrator.vibrate(VibrationEffect.createOneShot(vibration_Duration_MS, VibrationEffect.DEFAULT_AMPLITUDE));
            }
        }
    }

    private void updateCountdown(long remainingTimeMs) {
        int seconds = (int) (remainingTimeMs / 1000);
        int minutes = seconds / 60;
        seconds %= 60;

        String countdownString = String.format("%02d:%02d", minutes, seconds);
        countdownText.setText(countdownString);
    }
}