package com.pomodoro.jetpack.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.pomodoro.jetpack.model.PomodoroEntity
import kotlinx.coroutines.flow.Flow

/**
 * 数据访问对象 — Room DAO
 *
 * 【传统 Java 思维 → Jetpack 映射】
 *
 * 传统 Java 写法（DatabaseHelper.java）：
 *   public long insertRecord(PomodoroRecord record) {
 *       ContentValues values = new ContentValues();
 *       values.put("start_time", record.getStartTime());
 *       db.insert("pomodoro_records", null, values);
 *   }
 *
 *   public List<PomodoroRecord> getAllRecords() {
 *       Cursor cursor = db.query("pomodoro_records", ...);
 *       while (cursor.moveToNext()) { ... 手动组装 }
 *   }
 *
 * Jetpack Room 写法：
 *   @Insert → 编译期自动生成 insert SQL + ContentValues 代码
 *   @Query → 编译期验证 SQL 语句，自动映射 Cursor → Entity
 *   Flow 返回值 → 数据变化时自动通知 UI（不用手动刷新）
 *
 * 对比 Flutter 版（history_db.dart）：
 *   Future<int> insertRecord(PomodoroRecord record) async {
 *     return db.insert("pomodoro_records", record.toMap());
 *   }
 *   Room 的 @Insert 注解 = sqflite 的 db.insert()
 *   区别：Room 编译期检查，sqflite 运行时检查
 */
@Dao
interface PomodoroDao {

    /**
     * 插入一条记录
     * suspend = 协程挂起函数（不在主线程执行，自动切到 IO 线程）
     *
     * 传统 Java 对应：DatabaseHelper.insertRecord() 在主线程同步执行
     * Flutter 对应：Future<int> insertRecord() async { await db.insert(...) }
     */
    @Insert
    suspend fun insert(record: PomodoroEntity): Long

    /**
     * 查询所有记录（按结束时间倒序，最多 100 条）
     *
     * 返回 Flow<List<PomodoroEntity>>：
     *   Flow = 冷流，数据变化时自动发射新数据
     *   UI collect 这个 Flow，数据库一变 UI 自动更新
     *
     * 传统 Java 对应：List<PomodoroRecord> getAllRecords() 同步返回
     *   缺点：数据变了不会自动刷新，要手动重新调
     * Flutter 对应：Future<List<PomodoroRecord>> getAllRecords() async
     *   也没有自动更新（除非用 Stream）
     */
    @Query("SELECT * FROM pomodoro_records ORDER BY endTime DESC LIMIT 100")
    fun getAllRecords(): Flow<List<PomodoroEntity>>
}
