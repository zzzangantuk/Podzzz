package com.zzzangantuk.podzzz.ui.dialog.bottomsheet

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import com.zzzangantuk.podzzz.api.db.model.PodcastModel
import com.zzzangantuk.podzzz.ui.view.model.PodcastSettingsView
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PodcastSettingsBottomSheet(
    onDismiss: () -> Unit,
    podcast: PodcastModel
) {
    val scope = rememberCoroutineScope()

    val sheetState = rememberBottomSheetState(
        initialValue = SheetValue.Hidden,
        enabledValues = setOf(SheetValue.Hidden, SheetValue.Expanded)
    )

    ModalBottomSheet(
        onDismissRequest = {
            onDismiss()
        },

        sheetState = sheetState,
        contentWindowInsets = { WindowInsets() },
        dragHandle = null,
    ) {
        PodcastSettingsView(
            podcast = podcast,
            onBack = {
                scope.launch {
                    sheetState.hide()
                    onDismiss()
                }
            }
        )
    }
}