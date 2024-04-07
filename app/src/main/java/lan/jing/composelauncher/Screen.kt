package lan.jing.composelauncher

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.twotone.Download
import androidx.compose.material.icons.twotone.Home
import androidx.compose.material.icons.twotone.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(
    val route: String,
    val label: String,
    val unSelectedIcon: ImageVector,
    val selectedIcon: ImageVector
) {
    data object Home : Screen("home", "主页", Icons.TwoTone.Home, Icons.Filled.Home)
    data object Download : Screen("download", "下载", Icons.TwoTone.Download, Icons.Filled.Download)
    data object Setting : Screen("setting", "设置", Icons.TwoTone.Settings, Icons.Filled.Settings)
}