package com.example.achievementtracker;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.content.BroadcastReceiver;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.widget.TimePicker;
import android.widget.Button;
import android.content.Context;
import android.content.Intent;
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

        //네비게이션 바
        setupBottomNavigationView();

        //뒤로 가기
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> onBackPressed());

    }
    private void setDailyGoalAlarm() {
        // TimePicker에서 시간과 분 가져오기
        int hour = timePicker.getHour();
        int minute = timePicker.getMinute();

        // 알람 시간을 설정
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        // 알람 설정
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, GoalAlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        // 매일 설정된 시간에 알람 반복
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pendingIntent);

        //알림 시간 저장
        saveAlarmTime(hour, minute);

        // 알림 설정 시간 표시
        String alarmTimeText = String.format("알람이 설정되었습니다: %02d:%02d", hour, minute);
        alarmStatusTextView.setText(alarmTimeText);

        // 알림
        Toast.makeText(this, "Daily Goal Alarm Set for " + hour + ":" + minute, Toast.LENGTH_SHORT).show();
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
            // 저장된 시간이 있을 경우, TimePicker와 TextView 업데이트
            timePicker.setHour(hour);
            timePicker.setMinute(minute);

            String alarmTimeText = String.format("알람이 설정되었습니다: %02d:%02d", hour, minute);
            alarmStatusTextView.setText(alarmTimeText);
        } else {
            // 저장된 시간이 없을 경우 기본 텍스트 설정
            alarmStatusTextView.setText("알람이 설정되지 않았습니다");
        }
    }
}