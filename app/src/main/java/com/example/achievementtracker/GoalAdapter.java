package com.example.achievementtracker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class GoalAdapter extends RecyclerView.Adapter<GoalAdapter.GoalViewHolder> {
    public interface OnGoalCheckedChangeListener {
        void onGoalCheckedChanged();
    }
    private List<Goal> goalList;
    private OnItemLongClickListener onItemLongClickListener;
    private OnGoalCheckedChangeListener onGoalCheckedChangeListener;


    // 생성자
    public GoalAdapter(List<Goal> goalList, OnItemLongClickListener listener, OnGoalCheckedChangeListener checkListener) {
        this.goalList = goalList;
        this.onItemLongClickListener = listener;
        this.onGoalCheckedChangeListener = checkListener;

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
        holder.cbGoalCompleted.setOnCheckedChangeListener((buttonView, isChecked) -> {
            goal.setCompleted(isChecked);
            if (onGoalCheckedChangeListener != null) {
                onGoalCheckedChangeListener.onGoalCheckedChanged();
            }
        });
        // 길게 클릭하면 수정/삭제 옵션 호출
        holder.itemView.setOnLongClickListener(v -> {
            if (onItemLongClickListener != null) {
                // 수정/삭제 옵션 표시
                onItemLongClickListener.onLongClick(goal, position);
            }
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return goalList.size();
    }

    public interface OnItemLongClickListener {
        void onLongClick(Goal goal, int position);
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
