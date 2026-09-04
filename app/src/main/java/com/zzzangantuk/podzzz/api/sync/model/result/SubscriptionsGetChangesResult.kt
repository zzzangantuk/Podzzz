package com.zzzangantuk.podzzz.api.sync.model.result

import kotlinx.serialization.Serializable

@Serializable
data class SubscriptionsGetChangesResult(
    val add: List<String>,
    val remove: List<String>,
    val timestamp: Long
)