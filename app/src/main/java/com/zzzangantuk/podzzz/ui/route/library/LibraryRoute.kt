package com.zzzangantuk.podzzz.ui.route.library

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Download
import androidx.compose.material.icons.rounded.History
import androidx.compose.material.icons.rounded.NewReleases
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material3.Badge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.zzzangantuk.podzzz.R
import com.zzzangantuk.podzzz.api.db.model.PodcastEpisodeModel
import com.zzzangantuk.podzzz.ui.component.library.LibraryItem
import com.zzzangantuk.podzzz.ui.component.media.FloatingMediaPlayerSpacer
import com.zzzangantuk.podzzz.ui.component.media.LocalFloatingMediaPlayerShown
import com.zzzangantuk.podzzz.ui.component.model.list.ListLibraryItem
import com.zzzangantuk.podzzz.ui.dialog.bottomsheet.ListCreateBottomSheet
import com.zzzangantuk.podzzz.ui.helper.LocalDatabase
import com.zzzangantuk.podzzz.ui.vm.DownloadsViewModel
import com.zzzangantuk.podzzz.ui.vm.HistoryViewModel
import com.zzzangantuk.podzzz.ui.vm.home.ContinuePlayingViewModel
import com.zzzangantuk.podzzz.ui.vm.home.NewEpisodesViewModel
import com.zzzangantuk.podzzz.ui.vm.library.LibraryViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun LibraryRoute(
    onClickDownloads: () -> Unit,
    onClickHistory: () -> Unit,
    onClickContinuePlaying: () -> Unit,
    onClickNewEpisodes: () -> Unit,

    onClickPodcast: (origin: String) -> Unit,
    onClickEpisode: (episode: PodcastEpisodeModel) -> Unit,
    onClickList: (listId: Int) -> Unit
) {
    val db = LocalDatabase.current

    val vm = viewModel { LibraryViewModel(db) }

    val systemLists = vm.systemLists.collectAsLazyPagingItems()
    val nonSystemLists = vm.nonSystemLists.collectAsLazyPagingItems()

    val downloadsVm = viewModel { DownloadsViewModel(db) }
    val historyVm = viewModel { HistoryViewModel(db) }
    val continuePlayingVm = viewModel { ContinuePlayingViewModel(db) }
    val newEpisodesVm = viewModel { NewEpisodesViewModel(db) }

    val downloadsPager = remember { downloadsVm.downloads }.collectAsLazyPagingItems()
    val historyPager = remember { historyVm.historyElements }.collectAsLazyPagingItems()
    val continuePlayingPager =
        remember { continuePlayingVm.continuePlaying }.collectAsLazyPagingItems()
    val newEpisodesPager = remember { newEpisodesVm.newEpisodes }.collectAsLazyPagingItems()

    val scrollBehavior = SearchBarDefaults.enterAlwaysSearchBarScrollBehavior()

    Scaffold(
        Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LibrarySearch(
                scrollBehavior = scrollBehavior,

                onClickPodcast = onClickPodcast,
                onClickEpisode = onClickEpisode
            )
        }
    ) { inset ->
        val floatingMediaPlayerShown = LocalFloatingMediaPlayerShown.current

        LazyVerticalGrid(
            state = if(
                (systemLists.loadState.isIdle || systemLists.itemCount > 0)
                && (nonSystemLists.loadState.isIdle || nonSystemLists.itemCount > 0)
            ) {
                vm.lazyGridState
            } else {
                rememberLazyGridState()
            },
            modifier = Modifier.padding(inset),
            columns = GridCells.Adaptive(150.dp),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                LibraryItem(
                    title = stringResource(R.string.route_downloads),
                    icon = Icons.Rounded.Download,
                    contentDescription = stringResource(R.string.route_downloads),
                    badge = {
                        if(downloadsPager.itemCount > 0) Badge(
                            containerColor = MaterialTheme.colorScheme.primary
                        ) {
                            Text(
                                text = downloadsPager.itemCount.toString()
                            )
                        }
                    },
                    onClick = onClickDownloads
                )
            }

            item {
                LibraryItem(
                    title = stringResource(R.string.route_history),
                    icon = Icons.Rounded.History,
                    contentDescription = stringResource(R.string.route_history),
                    badge = {
                        if(historyPager.itemCount > 0) Badge(
                            containerColor = MaterialTheme.colorScheme.primary
                        ) {
                            Text(
                                text = historyPager.itemCount.toString()
                            )
                        }
                    },
                    onClick = onClickHistory
                )
            }

            item {
                LibraryItem(
                    title = stringResource(R.string.route_continue_playing),
                    icon = Icons.Rounded.Pause,
                    contentDescription = stringResource(R.string.route_continue_playing),
                    badge = {
                        if(continuePlayingPager.itemCount > 0) Badge(
                            containerColor = MaterialTheme.colorScheme.primary
                        ) {
                            Text(
                                text = continuePlayingPager.itemCount.toString()
                            )
                        }
                    },
                    onClick = onClickContinuePlaying
                )
            }

            item {
                LibraryItem(
                    title = stringResource(R.string.route_new_episodes),
                    icon = Icons.Rounded.NewReleases,
                    contentDescription = stringResource(R.string.route_new_episodes),
                    badge = {
                        if(newEpisodesPager.itemCount > 0) Badge(
                            containerColor = MaterialTheme.colorScheme.primary
                        ) {
                            Text(
                                text = newEpisodesPager.itemCount.toString()
                            )
                        }
                    },
                    onClick = onClickNewEpisodes
                )
            }

            item(span = { GridItemSpan(maxLineSpan) }) {
                HorizontalDivider(
                    Modifier.padding(24.dp)
                )
            }

            items(systemLists.itemCount) {
                systemLists[it]?.let { list ->
                    ListLibraryItem(
                        modifier = Modifier.animateItem(),
                        list = list,
                        onClick = {
                            onClickList(list.id)
                        }
                    )
                }
            }

            items(nonSystemLists.itemCount) {
                nonSystemLists[it]?.let { list ->
                    ListLibraryItem(
                        modifier = Modifier.animateItem(),
                        list = list,
                        onClick = {
                            onClickList(list.id)
                        }
                    )
                }
            }

            item {
                LibraryItem(
                    modifier = Modifier.animateItem(),
                    title = stringResource(R.string.common_action_create),
                    icon = Icons.Rounded.Add,
                    contentDescription = stringResource(R.string.common_action_create),
                    onClick = {
                        vm.showListCreateBottomSheet.value = true
                    }
                )
            }

            if(floatingMediaPlayerShown) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    FloatingMediaPlayerSpacer()
                }
            }
        }
    }

    if(vm.showListCreateBottomSheet.value) ListCreateBottomSheet {
        vm.showListCreateBottomSheet.value = false
    }
}