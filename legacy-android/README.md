# legacy-android —— 传统 Java 版（基线工程）

## 项目定位
面试学习项目的「**基线实现**」：用最传统的 Android 技术栈（Java + XML + Fragment + Navigation Component + SQLiteOpenHelper）还原一个多页面 App。
它是另外两个工程的对照起点，**功能最贴近最初的"番茄钟"原型**——没有网络、没有文件管理、没有传输模块。

## 技术栈
- 语言：Java
- UI：XML 布局 + Fragment + Navigation Component（`nav_graph.xml`）
- 状态 / 计时：Fragment 成员变量 + `Handler`
- 持久化：`SQLiteOpenHelper`（手写 SQL，`DatabaseHelper`）
- 依赖：**零** Jetpack 架构组件（无 ViewModel / LiveData / Room / Compose）

## 功能地图（当前真实状态）
底部 4 个 Tab（`TabContainerFragment` 内手动 `FragmentTransaction` 切换）：
| Tab | Fragment | 说明 |
|-----|----------|------|
| 番茄钟 | `TimerFragment` | 倒计时 + 圆环绘制 + 「进入 4 级导航演示」入口 |
| 统计 | `StatsFragment` | 番茄钟历史统计（原型里的"历史记录"承担者） |
| 设置 | `SettingsFragment` | 简单设置页 |
| 关于 | `AboutFragment` | 关于页 |

4 级导航演示（从番茄钟 Tab 内按钮进入，`nav_graph` 管理返回栈）：
`HomeFragment` → `ListFragment` → `DetailFragment/{itemId}` → `CommentFragment/{itemId}`

> ⚠️ 旧 README 写的 `ui/HistoryActivity.java` **不存在**；历史相关功能由 `StatsFragment` / `ListFragment` 承担，且全部是 Fragment 而非 Activity。

## 实际文件树（`app/src/main/` 以内）
```
app/src/main/
├── AndroidManifest.xml
├── java/com/pomodoro/legacy/
│   ├── MainActivity.java              # 宿主 Activity，承载 NavHostFragment
│   ├── TabContainerFragment.java      # 底部 4 Tab 容器（手动 FragmentTransaction）
│   ├── TimerFragment.java            # 番茄钟（计时 + 圆环 + 进入导航演示）
│   ├── StatsFragment.java            # 统计（历史）
│   ├── SettingsFragment.java         # 设置
│   ├── AboutFragment.java            # 关于
│   ├── HomeFragment.java             # 4 级导航：第 1 级
│   ├── ListFragment.java             # 4 级导航：第 2 级
│   ├── DetailFragment.java           # 4 级导航：第 3 级（带 itemId）
│   ├── CommentFragment.java          # 4 级导航：第 4 级（带 itemId）
│   ├── model/
│   │   └── PomodoroRecord.java       # 数据模型
│   └── data/
│       └── DatabaseHelper.java       # SQLiteOpenHelper 手写建表/增查
├── res/layout/                       # activity_main + 各 fragment_*.xml
└── res/navigation/nav_graph.xml      # 4 级导航图
```

## 如何运行
用 Android Studio 打开 `legacy-android/` → Sync Gradle → Run → 选模拟器。

## 与 jetpack / flutter 的差异
- 仅含「番茄钟 + 历史/统计 + 4 级导航演示 + 设置/关于」，**无 NAS 文件管理、无传输、无网络层**。
- 全部用 Fragment + 传统 XML，没有 Compose / Riverpod / ViewModel。
- 这是三个工程里**最原始、最贴近最初设计**的一个，适合作为"传统怎么做"的基线来读。
