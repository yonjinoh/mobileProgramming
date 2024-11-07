package com.example.achievementtracker;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatisticsHelper {

    // (달성) 카테고리별 비율 계산
    public static Map<String, Integer> calculateCategoryAchievement(List<Goal> goals) {
        Map<String, Integer> categoryCompletion = new HashMap<>();
        Map<String, Integer> categoryTotal = new HashMap<>();

        // 목표 리스트를 순회하면서 카테고리별 완료된 목표와 전체 목표 개수 계산
        for (Goal goal : goals) {
            String category = goal.getCategory();
            categoryTotal.put(category, categoryTotal.getOrDefault(category, 0) + 1);
            if (goal.isCompleted()) {
                categoryCompletion.put(category, categoryCompletion.getOrDefault(category, 0) + 1);
            }
        }

        // 각 카테고리의 달성률 계산
        Map<String, Integer> categoryAchievementRate = new HashMap<>();
        for (String category : categoryTotal.keySet()) {
            int completed = categoryCompletion.getOrDefault(category, 0);
            int total = categoryTotal.get(category);
            int rate = (int) ((completed / (float) total) * 100); // 달성률 계산
            categoryAchievementRate.put(category, rate);
        }

        return categoryAchievementRate;
    }

    // 미달성률 계산 메서드
    public static Map<String, Integer> calculateCategoryFailure(List<Goal> goals) {
        Map<String, Integer> categoryIncomplete = new HashMap<>();
        Map<String, Integer> categoryTotal = new HashMap<>();

        for (Goal goal : goals) {
            String category = goal.getCategory();
            categoryTotal.put(category, categoryTotal.getOrDefault(category, 0) + 1);
            if (!goal.isCompleted()) {
                categoryIncomplete.put(category, categoryIncomplete.getOrDefault(category, 0) + 1);
            }
        }

        Map<String, Integer> categoryFailureRate = new HashMap<>();
        for (String category : categoryTotal.keySet()) {
            int incomplete = categoryIncomplete.getOrDefault(category, 0);
            int total = categoryTotal.get(category);
            int rate = (int) ((incomplete / (float) total) * 100);
            categoryFailureRate.put(category, rate);
        }

        return categoryFailureRate;
    }


    public static Map<String, Integer> calculateDailyAchievement(List<Goal> goals) {
        // 필터링 로직을 추가하여 오늘 완료된 목표들만 계산
        return calculateCategoryAchievement(goals);
    }

    public static Map<String, Integer> calculateDailyFailure(List<Goal> goals) {
        return calculateCategoryFailure(goals);
    }
}
