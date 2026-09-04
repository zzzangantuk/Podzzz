package com.zzzangantuk.podzzz

import android.app.Application
import android.content.Context
import com.zzzangantuk.podzzz.background.worker.NightlyWorker
import com.zzzangantuk.podzzz.background.worker.PeriodicPodcastUpdateWorker
import com.zzzangantuk.podzzz.background.worker.sync.PartialSynchronizationWorker
import coil3.ImageLoader
import coil3.SingletonImageLoader
import coil3.request.crossfade
import coil3.util.DebugLogger
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class App : Application(), SingletonImageLoader.Factory {
    override fun onCreate() {
        super.onCreate()

        val settingsRepository = SettingsRepository(this)

        MainScope().launch {
            if(settingsRepository.sync.enable.first()) {
                PartialSynchronizationWorker.schedule(
                    context = this@App
                )

                PartialSynchronizationWorker.enqueue(
                    context = this@App
                )
            }

            PeriodicPodcastUpdateWorker.enqueueWorker(
                context = this@App,
                settingsRepository = settingsRepository,
                replace = false
            )
        }

        NightlyWorker.scheduleNightlyWork(this)
    }

    override fun newImageLoader(context: Context): ImageLoader {
        return ImageLoader.Builder(context)
            .logger(DebugLogger())
            .crossfade(true)
            .build()
    }
}