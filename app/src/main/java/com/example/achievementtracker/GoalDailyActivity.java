package com.example.achievementtracker;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GoalDailyActivity extends MenuActivity {

    private PieChart pieChart;
    private ProgressBar healthProgress, exerciseProgress, hobbyProgress;
    private TextView healthProgressText, exerciseProgressText, hobbyProgressText;
    private ToggleButton toggleStatus, toggleTimePeriod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal_daily);  // 새로 생성된 daily 레이아웃 파일

        // Initialize views
        pieChart = findViewById(R.id.pieChart);
        healthProgress = findViewById(R.id.healthProgress);
        exerciseProgress = findViewById(R.id.exerciseProgress);
        hobbyProgress = findViewById(R.id.hobbyProgress);
        healthProgressText = findViewById(R.id.healthProgressText);
        exerciseProgressText = findViewById(R.id.exerciseProgressText);
        hobbyProgressText = findViewById(R.id.hobbyProgressText);

        // Initialize toggle buttons
        toggleStatus = findViewById(R.id.toggleStatus);
        toggleTimePeriod = findViewById(R.id.toggleTimePeriod);

        // Set default statistics to show achievement rate for today
        updateStatistics(true);

        // Listener for status toggle (achievement/failure)
        toggleStatus.setOnCheckedChangeListener((buttonView, isChecked) -> {
            updateStatistics(isChecked);  // Update based on achievement or failure
        });

        // Toggle 버튼의 상태에 따른 페이지 전환
        toggleTimePeriod.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (!isChecked) {  // "이달"로 설정된 경우 월간 달성률 페이지로 이동
                Intent intent = new Intent(GoalDailyActivity.this, GoalMonthlyActivity.class);
                startActivity(intent);
            }
        });

        // 뒤로 가기 버튼 설정
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        // 하단 네비게이션바
        setupBottomNavigationView();
    }

    // Updates statistics based on achievement/failure for today
    private void updateStatistics(boolean showAchievement) {
        Map<String, Integer> categoryRate;

        if (showAchievement) {
            // Show achievement rate for today
            categoryRate = StatisticsHelper.calculateDailyAchievement(Goal.goalList);
            toggleStatus.setText("달성률 보기");
        } else {
            // Show failure rate for today
            categoryRate = StatisticsHelper.calculateDailyFailure(Goal.goalList);
            toggleStatus.setText("미달성률 보기");
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
