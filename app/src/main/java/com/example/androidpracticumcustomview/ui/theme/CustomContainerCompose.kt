package com.example.androidpracticumcustomview.ui.theme

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@Composable
fun CustomContainerCompose(
    firstChild: @Composable (() -> Unit)?,
    secondChild: @Composable (() -> Unit)?
) {
    var firstChildOffsetY by remember { mutableFloatStateOf(0f) }
    var secondChildOffsetY by remember { mutableFloatStateOf(0f) }
    var childrenAlpha by remember { mutableFloatStateOf(0f) }
    var animationStarted by rememberSaveable { mutableStateOf(false) }

    var firstChildHeight by remember { mutableFloatStateOf(0f) }
    var secondChildHeight by remember { mutableFloatStateOf(0f) }

    BoxWithConstraints(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        val boxWithConstraintsScope = this
        val density = LocalDensity.current
        val containerHeight = with(density) { boxWithConstraintsScope.maxHeight.toPx() }

        LaunchedEffect(Unit) {
            if (!animationStarted) {
                coroutineScope {
                    launch {
                        animate(
                            initialValue = 0f,
                            targetValue = -(containerHeight / 2 - firstChildHeight / 2),
                            animationSpec = tween(
                                durationMillis = 5000,
                                easing = LinearEasing
                            )
                        ) { value, _ ->
                            firstChildOffsetY = with(density) { value.toDp().value }
                        }
                    }

                    launch {
                        animate(
                            initialValue = 0f,
                            targetValue = (containerHeight / 2 - secondChildHeight / 2),
                            animationSpec = tween(
                                durationMillis = 5000,
                                easing = LinearEasing
                            )
                        ) { value, _ ->
                            secondChildOffsetY = with(density) { value.toDp().value }
                        }
                    }

                    launch {
                        animate(
                            initialValue = 0f,
                            targetValue = 1f,
                            animationSpec = tween(
                                durationMillis = 2000,
                                easing = LinearEasing
                            )
                        ) { value, _ ->
                            childrenAlpha = value
                        }
                    }
                    animationStarted = true
                }
            } else {
                // Set final positions immediately after configuration change
                firstChildOffsetY = with(density) { (-(containerHeight / 2 - firstChildHeight / 2)).toDp().value }
                secondChildOffsetY = with(density) { ((containerHeight / 2 - secondChildHeight / 2)).toDp().value }
                childrenAlpha = 1f
            }
        }

        firstChild?.let {
            Box(
                modifier = Modifier
                    .offset(y = firstChildOffsetY.dp)
                    .alpha(childrenAlpha)
                    .layout { measurable, constraints ->
                        val placeable = measurable.measure(constraints)
                        firstChildHeight = placeable.height.toFloat()
                        layout(placeable.width, placeable.height) {
                            placeable.place(0, 0)
                        }
                    }
            ) {
                it()
            }
        }

        secondChild?.let {
            Box(
                modifier = Modifier
                    .offset(y = secondChildOffsetY.dp)
                    .alpha(childrenAlpha)
                    .layout { measurable, constraints ->
                        val placeable = measurable.measure(constraints)
                        secondChildHeight = placeable.height.toFloat()
                        layout(placeable.width, placeable.height) {
                            placeable.place(0, 0)
                        }
                    }
            ) {
                it()
            }
        }
    }
}