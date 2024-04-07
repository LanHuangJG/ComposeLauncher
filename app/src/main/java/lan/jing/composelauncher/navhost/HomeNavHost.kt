package lan.jing.composelauncher.navhost

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import lan.jing.composelauncher.Screen
import lan.jing.composelauncher.model.Manifest
import lan.jing.composelauncher.screens.home.DownloadScreen
import lan.jing.composelauncher.screens.home.HomeScreen
import lan.jing.composelauncher.screens.home.SettingScreen

@Composable
fun HomeNavHost(
    toVersion:(Manifest.Version?) -> Unit
) {
    val screens = listOf(
        Screen.Home,
        Screen.Download,
        Screen.Setting
    )
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    Scaffold(
        contentWindowInsets = WindowInsets(0),
        bottomBar = {
            NavigationBar {
                screens.forEach { screen ->
                    NavigationBarItem(
                        icon = {
                            Icon(
                                imageVector = if (currentDestination?.hierarchy?.any { it.route == screen.route } == true) screen.selectedIcon else screen.unSelectedIcon,
                                contentDescription = screen.label
                            )
                        },
                        label = { Text(screen.label) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController,
            startDestination = Screen.Home.route,
            Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) {
                HomeScreen()
            }
            composable(Screen.Download.route) {
                DownloadScreen(
                    toVersion = toVersion
                )
            }
            composable(Screen.Setting.route) {
                SettingScreen()
            }
        }
    }
}