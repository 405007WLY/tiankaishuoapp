package com.tiankaishuo.daily;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

/**
 * 全局初始化
 * - 3个通知渠道
 * - SharedPreferences默认配置
 */
public class MainApplication extends Application {

    public static final String CHANNEL_REMINDER = "reminder_channel";
    public static final String CHANNEL_DONE = "done_channel";
    public static final String CHANNEL_ALARM = "alarm_channel";

    public static final String PREFS = "tiankaishuo_prefs";

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannels();
    }

    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager nm = getSystemService(NotificationManager.class);

            // 提醒渠道（带震动）
            NotificationChannel ch1 = new NotificationChannel(
                    CHANNEL_REMINDER,
                    "打卡提醒",
                    NotificationManager.IMPORTANCE_HIGH
            );
            ch1.setDescription("每日打卡任务提醒");
            ch1.enableVibration(true);
            ch1.setVibrationPattern(new long[]{0, 500, 200, 500});

            // 已完成渠道
            NotificationChannel ch2 = new NotificationChannel(
                    CHANNEL_DONE,
                    "任务完成通知",
                    NotificationManager.IMPORTANCE_LOW
            );

            // 闹钟渠道（最高优先级）
            NotificationChannel ch3 = new NotificationChannel(
                    CHANNEL_ALARM,
                    "闹钟提醒",
                    NotificationManager.IMPORTANCE_HIGH
            );
            ch3.setDescription("精确闹钟定时提醒");
            ch3.enableVibration(true);

            nm.createNotificationChannel(ch1);
            nm.createNotificationChannel(ch2);
            nm.createNotificationChannel(ch3);
        }
    }
}
