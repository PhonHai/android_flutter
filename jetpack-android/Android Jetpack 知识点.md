# Android Jetpack 面试宝典（Android开发必备）

> 面向：
>
> - Android传统开发工程师
> - Android面试
> - 企业级Android开发
> - Jetpack快速学习

---

# 一、Jetpack到底是什么？

Jetpack不是一个框架。

Jetpack是一套官方推荐的Android开发组件集合。

目的：

- 生命周期安全
- 减少模板代码
- MVVM开发
- 易维护
- 易测试

官方架构：

```
UI
│
ViewModel
│
Repository
│
Room / Retrofit
```

---

# 二、Jetpack最重要的组件（★★★★★）

按照企业使用频率排序：

★★★★★ 必会

- Lifecycle
- ViewModel
- LiveData
- Navigation
- Room
- DataStore
- WorkManager
- Paging3

★★★★

- SavedStateHandle
- ViewBinding
- Coroutine
- Flow

★★★

- Hilt(Dagger)
- CameraX
- Benchmark

★

- Compose（目前很多公司仍然View开发）

---

# 三、Lifecycle（★★★★★）

## 为什么需要Lifecycle？

传统开发：

```
Activity销毁了

Handler还在发送消息

容易内存泄漏
```

Lifecycle负责：

> 感知生命周期。

例如：

```
ON_CREATE

ON_START

ON_RESUME

ON_PAUSE

ON_STOP

ON_DESTROY
```

LifecycleOwner：

```
Activity

Fragment
```

LifecycleObserver：

```
监听生命周期变化
```

例如：

```
Camera

Location

播放器

蓝牙
```

面试常问：

> Lifecycle如何避免内存泄漏？

答：

生命周期结束自动取消监听，不需要手动管理。

---

# 四、ViewModel（★★★★★）

企业使用率：

★★★★★

作用：

保存UI数据。

Activity旋转：

```
旧Activity

↓

销毁

↓

新Activity
```

ViewModel不会销毁。

因此：

```
Activity

↓

ViewModel

↓

Repository
```

ViewModel特点：

✔ 生命周期安全

✔ 不持有View

✔ 数据共享

面试：

为什么不能在ViewModel保存Activity？

答：

容易内存泄漏。

---

# 五、LiveData（★★★★★）

作用：

生命周期感知的数据。

传统：

```
callback

Handler

interface
```

Jetpack：

```
LiveData
```

例如：

```
MutableLiveData<User>
```

更新：

```
setValue()

postValue()
```

区别：

setValue：

主线程。

postValue：

子线程。

观察：

```
observe()
```

特点：

Activity销毁：

自动取消观察。

面试：

LiveData为什么不会内存泄漏？

因为Lifecycle感知生命周期。

---

# 六、ViewModel + LiveData（★★★★★）

企业最经典写法：

```
Activity

↓

observe()

↓

ViewModel

↓

LiveData

↓

Repository

↓

Retrofit
```

UI：

```
只负责显示。
```

业务：

```
全部放ViewModel。
```

---

# 七、Navigation（★★★★★）

传统：

```
FragmentTransaction
```

现在：

```
Navigation
```

优势：

统一页面跳转。

支持：

- 返回栈
- SafeArgs
- DeepLink

主要组件：

```
NavHostFragment

NavController

navigation.xml
```

面试：

Navigation优点？

统一管理Fragment。

---

# 八、Room（★★★★★）

SQLite官方ORM。

传统：

```
SQLiteOpenHelper
```

现在：

```
Room
```

组成：

Entity

↓

Dao

↓

Database

例如：

```
User

↓

UserDao

↓

AppDatabase
```

Room支持：

- Flow
- LiveData
- Coroutine

面试：

Room为什么比SQLite方便？

自动生成SQL映射。

---

# 九、DataStore（★★★★★）

SharedPreferences升级版。

优点：

✔ 异步

✔ Flow

✔ 不阻塞UI

✔ 更安全

两种：

Preferences

Proto

企业：

Preferences最常见。

---

# 十、WorkManager（★★★★★）

后台任务。

例如：

上传日志

同步数据

下载

上传图片

特点：

即使APP退出：

仍可继续执行。

支持：

```
OneTimeWorkRequest

PeriodicWorkRequest
```

面试：

为什么不用Service？

WorkManager适合可靠后台任务。

---

# 十一、Paging3（★★★★★）

列表分页。

例如：

微博

淘宝

抖音评论

传统：

```
page=1

page=2

page=3
```

Jetpack：

PagingSource

↓

Pager

↓

PagingData

↓

Adapter

特点：

自动加载下一页。

---

# 十二、SavedStateHandle（★★★★）

作用：

恢复状态。

例如：

Activity被系统杀死。

恢复：

```
EditText内容

Tab位置
```

ViewModel：

```
SavedStateHandle
```

即可恢复。

---

# 十三、ViewBinding（★★★★）

替代：

findViewById()

例如：

```
ActivityMainBinding
```

优点：

自动生成代码。

类型安全。

---

# 十四、Coroutine（★★★★★）

Jetpack大量依赖Coroutine。

例如：

Room

Retrofit

WorkManager

Flow

必须会：

```
launch

async

withContext

Dispatchers.IO

Dispatchers.Main
```

面试：

launch和async区别？

launch：

没有返回值。

async：

返回Deferred。

---

# 十五、Flow（★★★★★）

LiveData升级版。

特点：

支持：

冷流。

支持：

多个操作符。

例如：

```
map

filter

combine

zip
```

企业：

Room

DataStore

大量使用Flow。

面试：

Flow和LiveData区别？

Flow支持：

- Kotlin
- Coroutine
- 操作符丰富

LiveData：

主要用于UI。

---

# 十六、Repository（★★★★★）

官方推荐：

```
UI

↓

ViewModel

↓

Repository

↓

Remote

↓

Local(Room)
```

Repository负责：

统一数据来源。

例如：

先查缓存。

没有：

请求网络。

---

# 十七、MVVM（★★★★★）

目前Android最主流。

```
Activity

↓

ViewModel

↓

Repository

↓

Retrofit

↓

Room
```

职责：

Activity：

显示UI。

ViewModel：

业务。

Repository：

数据。

---

# 十八、Jetpack面试最高频问题

## 1.ViewModel为什么不会因为旋转销毁？

因为ViewModelStore保存实例。

---

## 2.LiveData为什么不会泄漏？

Lifecycle自动解绑。

---

## 3.setValue和postValue区别？

set：

主线程。

post：

子线程。

---

## 4.Room和SQLite区别？

Room自动生成ORM。

---

## 5.DataStore为什么替代SP？

异步。

线程安全。

Flow支持。

---

## 6.WorkManager什么时候用？

可靠后台任务。

---

## 7.Paging3原理？

按需加载。

自动分页。

---

## 8.Navigation优点？

统一Fragment跳转。

支持SafeArgs。

---

## 9.Repository为什么存在？

统一管理数据来源。

---

## 10.MVVM为什么比MVC好？

UI和业务解耦。

方便测试。

---

# 十九、企业项目标准架构

```
Activity

↓

Fragment

↓

ViewModel

↓

Repository

↓

Retrofit
        ↓
      Room

↓

Server
```

---

# 二十、Android 岗位必须掌握（★★★★★）

## 第一梯队（必须熟练）

- Kotlin
- MVVM
- ViewModel
- LiveData
- Lifecycle
- Coroutine
- Flow
- Retrofit
- Room
- RecyclerView
- ViewBinding
- Navigation

---

## 第二梯队（最好会）

- DataStore
- Paging3
- WorkManager
- Hilt
- Glide/Coil
- OkHttp
- StateFlow
- SharedFlow

---

## 第三梯队（了解即可）

- Compose
- Flutter
- 鸿蒙ArkUI
- CameraX

---

# 二十一、推荐学习路线（7天速成）

Day1：

- Lifecycle
- ViewModel
- LiveData

Day2：

- MVVM
- Repository
- Retrofit

Day3：

- Room
- DataStore

Day4：

- Coroutine
- Flow

Day5：

- Navigation
- ViewBinding

Day6：

- WorkManager
- Paging3

Day7：

- 整体架构串联
- 高频面试题复盘
- 手写一个 MVVM Demo（登录 + 列表 + 本地缓存）

---

# 二十二、核心记忆图

```
UI(Activity/Fragment)
        │
        ▼
    ViewModel
        │
        ▼
    Repository
     ┌──────┴──────┐
     ▼             ▼
 Retrofit        Room
     │             │
     └──────┬──────┘
            ▼
          Data
            │
            ▼
    LiveData / Flow
            │
            ▼
           UI
```

---

# 总结

真正企业开发中，Jetpack并不是每个组件都会使用，但以下组件几乎是Android开发的标配：

- Lifecycle
- ViewModel
- LiveData / StateFlow
- Coroutine
- Flow
- Repository
- Room
- Navigation
- DataStore
- WorkManager
- Paging3
- ViewBinding

掌握以上内容，能够覆盖绝大多数Android应用开发和中高级Android岗位面试。