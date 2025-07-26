package com.example.androidpracticumcustomview.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.androidpracticumcustomview.R

@Composable
fun MainScreen(closeActivity: () -> Unit) {
    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .clickable { closeActivity.invoke() },
            contentAlignment = Alignment.Center
        ) {
            CustomContainerCompose(
                firstChild = {
                    Box(
                        modifier = Modifier.background(color = Color(0xFF03DAC5))
                    ) {
                        Text(
                            text = stringResource(R.string.custom_view_group_first_view_name),
                            color = Color(0xFF6200EE),
                            modifier = Modifier.padding(16.dp)
                        )
                    }

                },
                secondChild = {
                    Box(
                        modifier = Modifier.background(color = Color(0xFF03DAC5))
                    ) {
                        Text(
                            text = stringResource(R.string.custom_view_group_second_view_name),
                            color = Color(0xFF6200EE),
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            )
        }
    }
}