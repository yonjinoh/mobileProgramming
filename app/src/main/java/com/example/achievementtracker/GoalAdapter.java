package com.example.achievementtracker;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class GoalAdapter extends RecyclerView.Adapter<GoalAdapter.GoalViewHolder> {

    private List<Goal> goalList;

    // 생성자
    public GoalAdapter(List<Goal> goalList) {
        this.goalList = goalList;
    }

    @NonNull
    @Override
    public GoalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.goal_item, parent, false);
        return new GoalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GoalViewHolder holder, int position) {
        Goal goal = goalList.get(position);
        holder.tvGoalTitle.setText(goal.getTitle());
        holder.cbGoalCompleted.setChecked(goal.isCompleted());

        // 체크박스 상태가 변경될 때 완료 상태를 업데이트
        holder.cbGoalCompleted.setOnCheckedChangeListener((buttonView, isChecked) -> goal.setCompleted(isChecked));
    }

    @Override
    public int getItemCount() {
        return goalList.size();
    }

    // ViewHolder 클래스
    public static class GoalViewHolder extends RecyclerView.ViewHolder {
        TextView tvGoalTitle;
        CheckBox cbGoalCompleted;

        public GoalViewHolder(@NonNull View itemView) {
            super(itemView);
            tvGoalTitle = itemView.findViewById(R.id.tvGoalTitle);
            cbGoalCompleted = itemView.findViewById(R.id.cbGoalCompleted);
        }
    }
}
