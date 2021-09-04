package com.xsofty.shared.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun NavBarSpacer() {
    Spacer(modifier = Modifier.fillMaxWidth().height(54.dp))
}

@Composable
fun VerticalSpacer() {
    Spacer(modifier = Modifier.fillMaxWidth().height(16.dp))
}

@Composable
fun Loader(
    backgroundColor: Color,
    loaderColor: Color
) {
    Box(
        modifier = Modifier
            .background(color = backgroundColor)
            .fillMaxWidth()
            .fillMaxHeight(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = loaderColor
        )
    }
}