package com.zzzangantuk.podzzz.ui.helper

import androidx.compose.runtime.compositionLocalOf
import com.zzzangantuk.podzzz.SettingsRepository
import com.zzzangantuk.podzzz.api.db.AppDatabase

val LocalDatabase = compositionLocalOf<AppDatabase> { null!! }
val LocalSettingsRepository = compositionLocalOf<SettingsRepository> { null!! }