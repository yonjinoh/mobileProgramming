package com.example.achievementtracker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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
    private static final int REQUEST_CODE_EDIT_GOAL = 2;
    private RecyclerView recyclerViewGoals;
    private GoalAdapter goalAdapter;
    private List<Goal> goalList;
    private CalendarView calendarView;
    private TextView todayAchievement, monthAchievement;
    private static final String PREFS_NAME = "goals_prefs";
    private static final String DATE_GOALS_PREFIX = "goals_";
    private String selectedDate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 네비게이션 바 설정
        setupBottomNavigationView();

        // 목표 리스트 초기화
        goalList = new ArrayList<>();

        todayAchievement = findViewById(R.id.todayAchievement);
        monthAchievement = findViewById(R.id.monthAchievement);
        calendarView = findViewById(R.id.calendarView);
        recyclerViewGoals = findViewById(R.id.goalRecyclerView);

        // RecyclerView 설정 및 어댑터 할당
        recyclerViewGoals.setLayoutManager(new LinearLayoutManager(this));
        goalAdapter = new GoalAdapter(goalList, new GoalAdapter.OnItemLongClickListener() {
            @Override
            public void onLongClick(Goal goal, int position) {
                showEditDeleteDialog(goal, position);
            }
        }, new GoalAdapter.OnGoalCheckedChangeListener() {
            @Override
            public void onGoalCheckedChanged() {
                updateTodayAchievement();  // 일간 달성률 업데이트
                updateMonthlyAchievement(); // 월간 달성률 업데이트
            }
        });

        recyclerViewGoals.setAdapter(goalAdapter);

        FloatingActionButton fabAddGoal = findViewById(R.id.AddGoal);
        fabAddGoal.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddGoalActivity.class);
            startActivityForResult(intent, REQUEST_CODE_ADD_GOAL);
        });

        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            selectedDate = year + "/" + (month + 1) + "/" + dayOfMonth;
            Toast.makeText(MainActivity.this, "Selected date: " + selectedDate, Toast.LENGTH_SHORT).show();
            loadGoalsForDate(selectedDate);
        });

        selectedDate = getCurrentDate();
        loadGoalsForDate(selectedDate);
    }

    private void updateTodayAchievement() {
        int totalGoals = goalList.size();
        int completedGoals = 0;

        for (Goal goal : goalList) {
            if (goal.isCompleted()) { // Goal이 완료되었는지 확인
                completedGoals++;
            }
        }

        int achievementRate = (totalGoals > 0) ? (completedGoals * 100 / totalGoals) : 0;
        todayAchievement.setText(achievementRate + "%");
    }

    private void updateMonthlyAchievement() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        int totalGoals = 0;
        int completedGoals = 0;

        for (String dateKey : prefs.getAll().keySet()) {
            if (dateKey.startsWith(DATE_GOALS_PREFIX)) {
                String json = prefs.getString(dateKey, null);
                Type type = new TypeToken<ArrayList<Goal>>() {}.getType();
                List<Goal> goals = new Gson().fromJson(json, type);

                if (goals != null) {
                    totalGoals += goals.size();
                    for (Goal goal : goals) {
                        if (goal.isCompleted()) {
                            completedGoals++;
                        }
                    }
                }
            }
        }

        int achievementRate = (totalGoals > 0) ? (completedGoals * 100 / totalGoals) : 0;
        monthAchievement.setText(achievementRate + "%");
    }


    private void showEditDeleteDialog(Goal goal, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("어떤 작업을 진행하시겠습니까?")
                .setItems(new String[]{"수정", "삭제"}, (dialog, which) -> {
                    if (which == 0) {
                        // Edit: EditGoalActivity로 이동
                        Intent intent = new Intent(MainActivity.this, EditGoalActivity.class);
                        intent.putExtra("goal", goal);
                        startActivityForResult(intent, REQUEST_CODE_EDIT_GOAL);
                    } else {
                        // Delete: 리스트에서 목표 삭제
                        goalList.remove(position);
                        goalAdapter.notifyItemRemoved(position);
                        saveGoalsForDate(selectedDate, goalList);
                        Toast.makeText(this, "Goal deleted", Toast.LENGTH_SHORT).show();
                    }
                });
        builder.show();
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

    private void saveGoalsForDate(String date, List<Goal> goals) {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(goals);
        editor.putString(DATE_GOALS_PREFIX + date, json);
        editor.apply();
    }

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

        // 오늘 달성률 업데이트
        updateTodayAchievement();
        return goalList;
    }

    @Override
    protected void onResume() {
        super.onResume();
        goalAdapter.notifyDataSetChanged();

        // 월간 달성률 업데이트
        updateMonthlyAchievement();
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data == null) {
            Log.d("MainActivity", "onActivityResult: data is null");
            return;
        }

        if (requestCode == REQUEST_CODE_ADD_GOAL && resultCode == RESULT_OK) {
            Goal newGoal = (Goal) data.getSerializableExtra("newGoal");
            if (newGoal != null) {
                Log.d("MainActivity", "onActivityResult: newGoal 전달됨: " + newGoal.getTitle());
                goalList.add(newGoal); // 새로운 목표를 리스트에 추가
                goalAdapter.notifyDataSetChanged(); // RecyclerView 갱신
                saveGoalsForDate(selectedDate, goalList); // 업데이트된 목표 리스트 저장
            } else {
                Log.d("MainActivity", "onActivityResult: newGoal is null");
            }
        } else if (requestCode == REQUEST_CODE_EDIT_GOAL && resultCode == RESULT_OK) {
            Goal updatedGoal = (Goal) data.getSerializableExtra("updatedGoal");
            if (updatedGoal != null) {
                Log.d("MainActivity", "onActivityResult: updatedGoal 전달됨: " + updatedGoal.getTitle());
                int position = goalList.indexOf(updatedGoal);
                if (position != -1) {
                    goalList.set(position, updatedGoal);
                    goalAdapter.notifyDataSetChanged();
                    saveGoalsForDate(selectedDate, goalList);
                }
            } else {
                Log.d("MainActivity", "onActivityResult: updatedGoal is null");
            }
        }
    }







}
