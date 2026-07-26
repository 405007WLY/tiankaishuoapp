package com.tiankaishuo.daily;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 核心任务数据
 * 包含：14个工作日时间点、10个周末时段、4项保底、18周清单、证书、体检标准
 */
public class TaskData {

    // ========== 工作日14个时间点 ==========
    public static class TimeTask {
        public String time;
        public String title;
        public String detail;
        public boolean required;   // 必做项
        public boolean alternate;   // 隔天项
        public String tag;         // 分类标签

        public TimeTask(String time, String title, String detail, boolean required, boolean alternate, String tag) {
            this.time = time;
            this.title = title;
            this.detail = detail;
            this.required = required;
            this.alternate = alternate;
            this.tag = tag;
        }
    }

    public static List<TimeTask> getWorkdayTasks() {
        List<TimeTask> list = new ArrayList<>();
        list.add(new TimeTask("06:30", "起床洗漱", "闹钟放远处下床关冷水脸", true, false, "必做"));
        list.add(new TimeTask("06:40", "晨跑2km", "心率120-140参军体能储备", false, true, "隔天"));
        list.add(new TimeTask("07:10", "早餐+单词30", "墨墨背单词四级高频30新词", true, false, "必做"));
        list.add(new TimeTask("07:40", "普通话跟读", "单字→双字→短文录音对比", false, true, "隔天"));
        list.add(new TimeTask("08:00", "上课+笔记", "康奈尔笔记法主栏副栏总结栏", true, false, "必做"));
        list.add(new TimeTask("12:00", "午休30min", "设13:00闹钟不睡下午腰斩", true, false, "保命"));
        list.add(new TimeTask("13:00", "下午上课", "课后30分钟内复盘", true, false, "必做"));
        list.add(new TimeTask("17:30", "专业课预习复习", "占70%时间决定转专业排名", true, false, "核心"));
        list.add(new TimeTask("18:30", "晚餐+社交", "每周至少1次非功利社交", false, false, "弹性"));
        list.add(new TimeTask("19:15", "英语地基", "音标/语法/阅读/听力40-70min", true, false, "必做"));
        list.add(new TimeTask("20:00", "体能训练", "俯卧撑3×15+仰卧起坐3×20+眼保健操", false, true, "隔天"));
        list.add(new TimeTask("20:30", "转专业备战", "精读教康导论+政策笔记交替", false, true, "隔天"));
        list.add(new TimeTask("21:00", "复盘+明日计划", "勾选今日+明日3条核心计划", true, false, "必做"));
        list.add(new TimeTask("22:30", "睡觉", "手机放书桌不带上床", true, false, "保命"));
        return list;
    }

    // ========== 周末10个时段 ==========
    public static List<TimeTask> getWeekendTasks() {
        List<TimeTask> list = new ArrayList<>();
        list.add(new TimeTask("08:00", "起床+单词30", "周末不赖床单词先完成", true, false, "必做"));
        list.add(new TimeTask("08:30", "专业课深度学习", "攻难点转专业笔试", true, false, "核心"));
        list.add(new TimeTask("10:15", "英语专项1.5h", "语法阅读听力加量", true, false, "必做"));
        list.add(new TimeTask("11:30", "午餐+休息1h", "午休", false, false, "保命"));
        list.add(new TimeTask("14:00", "证书/副业/志愿", "手语NECCS教资选1-2项", false, false, "后置"));
        list.add(new TimeTask("15:30", "本周打卡勾选", "看完成率", true, false, "必做"));
        list.add(new TimeTask("16:00", "体能加量", "跑步3km/引体向上", false, false, "弹性"));
        list.add(new TimeTask("19:00", "政策研读", "每月第1周末政策+人脉", false, false, "核对"));
        list.add(new TimeTask("21:00", "睡觉准备", "22:30前睡", true, false, "保命"));
        list.add(new TimeTask("周日", "半休", "上午休息下午纯放松", true, false, "必做"));
        return list;
    }

    // ========== 保底4项（减负模式） ==========
    public static List<TimeTask> getBottom4Tasks() {
        List<TimeTask> list = new ArrayList<>();
        list.add(new TimeTask("每天", "英语：单词30+听力阅读20min", "零基础需9个月打底", true, false, "保底"));
        list.add(new TimeTask("每天", "专业课：当天作业+预习复习", "绩点是转专业入场券", true, false, "保底"));
        list.add(new TimeTask("每天", "睡眠：22:30前睡", "健康是1其他是0", true, false, "保底"));
        list.add(new TimeTask("每天", "体能微任务：10min拉伸/步行", "参军退路不可归零", true, false, "保底"));
        return list;
    }

    // ========== 18周清单 ==========
    public static class WeekPlan {
        public int week;
        public String phase;
        public String[] tasks;
        public String check;
        public boolean critical;  // 关键周（★标记）

        public WeekPlan(int week, String phase, String[] tasks, String check, boolean critical) {
            this.week = week;
            this.phase = phase;
            this.tasks = tasks;
            this.check = check;
            this.critical = critical;
        }
    }

    public static List<WeekPlan> getWeeks() {
        List<WeekPlan> list = new ArrayList<>();
        list.add(new WeekPlan(1, "入学适应", new String[]{"下载南特四份官方文件", "B站学音标4-5个/天", "社团只留1个数据社团"}, "四份文件已归档", false));
        list.add(new WeekPlan(2, "入学适应", new String[]{"音标进度过半", "高数预习启动", "第一次班会发言"}, "音标48个全会读", false));
        list.add(new WeekPlan(3, "绩点保底", new String[]{"词汇启动墨墨30词/天", "教康导论第1-3章", "高数错题本建立"}, "词汇100+", false));
        list.add(new WeekPlan(4, "绩点保底", new String[]{"词汇300+", "教康导论第4-6章", "普通话跟读隔天"}, "教康导论过半", false));
        list.add(new WeekPlan(5, "★兵役决策", new String[]{"与家长书面沟通入伍意向", "视力BMI精确测量", "近视手术咨询"}, "入伍意向书面决定", true));
        list.add(new WeekPlan(6, "绩点保底", new String[]{"词汇600+", "期中备考启动", "转专业笔试准备"}, "词汇600+期中不挂", false));
        list.add(new WeekPlan(7, "期中冲刺", new String[]{"期中考全科", "词汇800+", "NECCS报名确认"}, "期中全科合格", false));
        list.add(new WeekPlan(8, "期中冲刺", new String[]{"期中复盘", "词汇1000+", "转专业面试模拟1"}, "词汇1000+", false));
        list.add(new WeekPlan(9, "★转专业加速", new String[]{"转专业笔试准备", "词汇1200+", "教康导论精读完成"}, "笔试模拟≥80", true));
        list.add(new WeekPlan(10, "★转专业加速", new String[]{"面试模拟2次", "词汇1400+", "NECCS初赛冲刺"}, "面试话术熟练", true));
        list.add(new WeekPlan(11, "★转专业加速", new String[]{"NECCS初赛4月12日", "转专业材料提交", "词汇1500+"}, "初赛完成", true));
        list.add(new WeekPlan(12, "★转专业考核", new String[]{"转专业笔试+面试", "四级冲刺启动", "词汇1600+"}, "转专业结果公布", true));
        list.add(new WeekPlan(13, "★★★期末决战", new String[]{"期末复习全面启动", "四级模考1", "词汇1800+"}, "无挂科风险", true));
        list.add(new WeekPlan(14, "★★★期末决战", new String[]{"期末考试", "四级模考2", "词汇1900+"}, "GPA≥3.0", true));
        list.add(new WeekPlan(15, "★★★期末决战", new String[]{"四级正式考试6月", "NECCS成绩跟进", "词汇2000+"}, "四级≥425", true));
        list.add(new WeekPlan(16, "暑假规划", new String[]{"暑假小学期/实习", "参军报名决策", "近视手术预约"}, "暑假计划确定", false));
        list.add(new WeekPlan(17, "暑假", new String[]{"入伍政审准备", "康复治疗师考纲预习"}, "政审材料齐全", false));
        list.add(new WeekPlan(18, "暑假", new String[]{"大一下总结", "大二规划制定", "18周复盘"}, "18周全部验收✅", false));
        return list;
    }

    // ========== 四大应急方案 ==========
    public static class Emergency {
        public String scene;
        public String dayAction;
        public String weekAction;
        public String tip;

        public Emergency(String scene, String dayAction, String weekAction, String tip) {
            this.scene = scene;
            this.dayAction = dayAction;
            this.weekAction = weekAction;
            this.tip = tip;
        }
    }

    public static List<Emergency> getEmergencies() {
        List<Emergency> list = new ArrayList<>();
        list.add(new Emergency(
                "转专业失败",
                "当日：找辅导员要反馈+写复盘报告",
                "当周：启动B计划（辅修/跨考特教专硕/考公）",
                "话术：我理解了教康专业的真实门槛，这促使我..."));
        list.add(new Emergency(
                "四级未过",
                "当日：分析薄弱项(听力/阅读/写作)",
                "当周：切换备考策略报下一年6月场次",
                "话术：失败让我建立了完整的英语自学体系"));
        list.add(new Emergency(
                "体检淘汰",
                "当日：问清淘汰项+医学建议",
                "当周：近视手术决策/体能补足计划",
                "话术：体检让我更早认识了身体管理的意义"));
        list.add(new Emergency(
                "考编落榜",
                "当日：复盘行测申论分数",
                "当周：启动机构线/入伍线/考研线",
                "话术：公考失败让我理解了基层服务的真实需求"));
        return list;
    }

    // ========== 12项证书 ==========
    public static class Certificate {
        public String name;
        public String time;
        public String purpose;
        public int level;  // 0=必考 1=后置 2=不考

        public Certificate(String name, String time, String purpose, int level) {
            this.name = name;
            this.time = time;
            this.purpose = purpose;
            this.level = level;
        }
    }

    public static List<Certificate> getCertificates() {
        List<Certificate> list = new ArrayList<>();
        list.add(new Certificate("普通话二级甲等", "大一下", "教资/考公/特教必备", 0));
        list.add(new Certificate("英语四级≥425", "大一下6月", "毕业门槛+考研", 0));
        list.add(new Certificate("NECCS C类", "大一下4月", "竞赛加分+国奖", 0));
        list.add(new Certificate("英语六级≥425", "大二上", "考研+就业加分", 1));
        list.add(new Certificate("计算机二级", "大二上", "考公/事业编门槛", 1));
        list.add(new Certificate("教师资格证小学", "大三上", "特教编制必备", 0));
        list.add(new Certificate("康复治疗师资格", "大四应届", "医疗机构核心证⚠️需康复学专业", 1));
        list.add(new Certificate("心理咨询师中科院", "大二下", "特教+康复加分", 1));
        list.add(new Certificate("手语翻译等级", "大二全年", "南特特色⚠️非国家职业资格", 2));
        list.add(new Certificate("软考初/中级", "大二/大三", "考公信息化岗加分", 1));
        list.add(new Certificate("机动车驾驶证", "大二暑假", "参军/工作通用", 0));
        list.add(new Certificate("退役士兵证", "大二下退役", "考公/教师单列计划", 1));
        return list;
    }

    // ========== 参军体检12项 ==========
    public static class PhysicalItem {
        public String name;
        public String standard;
        public boolean reversible;  // true=可逆 false=不可逆

        public PhysicalItem(String name, String standard, boolean reversible) {
            this.name = name;
            this.standard = standard;
            this.reversible = reversible;
        }
    }

    public static List<PhysicalItem> getPhysicalItems() {
        List<PhysicalItem> list = new ArrayList<>();
        list.add(new PhysicalItem("身高", "男≥160cm", true));
        list.add(new PhysicalItem("BMI", "17.5≤BMI<30", true));
        list.add(new PhysicalItem("裸眼视力", "≥4.5，<4.8需矫正", true));
        list.add(new PhysicalItem("矫正度数", "≤600度", false));
        list.add(new PhysicalItem("血压", "90≤收缩<140/60≤舒张<90", true));
        list.add(new PhysicalItem("心率", "60-100次/分", true));
        list.add(new PhysicalItem("空腹血糖", "<7.0 mmol/L", true));
        list.add(new PhysicalItem("色觉", "无红绿色盲", false));
        list.add(new PhysicalItem("心电图", "无异常", true));
        list.add(new PhysicalItem("血检(肝肾功能)", "各项指标正常", true));
        list.add(new PhysicalItem("脊柱/扁平足", "无严重畸形", false));
        list.add(new PhysicalItem("文身/瘢痕", "裸露部位无文身", false));
        return list;
    }

    // ========== 政策核对7项 ==========
    public static String[] getPolicyItems() {
        return new String[]{
                "南京特师教务处：转专业名额/绩点门槛 [jwc.njts.edu.cn 025-89668111]",
                "康复科学学院：考核科目/报录比 [kfkx.njts.edu.cn 025-89668064]",
                "全国征兵网：兵役登记+报名 [www.gfbzb.gov.cn 截止8.10]",
                "四级报名：cet-bm.neea.edu.cn",
                "NECCS报名：saikr.com/neccs (截止3月13日)",
                "军队人才网：文职公告 [81rc.mil.cn]",
                "省公务员局：当年公考公告"
        };
    }

    // ========== 英语6阶段 ==========
    public static class EnglishStage {
        public String name;
        public String weeks;
        public String task;
        public String check;

        public EnglishStage(String name, String weeks, String task, String check) {
            this.name = name;
            this.weeks = weeks;
            this.task = task;
            this.check = check;
        }
    }

    public static List<EnglishStage> getEnglishStages() {
        List<EnglishStage> list = new ArrayList<>();
        list.add(new EnglishStage("①音标打底", "第1-2周", "B站48音标+跟读", "48个全会读"));
        list.add(new EnglishStage("②词汇启动", "第3-6周", "墨墨四级高频30/天", "累计600词"));
        list.add(new EnglishStage("③语法框架", "第7-10周", "5大句型+核心时态", "能分析句子"));
        list.add(new EnglishStage("④阅读起步", "第11-14周", "VOA慢速+四级阅读", "能读短文"));
        list.add(new EnglishStage("⑤听力突破", "第15-16周", "四级真题听力精听", "能听写句子"));
        list.add(new EnglishStage("⑥冲刺阶段", "第17-18周", "四级真题整套模考", "四级≥425"));
        return list;
    }

    // ========== 量化决策标准 ==========
    public static Map<String, String> getDecisionRules() {
        Map<String, String> map = new HashMap<>();
        map.put("考研", "GPA≥3.5 且 英语六级≥425 → 优先考研");
        map.put("考公", "行测模考≥65分 → 优先考公/事业编");
        map.put("入伍", "体检12项全部达标 → 优先入伍（学费代偿+单列岗位）");
        map.put("机构", "GPA<3.0 且 行测<55 → 优先康复机构就业");
        return map;
    }
}
