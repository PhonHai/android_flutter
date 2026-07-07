# Android Jetpack 面试宝典（Android开发必备）

> **面向**：
> - 有 Java 传统 Android 开发经验、正在学习 Jetpack 的工程师
> - Android 初中级 → 中高级岗位面试
> - 想理解"为什么这么设计"而非"怎么用"的开发者

---

# 一、Jetpack 到底是什么？

**Jetpack 不是一个框架。** 它是 Google 官方推出的一套 Android 开发组件集合，目的是解决传统 Android 开发的几大痛点：

```
传统痛点:                     Jetpack 方案:
───────────────────────────  ───────────────────────────
生命周期管理麻烦，容易泄漏    → Lifecycle 自动感知
Activity 旋转数据丢失         → ViewModel 保留数据
回调地狱（callback hell）     → LiveData / Flow 响应式
Fragment 跳转混乱             → Navigation 统一管理
SQLite 手写 SQL 容易错        → Room 自动生成
SharedPreferences 卡 UI 线程  → DataStore 异步 + Flow
后台任务被系统杀              → WorkManager 保证执行
```

### 官方推荐架构

```
┌─────────────────────────────────────┐
│  UI Layer (Activity / Fragment)     │ ← 只负责显示、事件
├─────────────────────────────────────┤
│  ViewModel                          │ ← 持有 UI 状态、业务逻辑
├─────────────────────────────────────┤
│  Repository                         │ ← 统一数据源（缓存策略）
├──────────────────┬──────────────────┤
│  Remote (网络)   │  Local (本地)    │
│  Retrofit / Ktor │  Room / DataStore│
└──────────────────┴──────────────────┘
```

> **Java 视角**：以前 MVC 中 Activity 既管 UI 又管数据，Jetpack 把每层拆开，各管各的。

---

# 二、Jetpack 最重要的组件（★★★★★）

按企业使用频率排序：

**★★★★★ 必会**
- Lifecycle
- ViewModel
- LiveData / StateFlow
- Navigation
- Room
- DataStore
- WorkManager
- Paging3

**★★★★ 高频**
- SavedStateHandle
- ViewBinding
- Coroutine（Jetpack 大量依赖协程）
- Flow

**★★★ 常用**
- Hilt（Dagger 简化版）
- CameraX
- Benchmark

**★ 新兴**
- Compose（很多公司仍以 View 体系为主）

---

# 三、Lifecycle（★★★★★）

## 传统 Java 的问题

```java
// Java 传统：需要手动管理生命周期
public class MyLocationActivity extends Activity {
    private LocationManager locationManager;
    private LocationListener locationListener;

    @Override
    protected void onStart() {
        super.onStart();
        // 1. 每次 onStart 都要注册
        locationManager.requestLocationUpdates(...);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // 2. 每次 onStop 都要注销（忘了就泄漏）
        locationManager.removeUpdates(locationListener);
    }

    // 3. 如果还有 Handler / 网络请求 / 蓝牙
    //    每个地方都要重复写 onCreate/onStart/onStop/onDestroy
    //    十处代码里漏一处 = 内存泄漏
}
```

**问题**：生命周期相关代码散落在各处，容易漏写某个回调，导致内存泄漏或崩溃。

## Lifecycle 解决思路

```kotlin
// Jetpack Lifecycle：声明式绑定，组件自己管自己
class MyLocationComponent(private val lifecycle: Lifecycle) : LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun start() {
        // 组件自动感知 onStart，不需要 Activity 手动调用
        startLocationUpdates()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun stop() {
        // 组件自动感知 onStop，不会忘记注销
        stopLocationUpdates()
    }
}

// Activity 里只需要一句
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(MyLocationComponent(lifecycle))
    }
    // 不需要写 onStart/onStop 了！组件自己监听
}
```

## 核心类

| 类 | 角色 | Java 对标 |
|-----|------|---------|
| `Lifecycle` | 持有生命周期状态（CREATED/STARTED/RESUMED/DESTROYED） | 无直接对标 |
| `LifecycleOwner` | 提供 Lifecycle 的对象（Activity/Fragment 默认实现） | 无 |
| `LifecycleObserver` | 监听生命周期变化的接口 | 无，以前靠手动 override |
| `DefaultLifecycleObserver` | Java 8+ 推荐方式（替代 @OnLifecycleEvent 注解） | 无 |

### 推荐写法：DefaultLifecycleObserver（替代 @OnLifecycleEvent）

```kotlin
// @OnLifecycleEvent 有反射性能开销，推荐用 DefaultLifecycleObserver
class MyCameraComponent : DefaultLifecycleObserver {
    override fun onStart(owner: LifecycleOwner) {
        // 启动相机预览
    }
    override fun onStop(owner: LifecycleOwner) {
        // 停止相机预览
    }
}

// 使用
lifecycle.addObserver(MyCameraComponent())
```

## 面试高频

> **Q: Lifecycle 如何避免内存泄漏？**
>
> A: Activity/Fragment 销毁时，Lifecycle 状态变为 DESTROYED，LifecycleObserver 通过 `DefaultLifecycleObserver.onDestroy()` 收到通知 → 自动取消注册/释放资源。**不需要开发者手动管理**。

> **Q: 除了 Activity/Fragment，还有什么实现了 LifecycleOwner？**
>
> 答：`ProcessLifecycleOwner`（监听整个 App 前后台）、自定义 `LifecycleRegistry`（手动管理）。

---

# 四、ViewModel（★★★★★）

## 为什么需要 ViewModel？

```java
// Java 传统：Activity 旋转后数据丢失
public class TimerActivity extends Activity {
    private int seconds = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 旋转屏幕后 seconds 变成 0，因为 Activity 重建了
        // 只能用 onSaveInstanceState + onRestoreInstanceState 手动保存
        if (savedInstanceState != null) {
            seconds = savedInstanceState.getInt("seconds");
        }
    }
}
```

```kotlin
// Jetpack ViewModel：旋转不丢失数据
class TimerViewModel : ViewModel() {
    var seconds = 0
        private set

    fun tick() {
        seconds++
    }
    // onCleared() 只在 Activity 真正 finish 时调用，旋转不触发
}

class TimerActivity : AppCompatActivity() {
    private val viewModel: TimerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 旋转屏幕后，viewModel.seconds 仍然是之前的值
    }
}
```

## 原理

```
旋转屏幕时：
  Activity 旧实例 → onDestroy（不是 finish）
  ViewModelStore 保留 ViewModel（存在 NonConfigurationInstances 里）
  Activity 新实例 → 从 ViewModelStore 取同一个 ViewModel

Activity finish 时：
  ViewModelStore 清空 → 调用 ViewModel.onCleared()
```

> **Java 视角**：以前 onRetainNonConfigurationInstance + getLastNonConfigurationInstance 手动保存数据，ViewModel 封装了这套机制。

## ViewModel 不能持有 View 引用

```kotlin
// ❌ 错误：ViewModel 持有 Activity 引用 → 内存泄漏
class BadViewModel : ViewModel() {
    var activity: Activity? = null  // Activity 已销毁，ViewModel 还引用着
}

// ✅ 正确：ViewModel 只持有 StateFlow/LiveData，用 observe 发到 UI
class GoodViewModel : ViewModel() {
    private val _result = MutableLiveData<String>()
    val result: LiveData<String> = _result  // 对外只读
}
```

## ViewModelFactory：带参数构造

```kotlin
class UserViewModel(
    private val userId: String,
    private val repo: UserRepository
) : ViewModel() {

    // 需要自定义 Factory 才能传参
    class Factory(
        private val userId: String,
        private val repo: UserRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return UserViewModel(userId, repo) as T
        }
    }
}

// Activity 里用
val viewModel: UserViewModel by viewModels {
    UserViewModel.Factory("123", UserRepository())
}
```

## viewModelScope：协程安全

```kotlin
class DataViewModel : ViewModel() {
    // viewModelScope 是 ViewModel 自带的 CoroutineScope
    // ViewModel 销毁时自动取消所有协程，不需要手动管理
    fun loadData() {
        viewModelScope.launch {
            val result = repository.fetchData()  // 网络请求
            _data.value = result
        }
    }
}
```

## 面试高频

> **Q: ViewModel 为什么不会因为旋转而销毁？**
>
> A: Activity 旋转时会先调用 `onDestroy()`（不是 finish），Activity 的 `ViewModelStore` 保存在 `NonConfigurationInstances` 中，新 Activity 从同一个 `ViewModelStore` 中获取 ViewModel 实例。只有 Activity 真正 finish 时，`ViewModelStore` 才会清空，触发 `onCleared()`。

> **Q: 为什么不能在 ViewModel 中保存 Activity 引用？**
>
> A: ViewModel 的生命周期长于 Activity — 旋转后 Activity 重建但 ViewModel 不变。如果 ViewModel 持有旧的 Activity 引用，会导致该 Activity 无法被 GC，造成**内存泄漏**。ViewModel 的设计原则就是不持有任何 View/Activity 引用。

---

# 五、LiveData（★★★★★）

## 传统 Java 的问题

```java
// Java 传统：数据变化通知 UI
// 方式 1：Callback 接口
public interface OnDataChanged {
    void onDataChanged(String data);
}
// 问题：不感知生命周期，Activity 销毁后回调还在执行 → 崩溃

// 方式 2：EventBus
// 问题：Activity 不可见时仍然收到事件，全局广播难以追踪

// 方式 3：Handler
// 问题：Activity 销毁后 Handler 消息还在队列里 → 内存泄漏
```

## LiveData 解决

```kotlin
// LiveData = 生命周期感知的可观察数据容器
class MyViewModel : ViewModel() {
    // 内部可变，对外只读
    private val _userName = MutableLiveData<String>()
    val userName: LiveData<String> = _userName

    fun loadUser() {
        _userName.value = "张三"       // 主线程更新
        // 子线程用 _userName.postValue("张三")
    }
}

class MyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val viewModel: MyViewModel by viewModels()

        // observe() 自动感知生命周期
        // Activity 可见 → 接收更新；不可见 → 暂停接收；销毁 → 自动取消
        viewModel.userName.observe(this) { name ->
            textView.text = name  // 只在 Activity 可见时更新 UI
        }
    }
}
```

## setValue vs postValue

```kotlin
// setValue：必须在主线程调用
viewModel.userName.value = "李四"

// postValue：在子线程调用，切回主线程更新
viewModel.userName.postValue("李四")

// postValue 的陷阱：连续多次 postValue 只会保留最后一次
// 因为 postValue 内部只存一个 pending value
viewModel.userName.postValue("A")  // 被覆盖
viewModel.userName.postValue("B")  // 最终值
// 观察者只会收到 "B"

// 如果多个值都要收到，用 setValue + 切主线程
```

## Transformations：数据转换

```kotlin
// 场景：根据 userId 查询用户详情
class UserViewModel(private val userId: String) : ViewModel() {
    private val _userId = MutableLiveData(userId)

    // map：一对一转换
    val userName: LiveData<String> = Transformations.map(_userId) { id ->
        "用户: $id"
    }

    // switchMap：一对多（每次输入变化时重新执行函数）
    val userDetail: LiveData<User> = Transformations.switchMap(_userId) { id ->
        repository.getUser(id)  // 返回 LiveData<User>
    }
}
```

## MediatorLiveData：合并多个数据源

```kotlin
class CombinedViewModel : ViewModel() {
    private val liveData1 = MutableLiveData<String>()
    private val liveData2 = MutableLiveData<Int>()

    val combined = MediatorLiveData<String>().apply {
        addSource(liveData1) { value = combine() }  // 任一变化都触发
        addSource(liveData2) { value = combine() }
    }

    private fun combine(): String {
        val v1 = liveData1.value ?: ""
        val v2 = liveData2.value?.toString() ?: ""
        return "结果: $v1 - $v2"
    }
}
```

## 面试高频

> **Q: LiveData 为什么不会内存泄漏？**
>
> A: `LiveData.observe()` 方法接收 `LifecycleOwner`（如 Activity），LiveData 会自动绑定到该 LifecycleOwner 的 Lifecycle。当 LifecycleOwner 状态变为 **DESTROYED** 时，LiveData 自动移除观察者，因此不会发生"Activity 已销毁但仍被回调持有"的内存泄漏。

> **Q: LiveData 和 StateFlow 的区别？**
>
> A: 见 Flow 章节。

---

# 六、ViewModel + LiveData 标准写法

```kotlin
// 1. Repository：数据层
class UserRepository {
    suspend fun fetchUser(id: String): Result<User> {
        return try {
            val response = RetrofitClient.api.getUser(id)
            Result.success(response.body())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

// 2. ViewModel：业务层
class UserViewModel(
    private val repository: UserRepository = UserRepository()
) : ViewModel() {

    // Loading / Success / Error 都用 LiveData 表示
    private val _user = MutableLiveData<UserUiState>()
    val user: LiveData<UserUiState> = _user

    fun loadUser(id: String) {
        _user.value = UserUiState.Loading
        viewModelScope.launch {
            repository.fetchUser(id).fold(
                onSuccess = { _user.value = UserUiState.Success(it) },
                onFailure = { _user.value = UserUiState.Error(it.message) }
            )
        }
    }
}

// 3. Activity：UI 层，只负责显示
class UserActivity : AppCompatActivity() {
    private val viewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        viewModel.user.observe(this) { state ->
            when (state) {
                is UserUiState.Loading -> showLoading()
                is UserUiState.Success -> showUser(state.data)
                is UserUiState.Error -> showError(state.message)
            }
        }
    }
}
```

---

# 七、Navigation（★★★★★）

## 传统 Java 的方式

```java
// Java 传统 Fragment 跳转
FragmentManager fm = getSupportFragmentManager();
FragmentTransaction ft = fm.beginTransaction();
ft.replace(R.id.container, new HomeFragment());
ft.addToBackStack(null);
ft.commit();
// 传参数需要 Bundle + arguments
// 返回栈管理混乱，多个 Fragment 之间跳转耦合严重
```

## Navigation XML 版

```xml
<!-- res/navigation/nav_graph.xml -->
<navigation xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    app:startDestination="@id/homeFragment">

    <fragment
        android:id="@+id/homeFragment"
        android:name="com.example.HomeFragment">
        <action
            android:id="@+id/to_detail"
            app:destination="@id/detailFragment" />
    </fragment>

    <fragment
        android:id="@+id/detailFragment"
        android:name="com.example.DetailFragment">
        <argument
            android:name="itemId"
            app:argType="string" />
    </fragment>
</navigation>
```

```kotlin
// Activity 里
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // NavHostFragment 在 xml 里声明
        val navController = findNavController(R.id.nav_host_fragment)
    }
}

// Fragment 里跳转
class HomeFragment : Fragment() {
    fun onItemClick(itemId: String) {
        // 使用 action ID 跳转（SafeArgs 推荐）
        val action = HomeFragmentDirections.toDetail(itemId)
        findNavController().navigate(action)
    }
}
```

## SafeArgs：类型安全的参数传递

```kotlin
// 传统方式：key-value 字符串，容易拼错
val bundle = Bundle()
bundle.putString("itemId", "123")
navController.navigate(R.id.to_detail, bundle)

// SafeArgs 方式：自动生成 Directions 类
// 1. build.gradle 添加 safe-args 插件
// 2. nav_graph.xml 声明 argument
// 3. 自动生成类：
val action = HomeFragmentDirections.toDetail("123")
navController.navigate(action)

// 接收方：
// DetailFragmentArgs.fromBundle(arguments).itemId
```

## Navigation Compose 版

```kotlin
// Compose 版不需要 XML，用函数声明路由
@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(navController, startDestination = "home") {
        composable("home") {
            HomeScreen(
                onNavigateToDetail = { id ->
                    navController.navigate("detail/$id")
                }
            )
        }
        composable(
            route = "detail/{itemId}",
            arguments = listOf(navArgument("itemId") { type = NavType.StringType })
        ) { backStackEntry ->
            val itemId = backStackEntry.arguments?.getString("itemId") ?: ""
            DetailScreen(itemId)
        }
    }
}
```

> **Java 视角**：Navigation 把 `<fragment> + <action>` 的导航关系从代码解耦到 XML，类似于以前的 `<intent-filter>` 的声明式思路。但 Compose 版又回到"代码声明路由"的方式。

## 面试高频

> **Q: Navigation 相比 FragmentTransaction 有什么优势？**
>
> A: ① 可视化导航图（XML 直观展示页面关系）② 自动管理返回栈 ③ SafeArgs 保证参数类型安全 ④ 支持 DeepLink ⑤ 支持动画/过渡 ⑥ 与 Toolbar 自动集成。

---

# 八、Room（★★★★★）

## 传统 SQLite vs Room

```java
// Java 传统 SQLiteOpenHelper
public class UserDbHelper extends SQLiteOpenHelper {
    public static final String CREATE_TABLE =
        "CREATE TABLE users (" +
        "id INTEGER PRIMARY KEY," +
        "name TEXT," +
        "age INTEGER)";

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);  // SQL 拼错只能运行时发现
    }

    // 查数据：
    Cursor cursor = db.rawQuery("SELECT * FROM users WHERE id = ?", new String[]{"1"});
    String name = cursor.getString(cursor.getColumnIndex("name"));  // 列名写错编译不报错
    // 手动关 Cursor，忘关 = 内存泄漏
}
```

```kotlin
// Room：Entity + DAO + Database
@Entity(tableName = "users")
data class User(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "name") val name: String,
    val age: Int
)

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE id = :id")
    suspend fun getUser(id: Int): User?  // 编译时自动检查 SQL 语法

    @Query("SELECT * FROM users")
    fun getAllUsers(): Flow<List<User>>  // Room + Flow = 自动刷新 UI
}

@Database(entities = [User::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        // 单例，避免多次创建
        @Volatile private var INSTANCE: AppDatabase? = null
        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context, AppDatabase::class.java, "app.db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}

// 使用
val user = db.userDao().getUser(1)  // Kotlin 协程，自动切 IO 线程
```

## TypeConverter：自定义类型

```kotlin
// Room 只支持基本类型，Date 需要转换
class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? = value?.let { Date(it) }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? = date?.time
}

@Database(entities = [User::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() { ... }
```

## Migration：数据库升级

```kotlin
val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE users ADD COLUMN phone TEXT")
    }
}

Room.databaseBuilder(context, AppDatabase::class.java, "app.db")
    .addMigrations(MIGRATION_1_2)
    .build()
```

> **Java 视角**：Room ≈ 把 SQLiteOpenHelper + Cursor + ContentValues 的重复工作交给编译期注解处理器，SQL 写在 `@Query` 注解里，编译时就帮你检查对错。再也不用写 `cursor.getString(cursor.getColumnIndex("xxx"))` 了。

---

# 九、DataStore（★★★★★）

## SharedPreferences 的问题

```java
// Java 传统 SP
SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
sp.edit().putString("token", "abc").apply();
String token = sp.getString("token", "");  // 可能在 IO 线程卡 UI
```

**SP 的三大问题**：
1. `apply()` 异步但不可观察，无法知道何时写完
2. `commit()` 同步但可能卡 UI 线程
3. **跨进程不安全**（MODE_MULTI_PROCESS 已废弃）
4. 首次加载会阻塞 UI（同步读文件）

## DataStore Preferences

```kotlin
// DataStore：完全异步，基于 Flow
val Context.dataStore by preferencesDataStore(name = "settings")

// 写
suspend fun saveToken(token: String, context: Context) {
    context.dataStore.edit { preferences ->
        preferences[stringPreferencesKey("token")] = token
    }
}

// 读（自动刷新）
val tokenFlow: Flow<String?> = context.dataStore.data.map { preferences ->
    preferences[stringPreferencesKey("token")]
}

// Activity 里
lifecycleScope.launch {
    dataStore.data.collect { prefs ->
        val token = prefs[stringPreferencesKey("token")] ?: ""
        // 值变化时自动收到通知
    }
}
```

## DataStore Proto

```kotlin
// Proto DataStore：类型安全，需要定义 proto schema
// settings.proto 文件
message UserPreferences {
    string token = 1;
    int32 theme = 2;
}

// 生成的代码：UserPreferences 是类型安全的对象
val token = preferences.token  // 不会拼错 key 名
```

> **Java 视角**：SP 的数据变化需要自己手动通知（或者用 ContentObserver），DataStore 直接用 Flow 推送变化，观察者模式内置。

---

# 十、WorkManager（★★★★★）

## 什么时候用？

- 上传日志 → 即使 App 退出也要保证上传成功
- 同步数据 → 需要网络条件满足时再执行
- 定期任务 → 每天清理一次缓存
- 约束条件 → 只在充电 + Wi-Fi 时执行

```kotlin
// 1. 定义任务
class UploadWorker(context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            uploadLogs()            // 协程，自动切后台线程
            Result.success()
        } catch (e: Exception) {
            if (runAttemptCount < 3) Result.retry()  // 自动重试
            else Result.failure()
        }
    }
}

// 2. 提交任务
val request = OneTimeWorkRequestBuilder<UploadWorker>()
    .setConstraints(
        Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)  // 有网才执行
            .setRequiresCharging(true)                       // 充电时才执行
            .build()
    )
    .setBackoffCriteria(BackoffPolicy.LINEAR, 10, TimeUnit.SECONDS)
    .build()

WorkManager.getInstance(context).enqueue(request)

// 3. 定期任务
val dailyRequest = PeriodicWorkRequestBuilder<UploadWorker>(24, TimeUnit.HOURS)
    .build()
WorkManager.getInstance(context).enqueue(dailyRequest)

// 4. 任务链（串行执行）
WorkManager.getInstance(context)
    .beginWith(workA)
    .then(workB)
    .then(workC)
    .enqueue()
```

> **Java 视角**：以前用 Service 做后台任务，但 Android 8+ 限制后台 Service，系统随时会杀。WorkManager 自动选择最优方案（JobScheduler / AlarmManager），保证任务在满足条件时一定会执行，App 退出也不影响。

---

# 十一、Paging3（★★★★★）

## 传统分页 vs Paging3

```java
// Java 传统分页
int currentPage = 0;
List<Item> allItems = new ArrayList<>();

void loadNextPage() {
    currentPage++;
    api.getItems(currentPage, new Callback() {
        // 手动拼接到列表
        // 手动处理加载状态（loading / error）
        // 还要处理重复点击、并发请求等问题
    });
}
```

```kotlin
// Paging3 声明式分页
class ItemPagingSource : PagingSource<Int, Item>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Item> {
        return try {
            val page = params.key ?: 1
            val response = api.getItems(page, params.loadSize)
            LoadResult.Page(
                data = response.items,
                prevKey = null,
                nextKey = if (response.hasNext) page + 1 else null
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}

// ViewModel
class ItemViewModel : ViewModel() {
    val flow = Pager(PagingConfig(pageSize = 20)) {
        ItemPagingSource()
    }.flow.cachedIn(viewModelScope)  // 旋转不丢失数据

    // 或者用 LiveData
    val liveData = Pager(PagingConfig(pageSize = 20)) {
        ItemPagingSource()
    }.liveData
}

// Activity 里
val adapter = ItemAdapter()
lifecycleScope.launch {
    viewModel.flow.collectLatest { pagingData ->
        adapter.submitData(pagingData)
    }
}
```

## RemoteMediator：网络 + 缓存

```kotlin
// 既从网络加载，又缓存到 Room
class ItemRemoteMediator(
    private val dao: ItemDao,
    private val api: ItemApi
) : RemoteMediator<Int, Item>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Item>
    ): MediatorResult {
        return try {
            val page = when (loadType) {
                LoadType.REFRESH -> 1
                LoadType.PREPEND -> return MediatorResult.Success(true)
                LoadType.APPEND -> state.lastItemOrNull()?.page ?: 1
            }
            val response = api.getItems(page)
            dao.insertAll(response.items)  // 缓存到 Room
            MediatorResult.Success(endOfPaginationReached = response.items.isEmpty())
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }
}
```

> **Java 视角**：传统分页要自己处理"是否正在加载"、"还有没有下一页"、"加载失败是否重试"等状态。Paging3 把这一切封装成 `PagingSource`，你只需要告诉它"给定页码，返回数据"即可。

---

# 十二、SavedStateHandle（★★★★）

## 场景

系统杀死 App 后恢复数据（不是旋转，是低内存被系统杀掉）。

```kotlin
// ViewModel 使用 SavedStateHandle
class MyViewModel(
    private val handle: SavedStateHandle  // 自动注入，不需要手动创建
) : ViewModel() {

    var count: Int
        get() = handle["count"] ?: 0
        set(value) { handle["count"] = value }

    // 或者用 LiveData 封装
    val countLiveData: LiveData<Int> = handle.getLiveData("count", 0)
}

// 用法（Activity）
val viewModel: MyViewModel by viewModels()
viewModel.count++  // 旋转/被杀后自动恢复

// 即使系统杀进程：
// SavedStateHandle = Bundle（会存到 onSaveInstanceState 里）
// 下次打开 Activity 时自动恢复数据
```

> **Java 视角**：相当于 `onSaveInstanceState(outState: Bundle)` + `onRestoreInstanceState(savedInstanceState: Bundle)` 的合成，不用再手动写 key-value 序列化。

---

# 十三、ViewBinding（★★★★）

## 替代 findViewById

```kotlin
// 1. 在 build.gradle 开启
android {
    viewBinding { enabled = true }
}

// 2. Activity 中使用
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)     // 替代 R.layout.activity_main

        binding.tvTitle.text = "Hello"   // 替代 findViewById(R.id.tv_title)
        binding.btnSubmit.setOnClickListener { ... }
    }
}

// Fragment 中使用
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, ...): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null  // 防止内存泄漏
    }
}
```

> **Java 视角**：`findViewById` 的问题——类型强制转换不安全、XML id 拼错不报错、每个 View 都要写一行声明。ViewBinding 编译时自动生成 `ActivityMainBinding`，类型安全、空安全。

---

# 十四、Coroutine（★★★★★）

## 为什么 Jetpack 需要协程？

```
传统 Java 异步：
  new Thread(() -> { ... }).start()           → 线程开销大
  Executors.newFixedThreadPool(5).execute()   → 管理麻烦
  Retrofit Call.enqueue(new Callback() {})    → 回调嵌套
  RxJava Observable                            → 学习成本高

Kotlin 协程：
  viewModelScope.launch(Dispatchers.IO) { ... }  → 轻量、可取消、结构化
```

## 三大核心

```kotlin
// 1. launch：启动协程，无返回值
viewModelScope.launch {
    val data = repository.fetch()  // 挂起，不阻塞线程
    // fetch 执行完成后自动回来，更新 UI
    textView.text = data
}

// 2. async：启动协程，有返回值
viewModelScope.launch {
    val user = async { repository.getUser() }    // 并发
    val posts = async { repository.getPosts() }  // 并发
    // await() 等待两个都完成
    show(user.await(), posts.await())  // 不阻塞，但 suspend 等待
}

// 3. withContext：切线程
viewModelScope.launch {
    val data = withContext(Dispatchers.IO) {
        // IO 线程执行
        repository.fetchFromDatabase()
    }
    // 回到主线程更新 UI
    textView.text = data
}
```

## Dispatchers

| Dispatcher | 作用 | Java 对标 |
|------------|------|-----------|
| `Dispatchers.Main` | 主线程（UI 操作） | Handler.post |
| `Dispatchers.IO` | 文件/网络/数据库 | new Thread 或线程池 |
| `Dispatchers.Default` | CPU 密集型计算 | Executors.newFixedThreadPool |
| `Dispatchers.Unconfined` | 不指定 | 很少用 |

## Structured Concurrency（结构化并发）

```kotlin
// 传统：线程一旦启动就失控
new Thread(() -> {
    while (true) {
        fetchData();  // 没人能取消它
        Thread.sleep(1000);
    }
}).start();

// 协程：自动取消
viewModelScope.launch {
    while (true) {
        fetchData()     // ViewModel 销毁时 cancel
        delay(1000)     // delay 比 Thread.sleep 好：可取消
    }
}
// viewModelScope.cancel() 会自动取消内部所有协程
```

## 面试高频

> **Q: launch 和 async 的区别？**
>
> A: `launch` 启动一个协程，**没有返回值**，适用于"执行完就不需要结果"的场景（如写入数据库）。`async` 返回 `Deferred<T>`，可以通过 `await()` 获取返回值，适用于"需要并发执行并收集结果"的场景。

> **Q: 协程是轻量级线程吗？为什么"轻量"？**
>
> A: 协程运行在线程上，但一个线程可以运行**成千上万个**协程。因为协程是用户态调度（编译器的状态机），不是操作系统调度；协程的挂起不阻塞线程，线程可以继续跑其他协程。

---

# 十五、Flow（★★★★★）

## 从 LiveData 到 Flow

```kotlin
// LiveData：够用但有限
// ① 只能在主线程 observe
// ② 操作符少（只有 map/switchMap）
// ③ 不支持背压

// Flow：协生代替代方案
class MyViewModel : ViewModel() {
    // LiveData 版本
    val userLiveData: LiveData<User> = repository.getUser()

    // StateFlow 版本（推荐替代 LiveData）
    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user.asStateFlow()

    fun loadUser() {
        viewModelScope.launch {
            // Flow 支持丰富的操作符
            repository.getUserStream()
                .map { it.toUiModel() }           // 转换
                .filter { it.isValid() }           // 过滤
                .catch { _user.value = null }      // 异常处理
                .flowOn(Dispatchers.IO)             // 切线程
                .collect { _user.value = it }
        }
    }
}

// Activity 里收集
lifecycleScope.launch {
    viewModel.user.collect { user ->
        // 每次变化自动收到
        textView.text = user?.name
    }
}
```

## 三大流水类型

```kotlin
// 1. 冷流：每次 collect 都重新执行（懒加载）
val coldFlow = flow {
    emit("只有被 collect 时才执行")
}

// 2. StateFlow：热流，有初始值，保留最新值
//    观察者从 collect 开始收到最新值
val stateFlow = MutableStateFlow("初始值")

// 3. SharedFlow：热流，无初始值，可配置 replay
//    事件通知场景（如导航、Toast）
private val _events = MutableSharedFlow<NavigationEvent>()
val events: SharedFlow<NavigationEvent> = _events.asSharedFlow()

fun navigateToHome() {
    viewModelScope.launch {
        _events.emit(NavigationEvent.GoHome)
    }
}
```

## StateFlow vs LiveData

| 维度 | LiveData | StateFlow |
|------|----------|-----------|
| 初始值 | 可为 null（不强制） | **强制**有初始值 |
| 线程 | 主线程 observe | 任意线程 collect |
| 生命周期感知 | ✅ 内置（observe 传 LifecycleOwner） | ❌ 需要 `collectAsStateWithLifecycle()` |
| 操作符 | map / switchMap（有限） | 全套 Flow 操作符 |
| 背压 | 不支持 | 支持（buffer/conflate） |
| 适合场景 | View 层 | ViewModel 层（推荐） |

## 面试高频

> **Q: Flow 和 LiveData 的区别？**
>
> A: ① Flow 是 Kotlin 协生生态的一部分，支持丰富的操作符（map/filter/combine/zip） ② Flow 支持线程切换（flowOn） ③ LiveData 自动感知生命周期，而 StateFlow 需要 `Lifecycle.repeatOnLifecycle` 或 `collectAsStateWithLifecycle` 配合 ④ 很多新组件（Room / DataStore / Retrofit）的 Flow 支持比 LiveData 更好。**推荐原则**：ViewModel 层用 StateFlow，View 层用 `collectAsStateWithLifecycle` 收集。

---

# 十六、Repository（★★★★★）

## 为什么需要 Repository？

```kotlin
// 没有 Repository 的写法：ViewModel 直接调网络和数据库

class WrongViewModel : ViewModel() {
    // ❌ ViewModel 直接依赖两个数据源
    private val api = RetrofitClient.api
    private val dao = AppDatabase.getInstance().userDao()

    fun getUser(id: String) {
        // ❌ ViewModel 里做"先查缓存再查网络"的逻辑
        // 另一个 ViewModel 也要做同样的逻辑 → 重复代码
        // 换数据源（比如从 Room 换 SQLDelight）→ 所有 ViewModel 都要改
    }
}
```

```kotlin
// 有 Repository：统一数据入口
class UserRepository(
    private val api: UserApi = RetrofitClient.api,
    private val dao: UserDao = AppDatabase.getInstance().userDao()
) {
    // 缓存策略：先查 Room 缓存，再查网络，网络成功更新 Room
    fun getUser(id: String): Flow<Result<User>> = flow {
        // 1. 先发缓存
        val cached = dao.getUser(id)
        if (cached != null) emit(Result.success(cached))

        // 2. 再查网络
        try {
            val response = api.getUser(id)
            dao.insert(response)  // 缓存到本地
            emit(Result.success(response))
        } catch (e: Exception) {
            if (cached == null) emit(Result.failure(e))
            // 有缓存就不报错
        }
    }
}

// ViewModel 只依赖 Repository
class UserViewModel(
    private val repository: UserRepository = UserRepository()
) : ViewModel() {
    fun getUser(id: String) {
        viewModelScope.launch {
            repository.getUser(id).collect { ... }
        }
    }
    // 如果以后要改数据来源（比如换数据源）
    // 只需要改 Repository，ViewModel 完全不用动
}
```

> **Java 视角**：Repository ≈ DAO 模式（Data Access Object）的升级版。以前每个 Activity 自己直接调 API 或数据库，Repository 把"数据从哪里来、缓存策略是什么"集中到一个地方，ViewModel 只需说"给我数据"，不需要知道数据从哪来。

---

# 十七、MVVM（★★★★★）

## 完整的 MVVM 实战

```kotlin
// ======= 1. Data Layer =======
// Room Entity
@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val completed: Boolean = false
)

// Room DAO
@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks ORDER BY id DESC")
    fun getAllTasks(): Flow<List<Task>>    // Room + Flow = 自动刷新

    @Insert
    suspend fun insert(task: Task)

    @Delete
    suspend fun delete(task: Task)
}

// Repository
class TaskRepository(private val dao: TaskDao) {
    val allTasks: Flow<List<Task>> = dao.getAllTasks()

    suspend fun addTask(title: String) {
        dao.insert(Task(title = title))
    }

    suspend fun deleteTask(task: Task) {
        dao.delete(task)
    }
}

// ======= 2. ViewModel =======
data class TaskUiState(
    val tasks: List<Task> = emptyList(),
    val isLoading: Boolean = false
)

class TaskViewModel(
    private val repository: TaskRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TaskUiState())
    val uiState: StateFlow<TaskUiState> = _uiState.asStateFlow()

    init {
        // 自动监听数据库变化
        viewModelScope.launch {
            repository.allTasks.collect { tasks ->
                _uiState.update { it.copy(tasks = tasks, isLoading = false) }
            }
        }
    }

    fun addTask(title: String) {
        viewModelScope.launch {
            repository.addTask(title)
            // Room Flow 会自动推送新数据，不需要手动更新 State
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            repository.deleteTask(task)
        }
    }
}

// ======= 3. UI Layer =======
class MainActivity : AppCompatActivity() {
    private val viewModel: TaskViewModel by viewModels {
        TaskViewModelFactory(TaskRepository(...))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 收集 StateFlow（需要 lifecycle-aware）
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    // 自动更新 UI
                    adapter.submitList(state.tasks)
                    progressBar.visibility = if (state.isLoading) VISIBLE else GONE
                }
            }
        }
    }
}
```

## 与传统 MVC 对比

```
MVC（传统）:                          MVVM（Jetpack）:
─────────────────────────            ─────────────────────────
Activity: View + Controller          Activity: 只负责 View
Model: 数据                          ViewModel: 业务逻辑
耦合: 高                             Repository: 数据管理
测试: 难（依赖 Android 环境）         松耦合，可测试性好
数据更新: 手动 setText()             响应式，数据变 UI 自动变
```

---

# 十八、Jetpack 面试最高频问题

## 1. ViewModel 为什么不会因为旋转销毁？

ViewModel 实例保存在 `ViewModelStore` 中，`ViewModelStore` 存储在 Activity 的 `NonConfigurationInstances` 对象里。Activity 旋转时触发 `onDestroy()` 但不触发 `finish()`，系统不会清理 `NonConfigurationInstances`。新 Activity 从同一个 `ViewModelStore` 中取出之前的 ViewModel。

## 2. LiveData 为什么不会内存泄漏？

`LiveData.observe(LifecycleOwner, Observer)` 会给 `LifecycleOwner` 添加 `LifecycleObserver`，当 `LifecycleOwner` 状态变为 `DESTROYED` 时，LiveData 自动移除 Observer。所以即使 Observer 持有 View 引用，Activity 销毁时引用链也会断开。

## 3. setValue 和 postValue 区别？

`setValue()` 必须在**主线程**调用，立即更新数据并通知观察者。`postValue()` 可以在**任意线程**调用，内部通过 Handler 切到主线程再更新。注意 `postValue()` 会覆盖中间值——连续两次 postValue 只会保留最后一次。

## 4. Room 和 SQLiteOpenHelper 区别？

| Room | SQLiteOpenHelper |
|------|----------------|
| 编译时 SQL 校验 | SQL 写错运行时才崩溃 |
| 自动生成代码 | 手写 Cursor + ContentValues |
| 返回 Flow/LiveData，自动刷新 | 手动监听变化 |
| 自动处理线程切换 | 手动切 IO 线程 |
| 内置 Migration 管理 | 手写升级逻辑 |

## 5. DataStore 为什么替代 SharedPreferences？

① **异步**：SP 的 `getString()` 可能同步读文件卡主线程；DataStore 完全基于 `Flow` / `suspend`。② **类型安全**：Proto DataStore 根据 schema 生成类。③ **异常安全**：SP 文件损坏时静默吞掉异常；DataStore 抛出 `CorruptionException`，保证数据一致性。

## 6. WorkManager 什么时候用？

需要**保证执行**的后台任务——上传日志、同步数据、定期清理缓存。即使 App 退出、系统重启，WorkManager 保证在条件满足时执行。**不适合**：即时任务（用协程）、短时间延迟（用 Handler/ AlarmManager）。

## 7. Paging3 原理？

核心是 `PagingSource` 声明式分页：传入 `LoadParams`（页码、加载数量），返回 `LoadResult.Page`（数据列表、下一页 key）。`Pager` 负责调度，`PagingData` 携带数据流，`PagingDataAdapter` 自动 DiffUtil 更新 RecyclerView。

## 8. Navigation 优点？

统一管理 Fragment 跳转：① 可视化导航图 ② SafeArgs 保证类型安全 ③ 自动返回栈管理 ④ DeepLink 支持 ⑤ 与 Toolbar 自动集成。

## 9. Repository 为什么存在？

集中管理数据来源和缓存策略。ViewModel 不需要知道数据来自网络还是本地缓存，只需要说"给我数据"。换数据源时只需改 Repository。

## 10. MVVM 为什么比 MVC 好？

MVVM 三层职责分明：View 只显示 UI，ViewModel 管业务逻辑，Repository 管数据。每层可独立测试。View 和 ViewModel 通过 LiveData/Flow 解耦，ViewModel 不持有 View 引用，避免内存泄漏。

---

# 十九、企业项目标准架构

```
┌─────────────────────────────────────────────────────┐
│  Presentation Layer                                 │
│  Activity / Fragment / Compose UI                   │
│  └── 只负责渲染、事件回调、observe/collect 数据      │
├─────────────────────────────────────────────────────┤
│  ViewModel Layer                                    │
│  └── 持有 UiState（StateFlow/LiveData）              │
│      调用 Repository，不直接接触网络/数据库           │
├─────────────────────────────────────────────────────┤
│  Repository Layer                                   │
│  └── 封装数据来源策略（先缓存→再网络）               │
├──────────────────┬──────────────────────────────────┤
│  Data Layer       │  Data Layer                      │
│  Remote           │  Local                           │
│  Retrofit/Ktor    │  Room/DataStore                  │
│  OkHttp           │  File/SharedPreferences(旧)      │
└──────────────────┴──────────────────────────────────┘
```

**依赖关系（从上往下，单行）：**
> UI → ViewModel → Repository → DataSource（Remote / Local）

---

# 二十、Android 岗位必须掌握（★★★★★）

## 第一梯队（必须熟练）★JD 100% 要求

- Kotlin 语言
- MVVM / MVI 架构
- ViewModel
- LiveData / StateFlow
- Lifecycle
- Coroutine
- Flow
- Retrofit + OkHttp
- Room
- **Jetpack Compose（声明式 UI，JD 显性要求）**
- **Hilt（依赖注入，JD 硬性要求）**
- Navigation（含 Navigation Compose）
- ViewBinding

## 第二梯队（最好会）

- DataStore
- Paging3
- WorkManager
- Glide / Coil
- SharedFlow（一次性事件）
- DataBinding（维护老项目）

## 第三梯队（了解即可）

- CameraX
- Benchmark
- 鸿蒙 ArkUI（看公司方向，你的目标产品线强相关）

---

# 二十一、推荐学习路线（7天速成）

**Day 1**：Lifecycle + ViewModel + LiveData（理解生命周期感知）
**Day 2**：MVVM 架构 + Repository 模式（搭建完整数据流）
**Day 3**：Room + DataStore（本地持久化）
**Day 4**：Coroutine + Flow（异步编程，替代回调）
**Day 5**：Hilt 依赖注入 + Navigation / Navigation Compose（DI + 页面管理）
**Day 6**：Jetpack Compose 声明式 UI（@Composable / 状态 / 重组 / 副作用 API）+ WorkManager
**Day 7**：整体架构串联（Compose + Hilt + ViewModel + Flow）+ 高频面试题复盘 + 手写 MVVM Demo

---

# 二十二、核心记忆图

```
UI (Activity / Fragment / Compose)
  │   observe() / collect()
  ▼
ViewModel
  │   viewModelScope.launch {}
  │   持有 UiState (StateFlow / LiveData)
  ▼
Repository ─── 缓存策略
  ┌──────┴──────┐
  ▼             ▼
Remote        Local
(Retrofit)    (Room / DataStore)
  │             │
  └──────┬──────┘
         ▼
       Service / Server
```

---

# 二十三、Hilt — 依赖注入（★★★★★）★JD 100% 必考

> JD 明确要求 **Hilt** 作为依赖注入方案。本项目（jetpack-android）已用 Hilt 注入 ViewModel/Retrofit/DataStore。原十七章 MVVM 用手写 `ViewModelFactory`，生产环境标准做法是用 Hilt。

## 为什么需要依赖注入（DI）？

- 传统写法：`ViewModel` 里 `val repo = TaskRepository(api, db)`，依赖在类内部 `new`——**紧耦合、难测试、难替换**。
- DI 思想：依赖由外部提供（构造传入），类只声明「我需要什么」，不关心怎么造。好处：可测试（注入假实现）、可复用、生命周期统一。

## Hilt 核心注解

| 注解 | 作用 |
|------|------|
| `@HiltAndroidApp` | 在 `Application` 上，触发 Hilt 代码生成（必备） |
| `@AndroidEntryPoint` | 标记 Activity/Fragment/View/ViewModel，让其可被注入 |
| `@Inject` | 标记「构造器/字段」需要被注入 |
| `@Module` + `@Provides` | 在模块里提供第三方对象（Retrofit、OkHttp、Room） |
| `@Singleton` / `@ViewModelScoped` | 控制实例作用域（全局单例 / 与 ViewModel 同生命周期） |
| `@HiltViewModel` | 标记 ViewModel，使其构造可注入 Repository |

## 最小可用示例

```kotlin
@HiltAndroidApp
class PomodoroApplication : Application()

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val vm: TimerViewModel by viewModels() // Hilt 自动注入构造依赖
}

@HiltViewModel
class TimerViewModel @Inject constructor(
    private val repository: TimerRepository   // Hilt 自动提供
) : ViewModel()

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides @Singleton
    fun provideRetrofit(): NasApiService =
        Retrofit.Builder().baseUrl(BASE).addConverterFactory(...).build()
            .create(NasApiService::class.java)
}
```

## Hilt vs Dagger vs Koin（150题·74）

| 维度 | Hilt | Dagger | Koin |
|------|------|--------|------|
| 出身 | Google 官方，基于 Dagger | Google，纯编译期 | 社区，运行时 |
| 与 Android 集成 | ✅ 内置 `@AndroidEntryPoint` | ❌ 需手动写 `AndroidInjector` | ✅ 简单 API |
| 编译速度 | 比 Dagger 慢但省心 | 最快但样板多 | 运行时解析稍慢 |
| 学习曲线 | 中（约定优于配置） | 陡 | 低 |

**面试高频：** Hilt = 「为 Android 量身定制的 Dagger」。99% 的 Android 项目选 Hilt，除非是纯 Kotlin 多平台（KMP）才用 Koin。

---

# 二十四、DataBinding — 数据绑定（★★★★）

> 150题有 3 道 DataBinding 题（65-67）。它与 ViewBinding 容易混淆：ViewBinding 只做「findViewById 的类型安全替代」，DataBinding 额外支持「布局里直接绑定数据 + 表达式」。

## DataBinding 是什么

- 在 XML 布局里用 `<layout>` 包裹，通过 `@{}` 表达式把数据直接绑到 View 属性，减少 Activity 里的赋值代码。
- 配合 `BaseObservable` / `ObservableField` 或 `LiveData` 可双向刷新。

```xml
<layout>
  <data>
    <variable name="user" type="com.x.User" />
  </data>
  <TextView android:text="@{user.name}" />
</layout>
```

## 双向绑定与 BindingAdapter（150题·66）

- 双向绑定：`android:text="@={viewModel.name}"`（注意 `=`），用户输入自动写回数据。
- `@BindingAdapter("imageUrl")`：自定义属性绑定逻辑（如 Glide 加载图片），是 DataBinding 最常用扩展点。

```kotlin
@BindingAdapter("imageUrl")
fun ImageView.load(url: String) = Glide.with(this).load(url).into(this)
```

## DataBinding vs ViewBinding（150题·67）

| 维度 | ViewBinding | DataBinding |
|------|-----------|------------|
| 能力 | 仅类型安全访问 View | 数据绑定 + 表达式 + 双向 |
| 编译开销 | 小 | 大（需处理表达式） |
| 适用 | 新项目首选 | 需要布局直接绑数据时用 |

**面试高频：** 现代项目**优先 ViewBinding + StateFlow/Compose**，DataBinding 表达式能力易被 Compose 取代；但若维护老项目或大量表单双向绑定，DataBinding 仍有价值。

---

# 二十五、Jetpack Compose — 声明式 UI（★★★★★）★JD 100% 必考

> JD 中「Jetpack Compose（声明式 UI）」为 100% 必选项，且本项目全部用 Compose 编写。本章是知识点版（对应《面试八股》第八章程 151-170），帮你建立心智模型，八股题直接背。

## 核心心智模型

- **UI = f(state)**：Composable 是纯函数，输入状态、输出 UI；状态变 → 框架自动**重组（Recomposition）**该 Composable 子树。
- **状态容器**：`MutableState`（Compose 原生）或来自 ViewModel 的 `StateFlow`（用 `collectAsStateWithLifecycle` 收集）。
- **单一可信源**：UI 状态放 ViewModel，事件（`onClick`）回传 ViewModel —— 单向数据流（UDF）。

## 状态与重组

```kotlin
@Composable
fun Counter() {
    var count by remember { mutableStateOf(0) } // remember 缓存，避免重组丢失
    Button(onClick = { count++ }) { Text("$count") } // 读 State → 变化触发重组
}
```

- `remember` 防重组丢状态；`rememberSaveable` 防旋转屏幕丢状态。
- `derivedStateOf` 把高频状态降频成低频派生值，减少重组。
- **稳定性**：数据类加 `@Immutable`/`@Stable`，否则被判定不稳定会全量重组。

## 副作用 API（生命周期感知）

| API | 时机 |
|-----|------|
| `LaunchedEffect(key)` | 进入组合/key 变化启动协程，退出自动取消 |
| `DisposableEffect` | 进入执行、`onDispose` 清理（注册监听） |
| `SideEffect` | 每次重组后同步给非 Compose 对象 |
| `rememberCoroutineScope` | 在回调里 launch 协程，绑定组合生命周期 |

## 与 ViewModel / 导航 / 主题

- 取 ViewModel：`viewModel()` / `hiltViewModel()`（Hilt 场景）。
- 收集流：`collectAsStateWithLifecycle()`（lifecycle-runtime-compose），**不要**裸用 `collectAsState`（不感知生命周期）。
- 导航：`NavHost(navController, startDestination)` 代码化路由，类型安全参数。
- 主题：`MaterialTheme` 经 `CompositionLocal` 向下提供颜色/字体。
- 列表：`LazyColumn { items(items, key = { it.id }) { ... } }`，`key` 防错位。

## Compose vs View 一句话

> XML 是「建好 UI 树再手动改」，Compose 是「描述当前状态对应的 UI，框架负责刷新」。

**面试高频（8年 Java 视角）：** 公司从 XML 切 Compose 的动因——开发效率、状态一致性、动态化、以及和 Flutter 思路相通（你接下来要接的 Flutter 模块），便于混合架构叙事。

---

# 总结

真正企业开发中，Jetpack 并非所有组件都会用到，但以下组件几乎是 Android 开发的标配：

- **Lifecycle** → 自动管理生命周期
- **ViewModel** → UI 数据在旋转/销毁时保留
- **LiveData / StateFlow** → 响应式数据推送
- **Coroutine + Flow** → 异步编程基础设施
- **Room** → 类型安全的本地数据库
- **Navigation** → 统一页面管理
- **DataStore** → SP 替代方案
- **WorkManager** → 可靠后台任务
- **Repository** → 统一数据入口
- **ViewBinding** → 类型安全 findViewById

掌握以上内容，可以覆盖绝大多数 Android 应用开发和中高级 Android 岗位面试。
