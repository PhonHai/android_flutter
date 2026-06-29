package com.pomodoro.legacy.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.pomodoro.legacy.model.PomodoroRecord;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据库帮助类 — 继承 SQLiteOpenHelper
 *
 * 【传统 Java 思维】
 * 这是 Android 最原始的数据库操作方式：
 * 1. 继承 SQLiteOpenHelper
 * 2. onCreate() 里执行 CREATE TABLE SQL
 * 3. onUpgrade() 里处理数据库升级
 * 4. 用 ContentValues 封装数据，用 rawQuery / insert / query 执行 SQL
 *
 * 对比 Jetpack 版：Room 用 @Entity + @Dao 注解，编译期自动生成这些代码
 * 对比 Flutter 版：sqflite 的 openDatabase + onCreate 回调，概念一样
 *
 * 【为什么要用时间戳(long)而不是字符串？】
 * 传统 Java 开发习惯用 long 存时间戳（System.currentTimeMillis()），
 * 比 String 更高效，排序也更快。Jetpack 版和 Flutter 版用了 String (ISO8601)，
 * 是为了展示不同的存法，实际项目中两种都常见。
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "pomodoro.db";
    private static final int DATABASE_VERSION = 1;

    // 表名和列名 — 传统写法用字符串常量
    private static final String TABLE_RECORDS = "pomodoro_records";
    private static final String COL_ID = "id";
    private static final String COL_START_TIME = "start_time";
    private static final String COL_DURATION = "duration_minutes";
    private static final String COL_END_TIME = "end_time";

    // 单例模式 — 传统 Java 写法
    private static DatabaseHelper instance;

    /**
     * 获取单例（线程安全）
     * 【传统 Java 思维】
     * SQLiteOpenHelper 推荐用单例，避免多线程问题。
     * 对比 Jetpack 版：Room 用 RoomDatabase.databaseBuilder() 也是单例。
     */
    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * 首次创建数据库时调用 — 执行建表 SQL
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_RECORDS + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_START_TIME + " INTEGER NOT NULL, " +
                COL_DURATION + " INTEGER NOT NULL, " +
                COL_END_TIME + " INTEGER NOT NULL)";
        db.execSQL(createTable);
    }

    /**
     * 数据库版本升级时调用
     * 这里简单处理：删表重建（实际项目应该做 ALTER TABLE 迁移）
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECORDS);
        onCreate(db);
    }

    /**
     * 插入一条记录
     *
     * 【传统 Java 思维】
     * 1. 创建 ContentValues（相当于一个 Map）
     * 2. 调用 db.insert() 执行插入
     * 3. 返回新记录的 id
     *
     * 对比 Jetpack 版：@Insert suspend fun insert(record: PomodoroEntity): Long
     * 对比 Flutter 版：db.insert("pomodoro_records", record.toMap())
     */
    public long insertRecord(PomodoroRecord record) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_START_TIME, record.getStartTime());
        values.put(COL_DURATION, record.getDurationMinutes());
        values.put(COL_END_TIME, record.getEndTime());
        return db.insert(TABLE_RECORDS, null, values);
    }

    /**
     * 查询所有记录（按结束时间倒序）
     *
     * 【传统 Java 思维】
     * 1. db.query() 返回 Cursor（游标）
     * 2. moveToNext() 遍历每一行
     * 3. getColumnIndex() 取列索引，再按类型 getInt/getLong 取值
     * 4. 手动 new PomodoroRecord 组装对象
     * 5. 最后 close() 释放游标
     *
     * 对比 Jetpack 版：@Query("SELECT * ... ORDER BY end_time DESC")  自动映射
     * 对比 Flutter 版：db.query() 返回 List<Map>，map() 转为对象
     */
    public List<PomodoroRecord> getAllRecords() {
        List<PomodoroRecord> records = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_RECORDS, null, null, null,
                null, null, COL_END_TIME + " DESC", "100");

        // 传统遍历游标方式
        while (cursor.moveToNext()) {
            Long id = cursor.getLong(cursor.getColumnIndex(COL_ID));
            long startTime = cursor.getLong(cursor.getColumnIndex(COL_START_TIME));
            int duration = cursor.getInt(cursor.getColumnIndex(COL_DURATION));
            long endTime = cursor.getLong(cursor.getColumnIndex(COL_END_TIME));
            records.add(new PomodoroRecord(id, startTime, duration, endTime));
        }
        cursor.close();
        return records;
    }
}
