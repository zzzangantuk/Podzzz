package com.zzzangantuk.podzzz.ui.route.licenses

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.zzzangantuk.podzzz.R
import com.zzzangantuk.podzzz.ui.component.common.BackButton
import com.mikepenz.aboutlibraries.ui.compose.android.produceLibraries
import com.mikepenz.aboutlibraries.ui.compose.m3.LibrariesContainer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LicensesRoute(
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                navigationIcon = {
                    BackButton(
                        onClick = onBack
                    )
                },
                title = {
                    Text(stringResource(R.string.route_licenses))
                }
            )
        }
    ) { inset ->
        val libraries by produceLibraries(R.raw.aboutlibraries)
        LibrariesContainer(libraries, Modifier
            .padding(inset)
            .fillMaxSize())
    }
}