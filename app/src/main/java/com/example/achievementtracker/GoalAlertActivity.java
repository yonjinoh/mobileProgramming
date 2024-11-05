package com.example.achievementtracker;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Calendar;

public class GoalAlertActivity extends AppCompatActivity {

    private Switch switchEnableAlert;
    private TextView tvAlertTime;
    private Button buttonSetTime;

    private int alertHour = 9; // 기본 시간 예시: 9시
    private int alertMinute = 0; // 기본 시간 예시: 0분

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal_alert);

        // View 초기화
        switchEnableAlert = findViewById(R.id.switchEnableAlert);
        tvAlertTime = findViewById(R.id.tvAlertTime);
        buttonSetTime = findViewById(R.id.buttonSetTime);

        // 기본 알림 시간 표시
        updateAlertTimeDisplay();

        // 알림 시간 설정 버튼 클릭 리스너
        buttonSetTime.setOnClickListener(v -> showTimePicker());

        // 알림 활성화 여부 설정
        switchEnableAlert.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // 알림 활성화 상태를 저장할 수 있음 (SharedPreferences 사용 가능)
        });
    }

    private void showTimePicker() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (TimePicker view, int hourOfDay, int minute) -> {
                    alertHour = hourOfDay;
                    alertMinute = minute;
                    updateAlertTimeDisplay();
                }, alertHour, alertMinute, true);
        timePickerDialog.show();
    }

    private void updateAlertTimeDisplay() {
        tvAlertTime.setText(String.format("Alert Time: %02d:%02d", alertHour, alertMinute));
    }
}
