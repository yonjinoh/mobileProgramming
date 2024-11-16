package com.example.achievementtracker;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class GoalMonthlyActivity extends MenuActivity {

    private PieChart pieChart;
    private ProgressBar healthProgress, exerciseProgress, hobbyProgress, readingProgress, studyProgress, etcProgress;
    private TextView healthProgressText, exerciseProgressText, hobbyProgressText, readingProgressText, studyProgressText, etcProgressText;
    private ToggleButton toggleStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal_monthly);

        initializeViews();

        // ToggleButton 초기 상태를 "달성"으로 설정
        toggleStatus.setChecked(true);

        Goal.loadGoalsFromFile(this);
        Log.d("GoalMonthlyActivity", "Loaded Goals in Monthly: " + Goal.goalList.size());

        if (Goal.goalList.isEmpty()) {
            Toast.makeText(this, "저장된 목표가 없습니다.", Toast.LENGTH_SHORT).show();
        } else {
            updateStatistics(); // 초기 통계 업데이트
        }

        toggleStatus.setOnCheckedChangeListener((buttonView, isChecked) -> updateStatistics());

        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        setupBottomNavigationView();
        setActiveMenuItem(R.id.navigation_statistics);
    }

    private void initializeViews() {
        pieChart = findViewById(R.id.pieChart);
        healthProgress = findViewById(R.id.healthProgress);
        exerciseProgress = findViewById(R.id.exerciseProgress);
        hobbyProgress = findViewById(R.id.hobbyProgress);
        readingProgress = findViewById(R.id.readingProgress);
        studyProgress = findViewById(R.id.studyProgress);
        etcProgress = findViewById(R.id.etcProgress);

        healthProgressText = findViewById(R.id.healthProgressText);
        exerciseProgressText = findViewById(R.id.exerciseProgressText);
        hobbyProgressText = findViewById(R.id.hobbyProgressText);
        readingProgressText = findViewById(R.id.readingProgressText);
        studyProgressText = findViewById(R.id.studyProgressText);
        etcProgressText = findViewById(R.id.etcProgressText);

        toggleStatus = findViewById(R.id.toggleStatus);
    }

    private void updateStatistics() {
        int healthTotal = 0, healthCompleted = 0;
        int readingTotal = 0, readingCompleted = 0;
        int exerciseTotal = 0, exerciseCompleted = 0;
        int studyTotal = 0, studyCompleted = 0;
        int etcTotal = 0, etcCompleted = 0;

        boolean showAchievements = toggleStatus.isChecked(); // 초기 상태는 "달성"

        for (Goal goal : Goal.goalList) {
            if ("건강".equals(goal.getCategory())) {
                healthTotal++;
                if (goal.isCompleted()) {
                    healthCompleted++;
                }
            } else if ("독서".equals(goal.getCategory())) {
                readingTotal++;
                if (goal.isCompleted()) {
                    readingCompleted++;
                }
            } else if ("운동".equals(goal.getCategory())) {
                exerciseTotal++;
                if (goal.isCompleted()) {
                    exerciseCompleted++;
                }
            } else if ("학습".equals(goal.getCategory())) {
                studyTotal++;
                if (goal.isCompleted()) {
                    studyCompleted++;
                }
            } else if ("기타".equals(goal.getCategory())) {
                etcTotal++;
                if (goal.isCompleted()) {
                    etcCompleted++;
                }
            }
        }

        int healthRate = calculateRate(showAchievements ? healthCompleted : (healthTotal - healthCompleted), healthTotal);
        int readingRate = calculateRate(showAchievements ? readingCompleted : (readingTotal - readingCompleted), readingTotal);
        int exerciseRate = calculateRate(showAchievements ? exerciseCompleted : (exerciseTotal - exerciseCompleted), exerciseTotal);
        int studyRate = calculateRate(showAchievements ? studyCompleted : (studyTotal - studyCompleted), studyTotal);
        int etcRate = calculateRate(showAchievements ? etcCompleted : (etcTotal - etcCompleted), etcTotal);

        updateProgressBar(healthProgress, healthProgressText, healthRate);
        updateProgressBar(readingProgress, readingProgressText, readingRate);
        updateProgressBar(exerciseProgress, exerciseProgressText, exerciseRate);
        updateProgressBar(studyProgress, studyProgressText, studyRate);
        updateProgressBar(etcProgress, etcProgressText, etcRate);

        updatePieChart(healthRate, readingRate, exerciseRate, studyRate, etcRate, showAchievements);

        Log.d("Statistics", (showAchievements ? "Achievements" : "Failures") +
                " - Health: " + healthRate + "%, Reading: " + readingRate + "%, Exercise: " + exerciseRate +
                "%, Study: " + studyRate + "%, Etc: " + etcRate + "%");
    }

    private int calculateRate(int value, int total) {
        return (total > 0) ? (value * 100 / total) : 0;
    }

    private void updateProgressBar(ProgressBar progressBar, TextView textView, int rate) {
        progressBar.setProgress(rate);
        textView.setText(rate + "%");
    }

    private void updatePieChart(int healthRate, int readingRate, int exerciseRate, int studyRate, int etcRate, boolean showAchievements) {
        List<PieEntry> entries = new ArrayList<>();

        if (healthRate > 0) entries.add(new PieEntry(healthRate, "건강"));
        if (readingRate > 0) entries.add(new PieEntry(readingRate, "독서"));
        if (exerciseRate > 0) entries.add(new PieEntry(exerciseRate, "운동"));
        if (studyRate > 0) entries.add(new PieEntry(studyRate, "학습"));
        if (etcRate > 0) entries.add(new PieEntry(etcRate, "기타"));

        PieDataSet dataSet = new PieDataSet(entries, showAchievements ? "달성률" : "미달성률");

        // 보라색 계열 색상 지정
        List<Integer> purpleColors = new ArrayList<>();
        purpleColors.add(Color.rgb(106, 90, 205)); // Medium Purple
        purpleColors.add(Color.rgb(147, 112, 219)); // Dark Orchid
        purpleColors.add(Color.rgb(186, 85, 211)); // Medium Orchid
        purpleColors.add(Color.rgb(128, 0, 128)); // Purple
        purpleColors.add(Color.rgb(138, 43, 226)); // Blue Violet

        dataSet.setColors(purpleColors);

        // Pie Chart 텍스트 및 스타일 설정
        dataSet.setValueTextSize(14f);
        dataSet.setValueTextColor(Color.WHITE); // 값 텍스트를 흰색으로
        dataSet.setSliceSpace(3f); // 각 슬라이스 간격 추가

        PieData pieData = new PieData(dataSet);

        pieChart.setData(pieData);
        pieChart.setUsePercentValues(true); // 퍼센트 값 사용
        pieChart.getDescription().setEnabled(false); // 기본 설명 비활성화
        pieChart.setDrawHoleEnabled(true); // 중앙 홀 그리기
        pieChart.setHoleColor(Color.TRANSPARENT); // 중앙 홀 배경 투명
        pieChart.setHoleRadius(40f); // 중앙 홀 크기
        pieChart.setTransparentCircleRadius(45f); // 투명한 외곽 크기
        pieChart.setEntryLabelTextSize(12f); // 항목 라벨 텍스트 크기 설정
        pieChart.setEntryLabelColor(Color.WHITE); // 항목 라벨 텍스트 색상 설정

        pieChart.invalidate(); // 차트 새로고침
    }


    @Override
    protected void onResume() {
        super.onResume();
        Goal.loadGoalsFromFile(this); // Reload data
        updateStatistics(); // Update statistics
        Log.d("GoalMonthlyActivity", "onResume: Data reloaded and statistics updated");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Goal.saveGoalsToFile(this); // Save data
        Log.d("GoalMonthlyActivity", "onPause: Data saved");
    }
}
