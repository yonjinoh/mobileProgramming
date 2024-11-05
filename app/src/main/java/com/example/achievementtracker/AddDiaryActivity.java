package com.example.achievementtracker;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class AddDiaryActivity extends MenuActivity {

    private EditText editDiaryTitle;
    private EditText editDiaryText;
    private Button buttonSaveDiary;
    private String selectedDate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_diary);


        editDiaryTitle = findViewById(R.id.editDiaryTitle);
        editDiaryText = findViewById(R.id.editDiaryText);
        buttonSaveDiary = findViewById(R.id.buttonSaveDiary);

        // 네비게이션 바 설정
        setupBottomNavigationView();

        selectedDate = getIntent().getStringExtra("selectedDate");

        buttonSaveDiary.setOnClickListener(v -> {
            String title = editDiaryTitle.getText().toString().trim();
            String text = editDiaryText.getText().toString().trim();

            Intent resultIntent = new Intent();
            resultIntent.putExtra("diaryTitle", title);
            resultIntent.putExtra("diaryText", text);
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }
}
