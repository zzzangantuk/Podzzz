package com.zzzangantuk.podzzz.ui.route.settings

import androidx.activity.compose.BackHandler
import androidx.annotation.OptIn
import androidx.compose.runtime.Composable
import androidx.media3.common.util.UnstableApi
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.zzzangantuk.podzzz.ui.DetailPaneKey
import com.zzzangantuk.podzzz.ui.component.common.BackButton
import com.zzzangantuk.podzzz.ui.navigation.OpmlImporting
import com.zzzangantuk.podzzz.ui.navigation.Restore
import com.zzzangantuk.podzzz.ui.route.settings.pane.SettingsAppearanceKey
import com.zzzangantuk.podzzz.ui.route.settings.pane.SettingsAppearancePane
import com.zzzangantuk.podzzz.ui.route.settings.pane.SettingsBackgroundActivityKey
import com.zzzangantuk.podzzz.ui.route.settings.pane.SettingsBackgroundActivityPane
import com.zzzangantuk.podzzz.ui.route.settings.pane.SettingsDatabaseKey
import com.zzzangantuk.podzzz.ui.route.settings.pane.SettingsDatabasePane
import com.zzzangantuk.podzzz.ui.route.settings.pane.SettingsDebugKey
import com.zzzangantuk.podzzz.ui.route.settings.pane.SettingsDebugPane
import com.zzzangantuk.podzzz.ui.route.settings.pane.SettingsDownloadsAndStorageKey
import com.zzzangantuk.podzzz.ui.route.settings.pane.SettingsDownloadsAndStoragePane
import com.zzzangantuk.podzzz.ui.route.settings.pane.SettingsPlaybackKey
import com.zzzangantuk.podzzz.ui.route.settings.pane.SettingsPlaybackPane
import com.zzzangantuk.podzzz.ui.route.settings.pane.SettingsPrivacyKey
import com.zzzangantuk.podzzz.ui.route.settings.pane.SettingsPrivacyPane
import com.zzzangantuk.podzzz.ui.route.settings.pane.SettingsSynchronizationKey
import com.zzzangantuk.podzzz.ui.route.settings.pane.SettingsSynchronizationPane
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
open class SettingsPaneKey : DetailPaneKey()

@OptIn(UnstableApi::class)
@Composable
fun SettingsPane(
    showBackButton: Boolean,

    contentKey: SettingsPaneKey,
    backStack: NavBackStack<NavKey>,

    onClose: () -> Unit
) {
    BackHandler {
        onClose()
    }

    @Composable
    fun NavigationIcon() {
        if(showBackButton) BackButton {
            onClose()
        }
    }

    when(contentKey) {
        is SettingsDatabaseKey -> {
            SettingsDatabasePane(
                navigationIcon = {
                    NavigationIcon()
                },

                onRestore = {
                    onClose()
                    backStack.add(Restore)
                },
                onOpmlImport = {
                    onClose()
                    backStack.add(OpmlImporting)
                }
            )
        }

        is SettingsAppearanceKey -> {
            SettingsAppearancePane(
                navigationIcon = {
                    NavigationIcon()
                }
            )
        }

        is SettingsPlaybackKey -> {
            SettingsPlaybackPane(
                navigationIcon = {
                    NavigationIcon()
                }
            )
        }

        is SettingsBackgroundActivityKey -> {
            SettingsBackgroundActivityPane(
                navigationIcon = {
                    NavigationIcon()
                }
            )
        }

        is SettingsDownloadsAndStorageKey -> {
            SettingsDownloadsAndStoragePane(
                navigationIcon = {
                    NavigationIcon()
                }
            )
        }

        is SettingsSynchronizationKey -> {
            SettingsSynchronizationPane(
                navigationIcon = {
                    NavigationIcon()
                }
            )
        }

        is SettingsPrivacyKey -> {
            SettingsPrivacyPane(
                navigationIcon = {
                    NavigationIcon()
                }
            )
        }

        is SettingsDebugKey -> {
            SettingsDebugPane(
                navigationIcon = {
                    NavigationIcon()
                }
            )
        }

        else -> throw Exception("Unknown instance of SettingsPaneKey")
    }
}