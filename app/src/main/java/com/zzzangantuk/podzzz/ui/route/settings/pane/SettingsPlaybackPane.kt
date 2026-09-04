package com.zzzangantuk.podzzz.ui.route.settings.pane

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Replay
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zzzangantuk.podzzz.R
import com.zzzangantuk.podzzz.ui.component.settings.SettingsSecondsSliderListItem
import com.zzzangantuk.podzzz.ui.custom.icons.ForwardIcon
import com.zzzangantuk.podzzz.ui.helper.LocalDatabase
import com.zzzangantuk.podzzz.ui.helper.LocalSettingsRepository
import com.zzzangantuk.podzzz.ui.route.settings.SettingsPaneKey
import com.zzzangantuk.podzzz.ui.vm.MediaPlayerViewModel
import com.zzzangantuk.podzzz.ui.vm.SettingsViewModel
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
class SettingsPlaybackKey : SettingsPaneKey()

@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SettingsPlaybackPane(
    navigationIcon: @Composable () -> Unit,
) {
    val scope = rememberCoroutineScope()

    val db = LocalDatabase.current
    val settingsRepository = LocalSettingsRepository.current

    val vm = viewModel { SettingsViewModel(db, settingsRepository) }
    val mediaPlayerViewModel = viewModel<MediaPlayerViewModel>()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                navigationIcon = navigationIcon,

                title = {
                    Text(stringResource(R.string.route_settings_playback))
                }
            )
        }
    ) {
        LazyColumn(
            Modifier
                .padding(it)
                .fillMaxSize(),
            contentPadding = PaddingValues(16.dp),

            verticalArrangement = Arrangement.spacedBy(ListItemDefaults.SegmentedGap)
        ) {
            item {
                val increment = vm.repository.behavior.playerSeekBackIncrement.collectAsState(10000)

                val seconds = remember { mutableIntStateOf(10) }
                LaunchedEffect(increment.value) { seconds.intValue = (increment.value / 1000).toInt() }

                SettingsSecondsSliderListItem(
                    icon = {
                        Icon(
                            Icons.Rounded.Replay,
                            stringResource(R.string.route_settings_player_seek_back_increment)
                        )
                    },
                    label = stringResource(R.string.route_settings_player_seek_back_increment),

                    value = seconds.intValue,
                    onValueChange = { value -> seconds.intValue = value },

                    min = 1,
                    max = 120,

                    onValueChangeFinished = {
                        scope.launch {
                            vm.repository.behavior.setPlayerSeekBackIncrement(seconds.intValue * 1000L)
                            mediaPlayerViewModel.updateSeekBackIncrement(seconds.intValue * 1000L)
                        }
                    },

                    index = 0,
                    count = 2
                )
            }

            item {
                val increment =
                    vm.repository.behavior.playerSeekForwardIncrement.collectAsState(10000)

                val seconds = remember { mutableIntStateOf(10) }
                LaunchedEffect(increment.value) { seconds.intValue = (increment.value / 1000).toInt() }

                SettingsSecondsSliderListItem(
                    icon = {
                        Icon(
                            ForwardIcon,
                            stringResource(R.string.route_settings_player_seek_forward_increment)
                        )
                    },
                    label = stringResource(R.string.route_settings_player_seek_forward_increment),

                    value = seconds.intValue,
                    onValueChange = { value -> seconds.intValue = value },

                    min = 1,
                    max = 120,

                    onValueChangeFinished = {
                        scope.launch {
                            vm.repository.behavior.setPlayerSeekForwardIncrement(seconds.intValue * 1000L)
                            mediaPlayerViewModel.updateSeekForwardIncrement(seconds.intValue * 1000L)
                        }
                    },

                    index = 1,
                    count = 2
                )
            }
        }
    }
}