package com.zzzangantuk.podzzz.api.apple.route

import com.zzzangantuk.podzzz.api.apple.ApplePodcastClient
import com.zzzangantuk.podzzz.api.apple.model.Genre
import com.zzzangantuk.podzzz.api.apple.model.top.TopPodcastsResponse
import com.zzzangantuk.podzzz.api.model.PodcastPreviewModel
import com.zzzangantuk.podzzz.utils.json
import io.ktor.client.call.body
import io.ktor.client.request.get

class TopPodcasts(
    client: ApplePodcastClient
) : ApiRoute(client) {

    suspend fun load(
        countryCode: String,
        limit: Int = 50,
        genre: Genre? = null
    ): List<PodcastPreviewModel> {
        val normalizedCountryCode = if(countryCode.length == 2) countryCode else "US"
        val genreStr = genre?.let { "genre=${it.id}/" } ?: ""

        val body =
            client.httpClient.get("https://itunes.apple.com/$normalizedCountryCode/rss/toppodcasts/limit=$limit/${genreStr}explicit=true/json")
                .body<String>()

        if(!body.trimStart().startsWith("{")) {
            throw IllegalStateException("Invalid response from Apple Podcasts API")
        }

        val response = json.decodeFromString<TopPodcastsResponse>(body)
        return response.feed.entry.mapNotNull { it.toPodcastPreview() }
    }

}