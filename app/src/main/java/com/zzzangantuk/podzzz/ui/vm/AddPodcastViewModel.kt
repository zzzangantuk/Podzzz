package com.zzzangantuk.podzzz.ui.vm

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zzzangantuk.podzzz.api.apple.ApplePodcastClient
import com.zzzangantuk.podzzz.api.db.AppDatabase
import com.zzzangantuk.podzzz.api.db.model.PodcastEpisodeModel
import com.zzzangantuk.podzzz.api.db.model.PodcastModel
import com.zzzangantuk.podzzz.api.rss.FetchPodcastClient
import com.zzzangantuk.podzzz.api.rss.FetchPodcastClientResult
import com.zzzangantuk.podzzz.manager.AddPodcastResult
import com.zzzangantuk.podzzz.manager.PodcastManager
import com.zzzangantuk.podzzz.utils.rss.toPodcast
import com.zzzangantuk.podzzz.utils.rss.toPodcastEpisode
import kotlinx.coroutines.launch

interface AddPodcastState {
    open class Idle : AddPodcastState
    open class Loading : AddPodcastState
    open class Preview(
        val imageUrl: String,
        val podcast: PodcastModel,
        val episodes: List<PodcastEpisodeModel>
    ) : AddPodcastState

    open class Done(
        val podcast: PodcastModel
    ) : AddPodcastState

    open class Duplicate(
        val duplicate: PodcastModel
    ) : AddPodcastState

    open class Error(
        val reason: String
    ) : AddPodcastState
}

class AddPodcastViewModel(
    val db: AppDatabase
) : ViewModel() {

    val podcastManager = PodcastManager(db)

    val fetchPodcastClient = FetchPodcastClient()
    val applePodcastClient = ApplePodcastClient()

    var state by mutableStateOf<AddPodcastState>(AddPodcastState.Idle())

    var origin by mutableStateOf("")
    var seedColor by mutableStateOf<Color?>(null)

    fun fetchRssPodcast() {
        viewModelScope.launch {
            state = AddPodcastState.Loading()

            try {
                val response = fetchPodcastClient.fetchNoCache(origin)

                if(response !is FetchPodcastClientResult.Success)
                    throw Exception(response.toString())

                val podcast = response.rssChannel.toPodcast(origin, response.fileSize, null)
                val episodes =
                    response.rssChannel.items.map { it.toPodcastEpisode(podcast = podcast) }

                seedColor = null
                state = AddPodcastState.Preview(
                    imageUrl = response.rssChannel.image?.url ?: "",
                    podcast = podcast,
                    episodes = episodes
                )
            } catch(e: Exception) {
                e.printStackTrace()
                state = AddPodcastState.Error(
                    reason = e.toString()
                )
            }
        }
    }

    fun addPodcast() {
        viewModelScope.launch {
            if(state is AddPodcastState.Preview) {
                try {
                    val podcast = (state as AddPodcastState.Preview).podcast
                    val episodes = (state as AddPodcastState.Preview).episodes

                    val result = podcastManager.addPodcast(
                        podcast = podcast,
                        episodes = episodes,
                        seedColor = seedColor
                    )

                    state = when(result) {
                        is AddPodcastResult.Duplicate ->
                            AddPodcastState.Duplicate(result.duplicate)

                        is AddPodcastResult.Created ->
                            AddPodcastState.Done(result.podcast)

                        else ->
                            AddPodcastState.Error("UNHANDLED_RESPONSE")
                    }
                } catch(e: Exception) {
                    e.printStackTrace()
                    state = AddPodcastState.Error(
                        reason = e.toString()
                    )
                }
            }
        }
    }

}