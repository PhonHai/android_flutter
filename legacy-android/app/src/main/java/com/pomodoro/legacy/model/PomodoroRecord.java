package com.pomodoro.legacy.model;

/**
 * 番茄钟记录实体 — 纯 Java POJO
 *
 * 【传统 Java 思维】
 * 这就是一个普通的 Java Bean / POJO。
 * 对应数据库表 pomodoro_records 的一行记录。
 *
 * 对比 Jetpack 版：用 data class（自动生成 equals/hashCode/toString）
 * 对比 Flutter 版：Dart 的 class + final 字段，概念一样
 */
public class PomodoroRecord {

    private Long id;                    // 数据库自增主键，新建时为 null
    private long startTime;             // 开始时间（时间戳毫秒）
    private int durationMinutes;        // 实际完成分钟数
    private long endTime;               // 结束时间（时间戳毫秒）

    // 构造函数（新建记录用，没有 id）
    public PomodoroRecord(long startTime, int durationMinutes, long endTime) {
        this.startTime = startTime;
        this.durationMinutes = durationMinutes;
        this.endTime = endTime;
    }

    // 构造函数（从数据库读取用，有 id）
    public PomodoroRecord(Long id, long startTime, int durationMinutes, long endTime) {
        this.id = id;
        this.startTime = startTime;
        this.durationMinutes = durationMinutes;
        this.endTime = endTime;
    }

    // Getter / Setter — 传统 Java 必须手写
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public long getStartTime() { return startTime; }
    public int getDurationMinutes() { return durationMinutes; }
    public long getEndTime() { return endTime; }
}
