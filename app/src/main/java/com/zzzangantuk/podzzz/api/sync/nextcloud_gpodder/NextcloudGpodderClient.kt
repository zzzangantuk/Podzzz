package com.zzzangantuk.podzzz.api.sync.nextcloud_gpodder

import android.os.Build
import com.zzzangantuk.podzzz.BuildConfig
import com.zzzangantuk.podzzz.api.sync.SyncClient
import com.zzzangantuk.podzzz.api.sync.model.result.SyncResult
import com.zzzangantuk.podzzz.api.sync.nextcloud_gpodder.route.Auth
import com.zzzangantuk.podzzz.api.sync.nextcloud_gpodder.route.EpisodeActions
import com.zzzangantuk.podzzz.api.sync.nextcloud_gpodder.route.Subscriptions
import com.zzzangantuk.podzzz.utils.json
import com.google.common.net.HttpHeaders
import io.ktor.client.HttpClient
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.basicAuth
import io.ktor.client.request.header
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json

class NextcloudGpodderClient(
    val baseUrl: String,
    val username: String = "",
    val password: String = ""
) : SyncClient {

    val httpClient = HttpClient {
        followRedirects = true

        install(DefaultRequest) {
            val appVersion = BuildConfig.VERSION_CODE

            val os = "Android ${Build.VERSION.RELEASE}"
            val model = Build.MODEL

            val github = "https://github.com/zzzangantuk/podzzz"

            header(HttpHeaders.USER_AGENT, "podzzz/$appVersion ($os; $model; +$github)")
            basicAuth(username, password)
        }

        install(ContentNegotiation) {
            json(json = json)
        }
    }

    val auth = Auth(this)
    val episodeActions = EpisodeActions(this)
    val subscriptions = Subscriptions(this)

    suspend fun <T> parseResponse(
        response: HttpResponse,
        parseResult: suspend () -> T
    ): SyncResult.Success<T> {
        if (response.status == HttpStatusCode.Unauthorized || response.status == HttpStatusCode.Forbidden)
            throw SyncResult.Unauthenticated(response)

        if (response.status != HttpStatusCode.OK)
            throw SyncResult.Failure(response)

        return SyncResult.Success(
            result = parseResult()
        )
    }
}