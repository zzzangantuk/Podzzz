package com.zzzangantuk.podzzz.background.worker

import android.content.Context
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.zzzangantuk.podzzz.SettingsRepository
import com.zzzangantuk.podzzz.background.work.PodcastEpisodeDownloadWork
import com.zzzangantuk.podzzz.manager.DatabaseManager
import kotlinx.coroutines.flow.first

const val KEY_EPISODE_ID = "KEY_EPISODE_ID"

class PodcastEpisodeDownloadWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(
    context, params
) {
    val db by lazy {
        DatabaseManager.build(context)
    }

    override suspend fun doWork(): Result {
        return try {
            val episodeId = inputData.getString(KEY_EPISODE_ID)
                ?: throw NullPointerException()

            val worker = PodcastEpisodeDownloadWork(applicationContext, db)
            val result = worker.doWork(episodeId)

            if(result) {
                Result.success()
            } else {
                Result.failure()
            }
        } catch(e: Exception) {
            e.printStackTrace()
            Result.failure()
        }
    }

    companion object {
        fun generateUniqueWorkName(episodeId: String): String {
            return "com.zzzangantuk.podzzz.background.worker.PodcastEpisodeDownloadWorker:$episodeId"
        }

        suspend fun enqueueDownload(
            context: Context,
            episodeId: String,
            settingsRepository: SettingsRepository = SettingsRepository(context),
            auto: Boolean = false
        ) {
            val downloadMetered = settingsRepository.behavior.downloadMetered.first()
            val downloadInRoaming = settingsRepository.behavior.downloadInRoaming.first()
            val applySettingsForAutoDownloads =
                settingsRepository.behavior.applySettingsForAutoDownloads.first()

            val podcastData = workDataOf(
                KEY_EPISODE_ID to episodeId
            )

            val networkType = when(downloadMetered) {
                false -> NetworkType.UNMETERED
                else -> when(downloadInRoaming) {
                    true -> NetworkType.CONNECTED
                    false -> NetworkType.NOT_ROAMING
                }
            }

            val downloadRequest = OneTimeWorkRequestBuilder<PodcastEpisodeDownloadWorker>()
                .setInputData(podcastData)
                .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
                .setConstraints(
                    Constraints.Builder()
                        .setRequiredNetworkType(
                            networkType = if(auto && !applySettingsForAutoDownloads)
                                NetworkType.UNMETERED
                            else
                                networkType
                        )
                        .build()
                ).build()

            WorkManager
                .getInstance(context)
                .enqueueUniqueWork(
                    generateUniqueWorkName(episodeId),
                    ExistingWorkPolicy.REPLACE,
                    downloadRequest
                )
        }

        fun stopDownload(
            context: Context,
            episodeId: String
        ) {
            WorkManager
                .getInstance(context)
                .cancelUniqueWork(generateUniqueWorkName(episodeId))
        }
    }

}