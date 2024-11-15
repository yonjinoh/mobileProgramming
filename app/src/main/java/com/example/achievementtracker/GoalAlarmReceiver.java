package com.example.achievementtracker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.os.Build;
import androidx.core.app.NotificationCompat;

public class GoalAlarmReceiver extends BroadcastReceiver {

    private static final String CHANNEL_ID = "GoalAlarmChannel";

    @Override
    public void onReceive(Context context, Intent intent) {
        // 알림 채널 설정 (Android 8.0 이상 필요)
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID, "Goal Alarm", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        // 알림 인텐트 설정
        Intent notificationIntent = new Intent(context, MainActivity.class); // MainActivity로 이동
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // 알림 생성
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_alert) // 아이콘 설정
                .setContentTitle("Daily Goal Reminder")
                .setContentText("Check your goals for today!")
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        notificationManager.notify(1, builder.build());
    }
}
