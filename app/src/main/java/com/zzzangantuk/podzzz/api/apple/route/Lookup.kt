package com.zzzangantuk.podzzz.api.apple.route

import com.zzzangantuk.podzzz.api.apple.ApplePodcastClient
import com.zzzangantuk.podzzz.api.apple.model.top.LookupResponse
import com.zzzangantuk.podzzz.utils.json
import io.ktor.client.call.body
import io.ktor.client.request.get

class Lookup(
    client: ApplePodcastClient,
) : ApiRoute(client) {

    suspend fun getRssFeedUrl(
        id: String
    ): String {
        val body = client.httpClient.get("https://itunes.apple.com/lookup?id=$id")
            .body<String>()

        val response = json.decodeFromString<LookupResponse>(body)
        return response.results.first().feedUrl
    }

}