# 田凯烁打卡 APP - Android 项目

## 📱 功能清单

### 系统级能力（Java · 8个类）
| 类名 | 行数 | 功能 |
|---|---|---|
| MainApplication | 63 | 全局初始化·3个通知渠道 |
| MainActivity | 214 | WebView全屏·JS Bridge·权限请求·电池白名单引导 |
| AlarmManagerCompat | 82 | Android 12+精确闹钟兼容 |
| ReminderService | 230 | ⭐ 闹钟引擎·14时间点·每分钟轮询·前台保活 |
| AlarmReceiver | 100 | 定时广播·通知构建·震动 |
| NotificationActionReceiver | 76 | ✅已完成/⏰稍后按钮处理 |
| BootReceiver | 41 | 开机自启·恢复闹钟 |
| TaskData | 269 | 全部任务数据（14+10+4+18周+12证书+12体检） |

### UI交互（HTML · 668行）
14个功能模块 · 63个勾选框 · 零乱码

## 🚀 构建方式

### 方式A：GitHub Actions（推荐·零安装）
1. 注册 GitHub.com
2. 新建仓库 `tiankaishuo-app`
3. 上传 `android/` 和 `.github/` 两个文件夹
4. Actions → Build APK → Run workflow
5. 等5-10分钟 → 下载 `app-debug.apk`

### 方式B：Android Studio（本机编译）
1. 安装 Android Studio
2. Open → 选择 `android/` 文件夹
3. 等待 Gradle 同步完成
4. Build → Build Bundle(s) / APK(s) → Build APK
5. 输出：`android/app/build/outputs/apk/debug/app-debug.apk`

### 方式C：命令行（Gradle Wrapper）
```bash
cd android
chmod +x gradlew
./gradlew assembleDebug
# 输出：app/build/outputs/apk/debug/app-debug.apk
```

## 📲 安装到手机

1. 将 APK 传到手机（微信/QQ/数据线）
2. 手机设置 → 安全 → 允许"未知来源"安装
3. 点击 APK 文件 → 安装
4. 首次打开权限请求：
   - ✅ 允许通知
   - ✅ 电池无限制（设置→电池→本应用→不优化）
   - ✅ 自启动（设置→应用管理→自启动→开启）
   - ✅ 多任务锁定（打开APP后上滑→锁定🔒）

## 🔒 隐私

- ❌ 不联网 · ❌ 不上传 · ❌ 无广告 · ❌ 无追踪
- ✅ 全部数据存手机本地（SharedPreferences + localStorage）
- ✅ 包名 `com.tiankaishuo.daily` · 版本 4.0

## 📌 政策时效声明

基于南特[2024]33号 / NECCS 2027(初赛4.12) / 2027征兵(截止8.10) / CET 2027(6月笔试) / 卫考办发〔2024〕1号 / 人社部+教育部2026退役军人教师通知 / 国家职业资格目录(2021) 等制定。以入学后**南京特师教务处(jwc.njts.edu.cn 025-89668111)、康复科学学院(kfkx.njts.edu.cn 025-89668064)、全国征兵网(www.gfbzb.gov.cn)**当年官方公告为准。**由AI辅助生成，请仔细甄别。**

## 📂 项目结构

```
tiankaishuo-app/
├── README.md
├── generate_icon.html
├── .github/workflows/build-apk.yml
└── android/
    ├── build.gradle
    ├── settings.gradle
    ├── gradle.properties
    ├── gradlew / gradlew.bat
    └── app/
        ├── build.gradle
        ├── proguard-rules.pro
        └── src/main/
            ├── AndroidManifest.xml
            ├── java/com/tiankaishuo/daily/  (8个Java类)
            ├── assets/dashboard.html            (668行·全部UI)
            └── res/values/                     (strings/colors/themes)
```

## ⚠️ 关于 gradle-wrapper.jar

本仓库不包含 `gradle-wrapper.jar`（二进制文件无法用文本创建）。
- **GitHub Actions**：云端自动处理，不需要此文件
- **Android Studio**：首次打开项目自动下载
- **手动获取**：从任意Android项目复制，或从Gradle官网下载

## 📞 关键电话速查

| 部门 | 电话 | 用途 |
|---|---|---|
| 康复科学学院 | 025-89668064 | 转专业考核科目/报录比 |
| 康复科学学院 | 025-89668061 | 同上备选 |
| 教务处 | 025-89668111 | 转专业名额/绩点门槛 |
| 教务处 | 025-89668222 | 同上备选 |

## 🌐 关键网址

| 网站 | 用途 |
|---|---|
| jwc.njts.edu.cn | 教务处·转专业政策 |
| kfkx.njts.edu.cn | 康复科学学院·专业介绍 |
| www.gfbzb.gov.cn | 全国征兵网·兵役登记 |
| cet-bm.neea.edu.cn | 四六级报名 |
| saikr.com/neccs | NECCS报名 |
| 81rc.mil.cn | 军队人才网·文职公告 |
