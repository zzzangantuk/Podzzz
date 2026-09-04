package com.zzzangantuk.podzzz.api.db.model

import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.room.Embedded
import androidx.room.Relation
import androidx.core.net.toUri

data class PodcastSubscriptionBundle(
    @Embedded val subscription: PodcastSubscriptionModel,
    @Relation(
        parentColumn = "origin",
        entityColumn = "origin"
    )
    val podcast: PodcastModel
) {
    fun createMediaItem(): MediaItem {
        return MediaItem.Builder()
            .setMediaId("podcast:${subscription.origin}")
            .setMediaMetadata(
                MediaMetadata.Builder()
                    .setTitle(podcast.title)
                    .setDescription(podcast.description)
                    .setArtist(podcast.author)
                    .setArtworkUri(podcast.imageUrl.toUri())
                    .setMediaType(MediaMetadata.MEDIA_TYPE_PODCAST)
                    .setIsBrowsable(true)
                    .setIsPlayable(false)
                    .build()
            )
            .build()
    }
}