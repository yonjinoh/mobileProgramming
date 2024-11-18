package com.example.achievementtracker;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class GoalAlertActivity extends MenuActivity {

    private TimePicker timePicker;
    private Button buttonSetAlarm;
    private TextView alarmStatusTextView;

    private static final String PREFS_NAME = "goal_alert_prefs";
    private static final String PREF_ALARM_HOUR = "alarm_hour";
    private static final String PREF_ALARM_MINUTE = "alarm_minute";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal_alert);

        timePicker = findViewById(R.id.timePicker);
        buttonSetAlarm = findViewById(R.id.buttonSetAlarm);
        alarmStatusTextView = findViewById(R.id.alarmStatusTextView);

        loadAlarmTime();

        buttonSetAlarm.setOnClickListener(v -> setDailyGoalAlarm());

        // 네비게이션 바
        setupBottomNavigationView();
        setActiveMenuItem(R.id.navigation_share);

        // 뒤로 가기 버튼
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> onBackPressed());
    }

    private void setDailyGoalAlarm() {
        int hour = timePicker.getHour();
        int minute = timePicker.getMinute();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, GoalAlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !canScheduleExactAlarms()) {
            requestExactAlarmPermission();
            Toast.makeText(this, "정확한 알람 권한이 필요합니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (alarmManager != null) {
            alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(),
                    pendingIntent
            );
            Log.d("GoalAlertActivity", "알람이 설정되었습니다. 시간: " + calendar.getTime());
        } else {
            Log.e("GoalAlertActivity", "AlarmManager를 가져올 수 없습니다.");
        }

        saveAlarmTime(hour, minute);
        alarmStatusTextView.setText(String.format("알람이 설정되었습니다: %02d:%02d", hour, minute));
        Toast.makeText(this, "알람이 설정되었습니다. " + hour + ":" + minute, Toast.LENGTH_SHORT).show();
    }

    private boolean canScheduleExactAlarms() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            return alarmManager != null && alarmManager.canScheduleExactAlarms();
        }
        return true; // Android 12 미만에서는 항상 true
    }

    private void requestExactAlarmPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            Intent intent = new Intent(android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
            startActivity(intent);
        }
    }

    private void saveAlarmTime(int hour, int minute) {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(PREF_ALARM_HOUR, hour);
        editor.putInt(PREF_ALARM_MINUTE, minute);
        editor.apply();
    }

    private void loadAlarmTime() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        int hour = prefs.getInt(PREF_ALARM_HOUR, -1);
        int minute = prefs.getInt(PREF_ALARM_MINUTE, -1);

        if (hour != -1 && minute != -1) {
            timePicker.setHour(hour);
            timePicker.setMinute(minute);
            alarmStatusTextView.setText(String.format("알람이 설정되었습니다: %02d:%02d", hour, minute));
        } else {
            alarmStatusTextView.setText("알람이 설정되지 않았습니다.");
        }
    }
}
