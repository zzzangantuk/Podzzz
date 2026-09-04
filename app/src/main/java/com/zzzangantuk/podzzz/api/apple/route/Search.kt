package com.zzzangantuk.podzzz.api.apple.route

import androidx.core.net.toUri
import com.zzzangantuk.podzzz.api.apple.ApplePodcastClient
import com.zzzangantuk.podzzz.api.apple.model.top.SearchResponse
import com.zzzangantuk.podzzz.api.model.PodcastPreviewModel
import com.zzzangantuk.podzzz.utils.json
import io.ktor.client.call.body
import io.ktor.client.request.get

class Search(
    client: ApplePodcastClient,
) : ApiRoute(client) {

    suspend fun search(
        query: String,
        countryCode: String
    ): List<PodcastPreviewModel> {
        val url = "https://itunes.apple.com/search?media=podcast".toUri()
            .buildUpon()
            .appendQueryParameter("country", countryCode)
            .appendQueryParameter("term", query)
            .build()

        val body = client.httpClient.get(url.toString()).body<String>()

        val response = json.decodeFromString<SearchResponse>(body)
        return response.results.mapNotNull { it.toPodcastPreview() }
    }

}