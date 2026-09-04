package com.zzzangantuk.podzzz.background.work

import android.content.Context
import android.util.Log
import com.zzzangantuk.podzzz.SettingsRepository
import com.zzzangantuk.podzzz.api.db.AppDatabase
import com.zzzangantuk.podzzz.api.db.model.PodcastEpisodeDownloadState
import com.zzzangantuk.podzzz.manager.DownloadManager

class ProcessOrphanDownloadsWork(
    val context: Context,
    val db: AppDatabase,
    val settingsRepository: SettingsRepository = SettingsRepository(context),
) {

    suspend fun doWork() {
        val existingFilePaths = mutableSetOf<String>()

        val all = db.podcastEpisodeDownloads().allRandomSync()
        for (bundle in all) {
            val download = bundle.download

            val file = bundle.episode.craftDownloadFile(context)

            if (file.exists()) {
                existingFilePaths.add(file.canonicalPath)
            } else if (download.state == PodcastEpisodeDownloadState.DOWNLOADED.value) {
                Log.d(
                    "ProcessOrphanDownloadsWork",
                    "Resetting download state of " + download.episodeId + " because download file doesn't exist (anymore) ...",
                )

                // reset state if download file doesn't exist (anymore)
                db.podcastEpisodeDownloads()
                    .setState(download.episodeId, PodcastEpisodeDownloadState.NOT_DOWNLOADED.value)
            }
        }

        val downloadsDir = DownloadManager.getDownloadsDirectory(context)
        downloadsDir.walkTopDown()
            .filter { it.isFile && !it.name.startsWith(".") }
            .filter { it.canonicalPath !in existingFilePaths }
            .forEach { file ->
                try {
                    Log.d(
                        "ProcessOrphanDownloadsWork",
                        "Deleting orphaned file " + file.canonicalPath + "...",
                    )
                    file.delete()
                } catch (e: Exception) {
                    Log.e(
                        "ProcessOrphanDownloadsWork",
                        "Could not delete file " + file.canonicalPath + ".",
                    )
                    e.printStackTrace()
                }
            }
    }

}