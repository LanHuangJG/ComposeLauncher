package lan.jing.composelauncher.navhost

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import lan.jing.composelauncher.screens.RuntimeScreen
import lan.jing.composelauncher.screens.VersionScreen
import lan.jing.composelauncher.viewmodel.MainNavHostViewModel
import java.net.URLEncoder

@Composable
fun MainNavHost(mainNavHostViewModel: MainNavHostViewModel = hiltViewModel()) {
    val navController = rememberNavController()
    val state by mainNavHostViewModel.state.collectAsState()
    NavHost(navController = navController, startDestination = state.startDestination.name) {
        composable("home") {
            HomeNavHost(
                toVersion = {
                    navController.navigate(
                        "version/${it?.id}/${
                            URLEncoder.encode(
                                it?.url,
                                "utf-8"
                            )
                        }"
                    )
                }
            )
        }
        composable("version/{version}/{url}", listOf(
            navArgument("version") {
                type = NavType.StringType
                nullable = false
            },
            navArgument("url") {
                type = NavType.StringType
                nullable = false
            }
        )) {
            VersionScreen(
                onBack = {
                    navController.navigateUp()
                },
                it.arguments?.getString("version").orEmpty(),
                it.arguments?.getString("url").orEmpty()
            )
        }
        composable("runtime") {
            RuntimeScreen(
                toHome = {
                    navController.navigate("home") {
                        popUpTo("runtime") {
                            inclusive = true
                        }
                    }
                }
            )
        }
    }
}