package com.yustar.dashboard.presentation.screen

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.yustar.core.ui.PokeApp_JetpackComposeTheme
import com.yustar.dashboard.presentation.viewmodel.DashboardViewModel

/**
 * Created by Yustar Pramudana on 08/03/26.
 */

@Composable
fun ProfileScreen(paddingValues: PaddingValues, viewModel: DashboardViewModel) {
    ProfileContent(paddingValues = paddingValues)
}

@Composable
fun ProfileContent(paddingValues: PaddingValues) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(paddingValues)
            .safeDrawingPadding(),
        contentAlignment = Alignment.Center
    ) {
        Text(fontSize = 40.sp, text = "PROFILE SCREEN")
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, device = Devices.PIXEL_4)
@Composable
fun NightModePreviewProfileScreen() {
    PokeApp_JetpackComposeTheme {
        ProfileContent(PaddingValues())
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, device = Devices.PIXEL_4)
@Composable
fun LightModePreviewProfileScreen() {
    PokeApp_JetpackComposeTheme {
        ProfileContent(PaddingValues())
    }
}