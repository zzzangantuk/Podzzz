package com.zzzangantuk.podzzz.api.sync.gpodder.route

import com.zzzangantuk.podzzz.api.sync.gpodder.GpodderClient
import com.zzzangantuk.podzzz.api.sync.model.result.SyncResult
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.path
import io.ktor.http.takeFrom

class Device(
    client: GpodderClient,
) : ApiRoute(client) {

    /**
     * Update device
     *
     * @param deviceType Type of device. Can be one of: desktop, laptop, mobile, server, other
     * @return whether update was successful
     */
    suspend fun update(
        deviceType: String = "mobile"
    ): SyncResult.Success<Any> {
        val response = client.httpClient.post {
            url {
                takeFrom(client.baseUrl)
                path("api", "2", "devices", client.username, "${client.deviceId}.json")
            }

            contentType(ContentType.Application.Json)

            setBody(
                mapOf(
                    "caption" to client.deviceCaption,
                    "type" to deviceType
                )
            )
        }

        return client.parseResponse(
            response = response
        )
    }

}