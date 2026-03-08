package com.yustar.dashboard.presentation.screen

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Boy
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.yustar.core.ui.PokeApp_JetpackComposeTheme
import com.yustar.dashboard.R
import com.yustar.dashboard.presentation.viewmodel.DashboardViewModel
import org.koin.androidx.compose.koinViewModel
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavHostController
import com.yustar.core.ui.Gray20
import com.yustar.core.ui.Gray80
import com.yustar.core.ui.Turquoise25
import com.yustar.dashboard.presentation.event.DashboardUiEvent

/**
 * Created by Yustar Pramudana on 08/03/26.
 */

data class Menu(
    val route: String,
    val label: String,
    val icon: ImageVector,
    val contentDescription: String
)

@Composable
fun DashboardScreen(navController: NavHostController, viewModel: DashboardViewModel = koinViewModel()) {
    DashboardContent(
        selectedMenu = viewModel.uiState.collectAsState().value.selectedTab,
        onMenuSelected = { viewModel.onEvent(DashboardUiEvent.OnMenuSelected(it)) }
    )
}

@Composable
fun DashboardContent(selectedMenu: Int, onMenuSelected: (Int) -> Unit) {
    val menus = arrayListOf(
        Menu("home", stringResource(R.string.home), Icons.Default.Home, stringResource(R.string.home)),
        Menu("profile", stringResource(R.string.profile), Icons.Default.Boy, stringResource(R.string.profile))
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .safeDrawingPadding(),
        contentAlignment = Alignment.TopCenter
    ) {
        Scaffold(
            bottomBar = {
                NavigationBar(
                    windowInsets = NavigationBarDefaults.windowInsets,
                    contentColor = Gray20
                ) {
                    menus.forEachIndexed { index, menu ->
                        NavigationBarItem(
                            alwaysShowLabel = true,
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.background,
                                selectedTextColor = MaterialTheme.colorScheme.background,
                                indicatorColor = Turquoise25,
                                unselectedIconColor = Gray80,
                                unselectedTextColor = Gray80
                            ),
                            selected = selectedMenu == index,
                            onClick = { onMenuSelected(index) },
                            icon = {
                                Icon(
                                    menu.icon,
                                    contentDescription =  menu.contentDescription
                                )
                            },
                            label = { menu.label }
                        )
                    }
                }
            }
        ) { contentPadding ->
            when (selectedMenu) {
                0 -> { MenuScreen(contentPadding) }
                1 -> { ProfileScreen(contentPadding) }
            }
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, device = Devices.PIXEL_4)
@Composable
fun NightModePreviewDashboardScreen() {
    PokeApp_JetpackComposeTheme {
        DashboardContent(0, {})
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, device = Devices.PIXEL_4)
@Composable
fun LightModePreviewDashboardScreen() {
    PokeApp_JetpackComposeTheme {
        DashboardContent(0, {})
    }
}