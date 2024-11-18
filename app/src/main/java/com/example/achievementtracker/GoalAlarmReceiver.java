package com.example.achievementtracker;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

public class GoalAlarmReceiver extends BroadcastReceiver {

    private static final String CHANNEL_ID = "GoalAlarmChannel";
    private static final String CHANNEL_NAME = "Daily Goal Notifications";
    private static final String CHANNEL_DESCRIPTION = "Notifications for daily goals";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("GoalAlarmReceiver", "onReceive() 호출됨");
        if (intent == null) {
            Log.e("GoalAlarmReceiver", "수신된 인텐트가 null입니다.");
            return;
        }

        Log.d("GoalAlarmReceiver", "수신된 인텐트 내용: " + intent.toString());

        // 알림 채널 생성
        createNotificationChannel(context);

        // 알림 클릭 시 실행할 인텐트
        Intent notificationIntent = new Intent(context, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        // PendingIntent 설정
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                0,
                notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        // 알림 생성
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)                // 알림 아이콘 설정
                .setContentTitle("목표 알림") // 알림 제목
                .setContentText("오늘의 목표를 확인하세요!") // 알림 내용
                .setPriority(NotificationCompat.PRIORITY_HIGH) // 알림 우선순위 설정
                .setAutoCancel(true) // 알림 클릭 시 자동 제거
                .setContentIntent(pendingIntent); // 클릭 시 실행될 인텐트 설정

        // 알림 전송
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.notify(1, builder.build());
            Log.d("GoalAlarmReceiver", "알림이 성공적으로 생성되었습니다.");
        } else {
            Log.e("GoalAlarmReceiver", "NotificationManager를 가져올 수 없습니다.");
        }
    }

    private void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            if (notificationManager != null && notificationManager.getNotificationChannel(CHANNEL_ID) == null) {
                NotificationChannel notificationChannel = new NotificationChannel(
                        CHANNEL_ID,
                        CHANNEL_NAME,
                        NotificationManager.IMPORTANCE_HIGH
                );
                notificationChannel.setDescription(CHANNEL_DESCRIPTION);
                notificationManager.createNotificationChannel(notificationChannel);

                Log.d("GoalAlarmReceiver", "알림 채널이 생성되었습니다: " + CHANNEL_ID);
            } else {
                Log.d("GoalAlarmReceiver", "알림 채널이 이미 존재합니다: " + CHANNEL_ID);
            }
        }
    }
}
