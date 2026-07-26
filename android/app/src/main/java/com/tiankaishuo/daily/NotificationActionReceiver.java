package com.tiankaishuo.daily;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

/**
 * 通知栏按钮接收器
 * - ✅已完成：标记任务完成 + 取消通知
 * - ⏰稍后：30分钟后重新触发
 */
public class NotificationActionReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        int taskIndex = intent.getIntExtra("task_index", 0);
        int realIndex = taskIndex - 20000;

        SharedPreferences prefs = context.getSharedPreferences(MainApplication.PREFS, Context.MODE_PRIVATE);
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if ("com.tiankaishuo.daily.ACTION_DONE".equals(action)) {
            // 标记完成
            prefs.edit().putBoolean("task_" + taskIndex, true).apply();
            // 取消通知
            if (nm != null) nm.cancel(50000 + realIndex);
            // 通知HTML页面刷新
            notifyHtmlPage(context, "task_done", String.valueOf(taskIndex));

        } else if ("com.tiankaishuo.daily.ACTION_LATER".equals(action)) {
            // 30分钟后重新触发
            if (nm != null) nm.cancel(50000 + realIndex);
            // 用AlarmManager设置30秒后（简化：实际应30分钟）
            android.app.AlarmManager am = (android.app.AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            if (am != null) {
                Intent retry = new Intent(context, AlarmReceiver.class);
                retry.putExtra("request_code", taskIndex);
                retry.putExtra("task_name", getTaskName(realIndex));
                android.app.PendingIntent pi = android.app.PendingIntent.getBroadcast(
                        context, taskIndex + 90000, retry,
                        android.app.PendingIntent.FLAG_UPDATE_CURRENT | android.app.PendingIntent.FLAG_IMMUTABLE
                );
                long trigger = System.currentTimeMillis() + 30 * 60 * 1000; // 30分钟后
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    am.setExactAndAllowWhileIdle(android.app.AlarmManager.RTC_WAKEUP, trigger, pi);
                } else {
                    am.setExact(android.app.AlarmManager.RTC_WAKEUP, trigger, pi);
                }
            }
        }
    }

    private String getTaskName(int index) {
        String[] names = {
                "起床洗漱", "晨跑2km", "早餐+单词30", "普通话跟读",
                "上课+笔记", "午休30min", "下午上课", "专业课预习复习",
                "晚餐+社交", "英语地基", "体能训练", "转专业备战",
                "复盘+明日计划", "睡觉"
        };
        return (index >= 0 && index < names.length) ? names[index] : "任务";
    }

    /**
     * 通过本地广播通知MainActivity刷新HTML
     */
    private void notifyHtmlPage(Context ctx, String event, String data) {
        Intent i = new Intent("com.tiankaishuo.daily.LOCAL_UPDATE");
        i.putExtra("event", event);
        i.putExtra("data", data);
        androidx.localbroadcastmanager.content.LocalBroadcastManager.getInstance(ctx).sendBroadcast(i);
    }
}
