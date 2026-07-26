# Keep WebView JavaScript interface
-keepclassmembers class com.tiankaishuo.daily.MainActivity$JsBridge {
    public *;
}

# Keep task data classes
-keep class com.tiankaishuo.daily.TaskData { *; }
-keep class com.tiankaishuo.daily.TaskData$* { *; }

# Keep notification receivers
-keep class com.tiankaishuo.daily.** { *; }

# WebView
-keep class android.webkit.** { *; }
-keep class * implements android.webkit.WebViewClient { *; }
-keep class * implements android.webkit.WebChromeClient { *; }
