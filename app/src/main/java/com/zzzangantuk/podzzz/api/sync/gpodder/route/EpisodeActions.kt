package com.zzzangantuk.podzzz.api.sync.gpodder.route

import com.zzzangantuk.podzzz.api.sync.gpodder.GpodderClient
import com.zzzangantuk.podzzz.api.sync.model.episodeactions.EpisodeAction
import com.zzzangantuk.podzzz.api.sync.model.result.EpisodeActionsGetResult
import com.zzzangantuk.podzzz.api.sync.model.result.SyncResult
import com.zzzangantuk.podzzz.api.sync.model.result.UploadChangesResult
import com.zzzangantuk.podzzz.utils.json
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.path
import io.ktor.http.takeFrom
import kotlinx.serialization.json.encodeToJsonElement

class EpisodeActions(
    client: GpodderClient,
) : ApiRoute(client) {

    /**
     * Upload episode actions
     *
     * @param actions List of episode actions
     * @return whether update was successful
     */
    suspend fun upload(
        actions: List<EpisodeAction>
    ): SyncResult.Success<UploadChangesResult> {
        val response = client.httpClient.post {
            url {
                takeFrom(client.baseUrl)
                path("api", "2", "episodes", "${client.username}.json")
            }

            contentType(ContentType.Application.Json)

            setBody(
                json.encodeToJsonElement(actions)
            )
        }

        return client.parseResponse(
            response = response
        ) {
            response.body<UploadChangesResult>()
        }
    }

    /**
     * Get episode actions
     *
     * @param since UNIX timestamp (seconds)
     * @param aggregated Only return latest actions
     */
    suspend fun get(
        since: Long,
        aggregated: Boolean = true
    ): SyncResult.Success<EpisodeActionsGetResult> {
        val response = client.httpClient.get {
            url {
                takeFrom(client.baseUrl)
                path("api", "2", "episodes", "${client.username}.json")
            }

            parameter("since", since)
            parameter("aggregated", aggregated)
        }

        return client.parseResponse(
            response = response
        ) {
            response.body<EpisodeActionsGetResult>()
        }
    }

}