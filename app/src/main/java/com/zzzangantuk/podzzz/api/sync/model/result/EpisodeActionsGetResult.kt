package com.zzzangantuk.podzzz.api.sync.model.result

import com.zzzangantuk.podzzz.api.sync.model.episodeactions.EpisodeAction
import kotlinx.serialization.Serializable

@Serializable
data class EpisodeActionsGetResult(
    val actions: List<EpisodeAction>,
    val timestamp: Long
)