package com.tiankaishuo.daily;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

/**
 * 精确闹钟兼容层
 * - Android 12+ 需要 SCHEDULE_EXACT_ALARM 权限
 * - 低版本直接用 setExact
 */
public class AlarmManagerCompat {

    /**
     * 注册精确闹钟
     */
    public static void setExactAlarm(Context ctx, int hour, int minute, int requestCode, String taskName) {
        AlarmManager am = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);
        if (am == null) return;

        Intent intent = new Intent(ctx, AlarmReceiver.class);
        intent.putExtra("task_name", taskName);
        intent.putExtra("request_code", requestCode);

        PendingIntent pi = PendingIntent.getBroadcast(
                ctx, requestCode, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        // 计算触发时间
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.set(java.util.Calendar.HOUR_OF_DAY, hour);
        cal.set(java.util.Calendar.MINUTE, minute);
        cal.set(java.util.Calendar.SECOND, 0);
        cal.set(java.util.Calendar.MILLISECOND, 0);

        // 如果今天已过，推到明天
        if (cal.getTimeInMillis() <= System.currentTimeMillis()) {
            cal.add(java.util.Calendar.DAY_OF_MONTH, 1);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            // Android 12+：需要检查权限
            if (am.canScheduleExactAlarms()) {
                am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pi);
            } else {
                // 降级：用非精确闹钟
                am.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pi);
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pi);
        } else {
            am.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pi);
        }
    }

    /**
     * 取消闹钟
     */
    public static void cancelAlarm(Context ctx, int requestCode) {
        AlarmManager am = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);
        if (am == null) return;

        Intent intent = new Intent(ctx, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(
                ctx, requestCode, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
        am.cancel(pi);
    }

    /**
     * 注册每天重复的闹钟
     */
    public static void setDailyAlarm(Context ctx, int hour, int minute, int requestCode, String taskName) {
        // 第一次注册
        setExactAlarm(ctx, hour, minute, requestCode, taskName);
    }
}
