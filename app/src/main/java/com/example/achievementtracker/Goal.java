package com.example.achievementtracker;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Goal implements Serializable {
    private String title;
    private String category;
    private boolean isCompleted;

    // 정적 리스트를 사용해 모든 Goal 객체를 저장 (DB 대신)
    public static List<Goal> goalList = new ArrayList<>();

    // 생성자
    public Goal(String title, String category, boolean isCompleted) {
        this.title = title;
        this.category = category;
        this.isCompleted = isCompleted;
    }

    // Getter 및 Setter 메서드
    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }
}
