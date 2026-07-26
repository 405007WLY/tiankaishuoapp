package com.tiankaishuo.daily;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * 开机自启接收器
 * 手机重启后自动恢复闹钟和后台服务
 */
public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // 启动后台提醒服务
        Intent serviceIntent = new Intent(context, ReminderService.class);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            context.startForegroundService(serviceIntent);
        } else {
            context.startService(serviceIntent);
        }

        // 重新注册所有闹钟
        for (int i = 0; i < 14; i++) {
            String[] names = {
                    "起床洗漱", "晨跑2km", "早餐+单词30", "普通话跟读",
                    "上课+笔记", "午休30min", "下午上课", "专业课预习复习",
                    "晚餐+社交", "英语地基", "体能训练", "转专业备战",
                    "复盘+明日计划", "睡觉"
            };
            int[][] times = {
                    {6,30},{6,40},{7,10},{7,40},{8,0},{12,0},{13,0},
                    {17,30},{18,30},{19,15},{20,0},{20,30},{21,0},{22,30}
            };
            AlarmManagerCompat.setExactAlarm(
                    context, times[i][0], times[i][1], 20000 + i, names[i]
            );
        }
    }
}
