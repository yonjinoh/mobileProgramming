package com.example.achievementtracker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class DiaryActivity extends MenuActivity {

    private CalendarView calendarView;
    private TextView diaryTextView;
    private TextView diaryTitleView;
    private FloatingActionButton addDiary;
    private String selectedDate;
    private static final String PREFS_NAME = "diary_prefs";
    private static final String DIARY_KEY = "diaries";
    private Map<String, DiaryEntry> diaryEntries;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);

        calendarView = findViewById(R.id.calendarView);
        diaryTitleView = findViewById(R.id.diaryTitle);
        diaryTextView = findViewById(R.id.diaryText);
        addDiary = findViewById(R.id.AddDiary);

        // 네비게이션 바 설정
        setupBottomNavigationView();
        setActiveMenuItem(R.id.navigation_diary);

        diaryEntries = loadDiaries();

        selectedDate = getCurrentDate();

        displayDiaryForDate(selectedDate);

        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            selectedDate = year + "/" + (month + 1) + "/" + dayOfMonth;
            displayDiaryForDate(selectedDate);
        });

        addDiary.setOnClickListener(v -> {
            Intent intent = new Intent(DiaryActivity.this, AddDiaryActivity.class);
            intent.putExtra("selectedDate", selectedDate);
            startActivityForResult(intent, 1);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            String title = data.getStringExtra("diaryTitle");
            String text = data.getStringExtra("diaryText");

            if (title != null && text != null) {
                DiaryEntry diaryEntry = new DiaryEntry(title, text);

                diaryEntries.put(selectedDate, diaryEntry);
                saveDiaries(diaryEntries);
                displayDiaryForDate(selectedDate);

                Toast.makeText(this, "일기가 저장되었습니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void displayDiaryForDate(String date) {
        DiaryEntry diaryEntry = diaryEntries.get(date);
        if (diaryEntry != null) {
            diaryTitleView.setText(diaryEntry.getTitle());
            diaryTextView.setText(diaryEntry.getText());
        } else {
            diaryTitleView.setText("일기 제목 없음");
            diaryTextView.setText("해당 날짜에 작성된 일기가 없습니다.");
        }
    }

    private void saveDiaries(Map<String, DiaryEntry> diaries) {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(diaries);
        editor.putString(DIARY_KEY, json);
        editor.apply();
    }

    private Map<String, DiaryEntry> loadDiaries() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = prefs.getString(DIARY_KEY, "{}");

        // 기본 형식을 Map<String, DiaryEntry>로 지정
        Type newType = new TypeToken<Map<String, DiaryEntry>>() {}.getType();

        try {
            // 새로운 DiaryEntry 객체 형식으로 불러오기 시도
            return gson.fromJson(json, newType);
        } catch (JsonSyntaxException e) {
            // 기존 데이터가 Map<String, String> 형식이라면, 이를 DiaryEntry로 변환
            Type oldType = new TypeToken<Map<String, String>>() {}.getType();
            Map<String, String> oldData = gson.fromJson(json, oldType);
            Map<String, DiaryEntry> newData = new HashMap<>();

            for (Map.Entry<String, String> entry : oldData.entrySet()) {
                // 기존 데이터를 새 DiaryEntry 객체로 변환하여 저장
                newData.put(entry.getKey(), new DiaryEntry("제목 없음", entry.getValue()));
            }

            // 변환된 데이터 저장
            saveDiaries(newData);
            return newData;
        }
    }


    private String getCurrentDate() {
        long date = calendarView.getDate();
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.setTimeInMillis(date);
        int year = calendar.get(java.util.Calendar.YEAR);
        int month = calendar.get(java.util.Calendar.MONTH) + 1;
        int day = calendar.get(java.util.Calendar.DAY_OF_MONTH);
        return year + "/" + month + "/" + day;
    }
}
