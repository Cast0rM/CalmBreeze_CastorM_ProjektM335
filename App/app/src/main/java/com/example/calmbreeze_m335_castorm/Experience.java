package com.example.calmbreeze_m335_castorm;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

public class Experience extends AppCompatActivity {
    private TextView countdownText;
    private long timeMs;
    private final Handler handler = new Handler();
    private final long Countdown_Duration_MS = 1 * 30 * 1000;
    private final long Countdown_Duration_MS = 10 * 60 * 1000;
    private String channelID = "firstNotification";

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
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(Experience.this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(Experience.this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 101);
            }
        }

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
                    countdownText.setText("00:00");
                    startVibration();
                    showNotification();
                }
            }
        });
    }

    private void showNotification() {
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("You did it!")
                .setContentText("You ended your Meditation session")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        Intent intent = new Intent(this, notification_activity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("data", "You Finished");

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_MUTABLE);
        notificationBuilder.setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = notificationManager.getNotificationChannel(channelID);
            if (notificationChannel == null) {
                int importance = NotificationManager.IMPORTANCE_HIGH;
                notificationChannel = new NotificationChannel(channelID, "cuidao", importance);
                notificationChannel.setLightColor(Color.BLUE);
                notificationChannel.enableVibration(true);
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }
        notificationManager.notify(0, notificationBuilder.build());
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