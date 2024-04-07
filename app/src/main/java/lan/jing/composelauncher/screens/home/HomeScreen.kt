package lan.jing.composelauncher.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import lan.jing.composelauncher.viewmodel.home.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val state by homeViewModel.state.collectAsState()
    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(text = "Compose Launcher")
            })
        }, contentWindowInsets = WindowInsets(0)
    ) {
        Column(
            Modifier
                .padding(it)
                .verticalScroll(rememberScrollState())
        ) {
            Button(onClick = {
                homeViewModel.loadLib()
            }) {
                Text(text = "加载运行库")
            }
            Button(onClick = {
                homeViewModel.version()
            }) {
                Text(text = "java --version")
            }
            Button(onClick = {
                homeViewModel.generateArgs()
            }) {
                Text(text = "生成参数")
            }
            Button(onClick = {
                homeViewModel.start()
            }) {
                Text(text = "启动")
            }
            SelectionContainer {
                Text(text = state.args)
            }
        }
    }
}