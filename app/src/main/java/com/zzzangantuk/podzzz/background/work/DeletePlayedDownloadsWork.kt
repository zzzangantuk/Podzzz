package com.zzzangantuk.podzzz.background.work

import android.content.Context
import com.zzzangantuk.podzzz.SettingsRepository
import com.zzzangantuk.podzzz.api.db.AppDatabase
import com.zzzangantuk.podzzz.manager.DownloadManager
import kotlinx.coroutines.flow.first

class DeletePlayedDownloadsWork(
    val context: Context,
    val db: AppDatabase,
    val settingsRepository: SettingsRepository = SettingsRepository(context),
) {

    suspend fun doWork() {
        if (settingsRepository.behavior.deletePlayedDownloads.first()) {
            val bundles = db.podcastEpisodeDownloads().allPlayedByTimestamp()

            for (bundle in bundles) {
                if (!bundle.playState.played) continue

                DownloadManager.deleteEpisodeDownload(
                    context = context,
                    db = db,
                    episode = bundle.episode,
                )
            }
        }
    }

}