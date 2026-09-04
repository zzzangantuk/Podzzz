package com.zzzangantuk.podzzz

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.zzzangantuk.podzzz.ui.DeepLink
import com.zzzangantuk.podzzz.ui.Main
import com.zzzangantuk.podzzz.ui.theme.PodzzzTheme

@Composable
fun PodzzzApp(
    deepLink: DeepLink?
) {
    PodzzzTheme {
        Box(
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            Main(deepLink)
        }
    }
}