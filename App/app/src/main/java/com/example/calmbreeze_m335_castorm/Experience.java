package com.example.calmbreeze_m335_castorm;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Experience extends AppCompatActivity implements SensorEventListener {
    private TextView countdownText;
    private long timeMs;
    private final Handler handler = new Handler();
    private final long Countdown_Duration_MS = 1 * 30 * 1000;
    private String channelID = "firstNotification";
    SensorManager sensorManager;
    Sensor accelerometer;
    private static final float GRAVITY_THRESHOLD = 2.0f;
    private boolean isBreathing = false;
    private long lastInhalation = 0;
    private TextView atemfrequenz;

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
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(Experience.this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(Experience.this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 101);
            }
        }

        countdownText = findViewById(R.id.countdown);
        atemfrequenz = findViewById(R.id.atemfrequenz);
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
    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }
    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float y = event.values[1];

            if (y > GRAVITY_THRESHOLD && !isBreathing) {
                isBreathing = true;
                long currentTime = System.currentTimeMillis();
                if (lastInhalation != 0) {
                    long timeDifference = currentTime - lastInhalation;
                    double breathsPerMinute = 60000.0 / timeDifference;
                    atemfrequenz.setText("Atemfrequenz: " + breathsPerMinute + " Atemzüge pro Minute");
                }
                lastInhalation = currentTime;
            } else if (y < -GRAVITY_THRESHOLD && isBreathing) {
                isBreathing = false;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}