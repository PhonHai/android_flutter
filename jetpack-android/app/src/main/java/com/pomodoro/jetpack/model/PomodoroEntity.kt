package com.pomodoro.jetpack.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 番茄钟记录实体 — Room 数据库表定义
 *
 * 【传统 Java 思维 → Jetpack 映射】
 *
 * 传统 Java 写法：
 *   手写 SQLiteOpenHelper，在 onCreate 里执行 CREATE TABLE SQL
 *   手写 PomodoroRecord POJO，手写 getter/setter
 *   手写 ContentValues 封装，手写 Cursor 遍历
 *
 * Jetpack Room 写法：
 *   @Entity 注解 = 自动生成 CREATE TABLE
 *   data class = 自动生成 equals/hashCode/toString/copy
 *   @PrimaryKey(autoGenerate = true) = INTEGER PRIMARY KEY AUTOINCREMENT
 *
 * 对比 Flutter 版：
 *   sqflite 手写 SQL（和传统 Java 一样），但 model 类用 Dart class
 *   toMap() / fromMap() 对应 ContentValues / Cursor 操作
 */
@Entity(tableName = "pomodoro_records")
data class PomodoroEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,              // 自增主键，新建时传 0

    // 传统 Java: private long startTime; + getter + setter
    // Jetpack: val = 不可变，Room 自动映射为 INTEGER 列
    val startTime: Long,           // 开始时间戳（毫秒）

    val durationMinutes: Int,      // 实际完成分钟数

    val endTime: Long              // 结束时间戳（毫秒）
)
