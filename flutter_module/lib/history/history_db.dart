// ignore_for_file: unintended_html_in_doc_comment
import 'package:sqflite/sqflite.dart';
import 'package:path/path.dart';  // join() 函数（≈ File.separator）
import '../models/pomodoro_record.dart';

/// ═══════════════════════════════════════════════════════════
/// sqflite 数据库 DAO — 对标 Android Room
/// ═══════════════════════════════════════════════════════════
///
/// ===== Android 概念映射 =====
/// 这个文件 ≈ Room Database + DAO 的组合
///
/// | Flutter (sqflite)    | Android (Room)                          | 说明                     |
/// |----------------------|-----------------------------------------|--------------------------|
/// | `HistoryDatabase`    | `@Database(...) abstract class PomodoroDb : RoomDatabase` | 数据库单例 |
/// | `instance`           | `Room.databaseBuilder(...).build()`     | 获取数据库实例            |
/// | `_initDb()`          | `.addCallback(object : Callback() { onCreate { ... } })` | 建表回调 |
/// | `insertRecord()`     | `@Insert fun insert(record)`           | 插入操作                 |
/// | `getAllRecords()`    | `@Query("SELECT * ORDER BY ...")`      | 查询操作                 |
/// | `toMap()`            | `@Entity data class PomodoroRecord`    | 数据序列化               |
/// | `fromMap()`          | Entity 的 fromCursor                   | 数据反序列化             |
///
/// ===== 为什么 Week 1 用 sqflite 而不是 Room？ =====
/// 策略：
///   Week 1 → sqflite 在 Flutter 侧直接持久化（快速出 MVP）
///   Week 2 → 改为 Flutter → MethodChannel → Room（数据归原生管理）
/// 面试话术：
///   "Week 1 用 sqflite 验证核心流程，Week 2 迁移到 Room，
///    让数据统一由原生管理，方便原生列表和鸿蒙壳复用。"
///
/// ===== 面试话术 =====
/// "HistoryDatabase 对标 Room 的 DAO 层。
///  用 OpenDatabase 回调的 onCreate 创建表（对标 Room 的 @Entity 注解建表）。
///  API 设计类似：insert 对标 @Insert，query 对标 @Query。
///  区别是 Room 用注解 + 编译期代码生成，sqflite 用手写 SQL。"
class HistoryDatabase {
  // ===== 单例模式 =====
  // Dart 私有构造函数（_()）≈ Kotlin private constructor
  // 静态 instance 保证全局只有一个数据库实例
  static HistoryDatabase? _instance;
  static Database? _db;  // sqflite 的 Database 对象

  HistoryDatabase._();

  /// 获取单例 — ≈ Room.databaseBuilder().build()
  ///
  /// ??= 操作符：如果左侧为 null 则赋值
  /// 等价于 Kotlin: _instance ?: HistoryDatabase._().also { _instance = it }
  static HistoryDatabase get instance => _instance ??= HistoryDatabase._();

  /// 获取数据库实例（懒加载）
  ///
  /// async 函数 = Kotlin suspend function
  /// await = 等待异步操作完成
  /// ≈ Room 的 suspend fun getDatabase(): PomodoroDb
  Future<Database> get database async {
    // 已初始化则直接返回
    if (_db != null) return _db!;
    // 未初始化则创建数据库
    _db = await _initDb();
    return _db!;
  }

  /// 初始化数据库 — 创建 pomodoro.db + 建表
  ///
  /// getDatabasesPath() = 获取 App 数据库目录
  ///   Android: /data/data/com.pomodoro.app/databases/
  ///   ≈ Android context.getDatabasePath("pomodoro.db")
  ///
  /// join() = 拼接路径（自动处理 /）
  ///   ≈ Kotlin: "$dbPath/pomodoro.db"
  Future<Database> _initDb() async {
    // 获取数据库目录路径
    final dbPath = await getDatabasesPath();
    // 拼接: /data/data/.../databases/pomodoro.db
    final path = join(dbPath, 'pomodoro.db');

    // openDatabase = 打开/创建数据库
    // ≈ Room.databaseBuilder(context, PomodoroDb::class.java, "pomodoro.db")
    //    .addCallback(object : RoomDatabase.Callback() {
    //        override fun onCreate(db: SupportSQLiteDatabase) { ... }
    //    }).build()
    return openDatabase(
      path,
      version: 1,  // 数据库版本（迁移用）
      // onCreate 回调: 只在数据库首次创建时执行
      // ≈ Room Database.Callback.onCreate()
      onCreate: (db, version) {
        // 创建番茄记录表
        // 对标 Room Entity:
        //   @Entity(tableName = "pomodoro_records")
        //   data class PomodoroRecord(
        //     @PrimaryKey(autoGenerate = true) val id: Int,
        //     val startTime: String,
        //     val durationMinutes: Int,
        //     val endTime: String,
        //   )
        return db.execute('''
          CREATE TABLE pomodoro_records (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            start_time TEXT NOT NULL,
            duration_minutes INTEGER NOT NULL,
            end_time TEXT NOT NULL
          )
        ''');
      },
    );
  }

  /// 插入一条记录
  ///
  /// 对标 Room:
  ///   @Insert fun insertRecord(record: PomodoroRecord): Long
  ///
  /// record.toMap() = 把实体转为 Map<String, dynamic> (≈ ContentValues)
  /// ..remove('id')  = 去掉 id（让 SQLite 自动生成 AUTOINCREMENT）
  ///   等价于 Kotlin: record.toContentValues().apply { remove("id") }
  Future<int> insertRecord(PomodoroRecord record) async {
    final db = await database;
    return db.insert(
      'pomodoro_records',          // 表名
      record.toMap()..remove('id'), // ContentValues（去掉 id 让自增）
    );
  }

  /// 查询所有记录（最多 100 条，倒序排列）
  ///
  /// 对标 Room:
  ///   @Query("SELECT * FROM pomodoro_records ORDER BY end_time DESC LIMIT 100")
  ///   suspend fun getAllRecords(): List<PomodoroRecord>
  ///
  /// db.query() → List<Map<String, dynamic>>
  /// map()     → 把每行转为 PomodoroRecord 对象
  ///   ≈ Kotlin: maps.map { PomodoroRecord.fromCursor(it) }
  Future<List<PomodoroRecord>> getAllRecords() async {
    final db = await database;
    final maps = await db.query(
      'pomodoro_records',
      orderBy: 'end_time DESC',  // 按结束时间倒序（最新在前）
      limit: 100,                // 最多 100 条（暂无分页，MVP 够用）
    );
    // 把 List<Map> 转为 List<PomodoroRecord>
    return maps.map((map) => PomodoroRecord.fromMap(map)).toList();
  }
}
