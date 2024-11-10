package com.example.achievementtracker;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Goal implements Serializable {
    private String title;
    private String category;
    private boolean isCompleted;

    public static List<Goal> goalList = new ArrayList<>();

    // 생성자
    public Goal(String title, String category, boolean isCompleted) {
        this.title = title;
        this.category = category;
        this.isCompleted = isCompleted;
    }

    // Getter 및 Setter 메서드
    public String getTitle() { return title; }
    public String getCategory() { return category; }
    public boolean isCompleted() { return isCompleted; }
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
        String json = prefs.getString("goalList", null);
        Type type = new TypeToken<ArrayList<Goal>>() {}.getType();
        List<Goal> loadedGoals = gson.fromJson(json, type);
        if (loadedGoals != null) {
            goalList.clear();
            goalList.addAll(loadedGoals);
        }
    }
}
