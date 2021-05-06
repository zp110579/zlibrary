package com.zee.bean

/**
 *created by zee on 2021/1/16.
 *
 */


enum class DateStyle(value: String, isShowOnly: Boolean) {
    YYYY_MM("yyyy-MM", false),
    YYYY_MM_DD("yyyy-MM-dd", false),
    YYYY_MM_DD_HH_MM("yyyy-MM-dd HH:mm", false),
    YYYY_MM_DD_HH_MM_SS("yyyy-MM-dd HH:mm:ss", false),
    YYYY_MM_EN("yyyy/MM", false),
    YYYY_MM_DD_EN("yyyy/MM/dd", false),
    YYYY_MM_DD_HH_MM_EN("yyyy/MM/dd HH:mm", false),
    YYYY_MM_DD_HH_MM_SS_EN("yyyy/MM/dd HH:mm:ss", false),
    YYYY_MM_MY1("yyyyMM", false),
    YYYY_MM_DD_MY1("yyyyMMdd", false),
    YYYY_MM_DD_HH_MM_MY1("yyyyMMdd HH:mm", false),
    YYYY_MM_DD_HH_MM_SS_MY1("yyyyMMdd HH:mm:ss", false),
    YYYY_MM_DD_HH_MM_SS_MY2("yyyy.MM.dd HH:mm:ss", false),
    YYYY_MM_DD_HH_MM_SS_INT("yyyyMMdd HHmmss", false),
    YYYY_MM_DD_HH_MM_SS_INT2("yyyyMMddHHmmss", false),
    YYYY_MM_DD_HH_MM_SS_INT3("yyyy_MM_dd_HH_mm_ss", false),
    YYYY_MM_CN("yyyy年MM月", false),
    YYYY_MM_DD_CN("yyyy年MM月dd日", false),
    YYYY_MM_DD_HH_MM_CN("yyyy年MM月dd日 HH:mm", false),
    YYYY_MM_DD_HH_MM_CN1("yyyy年MM月dd日 HH点mm分", false),  //2012年12月1日 15点4分
    YYYY_MM_DD_HH_MM_CN2("yyyy年MM月dd日 HH时mm分", false),  //2012年12月1日 15点4分
    YYYY_MM_DD_HH_MM_SS_CN("yyyy年MM月dd日 HH:mm:ss", false),
    YYYY_MM_DD_HH_MM_SS_CN1("yyyy年MM月dd日 HH点mm分ss秒", false),
    YYYY_MM_DD_HH_MM_SS_CN2("yyyy年MM月dd日 HH时mm分ss秒", false),
    HH_MM("HH:mm", true),
    HH_MM_SS("HH:mm:ss", true),
    MM_DD("MM-dd", true),
    MM_DD_HH_MM("MM-dd HH:mm", true),
    MM_DD_HH_MM_SS("MM-dd HH:mm:ss", true),
    MM_DD_EN("MM/dd", true),
    MM_DD_HH_MM_EN("MM/dd HH:mm", true),
    MM_DD_HH_MM_SS_EN("MM/dd HH:mm:ss", true),
    MM_DD_CN("MM月dd日", true),
    MM_DD_HH_MM_CN("MM月dd日 HH:mm", true),
    MM_DD_HH_MM_SS_CN("MM月dd日 HH:mm:ss", true);

    private val value: String = value
    private val isShowOnly: Boolean = isShowOnly
    fun getValue(): String {
        return value
    }

    fun isShowOnly(): Boolean {
        return isShowOnly
    }

}

enum class Week(var chineseName: String, var EnglishName: String, var shortName: String, var aliasName: String, var number: Int) {
    MONDAY("星期一", "Monday", "Mon.", "周一", 1),
    TUESDAY("星期二", "Tuesday", "Tues.", "周二", 2),
    WEDNESDAY("星期三", "Wednesday", "Wed.", "周三", 3),
    THURSDAY("星期四", "Thursday", "Thur.", "周四", 4),
    FRIDAY("星期五", "Friday", "Fri.", "周五", 5),
    SATURDAY("星期六", "Saturday", "Sat.", "周六", 6),
    SUNDAY("星期日", "Sunday", "Sun.", "周日", 7);
}