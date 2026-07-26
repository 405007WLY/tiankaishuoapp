package com.tiankaishuo.daily;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

/**
 * 主Activity
 * - WebView全屏加载dashboard.html
 * - JS Bridge双向通信
 * - 权限请求（通知/精确闹钟）
 * - 电池白名单引导
 */
public class MainActivity extends AppCompatActivity {

    private WebView webView;
    private SharedPreferences prefs;

    private static final int REQ_NOTIF = 1001;
    private static final int REQ_ALARM = 1002;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefs = getSharedPreferences(MainApplication.PREFS, MODE_PRIVATE);

        webView = new WebView(this);
        setContentView(webView);

        WebSettings ws = webView.getSettings();
        ws.setJavaScriptEnabled(true);
        ws.setDomStorageEnabled(true);
        ws.setAllowFileAccess(true);
        ws.setAllowContentAccess(true);
        ws.setCacheMode(WebSettings.LOAD_DEFAULT);
        ws.setMediaPlaybackRequiresUserGesture(false);
        ws.setTextZoom(100);

        // JS Bridge
        webView.addJavascriptInterface(new JsBridge(), "AndroidBridge");

        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                // 页面加载完成后注入任务数据
                injectTaskData();
            }
        });

        // 加载本地HTML
        webView.loadUrl("file:///android_asset/dashboard.html");

        // 启动后台提醒服务
        startService(new Intent(this, ReminderService.class));

        // 请求权限
        requestPermissions();

        // 电池白名单引导
        checkBatteryOptimization();
    }

    /**
     * 向HTML注入任务数据（通过JS Bridge传递）
     */
    private void injectTaskData() {
        // 通过URL hash传递初始化信号
        webView.evaluateJavascript("window.NATIVE_READY = true;", null);
    }

    /**
     * 请求通知+精确闹钟权限
     */
    private void requestPermissions() {
        // 通知权限（Android 13+）
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS}, REQ_NOTIF);
            }
        }

        // 精确闹钟（Android 12+）
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PowerManager pm = getSystemService(PowerManager.class);
            if (pm != null && !pm.isIgnoringBatteryOptimizations(getPackageName())) {
                // 提示用户
                new AlertDialog.Builder(this)
                        .setTitle("电池优化设置")
                        .setMessage("请将本应用设为「不优化」，否则后台提醒可能被系统杀掉。")
                        .setPositiveButton("去设置", (d, w) -> {
                            Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                            intent.setData(Uri.parse("package:" + getPackageName()));
                            startActivity(intent);
                        })
                        .setNegativeButton("稍后", null)
                        .show();
            }
        }
    }

    private void checkBatteryOptimization() {
        // 同上逻辑，确保后台存活
    }

    /**
     * JS Bridge：Java ↔ HTML 双向通信
     */
    public class JsBridge {
        @JavascriptInterface
        public void showToast(String msg) {
            Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
        }

        @JavascriptInterface
        public void saveData(String key, String value) {
            prefs.edit().putString(key, value).apply();
        }

        @JavascriptInterface
        public String loadData(String key) {
            return prefs.getString(key, "");
        }

        @JavascriptInterface
        public void taskChecked(String taskId, boolean checked) {
            prefs.edit().putBoolean("task_" + taskId, checked).apply();
        }

        @JavascriptInterface
        public boolean isTaskChecked(String taskId) {
            return prefs.getBoolean("task_" + taskId, false);
        }

        @JavascriptInterface
        public void setVocab(int count) {
            prefs.edit().putInt("vocab", count).apply();
        }

        @JavascriptInterface
        public int getVocab() {
            return prefs.getInt("vocab", 0);
        }

        @JavascriptInterface
        public void setMood(int mood) {
            prefs.edit().putInt("mood", mood).apply();
        }

        @JavascriptInterface
        public int getMood() {
            return prefs.getInt("mood", 5);
        }

        @JavascriptInterface
        public void logEvent(String event) {
            // 记录事件到本地
            long time = System.currentTimeMillis();
            prefs.edit().putLong("event_" + time, time).putString("event_data_" + time, event).apply();
        }
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            // 双击返回退出
            if (backPressed + 2000 > System.currentTimeMillis()) {
                super.onBackPressed();
            } else {
                Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
                backPressed = System.currentTimeMillis();
            }
        }
    }

    private long backPressed = 0;

    @Override
    protected void onPause() {
        super.onPause();
        // 通知服务保持运行
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
