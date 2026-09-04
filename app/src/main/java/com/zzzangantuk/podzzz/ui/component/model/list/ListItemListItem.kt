package com.zzzangantuk.podzzz.ui.component.model.list

import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ListItemColors
import androidx.compose.material3.ListItemDefaults
import androidx.compose.runtime.Composable
import com.zzzangantuk.podzzz.api.db.model.ListItemModelBundle
import com.zzzangantuk.podzzz.api.db.model.PodcastEpisodeModel
import com.zzzangantuk.podzzz.ui.component.model.PodcastListItem
import com.zzzangantuk.podzzz.ui.component.model.episode.PodcastEpisodeListItem

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ListItemListItem(
    listItem: ListItemModelBundle,

    index: Int,
    count: Int,

    colors: ListItemColors = ListItemDefaults.segmentedColors(),

    onClickPodcast: (origin: String) -> Unit,
    onClickEpisode: (episode: PodcastEpisodeModel) -> Unit
) {
    when(listItem.episode) {
        null -> if(listItem.podcast != null) PodcastListItem(
            podcast = listItem.podcast,

            colors = colors,

            index = index,
            count = count,

            onClick = { onClickPodcast(listItem.podcast.origin) }
        )

        else -> PodcastEpisodeListItem(
            bundle = listItem.toPodcastEpisodeBundle(),

            colors = colors,

            index = index,
            count = count,

            onClick = { onClickEpisode(listItem.episode) }
        )
    }
}