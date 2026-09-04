package com.zzzangantuk.podzzz.ui.route.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.History
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.zzzangantuk.podzzz.R
import com.zzzangantuk.podzzz.api.db.model.PodcastEpisodeBundle
import com.zzzangantuk.podzzz.api.db.model.PodcastEpisodeModel
import com.zzzangantuk.podzzz.api.db.model.PodcastHistoryBundle
import com.zzzangantuk.podzzz.ui.component.PodzzzSnackbarHost
import com.zzzangantuk.podzzz.ui.component.common.BackButton
import com.zzzangantuk.podzzz.ui.component.common.swipeable.SwipeableItem
import com.zzzangantuk.podzzz.ui.component.common.swipeable.SwipeableItemActionResult
import com.zzzangantuk.podzzz.ui.component.common.swipeable.SwipeableItemActions
import com.zzzangantuk.podzzz.ui.component.layout.InfoLayout
import com.zzzangantuk.podzzz.ui.component.layout.ListHeading
import com.zzzangantuk.podzzz.ui.component.media.FloatingMediaPlayerSpacer
import com.zzzangantuk.podzzz.ui.component.model.episode.PodcastEpisodeListItem
import com.zzzangantuk.podzzz.ui.formatPubDate
import com.zzzangantuk.podzzz.ui.helper.LocalDatabase
import com.zzzangantuk.podzzz.ui.helper.PagerScaffold
import com.zzzangantuk.podzzz.ui.vm.HistoryViewModel
import kotlinx.coroutines.CoroutineScope

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun HistoryRoute(
    onClickEpisode: (episode: PodcastEpisodeModel) -> Unit,
    onBack: () -> Unit,
) {
    val scope = rememberCoroutineScope()

    val db = LocalDatabase.current
    val vm = viewModel { HistoryViewModel(db) }

    val pager = remember { vm.historyElements }
        .collectAsLazyPagingItems()

    val todayPager = remember { vm.todayPager }
        .collectAsLazyPagingItems()

    val weekPager = remember { vm.weekPager }
        .collectAsLazyPagingItems()

    val monthPager = remember { vm.monthPager }
        .collectAsLazyPagingItems()

    val yearPager = remember { vm.yearPager }
        .collectAsLazyPagingItems()

    val olderPager = remember { vm.olderPager }
        .collectAsLazyPagingItems()

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        snackbarHost = {
            PodzzzSnackbarHost(vm.snackbarHostState)
        },
        topBar = {
            CenterAlignedTopAppBar(
                scrollBehavior = scrollBehavior,
                navigationIcon = {
                    BackButton { onBack() }
                },
                title = {
                    Text(stringResource(R.string.route_history))
                },
            )
        },
    ) { inset ->
        PagerScaffold(
            pager,
            isEmpty = {
                InfoLayout(
                    modifier = Modifier.padding(inset),
                    icon = Icons.Rounded.History,
                    title = { stringResource(R.string.route_history_empty_title) },
                ) {
                    Text(
                        text = stringResource(R.string.route_history_empty_text),
                        textAlign = TextAlign.Center,
                    )
                }
            },
        ) {
            LazyColumn(
                Modifier
                    .padding(inset)
                    .fillMaxSize(),
                state = when(pager.itemCount) {
                    0 -> LazyListState()
                    else -> vm.lazyListState
                },
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(ListItemDefaults.SegmentedGap),
            ) {
                pagerSection(
                    vm = vm,
                    scope = scope,
                    label = R.string.route_history_today,
                    pager = todayPager,
                    onClickEpisode = onClickEpisode,
                )

                pagerSection(
                    vm = vm,
                    scope = scope,
                    label = R.string.route_history_this_week,
                    pager = weekPager,
                    onClickEpisode = onClickEpisode,
                )

                pagerSection(
                    vm = vm,
                    scope = scope,
                    label = R.string.route_history_this_month,
                    pager = monthPager,
                    onClickEpisode = onClickEpisode,
                )

                pagerSection(
                    vm = vm,
                    scope = scope,
                    label = R.string.route_history_this_year,
                    pager = yearPager,
                    onClickEpisode = onClickEpisode,
                )

                pagerSection(
                    vm = vm,
                    scope = scope,
                    label = R.string.route_history_older,
                    pager = olderPager,
                    onClickEpisode = onClickEpisode,
                )

                item {
                    FloatingMediaPlayerSpacer()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
fun LazyListScope.pagerSection(
    vm: HistoryViewModel,
    scope: CoroutineScope,
    label: Int,
    pager: LazyPagingItems<PodcastHistoryBundle>,
    onClickEpisode: (episode: PodcastEpisodeModel) -> Unit,
) {
    if (pager.itemCount > 0) {
        item(
            key = "HEADER:$label",
        ) {
            ListHeading(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .animateItem(),
                heading = stringResource(label),
            )
        }

        items(
            count = pager.itemCount,
            key = pager.itemKey { it.history.id },
        ) {
            val historyElement = pager[it] ?: return@items

            val elementDeleted = stringResource(R.string.snackbar_element_deleted)

            SwipeableItem(
                scope = scope,
                modifier = Modifier.animateItem(),
                snackbarHostState = vm.snackbarHostState,
                endAction = SwipeableItemActions.DeleteAction {
                    vm.delete(historyElement)

                    SwipeableItemActionResult(
                        isDismissed = true,
                        message = elementDeleted,
                        onUndo = {
                            vm.insert(historyElement)
                        },
                    )
                },
                content = {
                    PodcastEpisodeListItem(
                        bundle = PodcastEpisodeBundle(
                            episode = historyElement.episode,
                            playState = historyElement.playState,
                            download = historyElement.download
                        ),

                        overlineText = formatPubDate(
                            LocalContext.current,
                            historyElement.history.timestamp / 1000L
                        ),
                        descriptionText = historyElement.episode.podcastTitle,

                        index = it,
                        count = pager.itemCount,

                        colors = ListItemDefaults.segmentedColors(
                            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                        ),
                    ) {
                        onClickEpisode(historyElement.episode)
                    }
                },
            )
        }
    }
}