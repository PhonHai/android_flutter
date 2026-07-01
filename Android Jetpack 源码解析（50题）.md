# Android Jetpack 源码解析（50题）

> 面向中高级 Android 开发者面试加分项，重点分析 Jetpack 核心组件的源码原理。每题深度剖析内部实现，展示源码级别的理解深度。

---

## 目录

### 一、ViewModel 源码深度（10题）
- [第1题：ViewModel 的创建流程](#第1题viewmodel-的创建流程)
- [第2题：ViewModel 如何做到 Configuration Change 存活？](#第2题viewmodel-如何做到-configuration-change-存活)
- [第3题：ViewModel 的生命周期与 onCleared() 时机](#第3题viewmodel-的生命周期与-oncleared-时机)
- [第4题：SavedStateHandle 源码原理](#第4题savedstatehandle-源码原理)
- [第5题：AndroidViewModel 与 ViewModel 的区别](#第5题androidviewmodel-与-viewmodel-的区别)
- [第6题：ViewModel 作用域与共享](#第6题viewmodel-作用域与共享)
- [第7题：ViewModel 如何避免被 GC 回收？](#第7题viewmodel-如何避免被-gc-回收)
- [第8题：Hilt 与 ViewModel 的结合](#第8题hilt-与-viewmodel-的结合)
- [第9题：ViewModel + Kotlin Flow 最佳实践源码分析](#第9题viewmodel--kotlin-flow-最佳实践源码分析)
- [第10题：自定义 ViewModelProvider.Factory 的高级用法](#第10题自定义-viewmodelproviderfactory-的高级用法)

### 二、LiveData 源码深度（8题）
- [第11题：LiveData.observe() 完整链路](#第11题livedataobserve-完整链路)
- [第12题：setValue() vs postValue() 源码对比](#第12题setvalue-vs-postvalue-源码对比)
- [第13题：LiveData 粘性事件解决方案源码分析](#第13题livedata-粘性事件解决方案源码分析)
- [第14题：MediatorLiveData 源码分析](#第14题mediatorlivedata-源码分析)
- [第15题：Transformations.map() 和 switchMap() 源码](#第15题transformationsmap-和-switchmap-源码)
- [第16题：LiveData 的 distinctUntilChanged 原理](#第16题livedata-的-distinctuntilchanged-原理)
- [第17题：LiveData 内存泄漏分析](#第17题livedata-内存泄漏分析)
- [第18题：LiveData vs Flow 深度对比（源码角度）](#第18题livedata-vs-flow-深度对比源码角度)

### 三、Lifecycle 源码深度（6题）
- [第19题：Lifecycle 状态机模型源码](#第19题lifecycle-状态机模型源码)
- [第20题：LifecycleRegistry 核心实现](#第20题lifecycleregistry-核心实现)
- [第21题：ProcessLifecycleOwner 的实现原理](#第21题processlifecycleowner-的实现原理)
- [第22题：LifecycleObserver 注解处理器](#第22题lifecycleobserver-注解处理器)
- [第23题：ReportFragment 的设计原理](#第23题reportfragment-的设计原理)
- [第24题：自定义 LifecycleOwner 的注意事项](#第24题自定义-lifecycleowner-的注意事项)

### 四、Room 源码深度（8题）
- [第25题：Room 编译时注解处理器（APT）流程](#第25题room-编译时注解处理器apt流程)
- [第26题：Room DAO 的 CRUD 代码生成](#第26题room-dao-的-crud-代码生成)
- [第27题：Room 数据库版本迁移（Migration）源码](#第27题room-数据库版本迁移migration源码)
- [第28题：Room 事务处理原理](#第28题room-事务处理原理)
- [第29题：Room 的 TypeConverter 源码分析](#第29题room-的-typeconverter-源码分析)
- [第30题：Room 与 Flow 集成原理](#第30题room-与-flow-集成原理)
- [第31题：Room 数据库的 WAL 模式](#第31题room-数据库的-wal-模式)
- [第32题：Room 与 LiveData 集成](#第32题room-与-livedata-集成)

### 五、Navigation 源码深度（5题）
- [第33题：Navigation 的导航图（NavGraph）构建流程](#第33题navigation-的导航图navgraph构建流程)
- [第34题：NavController 的导航栈管理](#第34题navcontroller-的导航栈管理)
- [第35题：Safe Args 的类型安全原理](#第35题safe-args-的类型安全原理)
- [第36题：Deep Link 在 Navigation 中的处理](#第36题deep-link-在-navigation-中的处理)
- [第37题：Navigation 返回栈的保存与恢复](#第37题navigation-返回栈的保存与恢复)

### 六、Paging 3 源码深度（4题）
- [第38题：PagingSource 的加载机制](#第38题pagingsource-的加载机制)
- [第39题：RemoteMediator 的网络+本地双重加载](#第39题remotemediator-的网络本地双重加载)
- [第40题：PagingData 的流式转换](#第40题pagingdata-的流式转换)
- [第41题：Paging 3 的分页状态与 UI 集成](#第41题paging-3-的分页状态与-ui-集成)

### 七、WorkManager 源码深度（3题）
- [第42题：WorkManager 的任务调度机制](#第42题workmanager-的任务调度机制)
- [第43题：WorkManager 的内部调度器](#第43题workmanager-的内部调度器)
- [第44题：WorkManager 的数据库设计](#第44题workmanager-的数据库设计)

### 八、Hilt/Dagger 依赖注入源码（3题）
- [第45题：Hilt 的编译时代码生成](#第45题hilt-的编译时代码生成)
- [第46题：Hilt 的组件层级和作用域](#第46题hilt-的组件层级和作用域)
- [第47题：Hilt 与 ViewModel 的集成原理](#第47题hilt-与-viewmodel-的集成原理)

### 九、DataStore 源码（3题）
- [第48题：Preferences DataStore 的读写原理](#第48题preferences-datastore-的读写原理)
- [第49题：Proto DataStore 与 Preferences DataStore 的对比](#第49题proto-datastore-与-preferences-datastore-的对比)
- [第50题：DataStore 的异常处理和迁移](#第50题datastore-的异常处理和迁移)

---

## 一、ViewModel 源码深度（10题）

<a id="第1题viewmodel-的创建流程"></a>
### 第1题：ViewModel 的创建流程

### 问题描述
ViewModel 是如何被创建并缓存到 ViewModelStore 中的？调用 `ViewModelProvider(owner).get(MyViewModel::class.java)` 的背后发生了什么？

### 核心源码分析

```kotlin
// ViewModelProvider.java
public <T extends ViewModel> T get(@NonNull Class<T> modelClass) {
    String canonicalName = modelClass.getCanonicalName();
    // 1. 构造 key：DEFAULT_KEY + ":" + 全限定类名
    return get(DEFAULT_KEY + ":" + canonicalName, modelClass);
}

public <T extends ViewModel> T get(@NonNull String key, @NonNull Class<T> modelClass) {
    // 2. 先从 ViewModelStore 中查找
    ViewModel viewModel = mViewModelStore.get(key);
    if (modelClass.isInstance(viewModel)) {
        if (mFactory instanceof OnRequeryFactory) {
            ((OnRequeryFactory) mFactory).onRequery(viewModel);
        }
        return (T) viewModel; // 命中缓存，直接返回
    }
    // 3. 缓存未命中，通过 Factory 创建新实例
    if (mFactory instanceof KeyedFactory) {
        viewModel = ((KeyedFactory) mFactory).create(key, modelClass);
    } else {
        viewModel = mFactory.create(modelClass); // 反射 newInstance()
    }
    // 4. 存入 ViewModelStore
    mViewModelStore.put(key, viewModel);
    return (T) viewModel;
}

// ViewModelStore.java
public class ViewModelStore {
    private final HashMap<String, ViewModel> mMap = new HashMap<>();
    
    final void put(String key, ViewModel viewModel) { mMap.put(key, viewModel); }
    final ViewModel get(String key) { return mMap.get(key); }
    public final void clear() { /* 遍历调用 onCleared() */ }
}
```

**调用链总结**：`ViewModelProvider.get()` → `ViewModelStore.get()`（HashMap查找）→ 若空则 `Factory.create()`（默认反射 `modelClass.newInstance()`）→ `ViewModelStore.put()`。

### 时序图说明

```
Activity/Fragment
  │
  ├─ new ViewModelProvider(owner, factory)
  │   └─ 持有 ViewModelStoreOwner 和 Factory
  │
  └─ get(MyViewModel.class)
      ├─ 1. 构造key: "androidx.lifecycle.ViewModelProvider.DefaultKey:MyViewModel"
      ├─ 2. ViewModelStore.get(key) → HashMap查找
      │     ├─ 命中 → 返回缓存的 ViewModel
      │     └─ 未命中 ↓
      ├─ 3. Factory.create(MyViewModel.class) → 反射 newInstance()
      ├─ 4. ViewModelStore.put(key, newViewModel)
      └─ 5. 返回 ViewModel 实例
```

### 面试加分点
- 说出 ViewModel 的默认 `Factory` 是 `NewInstanceFactory`，内部通过 `modelClass.newInstance()` 反射创建，要求无参构造函数。
- 指出自定义 `Factory` 通过 `ViewModelProvider.Factory.create()` 接口扩展，典型如 `AbstractSavedStateViewModelFactory`（支持 `SavedStateHandle` 和 `Application` 注入）。
- 提及 `AndroidViewModelFactory` 会检查 `AndroidViewModel` 子类，通过 `newInstance(application)` 创建。

### 常见误区
- **误区**：认为每次 `get()` 都创建新 ViewModel。**正解**：`ViewModelStore` 的 `HashMap<String, ViewModel>` 保证同一 `key` 只创建一次。
- **误区**：认为 `ViewModelProvider` 是单例。**正解**：每次 `new ViewModelProvider()` 是新建的，但共用同一个 `ViewModelStoreOwner` 的 `ViewModelStore`。
- **误区**：构造 ViewModel 时直接在构造函数中传参而不用 Factory。**正解**：默认 Factory 用反射无参构造，多参数必须自定义 Factory。

---

<a id="第2题viewmodel-如何做到-configuration-change-存活"></a>
### 第2题：ViewModel 如何做到 Configuration Change 存活？

### 问题描述
屏幕旋转时 Activity 被销毁重建，ViewModel 为什么能保持同一实例不被销毁？底层机制是什么？

### 核心源码分析

```java
// ComponentActivity.java
@NonNull
@Override
public final Object onRetainNonConfigurationInstance() {
    Object custom = onRetainCustomNonConfigurationInstance();
    ViewModelStore viewModelStore = mViewModelStore;
    if (viewModelStore == null) {
        // 如果还没创建过 ViewModelStore，生成一个空壳
        NonConfigurationInstances nci = (NonConfigurationInstances) getLastNonConfigurationInstance();
        if (nci != null) {
            viewModelStore = nci.viewModelStore;
        }
    }
    if (viewModelStore == null && custom == null) {
        return null;
    }
    // 2. 将 ViewModelStore 打包进 NonConfigurationInstances
    NonConfigurationInstances nci = new NonConfigurationInstances();
    nci.custom = custom;
    nci.viewModelStore = viewModelStore;
    return nci; // 3. 返回给 ActivityThread 保存
}
```

```java
// ComponentActivity.java — 重建时恢复 ViewModelStore
public ViewModelStore getViewModelStore() {
    if (mViewModelStore == null) {
        NonConfigurationInstances nc = 
            (NonConfigurationInstances) getLastNonConfigurationInstance();
        if (nc != null) {
            // 4. 从 NonConfigurationInstances 恢复 ViewModelStore
            mViewModelStore = nc.viewModelStore;
        }
        if (mViewModelStore == null) {
            mViewModelStore = new ViewModelStore();
        }
    }
    return mViewModelStore;
}
```

**核心机制**：系统在 `ActivityThread` 层持有 `ActivityClientRecord`，其中保存了 `lastNonConfigurationInstances`。Configuration Change 时，旧 Activity 调用 `onRetainNonConfigurationInstance()` 返回要保留的对象，系统将其暂存；新 Activity 通过 `getLastNonConfigurationInstance()` 取回。

### 时序图说明

```
【屏幕旋转】
ActivityThread
  │  detects config change
  ├─ 旧 Activity.onRetainNonConfigurationInstance()
  │   └─ 返回 NonConfigurationInstances{viewModelStore}
  │       └─ ViewModelStore{HashMap<key,ViewModel>} 被序列化到 ActivityClientRecord
  ├─ 旧 Activity.onDestroy() → ViewModelStore.clear() 不会被调用
  │   （系统判断 isChangingConfigurations() = true，跳过清理）
  │
  ├─ 新 Activity 创建
  │   └─ Activity.attach() ← ActivityClientRecord.lastNonConfigurationInstances
  │
  ├─ 新 Activity.onCreate()
  │   └─ ViewModelProvider.get()
  │       └─ getViewModelStore()
  │           └─ = getLastNonConfigurationInstance().viewModelStore
  │               └─ 取回同一个 ViewModelStore，拿回全部 ViewModel
```

### 面试加分点
- 强调 `ComponentActivity` 实现了接口 `ViewModelStoreOwner` 和 `HasDefaultViewModelProviderFactory`。
- 指出 `onRetainNonConfigurationInstance()` 是 Activity 的 final 方法，由系统框架保证调用时机（在 `onStop()` 和 `onDestroy()` 之间）。
- 对比 `onSaveInstanceState(Bundle)`（进程重建，Bundle 序列化有限制）vs `onRetainNonConfigurationInstance`（内存暂存，无限制）。
- 了解 `ViewModelStoreOwner` 的核心能力就是提供 `ViewModelStore` 并在适当时候清理。

### 常见误区
- **误区**：认为 ViewModel 通过 `onSaveInstanceState` 存活。**正解**：Configuration Change 是内存级别，走 `onRetainNonConfigurationInstance`；进程被杀才走 `onSaveInstanceState` + `SavedStateHandle`。
- **误区**：认为 ViewModel 一定安全，可以存任何引用。**正解**：ViewModel 虽然跨 Configuration Change 存活，但若持有旧 Activity 引用仍会导致内存泄漏。
- **误区**：在 Activity 中自己 `new ViewModel()` 存储。**正解**：必须通过 `ViewModelProvider` 创建，否则不会进入 `ViewModelStore`，也不会被正确清理。

---

<a id="第3题viewmodel-的生命周期与-oncleared-时机"></a>
### 第3题：ViewModel 的生命周期与 onCleared() 时机

### 问题描述
ViewModel 的 `onCleared()` 什么时候被调用？为什么旋转屏幕不会触发清理，而正常返回会触发？

### 核心源码分析

```java
// ComponentActivity.java
public ComponentActivity() {
    getLifecycle().addObserver(new LifecycleEventObserver() {
        @Override
        public void onStateChanged(@NonNull LifecycleOwner source,
                @NonNull Lifecycle.Event event) {
            if (event == Lifecycle.Event.ON_DESTROY) {
                // 关键判断：非配置变更才清理
                if (!isChangingConfigurations()) {
                    getViewModelStore().clear();
                }
            }
        }
    });
}
```

```java
// ViewModelStore.java
public final void clear() {
    for (ViewModel vm : new ArrayList<>(mMap.values())) {
        vm.clear(); // 调用每个 ViewModel 的 clear()
    }
    mMap.clear();
}

// ViewModel.java
final void clear() {
    mCleared = true;
    if (mBagOfTags != null) {
        synchronized (mBagOfTags) {
            for (Object value : mBagOfTags.values()) {
                closeWithRuntimeException(value); // 关闭 Closeable 对象
            }
        }
    }
    onCleared(); // 开发者可重写的回调
}
```

**关键判断点**：
```java
// Activity.java
public boolean isChangingConfigurations() {
    return mChangingConfigurations; 
    // 系统在 performPause() 时设置，performStop() 时清除
}
```

系统在生命周期走到 `ON_DESTROY` 时，通过 `isChangingConfigurations()` 判断：配置变更时跳过 `clear()`，真正销毁时才执行清理。

### 时序图说明

```
【正常关闭 Activity】
Activity.finish()
  └─ Lifecycle.Event.ON_DESTROY
      ├─ isChangingConfigurations() = false ✓
      └─ ViewModelStore.clear()
          ├─ vm1.onCleared()
          ├─ vm2.onCleared()
          └─ mMap.clear()

【屏幕旋转】
Activity.recreate()  // isChangingConfigurations = true
  └─ Lifecycle.Event.ON_DESTROY
      ├─ isChangingConfigurations() = true ✗
      └─ ViewModelStore.clear() 跳过不执行
          └─ onRetainNonConfigurationInstance() 保留 ViewModelStore
```

### 面试加分点
- 指出 `ComponentActivity` 的构造方法中通过 `LifecycleEventObserver` 监听 `ON_DESTROY` 事件，这是 ViewModel 自动清理的入口。
- 补充 Fragment 中也是同样逻辑：`Fragment.performDestroy()` 中检查 `!mFragmentManager.isStateSaved()` 和 `!mRemoving` 决定是否清理。
- 提及 `onCleared()` 中应取消协程、移除监听器、关闭资源，防止泄漏。通常配合 `viewModelScope` 自动取消。
- `closeWithRuntimeException()` 会遍历 `mBagOfTags`，关闭所有 `Closeable` 对象（`CloseableCoroutineScope` 即在此清理）。

### 常见误区
- **误区**：在 `onCleared()` 中做 UI 相关操作。**正解**：此时 Activity/Fragment 可能已销毁，UI 操作会崩溃。
- **误区**：认为 ViewModel 引用 Activity 没关系，反正在 `onCleared` 清理。**正解**：ViewModel 存活时间比 Activity 长，持有 Activity 引用在配置变更时泄漏旧 Activity。
- **误区**：手动调用 `ViewModelStore.clear()` 释放内存。**正解**：系统自动管理，手动调用可能导致生命周期不一致。

---

<a id="第4题savedstatehandle-源码原理"></a>
### 第4题：SavedStateHandle 源码原理

### 问题描述
`SavedStateHandle` 如何在 ViewModel 中保存和恢复数据？它与传统的 `onSaveInstanceState(Bundle)` 是什么关系？

### 核心源码分析

```java
// SavedStateViewModelFactory.java
@Override
public <T extends ViewModel> T create(@NonNull String key, @NonNull Class<T> modelClass) {
    // 1. 获取 SavedStateRegistry
    SavedStateRegistryController controller = SavedStateRegistryController.create(owner);
    controller.performRestore(null); // 确保恢复已完成

    if (isAndroidViewModel) {
        // 2. 通过反射构造，注入 Application 和 SavedStateRegistry
        return modelClass.getConstructor(Application.class, SavedStateHandle.class)
            .newInstance(app, controller.getSavedStateRegistry());
    }
    // 3. 普通 ViewModel：注入 SavedStateHandle
    return modelClass.getConstructor(SavedStateHandle.class)
        .newInstance(controller.getSavedStateRegistry());
}
```

```kotlin
// SavedStateHandle.kt — 内部存储
class SavedStateHandle {
    private val regular = mutableMapOf<String, Any?>()     // 普通数据
    private val savedStateProviders = mutableMapOf<String, SavedStateRegistry.SavedStateProvider>()
    private val liveDatas = mutableMapOf<String, MutableLiveData<Any?>>()
    private var restoredState: Bundle? = null  // 进程恢复的 Bundle

    // 恢复入口
    fun performRestore(savedState: Bundle?) {
        restoredState = savedState
        // 恢复时触发关联的 LiveData 更新
    }

    // 保存入口
    fun performSave(outBundle: Bundle) {
        // 1. 收集 savedStateProviders
        savedStateProviders.forEach { (key, provider) ->
            outBundle.putString(key, URLEncoder.encode(provider.saveState()))
        }
        // 2. 写入 regular 的简单类型
        regular.keys.forEach { key ->
            // Int/Long/String/Boolean 等直接写入 Bundle
        }
    }
}
```

**恢复链路**：`Activity.onRestoreInstanceState()` → `SavedStateRegistry.performRestore(Bundle)` → `SavedStateHandle.performRestore(Bundle)` → `LiveData.setValue()` 触发 observer。

**保存链路**：`Activity.onSaveInstanceState()` → `SavedStateRegistry.performSave(Bundle)` → `SavedStateHandle.performSave(Bundle)`。

### 时序图说明

```
【进程重建恢复流程】
Activity.onCreate(savedInstanceState)
  └─ SavedStateRegistryController.performRestore(savedInstanceState)
      └─ SavedStateRegistry.performRestore(bundle)
          └─ 遍历 registeredComponents 恢复
              └─ SavedStateHandle.performRestore(bundle)
                  ├─ restoredState = bundle
                  └─ 触发 LiveData 更新 → Observer 收到数据

【保存流程】
Activity.onSaveInstanceState(outState)
  └─ SavedStateRegistryController.performSave(outState)
      └─ SavedStateRegistry.performSave(outState)
          └─ SavedStateHandle.performSave(outState)
              ├─ 收集 savedStateProviders 数据
              └─ 写入 regular Map 中的简单类型
```

### 面试加分点
- 明确指出 `SavedStateHandle` 解决的是**进程被杀**（低内存、系统回收）的数据恢复，而非 Configuration Change。
- 补充 `SavedStateRegistry` 是 `ComponentActivity` 和 `Fragment` 各自维护的，生命期与 `Lifecycle.State.CREATED` 对齐。
- 提及 `getLiveData<T>(key)` 返回的 LiveData 会自动与 `SavedStateHandle` 同步。
- 对比：Configuration Change → `onRetainNonConfigurationInstance`（内存级，无大小限制）；进程重启 → `SavedStateHandle`（Bundle 序列化，有限制）。

### 常见误区
- **误区**：将 `SavedStateHandle` 当作普通的 `HashMap` 使用，存储大数据。**正解**：底层走 Bundle 序列化，数据量过大会导致 TransactionTooLargeException。
- **误区**：在 ViewModel 构造函数中使用 `SavedStateHandle` 但在 `init{}` 中访问数据时数据尚未恢复。**正解**：数据恢复发生在 `performRestore()` 之后，可在 `init{}` 之后的安全位置访问。
- **误区**：使用 `SavedStateModule.builder()` 时必须手动初始化 `SavedStateRegistry`。**正解**：通过 `SavedStateViewModelFactory` 创建时会自动完成 register 和 restore。

---

<a id="第5题androidviewmodel-与-viewmodel-的区别"></a>
### 第5题：AndroidViewModel 与 ViewModel 的区别

### 问题描述
`AndroidViewModel` 和普通 `ViewModel` 有什么本质区别？为什么需要单独提供 Application 引用？

### 核心源码分析

```java
// AndroidViewModel.java
public class AndroidViewModel extends ViewModel {
    private Application mApplication;
    
    public AndroidViewModel(@NonNull Application application) {
        mApplication = application;
    }
    
    @NonNull
    public <T extends Application> T getApplication() {
        return (T) mApplication;
    }
}

// AndroidViewModelFactory.java
public static class AndroidViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private static AndroidViewModelFactory sInstance;
    private Application mApplication;

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (AndroidViewModel.class.isAssignableFrom(modelClass)) {
            // AndroidViewModel 子类：反射调用两参构造
            try {
                return modelClass.getConstructor(Application.class).newInstance(mApplication);
            } catch (...) {
                throw new RuntimeException("Cannot create instance", e);
            }
        }
        // 普通 ViewModel：走父类无参反射
        return super.create(modelClass);
    }
}
```

**核心区别**：
1. `ViewModel` 构造函数无参，通过 `NewInstanceFactory` 反射 `newInstance()` 创建。
2. `AndroidViewModel` 构造函数接收 `Application`，由 `AndroidViewModelFactory` 通过 `getConstructor(Application.class).newInstance(app)` 创建。
3. `ComponentActivity` 的默认 Factory 就是 `SavedStateViewModelFactory(application, this, intent.extras)`，它同时支持 `AndroidViewModel` 和带 `SavedStateHandle` 的 ViewModel。

### 时序图说明

```
ViewModelProvider(activity).get(MyAndroidViewModel.class)
  └─ SavedStateViewModelFactory.create(key, modelClass)
      ├─ 检查 modelClass 是否继承 AndroidViewModel
      │   ├─ 是 → getConstructor(Application.class).newInstance(app)
      │   └─ 否 → getConstructor(SavedStateHandle.class).newInstance(savedStateRegistry)
      │        (或 getConstructor(Application.class, SavedStateHandle.class))
      └─ 返回 ViewModel 实例
```

### 面试加分点
- 指出 `SavedStateViewModelFactory` 是 `ComponentActivity` 默认使用的 Factory，它已经兼容 `AndroidViewModel`、`SavedStateHandle`、`Application` 的各种组合。
- 强调 `Application` 是全局单例，生命周期比 ViewModel 更长，不会造成泄漏。
- 明确使用场景：需要 `Context` 获取系统服务（如 `getSystemService`）、访问 `SharedPreferences`、初始化第三方 SDK 等。

### 常见误区
- **误区**：在普通 ViewModel 中通过构造函数传入 Activity Context。**正解**：Actity 会在 Configuration Change 时重建，导致泄漏旧引用。应使用 `AndroidViewModel` 获取 `Application` Context。
- **误区**：`AndroidViewModel` 可以持有 Activity 级别的 Context。**正解**：它只会持有 `Application`，是比 Activity 更长的生命周期。
- **误区**：以为 `AndroidViewModel` 需要自定义 Factory 才能使用。**正解**：`ComponentActivity` 的默认 Factory 是 `SavedStateViewModelFactory`，已支持通过反射创建 AndroidViewModel 子类。

---

<a id="第6题viewmodel-作用域与共享"></a>
### 第6题：ViewModel 作用域与共享

### 问题描述
Activity-scoped、Fragment-scoped、NavGraph-scoped 的 ViewModel 如何实现？它们的作用域隔离机制是什么？

### 核心源码分析

```kotlin
// 1. Activity-scoped ViewModel
// 整个 Activity 生命周期内唯一
viewModel = ViewModelProvider(activity).get(SharedViewModel::class.java)
// 内部：activity.viewModelStore 作为存储容器

// 2. Fragment-scoped ViewModel
// 当前 Fragment 生命周期内唯一
viewModel = ViewModelProvider(fragment).get(MyViewModel::class.java)
// 内部：fragment.mViewModelStore

// 3. Fragment 间共享（使用 Activity 作用域）
// FragmentA 和 FragmentB 共享
sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
```

```kotlin
// 4. NavGraph-scoped ViewModel（Navigation 组件支持）
// navGraphViewModels 扩展
@MainThread
inline fun <reified VM : ViewModel> Fragment.navGraphViewModels(
    @IdRes navGraphId: Int
): Lazy<VM> {
    return createViewModelLazy(VM::class, {
        // 查找 NavController，获取 NavBackStackEntry 作为 ViewModelStoreOwner
        val backStackEntry = findNavController().getBackStackEntry(navGraphId)
        backStackEntry
    }, { defaultViewModelProviderFactory })
}
```

**核心原理**：ViewModel 的唯一性由 `key` 保证，不同 `ViewModelStoreOwner` 有独立的 `ViewModelStore`（各自独立的 `HashMap`），从而实现了作用域隔离。

### 时序图说明

```
【不同作用域的 ViewModel 隔离】

Activity ViewModelStore（HashMap）
  ├─ "key:SharedViewModel" → Instance_A
  └─ 与 Fragment ViewModelStore 完全独立

FragmentA ViewModelStore（HashMap）
  ├─ "key:MyViewModel" → Instance_B
  └─ 销毁时：Fragment.onDestroy() → viewModelStore.clear()

FragmentB ViewModelStore（HashMap）
  ├─ "key:MyViewModel" → Instance_C  // FragmentA 同名但不同实例
  └─ 销毁时：Fragment.onDestroy() → viewModelStore.clear()

【共享 ViewModel】
Activity ViewModelStore（HashMap）
  └─ "key:SharedViewModel" → Instance_X
      ├─ FragmentA: ViewModelProvider(activity).get(...) → Instance_X
      └─ FragmentB: ViewModelProvider(activity).get(...) → Instance_X  // 同一实例
```

### 面试加分点
- 指出 `NavBackStackEntry` 实现了 `ViewModelStoreOwner` 接口，这使 NavGraph 级别的 ViewModel 成为可能。
- 提出自定义 `ViewModelStoreOwner`：只需实现 `getViewModelStore()` 方法，在适当的时机调用 `viewModelStore.clear()`。
- 补充 Hilt 中的 `@ActivityScoped`、`@FragmentScoped` 作用域注解与 ViewModel 作用域是两个独立概念，但可配合使用。

### 常见误区
- **误区**：所有 Fragment 的 ViewModel 都共享。**正解**：只有使用同一个 `ViewModelStoreOwner`（如同一个 Activity）才共享。
- **误区**：ViewModel 的 key 即使相同，不同作用域也会冲突。**正解**：不同 ViewModelStoreOwner 持有的 `ViewModelStore` 是不同对象，HashMap 各自独立，不会冲突。
- **误区**：DialogFragment 中直接用 `requireActivity()` 获取 ViewModel 是最佳实践。**正解**：可能获取到宿主 Activity 的 ViewModel 造成不必要耦合，建议评估作用域需求。

---

<a id="第7题viewmodel-如何避免被-gc-回收"></a>
### 第7题：ViewModel 如何避免被 GC 回收？

### 问题描述
ViewModel 对象会被 GC 回收吗？它的引用链是怎样的？为什么不能持有 View/Activity 引用？

### 核心源码分析

```java
// 引用链分析
ComponentActivity
  └─ NonConfigurationInstances (系统持有，非 GC Root)
      └─ ViewModelStore mViewModelStore
          └─ HashMap<String, ViewModel> mMap
              └─ MyViewModel (强引用)

// GC Root 链路：
// ActivityThread.mActivities → ActivityClientRecord
//   → .lastNonConfigurationInstances → NonConfigurationInstances
//     → viewModelStore → HashMap → ViewModel
```

**为什么不会被回收**：
- ViewModel 通过 `ViewModelStore` 的 `HashMap<String, ViewModel>` 被持有，是强引用。
- `ViewModelStore` 被 `NonConfigurationInstances` 持有。
- `NonConfigurationInstances` 被 `ActivityThread` 的 `ActivityClientRecord.lastNonConfigurationInstances` 持有。
- 这条引用链从 GC Root（`ActivityThread`，主线程静态持有）出发，ViewModel 始终可达，不会被 GC。

**为什么不能持有 Activity/View 引用**：
```kotlin
// 反例：错误做法
class MyViewModel(val activity: Activity) : ViewModel() {
    // Configuration Change 时：
    // 旧 Activity 被销毁，但 ViewModel 仍存活
    // → ViewModel 持有旧 Activity 引用
    // → GC Root 链：ActivityThread → ViewModel → 旧 Activity（泄漏！）
}
```

### 时序图说明

```
【GC Root 引用链】
GC Roots（静态变量、线程栈、JNI引用等）
  └─ ActivityThread（主线程静态实例）
      └─ ArrayMap<IBinder, ActivityClientRecord>
          └─ ActivityClientRecord
              └─ lastNonConfigurationInstances
                  └─ NonConfigurationInstances
                      └─ ViewModelStore
                          └─ HashMap<String, ViewModel>
                              └─ MyViewModel（强引用 → 不可回收）

【错误引用导致泄漏】
MyViewModel → Activity（已销毁）
  ↑
  └─ ViewModelStore → NonConfigurationInstances → ActivityThread（GC Root）
        → 旧 Activity 无法被 GC → 内存泄漏
```

### 面试加分点
- 补充 `onRetainNonConfigurationInstance()` 返回的 `NonConfigurationInstances` 对象在 Activity 真正 finish 后，系统会将其置为 null（`ActivityClientRecord.lastNonConfigurationInstances = null`），ViewModel 随之变为可回收。
- 指出 ViewModel 的 `finalize()` 方法不会被用于资源清理（因为不建议依赖 finalize），正确做法是依赖 `onCleared()` 和 `viewModelScope`。
- `mBagOfTags` 机制：ViewModel 内部维护一个 `Map<String, Object>`，用于关闭 `Closeable` 对象。`viewModelScope` 就是通过 `addCloseable()` 注册到此 Map 中，在 `clear()` 时自动关闭。

### 常见误区
- **误区**：ViewModel 是弱引用，GC 可能在 Configuration Change 时回收它。**正解**：是强引用，由系统框架保证可达性。
- **误区**：在 ViewModel 中使用 `WeakReference<Activity>` 就可以安全持有 Activity。**正解**：弱引用可避免泄漏，但应尽量避免 ViewModel 知道 View 层的存在，保持架构清晰。
- **误区**：Application Context 也会泄漏。**正解**：Application 是进程级别单例，生命周期与应用相同，不会被 ViewModel 泄漏。

---

<a id="第8题hilt-与-viewmodel-的结合"></a>
### 第8题：Hilt 与 ViewModel 的结合

### 问题描述
`@HiltViewModel` 注解如何实现依赖注入？Hilt 是如何在 ViewModel 构造函数中注入依赖的？

### 核心源码分析

```kotlin
// @HiltViewModel 注解
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.BINARY)
@MapKey
annotation class HiltViewModel

// Hilt 编译生成的代码模块
@Module
@InstallIn(ViewModelComponent::class)
abstract class MyViewModel_HiltModules {
    @Binds
    @IntoMap
    @HiltViewModelMap.Key(MyViewModel::class)
    abstract fun bind(viewModel: MyViewModel): ViewModel
    // 生成 ViewModel 的绑定，放入 ViewModelProvider.Factory 的 Map 中
}

// HiltViewModelFactory.java
@ActivityScoped
public class HiltViewModelFactory implements ViewModelProvider.Factory {
    private final Map<String, Provider<ViewModel>> viewModelMap;
    
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        String canonicalName = modelClass.getCanonicalName();
        Provider<ViewModel> provider = viewModelMap.get(canonicalName);
        if (provider == null) {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
        // 通过 Dagger Provider 创建，自动注入所有依赖
        return (T) provider.get();
    }
}
```

**工作流程**：
1. `@HiltViewModel` 触发 Hilt 的 APT 处理器，生成 `XXX_HiltModules` 模块。
2. 模块将 ViewModel 绑定到 `Map<String, Provider<ViewModel>>`，key 是全限定类名。
3. `HiltViewModelFactory`（`@ActivityScoped`）持有这个 Map，在 `create()` 时取出 `Provider` 创建实例。
4. 创建的 ViewModel 通过 `Provider.get()` 获得，Dagger 自动递归注入所有构造参数。

### 时序图说明

```
【@HiltViewModel 注入流程】

编译时：
@HiltViewModel annotation → Hilt APT 处理器
  └─ 生成 MyViewModel_HiltModules
      └─ @Module @InstallIn(ViewModelComponent)
          └─ @Binds @IntoMap bind(MyViewModel): ViewModel

运行时：
ViewModelProvider(activity).get(MyViewModel::class)
  └─ HiltViewModelFactory.create(MyViewModel.class)
      └─ viewModelMap.get("com.example.MyViewModel")
          └─ Provider<ViewModel>.get()
              └─ Dagger 构造 MyViewModel
                  ├─ @Inject constructor(repo: Repository)
                  │   └─ Dagger 自动解析 Repository 依赖
                  ├─ @Assisted SavedStateHandle（自动注入）
                  └─ 返回完整注入的 ViewModel 实例
```

### 面试加分点
- 指出 `ViewModelComponent` 是 Hilt 的特殊组件，生命周期跟随 ViewModel，拥有 `@ViewModelScoped` 作用域。
- `HiltViewModelFactory` 被注入到 `@AndroidEntryPoint` 的 Activity/Fragment，作为 `defaultViewModelProviderFactory` 替代默认工厂。
- 提及 `@Assisted` 注解：`SavedStateHandle` 不需要手动 `@Provides`，Hilt 内置 `SavedStateHandleAssistedFactory` 自动处理。
- 对比非 Hilt 场景：需要手动实现 `ViewModelProvider.Factory` 处理多参构造。

### 常见误区
- **误区**：使用 `@Inject` 注解 ViewModel 的构造函数就足够了。**正解**：还需要 `@HiltViewModel`，它触发额外的代码生成用于 ViewModel 注册。
- **误区**：`@ViewModelScoped` 的依赖可以在 Activity 间共享。**正解**：每个 ViewModel 实例都有独立的 ViewModelComponent 子组件，作用域限定在单个 ViewModel 实例内。
- **误区**：Hilt 无法处理不带 `SavedStateHandle` 的 ViewModel。**正解**：Hilt 的 `SavedStateHandleAssistedFactory` 已经处理了无 `SavedStateHandle` 的情况。

---

<a id="第9题viewmodel--kotlin-flow-最佳实践源码分析"></a>
### 第9题：ViewModel + Kotlin Flow 最佳实践源码分析

### 问题描述
`viewModelScope` 如何实现？`stateIn()/shareIn()` 在 ViewModel 中如何正确使用？

### 核心源码分析

```kotlin
// ViewModel.kt 中 viewModelScope 的实现
val ViewModel.viewModelScope: CoroutineScope
    get() {
        if (mBagOfTags == null) {
            mBagOfTags = HashMap()
        }
        var scope = mBagOfTags["androidx.lifecycle.viewmodel.internal.CloseableCoroutineScope"]
            as? CloseableCoroutineScope
        if (scope != null) return scope
        
        // 创建新 Scope，绑定 SupervisorJob + Main.immediate
        val newScope = CloseableCoroutineScope(
            SupervisorJob() + Dispatchers.Main.immediate
        )
        // 通过 addCloseable 注册到 mBagOfTags
        mBagOfTags["androidx.lifecycle.viewmodel.internal.CloseableCoroutineScope"] = newScope
        return newScope
    }

// ViewModel.clear() 时自动关闭
@MainThread
final override fun clear() {
    if (mBagOfTags != null) {
        for (value in mBagOfTags.values()) {
            closeWithRuntimeException(value)
        }
    }
    onCleared()
}
```

```kotlin
// stateIn 最佳实践
class MyViewModel(repo: MyRepository) : ViewModel() {
    // 错误：每次 collect 都重新订阅
    val uiState: StateFlow<UiState> = repo.observeData()
        .map { it.toUiState() }
        .stateIn(viewModelScope, SharingStarted.Eagerly, UiState.Loading)
    
    // 正确：stateIn 只在 viewModelScope 活跃时维持一个上游订阅
    // viewModelScope 在 onCleared 时自动取消
    // SharingStarted.WhileSubscribed(5000) → 订阅者消失后 5 秒停止上游
}
```

**关键设计**：
- `viewModelScope` 使用 `SupervisorJob()`：子协程异常不会取消其他协程。
- `Dispatchers.Main.immediate`：在主线程立即执行，避免不必要的调度延迟。
- 通过 `addCloseable("CloseableCoroutineScope", scope)` 注册，`clear()` 时自动遍历关闭。

### 时序图说明

```
【viewModelScope 生命周期】

MyViewModel 创建
  └─ viewModelScope 初始化（lazy）
      ├─ SupervisorJob() + Dispatchers.Main.immediate
      └─ 注册到 mBagOfTags["CloseableCoroutineScope"]

viewModelScope.launch { ... }
  └─ 协程在 SupervisorJob 下运行

Activity.finish()（非配置变更）
  └─ ViewModelStore.clear()
      └─ ViewModel.clear()
          └─ closeWithRuntimeException(scope)
              └─ scope.cancel()
                  ├─ SupervisorJob 取消
                  └─ 所有子协程自动取消（结构化并发）
          └─ onCleared()
```

### 面试加分点
- 指出 `stateIn()` 的 `SharingStarted.WhileSubscribed()` 策略：当没有订阅者时暂停上游流，节省资源。配合 `stopTimeoutMillis` 确保配置变更期间不重启。
- `shareIn()` 用于多订阅者共享上游，每个 `collect` 共用同一个冷流转换后的热流。
- 对比 `LiveData`：`Flow` 不是生命周期感知的，必须手动 `launch` 在 `viewModelScope` 或配合 `repeatOnLifecycle` 在 UI 层收集。
- 补充 `combine()` 的使用场景：多个 Flow 合并为一个 UI State。

### 常见误区
- **误区**：在 `init{}` 中 `launch` 协程但不使用 `viewModelScope`。**正解**：应使用 `viewModelScope.launch` 确保在 ViewModel 清理时自动取消。
- **误区**：在 Fragment/Activity 中使用 `lifecycleScope.launch { flow.collect }`。**正解**：应使用 `repeatOnLifecycle(Lifecycle.State.STARTED)` 确保在非活跃状态不收集数据。
- **误区**：`stateIn(Eagerly)` 是默认最佳选择。**正解**：`Eagerly` 立即启动上游且永不停止，可能浪费资源，推荐 `WhileSubscribed`。

---

<a id="第10题自定义-viewmodelproviderfactory-的高级用法"></a>
### 第10题：自定义 ViewModelProvider.Factory 的高级用法

### 问题描述
如何处理有参构造函数的 ViewModel？`AbstractSavedStateViewModelFactory` 解决了什么问题？

### 核心源码分析

```kotlin
// 方式一：实现 ViewModelProvider.Factory 接口
class MyFactory(private val repo: MyRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MyViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MyViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}

// 方式二：继承 AbstractSavedStateViewModelFactory
// 同时支持 SavedStateHandle 和 Application
class MySavedStateFactory(
    owner: SavedStateRegistryOwner,
    private val repo: MyRepository,
    defaultArgs: Bundle? = null
) : AbstractSavedStateViewModelFactory(owner, defaultArgs) {
    
    override fun <T : ViewModel> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        if (modelClass.isAssignableFrom(MyViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MyViewModel(repo, handle) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
```

```java
// AbstractSavedStateViewModelFactory.java 核心
public abstract class AbstractSavedStateViewModelFactory 
    extends ViewModelProvider.KeyedFactory {
    
    private final SavedStateRegistry mSavedStateRegistry;
    
    @NonNull
    @Override
    public final <T extends ViewModel> T create(@NonNull String key,
            @NonNull Class<T> modelClass) {
        // 1. 通过 SavedStateRegistry 消费 Controller 状态的 SavedStateProvider
        SavedStateHandleController controller = 
            SavedStateHandleController.create(mSavedStateRegistry, key, ...);
        // 2. 调用抽象方法，子类实现创建逻辑
        T viewModel = create(key, modelClass, controller.getHandle());
        // 3. 将 handle 关联到 ViewModel
        viewModel.addCloseable(key, controller);
        return viewModel;
    }
    
    // 子类需实现此方法
    protected abstract <T extends ViewModel> T create(
        @NonNull String key, @NonNull Class<T> modelClass, 
        @NonNull SavedStateHandle handle);
}
```

### 时序图说明

```
【AbstractSavedStateViewModelFactory 创建流程】

ViewModelProvider(activity, mySavedStateFactory).get("key", MyViewModel::class)
  └─ AbstractSavedStateViewModelFactory.create(key, MyViewModel.class)
      ├─ 1. SavedStateHandleController.create(registry, key, lifecycle)
      │     ├─ 在 SavedStateRegistry 注册 SavedStateProvider
      │     └─ 返回 SavedStateHandleController{handle}
      │
      ├─ 2. create(key, MyViewModel.class, handle) 
      │     └─ 返回 MyViewModel(repo, handle)
      │
      ├─ 3. viewModel.addCloseable("key", controller)
      │     └─ 确保 handle 与 ViewModel 生命周期绑定
      │
      └─ 4. 返回完整初始化的 ViewModel
```

### 面试加分点
- 对比 `ViewModelProvider.Factory` 和 `ViewModelProvider.KeyedFactory`：后者支持通过 `key` 区分同类型 ViewModel。
- 指出 `AbstractSavedStateViewModelFactory` 巧妙地将 `SavedStateRegistry` 的注册、恢复、保存完美封装，开发者只需实现 `create()`。
- 提及 Hilt 的 `@HiltViewModel` 是更推荐的方式，自动处理多参构造和 `SavedStateHandle`。

### 常见误区
- **误区**：所有多参 ViewModel 都必须用 `AbstractSavedStateViewModelFactory`。**正解**：不需要 `SavedStateHandle` 时，实现 `ViewModelProvider.Factory` 即可。
- **误区**：在 Factory 中创建 ViewModel 后忘记处理 `key`。**正解**：使用 `ViewModelProvider.KeyedFactory` 或 `AbstractSavedStateViewModelFactory` 可正确传递 `key`，支持同类型多实例。
- **误区**：Factory 内部持有 View 引用。**正解**：Factory 生命周期可能与 Activity 相同，应避免持有短生命周期引用。

---

## 二、LiveData 源码深度（8题）

<a id="第11题livedataobserve-完整链路"></a>
### 第11题：LiveData.observe() 完整链路

### 问题描述
调用 `liveData.observe(lifecycleOwner, observer)` 后发生了什么？生命周期感知是如何实现的？

### 核心源码分析

```java
// LiveData.java
@MainThread
public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<? super T> observer) {
    assertMainThread("observe");
    if (owner.getLifecycle().getCurrentState() == DESTROYED) {
        return; // 1. 已经 DESTROYED，忽略注册
    }
    // 2. 包装 Observer 为 LifecycleBoundObserver
    LifecycleBoundObserver wrapper = new LifecycleBoundObserver(owner, observer);
    ObserverWrapper existing = mObservers.putIfAbsent(observer, wrapper);
    if (existing != null && !existing.isAttachedTo(owner)) {
        throw new IllegalArgumentException("Cannot add same observer...");
    }
    if (existing != null) return;
    // 3. 注册到 Lifecycle
    owner.getLifecycle().addObserver(wrapper);
}
```

```java
// 内部关键类 LifecycleBoundObserver
class LifecycleBoundObserver extends ObserverWrapper implements LifecycleEventObserver {
    @NonNull final LifecycleOwner mOwner;
    
    @Override
    boolean shouldBeActive() {
        // 4. 生命周期 >= STARTED 时激活
        return mOwner.getLifecycle().getCurrentState().isAtLeast(STARTED);
    }
    
    @Override
    public void onStateChanged(@NonNull LifecycleOwner source,
            @NonNull Lifecycle.Event event) {
        Lifecycle.State currentState = mOwner.getLifecycle().getCurrentState();
        if (currentState == DESTROYED) {
            // 5. DESTROYED 时自动移除 Observer
            removeObserver(mObserver);
            return;
        }
        // 6. 同步 Lifecycle 状态变化到 ObserverWrapper
        activeStateChanged(shouldBeActive());
    }
}
```

```java
// ObserverWrapper 中的关键方法
void activeStateChanged(boolean newActive) {
    if (newActive == mActive) return;
    mActive = newActive;
    boolean wasInactive = LiveData.this.mActiveCount == 0;
    LiveData.this.mActiveCount += mActive ? 1 : -1;
    if (wasInactive && mActive) {
        onActive(); // 从无活跃 observer 变为有
    }
    if (LiveData.this.mActiveCount == 0 && !mActive) {
        onInactive();
    }
    if (mActive) {
        // 7. 激活时立即分发最新数据（这就是粘性事件的根源）
        dispatchingValue(this);
    }
}
```

**核心链路**：`observe()` → `LifecycleBoundObserver` → `Lifecycle.addObserver()` → `onStateChanged()` → `activeStateChanged()` → `dispatchingValue()`。

### 时序图说明

```
【observe 完整链路】

liveData.observe(lifecycleOwner, observer)
  ├─ 1. 主线程检查 assertMainThread()
  ├─ 2. Lifecycle 状态检查：DESTROYED → return
  ├─ 3. 创建 LifecycleBoundObserver(owner, observer)
  │     └─ 存入 mObservers: SafeIterableMap<Observer, ObserverWrapper>
  │
  ├─ 4. lifecycle.addObserver(wrapper)
  │     └─ LifecycleRegistry.addObserver()
  │         └─ 遍历 Observer 设置初始状态
  │             └─ wrapper.onStateChanged(owner, event)
  │
  └─ 5. onStateChanged → activeStateChanged
        └─ shouldBeActive() ? → STARTED+
            ├─ 是 → dispatchingValue(this) 
            │       └─ considerNotify(wrapper)
            │           ├─ 检查版本号 mLastVersion < mVersion
            │           └─ observer.onChanged(data)  // 分发粘性数据
            └─ 否 → 等待生命周期到达 STARTED
```

### 面试加分点
- 使用 `SafeIterableMap<Observer, ObserverWrapper>` 存储 observers，支持遍历过程中安全添加/移除 Observer。
- `LifecycleBoundObserver` 同时实现 `ObserverWrapper`（LiveData 内部）和 `LifecycleEventObserver`（Lifecycle 回调），是桥梁模式。
- `shouldBeActive()` 判断生命周期 >= STARTED，意味着 `onResume` 和 `onStart` 都处于 active 状态——这是 LiveData 的设计假设：UI 可见时才需要更新。
- `dispatchingValue()` 中的 `considerNotify()` 通过对比 `mVersion` 和 `mLastVersion` 决定是否通知，保证每个 observer 只收到一次。

### 常见误区
- **误区**：`onCreate` 中注册的 observer 立即收到数据。**正解**：收到 Lifecycle 状态同步和粘性事件，但只有在 `shouldBeActive()=true`（STARTED 之后）时才会触发 `dispatchingValue`。
- **误区**：`onDestroy` 时 LiveData 自动移除 observer。**正解**：是 `LifecycleBoundObserver.onStateChanged(DESTROYED)` 中调用 `removeObserver(observer)` 移除。
- **误区**：同一个 observer 可以绑定到多个 LifecycleOwner。**正解**：`putIfAbsent` 使用 observer 作为 key，第二次 observe 会抛异常。

---

<a id="第12题setvalue-vs-postvalue-源码对比"></a>
### 第12题：setValue() vs postValue() 源码对比

### 问题描述
`setValue()` 和 `postValue()` 有什么本质区别？为什么 `postValue()` 多次调用可能丢失数据？

### 核心源码分析

```java
// LiveData.java — setValue
@MainThread
protected void setValue(T value) {
    assertMainThread("setValue"); // 1. 强制主线程检查
    mVersion++; // 2. 版本号递增
    mData = value;
    dispatchingValue(null); // 3. 同步分发
}

// LiveData.java — postValue
protected void postValue(T value) {
    boolean postTask;
    synchronized (mDataLock) {
        postTask = mPendingData == NOT_SET; // 4. 是否已有待处理数据
        mPendingData = value; // 5. 直接覆盖旧值
    }
    if (!postTask) {
        return; // 6. 已有 pending 任务，用新值覆盖即可（旧值丢失）
    }
    // 7. 投递到主线程执行
    ArchTaskExecutor.getInstance().postToMainThread(mPostValueRunnable);
}

// mPostValueRunnable
private final Runnable mPostValueRunnable = new Runnable() {
    @Override
    public void run() {
        Object newValue;
        synchronized (mDataLock) {
            newValue = mPendingData;
            mPendingData = NOT_SET; // 8. 取出并重置
        }
        setValue((T) newValue); // 9. 最终调用 setValue
    }
};
```

**多线程下的 `postValue` 行为**：
```java
// 线程1: postValue("A")  → mPendingData="A", postTask=true, postToMainThread
// 线程2: postValue("B")  → mPendingData="B", postTask=false(已有), return
// 主线程: run() → newValue=mPendingData="B" → setValue("B")
// 结果："A" 被覆盖丢失，"B" 被分发
```

### 时序图说明

```
【setValue 流程】
setValue(data) [主线程]
  ├─ assertMainThread() ✓
  ├─ mVersion++
  ├─ mData = data
  └─ dispatchingValue(null) → 遍历所有 active observer
      └─ considerNotify() → observer.onChanged(data)

【postValue 流程】
postValue(data_A) [子线程A]
  ├─ synchronized(mDataLock) → mPendingData = data_A
  ├─ postTask = (mPendingData was NOT_SET? YES)
  └─ ArchTaskExecutor.postToMainThread(mPostValueRunnable)

postValue(data_B) [子线程B, 在 runnable 执行前]
  ├─ synchronized(mDataLock) → mPendingData = data_B (覆盖 A!)
  ├─ postTask = (mPendingData was NOT_SET? NO, 已有)
  └─ return (不投递新任务, 但 B 会覆盖 A)

主线程执行 runnable:
  └─ newValue = mPendingData = data_B
      └─ setValue(data_B) → dispatchingValue → observer.onChanged(data_B)
```

### 面试加分点
- `mPendingData == NOT_SET` 配合 `postTask` 标志，利用 `postTask` 确保每次只投递一个 runnable，但 `mPendingData` 会被后续值覆盖。
- `ArchTaskExecutor.getInstance().postToMainThread()` 的实现：实际上是 `mDelegate.postToMainThread(runnable)`，默认走 `Handler(Looper.getMainLooper()).post(runnable)`。
- `assertMainThread()` 通过 `Thread.currentThread() != Looper.getMainLooper().getThread()` 判断，违反时直接抛异常。
- 从源码可以看出 Google 的设计意图：子线程多次 `postValue` 只保留最后一次，避免主线程产生太多 `dispatchingValue` 调用。

### 常见误区
- **误区**：`postValue` 是异步的所以每个值都能被观察到。**正解**：`postValue` 的 runnable 在主线程执行前，后续的 `postValue` 会覆盖 `mPendingData`，中间值丢失。
- **误区**：子线程可以直接 `setValue`。**正解**：`setValue` 开头的 `assertMainThread()` 直接抛异常。
- **误区**：`postValue` 后立即读取 `getValue()` 可以拿到最新值。**正解**：`postValue` 尚未在主线程执行，`liveData.value` 仍是旧值。

---

<a id="第13题livedata-粘性事件解决方案源码分析"></a>
### 第13题：LiveData 粘性事件解决方案源码分析

### 问题描述
LiveData 的粘性事件是什么？为什么需要解决？有哪些源码级别的解决方案？

### 核心源码分析

**粘性事件的根源**：
```java
// ObserverWrapper.activeStateChanged()
if (mActive) {
    dispatchingValue(this); // 从 inactive 变 active 时，自动分发最新数据
}
```

**方案一：SingleLiveEvent（反射重置版本号）**
```kotlin
class SingleLiveEvent<T> : MutableLiveData<T>() {
    private val pending = AtomicBoolean(false)
    
    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        super.observe(owner, Observer { t ->
            if (pending.compareAndSet(true, false)) {
                observer.onChanged(t) // 消费后才回调真正的 observer
            }
        })
    }
    
    @MainThread
    override fun setValue(value: T) {
        pending.set(true)
        super.setValue(value)
    }
}
```

**方案二：Event Wrapper（Google 官方推荐）**
```kotlin
class Event<out T>(private val content: T) {
    private var hasBeenHandled = false
    
    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }
    
    fun peekContent(): T = content
}
```

### 时序图说明

```
【粘性事件问题场景】

Fragment 启动时 → observe(liveData)
  └─ activeStateChanged(true)
      └─ dispatchingValue(this)
          └─ observer.onChanged("上次的 SnackBar 消息")  ← 问题！
              └─ SnackBar 再次弹出（非用户预期）

【SingleLiveEvent 解决】
SingleLiveEvent.setValue("新消息")
  ├─ pending = AtomicBoolean(true)
  └─ super.setValue("新消息")

新 Observer 注册
  └─ observer.onChanged("新消息")
      └─ pending.compareAndSet(true, false) → true → 分发 ✓
  
旋转屏幕后重新 Observer 注册
  └─ observer.onChanged("新消息")
      └─ pending.compareAndSet(true, false) → false（已被消费）→ 不分发 ✓
```

### 面试加分点
- 区分两种粘性场景：① 事件类型（SnackBar/Navigation）不应粘性；② 状态类型（UI State）应该粘性。
- `SingleLiveEvent` 的 `AtomicBoolean` 保证线程安全，`compareAndSet` 确保单次消费。
- `Event Wrapper` 模式的 `getContentIfNotHandled()` 使用消费标志，比反射方案更纯粹。
- 指出更好的替代方案：使用 `Channel` 或 `SharedFlow` 替代 LiveData 发送一次性事件。

### 常见误区
- **误区**：所有 LiveData 都应该用 SingleLiveEvent。**正解**：只有一次性事件（SnackBar、导航、单次操作）需要，UI 状态数据需要粘性。
- **误区**：`SingleLiveEvent` 反射修改 `mVersion` 可以避免所有粘性问题。**正解**：`SingleLiveEvent` 不需要反射，它是通过 `AtomicBoolean` 消费标志实现的。
- **误区**：`observe` 一定会有粘性事件。**正解**：如果 LiveData 在 observe 前从未 `setValue`，`mVersion` 是 -1，`mLastVersion` 也是 -1，不会触发 `considerNotify`。

---

<a id="第14题mediatorlivedata-源码分析"></a>
### 第14题：MediatorLiveData 源码分析

### 问题描述
`MediatorLiveData` 如何管理多个数据源？添加和移除 Source 的内部原理是什么？

### 核心源码分析

```java
// MediatorLiveData.java
public class MediatorLiveData<T> extends MutableLiveData<T> {
    // 存储所有数据源及其 Observer
    private SafeIterableMap<LiveData<?>, Source<?>> mSources = new SafeIterableMap<>();
    
    public <S> void addSource(@NonNull LiveData<S> source, 
            @NonNull Observer<? super S> onChanged) {
        // 1. 创建 Source 并放入 Map
        Source<S> e = new Source<>(source, onChanged);
        Source<?> existing = mSources.putIfAbsent(source, e);
        if (existing != null && existing.mObserver != onChanged) {
            throw new IllegalArgumentException(
                "This source was already added with the different observer");
        }
        if (existing != null) return;
        
        // 2. 如果当前 MediatorLiveData 是活跃状态，启动观察
        if (hasActiveObservers()) {
            e.plug();
        }
    }
    
    public <S> void removeSource(@NonNull LiveData<S> remoteSource) {
        Source<?> source = mSources.remove(remoteSource);
        if (source != null) {
            source.unplug(); // 3. 从原 LiveData 移除 Observer
        }
    }
    
    // 内部 Source 类
    private static class Source<V> implements Observer<V> {
        final LiveData<V> mLiveData;
        final Observer<? super V> mObserver;
        int mVersion = START_VERSION;
        
        void plug() {
            mLiveData.observeForever(this); // 永久观察（不受 Lifecycle 限制）
        }
        
        void unplug() {
            mLiveData.removeObserver(this);
        }
        
        @Override
        public void onChanged(@Nullable V v) {
            if (mVersion != mLiveData.getVersion()) {
                mVersion = mLiveData.getVersion();
                mObserver.onChanged(v); // 代理到用户 Observer
            }
        }
    }
}
```

### 时序图说明

```
【addSource 添加流程】

mediatorLiveData.addSource(userLiveData) { user -> ... }
  ├─ 创建 Source(userLiveData, observer)
  ├─ mSources.putIfAbsent(userLiveData, source)
  │   └─ 已存在且 Observer 不同 → 抛异常
  │   └─ 已存在同 Observer → 忽略
  │   └─ 不存在 ↓
  └─ hasActiveObservers()? 
      ├─ true → source.plug()
      │   └─ userLiveData.observeForever(source) 
      │       └─ source.onChanged(data) → observer.onChanged(data)
      │           └─ mediatorLiveData.setValue(transformed)
      └─ false → 等待 MediatorLiveData 有活跃 observer 时才 plug

【removeSource 移除流程】
mediatorLiveData.removeSource(userLiveData)
  ├─ mSources.remove(userLiveData) → source
  └─ source.unplug()
      └─ userLiveData.removeObserver(source)
```

### 面试加分点
- `SafeIterableMap` 是 `MediatorLiveData` 存储多个 Source 的数据结构，支持遍历时安全增删。
- 使用 `observeForever()` 而非 `observe(lifecycleOwner)`，因为 `MediatorLiveData` 本身是生命周期感知的，不需要双重感知。
- `onActive()` / `onInactive()` 方法：当 MediatorLiveData 有/无活跃 observer 时，自动 plug/unplug 所有 Source。
- 版本号 `mVersion` 对比：`Source.onChanged()` 内部判断 `mVersion != mLiveData.getVersion()` 防止重复通知。

### 常见误区
- **误区**：同一个 LiveData 可以用不同 Observer 添加两次到 MediatorLiveData。**正解**：`putIfAbsent` 检查 source 作为 key，已存在时会抛异常或忽略。
- **误区**：`addSource` 后 LiveData 会一直在后台运行。**正解**：只有在 MediatorLiveData 有活跃 observer 时，`observeForever` 才生效。
- **误区**：`removeSource` 后数据源会立即停止更新。**正解**：`unplug()` 调用的 `removeObserver` 彻底解除观察。

---

<a id="第15题transformationsmap-和-switchmap-源码"></a>
### 第15题：Transformations.map() 和 switchMap() 源码

### 问题描述
`Transformations.map()` 和 `switchMap()` 的底层是如何实现的？`switchMap` 如何自动切换数据源？

### 核心源码分析

```java
// Transformations.java — map
@MainThread
public static <X, Y> LiveData<Y> map(
        @NonNull LiveData<X> source,
        @NonNull final Function<X, Y> mapFunction) {
    final MediatorLiveData<Y> result = new MediatorLiveData<>();
    result.addSource(source, new Observer<X>() {
        @Override
        public void onChanged(@Nullable X x) {
            result.setValue(mapFunction.apply(x)); // 转换后 set
        }
    });
    return result;
}

// Transformations.java — switchMap
@MainThread
public static <X, Y> LiveData<Y> switchMap(
        @NonNull LiveData<X> trigger,
        @NonNull final Function<X, LiveData<Y>> switchMapFunction) {
    final MediatorLiveData<Y> result = new MediatorLiveData<>();
    result.addSource(trigger, new Observer<X>() {
        LiveData<Y> mSource; // 持有上一次的数据源
        
        @Override
        public void onChanged(@Nullable X x) {
            LiveData<Y> newLiveData = switchMapFunction.apply(x);
            if (mSource == newLiveData) return; // 同一实例，跳过
            
            if (mSource != null) {
                result.removeSource(mSource); // 1. 移除旧的 LiveData
            }
            mSource = newLiveData;
            if (mSource != null) {
                result.addSource(mSource, new Observer<Y>() {
                    @Override
                    public void onChanged(@Nullable Y y) {
                        result.setValue(y); // 2. 转发新 LiveData 的值
                    }
                });
            }
        }
    });
    return result;
}
```

### 时序图说明

```
【map 工作流程】
sourceLiveData.setValue(User("Alice"))
  └─ MediatorLiveData.onChanged(User("Alice"))
      └─ mapFunction.apply(User("Alice")) → "Alice"
          └─ result.setValue("Alice") → observer 收到 "Alice"

【switchMap 工作流程】
userIdLiveData.setValue(1)
  └─ switchMapFunction.apply(1) → userRepo.getUserLiveData(1)  // newLiveData_1
      ├─ removeSource(mSource_old) [如果存在]
      ├─ mSource = newLiveData_1
      └─ addSource(newLiveData_1, ...) → 监听 newLiveData_1 的变化

userIdLiveData.setValue(2)  // 触发切换
  └─ switchMapFunction.apply(2) → userRepo.getUserLiveData(2)  // newLiveData_2
      ├─ removeSource(newLiveData_1) ← 自动移除旧数据源！
      ├─ mSource = newLiveData_2
      └─ addSource(newLiveData_2, ...) → 只监听新数据源的变化
```

### 面试加分点
- `map` 内部是 `MediatorLiveData` 的单数据源应用，`switchMap` 内部通过 `mSource` 保持对
旧数据源的引用并动态切换。
- `switchMap` 的 `removeSource` 是自动的：每次 `trigger` 值改变时，先移除旧的 `LiveData` 再添加新的，保证只有一个活跃数据源。
- 指出 `distinctUntilChanged()`（第16题）可配合使用，避免重复转换。
- `switchMap` 适用于场景：用户 ID 变化 → 自动切换查询该用户的数据。

### 常见误区
- **误区**：`switchMap` 内部使用 `observeForever` 会内存泄漏。**正解**：只在 `result` (MediatorLiveData) 活跃时才会添加，且切换时自动移除旧源。
- **误区**：`map` 和 `switchMap` 是函数式编程的纯函数。**正解**：它们操作的是有状态的 LiveData，`switchMap` 持有 `mSource` 引用，是带状态的。
- **误区**：`switchMap` 返回的 LiveData 会同时接收新旧两个数据源的值。**正解**：旧数据源被 `removeSource` 移除，不会再收到更新。

---

<a id="第16题livedata-的-distinctuntilchanged-原理"></a>
### 第16题：LiveData 的 distinctUntilChanged 原理

### 问题描述
LiveData 如何避免相同值重复分发？`mVersion` 版本号机制是怎样的？

### 核心源码分析

```java
// LiveData.java — 数据分发核心
void dispatchingValue(@Nullable ObserverWrapper initiator) {
    if (mDispatchingValue) {
        mDispatchInvalidated = true; // 防止重入
        return;
    }
    mDispatchingValue = true;
    do {
        mDispatchInvalidated = false;
        if (initiator != null) {
            // 单一 Observer 分发
            considerNotify(initiator);
            initiator = null;
        } else {
            // 遍历所有 Observer
            for (Iterator<Map.Entry<Observer<? super T>, ObserverWrapper>> iterator =
                    mObservers.iteratorWithAdditions(); iterator.hasNext(); ) {
                considerNotify(iterator.next().getValue());
                if (mDispatchInvalidated) break;
            }
        }
    } while (mDispatchInvalidated);
    mDispatchingValue = false;
}

// 关键：版本号比对
private void considerNotify(ObserverWrapper observer) {
    if (!observer.mActive) {
        return; // 1. 非活跃状态不通知
    }
    if (!observer.shouldBeActive()) {
        observer.activeStateChanged(false); // 再次检查活跃状态
        return;
    }
    // 2. 版本号判断：已经通知过的版本不再通知
    if (observer.mLastVersion >= mVersion) {
        return; // ← 这就是 distinctUntilChanged 的核心！
    }
    observer.mLastVersion = mVersion;
    // 3. 通知 Observer
    observer.mObserver.onChanged((T) mData);
}
```

**Object.equals() 对比**：
```java
// LiveData 的 setValue 中没有 equals 判断！
// distinctUntilChanged 完全依赖版本号机制，而非值比较：
setValue("Hello")
  ├─ mVersion: 1, mData: "Hello"
  └─ setValue("Hello")  // 相同值再次 set
      ├─ mVersion: 2, mData: "Hello"
      └─ considerNotify → mLastVersion(1) < mVersion(2) → 仍然分发！
```

**结论**：LiveData 的 `distinctUntilChanged` 是通过**版本号递增**机制实现的，而非**值相等**比较。每次 `setValue/postValue` 都会 `mVersion++`，但只有当 observer 的 `mLastVersion < mVersion` 时才会通知。

### 时序图说明

```
【相同值重复 setValue 的行为】

第一次 setValue("Hello")
  ├─ mVersion = 1
  ├─ mData = "Hello"
  └─ considerNotify(observer)
      ├─ observer.mLastVersion(-1) < mVersion(1) ✓
      └─ observer.onChanged("Hello")
          └─ observer.mLastVersion = 1

第二次 setValue("Hello")  ← 同样的值
  ├─ mVersion = 2
  ├─ mData = "Hello"
  └─ considerNotify(observer)
      ├─ observer.mLastVersion(1) < mVersion(2) ✓ ← 仍然通过！
      └─ observer.onChanged("Hello")  ← 相同值仍被分发
          └─ observer.mLastVersion = 2

object.equals 判断不会被触发
```

### 面试加分点
- 原生 LiveData 的 `distinctUntilChanged` 只是版本号递增对比，**不做值相等判断**——这是一个重要的源码细节。
- 需要真正的值去重，必须在 ViewModel 中手动判断：`if (newValue != currentValue) { _liveData.value = newValue }` 或在 `MediatorLiveData` 中添加 `distinctUntilChanged()` 逻辑。
- `mDispatchingValue` 标志防止分发过程中重入，`mDispatchInvalidated` 标志确保重入时的数据一致性（使用 do-while 循环重新分发）。
- 对比 Flow 的 `distinctUntilChanged()`：Flow 默认使用 `areItemsTheSame` 回调，默认用 `equals()` 做值对比。

### 常见误区
- **误区**：LiveData 默认使用值上的 `distinctUntilChanged`。**正解**：LiveData 每次 `setValue` 递增版本号，不做值相等比较。重复 set 相同值仍会通知。
- **误区**：在 `onChanged` 中对相同的值做 `if` 过滤就够了。**正解**：应在设置值之前做判断，避免无效的 `setValue` 导致不必要的 `dispatchingValue` 遍历开销。
- **误区**：`DistinctUntilChanged` 是 LiveData 的一个配置开关。**正解**：LiveData 没有此配置，需要自己实现。

---

<a id="第17题livedata-内存泄漏分析"></a>
### 第17题：LiveData 内存泄漏分析

### 问题描述
LiveData 有哪些典型的内存泄漏场景？`observeForever` 有什么风险？

### 核心源码分析

```java
// 正常情况：observe(lifecycleOwner, observer) — 自动清理
class LifecycleBoundObserver extends ObserverWrapper 
        implements LifecycleEventObserver {
    @Override
    public void onStateChanged(LifecycleOwner source, Lifecycle.Event event) {
        if (mOwner.getLifecycle().getCurrentState() == DESTROYED) {
            // DESTROYED 时自动 remove
            removeObserver(mObserver);
            return;
        }
        activeStateChanged(shouldBeActive());
    }
}

// 危险情况：observeForever — 永不自动移除
@MainThread
public void observeForever(@NonNull Observer<? super T> observer) {
    assertMainThread("observeForever");
    AlwaysActiveObserver wrapper = new AlwaysActiveObserver(observer);
    ObserverWrapper existing = mObservers.putIfAbsent(observer, wrapper);
    if (existing != null && existing instanceof LiveData.LifecycleBoundObserver) {
        throw new IllegalArgumentException("Cannot use observeForever...");
    }
    existing = mObservers.putIfAbsent(observer, wrapper);
    if (existing != null) return;
    wrapper.activeStateChanged(true); // 立即激活，不绑定 Lifecycle
}

// AlwaysActiveObserver
private class AlwaysActiveObserver extends ObserverWrapper {
    @Override
    boolean shouldBeActive() {
        return true; // 永远活跃，永远不会自动移除
    }
}
```

**泄漏场景分析**：
1. **Fragment 中的 observer 引用被 LifecycleBoundObserver 持有**：Fragment 销毁时 `onStateChanged(DESTROYED)` 自动清理，不会泄漏。
2. **observeForever + 忘记 removeObserver**：observer 持有的外部引用永远不会被释放。
3. **匿名内部类 observer 持有 Activity 引用**：即使 Fragment/Activity 销毁，若 `observeForever` 的 observer 未被移除，Activity 无法 GC。

### 时序图说明

```
【安全：observe(owner, observer)】
Fragment observe → LifecycleBoundObserver → Lifecycle.addObserver
  Fragment.onDestroy() 
    → LifecycleBoundObserver.onStateChanged(DESTROYED)
      → removeObserver(observer) ✓ 自动清理

【危险：observeForever(observer)】
Activity observeForever(observer)
  Activity.finish()
    → observer 仍在 LiveData.mObservers 中
    → observer 持有 Activity 引用（匿名内部类）
    → LiveData 被 ViewModel 持有 → ViewModel 未被清理
    → Activity 无法 GC ❌ 内存泄漏

【潜在泄漏：Fragment observe + 后台线程引用】
Fragment.observe(liveData) { data ->
    // this 隐式持有 Fragment 引用
}
// Fragment 已 onDestroy → LifecycleBoundObserver 已移除 ✓
// 但如果 Fragment 被 addToBackStack 后 detach
// → Fragment.mLifecycleRegistry 状态可能为 CREATED
// → shouldBeActive() = false → ObserverWrapper 保留在内存中
// → 持有 Fragment 引用，直到 ViewModel 清理
```

### 面试加分点
- `SafeIterableMap` 中的 `IteratorWithAdditions` 允许在遍历时安全删除，保证 `removeObserver` 不会 ConcurrentModificationException。
- ViewModel 中的 LiveData 生命周期与 ViewModel 一致：ViewModel.onCleared() 不会回收 LiveData 的 observer，但可以中断数据更新。
- 在测试中 `observeForever` 非常有用（因为不需要 LifecycleOwner），但必须在 `@After` 中 `removeObserver`。
- 配合 `viewModelScope` 使用 Flow 可以彻底避免此类泄漏。

### 常见误区
- **误区**：只要使用 `observe(lifecycleOwner)` 就绝对安全。**正解**：Fragment 在 back stack 中 detach 后，View 被销毁但 Fragment 实例存活，observer 仍持有 Fragment 引用。
- **误区**：`observeForever` 应该用在 Repository 层。**正解**：Repository 不应持有 UI 层的引用，`observeForever` 有泄漏风险，推荐使用 Flow。
- **误区**：`removeObserver` 后 observer 会被 GC。**正解**：还需要确保外部没有强引用持有 observer。如果 observer 是匿名内部类且持有外部类引用，外层对象也可能被阻止 GC。

---

<a id="第18题livedata-vs-flow-深度对比源码角度"></a>
### 第18题：LiveData vs Flow 深度对比（源码角度）

### 问题描述
从源码角度对比 LiveData 和 Kotlin Flow 的设计理念、线程模型和生命周期管理。

### 核心源码分析

**LiveData 核心设计**：
```java
// LiveData — 生命周期感知 + 主线程绑定
public abstract class LiveData<T> {
    volatile Object mData;       // 单值存储
    int mVersion;
    // 线程模型：setValue 绑 Main，postValue 走 Handler 投递
    // 生命周期：LifecycleBoundObserver → shouldBeActive() 判断
    // 运算符：Transformations.map/switchMap（数量有限）
}
```

**Flow 核心设计**：
```kotlin
// Flow — 协程原生 + 灵活调度
public interface Flow<out T> {
    suspend fun collect(collector: FlowCollector<T>)
}
// 冷流，每次 collect 重新执行
// 线程：通过 flowOn(Dispatchers.IO) 灵活切换
// 生命周期：配合 repeatOnLifecycle 实现安全收集
// 运算符：map/filter/combine/flatMapLatest 等 50+ 操作符
```

**完整的对比表**：

| 维度 | LiveData | Flow |
|------|----------|------|
| 线程模型 | 主线程（setValue 强制检查） | 协程调度，灵活指定 |
| 生命周期感知 | 内置（LifecycleBoundObserver） | 需配合 `repeatOnLifecycle` |
| 粘性事件 | 默认粘性（版本号机制） | `StateFlow` 粘性，`SharedFlow` 可控 |
| 冷/热流 | 热流（被观察者模式） | 默认冷流，`SharedFlow/StateFlow` 变热 |
| 操作符丰富度 | 仅 map/switchMap | 50+ 标准操作符 |
| 背压处理 | 无（漏桶：只保留最新值） | 丰富的背压策略（BUFFER/DROP/...） |
| 错误处理 | 无内置机制 | `catch`/`retry` 等 |

### 时序图说明

```
【LiveData 数据流】
Repository → LiveData.setValue() [Main Thread]
  └─ dispatchingValue(null)
      └─ LifecycleBoundObserver.onStateChanged()
          └─ shouldBeActive()? (lifecycle >= STARTED)
              └─ → observer.onChanged(data) [Main Thread]

【Flow 数据流 + repeatOnLifecycle】
Repository → Flow.flow { emit(data) } [Dispatchers.IO]
  └─ .flowOn(Dispatchers.IO)
  └─ lifecycleScope.launch {
      repeatOnLifecycle(STARTED) {
        flow.collect { data ->
          // 生命周期安全收集，Dispatcher 灵活
          updateUI(data)  // Main Thread
        }
      }
    }

// repeatOnLifecycle 源码行为
// 进入 STARTED → 启动收集
// 离开 STARTED → 取消收集（结构化并发）
// 重新进入 STARTED → 重新启动收集
```

### 面试加分点
- `repeatOnLifecycle(State.STARTED)` 的优势：协程被取消时释放资源，重新进入时重新订阅（比 LiveData 的无限期等待更高效）。
- `StateFlow` 与 LiveData 的对应：`StateFlow` 始终有值，支持 `distinctUntilChanged`，是 Flow 世界中最接近 LiveData 的类型。
- `SharedFlow` 的 `replay` 参数可以精确控制粘性事件的数量，而 LiveData 的粘性是 0 或 1。
- 最佳实践：ViewModel 中使用 `StateFlow` + `WhileSubscribed()`，UI 层使用 `repeatOnLifecycle` 收集。

### 常见误区
- **误区**：Flow 自动支持生命周期感知。**正解**：Flow 是协程框架的一部分，需要显式配合 `lifecycleScope` + `repeatOnLifecycle`。
- **误区**：`lifecycleScope.launch { flow.collect{} }` 在 onCreate 中启动就够了。**正解**：这种方式在 Fragment 进入 back stack 时不会取消，浪费资源。应该使用 `repeatOnLifecycle(STARTED)`。
- **误区**：Flow 一定比 LiveData 更好。**正解**：对于简单的单值 UI 状态，LiveData 更轻量和直观（DataBinding 直接支持）。Flow 在复杂数据转换场景更有优势。

---

## 三、Lifecycle 源码深度（6题）

<a id="第19题lifecycle-状态机模型源码"></a>
### 第19题：Lifecycle 状态机模型源码

### 问题描述
Lifecycle 的 State 和 Event 是如何转换的？状态机包含哪些核心状态和事件？

### 核心源码分析

```java
// Lifecycle.java — 5个 State
public enum State {
    DESTROYED,
    INITIALIZED,
    CREATED,
    STARTED,
    RESUMED;
    
    public boolean isAtLeast(@NonNull State state) {
        return compareTo(state) >= 0;
        // 例如：STARTED.isAtLeast(CREATED) = true
    }
}

// Lifecycle.java — 7个 Event
public enum Event {
    ON_CREATE, ON_START, ON_RESUME, ON_PAUSE, ON_STOP, ON_DESTROY, ON_ANY
}

// LifecycleRegistry.java — 状态转移核心
static State getStateAfter(Event event) {
    switch (event) {
        case ON_CREATE:
        case ON_STOP:
            return CREATED;
        case ON_START:
        case ON_PAUSE:
            return STARTED;
        case ON_RESUME:
            return RESUMED;
        case ON_DESTROY:
            return DESTROYED;
        case ON_ANY:
            break;
    }
    throw new IllegalArgumentException("Unexpected event: " + event);
}
```

```java
// 状态机图（源码中的 moveToState 逻辑）
//           ON_CREATE
//  INITIALIZED ────────→ CREATED
//                ON_START          ON_RESUME
//           ←──────────── STARTED ←───────── RESUMED
//                ON_PAUSE           ON_STOP
//
//  向下事件（upEvent）：
//  INITIALIZED → CREATED → STARTED → RESUMED
//
//  向上事件（downEvent）：
//  RESUMED → STARTED → CREATED → DESTROYED
```

### 时序图说明

```
【Lifecycle 状态转换图】

Activity/Fragment 生命周期：

         INITIALIZED ──────────────────────────────
              │                                     │
         ON_CREATE                              ON_DESTROY
              ↓                                     ↑
           CREATED ───────────────────── DESTROYED
              │                                     ↑
         ON_START                               ON_DESTROY
              ↓                                     │
           STARTED ←───────────────────────────────
              │         ON_STOP
         ON_RESUME
              ↓
           RESUMED ←────────── ON_PAUSE

【内部 moveToState 决策】
moveToState(targetState):
  while (mState != targetState):
    if (mState > targetState):   // 向下移动
      event = upEvent(mState)    // RESUMED → ON_PAUSE
    else:                         // 向上移动
      event = downEvent(mState)  // CREATED → ON_START
    mState = getStateAfter(event)
    sync()
```

### 面试加分点
- `isAtLeast()` 方法的巧妙设计：利用枚举的 `ordinal()` 做有序比较，`CREATED(1).isAtLeast(STARTED(2))` = false，`STARTED(2).isAtLeast(CREATED(1))` = true。
- 5 个 State + 7 个 Event 的非对称设计：State 是对生命周期的**分段描述**，Event 是**边界转换**。
- `ON_ANY` 的特殊性：不是真正的生命期事件，用于 `LifecycleEventObserver` 监听全部事件。
- `getStateAfter()` 的核心：`ON_CREATE` 和 `ON_STOP` 都映射到 `CREATED`（state 图是收敛的，不是一一映射）。

### 常见误区
- **误区**：`ON_START` 之后 State 变为 `STARTED`，`ON_STOP` 之后变为 `CREATED`。**正解**：`getStateAfter(ON_START)` = `STARTED` ✓；`getStateAfter(ON_STOP)` = `CREATED` ✓。很多开发者混淆事件和目标状态。
- **误区**：State 有 7 个，Event 有 5 个。**正解**：正好相反，State 5 个，Event 7 个（含 ON_ANY）。
- **误区**：直接从 `CREATED` 到 `DESTROYED` 需要 `ON_DESTROY` 事件。**正解**：是的，`getStateAfter(ON_DESTROY)` = `DESTROYED`。

---

<a id="第20题lifecycleregistry-核心实现"></a>
### 第20题：LifecycleRegistry 核心实现

### 问题描述
`LifecycleRegistry.moveToState()` 的实现中，如何保证状态同步？为什么需要 `mAddingObserverCounter`？

### 核心源码分析

```java
// LifecycleRegistry.java
public class LifecycleRegistry extends Lifecycle {
    private State mState = INITIALIZED;        // 当前状态
    private final WeakReference<LifecycleOwner> mLifecycleOwner;
    private FastSafeIterableMap<LifecycleObserver, ObserverWithState> mObserverMap;
    private int mAddingObserverCounter = 0;    // 正在添加的 Observer 计数
    
    // 核心：状态同步
    private void moveToState(State next) {
        if (mState == next) return;
        mState = next;
        // 如果正在添加 Observer 或正在同步，延后 sync
        if (mHandlingEvent || mAddingObserverCounter != 0) {
            mNewEventOccurred = true;
            return; // 等处理完毕再 sync
        }
        mHandlingEvent = true;
        sync();     // ← 核心同步逻辑
        mHandlingEvent = false;
    }
    
    // synced 状态判断
    private void sync() {
        LifecycleOwner lifecycleOwner = mLifecycleOwner.get();
        if (lifecycleOwner == null) {
            throw new IllegalStateException("LifecycleOwner is garbage collected");
        }
        while (!isSynced()) { // 循环直到状态同步
            mNewEventOccurred = false;
            // backwardPass vs forwardPass 的选择
            if (mState.compareTo(mObserverMap.eldest().getValue().mState) < 0) {
                backwardPass(lifecycleOwner); // 状态回退
            }
            // 前向同步
            Map.Entry<LifecycleObserver, ObserverWithState> newest = 
                mObserverMap.newest();
            if (!mNewEventOccurred && newest != null
                    && mState.compareTo(newest.getValue().mState) > 0) {
                forwardPass(lifecycleOwner); // 状态前进
            }
        }
        mNewEventOccurred = false;
    }
    
    // 是否已同步的判断
    private boolean isSynced() {
        if (mObserverMap.size() == 0) return true;
        State eldestObserverState = mObserverMap.eldest().getValue().mState;
        State newestObserverState = mObserverMap.newest().getValue().mState;
        // 所有 Observer 处于同一状态，且与 Registry 状态一致
        return eldestObserverState == newestObserverState && mState == newestObserverState;
    }
}
```

```java
// 前向传递（状态升级）
private void forwardPass(LifecycleOwner lifecycleOwner) {
    Iterator<Entry<LifecycleObserver, ObserverWithState>> ascendingIterator =
            mObserverMap.iteratorWithAdditions();
    while (ascendingIterator.hasNext() && !mNewEventOccurred) {
        Entry<LifecycleObserver, ObserverWithState> entry = ascendingIterator.next();
        while ((observer.mState.compareTo(mState) < 0) && !mNewEventOccurred
                && mObserverMap.contains(entry.getKey())) {
            pushParentState(observer.mState);
            final Event event = Event.upFrom(observer.mState); // CREATED → ON_START
            observer.dispatchEvent(lifecycleOwner, event);
            popParentState();
        }
    }
}
```

### 时序图说明

```
【sync() 同步流程】

moveToState(STARTED)
  ├─ mState = STARTED
  ├─ mHandlingEvent = true
  └─ sync()
      └─ while (!isSynced()):
          │
          ├─ backwardPass? 
          │   └─ mState < eldestObserver.mState → 回退
          │       └─ Observer 逐个收 ON_PAUSE/ON_STOP/...
          │
          └─ forwardPass?
              └─ mState > newestObserver.mState → 前进
                  └─ Observer 逐个收 ON_CREATE/ON_START
                      └─ Event.upFrom(observer.mState)
                          └─ CREATED + upFrom → ON_START
                              └─ observer.dispatchEvent(event)
                                  └─ 更新 observer.mState = STARTED
      
      └─ isSynced() → true → 退出循环
```

### 面试加分点
- `mAddingObserverCounter` 的作用：在 `addObserver()` 过程中，如果同步触发 `sync()` 会导致新添加的 Observer 状态不一致。计数器确保添加完毕后才统一同步。
- `FastSafeIterableMap`：自定义的双向链表 + HashMap 数据结构，同时支持快速遍历和安全插入。
- `backwardPass` 的处理顺序：从最老（eldest）到最新（newest），保证嵌套 Lifecycle 的内层先被通知。
- `forwardPass` 的处理顺序：从最老到最新，保证父 Observer 先升级状态，子 Observer 后升级。

### 常见误区
- **误区**：`forwardPass` 和 `backwardPass` 会同时执行。**正解**：`isSynced()` 保证每次循环只执行一种，直到所有 Observer 同步完成。
- **误区**：`sync()` 是递归的。**正解**：使用 `while` 循环而非递归，避免栈溢出。
- **误区**：Observers 状态的同步是立即的。**正解**：通过 `mNewEventOccurred` 标志处理同步过程中新事件的插入（如 observer 在 `onStart` 回调中又调用 `moveToState`）。

---

<a id="第21题processlifecycleowner-的实现原理"></a>
### 第21题：ProcessLifecycleOwner 的实现原理

### 问题描述
`ProcessLifecycleOwner` 如何判断应用在前台还是后台？为什么有 700ms 的延迟？

### 核心源码分析

```java
// ProcessLifecycleOwner.java
public class ProcessLifecycleOwner implements LifecycleOwner {
    static final long TIMEOUT_MS = 700; // 前后台切换的延迟时间
    
    // Activity 计数器
    private int mStartedCounter = 0;    // onStart 计数
    private int mResumedCounter = 0;    // onResume 计数
    
    // 处理 Activity 生命周期
    void activityStarted() {
        mStartedCounter++;
        if (mStartedCounter == 1 && mStopSent) {
            // 第一个 Activity onStart：应用从后台切到前台
            mRegistry.handleLifecycleEvent(Event.ON_START);
            mStopSent = false;
        }
    }
    
    void activityStopped() {
        mStartedCounter--;
        dispatchPauseOrStopIfNeeded();
    }
    
    // 700ms 延迟处理
    private void dispatchPauseOrStopIfNeeded() {
        if (mStartedCounter == 0) {
            mStopSent = true;
            mHandler.postDelayed(mDelayedStopRunnable, TIMEOUT_MS);
        }
    }
    
    // 延迟 Runnable
    private Runnable mDelayedStopRunnable = new Runnable() {
        @Override
        public void run() {
            // 二次确认：确保 700ms 内没有新的 Activity 启动
            if (mStartedCounter == 0 && mResumedCounter == 0) {
                mRegistry.handleLifecycleEvent(Event.ON_STOP);
            }
        }
    };
    
    // 前台判断
    public boolean isInForeground() {
        return mResumedCounter > 0;
    }
}
```

```java
// 注入方式：ReportFragment 回调
// 每个 Activity 的 onCreate 中，ReportFragment.injectIfNeededIn(activity)
// ReportFragment 接收生命周期回调后，分发给 ProcessLifecycleOwner
dispatch(activity, ON_START) → ProcessLifecycleOwner.activityStarted()
dispatch(activity, ON_STOP)  → ProcessLifecycleOwner.activityStopped()
```

### 时序图说明

```
【前后台切换流程】

【进入前台】
ActivityA.onCreate → counter 不变
ActivityA.onStart  → mStartedCounter = 1
  └─ mStartedCounter == 1 && mStopSent == true
      └─ handleLifecycleEvent(ON_START)  ← 通知前台
          └─ cancel mDelayedStopRunnable

【进入后台】
ActivityA.onStop  → mStartedCounter = 0
  └─ dispatchPauseOrStopIfNeeded()
      └─ mHandler.postDelayed(mDelayedStopRunnable, 700ms)
          │
          ├─ < 700ms 内有新 Activity 启动 → mStartedCounter > 0
          │   └─ cancel Runnable → 取消后台通知
          │
          └─ 700ms 后仍未启动新 Activity
              └─ run() → mStartedCounter == 0 → ON_STOP
```

### 面试加分点
- 700ms 延迟的设计原因：Activity 切换期间（如 A→B），A 先 onStop 再 B onCreate/onStart，若立即发送 ON_STOP，应用会在短暂时间内被误判为"后台"。
- `ReportFragment` 注入机制：`ProcessLifecycleOwner.init()` 通过 `application.registerActivityLifecycleCallbacks` 给每个 Activity 注入 `ReportFragment`，实现透明化的生命周期监听。
- 计数器机制的优势：`mStartedCounter` 和 `mResumedCounter` 确保多个 Activity 同时在前台时，只有全部停止才算后台。
- API 29+ 的优化：Android Q 引入 `Activity.getLifecycle()`，不再需要 `ReportFragment` 注入，但 `ProcessLifecycleOwner` 保持此机制以兼容旧版本。

### 常见误区
- **误区**：应用切到后台立即触发 ON_STOP。**正解**：有 700ms 延迟，用于避免 Activity 切换期间误判。
- **误区**：`isInForeground()` 等价于 `currentState == RESUMED`。**正解**：`isInForeground()` 判断的是 `mResumedCounter > 0`，即至少有一个 Activity 在 RESUMED 状态。
- **误区**：`ProcessLifecycleOwner` 可以直接在 Application.onCreate 中使用。**正解**：需要在调用 `ProcessLifecycleOwner.get().lifecycle` 之后，因为 `init()` 是懒加载的。

---

<a id="第22题lifecycleobserver-注解处理器"></a>
### 第22题：LifecycleObserver 注解处理器

### 问题描述
`@OnLifecycleEvent` 注解是如何被处理的？为什么推荐使用 `DefaultLifecycleObserver` 接口？

### 核心源码分析

```java
// 注解方式（已废弃）
class MyObserver : LifecycleObserver {
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() { }
    
    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() { }
}

// APT 生成的代码（GeneratedAdapter）
public class MyObserver_LifecycleAdapter implements GeneratedAdapter {
    final MyObserver mReceiver;
    
    @Override
    public void callMethods(LifecycleOwner source, Lifecycle.Event event,
            boolean onAny, MethodCallsLogger logger) {
        if (onAny) return;
        if (event == Lifecycle.Event.ON_RESUME) {
            mReceiver.onResume();
            return;
        }
        if (event == Lifecycle.Event.ON_PAUSE) {
            mReceiver.onPause();
            return;
        }
    }
}
```

```java
// LifecycleRegistry.addObserver() 中的查找逻辑
@Override
public void addObserver(@NonNull LifecycleObserver observer) {
    State initialState = mState == DESTROYED ? DESTROYED : INITIALIZED;
    ObserverWithState statefulObserver = new ObserverWithState(observer, initialState);
    ObserverWithState previous = mObserverMap.putIfAbsent(observer, statefulObserver);
    
    if (previous != null) return;
    
    LifecycleOwner lifecycleOwner = mLifecycleOwner.get();
    if (lifecycleOwner == null) return;
    
    // 关键：解析 Observer 类型
    boolean isReentrance = mAddingObserverCounter != 0 || mHandlingEvent;
    State targetState = calculateTargetState(observer);
    mAddingObserverCounter++;
    
    // 当 Observer 从初始状态提升到 targetState 的过程中，
    // ObserverWithState.dispatchEvent 会查找 GeneratedAdapter 或 DefaultLifecycleObserver
    while ((statefulObserver.mState.compareTo(targetState) < 0)
            && mObserverMap.contains(observer)) {
        pushParentState(statefulObserver.mState);
        statefulObserver.dispatchEvent(lifecycleOwner, 
            upEvent(statefulObserver.mState));
        popParentState();
        targetState = calculateTargetState(observer);
    }
    
    if (!isReentrance) sync();
    mAddingObserverCounter--;
}
```

```java
// ObserverWithState — 适配器选择优先级
static class ObserverWithState {
    State mState;
    LifecycleEventObserver mLifecycleObserver;
    
    ObserverWithState(LifecycleObserver observer, State initialState) {
        // 1. 优先：LifecycleEventObserver 接口（DefaultLifecycleObserver）
        if (observer instanceof LifecycleEventObserver) {
            mLifecycleObserver = (LifecycleEventObserver) observer;
        } else if (observer instanceof FullLifecycleObserver) {
            mLifecycleObserver = new FullLifecycleObserverAdapter(...);
        } else {
            // 2. 回退：GeneratedAdapter（注解方式）
            mLifecycleObserver = new ReflectiveGenericLifecycleObserver(observer);
        }
        mState = initialState;
    }
}
```

### 时序图说明

```
【Observer 类型判断优先级】

addObserver(myObserver)
  └─ new ObserverWithState(myObserver, INITIALIZED)
      └─ 判断 Observer 类型（优先级从高到低）:
          ├─ DefaultLifecycleObserver ✓ （直接适配，无反射）
          ├─ LifecycleEventObserver ✓ （直接适配）
          ├─ FullLifecycleObserver ✓ 
          └─ 其他 → ReflectiveGenericLifecycleObserver（反射 + APT 生成类查找）
              └─ 查找 MyObserver_LifecycleAdapter
                  └─ 找到 → 实例化 → callMethods()
                  └─ 未找到 → 用反射调用 @OnLifecycleEvent 注解方法
```

### 面试加分点
- APT 生成的 `GeneratedAdapter` 避免运行时反射，性能优于纯反射回退。
- `DefaultLifecycleObserver` 直接实现 `LifecycleEventObserver` 接口，无需任何反射或代码生成，性能最优。
- `ReflectiveGenericLifecycleObserver` 是先查 APT 生成的 Adapter，找不到才用反射（保底机制）。
- 指出 Java 8 接口默认方法使 `DefaultLifecycleObserver` 成为可能（所有方法都有默认空实现）。

### 常见误区
- **误区**：`DefaultLifecycleObserver` 也需要 APT。**正解**：它直接实现 `LifecycleEventObserver` 接口，完全不需要代码生成。
- **误区**：Kotlin 中可以用 `@OnLifecycleEvent`。**正解**：Kotlin 的 kapt 支持有限，推荐使用 `DefaultLifecycleObserver` 或 `LifecycleEventObserver`。
- **误区**：所有 Observer 都会走反射。**正解**：APT 生成代码>接口实现>反射回退的优先级链路，多数情况不走反射。

---

<a id="第23题reportfragment-的设计原理"></a>
### 第23题：ReportFragment 的设计原理

### 问题描述
为什么用 Fragment 来分发 Activity 生命周期，而不是直接在 Activity 回调中处理？

### 核心源码分析

```java
// ReportFragment.java
public class ReportFragment extends android.app.Fragment {
    
    public static void injectIfNeededIn(Activity activity) {
        if (Build.VERSION.SDK_INT >= 29) {
            // API 29+: 直接用 Activity.registerActivityLifecycleCallbacks
            LifecycleCallbacks.registerIn(activity);
        }
        // 兼容旧版：注入 Fragment
        android.app.FragmentManager manager = activity.getFragmentManager();
        if (manager.findFragmentByTag(REPORT_FRAGMENT_TAG) == null) {
            manager.beginTransaction()
                .add(new ReportFragment(), REPORT_FRAGMENT_TAG)
                .commit();
            manager.executePendingTransactions();
        }
    }
    
    @Override
    public void onResume() {
        super.onResume();
        dispatch(Lifecycle.Event.ON_RESUME);
        // 分发给 LifecycleRegistry
    }
    
    @Override
    public void onPause() {
        super.onPause();
        dispatch(Lifecycle.Event.ON_PAUSE);
    }
    // ... 其他生命周期方法同理
    
    private void dispatch(@NonNull Lifecycle.Event event) {
        if (Build.VERSION.SDK_INT < 29) {
            // 分发给当前 Activity 的 LifecycleRegistry
            dispatch(getActivity(), event);
        }
    }
    
    // 静态分发
    static void dispatch(@NonNull Activity activity, @NonNull Lifecycle.Event event) {
        if (activity instanceof LifecycleRegistryOwner) {
            ((LifecycleRegistryOwner) activity).getLifecycle()
                .handleLifecycleEvent(event);
        } else if (activity instanceof LifecycleOwner) {
            LifecycleRegistry registry = 
                ((LifecycleOwner) activity).getLifecycle();
            registry.handleLifecycleEvent(event);
        }
    }
}
```

### 时序图说明

```
【ReportFragment 注入和分发】

Activity.onCreate()
  └─ ComponentActivity.onCreate()
      └─ ReportFragment.injectIfNeededIn(this)
          ├─ API >= 29 → LifecycleCallbacks.registerIn(activity)
          │   └─ activity.registerActivityLifecycleCallbacks(callbacks)
          └─ API < 29 → 注入 ReportFragment
              └─ fragmentManager.add(ReportFragment)

Activity.onStart()
  └─ FragmentManager 调用 ReportFragment.onStart()
      └─ dispatch(ON_START)
          └─ activity.getLifecycle().handleLifecycleEvent(ON_START)
              └─ LifecycleRegistry.moveToState(STARTED)
                  └─ sync() → 通知所有 Observer

Activity.onStop()
  └─ FragmentManager 调用 ReportFragment.onStop()
      └─ dispatch(ON_STOP)
          └─ LifecycleRegistry.handleLifecycleEvent(ON_STOP)
              └─ moveToState(CREATED)
```

### 面试加分点
- Using Fragment 的核心原因：`Fragment` 的生命周期方法与 `Activity` 是**一一对应**的（包括 onStart/onResume/onPause/onStop/onDestroy），天然适配生命周期分发。
- 兼容性考虑：Android 生命周期 API 在 API 29 之前不完善，Fragment 注入方式可以兼容到 API 14+。
- 与 ProcessLifecycleOwner 的关系：每个 Activity 的 `ReportFragment` 不仅分发给自己的 `LifecycleRegistry`，还通知 `ProcessLifecycleOwner`（全局状态）。
- API 29+ 的优势：`LifecycleCallbacks` 无需注入 Fragment，避免了 FragmentTransaction 的开销。

### 常见误区
- **误区**：`ReportFragment` 会影响 UI 渲染。**正解**：不使用 `setContentView`，无任何 UI 元素。
- **误区**：手动移除 `ReportFragment` 可以禁用 Lifecycle。**正解**：可能导致 ViewModel 等依赖 Lifecycle 的组件行为异常。
- **误区**：`ReportFragment` 只在 API < 29 时使用。**正解**：API 29+ 依然注入，但主要用于 `ProcessLifecycleOwner` 的分发（其生命周期回调可能被移除，但注入逻辑保留）。

---

<a id="第24题自定义-lifecycleowner-的注意事项"></a>
### 第24题：自定义 LifecycleOwner 的注意事项

### 问题描述
如何手动实现一个 `LifecycleOwner`？有哪些常见的实现错误？

### 核心源码分析

```kotlin
// 标准自定义 LifecycleOwner 模板
class MyLifecycleOwner : LifecycleOwner {
    private val lifecycleRegistry = LifecycleRegistry(this)
    
    override val lifecycle: Lifecycle
        get() = lifecycleRegistry
    
    fun onCreate() {
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
    }
    
    fun onStart() {
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START)
    }
    
    fun onResume() {
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
    }
    
    fun onPause() {
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    }
    
    fun onStop() {
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_STOP)
    }
    
    fun onDestroy() {
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    }
}
```

```java
// LifecycleRegistry 内部的关键实现
public LifecycleRegistry(@NonNull LifecycleOwner provider) {
    this(provider, true);
}

private LifecycleRegistry(@NonNull LifecycleOwner provider, boolean enforceMainThread) {
    mLifecycleOwner = new WeakReference<>(provider); // 1. 弱引用持有 Owner！
    mState = INITIALIZED;
    mEnforceMainThread = enforceMainThread;
}
```

**关键注意事项**：
1. **WeakReference 陷阱**：`LifecycleRegistry` 内部使用 `WeakReference<LifecycleOwner>` 持有 Owner，如果 Owner 没有外部强引用，会被 GC 回收。
2. **线程安全**：默认 `mEnforceMainThread = true`，必须在主线程调用 `handleLifecycleEvent`。
3. **事件顺序**：必须按照正确的生命周期顺序调用 `handleLifecycleEvent`。
4. **addObserver 时机**：Observer 在 `addObserver()` 时会被同步到当前状态。

### 时序图说明

```
【正确的 LifecycleOwner 生命周期调用】

create:
  ├─ handleLifecycleEvent(ON_CREATE)
  │   └─ moveToState(CREATED)
  └─ addObserver(myObserver) ← 在 ON_CREATE 之后
      └─ Observer 被同步到 CREATED 状态
      └─ while(state < targetState) { dispatch upEvent }
          └─ ON_CREATE → CREATED
      
start:
  └─ handleLifecycleEvent(ON_START)
      └─ moveToState(STARTED)
      └─ sync() → Observer 升级到 STARTED

【错误的实现：事件顺序混乱】
handleLifecycleEvent(ON_RESUME)  // 跳过 ON_CREATE 和 ON_START！
  └─ moveToState(RESUMED)
  └─ getStateAfter(ON_RESUME) = RESUMED
  └─ while(mState < RESUMED) → 需要向前推进
      └─ 但 mState 是 INITIALIZED，ON_RESUME 被误用
      └─ 异常或状态不一致！
```

### 面试加分点
- `LifecycleRegistry` 的构造函数有一个 `enforceMainThread` 参数（非公开 API），测试场景可以关闭主线程检查。
- 指出 Kotlin 协程的 `lifecycleScope` 扩展：`val LifecycleOwner.lifecycleScope: LifecycleCoroutineScope` 依赖于 `lifecycle` 属性，会在 `ON_DESTROY` 时自动取消所有协程。
- `addObserver` 的回调时机：Observer 被添加后会立即同步到当前的 Lifecycle 状态（从 INITIALIZED 到当前状态的 upEvent 逐个触发）。
- `handleLifecycleEvent` 是幂等的吗？取决于当前状态和目标状态，如果已经处于更高状态则忽略。

### 常见误区
- **误区**：`LifecycleRegistry` 强引用 `LifecycleOwner`。**正解**：使用 `WeakReference`，如果 Owner 没有外部强引用，会被 GC 回收。
- **误区**：可以在任意线程调用 `handleLifecycleEvent`。**正解**：默认强制主线程，`assertMainThread()` 检查。
- **误区**：只需要在 `init{}` 中 `addObserver` 即可。**正解**：若在 `handleLifecycleEvent(ON_CREATE)` 之前添加，Observer 会被同步到 `CREATED` 状态（包括收到 ON_CREATE 事件）。
- **误区**：忘记实现 `LifecycleOwner` 接口，直接 new 一个 Lifecycle 对象。**正解**：必须通过 `LifecycleRegistry(this)` 关联 Owner。

---

## 四、Room 源码深度（8题）

<a id="第25题room-编译时注解处理器apt流程"></a>
### 第25题：Room 编译时注解处理器（APT）流程

### 问题描述
`@Entity`、`@Dao`、`@Database` 注解在编译时如何生成代码？生成的 `Impl` 类包含什么？

### 核心源码分析

```java
// Room 的 APT 处理入口（简化）
// RoomProcessor.java
public class RoomProcessor extends BasicAnnotationProcessor {
    @Override
    protected Iterable<? extends ProcessingStep> initSteps() {
        return Arrays.asList(
            new EntityProcessingStep(),    // 1. 处理 @Entity
            new DaoProcessingStep(),       // 2. 处理 @Dao
            new DatabaseProcessingStep()   // 3. 处理 @Database
        );
    }
}

// EntityProcessor — 生成表结构信息
// @Entity(tableName = "users") → 生成：
//   - 表名、列名、主键、外键、索引信息
//   - EntityInsertionAdapter / EntityDeletionOrUpdateAdapter
//   - 字段的 ColumnInfo 列表

// DaoProcessor — 生成 DAO 实现
// @Dao interface UserDao → 生成 UserDao_Impl
//   - 每个方法生成对应的 SQL 语句和执行逻辑
//   - @Insert → EntityInsertionAdapter.insert()
//   - @Query("SELECT * FROM users") → RoomSQLiteQuery + Cursor 解析

// DatabaseProcessor — 生成数据库实现
// @Database(entities = {User.class}, version = 1) 
// → 生成 AppDatabase_Impl extends RoomDatabase:
public class AppDatabase_Impl extends AppDatabase {
    @Override
    protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration config) {
        return config.sqliteOpenHelperFactory.create(...);
    }
    
    @Override
    protected InvalidationTracker createInvalidationTracker() {
        return new InvalidationTracker(this, "users", "posts");
    }
    
    @Override
    public UserDao userDao() {
        if (_userDao != null) return _userDao;
        // 懒加载：首次调用时创建 DAO 实现
        _userDao = new UserDao_Impl(this);
        return _userDao;
    }
}
```

### 时序图说明

```
【编译时处理流程】

源代码 (@Entity, @Dao, @Database)
  └─ javac / kotlinc 调用 APT 处理器
      └─ RoomProcessor.process()
          ├─ EntityProcessingStep
          │   └─ 收集所有 @Entity 类的表结构信息
          │       └─ 生成 EntityCursorConverter 等辅助类
          │
          ├─ DaoProcessingStep
          │   └─ 遍历所有 @Dao 接口
          │       └─ 对每个方法生成对应的 RoomSQLiteQuery 执行代码
          │           ├─ @Insert → EntityInsertionAdapter
          │           ├─ @Query → 解析 SQL 字符串 → 参数绑定 → Cursor 映射
          │           └─ @Transaction → beginTransaction/endTransaction 包裹
          │
          └─ DatabaseProcessingStep
              └─ 生成 XXX_Impl extends RoomDatabase
                  ├─ createOpenHelper() → 数据库文件创建
                  ├─ createInvalidationTracker() → 表变化监听
                  └─ clearAllTables() → 清空所有表的 SQL
```

### 面试加分点
- KSP（Kotlin Symbol Processing）相比 KAPT 更快，因为直接处理 Kotlin 源码级 AST，无需生成 Java stub。
- 生成的 `_Impl` 类继承自 `RoomDatabase`，dao 方法是懒加载的（首次调用时才 `new XXDao_Impl(this)`）。
- `RoomSQLiteQuery` 继承自 `SupportSQLiteQuery`，持有 SQL 字符串和参数数组，配合 `bindArgs()` 防止 SQL 注入。
- 数据库的 `clearAllTables()` 在生成的 `_Impl` 中自动生成所有 `DELETE FROM tableName` 语句。

### 常见误区
- **误区**：Room 运行时用反射创建 DAO 实例。**正解**：编译时生成 DAO 实现类，运行时直接 `new`，无反射开销。
- **误区**：KSP 与 KAPT 功能完全等价。**正解**：KSP 目前是实验性支持，部分高级 Room 特性可能不完全兼容。
- **误区**：修改 Entity 后只需重新编译。**正解**：需要同时升级数据库版本并提供 `Migration`，否则会 crash。

---

<a id="第26题room-dao-的-crud-代码生成"></a>
### 第26题：Room DAO 的 CRUD 代码生成

### 问题描述
`@Insert`、`@Update`、`@Delete`、`@Query` 分别生成了什么代码？参数是如何绑定的？

### 核心源码分析

```java
// @Insert 生成的代码（UserDao_Impl.java 片段）
@Override
public void insertUser(final User user) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
        __insertionAdapterOfUser.insert(user); // EntityInsertionAdapter
        __db.setTransactionSuccessful();
    } finally {
        __db.endTransaction();
    }
}

// @Query 生成的代码
@Override
public LiveData<List<User>> getUsersByAge(final int minAge) {
    final String _sql = "SELECT * FROM users WHERE age >= ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    // 参数绑定（防止 SQL 注入的关键）
    _statement.bindLong(1, minAge); // 索引从 1 开始

    return __db.getInvalidationTracker().createLiveData(
        new String[]{"users"}, false, 
        new Callable<List<User>>() {
            @Override
            public List<User> call() throws Exception {
                final Cursor _cursor = __db.query(_statement);
                try {
                    // Cursor 解析 → User 对象
                    final int _cursorIndexOfId = CursorUtil.getColumnIndex(_cursor, "id");
                    final int _cursorIndexOfName = CursorUtil.getColumnIndex(_cursor, "name");
                    final List<User> _result = new ArrayList<>();
                    while (_cursor.moveToNext()) {
                        final User _item = new User();
                        _item.id = _cursor.getLong(_cursorIndexOfId);
                        _item.name = _cursor.getString(_cursorIndexOfName);
                        _result.add(_item);
                    }
                    return _result;
                } finally {
                    _cursor.close();
                }
            }
        });
}
```

```java
// RoomSQLiteQuery.acquire() 的对象池机制
static RoomSQLiteQuery acquire(String query, int argumentCount) {
    // 从对象池中复用 RoomSQLiteQuery 实例，减少 GC 压力
    RoomSQLiteQuery sqLiteQuery = sQueryPool.acquire();
    if (sqLiteQuery == null) {
        sqLiteQuery = new RoomSQLiteQuery(argumentCount);
    }
    sqLiteQuery.init(query, argumentCount);
    return sqLiteQuery;
}
```

### 时序图说明

```
【@Insert 执行流程】

dao.insertUser(user)
  └─ UserDao_Impl.insertUser(user)
      ├─ __db.assertNotSuspendingTransaction()
      ├─ __db.beginTransaction()
      │   └─ SupportSQLiteDatabase.beginTransaction()
      │
      ├─ __insertionAdapterOfUser.insert(user)
      │   └─ EntityInsertionAdapter.insert(user)
      │       ├─ 生成 INSERT OR REPLACE INTO users(...) VALUES(...)
      │       ├─ bindArgs(statement, user)  // 绑定各字段
      │       └─ statement.executeInsert()
      │           └─ 返回 rowId
      │
      ├─ __db.setTransactionSuccessful()
      └─ __db.endTransaction()

【@Query 执行流程】
dao.getUsersByAge(18)
  └─ RoomSQLiteQuery.acquire(_sql, 1)  // 从对象池获取
      ├─ _statement.bindLong(1, 18)  // 参数绑定
      └─ __db.query(_statement)
          └─ Cursor 遍历 → User 对象映射
              └─ _statement.release()  // 归还对象池
```

### 面试加分点
- `RoomSQLiteQuery` 使用对象池（`sQueryPool`）减少频繁创建 SQL 对象的 GC 开销。
- `bindArgs` 的类型安全：`bindLong`、`bindString` 等根据参数类型自动选择正确的绑定方法。
- `@Insert` 自动包裹事务：生成代码中包含 `beginTransaction()` 和 `endTransaction()`，多行插入在单个事务中完成。
- `@Delete` 和 `@Update` 使用 `EntityDeletionOrUpdateAdapter`，内部通过主键匹配进行 WHERE 条件拼接。

### 常见误区
- **误区**：`@Query` 的返回值不能是 `Flow`。**正解**：Room 2.2+ 支持 `Flow`，内部通过 `InvalidationTracker` 自动监听。
- **误区**：`@Insert` 返回值 `void` 和 `Long` 是一样的实现。**正解**：`Long` 返回值时会包装 `executeInsert()` 返回的 rowId，`void` 则忽略。
- **误区**：`@Update` 会更新所有列。**正解**：只更新 `@ColumnInfo` 注解的列，默认生成 `UPDATE SET field1=?, field2=?` 包含所有非主键字段。

---

<a id="第27题room-数据库版本迁移migration源码"></a>
### 第27题：Room 数据库版本迁移（Migration）源码

### 问题描述
Room 的 `Migration` 如何执行？从 v1 到 v3 的跨版本迁移如何合并？

### 核心源码分析

```java
// RoomOpenHelper.java
public class RoomOpenHelper extends SupportSQLiteOpenHelper.Callback {
    private DatabaseConfiguration mConfiguration;
    private List<Migration> mMigrations;
    
    @Override
    public void onUpgrade(SupportSQLiteDatabase db, int oldVersion, int newVersion) {
        boolean migrated = false;
        // 查找从 oldVersion 到 newVersion 的完整迁移链
        List<Migration> migrations = findMigrationPath(oldVersion, newVersion);
        if (migrations != null) {
            // 按版本顺序依次执行 Migration
            for (Migration migration : migrations) {
                migration.migrate(db);
            }
            migrated = true;
        }
        // 未找到完整路径且需要自动迁移
        if (!migrated && mAutoMigrationSpecs != null) {
            // 尝试 AutoMigration
        }
        if (!migrated) {
            // 没找到迁移方案且未设置 fallbackToDestructiveMigration
            throw new IllegalStateException(
                "A migration from " + oldVersion + " to " + newVersion + " was required...");
        }
    }
    
    // 关键：查找迁移路径
    private List<Migration> findMigrationPath(int start, int end) {
        if (start == end) return Collections.emptyList();
        
        // 深度优先搜索：找到从 start 到 end 的完整路径
        for (Migration migration : mMigrations) {
            if (migration.startVersion == start) {
                // 递归查找后续路径
                List<Migration> subPath = findMigrationPath(migration.endVersion, end);
                if (subPath != null) {
                    List<Migration> result = new ArrayList<>();
                    result.add(migration);
                    result.addAll(subPath);
                    return result;
                }
            }
        }
        return null; // 找不到完整路径
    }
}
```

```kotlin
// 典型的多版本 Migration 链
Room.databaseBuilder(context, AppDatabase::class.java, "app.db")
    .addMigrations(
        MIGRATION_1_2,  // startVersion=1, endVersion=2
        MIGRATION_2_3,  // startVersion=2, endVersion=3
    )
    .build()

// findMigrationPath(1, 3) 的搜索过程：
// 1. 尝试 MIGRATION_1_2(start=1, end=2) → 递归 findMigrationPath(2, 3)
// 2. 找到 MIGRATION_2_3(start=2, end=3) → 到达终点
// 3. 返回 [MIGRATION_1_2, MIGRATION_2_3]
```

### 时序图说明

```
【数据库升级：v1 → v3】

AppDatabase_Impl.openHelper.onUpgrade(db, oldVersion=1, newVersion=3)
  ├─ findMigrationPath(1, 3)
  │   ├─ DFS 搜索：
  │   │   MIGRATION_1_2(1→2) + findMigrationPath(2,3)
  │   │   └─ MIGRATION_2_3(2→3) + findMigrationPath(3,3)
  │   │       └─ return []
  │   │   → 返回 [MIGRATION_1_2, MIGRATION_2_3]
  │   └─ 验证路径完整性
  │
  ├─ for each Migration in path:
  │   ├─ MIGRATION_1_2.migrate(db)
  │   │   └─ db.execSQL("ALTER TABLE users ADD COLUMN age INTEGER")
  │   └─ MIGRATION_2_3.migrate(db)
  │       └─ db.execSQL("CREATE TABLE posts (...)")
  │
  └─ 路径不完整 → IllegalStateException or destructiveMigration
```

### 面试加分点
- `findMigrationPath` 使用 DFS 而非简单的线性查找，支持复杂的多分支迁移图（如 1→2, 1→3, 2→3 等）。
- `destructiveMigrationOnDowngrade()` 和 `fallbackToDestructiveMigration()` 作为容错回退，重建数据库。
- Room 2.2+ 引入 `AutoMigration`，通过 `@AutoMigration` 注解声明式处理表结构变化，减少手动编写 SQL 的工作量。
- `Migration` 中的 `migrate` 方法接收 `SupportSQLiteDatabase`，支持 `execSQL` 执行原生 SQL。

### 常见误区
- **误区**：每个版本变化只需要一个 Migration 文件。**正解**：每个版本跨度一个，必须形成完整链路。
- **误区**：数据库降级（downgrade）自动走 Migration 反向执行。**正解**：默认降级会 crash，必须显式调用 `fallbackToDestructiveMigration()` 或提供专门的降级 Migration。
- **误区**：Migration 只需要建表语句就够了。**正解**：还需要考虑数据迁移（INSERT INTO newTable SELECT ...），否则数据丢失。

---

<a id="第28题room-事务处理原理"></a>
### 第28题：Room 事务处理原理

### 问题描述
`@Transaction` 注解的内部原理是什么？嵌套事务如何处理？

### 核心源码分析

```java
// @Transaction 注解的编译处理
// 在 DaoProcessor 中，如果方法有 @Transaction 注解：
@Override
public void insertWithTransaction(final User user, final List<Post> posts) {
    __db.beginTransaction();
    try {
        // 方法体内的所有 DAO 操作都在同一事务中
        __insertionAdapterOfUser.insert(user);
        __insertionAdapterOfPost.insert(posts);
        __db.setTransactionSuccessful();
    } finally {
        __db.endTransaction();
    }
}

// RoomDatabase.java — 事务方法
public void beginTransaction() {
    assertNotMainThread(); // 禁止主线程事务
    SupportSQLiteDatabase db = mOpenHelper.getWritableDatabase();
    db.beginTransaction(); // 内部：mDatabase.beginTransaction()
}

@Deprecated
public void endTransaction() {
    mOpenHelper.getWritableDatabase().endTransaction();
}

public void setTransactionSuccessful() {
    mOpenHelper.getWritableDatabase().setTransactionSuccessful();
}

// 支持 suspend 函数的事务
suspend fun <R> withTransaction(block: suspend () -> R): R {
    // 使用挂起事务，协程取消时自动回滚
    val transactionExecutor = mTransactionExecutor
    // ...
}
```

```java
// Android 原生 SQLite 的嵌套事务处理
// SQLiteDatabase.java
public void beginTransaction() {
    if (mTransactionListener != null) {
        mTransactionListener.onBegin();
    }
    // 嵌套事务：记录嵌套层级，只有第一次真正 begin
    if (mNumTransactions == 0) {
        executeSql("BEGIN EXCLUSIVE");
    }
    mNumTransactions++; // 嵌套计数器
    mInnerTransactionIsSuccessful = false;
}

public void endTransaction() {
    if (mTransactionListener != null) {
        mTransactionListener.onCommit();
    }
    mNumTransactions--;
    if (mNumTransactions == 0) {
        // 只有最外层才真正 COMMIT 或 ROLLBACK
        if (mInnerTransactionIsSuccessful) {
            executeSql("COMMIT");
        } else {
            executeSql("ROLLBACK");
        }
    } else {
        // 内层事务：标记成功标志
        mInnerTransactionIsSuccessful = true;
    }
}
```

### 时序图说明

```
【事务执行流程】

@Transaction
insertWithTransaction(user, posts)
  ├─ __db.beginTransaction()
  │   └─ SupportSQLiteDatabase.beginTransaction()
  │       └─ SQLiteDatabase.executeSql("BEGIN EXCLUSIVE")
  │           └─ mNumTransactions = 1
  │
  ├─ insertUser(user)
  │   └─ EntityInsertionAdapter.insert()
  │       └─ statement.executeInsert()  // 在事务中
  │
  ├─ insertPosts(posts) [循环插入]
  │   └─ 每个 post 的 INSERT 在同一事务中
  │
  ├─ __db.setTransactionSuccessful()
  │   └─ mInnerTransactionIsSuccessful = true
  │
  └─ __db.endTransaction()
      └─ mNumTransactions = 0 → executeSql("COMMIT")
```

### 面试加分点
- `@Transaction` 注解的方法自动包裹在 `beginTransaction` / `endTransaction` 之间。
- `mNumTransactions` 计数器实现嵌套事务：Android 原生的 SQLite 不支持真正的嵌套事务，使用计数器模拟。只有最外层决定 COMMIT/ROLLBACK。
- `withTransaction` (suspend) 的优势：协程取消时自动 ROLLBACK，避免数据不一致。
- `assertNotMainThread()` 检查（非 suspend 事务）：主线程执行事务可能导致 ANR。

### 常见误区
- **误区**：SQLite 支持真正的嵌套事务（SAVEPOINT）。**正解**：Android 的 `SQLiteDatabase.beginTransaction()` 使用计数器模拟，不支持真正的 SAVEPOINT 嵌套。
- **误区**：`@Transaction` 可以用在 `@Query` 上。**正解**：`@Query` 是只读查询，不需要事务，`@Transaction` 主要用于 `@Insert`/`@Update`/`@Delete` 组合操作。
- **误区**：事务中抛异常会自动 `ROLLBACK`。**正解**：是的，因为 `finally { endTransaction() }` 中 `mInnerTransactionIsSuccessful` 未被设置为 true，执行 ROLLBACK。

---

<a id="第29题room-的-typeconverter-源码分析"></a>
### 第29题：Room 的 TypeConverter 源码分析

### 问题描述
Room 如何将一个复杂对象（如 `Date` 或自定义类型）存储到 SQLite 并读取回来？

### 核心源码分析

```java
// 用户定义的 TypeConverter
class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }
    
    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.let { it.time }
    }
}

// Room 编译时生成的代码
@Database(entities = [User::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase()

// 生成的 UserDao_Impl 中使用 TypeConverter
@Override
public void insertUser(final User user) {
    // 绑定 Date 列时调用转换
    final Long _tmp = __converters.dateToTimestamp(user.getCreatedAt());
    _statement.bindLong(4, _tmp); // 将 Date → Long 存入数据库
}

@Override
public User mapCursorToUser(Cursor cursor) {
    final Long _tmp = cursor.getLong(cursorIndexOfCreatedAt);
    // 读取时调用反向转换
    user.setCreatedAt(__converters.fromTimestamp(_tmp)); // Long → Date
}
```

```java
// TypeConverter 的注册流程（编译时）
// RoomProcessor 收集所有 @TypeConverters 注解
// 在生成的 Database_Impl 中：
public class AppDatabase_Impl extends AppDatabase {
    private Converters __converters; // TypeConverter 实例
    
    @Override
    protected void createOpenHelper(DatabaseConfiguration config) {
        // 将 TypeConverter 注册到 DatabaseConfiguration
        config.typeConverters.addAll(
            Arrays.asList(Converters.class.getDeclaredMethods())
        );
    }
}
```

### 时序图说明

```
【TypeConverter 注册和使用】

编译时：
@TypeConverters(Converters::class) → RoomProcessor
  └─ 扫描 Converters 中所有 @TypeConverter 方法
      ├─ fromTimestamp(Long → Date)   → 列类型 Long → Kotlin 类型 Date
      └─ dateToTimestamp(Date → Long) → Kotlin 类型 Date → 列类型 Long
      └─ 记录在 DatabaseConfiguration.typeConverters 中

运行时写入：
dao.insertUser(User(name="Alice", createdAt=Date(now)))
  └─ UserDao_Impl.insertUser()
      └─ __converters.dateToTimestamp(user.createdAt) → Long
          └─ _statement.bindLong(index, longValue) → 存入数据库

运行时读取：
Cursor → User 映射
  └─ cursor.getLong("createdAt") → Long
      └─ __converters.fromTimestamp(longValue) → Date
          └─ user.createdAt = date
```

### 面试加分点
- TypeConverter 的查找是基于类型匹配的：输入/输出类型对应 SQLite 支持的类型（Long/String/Double/ByteArray 等）。
- 多个 Converter 类：`@TypeConverters` 可以标注在 `@Database`、`@Entity`、`@Dao`、`@Query` 等不同级别，就近原则查找。
- `@ProvidedTypeConverter` (Room 2.5+) 允许通过依赖注入提供 Converter，而不是写静态方法。
- 枚举类型的特殊处理：如果枚举有 `@TypeConverter`，自动转换；否则 Room 2.1+ 可以使用内置的枚举转换器。

### 常见误区
- **误区**：TypeConverter 可以转换任意类型。**正解**：中间类型必须是 SQLite 支持的基础类型（int/long/float/double/String/byte[]）。
- **误区**：TypeConverter 方法名影响匹配。**正解**：只看输入/输出类型和方法上的 `@TypeConverter` 注解。
- **误区**：需要在每个 DAO 上标注 `@TypeConverters`。**正解**：标注在 `@Database` 上即可全局生效。

---

<a id="第30题room-与-flow-集成原理"></a>
### 第30题：Room 与 Flow 集成原理

### 问题描述
`@Query` 返回 `Flow<List<T>>` 时，Room 如何感知数据变化并自动重新发射数据？

### 核心源码分析

```java
// Room 生成的代码：返回 Flow 的 @Query
@Override
public Flow<List<User>> getAllUsers() {
    final String _sql = "SELECT * FROM users";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    
    return CoroutinesRoom.createFlow(__db, new String[]{"users"}, 
        new Callable<List<User>>() {
            @Override
            public List<User> call() throws Exception {
                // Lambda：实际执行查询并返回结果
                final Cursor _cursor = __db.query(_statement);
                try {
                    return /* Cursor → List<User> 映射 */;
                } finally {
                    _cursor.close();
                }
            }
        });
}

// CoroutinesRoom.java — Flow 创建核心
@JvmStatic
public <T> Flow<T> createFlow(
        RoomDatabase database, 
        String[] tableNames, 
        Callable<T> callable) {
    return flow {
        // 1. 首先生成初始数据
        emit(callable.call()); // 立即执行查询
        
        // 2. 创建挂起回调（等待表变化）
        val channel = Channel<Unit>(Channel.CONFLATED)
        
        // 3. 注册 InvalidationTracker.Observer
        val observer = object : InvalidationTracker.Observer(tableNames) {
            override fun onInvalidated(tables: MutableSet<String>) {
                channel.trySend(Unit) // 表变化时通知 Channel
            }
        }
        
        // 4. 循环：等待变化 → 重新查询 → 发射数据
        database.invalidationTracker.addObserver(observer)
        try {
            for (unit in channel) {
                emit(callable.call()) // 数据变化时重新查询并发射
            }
        } finally {
            database.invalidationTracker.removeObserver(observer)
        }
    }
}
```

```java
// InvalidationTracker.java — 表级别变化监听
public class InvalidationTracker {
    // 每当有 INSERT/UPDATE/DELETE 操作时调用
    public void refreshAsync(String[] tables) {
        for (String table : tables) {
            // 通知该表的所有 Observer
            mTableObservers.notifyByTable(table);
        }
    }
}
```

### 时序图说明

```
【Flow 数据流】

getAllUsers().collect { users ->
    updateUI(users)
}
  ├─ CoroutinesRoom.createFlow()
  │   └─ flow {
  │       ├─ emit(callable.call())  ← 立即执行查询，发射初始数据
  │       │
  │       ├─ InvalidationTracker.addObserver(observer)
  │       │   └─ 监听 "users" 表的变化
  │       │
  │       └─ for (unit in channel) {
  │             emit(callable.call())  ← 表变化时重新查询
  │           }
  │       }

INSERT INTO users (name) VALUES ('Bob')
  └─ InvalidationTracker.refreshAsync(["users"])
      └─ observer.onInvalidated({"users"})
          └─ channel.trySend(Unit)  ← 触发重新查询
              └─ emit(callable.call())
                  └─ 新数据发射 → UI 更新
```

### 面试加分点
- `InvalidationTracker` 使用 `tableName` 级别监听（非行级别），意味着任何对该表的写操作都触发重查询。
- 使用 `Channel.CONFLATED` 作为背压策略：多次快速变化合并为一次通知，减少不必要的重复查询。
- `CoroutinesRoom.createFlow` 中的 `try-finally` 保证 Observer 在 Flow 被取消时自动移除。
- 对比 LiveData 集成：两者底层都依赖 `InvalidationTracker`，但 Flow 在协程中处理，LiveData 在主线程。

### 常见误区
- **误区**：Room 返回的 Flow 是在后台线程执行查询。**正解**：`createFlow` 中 `callable.call()` 在 `flow{}` 构建器中执行，继承父协程的调度器，需要在外部指定 `flowOn(Dispatchers.IO)` 切到后台线程。
- **误区**：Room Flow 在数据没有变化时也会定期轮询。**正解**：完全事件驱动，只在 `InvalidationTracker` 收到表变化通知时才触发。
- **误区**：Room Flow 是热流，不 `collect` 也会查询。**正解**：`flow{}` 构建器是冷流，只有 `collect` 时才启动。但要配合 `shareIn/stateIn` 转换为热流。

---

<a id="第31题room-数据库的-wal-模式"></a>
### 第31题：Room 数据库的 WAL 模式

### 问题描述
Room 默认使用 WAL 模式吗？WAL 相比传统 Journal Mode 有什么优势？

### 核心源码分析

```java
// RoomDatabase.java — 数据库打开配置
public static class Builder<T extends RoomDatabase> {
    // JournalMode 配置
    private JournalMode mJournalMode = JournalMode.AUTOMATIC;
    
    public Builder<T> setJournalMode(@NonNull JournalMode journalMode) {
        mJournalMode = journalMode;
        return this;
    }
}

// JournalMode 枚举
public enum JournalMode {
    AUTOMATIC,      // 系统自动选择（API 16+）
    TRUNCATE,       // 传统日志模式
    WRITE_AHEAD_LOGGING  // WAL 模式
}

// RoomOpenHelper.java — 实际的 Journal Mode 设置
public class RoomOpenHelper extends SupportSQLiteOpenHelper.Callback {
    @Override
    public void onConfigure(SupportSQLiteDatabase db) {
        super.onConfigure(db);
        // 设置 WAL 模式
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            db.setJournalMode(JournalMode.WRITE_AHEAD_LOGGING);
        }
    }
}
```

```java
// SupportSQLiteDatabase — WAL 设置
// FrameworkSQLiteDatabase.java (Android 原生实现)
@Override
public void setJournalMode(@NonNull JournalMode journalMode) {
    if (journalMode == JournalMode.WRITE_AHEAD_LOGGING) {
        // 执行 PRAGMA journal_mode=WAL
        if (!query("PRAGMA journal_mode=WAL").moveToFirst()) {
            Log.w(TAG, "Could not set WAL mode");
        }
        // 可选：设 WAL 检查点大小
        execSQL("PRAGMA wal_autocheckpoint=100");
    }
}
```

**WAL vs Journal Mode 对比**：

| 特性 | Journal Mode | WAL Mode |
|------|-------------|----------|
| 并发读写 | 写时阻塞读 | 读写不阻塞 |
| 性能 | 单次写入快 | 多次写入更快 |
| 文件 | db + db-journal | db + db-wal + db-shm |
| 回滚 | 覆盖原始文件 | 移除 WAL 条目 |
| 内存占用 | 低 | 需要 WAL 内存 |

### 时序图说明

```
【WAL 并发读写示意】

Journal Mode:
Thread_Read  ──────────[等待...]──[读]──
Thread_Write ──[BEGIN]──[写]──[COMMIT]──
              ↑ 读被写阻塞

WAL Mode:
Thread_Read  ──[读]────────────────────
Thread_Write ──[写 WAL 文件]──────────[CHECKPOINT 合并到主 DB]
              ↑ 读直接读主 DB + WAL，不被阻塞
```

### 面试加分点
- Android 9 (API 28) 及以上，`SQLiteDatabase` 默认开启 WAL 模式。Room 的 `JournalMode.AUTOMATIC` 会在 `onConfigure` 中设置 WAL。
- WAL 的 `-wal` 和 `-shm` 文件可能占用额外磁盘空间，CHECKPOINT 会将 WAL 合并回主数据库文件。
- Room 的 `JournalMode.TRUNCATE` 可强制使用传统模式，适用于特定场景（如需要多进程访问同一 DB 文件，WAL 不支持多进程）。
- WAL 不适合的场景：极小的、纯追加的 SQL 操作（如只用 INSERT），因为 WAL 文件管理的开销比收益大。

### 常见误区
- **误区**：Room 始终是 WAL 模式。**正解**：可以通过 `setJournalMode(TRUNCATE)` 改为传统日志模式。
- **误区**：WAL 模式下不需要考虑数据库文件损坏。**正解**：WAL 文件损坏也会导致数据问题，CHECKPOINT 过程中断电可能损坏主数据库。
- **误区**：删除 `-wal` 文件可以清理数据库空间。**正解**：可能导致数据丢失，应使用 `PRAGMA wal_checkpoint(TRUNCATE)` 安全清理。

---

<a id="第32题room-与-livedata-集成"></a>
### 第32题：Room 与 LiveData 集成

### 问题描述
为什么 Room 返回的 `LiveData` 不需要手动切换线程？底层是如何实现的？

### 核心源码分析

```java
// Room 生成的 @Query 返回 LiveData 的代码
@Override
public LiveData<List<User>> getAllUsers() {
    final String _sql = "SELECT * FROM users";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    
    // 关键：通过 InvalidationTracker 创建 LiveData
    return __db.getInvalidationTracker().createLiveData(
        new String[]{"users"},  // 监听的表
        false,                   // inTransaction
        new Callable<List<User>>() {
            @Override
            public List<User> call() throws Exception {
                // 实际查询在后台线程执行
                final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
                try {
                    return /* Cursor → List<User> */;
                } finally {
                    _cursor.close();
                }
            }
        });
}

// InvalidationTracker.java — LiveData 创建
public <T> LiveData<T> createLiveData(String[] tableNames, 
        boolean inTransaction, Callable<T> computeFunction) {
    return new RoomTrackingLiveData<>(this, tableNames, inTransaction, computeFunction);
}

// RoomTrackingLiveData.java — 后台执行查询
class RoomTrackingLiveData<T> extends LiveData<T> {
    final Callable<T> mComputeFunction;
    
    @Override
    protected void onActive() {
        super.onActive();
        // 当有活跃 Observer 时，在后台线程执行查询
        mDatabase.getQueryExecutor().execute(mRefreshRunnable);
    }
    
    final Runnable mRefreshRunnable = new Runnable() {
        @Override
        public void run() {
            // 后台线程中执行计算
            T value = mComputeFunction.call(); // Callable.call()
            // 主线程设置值
            postValue(value); // 使用 postValue，因为当前在后台线程
        }
    };
    
    @Override
    protected void onInactive() {
        super.onInactive();
        mDatabase.getQueryExecutor().removeCallbacks(mRefreshRunnable);
    }
}
```

### 时序图说明

```
【Room + LiveData 线程模型】

Fragment.observe(viewModel.users) { list ->
    // list 一定在主线程
}

// 底层执行：
InvalidationTracker.createLiveData(...)
  └─ RoomTrackingLiveData
      └─ onActive() → 有活跃 observer
          └─ mDatabase.getQueryExecutor().execute(runnable)
              └─ [后台线程池] runnable.run()
                  ├─ mComputeFunction.call()
                  │   └─ __db.query(_statement)
                  │       └─ Cursor → List<User>
                  └─ liveData.postValue(list)
                      └─ postToMainThread → setValue → observer.onChanged()
                          └─ [主线程] observer 收到数据

数据变化时：
INSERT INTO users (...)
  └─ InvalidationTracker.refreshAsync(["users"])
      └─ mDatabase.getQueryExecutor().execute(mRefreshRunnable)
          └─ [后台线程] 重新查询
              └─ postValue(newList) → observer.onChanged() [主线程]
```

### 面试加分点
- `getQueryExecutor()` 使用的是 Room 内置的 `ArchTaskExecutor` 或自定义 `QueryExecutor`，默认在后台线程执行数据库操作。
- `postValue()` 将结果从后台线程投递到主线程，这就是 LiveData 自动线程切换的秘密。
- `onActive/onInactive` 保证只在有 UI Observer 时执行查询，节省资源。
- 与直接返回 `Flow` 的对比：`LiveData` 方式对 Java 项目更友好，`Flow` 对协程项目更方便。

### 常见误区
- **误区**：Room 返回的 LiveData 在调用线程执行查询。**正解**：内部使用 `QueryExecutor` 在后台线程执行，通过 `postValue` 切回主线程。
- **误区**：可以手动 `setValue` 修改 Room 返回的 LiveData。**正解**：Room 返回的是只读 LiveData，不应尝试修改。
- **误区**：Room 中返回 LiveData 和 Flow 的代码底层实现完全不同。**正解**：都基于 `InvalidationTracker`，只是包装方式不同。

---

## 五、Navigation 源码深度（5题）

<a id="第33题navigation-的导航图navgraph构建流程"></a>
### 第33题：Navigation 的导航图（NavGraph）构建流程

### 问题描述
XML 中定义的 `navigation` 资源如何被解析为 `NavGraph` 对象？`NavInflater` 如何处理嵌套图？

### 核心源码分析

```java
// NavInflater.java — 核心解析
public class NavInflater {
    private Context mContext;
    private Resources mResources;
    
    public NavGraph inflate(@NavigationRes int graphResId) {
        XmlResourceParser parser = mResources.getXml(graphResId);
        // 解析根节点 <navigation>
        AttributeSet attrs = Xml.asAttributeSet(parser);
        int type;
        while ((type = parser.next()) != XmlPullParser.START_TAG
                && type != XmlPullParser.END_DOCUMENT) { }
        
        String rootElement = parser.getName();
        NavDestination destination = inflate(parser, attrs, graphResId); // 递归解析
        if (!(destination instanceof NavGraph)) {
            throw new IllegalArgumentException("Root element must be NavGraph");
        }
        return (NavGraph) destination;
    }
    
    // 递归解析每个元素
    private NavDestination inflate(XmlResourceParser parser, 
            AttributeSet attrs, int graphResId) {
        NavDestination destination = createDestination(parser.getName());
        destination.onInflate(mContext, attrs);
        
        // 解析子元素
        int innerType;
        while ((innerType = parser.next()) != XmlPullParser.END_DOCUMENT) {
            if (innerType == XmlPullParser.START_TAG) {
                // 递归解析嵌套元素
                NavDestination child = inflate(parser, attrs, graphResId);
                if (child instanceof NavGraph) {
                    ((NavGraph) destination).addDestination(child);
                }
            } else if (innerType == XmlPullParser.END_TAG) {
                if (parser.getName().equals(destination.getDisplayName())) {
                    return destination;
                }
            }
        }
        return destination;
    }
    
    // 根据标签名创建对应的 NavDestination
    private NavDestination createDestination(String type) {
        switch (type) {
            case TAG_FRAGMENT: return new FragmentNavigator.Destination(this);
            case TAG_ACTIVITY: return new ActivityNavigator.Destination(this);
            case TAG_NAVIGATION: return new NavGraph(this); // 嵌套图
            case TAG_INCLUDE: return inflate(resId); // <include> 递归
            default: 
                // 查找自定义 Navigator 的 destination
                Navigator<?> navigator = mNavigatorProvider.getNavigator(type);
                return navigator.createDestination();
        }
    }
}
```

### 时序图说明

```
【NavGraph 解析流程】

navController.setGraph(R.navigation.main_graph)
  └─ NavInflater.inflate(R.navigation.main_graph)
      └─ XML Parser 解析：
      
          <navigation startDestination="@id/home">
            ├─ <fragment id="home" />  → FragmentNavigator.Destination("home")
            ├─ <fragment id="detail">  → FragmentNavigator.Destination("detail")
            │   └─ <argument name="itemId" type="integer" />
            │       └─ 生成 NavArgument(integer, null, false)
            │
            ├─ <include graph="@navigation/settings_graph" />
            │   └─ inflate(R.navigation.settings_graph)  ← 递归解析嵌套图
            │       └─ NavGraph("settings")
            │           ├─ <fragment id="settings_main" />
            │           └─ <fragment id="settings_detail" />
            │
            └─ <action id="to_detail" destination="@id/detail" />
                └─ 生成 NavAction(id=to_detail, destId=detail)
      
      最终构建：NavGraph (main_graph)
        ├─ home (FragmentDestination)
        ├─ detail (FragmentDestination)
        │   └─ arguments: {itemId: NavArgument}
        ├─ settings (NavGraph)  ← 嵌套子图
        │   ├─ settings_main
        │   └─ settings_detail
        └─ actions: {to_detail: NavAction}
```

### 面试加分点
- `NavInflater` 使用 `XmlPullParser` 做流式解析而非 DOM，节省内存。
- `<include>` 标签递归调用 `inflate()` 实现图的分模块化。
- `NavigatorProvider` 通过 `getNavigator(name)` 支持自定义导航器（如 Bottom Navigation 的自定义 Navigator）。
- `NavGraph` 本身也是 `NavDestination`，所以支持图的嵌套（嵌套导航图）。

### 常见误区
- **误区**：`<include>` 是引用拷贝，不改变图结构。**正解**：被 include 的图作为 `NavGraph` 节点被加入，是真正的子图。
- **误区**：`NavInflater` 预加载所有 Fragment。**正解**：解析时只创建 Destination 数据结构，Fragment 在导航时才实例化。
- **误区**：自定义 Navigator 必须用特定标签名。**正解**：通过 `NavigatorProvider.addNavigator()` 注册后可 `@Navigator.Name` 绑定到任意 XML 标签名。

---

<a id="第34题navcontroller-的导航栈管理"></a>
### 第34题：NavController 的导航栈管理

### 问题描述
`NavController.navigate()` 内部如何管理导航栈？`popBackStack()` 如何恢复到上一个目的地？

### 核心源码分析

```java
// NavController.java — 导航栈
public class NavController {
    // 核心：双端队列实现的回退栈
    final Deque<NavBackStackEntry> mBackStack = new ArrayDeque<>();
    private NavigatorProvider mNavigatorProvider;
    
    // navigate() 核心实现
    public void navigate(@IdRes int resId, @Nullable Bundle args,
            @Nullable NavOptions navOptions) {
        navigate(resId, args, navOptions, null);
    }
    
    private void navigate(int resId, Bundle args, NavOptions navOptions,
            Navigator.Extras navigatorExtras) {
        // 1. 查找目标 Destination
        NavDestination destination = mGraph.findNode(resId);
        if (destination == null) {
            throw new IllegalArgumentException("Navigation destination not found");
        }
        
        // 2. 获取对应的 Navigator
        Navigator<NavDestination> navigator = 
            mNavigatorProvider.getNavigator(destination.getNavigatorName());
        
        // 3. 准备 NavBackStackEntry
        Bundle finalArgs = destination.addInDefaultArgs(args);
        NavBackStackEntry newEntry = new NavBackStackEntry(
            mContext, destination, finalArgs, mLifecycleOwner, this);
        
        // 4. Navigator 执行实际导航（如 FragmentTransaction）
        navigator.navigate(newEntry, navOptions, navigatorExtras);
        
        // 5. 管理回退栈
        if (navOptions != null && navOptions.shouldLaunchSingleTop()) {
            // popUpTo 逻辑：清除指定目标之上的所有 entry
            popBackStackInternal(navOptions.getPopUpTo(), navOptions.isPopUpToInclusive());
        }
        
        // 6. 更新生命周期
        newEntry.updateState();
    }
    
    // popBackStack 实现
    public boolean popBackStack() {
        if (mBackStack.isEmpty()) return false;
        
        // 弹出最后一个 entry
        NavBackStackEntry popped = mBackStack.pollLast();
        // 恢复前一个 entry 的状态
        NavBackStackEntry previous = mBackStack.peekLast();
        if (previous != null) {
            previous.updateState(); // 恢复到 STARTED/RESUMED
        }
        // Navigator 执行实际的回退（如 FragmentManager.popBackStack()）
        Navigator<NavDestination> navigator = 
            mNavigatorProvider.getNavigator(popped.getDestination().getNavigatorName());
        navigator.popBackStack(popped, true);
        return true;
    }
}
```

### 时序图说明

```
【导航栈操作示意】

初始状态：HomeFragment
  mBackStack: [HomeEntry(RESUMED)]

navigate(R.id.detail, args)
  ├─ 1. 查找 Destination → FragmentDestination("detail")
  ├─ 2. FragmentNavigator.navigate()
  │   └─ FragmentTransaction
  │       ├─ replace(R.id.container, DetailFragment)
  │       └─ addToBackStack("detail")
  ├─ 3. new NavBackStackEntry(detail, args)
  ├─ 4. mBackStack.push(detailEntry)
  └─ 5. 生命周期更新
        HomeEntry: RESUMED → STARTED
        detailEntry: CREATED → STARTED → RESUMED
  
  mBackStack: [HomeEntry(STARTED), detailEntry(RESUMED)]

popBackStack()
  ├─ 1. mBackStack.pollLast() → detailEntry
  ├─ 2. previous = HomeEntry
  │   └─ HomeEntry.updateState() → RESUMED
  ├─ 3. FragmentNavigator.popBackStack()
  │   └─ FragmentManager.popBackStack()
  └─ 4. detailEntry 生命周期 → DESTROYED
  
  mBackStack: [HomeEntry(RESUMED)]
```

### 面试加分点
- `ArrayDeque` 双端队列作为回退栈，支持 LIFO 的 push/pop 操作。
- `NavBackStackEntry` 实现了 `LifecycleOwner` 接口，其生命周期由 `NavController` 管理：进入栈顶时为 `RESUMED`，弹出时为 `DESTROYED`。
- `navigate` 中通过 `NavOptions` 支持 `singleTop`、`popUpTo` 等高级栈策略。
- `popBackStack(destinationId, inclusive)` 可以弹出到指定目的地，而非只弹一个。

### 常见误区
- **误区**：`NavController.popBackStack()` 等价于 `FragmentManager.popBackStack()`。**正解**：`NavController.popBackStack()` 先管理栈状态和生命周期，然后委托给 `Navigator.popBackStack()`。
- **误区**：导航栈不受进程重建影响。**正解**：`NavController.saveState()` → `Bundle`，`restoreState()` 恢复栈，但需要主动保存/恢复。
- **误区**：每次 `navigate` 都会创建新的 Fragment 实例。**正解**：默认创建新实例。配合 `singleTop` 或使用 `navGraphViewModels` 共享 ViewModel 可复用已有实例。

---

<a id="第35题safe-args-的类型安全原理"></a>
### 第35题：Safe Args 的类型安全原理

### 问题描述
Safe Args 如何保证参数传递的类型安全？它与传统 Bundle 方式有什么本质区别？

### 核心源码分析

```xml
<!-- navigation.xml 中声明 argument -->
<fragment
    android:id="@+id/detailFragment"
    android:name="com.example.DetailFragment">
    <argument
        android:name="itemId"
        app:argType="integer" />
    <argument
        android:name="itemName"
        app:argType="string"
        android:defaultValue="Unknown" />
</fragment>
```

```java
// Safe Args 插件生成的代码（编译时）
// DetailFragmentArgs.java
public class DetailFragmentArgs implements NavArgs {
    private final int itemId;
    private final String itemName;
    
    // 私有构造，只能通过 Builder 创建
    DetailFragmentArgs(int itemId, @Nullable String itemName) {
        this.itemId = itemId;
        this.itemName = itemName;
    }
    
    // 从 Bundle 读
    public static DetailFragmentArgs fromBundle(Bundle bundle) {
        DetailFragmentArgs result = new DetailFragmentArgs(
            bundle.getInt("itemId"),
            bundle.getString("itemName")
        );
        // 编译时已知类型，无需运行时类型检查
        return result;
    }
    
    // 写回 Bundle
    public Bundle toBundle() {
        Bundle bundle = new Bundle();
        bundle.putInt("itemId", this.itemId);
        bundle.putString("itemName", this.itemName);
        return bundle;
    }
    
    // Builder 模式
    public static class Builder {
        private int itemId;
        private String itemName = "Unknown";
        
        public Builder setItemId(int itemId) {
            this.itemId = itemId;
            return this;
        }
        
        public Builder setItemName(String itemName) {
            this.itemName = itemName;
            return this;
        }
        
        public DetailFragmentArgs build() {
            return new DetailFragmentArgs(itemId, itemName);
        }
    }
}

// 生成的导航 Action 类
// NavGraphDirections.java
public class NavGraphDirections {
    public static NavDirections actionToDetail(int itemId, String itemName) {
        return new ActionOnlyNavDirections(R.id.action_to_detail) {
            // 内部持有 Bundle ...
        };
    }
}
```

### 时序图说明

```
【Safe Args 使用流程】

发送方：
val args = DetailFragmentArgs.Builder()
    .setItemId(42)
    .setItemName("Hello")
    .build()
navController.navigate(R.id.detailFragment, args.toBundle())

接收方（DetailFragment）：
val args = DetailFragmentArgs.fromBundle(requireArguments())
val itemId: Int = args.itemId      // 编译时类型安全
val itemName: String = args.itemName

【对比传统 Bundle 方式】
// 不安全：运行时 key 拼写错误、类型不对
val itemId = bundle.getInt("iteemId")  // 拼写错误，返回 0，无编译错误
val itemName = bundle.getString("itemId")  // 用了错误的 key
```

### 面试加分点
- Safe Args 是编译时代码生成，生成的 `Args` 类在编译时就确定了类型，避免了 Bundle 的运行时类型不安全问题。
- 支持的数据类型：integer, string, boolean, float, long, reference, parcelable, serializable, enum。
- `defaultValue` 在生成代码的 `Builder` 中直接作为默认值，`fromBundle` 缺少参数时也使用它。
- 配合 `@Parcelize` 或 `Parcelable` 的传参，Safe Args 也能保证序列化类型的正确性。

### 常见误区
- **误区**：Safe Args 完全替代了 Bundle。**正解**：底层仍然使用 Bundle 传递参数，Safe Args 只是编译时包装确保类型安全。
- **误区**：Safe Args 不需要声明参数类型。**正解**：XML 中必须声明 `argType`，编译时才知道生成的 getter/setter 类型。
- **误区**：接收方可以修改 Safe Args 构造的参数。**正解**：生成的 `Args` 类字段是 `final`，不可修改。

---

<a id="第36题deep-link-在-navigation-中的处理"></a>
### 第36题：Deep Link 在 Navigation 中的处理

### 问题描述
Navigation 组件如何处理 Deep Link？隐式和显式 Deep Link 的区别是什么？

### 核心源码分析

```xml
<!-- 声明 Deep Link -->
<fragment
    android:id="@+id/deepLinkFragment"
    android:name="com.example.DeepLinkFragment">
    <deepLink
        android:id="@+id/deepLink"
        app:uri="myapp://detail/{itemId}" />
    <!-- 也支持多个 deepLink -->
</fragment>
```

```java
// NavDeepLink.java — Deep Link 解析
public class NavDeepLink {
    private final String mUriPattern;
    private final Pattern mPattern; // 编译好的正则表达式
    
    // 匹配 URI
    public boolean matches(@NonNull Uri deepLink) {
        Matcher matcher = mPattern.matcher(deepLink.toString());
        return matcher.matches();
    }
    
    // 提取参数
    public Bundle getMatchingArguments(@NonNull Uri deepLink, 
            @NonNull Map<String, NavArgument> arguments) {
        Bundle bundle = new Bundle();
        // 从 URI 中提取路径参数
        Matcher matcher = mPattern.matcher(deepLink.toString());
        if (matcher.matches()) {
            // myapp://detail/42 → {itemId: "42"}
            for (String paramName : mParamNames) {
                String value = matcher.group(paramName);
                // 根据声明的 argType 转换类型
                NavArgument argument = arguments.get(paramName);
                // ... 类型转换放入 Bundle
            }
        }
        return bundle;
    }
}

// NavController.java — handleDeepLink()
public boolean handleDeepLink(@Nullable Intent intent) {
    if (intent == null || intent.getData() == null) return false;
    
    Uri deepLink = intent.getData();
    // 遍历所有 Destination 的 deepLink 声明
    NavDestination destination = mGraph.matchDeepLink(deepLink);
    if (destination != null) {
        Bundle args = destination.getMatchingArguments(deepLink);
        // 构建导航路径（可能经过多级父图）
        navigate(destination.getId(), args);
        return true;
    }
    return false;
}
```

### 时序图说明

```
【Deep Link 处理流程】

外部 Intent 打开应用：
Intent(intent.action.VIEW, Uri.parse("myapp://detail/42"))

Activity.onCreate(intent)
  └─ NavController.handleDeepLink(intent)
      ├─ 获取 Uri = "myapp://detail/42"
      │
      ├─ mGraph.matchDeepLink(uri)
      │   └─ 遍历所有 Destination 注册的 NavDeepLink
      │       └─ matches(uri) → 正则匹配
      │           └─ "myapp://detail/(.*)" 匹配 "myapp://detail/42" ✓
      │
      ├─ 提取参数：itemId = "42" (String → Integer)
      │   └─ Bundle {itemId: 42}
      │
      ├─ 构建导航栈
      │   └─ 确保到目标 Destination 的完整路径（包括父图各级）
      │       ├─ navigate(parentGraph)
      │       └─ navigate(detailFragment, args)
      │
      └─ 显示目标 Fragment
```

### 面试加分点
- `NavDeepLink` 使用 `Pattern`（正则表达式）做 URI 匹配，支持路径参数。
- `matchDeepLink()` 先在当前图查找，找不到再到父图中查找（递归查找）。
- 支持多种 URI 模式：`http://`、`https://`、自定义 scheme、甚至不包括 scheme 的相对路径。
- 配合 `PendingIntent`：通知栏点击时通过 Deep Link 导航到指定页面。

### 常见误区
- **误区**：Deep Link 只能匹配一级路径。**正解**：`NavDeepLink` 的 Pattern 支持完整路径，包括多级路径匹配。
- **误区**：Deep Link 必须通过 `<data>` 在 Manifest 中声明。**正解**：Navigation 的 deepLink 是应用内的，通过 `handleDeepLink` 处理；Manifest 的 `<intent-filter>` 是系统级声明。
- **误区**：Deep Link 打开页面时回退栈为空。**正解**：Navigation 会自动构建回退栈，保证 `popBackStack` 不会退出应用。

---

<a id="第37题navigation-返回栈的保存与恢复"></a>
### 第37题：Navigation 返回栈的保存与恢复

### 问题描述
进程被杀死后重新启动，Navigation 如何恢复导航栈？`onRestoreState` 的原理是什么？

### 核心源码分析

```java
// NavController.java
public class NavController {
    // 保存状态
    @Nullable
    public Bundle saveState() {
        Bundle b = new Bundle();
        
        // 1. 保存回退栈中所有 NavBackStackEntry 的 ID 和参数
        ArrayList<Parcelable> backStackDestIds = new ArrayList<>();
        ArrayList<Bundle> backStackArgs = new ArrayList<>();
        for (NavBackStackEntry entry : mBackStack) {
            backStackDestIds.add(entry.getDestination().getId());
            backStackArgs.add(entry.getArguments());
        }
        b.putParcelableArrayList("android-support-nav:controller:backStackDestIds", backStackDestIds);
        b.putParcelableArrayList("android-support-nav:controller:backStackArgs", backStackArgs);
        
        // 2. 保存当前导航图 ID
        b.putInt("android-support-nav:controller:graphId", mGraph.getId());
        
        return b;
    }
    
    // 恢复状态
    public void restoreState(@Nullable Bundle navState) {
        if (navState == null) return;
        
        // 1. 恢复导航图
        int graphId = navState.getInt("android-support-nav:controller:graphId");
        setGraph(graphId);
        
        // 2. 恢复回退栈
        ArrayList<Integer> backStackDestIds = 
            navState.getIntegerArrayList("android-support-nav:controller:backStackDestIds");
        ArrayList<Bundle> backStackArgs = 
            navState.getParcelableArrayList("android-support-nav:controller:backStackArgs");
        
        // 3. 重新执行导航重建栈
        for (int i = 0; i < backStackDestIds.size(); i++) {
            int destId = backStackDestIds.get(i);
            Bundle args = backStackArgs.get(i);
            navigate(destId, args); // 重建每个 entry
        }
    }
}
```

### 时序图说明

```
【状态保存与恢复】

【进程被杀前】
Activity.onSaveInstanceState(outState)
  └─ NavController.saveState()
      ├─ 遍历 mBackStack
      │   ├─ HomeEntry(id=home) → [home_id, empty_args]
      │   ├─ DetailEntry(id=detail, args={itemId:42}) → [detail_id, {itemId:42}]
      │   └─ InfoEntry(id=info) → [info_id, empty_args]
      │
      └─ 返回 Bundle {
            graphId: R.id.main_graph,
            backStackDestIds: [home_id, detail_id, info_id],
            backStackArgs: [{}, {itemId:42}, {}]
          }

【进程恢复后】
Activity.onCreate(savedInstanceState)
  └─ NavController.restoreState(savedInstanceState的navState)
      ├─ setGraph(R.id.main_graph)
      └─ for (i: 0..2)
          ├─ navigate(home_id)     → HomeFragment
          ├─ navigate(detail_id, {itemId:42}) → DetailFragment
          └─ navigate(info_id)     → InfoFragment
      └─ 栈恢复: [Home, Detail, Info]
```

### 面试加分点
- `saveState()` 序列化的是 Destination ID（Int）和参数（Bundle），不是 Fragment 实例本身。Fragment 实例在恢复时重新创建。
- `restoreState` 时通过循环执行 `navigate` 重建完整的回退栈，而不是一次性恢复。
- 配合 `SavedStateHandle`：Fragment ViewModel 中通过 `SavedStateHandle` 保存的数据在进程重建时也能恢复。
- 关键区别：`onSaveInstanceState` 保存 Navigation 状态是标准生命周期行为，开发者无需手动处理。

### 常见误区
- **误区**：Navigation 可以恢复所有状态的 Fragment。**正解**：恢复的是导航栈结构（哪些 Destination、顺序、参数），Fragment 实例由 `FragmentNavigator` 重新创建。
- **误区**：保存和恢复均由 Navigation 自动完成。**正解**：需要在 `Activity.onCreate` 中调用 `navController.restoreState(savedInstanceState)`。
- **误区**：回退栈中所有 Fragment 在恢复时都会被重建。**正解**：当前可见的 Fragment 被重建，不可见的回退栈 Fragment 在需要时（popBackStack）才会被重建。

---

## 六、Paging 3 源码深度（4题）

<a id="第38题pagingsource-的加载机制"></a>
### 第38题：PagingSource 的加载机制

### 问题描述
`PagingSource.load()` 如何被触发？`LoadParams` 和 `LoadResult` 分别携带什么信息？

### 核心源码分析

```kotlin
// PagingSource.kt — 核心加载接口
abstract class PagingSource<Key : Any, Value : Any> {
    
    // 子类必须实现的加载方法
    abstract suspend fun load(params: LoadParams<Key>): LoadResult<Key, Value>
    
    // LoadParams：告诉 PagingSource 要加载什么
    class LoadParams<Key : Any>(
        val key: Key?,           // 加载的起始 key（null 表示首次加载）
        val loadSize: Int        // 期望加载的数量
    )
    
    // LoadResult：加载结果
    sealed class LoadResult<Key : Any, Value : Any> {
        // 成功页面
        data class Page<Key : Any, Value : Any>(
            val data: List<Value>,          // 数据列表
            val prevKey: Key?,              // 前一页的 key（null 表示到头了）
            val nextKey: Key?               // 下一页的 key（null 表示到头了）
        ) : LoadResult<Key, Value>()
        
        // 错误
        data class Error<Key : Any, Value : Any>(
            val throwable: Throwable
        ) : LoadResult<Key, Value>()
        
        // 加载中状态（仅 RemoteMediator 使用）
        object Loading : LoadResult<Nothing, Nothing>()
    }
    
    // Refresh 时计算锚点 key
    open fun getRefreshKey(state: PagingState<Key, Value>): Key? {
        // 默认：取最后一页的 nextKey
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.nextKey
        }
    }
}
```

```kotlin
// 典型实现示例
class UserPagingSource(
    private val api: UserApi
) : PagingSource<Int, User>() {
    
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, User> {
        val page = params.key ?: 1 // 首次加载 page=1
        return try {
            val response = api.getUsers(page, params.loadSize)
            LoadResult.Page(
                data = response.users,
                prevKey = if (page > 1) page - 1 else null,
                nextKey = if (response.hasMore) page + 1 else null
            )
        } catch (e: Exception) {
            LoadResult.Error(e) // 返回错误，不是抛异常
        }
    }
    
    override fun getRefreshKey(state: PagingState<Int, User>): Int? {
        return state.anchorPosition?.let { position ->
            state.closestPageToPosition(position)?.prevKey?.plus(1)
        }
    }
}
```

### 时序图说明

```
【PagingSource 加载流程】

Pager(PagingConfig(pageSize=20), pagingSourceFactory = { UserPagingSource() })
  └─ PagingSource.load(LoadParams(key=null, loadSize=20))   ← REFRESH（首次）
      ├─ API 请求第 1 页
      └─ LoadResult.Page(data=list, prevKey=null, nextKey=2)
          └─ UI 展示第 1 页

用户向下滚动到列表底部 →
  └─ PagingSource.load(LoadParams(key=2, loadSize=20))     ← APPEND
      ├─ API 请求第 2 页
      └─ LoadResult.Page(data=list, prevKey=1, nextKey=3)
          └─ UI 追加第 2 页

用户向上滚动到列表顶部 →
  └─ PagingSource.load(LoadParams(key=1, loadSize=20))     ← PREPEND
      ├─ 但 prevKey=null → 到顶了，不再加载
      └─ 无操作

数据刷新（下拉刷新）→ PagingSource.invalidate()
  └─ 重新调用 load(key=null) → REFRESH
      └─ getRefreshKey() 计算锚点，保证用户大致看到相同位置的数据
```

### 面试加分点
- `LoadResult.Error` 不是异常，不应 throw。Paging 框架会正确处理这个密封类并显示错误 UI。
- `getRefreshKey()` 的作用：在 REFRESH 时计算"从哪一页重新开始加载"，保持用户当前的滚动位置（锚点）不跳变。
- `prevKey` 和 `nextKey` 为 null 意味着数据到边界，Paging 不再触发对应方向的加载。
- `PagingSource` 天然支持协程挂起函数，`load()` 是 suspend 函数，默认在 `Dispatchers.IO` 执行。

### 常见误区
- **误区**：在 `load()` 中抛出异常。**正解**：应捕获异常并返回 `LoadResult.Error(e)`。
- **误区**：每页返回的数据量必须等于 `loadSize`。**正解**：可以是任意数量，少于 `loadSize` 表示已到尾页。
- **误区**：`prevKey` 和 `nextKey` 可以随意计算。**正解**：null 值有特殊含义（到达边界），必须准确反映是否还有更多数据。

---

<a id="第39题remotemediator-的网络本地双重加载"></a>
### 第39题：RemoteMediator 的网络+本地双重加载

### 问题描述
`RemoteMediator` 如何协调网络和本地数据库？`REFRESH`、`PREPEND`、`APPEND` 三种 LoadType 分别用于什么场景？

### 核心源码分析

```kotlin
// RemoteMediator.kt — 核心抽象
@ExperimentalPagingApi
abstract class RemoteMediator<Key : Any, Value : Any> {
    
    abstract suspend fun load(
        loadType: LoadType,
        state: PagingState<Key, Value>
    ): MediatorResult
    
    enum class LoadType {
        REFRESH,  // 初始加载或 refresh
        PREPEND,  // 在开头加载更早的数据
        APPEND    // 在末尾加载更多的数据
    }
    
    sealed class MediatorResult {
        data class Success(val endOfPaginationReached: Boolean) : MediatorResult()
        data class Error(val throwable: Throwable) : MediatorResult()
    }
}

// 典型实现：网络 → 数据库 → UI
@OptIn(ExperimentalPagingApi::class)
class UserRemoteMediator(
    private val api: UserApi,
    private val db: UserDatabase
) : RemoteMediator<Int, User>() {
    
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, User>
    ): MediatorResult {
        return try {
            // 1. 根据 LoadType 决定请求的页面
            val page = when (loadType) {
                LoadType.REFRESH -> {
                    // REFRESH：从数据库获取离 anchor 最近的 page
                    val remoteKeys = db.remoteKeysDao().getRemoteKeys(
                        state.anchorPosition ?: 0
                    )
                    remoteKeys?.nextKey?.minus(1) ?: 1 // 重新加载最近的页
                }
                LoadType.PREPEND -> {
                    val remoteKeys = db.remoteKeysDao().getRemoteKeyForFirstItem()
                    remoteKeys?.prevKey ?: return MediatorResult.Success(true)
                }
                LoadType.APPEND -> {
                    val remoteKeys = db.remoteKeysDao().getRemoteKeyForLastItem()
                    remoteKeys?.nextKey ?: return MediatorResult.Success(true)
                }
            }
            
            // 2. 从网络请求数据
            val response = api.getUsers(page, state.config.pageSize)
            val users = response.users
            val endOfPagination = users.isEmpty()
            
            // 3. 在事务中保存到本地数据库
            db.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    db.remoteKeysDao().clearRemoteKeys()
                    db.userDao().clearAll()
                }
                db.remoteKeysDao().insertAll(
                    users.map { RemoteKeys(it.id, prevKey = page-1, nextKey = page+1) }
                )
                db.userDao().insertAll(users)
            }
            
            MediatorResult.Success(endOfPaginationReached = endOfPagination)
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }
}
```

### 时序图说明

```
【RemoteMediator 三层架构】

UI (RecyclerView + PagingDataAdapter)
  ↑ collect PagingData
PagingSource (本地 Room DAO)
  ↑ 从本地数据库读取
Local Database (Room)
  ↑ 被 RemoteMediator 写入
RemoteMediator
  ↑ 从网络获取
API Server

【三种 LoadType 触发时机】

REFRESH：
  首次加载 / 下拉刷新 / 数据库清空后
  ├─ 清空本地数据库
  ├─ 从网络加载数据
  └─ 写入数据库 → PagingSource 自动感知变化

APPEND：
  用户滚动到列表底部
  ├─ 从数据库读取 lastItem 的 nextKey
  ├─ 从网络加载下一页
  └─ 追加写入数据库

PREPEND：
  用户滚动到列表顶部（聊天记录场景）
  ├─ 从数据库读取 firstItem 的 prevKey
  ├─ 从网络加载上一页
  └─ 前置写入数据库
```

### 面试加分点
- `RemoteMediator` 实现了 **Network + Local** 的分层架构：网络层负责拉取数据，本地 DB 是唯一的数据源（Single Source of Truth），UI 只从 DB 读取。
- `RemoteKeys` 表是关键：存储每个 item 的 `prevKey` 和 `nextKey`，用于 `PREPEND` 和 `APPEND` 时知道从哪一页继续加载。
- `REFRESH` 会清空本地数据后再从网络拉取，保证数据的"绝对新鲜"。
- `endOfPaginationReached` 告诉 Paging 是否还有更多数据，影响后续是否继续触发加载。

### 常见误区
- **误区**：网络请求失败时在 `RemoteMediator.load()` 中抛异常。**正解**：应 catch 并返回 `MediatorResult.Error(e)`，Paging 会展示错误并支持 retry。
- **误区**：`RemoteMediator` 直接返回数据给 UI。**正解**：数据写入数据库后，由 `PagingSource` 从数据库读取并交还给 UI。
- **误区**：`PREPEND` 和 `APPEND` 的 key 计算可以省略 `RemoteKeys` 表。**正解**：没有 `RemoteKeys` 表就无法知道当前已加载的范围，`PREPEND/APPEND` 会出错。

---

<a id="第40题pagingdata-的流式转换"></a>
### 第40题：PagingData 的流式转换

### 问题描述
`Pager.flow` 如何生成 `PagingData`？`.cachedIn()` 内部做了什么？`map`/`filter` 等转换操作的作用域是什么？

### 核心源码分析

```kotlin
// Pager.kt — 核心 Flow 生成
class Pager<Key : Any, Value : Any>(
    private val config: PagingConfig,
    private val pagingSourceFactory: () -> PagingSource<Key, Value>
) {
    // 核心 Flow
    val flow: Flow<PagingData<Value>> = flow {
        // 监听 PagingSource 变化（invalidate 等）
        // 每次 change 生成新的 PagingData
        // 内部启动 PagingDataDiffer 进行增量计算
    }
}

// cachedIn() — 作用域管理
@JvmOverloads
fun <T : Any> Flow<PagingData<T>>.cachedIn(
    scope: CoroutineScope
): Flow<PagingData<T>> {
    return scope.cachedIn(scope)
    // 内部使用 SharedFlow 实现：
    // - cacheUpstream：向上游保持订阅
    // - replay 1：新订阅者获取最新的 PagingData
}

// PagingData 转换操作
val pager = Pager(PagingConfig(pageSize = 20)) {
    repository.getAllUsers()
}

// .cachedIn() 的作用域示例
class MyViewModel(repo: MyRepository) : ViewModel() {
    val users: Flow<PagingData<User>> = Pager(config, pagingSourceFactory = {
        repo.userPagingSource()
    }).flow
        .cachedIn(viewModelScope) // 在 ViewModel 作用域内缓存
        .map { pagingData -> 
            pagingData.map { user -> user.toUiModel() } // PagingData 内的 map
        }
}
```

```kotlin
// PagingData 内部的 map 操作（非 Flow.map）
// PagingDataTransformation.kt
fun <T : Any, R : Any> PagingData<T>.map(
    transform: (T) -> R
): PagingData<R> {
    // 包裹为 TransformedPagingData
    // 不立即执行，在 collect 时实际应用转换
    // 支持在后台线程中执行（通过 PagingConfig 的 Dispatcher）
}
```

### 时序图说明

```
【PagingData Flow 的数据流】

ViewModel:
Pager(config, factory).flow
  └─ .cachedIn(viewModelScope)  ← 在 ViewModel 生命周期内保持活跃
      ├─ 内部使用 SharedFlow(replay=1)
      └─ .map { pagingData.map { ... } }  ← PagingData 内容转换

UI Layer:
lifecycleScope.launch {
    viewModel.users.collect { pagingData ->
        adapter.submitData(pagingData)  // 提交给 PagingDataAdapter
    }
}

【.cachedIn() 的作用】
没有 cachedIn()：
  每次 Activity 重建 → 重新 collect → 重新创建 PagingSource → 重新加载
  
有 cachedIn(viewModelScope)：
  Activity 重建 → 重新 collect → 从 SharedFlow 获取缓存的最新 PagingData
  PagingSource 不重建，数据不重新加载
```

### 面试加分点
- `.cachedIn(viewModelScope)` 将冷流转换为有作用域的热流，核心是 `SharedFlow`。在 `viewModelScope` 存活期间，即使没有 UI 收集者也会保持活跃。
- `PagingData.map()` 和 `Flow.map()` 不同：前者作用于 `PagingData` 内容（每个 item 的转换），后者作用于 `PagingData` 本身（按页面的转换）。
- `PagingData.filter()` 实际是 `.map { data -> data.filter { ... } }` 的简写。
- 转换操作（`map`/`filter`）发生在 `PagingConfig` 指定的 dispatcher 上，通常可配置为 `Dispatchers.Default`。

### 常见误区
- **误区**：`cachedIn()` 调用后会破坏冷流特性。**正解**：这是设计目的——避免配置变更时重新加载。
- **误区**：`.map()` 转换在主线程执行。**正解**：可在 `Dispatchers.Default` 执行，但需要确保数据类线程安全。
- **误区**：每个 `collect` 都创建一个新的 PagingData。**正解**：`cachedIn` 后是 SharedFlow，所有 collector 共享同一份数据。

---

<a id="第41题paging-3-的分页状态与-ui-集成"></a>
### 第41题：Paging 3 的分页状态与 UI 集成

### 问题描述
`LoadStateAdapter` 如何与 `PagingDataAdapter` 协作？`CombinedLoadStates` 包含哪些状态信息？

### 核心源码分析

```kotlin
// PagingDataAdapter 中的 LoadState 监听
class PagingDataAdapter {
    // 加载状态监听
    fun addLoadStateListener(listener: (CombinedLoadStates) -> Unit) {
        // differ.addLoadStateListener(listener)
    }
    
    // 当前加载状态
    val loadStateFlow: Flow<CombinedLoadStates>
}

// CombinedLoadStates — 聚合的加载状态
data class CombinedLoadStates(
    val refresh: LoadState,    // 初始加载/刷新状态
    val prepend: LoadState,    // 头部加载状态
    val append: LoadState,     // 尾部加载状态
    val source: LoadStates     // PagingSource 级别状态（Paging 2 兼容）
)

// LoadState — 三种可能的状态
sealed class LoadState {
    object NotLoading : LoadState()       // 非加载中（endOfPaginationReached）
    object Loading : LoadState()           // 加载中
    data class Error(val error: Throwable) : LoadState() // 加载失败
}

// LoadStateAdapter — 展示加载状态的 Footer/Header
abstract class LoadStateAdapter<VH : RecyclerView.ViewHolder> :
    RecyclerView.Adapter<VH>() {
    
    // 与 PagingDataAdapter 结合使用
    fun withLoadStateFooter(footer: LoadStateAdapter<*>): ConcatAdapter {
        return ConcatAdapter(this, footer)
    }
    
    // 子类实现不同的 LoadState 展示
    abstract fun onBindLoadState(loadState: LoadState, holder: VH)
}
```

```kotlin
// 典型使用示例
class UserLoadStateAdapter(
    private val retry: () -> Unit
) : LoadStateAdapter<UserLoadStateAdapter.ViewHolder>() {
    
    override fun onBindViewHolder(holder: ViewHolder, loadState: LoadState) {
        holder.bind(loadState, retry)
    }
    
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(loadState: LoadState, retry: () -> Unit) {
            when (loadState) {
                is LoadState.NotLoading -> { /* 隐藏 */ }
                is LoadState.Loading -> { /* 显示 loading */ }
                is LoadState.Error -> { 
                    /* 显示错误和重试按钮 */
                    retryButton.setOnClickListener { retry() }
                }
            }
        }
    }
}

// 使用
val adapter = UserAdapter().withLoadStateFooter(
    footer = UserLoadStateAdapter { adapter.retry() }
)
```

### 时序图说明

```
【LoadState 状态流转】

首次加载：
refresh: Loading → append/prepend: NotLoading
  ├─ Footer 显示 Loading spinner
  └─ 加载完成 → refresh: NotLoading
      ├─ Footer 隐藏
      └─ 还有更多数据 → append: NotLoading → 等待用户滚动

用户滚动到底部：
append: Loading
  ├─ Footer 显示 Loading
  └─ 加载成功 → append: NotLoading
  
网络错误：
append: Error(IOException)
  ├─ Footer 显示 "加载失败，点击重试"
  └─ 用户点击重试 → adapter.retry()
      └─ append: Loading → 重新触发 APPEND 加载
```

### 面试加分点
- `CombinedLoadStates` 聚合了 `PREPEND`、`APPEND`、`REFRESH` 三种方向的状态，互不干扰。
- `LoadStateAdapter` + `ConcatAdapter` 实现 Footer/Header 的复用，不需要修改主 Adapter。
- `adapter.retry()` 是 Paging 3 内置支持的重试机制，自动触发最后一次失败的 `loadType`。
- `.loadStateFlow` 提供 Flow 形式的状态监听，可配合 `distinctUntilChanged` 使用。

### 常见误区
- **误区**：`LoadState.NotLoading` 表示所有数据加载完毕。**正解**：仅表示"当前没有加载操作在执行"，后续可能因用户滚动再次变为 `Loading`。
- **误区**：Footer 的 `Loading` 状态可以用来判断数据是否加载完。**正解**：`endOfPaginationReached` 由 `PagingSource` 返回，反映在 `append: NotLoading` 状态，但单独看 LoadState 无法直接判断是否已到尾页。
- **误区**：`retry()` 只能重试最后一次失败。**正解**：确实如此，它触发的是最近一次失败的 `LoadType`。

---

## 七、WorkManager 源码深度（3题）

<a id="第42题workmanager-的任务调度机制"></a>
### 第42题：WorkManager 的任务调度机制

### 问题描述
`Worker`、`ListenableWorker`、`CoroutineWorker` 的区别是什么？`WorkRequest` 如何构建和执行？

### 核心源码分析

```kotlin
// ListenableWorker.java — 最基础的工作单元
abstract class ListenableWorker {
    internal var mWorkerParams: WorkerParameters
    private var mFuture: SettableFuture<Result>
    
    @MainThread
    abstract fun startWork(): ListenableFuture<Result>
    // 子类实现：返回 ListenableFuture，在后台线程执行
    
    fun getBackgroundExecutor(): Executor = mBackgroundExecutor
}

// Worker.java — 基于 ListenableWorker 的同步简化版
abstract class Worker : ListenableWorker() {
    abstract fun doWork(): Result  // 同步执行
    
    override fun startWork(): ListenableFuture<Result> {
        mFuture = SettableFuture.create()
        backgroundExecutor.execute {
            val result = doWork()  // 在后台线程执行
            mFuture.set(result)    // 设置结果
        }
        return mFuture
    }
}

// CoroutineWorker.kt — 协程版本
abstract class CoroutineWorker : ListenableWorker() {
    abstract suspend fun doWork(): Result  // 挂起函数
    
    override fun startWork(): ListenableFuture<Result> {
        val future = SettableFuture.create<Result>()
        // 从 CoroutineContext 中获取 scope
        coroutineScope.launch {
            try {
                val result = doWork()  // 挂起执行
                future.set(result)
            } catch (e: Throwable) {
                future.setException(e)
            }
        }
        return future
    }
}
```

```kotlin
// WorkRequest 构建链
val workRequest = OneTimeWorkRequestBuilder<MyWorker>()
    .setConstraints(
        Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED) // 需要网络
            .setRequiresCharging(true)                     // 需要充电
            .setRequiresBatteryNotLow(true)                // 电池不低
            .build()
    )
    .setInputData(workDataOf("key" to "value"))
    .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 10, TimeUnit.SECONDS)
    .addTag("my_work")
    .build()

// 提交给 WorkManager
WorkManager.getInstance(context).enqueue(workRequest)
```

### 时序图说明

```
【任务调度流程】

WorkManager.enqueue(workRequest)
  └─ WorkManagerImpl.enqueue()
      └─ WorkDatabase 写入 WorkSpec 记录
          ├─ id: UUID
          ├─ state: ENQUEUED
          ├─ constraints: {network=CONNECTED, charging=true}
          └─ backoffPolicy: EXPONENTIAL

调度器检查任务：
  ├─ GreedyScheduler（应用进程内）
  │   └─ 检查 Constraints 是否满足
  │       ├─ 满足 → 启动 Worker
  │       └─ 不满足 → 等待
  │
  └─ AlarmManager / JobScheduler（系统级）
      └─ 进程被杀后仍能唤醒执行

Worker.doWork() 执行：
  ├─ 成功 → Result.success() → state: SUCCEEDED
  ├─ 失败 → Result.failure() → state: FAILED → 重试/放弃
  └─ 重试 → Result.retry()   → state: ENQUEUED → 按 backoff 延迟重试
```

### 面试加分点
- `CoroutineWorker` 是推荐的选择：协程自动处理取消（`coroutineScope.launch` 绑定），避免 ANR，支持结构化并发。
- 三种 `Result` 类型非常重要：`success()`（任务完成）、`failure()`（不重试）、`retry()`（按 backoff 策略重试）。
- `Constraints` 由 WorkManager 内部管理，通过 `ConstraintTracker` 监听系统广播（网络变化、充电状态等）自动决定何时执行。
- 重试策略：`BackoffPolicy.LINEAR` 和 `EXPONENTIAL`，可设置最短延迟和最大延迟。

### 常见误区
- **误区**：`Worker.doWork()` 在主线程执行。**正解**：`Worker` 内部通过 `backgroundExecutor.execute` 在后台线程执行。
- **误区**：`doWork()` 超时 WorkManager 会自动返回 `Result.failure()`。**正解**：可以在设置 `setInitialDelay` 或通过 WorkRequest 的 Timeout 配置超时。
- **误区**：`enqueue()` 后任务立即执行。**正解**：需要满足所有 `Constraints`，否则等待条件满足。

---

<a id="第43题workmanager-的内部调度器"></a>
### 第43题：WorkManager 的内部调度器

### 问题描述
WorkManager 内部使用了哪些调度器？它们如何协作？不同 Android 版本如何选择调度策略？

### 核心源码分析

```java
// WorkManager 内部的调度器架构
// Scheduler.java — 调度器接口
interface Scheduler {
    fun schedule(vararg workSpecs: WorkSpec)
    fun cancel(workSpecId: String)
    fun hasLimitedSchedulingSlots(): Boolean
}

// 三个核心调度器实现：

// 1. GreedyScheduler — 进程内调度器
class GreedyScheduler : Scheduler, DefaultLifecycleObserver {
    // 在应用进程活着时，直接在线程池执行
    // 优点：无延迟，立即执行
    // 缺点：进程被杀后失效
    fun schedule(workSpec: WorkSpec) {
        backgroundExecutor.execute {
            worker.doWork() // 直接在当前进程执行
        }
    }
}

// 2. SystemJobScheduler (API 23+) — JobScheduler 封装
class SystemJobScheduler : Scheduler {
    // 通过 JobScheduler API 调度
    // 优点：系统级保活，进程死后可唤醒
    // 缺点：有最小延迟（15分钟+）
    fun schedule(workSpec: WorkSpec) {
        val jobInfo = JobInfo.Builder(jobId, componentName)
            .setRequiredNetworkType(...)
            .setRequiresCharging(...)
            .build()
        jobScheduler.schedule(jobInfo)
    }
}

// 3. SystemAlarmScheduler (API < 23) — AlarmManager 封装
class SystemAlarmScheduler : Scheduler {
    // 通过 AlarmManager 调度
    // 适用于低版本 Android
}

// Schedulers.java — 调度策略选择
class Schedulers {
    fun schedule(workSpec: WorkSpec) {
        // 默认：API 23+ 使用 GreedyScheduler + SystemJobScheduler
        // API < 23：使用 GreedyScheduler + SystemAlarmScheduler
        
        if (Build.VERSION.SDK_INT >= 23) {
            systemJobScheduler.schedule(workSpec)
        } else {
            systemAlarmScheduler.schedule(workSpec)
        }
        // GreedyScheduler 总是配合使用
        greedyScheduler.schedule(workSpec)
    }
}
```

### 时序图说明

```
【两种调度器的协作】

API 23+ 场景：

WorkRequest 提交：
  ├─ GreedyScheduler.schedule()
  │   └─ Constraints 满足？ → 在线程池中直接执行 ✓
  │
  └─ SystemJobScheduler.schedule()
      └─ jobScheduler.schedule(jobInfo)  ← 保底方案

进程在前台：
  └─ GreedyScheduler 直接执行（立即）

进程被杀死：
  └─ GreedyScheduler 失效 ✗
  └─ SystemJobScheduler 保留任务 ✗
      └─ 当 Constraints 满足 + JobScheduler 唤醒进程
          └─ WorkManager 从数据库读取任务 → 执行

【调度策略对比】
GreedyScheduler:   延迟 ~0ms | 进程存活 | 线程池
SystemJobScheduler: 延迟 >15min | 进程死亡可唤醒 | 系统服务
SystemAlarmScheduler: 延迟精确 | API <23 | AlarmManager
```

### 面试加分点
- `GreedyScheduler` + `SystemJobScheduler` 双保险机制：进程存活时 GreedyScheduler 立即执行（零延迟）；进程被杀时 SystemJobScheduler 保证任务不会丢失。
- API 23 的分水岭：`JobScheduler` 从 API 21 引入，但 WorkManager 在 API 23+ 才使用，因为低版本的 `JobScheduler` 有 bug（如 `setPeriodic` 不生效）。
- `hasLimitedSchedulingSlots()`：`SystemJobScheduler` 受限于系统 JobScheduler 的槽位数（约 100 个），GreedyScheduler 没有此限制。
- `WorkManager` 在 Android 12 (API 31) 后配合 `ExpeditedWorkRequest` 可以享受更高优先级的调度（如 `JobScheduler` 的 `setExpedited`）。

### 常见误区
- **误区**：`SystemJobScheduler` 会立即执行任务。**正解**：系统 JobScheduler 有最小 15 分钟的保证延迟，不能保证立即执行。即时任务应配合 `GreedyScheduler`。
- **误区**：WorkManager 只使用一个调度器。**正解**：内部有三个调度器，根据 API 版本和约束条件自动选择。
- **误区**：进程前台的 WorkManager 临时任务一定会执行。**正解**：还必须满足 `Constraints`。

---

<a id="第44题workmanager-的数据库设计"></a>
### 第44题：WorkManager 的数据库设计

### 问题描述
WorkManager 内部使用的 Room 数据库有哪些表？`WorkSpec` 的状态如何流转？

### 核心源码分析

```java
// WorkDatabase.java — WorkManager 内部数据库
@Database(
    entities = {
        WorkSpec.class,
        WorkProgress.class,
        SystemIdInfo.class,
        WorkName.class,
        WorkTag.class,
        Dependency.class
    },
    version = 16
)
abstract class WorkDatabase extends RoomDatabase {
    abstract fun workSpecDao(): WorkSpecDao
    abstract fun workProgressDao(): WorkProgressDao
    abstract fun systemIdInfoDao(): SystemIdInfoDao
}

// WorkSpec — 核心任务表
@Entity
data class WorkSpec(
    @PrimaryKey val id: String,            // UUID
    val state: WorkInfo.State,             // 任务状态
    val workerClassName: String,           // Worker 的全限定类名
    val inputMergerClassName: String?,
    val output: Data?,                     // 输出数据
    val input: Data?,                      // 输入数据
    val constraints: Constraints?,         // 约束条件
    val initialDelay: Long,               // 初始延迟
    val intervalDuration: Long,            // 周期性任务的间隔
    val flexDuration: Long,               // 灵活窗口
    val backoffDelayDuration: Long,        // 退避延迟
    val backoffPolicy: BackoffPolicy,     // 退避策略
    val runAttemptCount: Int,             // 尝试次数
    val periodStartTime: Long,            // 周期开始时间
    val scheduleRequestedAt: Long         // 调度请求时间
)

// WorkSpecDao — 核心增删改查
@Dao
interface WorkSpecDao {
    @Query("SELECT * FROM workspec WHERE state IN (:states)")
    suspend fun getWorkSpecsByState(states: List<State>): List<WorkSpec>
    
    @Query("UPDATE workspec SET state=:state WHERE id=:id")
    suspend fun setState(id: String, state: State)
    
    @Query("SELECT * FROM workspec WHERE id=:id")
    suspend fun getWorkSpec(id: String): WorkSpec?
}

// 任务状态枚举
enum class State {
    ENQUEUED,      // 已入队，等待满足约束
    RUNNING,       // 正在执行
    SUCCEEDED,     // 执行成功
    FAILED,        // 执行失败（可能重试）
    BLOCKED,       // 被依赖阻塞
    CANCELLED      // 已取消
}
```

### 时序图说明

```
【WorkSpec 状态流转】

ENQUEUED ────────────────────────────────
  │  (满足约束)                              │ (不满足约束)
  ↓                                         │ 等待...
RUNNING ──────────────────────────────      │
  │ doWork()                      │        │
  ├─ Result.success()             │ Result.retry()
  │    ↓                          │    ↓
  │ SUCCEEDED                     │ ENQUEUED ←── 重新入队
  │    (从数据库删除)              │    │         (backoff 延迟后)
  │                               │ Result.failure()
  │                               │    ↓
  │                               │ FAILED ←── 不重试
  │                               │
  │                               └ 用户取消
  │                                    ↓
  │                               CANCELLED ←── 从数据库删除

依赖状态：
WorkB.dependsOn(WorkA)
  ├─ WorkA = ENQUEUED
  └─ WorkB = BLOCKED ←── 等待 WorkA 完成
      └─ WorkA → SUCCEEDED → WorkB → ENQUEUED
```

### 面试加分点
- `WorkSpec` 对应数据库中的 `workspec` 表，持久化了 Worker 的所有配置信息，保证进程死亡后数据不丢失。
- `Dependency` 表存储任务之间的依赖关系（`WorkSpec.id` → `prerequisite_id`），通过外键关联。
- `WorkTag` 和 `WorkName` 表支持按 tag 和 name 查询/取消任务。
- `runAttemptCount` 配合 `backoffPolicy` 实现指数退避重试：第 0 次立即执行，失败后第 1 次等 10 秒，第 2 次等 20 秒...

### 常见误区
- **误区**：WorkManager 在内存中管理所有任务。**正解**：所有任务都持久化在 Room 数据库中，进程死亡后重建时从数据库恢复。
- **误区**：`SUCCEEDED` 的记录会永久保留。**正解**：WorkManager 有清理策略（`WorkManager.keepResultsForAtLeast()`），超时后自动删除完成记录。
- **误区**：`FAILED` 状态表示永远失败。**正解**：取决于 `doWork()` 的返回值：`Result.failure()` 直接 FAILED 不重试；`Result.retry()` 回到 ENQUEUED 并按 backoff 重试。

---

## 八、Hilt/Dagger 依赖注入源码（3题）

<a id="第45题hilt-的编译时代码生成"></a>
### 第45题：Hilt 的编译时代码生成

### 问题描述
`@HiltAndroidApp` 和 `@AndroidEntryPoint` 如何处理？生成了哪些基类？

### 核心源码分析

```kotlin
// @HiltAndroidApp → 生成 Hilt_MyApplication
@HiltAndroidApp(Application::class)
class MyApplication : Application() {
    // Hilt 的 APT 会生成：
    // Hilt_MyApplication extends Application implements GeneratedComponentManagerHolder
}

// 生成的代码（简化）
abstract class Hilt_MyApplication : Application(), 
    GeneratedComponentManagerHolder {
    
    private var componentManager: GeneratedComponentManager<*>? = null
    
    override fun onCreate() {
        // 1. 初始化依赖图
        componentManager = GeneratedComponentManager(
            MyApplication_GeneratedInjector::class.java,
            DaggerMyApplication_HiltComponents_SingletonC.builder()
                .applicationContextModule(ApplicationContextModule(this))
                .build()
        )
        super.onCreate() // 调用用户代码
    }
    
    override fun generatedComponent(): Any {
        // 2. 返回 SingletonComponent
        return componentManager.generatedComponent()
    }
}

// @AndroidEntryPoint(Activity::class) → 生成 Hilt_MyActivity
@AndroidEntryPoint
class MyActivity : AppCompatActivity() {
    // Hilt 生成 Hilt_MyActivity extends AppCompatActivity
}

abstract class Hilt_MyActivity : AppCompatActivity() {
    private var injected = false
    
    override fun onCreate(savedInstanceState: Bundle?) {
        // 1. 执行注入
        inject()
        super.onCreate(savedInstanceState)
    }
    
    private fun inject() {
        if (injected) return
        injected = true
        // 2. 获取 EntryPoint 并注入
        val entryPoint = EntryPoints.get(
            applicationContext, 
            MyActivity_GeneratedInjector::class.java
        )
        entryPoint.injectMyActivity(this as MyActivity)
    }
}
```

### 时序图说明

```
【Hilt 编译时代码生成流程】

@HiltAndroidApp MyApplication
  └─ APT → Hilt_MyApplication
      ├─ implements GeneratedComponentManagerHolder
      ├─ onCreate() 中自动初始化 SingletonComponent
      └─ generatedComponent() 返回 Dagger 生成的 Component 实例

@AndroidEntryPoint MyActivity
  └─ APT → Hilt_MyActivity extends 用户继承的父类
      ├─ onCreate() 中自动调用 inject()
      │   └─ EntryPoints.get(entryPointClass).injectMyActivity(this)
      └─ @Inject 字段在 inject() 后完成初始化

@AndroidEntryPoint MyFragment
  └─ APT → Hilt_MyFragment extends 用户继承的父类
      ├─ onAttach() 中自动调用 inject()
      └─ Fragment 的注入要求在 onAttach(context) 之后
```

### 面试加分点
- 生成的基类通过继承链插入初始化代码，开发者无需手动调用任何注入方法。
- `GeneratedComponentManagerHolder` 接口统一了 Component 的生命周期管理。
- `EntryPoints.get()` 是 Dagger 的 Hilt 扩展，用于在非 @Inject 可触及的位置获取依赖。
- 所有生成的代码都在 `build/generated/source/kapt` 目录下，可以直接查看。

### 常见误区
- **误区**：`@AndroidEntryPoint` 可以标注在任何类上。**正解**：只支持 Activity、Fragment、View、Service、BroadcastReceiver 四种 Android 组件。
- **误区**：Hilt 生成的基类会改变类的继承关系。**正解**：生成的基类继承用户指定的父类，用户类再继承生成的基类，保证不影响类型层级。
- **误区**：`@HiltAndroidApp` 是可选的。**正解**：这是整个依赖图的根节点，所有 Hilt 注入的入口，不标注就无法初始化 `SingletonComponent`。

---

<a id="第46题hilt-的组件层级和作用域"></a>
### 第46题：Hilt 的组件层级和作用域

### 问题描述
Hilt 的 `SingletonComponent`、`ActivityComponent`、`FragmentComponent` 等组件是如何组织的？`@ActivityScoped` 的作用域是什么？

### 核心源码分析

```kotlin
// Hilt 的组件层级（定义在 HiltComponent 中）
// 源码层级关系：

@Singleton
@Component
interface SingletonComponent {
    // 应用级别，整个 Application 生命周期
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance app: Application): SingletonComponent
    }
}

@ActivityScoped
@Subcomponent
interface ActivityComponent {
    // Activity 级别，每个 Activity 一个实例
    // 子组件：FragmentComponent, ViewComponent, ViewWithFragmentComponent
}

@FragmentScoped
@Subcomponent
interface FragmentComponent {
    // Fragment 级别，每个 Fragment 一个实例
}

@ViewModelScoped
@Subcomponent
interface ViewModelComponent {
    // ViewModel 级别，绑定到 ViewModel 生命周期
}

// 实际生成的 Dagger Component（简化）
@Singleton
@Component(modules = [ApplicationContextModule::class])
interface SingletonC : SingletonComponent {
    @Component.Factory
    interface Factory : SingletonComponent.Factory
}

// ActivityComponent 的 Builder
@ActivityScoped
@Subcomponent(modules = [ActivityModule::class])
interface ActivityC : ActivityComponent {
    @Subcomponent.Factory
    interface Factory : ActivityComponent.Factory
}

// Hilt 生成的代码中，SingletonComponent 持有 ActivityRetainedComponent
// ActivityRetainedComponent 持有 ActivityComponent 的 Builder
// ActivityComponent 持有 FragmentComponent 的 Builder
```

```kotlin
// 作用域注解与生命周期的对应
@Singleton        → Application 生命周期
@ActivityRetainedScoped → Configuration Change 存活
@ActivityScoped   → Activity 生命周期
@FragmentScoped   → Fragment 生命周期
@ViewScoped       → View 生命周期
@ViewModelScoped  → ViewModel 生命周期
@ServiceScoped    → Service 生命周期
```

### 时序图说明

```
【Hilt 组件层级和子组件关系】

SingletonComponent (@Singleton)
  ├─ Application 创建时构建
  ├─ Application 销毁时释放
  │
  └─ ActivityRetainedComponent (@ActivityRetainedScoped)
      ├─ 首次 Activity 创建时构建
      ├─ Configuration Change 时保留
      │
      └─ ActivityComponent (@ActivityScoped)
          ├─ 每个 Activity 一个
          ├─ Activity.onDestroy 时销毁
          │
          ├─ ViewModelComponent (@ViewModelScoped)
          │   └─ 绑定 ViewModel 生命周期
          │
          └─ FragmentComponent (@FragmentScoped)
              ├─ 每个 Fragment 一个
              └─ Fragment.onDestroy 时销毁
                  └─ ViewComponent (@ViewScoped)
```

### 面试加分点
- `ActivityRetainedComponent` 的独特地位：在 Configuration Change 时保留，比 ActivityComponent 生命周期长，适合存放需要跨旋转屏的依赖。
- `ViewModelComponent` 持有 `@ViewModelScoped` 的依赖，其生命周期与 ViewModel 绑定（`onCleared()` 时销毁）。
- 组件层级决定了依赖的可见性：`SingletonComponent` 的依赖在所有子组件中可见，但 `FragmentComponent` 的依赖只在 `FragmentComponent` 及其子组件中可见。
- `@Subcomponent.Factory` 允许子组件在构建时接收父组件传递的参数（如 `SavedStateHandle`）。

### 常见误区
- **误区**：`@ActivityScoped` 的对象在 Activity 旋转屏幕时保留。**正解**：会随 Activity 销毁而销毁，如需保留应使用 `@ActivityRetainedScoped`。
- **误区**：`@ViewModelScoped` 和 `@ActivityRetainedScoped` 生命周期相同。**正解**：ViewModelScoped 绑定到 ViewModel，ActivityRetainedScoped 绑定到 Activity 的 `NonConfigurationInstances`。
- **误区**：所有依赖都可以用 `@Singleton` 作用域。**正解**：`@Singleton` 的对象永远不被 GC，应谨慎使用。只有真正的全局对象才适合。

---

<a id="第47题hilt-与-viewmodel-的集成原理"></a>
### 第47题：Hilt 与 ViewModel 的集成原理

### 问题描述
`@HiltViewModel` 如何让 ViewModel 被 Hilt 管理？`SavedStateHandle` 如何自动注入？

### 核心源码分析

```kotlin
// @HiltViewModel 注解
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.BINARY)
@MapKey
annotation class HiltViewModel

// Hilt APT 生成的模块代码
@Module
@InstallIn(ViewModelComponent::class)
abstract class MyViewModel_HiltModules {
    
    @Binds
    @IntoMap
    @HiltViewModelMap.Key(MyViewModel::class)
    abstract fun bind(viewModel: MyViewModel): ViewModel
    
    // 此绑定将 MyViewModel 放入 Map<String, Provider<ViewModel>>
}

// HiltViewModelFactory.java — Hilt 提供的 ViewModel Factory
@ActivityScoped
public class HiltViewModelFactory implements ViewModelProvider.Factory {
    private final Map<String, Provider<ViewModel>> viewModelMap;
    
    @Inject
    HiltViewModelFactory(
        @ViewModelComponentViewModelMap 
        Map<String, Provider<ViewModel>> viewModelMap
    ) {
        this.viewModelMap = viewModelMap;
    }
    
    @Override
    @NonNull
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        String canonicalName = modelClass.getCanonicalName();
        Provider<ViewModel> provider = viewModelMap.get(canonicalName);
        if (provider == null) {
            throw new IllegalStateException(
                "Unknown ViewModel class: " + canonicalName);
        }
        return (T) provider.get();
        // provider.get() 内部由 Dagger 构造并注入所有依赖
    }
}

// SavedStateHandle 的自动注入
// 在 ViewModelComponent 中预置了 SavedStateHandle 的绑定
@Module
@InstallIn(ViewModelComponent::class)
object SavedStateHandleModule {
    @Provides
    @ViewModelScoped
    fun provideSavedStateHandle(
        // SavedStateHandle 的创建由 SavedStateHandleHolder 提供
    ): SavedStateHandle {
        // ... 
    }
}

// 用户使用
@HiltViewModel
class MyViewModel @Inject constructor(
    private val repository: MyRepository,    // 自动注入
    private val savedStateHandle: SavedStateHandle  // 自动注入
) : ViewModel() {
    // 构造函数参数由 Dagger 自动解析
}
```

### 时序图说明

```
【@HiltViewModel 注入流程】

@HiltViewModel
class MyViewModel @Inject constructor(
    private val repo: MyRepository,
    private val savedStateHandle: SavedStateHandle
)

编译时生成：
├─ MyViewModel_HiltModules.kt
│   └─ @Binds @IntoMap bind(MyViewModel): ViewModel
│       → 注册到 Map<String, Provider<ViewModel>>
│
└─ Dagger 为 ViewModelComponent 生成 Provider

运行时使用：
ViewModelProvider(activity).get(MyViewModel::class)
  └─ 默认 Factory = HiltViewModelFactory
      └─ viewModelMap["com.example.MyViewModel"].get()
          └─ Dagger 构造 MyViewModel
              ├─ 解析 repo: MyRepository → 查找 @Provides/@Binds
              │   └─ 可能来自 SingletonComponent 或其他
              └─ 解析 savedStateHandle: SavedStateHandle
                  └─ ViewModelComponent 内置提供
```

### 面试加分点
- `@HiltViewModel` 的作用是让 ViewModel 的绑定信息以 Map 形式注册到 `ViewModelComponent` 中，使 `HiltViewModelFactory` 能找到它。
- `HiltViewModelFactory` 是 `@ActivityScoped` 的，由 Hilt 自动设置为 `ComponentActivity.getDefaultViewModelProviderFactory()`。
- `SavedStateHandle` 在 ViewModelComponent 中自动可用，不需要额外的 `@Provides`。
- ViewModelComponent 是 Hilt 的特殊组件：虽然它是 ActivityRetainedComponent 的子组件，但生命周期独立绑定到 ViewModel。

### 常见误区
- **误区**：`@Inject constructor` 就足够让 ViewModel 被 Hilt 管理。**正解**：必须同时加 `@HiltViewModel`，因为 ViewModel 不走普通的 Dagger 创建（由 `ViewModelProvider` 框架管理）。
- **误区**：Hilt 中 `SavedStateHandle` 需要手动注入。**正解**：`ViewModelComponent` 内置提供，构造函数中直接声明参数即可。
- **误区**：`@HiltViewModel` 和 `@Inject constructor` 可以只用其一。**正解**：两者必须同时使用：`@HiltViewModel` 用于注册 ViewModel，`@Inject constructor` 用于声明依赖。

---

## 九、DataStore 源码（3题）

<a id="第48题preferences-datastore-的读写原理"></a>
### 第48题：Preferences DataStore 的读写原理

### 问题描述
`DataStore<T>.data` 的 Flow 是如何实现的？`updateData()` 内部的事务机制是怎样的？

### 核心源码分析

```kotlin
// DataStore.kt — 核心接口
interface DataStore<T> {
    val data: Flow<T>  // 数据变化的 Flow
    
    suspend fun updateData(transform: suspend (t: T) -> T): T
    // 原子读写操作
}

// DataStoreImpl.kt — Preferences DataStore 实现
internal class DataStoreImpl<T>(
    private val storage: Storage<T>,        // 文件存储
    private val serializer: Serializer<T>,  // 序列化器
    private val scope: CoroutineScope       // 作用域
) : DataStore<T> {
    
    // 核心：data Flow 的实现
    override val data: Flow<T> = flow {
        val currentDownstreamFlowState = downstreamFlowState
        // 1. 首次发射当前缓存值
        emit(currentDownstreamFlowState.value)
        
        // 2. 注册文件变化监听
        val channel = Channel<T>(Channel.CONFLATED)
        downstreamFlowState.actor.offer(
            DataStoreImpl.Message.ReadActor.AddSubscriber(channel)
        )
        
        // 3. 当文件变化时重新发射
        for (value in channel) {
            emit(value) // Actor 确保线程安全
        }
    }
    
    // updateData 的事务实现
    override suspend fun updateData(transform: suspend (t: T) -> T): T {
        return actor.offerAndAwait { 
            // 单线程 Actor 确保事务原子性
            val currentData = readData()
            val newData = transform(currentData) // 用户转换
            writeData(newData)                   // 写入文件
            newData                              // 返回新值
        }
    }
}
```

```kotlin
// PreferencesSerializer.kt — 序列化存储
internal class PreferencesSerializer : Serializer<Preferences> {
    override val defaultValue: Preferences = emptyPreferences()
    
    override suspend fun readFrom(input: InputStream): Preferences {
        // 读取 .preferences_pb 文件
        // 使用 protobuf 解析
        return PreferencesProtoAdapter.readFrom(input)
    }
    
    override suspend fun writeTo(t: Preferences, output: OutputStream) {
        // 写入 .preferences_pb 文件
        // 写入临时文件 → 原子重命名，保证写入原子性
        val tmpFile = File(file.absolutePath + ".tmp")
        tmpFile.outputStream().use { PreferencesProtoAdapter.writeTo(t, it) }
        tmpFile.renameTo(file) // 原子操作
    }
}
```

### 时序图说明

```
【DataStore 读写流程】

初始化：
DataStoreFactory.create(serializer) { file }
  └─ 检查文件是否存在
      ├─ 存在 → 读取并反序列化
      └─ 不存在 → 使用 serializer.defaultValue

读取：
dataStore.data.collect { preferences ->
    val name = preferences[KEY_NAME] // 读取某个 key
}
  └─ Flow 发射当前值
  └─ 注册文件变化监听
      └─ 文件变化时自动发射新值

写入：
dataStore.updateData { preferences ->
    preferences.toMutablePreferences().apply {
        set(KEY_NAME, "Alice")  // 设置值
    }
}
  └─ Actor 序列化执行
      ├─ readData() → 读出当前 Preferences
      ├─ transform(current) → 用户修改
      ├─ writeData(new) → 写入 tmp 文件
      └─ tmpFile.renameTo(original) → 原子替换
          └─ 通知 Flow 发射新值
```

### 面试加分点
- DataStore 使用单线程 `Actor` 模型保证 `updateData()` 的原子性：多个协程调用 `updateData` 时被串行化执行，防止竞态条件。
- 文件写入采用"先写临时文件再原子重命名"的策略，防止写入过程中断电导致的数据损坏。
- `Channel.CONFLATED` 用于数据发射：如果多个文件变化快速发生，只保留最新的，减少不必要的 UI 更新。
- 底层使用 protobuf 序列化（`.preferences_pb` 格式），是二进制格式，比 XML 的 SharedPreferences 更高效。

### 常见误区
- **误区**：`data` Flow 是热流。**正解**：是冷流，但在 `DataStore` 内部被转换为有状态热流（缓存最新值）。
- **误区**：`updateData()` 可以并发执行。**正解**：Actor 串行化保证一次只有一个 updateData 在执行。
- **误区**：DataStore 数据储存在 SharedPreferences 文件中。**正解**：储存在独立的 `.preferences_pb` 文件中，格式完全不同。

---

<a id="第49题proto-datastore-与-preferences-datastore-的对比"></a>
### 第49题：Proto DataStore 与 Preferences DataStore 的对比

### 问题描述
两种 DataStore 在 Schema 定义、类型安全、性能等方面有什么区别？

### 核心源码分析

```kotlin
// Preferences DataStore — 无 Schema
// 定义方式：
val Context.dataStore by preferencesDataStore(name = "settings")

// 使用 KEY 访问，无类型约束
object PreferencesKeys {
    val NAME = stringPreferencesKey("name")
    val AGE = intPreferencesKey("age")
    val IS_ADMIN = booleanPreferencesKey("is_admin")
}

// 读取（Key-Value 方式，无 Schema）
dataStore.data.map { preferences ->
    UserSettings(
        name = preferences[PreferencesKeys.NAME] ?: "",
        age = preferences[PreferencesKeys.AGE] ?: 0,
        isAdmin = preferences[PreferencesKeys.IS_ADMIN] ?: false
    )
}

// Proto DataStore — 有 Schema
// 定义 .proto 文件（编译时 Schema）：
// message UserSettings {
//   string name = 1;
//   int32 age = 2;
//   bool is_admin = 3;
// }

// 序列化器定义：
object UserSettingsSerializer : Serializer<UserSettings> {
    override val defaultValue: UserSettings = UserSettings.getDefaultInstance()
    
    override suspend fun readFrom(input: InputStream): UserSettings {
        return UserSettings.parseFrom(input) // protobuf 解析
    }
    
    override suspend fun writeTo(t: UserSettings, output: OutputStream) {
        t.writeTo(output) // protobuf 序列化
    }
}

// 使用（完整类型安全）
val Context.userSettingsStore by dataStore(
    fileName = "user_settings.pb",
    serializer = UserSettingsSerializer
)

userSettingsStore.data.collect { settings ->
    val name = settings.name  // 编译时类型安全的访问
    val age = settings.age
}
```

```kotlin
// 对比表（源码角度）
// Preferences DataStore:
//   存储: Map<String, Any?> 序列化为 protobuf
//   序列化器: PreferencesSerializer (内置)
//   类型安全: 运行时（Key + 类型泛型）
//   Schema: 无
//   文件大小: 较小（Key-Value）
//   API 复杂度: 简单
//
// Proto DataStore:
//   存储: 自定义 protobuf Message
//   序列化器: 用户定义Serializer<ProtoMessage>
//   类型安全: 编译时（Proto 定义）
//   Schema: 有（.proto 文件）
//   文件大小: 可优化（protobuf 字段编号）
//   API 复杂度: 较高（需要编写 .proto）
```

### 时序图说明

```
【两种 DataStore 的读写差异】

Preferences DataStore 读取：
dataStore.data.map { prefs -> prefs[KEY_NAME] }
  ├─ 读取 .preferences_pb 文件
  ├─ PreferencesProtoAdapter 解析为 Map<String, Any>
  └─ 通过 Key<T> 的类型信息在运行时取对应类型值

Proto DataStore 读取：
dataStore.data.collect { settings -> settings.name }
  ├─ 读取 .pb 文件
  ├─ UserSettings.parseFrom(inputStream) → protobuf 解析
  └─ 返回编译时生成的 UserSettings 对象 ← 类型安全

【类型安全对比】

// Preferences DataStore — 运行时错误可能
val name: String = prefs[PreferencesKeys.NAME] // 返回 String?
val name: Int = prefs[PreferencesKeys.NAME]  // 编译通过但运行时类型不匹配

// Proto DataStore — 编译时错误
val name: String = settings.name  // 编译时就知道类型是 String
val name: Int = settings.name     // 编译错误！
```

### 面试加分点
- Preferences DataStore 适合存储少量、结构简单的配置项（如主题色、开关状态），Proto DataStore 适合存储复杂结构对象。
- Proto DataStore 的 Schema 带来编译时类型安全、前后兼容（protobuf 的字段编号机制）和更好的性能（二进制编码更紧凑）。
- protobuf 的向后兼容性：添加新字段不破坏旧数据，移除字段只需标记 `reserved`。
- Preferences DataStore 内部也使用 protobuf 存储，但不能自定义 Schema。

### 常见误区
- **误区**：Proto DataStore 一定比 Preferences DataStore 快。**正解**：对于少量简单键值对，Preferences DataStore 足够。Proto DataStore 适合复杂对象。
- **误区**：两种 DataStore 可以互相转换数据。**正解**：存储格式不同，需要手动实现迁移逻辑。
- **误区**：Preferences DataStore 的 Key 在运行时类型检查足够安全。**正解**：Key 的泛型在运行时擦除，`stringPreferencesKey` 只是标注了类型但无编译保证。

---

<a id="第50题datastore-的异常处理和迁移"></a>
### 第50题：DataStore 的异常处理和迁移

### 问题描述
DataStore 如何处理文件损坏？`SharedPreferencesMigration` 如何工作？

### 核心源码分析

```kotlin
// DataStore 的异常处理机制
internal class DataStoreImpl<T>(
    private val storage: Storage<T>,
    private val serializer: Serializer<T>,
    private val scope: CoroutineScope
) : DataStore<T> {
    
    // 读取时的异常处理
    private suspend fun readData(): T {
        return try {
            storage.readFrom().use { input ->
                if (input.available() > 0) {
                    serializer.readFrom(input)  // 正常读取
                } else {
                    serializer.defaultValue      // 空文件，用默认值
                }
            }
        } catch (ex: IOException) {
            // IOException 自动恢复，返回默认值
            Log.e(TAG, "Failed to read data", ex)
            serializer.defaultValue  // 损坏文件时返回默认值
        }
    }
    
    // 写入时先写临时文件
    override suspend fun updateData(transform: suspend (t: T) -> T): T {
        return actor.offerAndAwait {
            val currentData = readData()
            val newData = transform(currentData)
            
            try {
                // 先写临时文件
                val file = storage.file
                val tmpFile = File(file.absolutePath + ".tmp")
                tmpFile.outputStream().use { output ->
                    serializer.writeTo(newData, output)
                }
                // 原子替换
                if (!tmpFile.renameTo(file)) {
                    throw IOException("Failed to rename temp file")
                }
            } catch (ex: IOException) {
                throw ex  // 写入失败，旧文件保持不变
            }
            newData
        }
    }
}

// SharedPreferencesMigration — 从 SharedPreferences 迁移
class SharedPreferencesMigration<T>(
    private val context: Context,
    private val sharedPreferencesName: String,
    private val shouldRunMigration: (Preferences) -> Boolean = { true }
) : DataMigration<T> {
    
    override suspend fun shouldMigrate(currentData: T): Boolean {
        // 检查 SharedPreferences 文件是否还存在
        return sharedPrefsFile.exists() && 
               shouldRunMigration(currentData as Preferences)
    }
    
    override suspend fun migrate(currentData: T): T {
        // 1. 读取旧 SharedPreferences
        val prefs = context.getSharedPreferences(sharedPreferencesName, Context.MODE_PRIVATE)
        val mutablePreferences = (currentData as Preferences).toMutablePreferences()
        
        // 2. 逐对迁移
        prefs.all.forEach { (key, value) ->
            when (value) {
                is String -> mutablePreferences[stringPreferencesKey(key)] = value
                is Int -> mutablePreferences[intPreferencesKey(key)] = value
                is Boolean -> mutablePreferences[booleanPreferencesKey(key)] = value
                is Float -> mutablePreferences[floatPreferencesKey(key)] = value
                is Long -> mutablePreferences[longPreferencesKey(key)] = value
                is Set<*> -> {
                    @Suppress("UNCHECKED_CAST")
                    mutablePreferences[stringSetPreferencesKey(key)] = value as Set<String>
                }
            }
        }
        
        // 3. 删除旧的 SharedPreferences 文件
        prefs.edit().clear().commit()
        sharedPrefsFile.delete()
        
        return mutablePreferences
    }
    
    override suspend fun cleanUp() {
        // 迁移完成后删除旧文件
        sharedPrefsFile.delete()
    }
}
```

### 时序图说明

```
【文件损坏恢复流程】

DataStore 初始化 → readData()
  ├─ 尝试读取文件
  ├─ 文件不存在 → serializer.defaultValue ✓
  ├─ 文件存在但为空 → serializer.defaultValue ✓
  ├─ 文件存在且有效 → serializer.readFrom(input) → 数据 ✓
  └─ 文件存在但损坏（IOException）
      └─ catch (IOException) → Log 错误
          └─ serializer.defaultValue → 返回默认值 ✓
              └─ 下次 updateData 时会覆盖损坏文件

【SharedPreferences 迁移流程】

DataStore 构建时：
DataStoreFactory.create(
    serializer, 
    migrations = listOf(SharedPreferencesMigration(context, "my_prefs"))
)
  └─ shouldMigrate() → SharedPreferences 文件存在？
      ├─ 是 → migrate()
      │   ├─ 读取 SP 所有键值对
      │   ├─ 逐对转为 Preferences Key-Value
      │   ├─ 写入 DataStore
      │   └─ 删除旧 SP 文件（.edit().clear().commit() + .delete()）
      └─ 否 → 跳过迁移
```

### 面试加分点
- 先写临时文件再原子重命名的策略保证了**写入原子性**：系统在 `renameTo()` 之前断电，旧文件完整保留。
- `IOException` 自动恢复：文件损坏时降级为返回 `defaultValue`，同时旧文件不会丢失（下次 `updateData` 时覆盖）。
- `SharedPreferencesMigration` 支持条件迁移（`shouldRunMigration`），可控制是否迁移（如仅首次启动时）。
- 迁移支持链式组合：`migrations = listOf(migrationV1_2, migrationV2_3)`，按顺序执行。

### 常见误区
- **误区**：DataStore 文件永远不会损坏。**正解**：可能因为磁盘满、系统崩溃、写入中断等原因损坏，但 IOException 自动恢复保证了可用性。
- **误区**：`SharedPreferencesMigration` 自动删除旧的 SP 文件。**正解**：是的，`migrate()` 完成后自动调用 `cleanUp()` 删除。
- **误区**：DataStore 的 `updateData()` 可能部分写入。**正解**：临时文件 + 重命名方案保证原子性，要么完全写入，要么根本不写入。

---

> **文档完成**：共 50 题，覆盖 ViewModel、LiveData、Lifecycle、Room、Navigation、Paging 3、WorkManager、Hilt/Dagger、DataStore 九大模块的源码深度解析。

