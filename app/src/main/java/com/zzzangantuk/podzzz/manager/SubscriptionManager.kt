package com.zzzangantuk.podzzz.manager

import com.zzzangantuk.podzzz.api.db.AppDatabase

class SubscriptionManager(
    val db: AppDatabase
) {

    suspend fun subscribe(origin: String) {
        db.podcastSubscriptions()
            .subscribe(origin)

        db.syncActions()
            .addSubscribe(origin)
    }

    suspend fun unsubscribe(origin: String) {
        db.podcastSubscriptions()
            .unsubscribe(origin)

        db.syncActions()
            .addUnsubscribe(origin)
    }

}