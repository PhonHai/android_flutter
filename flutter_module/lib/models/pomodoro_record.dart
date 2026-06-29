// ignore_for_file: unintended_html_in_doc_comment

/// ═══════════════════════════════════════════════════════════
/// 番茄钟记录数据模型 — 对标 Room @Entity
/// ═══════════════════════════════════════════════════════════
///
/// ===== Android 概念映射 =====
///
/// | Flutter                 | Android (Room)                           | 说明                     |
/// |-------------------------|------------------------------------------|--------------------------|
/// | `class PomodoroRecord`  | `@Entity data class PomodoroRecord`      | 数据实体                  |
/// | `toMap()`               | `fun toContentValues(): ContentValues`   | 序列化为数据库格式         |
/// | `fromMap()`             | `fun fromCursor(cursor: Cursor)`         | 从数据库格式反序列化       |
/// | `factory` 构造函数      | `companion object { operator fun invoke() }` | 工厂方法（命名构造）      |
/// | `final` 字段            | `val` 属性                               | 不可变                    |
/// | `int? id`               | `@PrimaryKey(autoGenerate = true) val id: Int?` | 自增主键             |
/// | `DateTime`              | `java.time.Instant` / `Long (millis)`    | 时间类型                  |
/// | `String`                | `String`                                 | 字符串                   |
/// | `toIso8601String()`     | `Instant.toString()`                     | ISO8601 格式化            |
/// | `DateTime.parse()`      | `Instant.parse(str)`                     | ISO8601 解析              |
///
/// ===== 对标 Kotlin 代码 =====
/// ```kotlin
/// @Entity(tableName = "pomodoro_records")
/// data class PomodoroRecord(
///   @PrimaryKey(autoGenerate = true) val id: Int? = null,
///   @ColumnInfo(name = "start_time") val startTime: String,
///   @ColumnInfo(name = "duration_minutes") val durationMinutes: Int,
///   @ColumnInfo(name = "end_time") val endTime: String,
/// ) {
///   fun toContentValues() = ContentValues().apply {
///     put("start_time", startTime)
///     put("duration_minutes", durationMinutes)
///     put("end_time", endTime)
///   }
///
///   companion object {
///     fun fromCursor(cursor: Cursor) = PomodoroRecord(
///       id = cursor.getInt(cursor.getColumnIndex("id")),
///       startTime = cursor.getString(cursor.getColumnIndex("start_time")),
///       durationMinutes = cursor.getInt(cursor.getColumnIndex("duration_minutes")),
///       endTime = cursor.getString(cursor.getColumnIndex("end_time")),
///     )
///   }
/// }
/// ```
///
/// ===== 面试话术 =====
/// "PomodoroRecord 对标 Room 的 @Entity 数据类。
///  toMap 对应 ContentValues 序列化，
///  fromMap 对应 Cursor 反序列化。
///  字段全部用 final 保证不可变（对标 data class 的 val 属性）。
///  Dart 没有注解处理器，手动写序列化代码，
///  和 Room 编译期生成的区别是：Room 自动生成，sqflite 手写 SQL。"
class PomodoroRecord {
  // ===== 字段定义 =====
  // Dart final = Kotlin val（不可变）
  // int? = 可空整数（≈ Int?）
  // DateTime = Dart 内置时间类型（≈ java.time.Instant）
  final int? id;                    // 数据库自增主键（新建时可为 null）
  final DateTime startTime;         // 开始时间
  final int durationMinutes;        // 实际完成的番茄时长（分钟）
  final DateTime endTime;           // 结束时间

  /// 构造函数
  ///
  /// required = 必传参数（不传编译报错）
  /// this.id  = 可选参数，新建记录时不传（数据库自动生成）
  PomodoroRecord({
    this.id,
    required this.startTime,
    required this.durationMinutes,
    required this.endTime,
  });

  /// 序列化为数据库格式
  ///
  /// Map<String, dynamic> = Dart 的字典（≈ Kotlin Map<String, Any>）
  /// dynamic = 动态类型（≈ Kotlin Any, 运行时确定类型）
  /// => 语法 = Dart 单行函数体（≈ Kotlin = 赋值表达式）
  ///
  /// 对标 Kotlin:
  /// ```kotlin
  /// fun toContentValues() = ContentValues().apply {
  ///   id?.let { put("id", it) }
  ///   put("start_time", startTime.toString())  // ISO8601
  ///   put("duration_minutes", durationMinutes)
  ///   put("end_time", endTime.toString())
  /// }
  /// ```
  Map<String, dynamic> toMap() => {
        'id': id,                                         // id 可为 null
        'start_time': startTime.toIso8601String(),         // DateTime → ISO8601 字符串
        'duration_minutes': durationMinutes,
        'end_time': endTime.toIso8601String(),
      };

  /// 从数据库格式反序列化
  ///
  /// factory 构造函数 = 工厂方法（可以不返回新实例，不一定叫 PomodoroRecord）
  /// 对标 Kotlin companion object 里的工厂函数
  ///
  /// DateTime.parse(str) = 把 ISO8601 字符串转回 DateTime
  /// map['key'] as int?    = 类型转换（≈ Kotlin map["key"] as? Int）
  factory PomodoroRecord.fromMap(Map<String, dynamic> map) => PomodoroRecord(
        id: map['id'] as int?,
        startTime: DateTime.parse(map['start_time'] as String),
        durationMinutes: map['duration_minutes'] as int,
        endTime: DateTime.parse(map['end_time'] as String),
      );

  /// 用于调试输出
  ///
  /// 对标 Kotlin data class 自动生成的 toString()
  @override
  String toString() =>
      'Pomodoro(${startTime.hour}:${startTime.minute}, ${durationMinutes}min)';
}
