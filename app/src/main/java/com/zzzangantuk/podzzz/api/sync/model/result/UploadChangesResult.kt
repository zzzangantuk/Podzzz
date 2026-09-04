package com.zzzangantuk.podzzz.api.sync.model.result

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UploadChangesResult(
    val timestamp: Long,
    @SerialName("update_urls")
    private val _updateUrls: List<List<String>> = listOf()
) {
    @Suppress("unused")
    val updateUrls: Map<String, String>
        get() {
            return _updateUrls.associate { it[0] to it[1] }
        }

}