package com.zzzangantuk.podzzz.background.work

import android.content.Context
import com.zzzangantuk.podzzz.api.db.AppDatabase
import com.zzzangantuk.podzzz.api.db.model.PodcastEpisodeDownloadState
import com.zzzangantuk.podzzz.api.download.HttpDownloadClient
import com.zzzangantuk.podzzz.api.download.HttpDownloadClientResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlin.time.Duration.Companion.milliseconds

class PodcastEpisodeDownloadWork(
    val context: Context,
    val db: AppDatabase,
    val httpDownloadClient: HttpDownloadClient = HttpDownloadClient(),
) {

    val progressUpdateDelay = 250L
    var lastProgressUpdate = 0L

    suspend fun doWork(
        episodeId: String
    ): Boolean {
        val bundle = db.podcastEpisodes().getSync(episodeId)
        val episode = bundle.episode

        if(bundle.download == null)
            return false

        delay(1000.milliseconds)

        db.podcastEpisodeDownloads()
            .setState(episodeId, PodcastEpisodeDownloadState.DOWNLOADING.value)

        val file = episode.craftDownloadFile(context)

        val result = withContext(Dispatchers.IO) {
            httpDownloadClient.download(
                url = episode.audioUrl,
                output = file,
                onProgress = { progress, total ->
                    if (total == 0L) return@download

                    val currentTime = System.currentTimeMillis()
                    if ((currentTime - lastProgressUpdate) < progressUpdateDelay) return@download

                    lastProgressUpdate = currentTime

                    db.podcastEpisodeDownloads()
                        .setProgress(episodeId, progress, total)
                }
            )
        }

        when(result) {
            is HttpDownloadClientResult.Success -> {
                db.podcastEpisodeDownloads()
                    .registerDownload(episodeId, file.absolutePath)
                return true
            }

            is HttpDownloadClientResult.Failure -> {
                db.podcastEpisodeDownloads()
                    .setState(episodeId, PodcastEpisodeDownloadState.NOT_DOWNLOADED.value)
                return false
            }
        }

        return false
    }

}