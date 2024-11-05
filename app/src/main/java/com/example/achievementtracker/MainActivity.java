package com.example.achievementtracker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends MenuActivity {

    private static final int REQUEST_CODE_ADD_GOAL = 1;
    private RecyclerView recyclerViewGoals;
    private GoalAdapter goalAdapter;
    private List<Goal> goalList;
    private CalendarView calendarView;
    private TextView todayAchievement, monthAchievement;
    private static final String PREFS_NAME = "goals_prefs";
    private static final String DATE_GOALS_PREFIX = "goals_";  // 날짜별 목표 구분

    private String selectedDate;  // 현재 선택된 날짜를 저장하는 변수

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 네비게이션 바 설정
        setupBottomNavigationView();

        // 목표 리스트 초기화
        goalList = new ArrayList<>();

        //상단 월간/주간 버튼 로직
        // 오늘 달성률 영역 클릭 시 통계 화면으로 이동
        LinearLayout todayAchievementContainer = findViewById(R.id.todayAchievementContainer);
        todayAchievementContainer.setOnClickListener(v -> openStatisticsActivity());

        // 월 달성률 영역 클릭 시 통계 화면으로 이동
        LinearLayout monthAchievementContainer = findViewById(R.id.monthAchievementContainer);
        monthAchievementContainer.setOnClickListener(v -> openStatisticsActivity());


        // View 초기화
        todayAchievement = findViewById(R.id.todayAchievement);
        monthAchievement = findViewById(R.id.monthAchievement);
        calendarView = findViewById(R.id.calendarView);
        recyclerViewGoals = findViewById(R.id.goalRecyclerView);

        // RecyclerView 설정
        recyclerViewGoals.setLayoutManager(new LinearLayoutManager(this));
        goalAdapter = new GoalAdapter(goalList);
        recyclerViewGoals.setAdapter(goalAdapter);

        // FloatingActionButton 클릭 이벤트 (목표 추가)
        FloatingActionButton fabAddGoal = findViewById(R.id.AddGoal);
        fabAddGoal.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddGoalActivity.class);
            startActivityForResult(intent, REQUEST_CODE_ADD_GOAL);
        });

        // CalendarView 날짜 변경 리스너 설정
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            selectedDate = year + "/" + (month + 1) + "/" + dayOfMonth;
            Toast.makeText(MainActivity.this, "Selected date: " + selectedDate, Toast.LENGTH_SHORT).show();

            // 선택한 날짜에 따라 목표 리스트 로드
            loadGoalsForDate(selectedDate);
        });

        // 초기 달성률 설정
        initializeAchievements();

        // 초기 날짜 설정 및 목표 로드
        selectedDate = getCurrentDate();
        loadGoalsForDate(selectedDate);
    }

    private void openStatisticsActivity() {
        Intent intent = new Intent(MainActivity.this, GoalMonthlyActivity.class);
        startActivity(intent);
    }

    private String getCurrentDate() {
        // 현재 날짜를 "yyyy/MM/dd" 형식으로 반환
        long date = calendarView.getDate();
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.setTimeInMillis(date);
        int year = calendar.get(java.util.Calendar.YEAR);
        int month = calendar.get(java.util.Calendar.MONTH) + 1;
        int day = calendar.get(java.util.Calendar.DAY_OF_MONTH);
        return year + "/" + month + "/" + day;
    }

    // 샘플 데이터로 오늘/월간 달성률 설정 (DB에서 불러오거나 계산 가능)
    private void initializeAchievements() {
        todayAchievement.setText("60%");
        monthAchievement.setText("70%");
    }

    // 목표 리스트를 SharedPreferences에 날짜별로 저장
    private void saveGoalsForDate(String date, List<Goal> goals) {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(goals);  // 리스트를 JSON 문자열로 변환
        editor.putString(DATE_GOALS_PREFIX + date, json);
        editor.apply();  // 비동기 방식으로 저장
    }

    // 날짜별 목표 리스트를 SharedPreferences에서 불러오기
    private List<Goal> loadGoalsForDate(String date) {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = prefs.getString(DATE_GOALS_PREFIX + date, null);
        Type type = new TypeToken<ArrayList<Goal>>() {}.getType();
        List<Goal> goals = gson.fromJson(json, type);
        goalList.clear();
        if (goals != null) {
            goalList.addAll(goals);
        }
        goalAdapter.notifyDataSetChanged();
        return goalList;
    }

    // AddGoalActivity에서 돌아올 때 호출
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_ADD_GOAL && resultCode == RESULT_OK) {
            Goal newGoal = (Goal) data.getSerializableExtra("newGoal");
            if (newGoal != null) {
                goalList.add(newGoal);
                goalAdapter.notifyDataSetChanged();
                saveGoalsForDate(selectedDate, goalList);  // 새로운 목표를 현재 선택된 날짜에 저장
            }
        }
    }
}
