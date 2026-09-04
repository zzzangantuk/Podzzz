package com.zzzangantuk.podzzz.ui.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.togetherWith
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.ui.NavDisplay
import com.zzzangantuk.podzzz.api.db.model.PodcastEpisodeModel
import com.zzzangantuk.podzzz.ui.DetailPaneKey
import com.zzzangantuk.podzzz.ui.route.add.AddPodcastRoute
import com.zzzangantuk.podzzz.ui.route.content.ContinuePlayingRoute
import com.zzzangantuk.podzzz.ui.route.content.LocallyAvailableRoute
import com.zzzangantuk.podzzz.ui.route.content.NewEpisodesRoute
import com.zzzangantuk.podzzz.ui.route.content.SubscriptionsRoute
import com.zzzangantuk.podzzz.ui.route.discover.DiscoverRoute
import com.zzzangantuk.podzzz.ui.route.downloads.DownloadsRoute
import com.zzzangantuk.podzzz.ui.route.history.HistoryRoute
import com.zzzangantuk.podzzz.ui.route.home.HomeRoute
import com.zzzangantuk.podzzz.ui.route.importing.OpmlImportingRoute
import com.zzzangantuk.podzzz.ui.route.library.LibraryRoute
import com.zzzangantuk.podzzz.ui.route.licenses.LicensesRoute
import com.zzzangantuk.podzzz.ui.route.list.ListRoute
import com.zzzangantuk.podzzz.ui.route.restore.RestoreRoute
import com.zzzangantuk.podzzz.ui.route.settings.SettingsRoute
import kotlinx.serialization.Serializable

// Routes
@Serializable
open class PodzzzNavKey(
    val showNavBar: Boolean = true,
    val showMediaPlayer: Boolean = true,
) : NavKey

@Serializable
data object Home : PodzzzNavKey()
@Serializable
data object Discover : PodzzzNavKey()
@Serializable
data object Library : PodzzzNavKey()

@Serializable
data object History : PodzzzNavKey(showNavBar = false)
@Serializable
data object Downloads : PodzzzNavKey(showNavBar = false)
@Serializable
data object Subscriptions : PodzzzNavKey(showNavBar = false)
@Serializable
data object ContinuePlaying : PodzzzNavKey(showNavBar = false)
@Serializable
data object NewEpisodes : PodzzzNavKey(showNavBar = false)
@Serializable
data object LocallyAvailable : PodzzzNavKey(showNavBar = false)

@Serializable
data class List(val listId: Int) : PodzzzNavKey(showNavBar = false)

@Serializable
data object AddPodcast : PodzzzNavKey(showNavBar = false, showMediaPlayer = false)
@Serializable
data object Settings : PodzzzNavKey(showNavBar = false, showMediaPlayer = false)
@Serializable
data object Licenses : PodzzzNavKey(showNavBar = false, showMediaPlayer = false)
@Serializable
data object Restore : PodzzzNavKey(showNavBar = false, showMediaPlayer = false)

@Serializable
data object OpmlImporting : PodzzzNavKey(showNavBar = false, showMediaPlayer = false)
@Serializable
data object Unknown : PodzzzNavKey()

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun Navigation(
    backStack: NavBackStack<NavKey>,

    onOpenPane: (key: DetailPaneKey) -> Unit,
    onClosePane: () -> Unit,

    onBack: () -> Unit = { },

    onClickPodcast: (origin: String) -> Unit,
    onClickEpisode: (episode: PodcastEpisodeModel) -> Unit,
) {
    NavDisplay(
        backStack = backStack,
        onBack = onBack,
        transitionSpec = {
            (fadeIn(animationSpec = tween(220, delayMillis = 90)) +
                    scaleIn(initialScale = 0.92f, animationSpec = tween(220, delayMillis = 90)))
                .togetherWith(fadeOut(animationSpec = tween(90)))
        },
        popTransitionSpec = {
            (fadeIn(animationSpec = tween(220, delayMillis = 90)) +
                    scaleIn(initialScale = 0.92f, animationSpec = tween(220, delayMillis = 90)))
                .togetherWith(fadeOut(animationSpec = tween(90)))
        },
        predictivePopTransitionSpec = {
            (fadeIn(animationSpec = tween(220, delayMillis = 90)) +
                    scaleIn(initialScale = 0.92f, animationSpec = tween(220, delayMillis = 90)))
                .togetherWith(fadeOut(animationSpec = tween(90)))
        },
    ) { key ->
        when (key) {
                is Home -> NavEntry(key) {
                    HomeRoute(
                        onSettings = { backStack.add(Settings) },

                        onClickSubscriptions = { backStack.add(Subscriptions) },
                        onClickContinuePlaying = { backStack.add(ContinuePlaying) },
                        onClickNewEpisodes = { backStack.add(NewEpisodes) },
                        onClickLocallyAvailable = { backStack.add(LocallyAvailable) },

                        onClickAddPodcast = { backStack.add(AddPodcast) },

                        onClickDiscover = { backStack.add(Discover) },

                        onClickPodcast = onClickPodcast,
                        onClickEpisode = onClickEpisode,
                    )
                }

                is Discover -> NavEntry(key) {
                    DiscoverRoute(
                        onClickPodcast = { podcast -> onClickPodcast(podcast) }
                    )
                }

                is Library -> NavEntry(key) {
                    LibraryRoute(
                        onClickDownloads = { backStack.add(Downloads) },
                        onClickHistory = { backStack.add(History) },
                        onClickContinuePlaying = { backStack.add(ContinuePlaying) },
                        onClickNewEpisodes = { backStack.add(NewEpisodes) },

                        onClickPodcast = onClickPodcast,
                        onClickEpisode = onClickEpisode,

                        onClickList = {
                            backStack.add(List(it))
                        }
                    )
                }

                is History -> NavEntry(key) {
                    HistoryRoute(
                        onClickEpisode = onClickEpisode
                    ) {
                        backStack.removeLastOrNull()
                    }
                }

                is Downloads -> NavEntry(key) {
                    DownloadsRoute(
                        onClickEpisode = onClickEpisode
                    ) {
                        backStack.removeLastOrNull()
                    }
                }

                is Subscriptions -> NavEntry(key) {
                    SubscriptionsRoute(
                        onClickPodcast = onClickPodcast
                    ) {
                        backStack.removeLastOrNull()
                    }
                }

                is ContinuePlaying -> NavEntry(key) {
                    ContinuePlayingRoute(
                        onClickEpisode = onClickEpisode
                    ) {
                        backStack.removeLastOrNull()
                    }
                }

                is NewEpisodes -> NavEntry(key) {
                    NewEpisodesRoute(
                        onClickEpisode = onClickEpisode
                    ) {
                        backStack.removeLastOrNull()
                    }
                }

                is LocallyAvailable -> NavEntry(key) {
                    LocallyAvailableRoute(
                        onClickPodcast = onClickPodcast
                    ) {
                        backStack.removeLastOrNull()
                    }
                }


                is List -> NavEntry(key) {
                    ListRoute(
                        listId = key.listId,
                        onClickPodcast = onClickPodcast,
                        onClickEpisode = onClickEpisode
                    ) {
                        backStack.removeLastOrNull()
                    }
                }


                is AddPodcast -> NavEntry(key) {
                    AddPodcastRoute(
                        onOpenPodcast = onClickPodcast
                    ) {
                        backStack.removeLastOrNull()
                    }
                }

                is Settings -> NavEntry(key) {
                    SettingsRoute(
                        onLicenses = {
                            backStack.add(Licenses)
                        },
                        onPane = {
                            onOpenPane(
                                it,
                            )
                        },
                    ) {
                        onClosePane()
                        backStack.removeLastOrNull()
                    }
                }

                is Licenses -> NavEntry(key) {
                    LicensesRoute {
                        backStack.removeLastOrNull()
                    }
                }

                is Restore -> NavEntry(key) {
                    RestoreRoute()
                }

                is OpmlImporting -> NavEntry(key) {
                    OpmlImportingRoute {
                        backStack.removeLastOrNull()
                    }
                }


                else -> NavEntry(Unknown) {
                    Text("Unknown route")
                }
            }
        }
}