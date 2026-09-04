package com.zzzangantuk.podzzz.ui.vm.home

import androidx.compose.foundation.lazy.LazyListState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.zzzangantuk.podzzz.api.db.AppDatabase
import com.zzzangantuk.podzzz.api.db.model.PodcastEpisodeBundle
import kotlinx.coroutines.launch

class NewEpisodesViewModel(
    val db: AppDatabase
) : ViewModel() {

    val lazyListState = LazyListState()

    val newEpisodes = Pager(
        PagingConfig(
            pageSize = 15
        )
    ) {
        db.podcastEpisodes().allNew()
    }.flow

    fun unnew(item: PodcastEpisodeBundle) {
        viewModelScope.launch {
            db.podcastEpisodes()
                .unnewAndUpdateNewEpisodesCount(item.episode.origin, item.episode.id)
        }
    }

    fun new(item: PodcastEpisodeBundle) {
        viewModelScope.launch {
            db.podcastEpisodes()
                .newAndUpdateNewEpisodesCount(item.episode.origin, item.episode.id)
        }
    }

}