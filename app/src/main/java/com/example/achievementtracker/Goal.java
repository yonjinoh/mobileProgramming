package com.example.achievementtracker;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Goal implements Serializable {
    private static int nextId = 1;
    private String title;
    private String category;
    private boolean isCompleted;
    private int id;


    public static List<Goal> goalList = new ArrayList<>();

    // 생성자
    public Goal(String title, String category, boolean isCompleted) {
        this.title = title;
        this.category = category;
        this.isCompleted = isCompleted;
        this.id = nextId++;
    }

    // Getter 및 Setter 메서드
    public String getTitle() { return title; }
    public String getCategory() { return category; }
    public boolean isCompleted() { return isCompleted; }
    public int getId() {return id;}
    public void setTitle(String title) { this.title = title; }
    public void setCategory(String category) { this.category = category; }
    public void setCompleted(boolean completed) { isCompleted = completed; }

    // Goals를 파일에 저장하는 메서드
    public static void saveGoalsToFile(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("goals_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(goalList);
        editor.putString("goalList", json);
        editor.apply();
    }

    // Goals를 파일에서 불러오는 메서드
    public static void loadGoalsFromFile(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("goals_prefs", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        goalList.clear();

        for (String key : prefs.getAll().keySet()) {
            Log.d("Goal", "SharedPreferences Key: " + key); // 저장된 키 확인
            if (key.startsWith("goals_")) {
                String json = prefs.getString(key, null);
                Type type = new TypeToken<ArrayList<Goal>>() {}.getType();
                List<Goal> loadedGoals = gson.fromJson(json, type);
                if (loadedGoals != null) {
                    goalList.addAll(loadedGoals);
                    Log.d("Goal", "로드된 목표 개수: " + loadedGoals.size());
                }
            }
        }

        Log.d("Goal", "최종 로드된 목표 총 개수: " + goalList.size());
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Goal goal = (Goal) obj;
        return this.id == goal.id; // 각 Goal 객체에 고유 id가 있다면 이를 비교 기준으로 사용
    }

    @Override
    public int hashCode() {
        return Objects.hash(id); // 고유 id를 사용하여 hashCode 생성
    }

}
