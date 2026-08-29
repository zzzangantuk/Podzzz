package app.podiumpodcasts.podium.api.sync.model.result

import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.request

interface SyncResult<out T> {
    data class Success<out T>(val result: T) : SyncResult<T>

    open class Failure(val response: HttpResponse) : Exception() {
        override val message: String
            get() = "${response.status} / ${response.request.url}"
    }

    class Unauthenticated(response: HttpResponse) : Failure(response)

    class NotSupported<out T> : SyncResult<T>
}