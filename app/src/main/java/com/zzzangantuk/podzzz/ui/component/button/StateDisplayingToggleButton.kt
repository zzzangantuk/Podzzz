package com.zzzangantuk.podzzz.ui.component.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ToggleButton
import androidx.compose.material3.ToggleButtonColors
import androidx.compose.material3.ToggleButtonDefaults
import androidx.compose.material3.ToggleButtonElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.zzzangantuk.podzzz.ui.component.common.StateDisplayingComponent

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun StateDisplayingToggleButton(
    state: Float,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    minimumState: Float = 0f,
    enabled: Boolean = true,
    colors: ToggleButtonColors = ToggleButtonDefaults.colors(),
    elevation: ToggleButtonElevation? = ToggleButtonDefaults.elevation(),
    border: BorderStroke? = null,
    contentPadding: PaddingValues = ButtonDefaults.contentPaddingFor(ButtonDefaults.MinHeight),
    interactionSource: MutableInteractionSource? = null,
    content: @Composable RowScope.() -> Unit
) {
    val leftColor = ToggleButtonDefaults.colors(
        containerColor = colors.checkedContainerColor,
        contentColor = colors.checkedContentColor,
        checkedContainerColor = colors.checkedContainerColor,
        checkedContentColor = colors.checkedContentColor
    )

    val rightColor = ToggleButtonDefaults.colors(
        containerColor = colors.containerColor,
        contentColor = colors.contentColor,
        checkedContainerColor = colors.containerColor,
        checkedContentColor = colors.contentColor
    )

    StateDisplayingComponent(
        state = state,
        minimumState = minimumState
    ) { left ->
        ToggleButton(
            checked = checked,
            onCheckedChange = onCheckedChange,
            modifier = modifier,
            enabled = enabled,
            colors = if(left) leftColor else rightColor,
            elevation = elevation,
            border = border,
            contentPadding = contentPadding,
            interactionSource = interactionSource,
            content = content
        )
    }
}