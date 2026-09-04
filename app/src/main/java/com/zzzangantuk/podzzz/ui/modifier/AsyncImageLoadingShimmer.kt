package com.zzzangantuk.podzzz.ui.modifier

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import coil3.compose.AsyncImagePainter.State
import com.valentinilk.shimmer.shimmer

class AsyncImageLoadingShimmerState {
    var loadingState by mutableStateOf<State>(State.Loading(painter = null))

    @Suppress("unused")
    fun onState(): (State) -> Unit {
        return { loadingState = it }
    }
}

@Suppress("unused")
@Composable
fun Modifier.asyncImageLoadingShimmer(
    state: AsyncImageLoadingShimmerState,
): Modifier {
    return if(state.loadingState is State.Loading)
        shimmer()
    else
        this
}