package com.example.achievementtracker;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GoalMonthlyActivity extends MenuActivity {

    private PieChart pieChart;
    private ProgressBar healthProgress, exerciseProgress, hobbyProgress;
    private TextView healthProgressText, exerciseProgressText, hobbyProgressText;
    private ToggleButton toggleStatus, toggleTimePeriod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal_monthly);

        // Initialize views
        pieChart = findViewById(R.id.pieChart);
        healthProgress = findViewById(R.id.healthProgress);
        exerciseProgress = findViewById(R.id.exerciseProgress);
        hobbyProgress = findViewById(R.id.hobbyProgress);
        healthProgressText = findViewById(R.id.healthProgressText);
        exerciseProgressText = findViewById(R.id.exerciseProgressText);
        hobbyProgressText = findViewById(R.id.hobbyProgressText);

        toggleStatus = findViewById(R.id.toggleStatus);
        toggleTimePeriod = findViewById(R.id.toggleTimePeriod);


        // Set default statistics to show achievement rate for the current month
        updateStatistics(true, "monthly");

        // Listener for status toggle (achievement/failure)
        toggleStatus.setOnCheckedChangeListener((buttonView, isChecked) -> {
            boolean showAchievement = isChecked; // true if "달성" is selected
            String period = toggleTimePeriod.isChecked() ? "daily" : "monthly";
            updateStatistics(showAchievement, period);
        });

        // Toggle 버튼의 상태에 따른 페이지 전환
        toggleTimePeriod.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // "오늘"로 설정된 경우 오늘 달성률 페이지로 이동
                Intent intent = new Intent(GoalMonthlyActivity.this, GoalDailyActivity.class);
                startActivity(intent);
            }
        });

        // 뒤로 가기 버튼 설정
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        // 하단 네비게이션바
        setupBottomNavigationView();
    }

    // Updates statistics based on achievement/failure and time period
    private void updateStatistics(boolean showAchievement, String period) {

        Log.d("GoalDataCheck", "Goal List Size: " + Goal.goalList.size());
        for (Goal goal : Goal.goalList) {
            Log.d("GoalDataCheck", "Title: " + goal.getTitle() + ", Category: " + goal.getCategory() + ", Completed: " + goal.isCompleted());
        }

        Map<String, Integer> categoryRate;

        if (showAchievement) {
            if (period.equals("monthly")) {
                categoryRate = StatisticsHelper.calculateCategoryAchievement(Goal.goalList);
            } else {
                categoryRate = StatisticsHelper.calculateDailyAchievement(Goal.goalList);
            }
            toggleStatus.setText("달성");
        } else {
            if (period.equals("monthly")) {
                categoryRate = StatisticsHelper.calculateCategoryFailure(Goal.goalList);
            } else {
                categoryRate = StatisticsHelper.calculateDailyFailure(Goal.goalList);
            }
            toggleStatus.setText("미달성률");
        }

        setupPieChart(categoryRate);
        updateProgressBars(categoryRate);
    }

    private void setupPieChart(Map<String, Integer> categoryAchievementRate) {
        List<PieEntry> entries = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : categoryAchievementRate.entrySet()) {
            entries.add(new PieEntry(entry.getValue(), entry.getKey()));
        }

        PieDataSet dataSet = new PieDataSet(entries, "카테고리별 달성률");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        PieData pieData = new PieData(dataSet);

        pieChart.setData(pieData);
        pieChart.invalidate(); // Refresh the chart
    }

    private void updateProgressBars(Map<String, Integer> categoryAchievementRate) {
        if (categoryAchievementRate.containsKey("건강")) {
            int healthRate = categoryAchievementRate.get("건강");
            healthProgress.setProgress(healthRate);
            healthProgressText.setText(healthRate + "%");
        }
        if (categoryAchievementRate.containsKey("운동")) {
            int exerciseRate = categoryAchievementRate.get("운동");
            exerciseProgress.setProgress(exerciseRate);
            exerciseProgressText.setText(exerciseRate + "%");
        }
        if (categoryAchievementRate.containsKey("취미")) {
            int hobbyRate = categoryAchievementRate.get("취미");
            hobbyProgress.setProgress(hobbyRate);
            hobbyProgressText.setText(hobbyRate + "%");
        }
    }
}
