package com.example.achievementtracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import java.util.ArrayList;

public class GoalMonthlyActivity extends AppCompatActivity {

    private TextView tvMonthlySummary;
    private TextView tvGoalCompletionRate;
    private BarChart barChartGoals;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal_monthly);

        // View 초기화
        tvMonthlySummary = findViewById(R.id.tvMonthlySummary);
        tvGoalCompletionRate = findViewById(R.id.tvGoalCompletionRate);
        barChartGoals = findViewById(R.id.barChartGoals);

        // 예제 데이터 생성
        setMonthlyData();

    }

    private void setMonthlyData() {
        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(1, 5)); // 예시 데이터: 1월, 5개 목표 달성
        entries.add(new BarEntry(2, 8)); // 예시 데이터: 2월, 8개 목표 달성

        BarDataSet dataSet = new BarDataSet(entries, "Monthly Goals");
        BarData data = new BarData(dataSet);
        barChartGoals.setData(data);
        barChartGoals.invalidate();

        // 텍스트 요약 설정
        tvMonthlySummary.setText("This Month's Goal Achievement");
        tvGoalCompletionRate.setText("Completion Rate: 67%");
    }
}
