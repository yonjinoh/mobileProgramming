package com.example.achievementtracker;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class EditGoalActivity extends MenuActivity {

    private EditText editTextGoalTitle;
    private Spinner spinnerCategory;
    private Goal goal;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_goal);

        editTextGoalTitle = findViewById(R.id.editTextGoalTitle);
        spinnerCategory = findViewById(R.id.spinnerCategory); // Spinner 추가
        Button buttonSave = findViewById(R.id.buttonEditGoal);

        // 네비게이션 바 설정
        setupBottomNavigationView();

        // Spinner에 카테고리 데이터 추가
        String[] categories = {"건강", "독서", "운동", "학습", "기타"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);

        // 전달된 Goal 객체 가져오기
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("goal")) {
            goal = (Goal) intent.getSerializableExtra("goal");
            editTextGoalTitle.setText(goal.getTitle()); // 기존 제목을 표시

            // 기존 카테고리에 맞는 Spinner 선택 설정
            int categoryPosition = adapter.getPosition(goal.getCategory());
            spinnerCategory.setSelection(categoryPosition);
        }

        // 저장 버튼 클릭 시
        buttonSave.setOnClickListener(v -> {
            // 수정된 제목 및 카테고리로 goal 업데이트
            String updatedTitle = editTextGoalTitle.getText().toString();
            String updatedCategory = spinnerCategory.getSelectedItem().toString();
            goal.setTitle(updatedTitle);
            goal.setCategory(updatedCategory);

            // 수정된 goal 객체를 결과로 반환
            Intent resultIntent = new Intent();
            resultIntent.putExtra("updatedGoal", goal);
            setResult(RESULT_OK, resultIntent);
            Log.d("EditGoalActivity", "Goal updated: Title=" + updatedTitle + ", Category=" + updatedCategory);

            buttonSave.postDelayed(this::finish, 100); // 액티비티 종료
        });
    }
}
