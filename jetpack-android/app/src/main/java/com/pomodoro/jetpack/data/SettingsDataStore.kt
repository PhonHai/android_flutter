package com.pomodoro.jetpack.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * DataStore 文件级单例
 *
 * ═══════════════════════════════════════════════════════════
 * 这行代码在做什么？
 * ═══════════════════════════════════════════════════════════
 *
 *   private val Context.dataStore: DataStore<Preferences>
 *       by preferencesDataStore(name = "settings")
 *
 *   拆解：
 *   1. Context.dataStore = Context 的扩展属性，任意 Context 都能 .dataStore 访问
 *   2. by preferencesDataStore(...) = Kotlin 属性委托，背后是懒加载单例
 *      - 第一次访问时创建 DataStore 实例
 *      - 之后每次访问都返回同一个实例（单例）
 *      - 底层文件路径：/data/data/包名/files/datastore/settings.preferences_pb
 *   3. name = "settings" = 文件名，对应磁盘上的 settings.preferences_pb
 *   4. private = 只在这个文件内部可见，外部通过 SettingsDataStore 类访问
 *
 *   对比 SharedPreferences 的创建方式：
 *     val sp = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
 *   DataStore 更简洁，一行搞定，而且是类型安全的。
 */
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

/**
 * 应用设置 DataStore — 替代 SharedPreferences 的现代化方案
 *
 * ═══════════════════════════════════════════════════════════
 * 【面试题37：DataStore 对比 SharedPreferences — 5 个维度】
 * ═══════════════════════════════════════════════════════════
 *
 *   | 维度        | SharedPreferences            | DataStore                          |
 *   |------------|------------------------------|------------------------------------|
 *   | 线程安全    | ❌ 主线程读写可能 ANR          | ✅ 协程异步，suspend 函数自动挂起     |
 *   | 类型安全    | ❌ 运行时 String 强制转换      | ✅ 编译期 Key 类型检查（intKey 只能存 Int）|
 *   | 错误处理    | ❌ 无异常，失败静默             | ✅ 抛 IOException，可 try-catch      |
 *   | 数据一致性  | ❌ apply() 异步可能丢数据        | ✅ edit {} 事务性，全成功或全失败     |
 *   | 数据迁移    | ❌ 手动迁移                    | ✅ SharedPreferencesMigration 自动迁移 |
 *
 *   面试话术："DataStore 基于协程和 Flow 构建，所有读写操作都在
 *     Dispatchers.IO 上执行，不阻塞主线程。edit {} 保证事务性，
 *     用 preferencesDataStore 委托创建单例，零样板代码。"
 *
 * ═══════════════════════════════════════════════════════════
 * 【读/写模式：Flow 读 + suspend 写】
 * ═══════════════════════════════════════════════════════════
 *
 *   读：返回 Flow<T>，每次数据变化自动发射新值
 *     val flow: Flow<Int> = context.dataStore.data.map { prefs -> prefs[KEY] ?: 0 }
 *     订阅者（UI）自动收到更新，不需要手动 refresh。
 *
 *   写：suspend 函数，在 edit {} 块里修改
 *     suspend fun setXxx(value: Int) {
 *         context.dataStore.edit { prefs -> prefs[KEY] = value }
 *     }
 *     必须用 suspend 因为 edit 是 IO 操作，不能阻塞主线程。
 *
 *   "读用 Flow，写用 suspend"——这是 DataStore 最核心的设计模式。
 *
 * ═══════════════════════════════════════════════════════════
 * 【完整数据流：用户改设置 → 写磁盘 → UI 自动更新】
 * ═══════════════════════════════════════════════════════════
 *
 *   用户点击"20 秒" RadioButton
 *     → SettingsScreen: viewModel.setPomodoroDuration(20)
 *       → SettingsViewModel: viewModelScope.launch { dataStore.setPomodoroDuration(20) }
 *         → SettingsDataStore: context.dataStore.edit { prefs -> prefs[POMODORO_DURATION] = 20 }
 *           → 写入磁盘文件 settings.preferences_pb
 *             → dataStore.data Flow 自动发射新 Preferences
 *               → SettingsDataStore.pomodoroDuration Flow 的 map 收到新值 → 发射 20
 *                 → SettingsViewModel 的 stateIn() 收到 20 → StateFlow 更新
 *                   → SettingsScreen 的 collectAsState() 收到 20 → RadioButton 选中状态更新
 *
 *   全程不需要手动 notify、不需要手动 refresh，Flow 自动串联。
 *
 * ═══════════════════════════════════════════════════════════
 * 【注入链路】
 * ═══════════════════════════════════════════════════════════
 *
 *   @ApplicationContext → SettingsDataStore → SettingsViewModel
 *         ↑                     ↑                  ↑
 *   框架自动提供           @Inject constructor   @HiltViewModel
 *
 *   为什么需要 @ApplicationContext 而不是普通 Context？
 *     DataStore 是单例，生命周期和 Application 一样长。
 *     如果用 Activity 的 Context，Activity 销毁时 DataStore 被回收，
 *     下次再创建就是新实例，单例失效。
 */
@Singleton
class SettingsDataStore @Inject constructor(
    @ApplicationContext private val context: Context  // ← Application 级别的 Context，不会泄漏
) {

    // ═══════════════════════════════════════════════════════════
    // Key 定义：类型安全的键
    // ═══════════════════════════════════════════════════════════
    //
    // 对比 SharedPreferences：
    //   val KEY = "pomodoro_duration"  // 字符串，容易拼错
    //   val value = sp.getInt(KEY, 25)  // 运行时才知道类型
    //
    // DataStore：
    //   val KEY = intPreferencesKey("pomodoro_duration")  // 编译期就知道是 Int 类型
    //   val value = prefs[KEY] ?: 25  // 取出来的一定是 Int（或 null）
    //
    // 如果写 prefs[KEY] = "hello" 会编译报错，因为 KEY 是 intPreferencesKey。
    companion object {
        val POMODORO_DURATION = intPreferencesKey("pomodoro_duration")
        val SHORT_BREAK_DURATION = intPreferencesKey("short_break_duration")
        val LONG_BREAK_DURATION = intPreferencesKey("long_break_duration")
        val THEME_MODE = intPreferencesKey("theme_mode")  // 0=系统 1=浅色 2=深色
    }

    // ═══════════════════════════════════════════════════════════
    // 读：返回 Flow，自动推送变化
    // ═══════════════════════════════════════════════════════════
    //
    // 每一行拆解（以 pomodoroDuration 为例）：
    //   context.dataStore.data    → Flow<Preferences>，文件变化时自动发射
    //   .map { prefs ->           → 对每次发射的 Preferences 做转换
    //       prefs[POMODORO_DURATION] → 根据 Key 取值，返回 Int?
    //       ?: 10                  → 如果没存过（首次使用），返回默认值 10
    //   }
    //   最终类型：Flow<Int>
    //
    // 为什么要用 Flow 而不是直接返回 Int？
    //   如果直接返回 Int，用户改了设置后，UI 不知道，不会自动更新。
    //   用 Flow，用户改了设置 → 文件变化 → Flow 自动发射新值 → UI 自动更新。
    //   类比：Flow 是"订阅通知"，不是"一次性查询"。

    /** 番茄钟时长（秒），默认 10 秒（演示用） */
    val pomodoroDuration: Flow<Int> = context.dataStore.data.map { prefs ->
        prefs[POMODORO_DURATION] ?: 10
    }

    /** 短休息时长（秒），默认 5 分钟 */
    val shortBreakDuration: Flow<Int> = context.dataStore.data.map { prefs ->
        prefs[SHORT_BREAK_DURATION] ?: 5 * 60
    }

    /** 长休息时长（秒），默认 15 分钟 */
    val longBreakDuration: Flow<Int> = context.dataStore.data.map { prefs ->
        prefs[LONG_BREAK_DURATION] ?: 15 * 60
    }

    /** 主题模式，默认 0（跟随系统） */
    val themeMode: Flow<Int> = context.dataStore.data.map { prefs ->
        prefs[THEME_MODE] ?: 0
    }

    // ═══════════════════════════════════════════════════════════
    // 写：suspend 函数，在 edit {} 块里修改
    // ═══════════════════════════════════════════════════════════
    //
    // 为什么是 suspend 函数？
    //   edit {} 内部会做磁盘 IO，不能在主线程执行。
    //   suspend 让调用方决定在哪个线程执行（通常 Dispatchers.IO）。
    //
    // edit {} 是事务性的：
    //   要么全部成功写入，要么全部回滚，不会出现"写了一半"的情况。
    //
    // 对比 SharedPreferences：
    //   sp.edit().putInt("key", 1).putInt("key2", 2).apply()
    //   apply() 是异步的，但可能丢数据（进程被杀时）。
    //   DataStore 的 edit {} 是同步等待写入完成的，不会丢数据。

    suspend fun setPomodoroDuration(seconds: Int) {
        context.dataStore.edit { prefs ->
            prefs[POMODORO_DURATION] = seconds
        }
    }

    suspend fun setShortBreakDuration(seconds: Int) {
        context.dataStore.edit { prefs ->
            prefs[SHORT_BREAK_DURATION] = seconds
        }
    }

    suspend fun setLongBreakDuration(seconds: Int) {
        context.dataStore.edit { prefs ->
            prefs[LONG_BREAK_DURATION] = seconds
        }
    }

    suspend fun setThemeMode(mode: Int) {
        context.dataStore.edit { prefs ->
            prefs[THEME_MODE] = mode
        }
    }
}