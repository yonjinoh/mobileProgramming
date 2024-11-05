package com.example.achievementtracker;

public class DiaryEntry {
    private String title;
    private String text;

    public DiaryEntry(String title, String text) {
        this.title = title;
        this.text = text;
    }

    // Getter 메서드들
    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }
}
