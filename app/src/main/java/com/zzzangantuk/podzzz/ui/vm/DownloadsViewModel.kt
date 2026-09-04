package com.zzzangantuk.podzzz.ui.vm

import androidx.compose.foundation.lazy.LazyListState
import androidx.lifecycle.ViewModel
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.zzzangantuk.podzzz.api.db.AppDatabase

class DownloadsViewModel(
    val db: AppDatabase
) : ViewModel() {

    val lazyListState = LazyListState()

    val totalSize = db.podcastEpisodeDownloads().totalSize()

    val downloads = Pager(
        PagingConfig(
            pageSize = 30
        )
    ) {
        db.podcastEpisodeDownloads()
            .all()
    }.flow

}