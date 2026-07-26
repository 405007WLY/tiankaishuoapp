package com.tiankaishuo.daily;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * 闹钟广播接收器
 * 在指定时间点触发通知
 */
public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        int requestCode = intent.getIntExtra("request_code", 0);
        String taskName = intent.getStringExtra("task_name");

        // 交给ReminderService处理通知
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (nm == null) return;

        // 计算任务索引
        int index = requestCode - 20000;

        // 检查是否已完成
        android.content.SharedPreferences prefs = context.getSharedPreferences(MainApplication.PREFS, Context.MODE_PRIVATE);
        boolean done = prefs.getBoolean("task_" + requestCode, false);
        if (done) return;

        // 构建通知
        Intent doneIntent = new Intent(context, NotificationActionReceiver.class);
        doneIntent.setAction("com.tiankaishuo.daily.ACTION_DONE");
        doneIntent.putExtra("task_index", requestCode);
        PendingIntent donePi = PendingIntent.getBroadcast(
                context, 30000 + index, doneIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        Intent laterIntent = new Intent(context, NotificationActionReceiver.class);
        laterIntent.setAction("com.tiankaishuo.daily.ACTION_LATER");
        laterIntent.putExtra("task_index", requestCode);
        PendingIntent laterPi = PendingIntent.getBroadcast(
                context, 40000 + index, laterIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        Notification.Builder builder;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            builder = new Notification.Builder(context, MainApplication.CHANNEL_REMINDER);
        } else {
            builder = new Notification.Builder(context);
        }

        String[] details = {
                "闹钟放远处下床关冷水脸",
                "心率120-140参军体能储备",
                "墨墨背单词四级高频30新词",
                "单字→双字→短文录音对比",
                "康奈尔笔记法主栏副栏总结栏",
                "设13:00闹钟不睡下午腰斩",
                "课后30分钟内复盘当天内容",
                "占70%时间决定转专业排名",
                "每周至少1次非功利社交",
                "音标/语法/阅读/听力40-70min",
                "俯卧撑3×15+仰卧起坐3×20",
                "精读教康导论+政策笔记交替",
                "勾选今日+写明日3条核心计划",
                "手机放书桌不带上床",
        };

        String detail = (index >= 0 && index < details.length) ? details[index] : "";

        Notification notif = builder
                .setContentTitle("⏰ " + taskName)
                .setContentText(detail)
                .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
                .setVibrate(new long[]{0, 500, 200, 500})
                .setAutoCancel(true)
                .addAction(android.R.drawable.ic_menu_save, "✅已完成", donePi)
                .addAction(android.R.drawable.ic_menu_recent_history, "⏰稍后", laterPi)
                .build();

        nm.notify(50000 + index, notif);

        // 震动
        android.os.Vibrator vibrator = (android.os.Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator != null) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                vibrator.vibrate(android.os.VibrationEffect.createWaveform(
                        new long[]{0, 500, 200, 500}, -1));
            } else {
                vibrator.vibrate(new long[]{0, 500, 200, 500}, -1);
            }
        }
    }
}
