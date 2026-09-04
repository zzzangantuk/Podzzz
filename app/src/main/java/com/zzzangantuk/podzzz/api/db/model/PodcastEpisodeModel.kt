package com.zzzangantuk.podzzz.api.db.model

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey
import com.zzzangantuk.podzzz.manager.DownloadManager
import com.zzzangantuk.podzzz.utils.sha256
import java.io.File

enum class MediaMetadataExtra {
    ORIGIN,
    EPISODE_ID,
    AUDIO_URL,
    IMAGE_SEED_COLOR,
    RESUME_AT,
    IS_DOWNLOAD,
}

@Entity(
    tableName = "podcastEpisode",
    indices = [androidx.room.Index(value = ["origin"])],
    foreignKeys = [ForeignKey(
        entity = PodcastModel::class,
        parentColumns = arrayOf("origin"),
        childColumns = arrayOf("origin"),
        onDelete = CASCADE
    )]
)
data class PodcastEpisodeModel(
    @PrimaryKey
    @ColumnInfo("id")
    val id: String,
    @ColumnInfo("guid")
    val guid: String,
    @ColumnInfo("origin")
    val origin: String,
    @ColumnInfo("link")
    val link: String,
    @ColumnInfo("title")
    val title: String,
    @ColumnInfo("description")
    val description: String,
    @ColumnInfo("imageUrl")
    var imageUrl: String?,
    @ColumnInfo("author")
    val author: String,
    @ColumnInfo("pubDate")
    val pubDate: Long,
    @ColumnInfo("duration")
    val duration: Int,
    @ColumnInfo("audioUrl")
    val audioUrl: String,
    @ColumnInfo("podcastTitle")
    val podcastTitle: String,
    @ColumnInfo("imageSeedColor")
    var imageSeedColor: Int,
    @ColumnInfo("new")
    val new: Boolean = false
) {
    fun createMediaItem(
        context: Context,
        playState: PodcastEpisodePlayStateModel? = null
    ): MediaItem {
        val downloadFile = craftDownloadFile(context)
        val isDownload = downloadFile.exists()

        return MediaItem.Builder()
            .setMediaId("episode:$id")
            .setMediaMetadata(
                MediaMetadata.Builder()
                    .setTitle(title)
                    .setDescription(description)
                    .setArtist(podcastTitle)
                    .setSubtitle(podcastTitle)
                    .setDisplayTitle(title)
                    .apply {
                        if (!imageUrl.isNullOrBlank()) {
                            setArtworkUri(imageUrl?.toUri())
                        }
                    }
                    .setExtras(
                        Bundle().apply {
                            putString(MediaMetadataExtra.ORIGIN.name, origin)
                            putString(MediaMetadataExtra.EPISODE_ID.name, id)
                            putString(MediaMetadataExtra.AUDIO_URL.name, audioUrl)
                            putInt(MediaMetadataExtra.IMAGE_SEED_COLOR.name, imageSeedColor)
                            putBoolean(MediaMetadataExtra.IS_DOWNLOAD.name, isDownload)
                            
                            if (playState != null) putLong(
                                MediaMetadataExtra.RESUME_AT.name,
                                playState.state * 1000L
                            )

                            // Using literal string to bypass restricted Media3 legacy API
                            putLong(
                                "android.media.utils.EXTRA_DOWNLOAD_STATUS",
                                if (isDownload) 2L else 0L
                            )
                        }
                    )
                    .setMediaType(MediaMetadata.MEDIA_TYPE_PODCAST_EPISODE)
                    .setIsBrowsable(false)
                    .setIsPlayable(true)
                    .build()
            )
            .setUri(if (isDownload) Uri.fromFile(downloadFile) else audioUrl.toUri())
            .build()
    }

    fun craftDownloadFile(
        context: Context
    ): File {
        val podcastDownloadsDir =
            File(DownloadManager.getDownloadsDirectory(context), origin.sha256())
        if (!podcastDownloadsDir.exists()) podcastDownloadsDir.mkdirs()
        return File(podcastDownloadsDir, audioUrl.sha256())
    }
}