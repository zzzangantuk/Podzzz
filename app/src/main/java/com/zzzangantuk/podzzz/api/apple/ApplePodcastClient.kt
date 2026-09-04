package com.zzzangantuk.podzzz.api.apple

import com.zzzangantuk.podzzz.api.apple.route.Lookup
import com.zzzangantuk.podzzz.api.apple.route.Search
import com.zzzangantuk.podzzz.api.apple.route.TopPodcasts
import com.zzzangantuk.podzzz.utils.json
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json

class ApplePodcastClient {

    val httpClient = HttpClient {
        followRedirects = true

        install(ContentNegotiation) {
            json(json = json)
        }
    }

    val lookup = Lookup(this)
    val search = Search(this)
    val topPodcasts = TopPodcasts(this)

}