package com.zzzangantuk.podzzz.manager

import android.content.Context
import com.zzzangantuk.podzzz.SettingsRepository
import com.zzzangantuk.podzzz.api.db.AppDatabase
import com.zzzangantuk.podzzz.api.db.model.PodcastEpisodeModel
import com.zzzangantuk.podzzz.background.worker.PodcastEpisodeDownloadWorker
import java.io.File

class DownloadManager {
    companion object {
        suspend fun requeueDownloads(
            context: Context,
            db: AppDatabase,
            settingsRepository: SettingsRepository = SettingsRepository(context)
        ) {
            db.podcastEpisodeDownloads().allNotDownloadedSync().forEach { download ->
                PodcastEpisodeDownloadWorker.enqueueDownload(
                    context = context,
                    episodeId = download.episodeId,
                    settingsRepository = settingsRepository
                )
            }
        }

        suspend fun downloadEpisode(
            context: Context,
            db: AppDatabase,
            episodeId: String,
            auto: Boolean = false
        ) {
            db.podcastEpisodeDownloads()
                .add(episodeId)

            PodcastEpisodeDownloadWorker.enqueueDownload(
                context = context,
                episodeId = episodeId,
                auto = auto
            )
        }

        suspend fun deleteEpisodeDownload(
            context: Context,
            db: AppDatabase,
            episode: PodcastEpisodeModel
        ) {
            val downloadFile = episode.craftDownloadFile(context)
            if(downloadFile.exists()) downloadFile.delete()

            PodcastEpisodeDownloadWorker.stopDownload(
                context = context,
                episodeId = episode.id
            )

            db.podcastEpisodeDownloads()
                .delete(episode.id)
        }

        fun getDownloadsDirectory(context: Context): File {
            val downloadsDir = File(context.noBackupFilesDir, "downloads")
            downloadsDir.mkdirs()
            return downloadsDir
        }
    }
}