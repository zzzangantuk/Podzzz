package com.zzzangantuk.podzzz.manager

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.zzzangantuk.podzzz.api.db.AppDatabase
import com.zzzangantuk.podzzz.api.db.model.PodcastEpisodeModel
import com.zzzangantuk.podzzz.api.db.model.PodcastModel
import com.zzzangantuk.podzzz.api.rss.FetchPodcastClient
import com.zzzangantuk.podzzz.api.rss.FetchPodcastClientResult
import com.zzzangantuk.podzzz.utils.rss.toPodcast
import com.zzzangantuk.podzzz.utils.rss.toPodcastEpisode

interface AddPodcastResult {
    data class Duplicate(val duplicate: PodcastModel) : AddPodcastResult
    data class Created(val podcast: PodcastModel) : AddPodcastResult
}

class PodcastManager(
    val db: AppDatabase
) {

    private val fetchPodcastClient = FetchPodcastClient()

    suspend fun addPodcast(
        origin: String,
        seedColor: Color?
    ): AddPodcastResult {
        db.podcasts().getSync(origin)?.let { duplicate ->
            return AddPodcastResult.Duplicate(
                duplicate = duplicate
            )
        }

        val response = fetchPodcastClient.fetchNoCache(origin)

        if(response !is FetchPodcastClientResult.Success)
            throw Exception(response.toString())

        val podcast = response.rssChannel.toPodcast(origin, response.fileSize, null)
        val episodes = response.rssChannel.items.map { it.toPodcastEpisode(podcast = podcast) }

        return addPodcast(podcast, episodes, seedColor, false)
    }

    suspend fun addPodcast(
        podcast: PodcastModel,
        episodes: List<PodcastEpisodeModel>,
        seedColor: Color?,
        duplicateCheck: Boolean = true
    ): AddPodcastResult {
        if(duplicateCheck) db.podcasts().getSync(podcast.origin)?.let { duplicate ->
            return AddPodcastResult.Duplicate(
                duplicate = duplicate
            )
        }

        podcast.imageSeedColor = seedColor?.toArgb() ?: 0
        episodes.forEach { it.imageSeedColor = podcast.imageSeedColor }

        db.podcasts().insertAll(podcast)
        db.podcastEpisodes().insertAll(*episodes.toTypedArray())
        episodes.forEach { db.podcastEpisodePlayStates().initState(it.id) }

        return AddPodcastResult.Created(
            podcast = podcast
        )
    }

}