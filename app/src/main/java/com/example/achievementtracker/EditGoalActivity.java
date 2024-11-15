package com.example.achievementtracker;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class EditGoalActivity extends MenuActivity {

    private EditText editTextGoalTitle;
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
        Button buttonSave = findViewById(R.id.buttonEditGoal);

        // 네비게이션 바 설정
        setupBottomNavigationView();

        // 전달된 Goal 객체 가져오기
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("goal")) {
            goal = (Goal) intent.getSerializableExtra("goal");
            editTextGoalTitle.setText(goal.getTitle()); // 기존 제목을 표시
        }

        // 저장 버튼 클릭 시
        buttonSave.setOnClickListener(v -> {
            // 수정된 제목으로 goal 업데이트
            String updatedTitle = editTextGoalTitle.getText().toString();
            goal.setTitle(updatedTitle);

            // 수정된 goal 객체를 결과로 반환
            Intent resultIntent = new Intent();
            resultIntent.putExtra("updatedGoal", goal);
            setResult(RESULT_OK, resultIntent);
            Log.d("EditGoalActivity", "finish() 호출");
            buttonSave.postDelayed(this::finish, 100); // 액티비티 종료
        });
    }
}
