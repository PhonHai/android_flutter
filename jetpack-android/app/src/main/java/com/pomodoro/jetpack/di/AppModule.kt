package com.pomodoro.jetpack.di

import android.content.Context
import androidx.room.Room
import com.pomodoro.jetpack.data.PomodoroDao
import com.pomodoro.jetpack.data.PomodoroDatabase
import com.pomodoro.jetpack.network.NasApiService
import com.pomodoro.jetpack.network.OkHttpClientProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * Hilt DI 模块 — 依赖注入配置
 *
 * 【面试题34：Hilt vs Dagger vs Koin】
 *
 *   Hilt 是 Dagger 的 Android 封装，基于编译期注解：
 *   - @HiltAndroidApp: 触发代码生成
 *   - @Module @InstallIn: 声明模块，指定生命周期（SingletonComponent 全局单例）
 *   - @Provides @Singleton: 提供实例，Hilt 管理生命周期
 *   - 对比 Koin：Hilt 编译期检查 → 错误更早暴露；Koin 运行时 → 启动快但无编译期检查
 *
 * 【面试题50：DI 的原理和好处】
 *   - 控制反转（IoC）：不由使用者自己 new，交给容器管理
 *   - 依赖注入（DI）：容器自动把依赖传给使用者
 *   - 好处：解耦、可测试（Mock 注入）、生命周期自动管理
 *
 * 注入链路（NAS 文件列表为例）：
 *   AppModule → OkHttpClient → Retrofit → NasApiService → FileRepository → FileViewModel
 *                                              ↑               ↑
 *                                         @Provides      @Inject constructor
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideOkHttpClient() = OkHttpClientProvider.create()

    /**
     * Retrofit 配置
     *
     * baseUrl: "http://localhost/" 仅用于 Mock 拦截器
     * 真实 NAS 环境替换为设备 IP（如 "http://192.168.1.100:5000/"）
     */
    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: okhttp3.OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://localhost/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideNasApiService(retrofit: Retrofit): NasApiService {
        return retrofit.create(NasApiService::class.java)
    }

    /**
     * Room 数据库
     *
     * 之前：PomodoroDatabase.getInstance(context) 手动单例
     * 现在：Hilt 管理 Room 单例，@Provides @Singleton 保证全局唯一
     */
    @Provides
    @Singleton
    fun providePomodoroDatabase(@ApplicationContext context: Context): PomodoroDatabase {
        return Room.databaseBuilder(
            context,
            PomodoroDatabase::class.java,
            "pomodoro_db"
        ).build()
    }

    @Provides
    @Singleton
    fun providePomodoroDao(database: PomodoroDatabase): PomodoroDao {
        return database.pomodoroDao()
    }
}