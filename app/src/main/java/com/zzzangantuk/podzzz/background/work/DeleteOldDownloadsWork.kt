package com.zzzangantuk.podzzz.background.work

import android.content.Context
import com.zzzangantuk.podzzz.SettingsRepository
import com.zzzangantuk.podzzz.api.db.AppDatabase
import com.zzzangantuk.podzzz.manager.DownloadManager
import kotlinx.coroutines.flow.first

class DeleteOldDownloadsWork(
    val context: Context,
    val db: AppDatabase,
    val settingsRepository: SettingsRepository = SettingsRepository(context),
) {

    suspend fun doWork() {
        val afterSeconds = settingsRepository.behavior.deleteDownloadsAfterSeconds.first()
        if (afterSeconds == -1) return

        val bundles = db.podcastEpisodeDownloads()
            .getOlderThanSync(System.currentTimeMillis() - (afterSeconds * 1000L))

        for (bundle in bundles) {
            DownloadManager.deleteEpisodeDownload(
                context = context,
                db = db,
                episode = bundle.episode,
            )
        }
    }

}