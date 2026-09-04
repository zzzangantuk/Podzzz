package com.zzzangantuk.podzzz.background.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import com.zzzangantuk.podzzz.R
import com.zzzangantuk.podzzz.api.db.model.PodcastSubscriptionBundle
import com.zzzangantuk.podzzz.api.rss.FetchPodcastClientResult
import kotlin.math.roundToInt

class DebugUpdateNotification(
    val subscription: PodcastSubscriptionBundle,
    val response: FetchPodcastClientResult
) : NotificationContainer() {

    override fun notificationId(): Int = (Math.random() * 10000).roundToInt()

    override fun channelId(): String = "debug_update_worker"

    override fun createChannel(
        context: Context
    ): NotificationChannel {
        return NotificationChannel(
            channelId(),
            "Podcast update worker debug notification",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "Get notified when podcast update worker ran"
        }
    }

    override fun createNotification(
        context: Context
    ): Notification {
        val builder = NotificationCompat.Builder(context, channelId())
            .setSmallIcon(R.drawable.ic_notification_icon)
            .setContentTitle("Update work ran for » ${subscription.podcast.fetchTitle()} «")
            .setContentText(
                when(response) {
                    is FetchPodcastClientResult.Success -> "Success (200)"
                    is FetchPodcastClientResult.Unchanged -> "Not modified (304, ${response.reason})"
                    is FetchPodcastClientResult.Failure -> "Failure: " + response.e.toString()
                    else -> "UNKNOWN"
                }
            )
            .setGroup(channelId())
            .setAutoCancel(true)

        return builder.build()
    }

}