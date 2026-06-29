package com.pomodoro.jetpack.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.pomodoro.jetpack.model.PomodoroEntity

/**
 * Room 数据库 — 对标传统 SQLiteOpenHelper
 *
 * 【传统 Java 思维 → Jetpack 映射】
 *
 * 传统 Java 写法：
 *   public class DatabaseHelper extends SQLiteOpenHelper {
 *       public void onCreate(SQLiteDatabase db) {
 *           db.execSQL("CREATE TABLE pomodoro_records (...)");
 *       }
 *       private static DatabaseHelper instance;
 *       public static synchronized DatabaseHelper getInstance(Context ctx) { ... }
 *   }
 *
 * Jetpack Room 写法：
 *   @Database 注解 → 自动生成建表 SQL
 *   abstract fun dao() → 编译期生成实现类
 *   Room.databaseBuilder() → 创建单例
 *
 * 对比 Flutter 版（history_db.dart）：
 *   class HistoryDatabase {
 *     static Database? _db;
 *     Future<Database> get database async { ... }
 *     openDatabase(path, version: 1, onCreate: (db, v) { db.execute(SQL) })
 *   }
 *   概念一样，API 不同
 */
@Database(entities = [PomodoroEntity::class], version = 1, exportSchema = false)
abstract class PomodoroDatabase : RoomDatabase() {

    // 获取 DAO（编译期生成实现）
    abstract fun pomodoroDao(): PomodoroDao

    companion object {
        @Volatile
        private var INSTANCE: PomodoroDatabase? = null

        /**
         * 获取数据库单例
         * 双重检查锁定（DCL）保证线程安全
         *
         * 传统 Java 对应：
         *   public static synchronized DatabaseHelper getInstance(Context ctx) {
         *       if (instance == null) instance = new DatabaseHelper(ctx);
         *       return instance;
         *   }
         */
        fun getInstance(context: Context): PomodoroDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    PomodoroDatabase::class.java,
                    "pomodoro.db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}
