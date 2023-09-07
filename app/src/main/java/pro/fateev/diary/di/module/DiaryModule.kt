/*
 * Copyright 2023 Ivan Fateev
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

@file:Suppress("unused")

package pro.fateev.diary.di.module

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import pro.fateev.diary.feature.auth.data.AuthRepositoryImpl
import pro.fateev.diary.feature.auth.domain.AuthRepository
import pro.fateev.diary.feature.diary.data.DiaryRepositoryImpl
import pro.fateev.diary.feature.diary.data.MediaRepositoryImpl
import pro.fateev.diary.feature.diary.data.room.AppDatabase
import pro.fateev.diary.feature.diary.domain.DiaryRepository
import pro.fateev.diary.feature.diary.domain.MediaRepository
import pro.fateev.diary.feature.pin.data.PINRepositoryImpl
import pro.fateev.diary.feature.pin.domain.PINRepository
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class DiaryModule {
    @Singleton
    @Binds
    abstract fun provideDiaryRepo(repo: DiaryRepositoryImpl): DiaryRepository
    @Singleton
    @Binds
    abstract fun provideMediaRepo(repo: MediaRepositoryImpl): MediaRepository

    @Singleton
    @Binds
    abstract fun providePINRepo(repo: PINRepositoryImpl): PINRepository

    @Singleton
    @Binds
    abstract fun provideAuthRepo(repo: AuthRepositoryImpl): AuthRepository

    companion object {
        @Singleton
        @Provides
        fun provideDB(@ApplicationContext context: Context) : AppDatabase =
            Room.databaseBuilder(
                context,
                AppDatabase::class.java, "diary"
            ).build()

        @Singleton
        @Provides
        fun provideSharedPrefs(@ApplicationContext context: Context) : SharedPreferences {
            val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
            return EncryptedSharedPreferences.create(
                "secret_shared_prefs",
                masterKeyAlias,
                context,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
        }
    }

}
