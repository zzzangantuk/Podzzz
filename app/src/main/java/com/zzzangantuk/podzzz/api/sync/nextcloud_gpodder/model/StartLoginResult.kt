package com.zzzangantuk.podzzz.api.sync.nextcloud_gpodder.model

import kotlinx.serialization.Serializable

@Serializable
data class Poll(
    val endpoint: String,
    val token: String
)

@Serializable
data class StartLoginResult(
    val poll: Poll,
    val login: String
)