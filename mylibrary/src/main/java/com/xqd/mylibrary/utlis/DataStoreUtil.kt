package com.xqd.mylibrary.utlis

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.createDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.IOException

/**
 * Created by 谢邱东 on 2021/1/13 10:12.
 * NO bug
 */
object DataStoreUtil {

    private var context: Context? = null
    private var dataStore: DataStore<Preferences>? = null

    fun init(context: Context) {
        this.context = context
        dataStore = context.createDataStore("dataStorePreferences")
    }

    suspend fun getLong(key: String) = getLong(key, 0)

    suspend fun getLong(key: String, default: Long): Long {
        val data: Flow<Long> = dataStore?.data!!
            .catch {
                // 当读取数据遇到错误时，如果是 `IOException` 异常，发送一个 emptyPreferences 来重新使用
                // 但是如果是其他的异常，最好将它抛出去，不要隐藏问题
                if (it is IOException) {
                    it.printStackTrace()
                    emit(emptyPreferences())
                } else {
                    throw it
                }
            }
            .map {
                it[preferencesKey(key)] ?: default
            }
        return data.first()
    }

    suspend fun saveLong(key: String, value: Long) {
        dataStore?.edit { it[preferencesKey(key)] = value }
    }

    suspend fun getInt(key: String) = getInt(key, 0)

    suspend fun getInt(key: String, default: Int): Int {
        val data: Flow<Int> = dataStore?.data!!
            .catch {
                if (it is IOException) {
                    it.printStackTrace()
                    emit(emptyPreferences())
                } else {
                    throw it
                }
            }
            .map {
                it[preferencesKey(key)] ?: default
            }
        return data.first()
    }

    suspend fun saveInt(key: String, value: Int) {
        dataStore?.edit { it[preferencesKey(key)] = value }
    }

    suspend fun getBoolean(key: String) = getBoolean(key, false)

    suspend fun getBoolean(key: String, default: Boolean): Boolean {
        val data: Flow<Boolean> = dataStore?.data!!
            .catch {
                if (it is IOException) {
                    it.printStackTrace()
                    emit(emptyPreferences())
                } else {
                    throw it
                }
            }
            .map {
                it[preferencesKey(key)] ?: default
            }
        return data.first()
    }

    suspend fun saveBoolean(key: String, value: Boolean) {
        dataStore?.edit { it[preferencesKey(key)] = value }
    }

    suspend fun getFloat(key: String) = getFloat(key, 0f)

    suspend fun getFloat(key: String, default: Float): Float {
        val data: Flow<Float> = dataStore?.data!!
            .catch {
                if (it is IOException) {
                    it.printStackTrace()
                    emit(emptyPreferences())
                } else {
                    throw it
                }
            }
            .map {
                it[preferencesKey(key)] ?: default
            }
        return data.first()
    }

    suspend fun saveFloat(key: String, value: Float) {
        dataStore?.edit { it[preferencesKey(key)] = value }
    }

    suspend fun getString(key: String) = getString(key, "")

    suspend fun getString(key: String, default: String): String {
        val data: Flow<String> = dataStore?.data!!
            .catch {
                if (it is IOException) {
                    it.printStackTrace()
                    emit(emptyPreferences())
                } else {
                    throw it
                }
            }
            .map {
                it[preferencesKey(key)] ?: default
            }
        return data.first()
    }

    suspend fun saveString(key: String, value: String) {
        dataStore?.edit { it[preferencesKey(key)] = value }
    }
}