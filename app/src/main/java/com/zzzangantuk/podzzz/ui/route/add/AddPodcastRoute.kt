package com.zzzangantuk.podzzz.ui.route.add

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.zzzangantuk.podzzz.R
import com.zzzangantuk.podzzz.ui.component.common.BackButton
import com.zzzangantuk.podzzz.ui.view.model.AddPodcastView

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AddPodcastRoute(
    onOpenPodcast: (origin: String) -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                navigationIcon = {
                    BackButton {
                        onBack()
                    }
                },
                title = {
                    Text(stringResource(R.string.route_add_podcast))
                }
            )
        }
    ) { inset ->
        Box(
            Modifier.padding(inset)
        ) {
            AddPodcastView {
                onOpenPodcast(it)
                onBack()
            }
        }
    }
}