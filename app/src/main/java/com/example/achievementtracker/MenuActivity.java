package com.example.achievementtracker;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MenuActivity extends AppCompatActivity {

    protected BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // 네비게이션 바 설정 메서드
    protected void setupBottomNavigationView() {
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        // 메뉴 아이템 클릭 이벤트 처리
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.navigation_home) {
                startActivity(new Intent(this, MainActivity.class));
                return true;
            } else if (item.getItemId() == R.id.navigation_statistics) {
                startActivity(new Intent(this, GoalMonthlyActivity.class));
                return true;
            } else if (item.getItemId() == R.id.navigation_diary) {
                startActivity(new Intent(this, DiaryActivity.class));
                return true;
            } else if (item.getItemId() == R.id.navigation_share) {
                startActivity(new Intent(this, GoalAlertActivity.class));
                return true;
            }
            return false;
        });
    }
}
