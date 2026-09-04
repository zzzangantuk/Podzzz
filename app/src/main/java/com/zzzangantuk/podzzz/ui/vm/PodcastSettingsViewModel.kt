package com.zzzangantuk.podzzz.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zzzangantuk.podzzz.api.db.AppDatabase
import com.zzzangantuk.podzzz.api.db.model.PodcastModel
import com.zzzangantuk.podzzz.api.db.model.PodcastSubscriptionModel
import kotlinx.coroutines.launch

class PodcastSettingsViewModel : ViewModel() {

    fun toggleSubscriptionAutoDownload(
        db: AppDatabase,
        subscription: PodcastSubscriptionModel,
        enable: Boolean
    ) {
        viewModelScope.launch {
            if(enable) {
                db.podcastSubscriptions().enableAutoDownload(subscription.origin)
            } else {
                db.podcastSubscriptions().disableAutoDownload(subscription.origin)
            }
        }
    }

    fun toggleSubscriptionNotifications(
        db: AppDatabase,
        subscription: PodcastSubscriptionModel,
        enable: Boolean
    ) {
        viewModelScope.launch {
            if(enable) {
                db.podcastSubscriptions().enableNotifications(subscription.origin)
            } else {
                db.podcastSubscriptions().disableNotifications(subscription.origin)
            }
        }
    }

    fun setSkipBeginning(db: AppDatabase, podcast: PodcastModel, value: Int) {
        viewModelScope.launch {
            db.podcasts().setSkipBeginning(podcast.origin, value)
        }
    }

    fun setSkipEnding(db: AppDatabase, podcast: PodcastModel, value: Int) {
        viewModelScope.launch {
            db.podcasts().setSkipEnding(podcast.origin, value)
        }
    }

    fun setOverrideTitle(db: AppDatabase, podcast: PodcastModel, overrideTitle: String) {
        viewModelScope.launch {
            db.podcasts().setOverrideTitle(podcast.origin, overrideTitle)
        }
    }

}