package com.zzzangantuk.podzzz.ui.vm

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zzzangantuk.podzzz.api.db.AppDatabase
import com.zzzangantuk.podzzz.api.db.model.PodcastEpisodeBundle
import com.zzzangantuk.podzzz.api.db.model.PodcastModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

class MainViewModel(
    val db: AppDatabase,
    val defaultShowMediaPlayerBottomSheet: Boolean
) : ViewModel() {

    val hideFloatingMediaPlayer = mutableStateOf(false)

    val showMediaPlayerBottomSheet = mutableStateOf(false)

    init {
        viewModelScope.launch {
            delay(500.milliseconds)
            showMediaPlayerBottomSheet.value = defaultShowMediaPlayerBottomSheet
        }
    }

    fun fetchPodcast(
        origin: String
    ): Flow<PodcastModel> {
        return db.podcasts().get(origin)
    }

    fun fetchEpisode(
        id: String
    ): Flow<PodcastEpisodeBundle> {
        return db.podcastEpisodes().get(id)
    }

}