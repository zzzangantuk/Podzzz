package com.zzzangantuk.podzzz.ui.dialog.bottomsheet

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.zzzangantuk.podzzz.api.db.model.PodcastModel
import com.zzzangantuk.podzzz.api.model.PodcastPreviewModel
import com.zzzangantuk.podzzz.ui.view.model.PodcastPreviewView
import kotlinx.coroutines.launch

class PodcastPreviewBottomSheetState {

    val shown = mutableStateOf(false)
    internal val model = mutableStateOf<PodcastPreviewModel?>(null)

    fun show(podcastPreviewModel: PodcastPreviewModel) {
        this.model.value = podcastPreviewModel
        this.shown.value = true
    }

    fun hide() {
        this.shown.value = false
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PodcastPreviewBottomSheet(
    state: PodcastPreviewBottomSheetState,
    onOpenPodcast: (podcast: PodcastModel) -> Unit
) {
    val scope = rememberCoroutineScope()
    if(!state.shown.value) return

    state.model.value?.let { preview ->
        val sheetState = rememberBottomSheetState(
            initialValue = SheetValue.Hidden,
            enabledValues = setOf(SheetValue.Hidden, SheetValue.Expanded)
        )

        ModalBottomSheet(
            onDismissRequest = { state.hide() },

            sheetState = sheetState,
            contentWindowInsets = { WindowInsets() },
            dragHandle = null,
        ) {
            Column(
                modifier = Modifier.fillMaxHeight(0.8f)
            ) {
                PodcastPreviewView(
                    podcast = preview,

                    onOpenPodcast = onOpenPodcast,
                    onBack = {
                        scope.launch {
                            sheetState.hide()
                            state.hide()
                        }
                    }
                )
            }
        }
    }
}