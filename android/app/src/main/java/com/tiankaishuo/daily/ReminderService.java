package com.tiankaishuo.daily;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.PowerManager;

/**
 * 提醒服务
 * - 注册14个时间点闹钟
 * - 每分钟轮询双保险
 * - 前台服务保活
 */
public class ReminderService extends Service {

    private Handler pollHandler;
    private Runnable pollRunnable;
    private static final int FOREGROUND_ID = 10001;

    // 14个时间点（小时+分钟）
    private static final int[][] TIMES = {
            {6, 30},   // 起床
            {6, 40},   // 晨跑
            {7, 10},   // 早餐+单词
            {7, 40},   // 普通话
            {8, 0},    // 上课
            {12, 0},   // 午休
            {13, 0},   // 下午上课
            {17, 30},  // 专业课
            {18, 30},  // 晚餐
            {19, 15},  // 英语地基
            {20, 0},   // 体能
            {20, 30},  // 转专业备战
            {21, 0},   // 复盘
            {22, 30},  // 睡觉
    };

    private static final String[] TASK_NAMES = {
            "起床洗漱", "晨跑2km", "早餐+单词30", "普通话跟读",
            "上课+笔记", "午休30min", "下午上课", "专业课预习复习",
            "晚餐+社交", "英语地基", "体能训练", "转专业备战",
            "复盘+明日计划", "睡觉"
    };

    @Override
    public void onCreate() {
        super.onCreate();

        // 注册所有闹钟
        for (int i = 0; i < TIMES.length; i++) {
            AlarmManagerCompat.setDailyAlarm(this, TIMES[i][0], TIMES[i][1], 20000 + i, TASK_NAMES[i]);
        }

        // 启动前台服务（保活）
        startForeground(FOREGROUND_ID, buildForegroundNotification());

        // 每分钟轮询双保险
        pollHandler = new Handler(Looper.getMainLooper());
        pollRunnable = new Runnable() {
            @Override
            public void run() {
                checkAndFire();
                pollHandler.postDelayed(this, 60 * 1000); // 每分钟
            }
        };
        pollHandler.post(pollRunnable);
    }

    /**
     * 前台服务通知（保持服务不被杀）
     */
    private Notification buildForegroundNotification() {
        Intent notifIntent = new Intent(this, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(
                this, 0, notifIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        Notification.Builder builder;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            builder = new Notification.Builder(this, MainApplication.CHANNEL_ALARM);
        } else {
            builder = new Notification.Builder(this);
        }

        return builder
                .setContentTitle("田凯烁打卡正在运行")
                .setContentText("后台提醒已激活，14个时间点准时通知")
                .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
                .setContentIntent(pi)
                .setOngoing(true)
                .build();
    }

    /**
     * 每分钟检查：当前时间是否匹配某个任务时间点
     * （双保险：即使AlarmManager被系统限制，轮询也能触发）
     */
    private void checkAndFire() {
        java.util.Calendar now = java.util.Calendar.getInstance();
        int h = now.get(java.util.Calendar.HOUR_OF_DAY);
        int m = now.get(java.util.Calendar.MINUTE);

        for (int i = 0; i < TIMES.length; i++) {
            if (TIMES[i][0] == h && TIMES[i][1] == m) {
                // 防止同一分钟重复触发（用秒数判断）
                int s = now.get(java.util.Calendar.SECOND);
                if (s < 5) {
                    fireNotification(TASK_NAMES[i], i);
                }
                break;
            }
        }
    }

    /**
     * 触发通知（带震动）
     */
    private void fireNotification(String taskName, int index) {
        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (nm == null) return;

        // 检查是否已完成（从SharedPreferences读取）
        android.content.SharedPreferences prefs = getSharedPreferences(MainApplication.PREFS, MODE_PRIVATE);
        boolean done = prefs.getBoolean("task_" + (20000 + index), false);
        if (done) return; // 已完成的不重复提醒

        Intent doneIntent = new Intent(this, NotificationActionReceiver.class);
        doneIntent.setAction("com.tiankaishuo.daily.ACTION_DONE");
        doneIntent.putExtra("task_index", 20000 + index);
        PendingIntent donePi = PendingIntent.getBroadcast(
                this, 30000 + index, doneIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        Intent laterIntent = new Intent(this, NotificationActionReceiver.class);
        laterIntent.setAction("com.tiankaishuo.daily.ACTION_LATER");
        laterIntent.putExtra("task_index", 20000 + index);
        PendingIntent laterPi = PendingIntent.getBroadcast(
                this, 40000 + index, laterIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        Notification.Builder builder;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            builder = new Notification.Builder(this, MainApplication.CHANNEL_REMINDER);
        } else {
            builder = new Notification.Builder(this);
        }

        Notification notif = builder
                .setContentTitle("⏰ " + taskName)
                .setContentText(getTaskDetail(index))
                .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
                .setVibrate(new long[]{0, 500, 200, 500})
                .setAutoCancel(true)
                .addAction(android.R.drawable.ic_menu_save, "✅已完成", donePi)
                .addAction(android.R.drawable.ic_menu_recent_history, "⏰稍后", laterPi)
                .build();

        nm.notify(50000 + index, notif);

        // 震动
        vibrate();
    }

    private String getTaskDetail(int index) {
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
        return index >= 0 && index < details.length ? details[index] : "";
    }

    private void vibrate() {
        android.os.Vibrator vibrator = (android.os.Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator != null) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                vibrator.vibrate(android.os.VibrationEffect.createWaveform(
                        new long[]{0, 500, 200, 500}, -1));
            } else {
                vibrator.vibrate(new long[]{0, 500, 200, 500}, -1);
            }
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY; // 被杀后自动重启
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (pollHandler != null && pollRunnable != null) {
            pollHandler.removeCallbacks(pollRunnable);
        }
        // 服务被杀后尝试重启
        Intent restart = new Intent(getApplicationContext(), ReminderService.class);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            startForegroundService(restart);
        } else {
            startService(restart);
        }
    }
}
