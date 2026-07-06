package com.pomodoro.jetpack.network

/**
 * 统一网络请求结果封装 — sealed class
 *
 * 【面试题4：Kotlin 密封类的实际应用】
 *
 * sealed class 的优势：
 *   1. 类型安全：when 分支穷举，编译器检查是否遗漏
 *   2. 不可继承：所有子类必须在同一文件，防止外部扩展
 *   3. 对比传统 Java 写法：
 *      public class Result<T> {
 *          public boolean isSuccess;
 *          public T data;
 *          public String errorMsg;  // 散落在不同字段，容易遗漏判空
 *      }
 *
 * 使用示例：
 *   when (result) {
 *       is Result.Success -> showData(result.data)
 *       is Result.Error -> showError(result.message)
 *       is Result.Loading -> showLoading()
 *   }
 */
sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val message: String, val exception: Throwable? = null) : Result<Nothing>()
    data object Loading : Result<Nothing>()
}