package com.example.achievementtracker;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class GoalDetailActivity extends MenuActivity {

    private EditText tvGoalTitle;
    private Spinner spinnerCategory;
    private Button buttonAddGoal;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal_detail);

        // View 초기화
        tvGoalTitle = findViewById(R.id.tvGoalTitle);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        buttonAddGoal = findViewById(R.id.buttonDeleteGoal);

        //네비게이션 바
        setupBottomNavigationView();

        // Spinner 설정
        setupCategorySpinner();

        // 등록 버튼 클릭 이벤트 설정
        buttonAddGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addGoal();
            }
        });
    }


    // 카테고리 스피너 설정
    private void setupCategorySpinner() {
        // 카테고리 목록을 배열로 정의
        String[] categories = {"건강", "독서", "운동", "학습", "기타"};

        // 어댑터 생성 및 설정
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);

        // 스피너 항목 선택 리스너
        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // 선택된 카테고리를 가져오기
                String selectedCategory = parent.getItemAtPosition(position).toString();
                Toast.makeText(GoalDetailActivity.this, "Selected Category: " + selectedCategory, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // 아무것도 선택되지 않은 경우
            }
        });
    }



    private void addGoal() {
        // 입력된 목표 제목과 선택한 카테고리 가져오기
        String goalTitle = tvGoalTitle.getText().toString().trim();
        String selectedCategory = spinnerCategory.getSelectedItem().toString();

        if (goalTitle.isEmpty()) {
            Toast.makeText(this, "목표 제목을 입력하세요.", Toast.LENGTH_SHORT).show();
        } else {
            // 새 Goal 객체 생성 및 정적 리스트에 추가
            Goal newGoal = new Goal(goalTitle, selectedCategory, false);
            Goal.goalList.add(newGoal);

            Toast.makeText(this, "목표가 등록되었습니다!\n제목: " + goalTitle + "\n카테고리: " + selectedCategory, Toast.LENGTH_SHORT).show();

            // 입력 필드 초기화
            tvGoalTitle.setText("");
            spinnerCategory.setSelection(0);
        }
    }



}
