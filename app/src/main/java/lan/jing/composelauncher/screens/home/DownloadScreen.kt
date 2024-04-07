package lan.jing.composelauncher.screens.home

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.twotone.KeyboardArrowRight
import androidx.compose.material.icons.twotone.AutoAwesome
import androidx.compose.material.icons.twotone.BugReport
import androidx.compose.material.icons.twotone.Games
import androidx.compose.material.icons.twotone.Refresh
import androidx.compose.material.icons.twotone.VideogameAsset
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LeadingIconTab
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import lan.jing.composelauncher.model.Manifest
import lan.jing.composelauncher.viewmodel.home.DownloadViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DownloadScreen(
    downloadViewModel: DownloadViewModel = hiltViewModel(),
    toVersion: (Manifest.Version?) -> Unit
) {
    val pagerState = rememberPagerState(pageCount = {
        3
    })
    val icons =
        listOf(Icons.TwoTone.Games, Icons.TwoTone.VideogameAsset, Icons.TwoTone.BugReport)
    val titles = listOf("稳定版", "快照版", "测试版")
    val scope = rememberCoroutineScope()
    val state by downloadViewModel.state.collectAsState()
    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(text = "下载")
            }, actions = {
                IconButton(onClick = {
                    downloadViewModel.refresh()
                }) {
                    Icon(imageVector = Icons.TwoTone.Refresh, contentDescription = null)
                }
            })
        }, contentWindowInsets = WindowInsets(0)
    ) { paddingValues ->
        Column(Modifier.padding(paddingValues)) {
            PrimaryTabRow(
                selectedTabIndex =
                pagerState.currentPage
            ) {
                titles.forEachIndexed { index, title ->
                    LeadingIconTab(
                        selected = pagerState.currentPage == index,
                        onClick = {
                            scope.launch {
                                pagerState.scrollToPage(index)
                            }
                        },
                        text = { Text(text = title) },
                        icon = {
                            Icon(imageVector = icons[index], contentDescription = null)
                        }
                    )
                }
            }
            HorizontalPager(state = pagerState, modifier = Modifier.weight(1f)) { page ->
                AnimatedContent(targetState = state.isLoading, label = "") {
                    if (it) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    } else {
                        when (page) {
                            0 -> {
                                LazyColumn(Modifier.padding(0.dp, 8.dp)) {
                                    state.release?.forEach {
                                        item {
                                            Item(it, toVersion)
                                        }
                                    }
                                }
                            }

                            1 -> {
                                LazyColumn(Modifier.padding(0.dp, 8.dp)) {
                                    state.snapshot?.forEach {
                                        item {
                                            Item(it, toVersion)
                                        }
                                    }
                                }
                            }

                            2 -> {
                                LazyColumn(Modifier.padding(0.dp, 8.dp)) {
                                    state.alpha?.forEach {
                                        item {
                                            Item(it, toVersion)

                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
private fun Item(version: Manifest.Version?, toVersion: (Manifest.Version?) -> Unit) {
    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp, 8.dp),
        onClick = {
            toVersion(
                version
            )
        }
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            Icon(imageVector = Icons.TwoTone.AutoAwesome, contentDescription = null)
            Text(
                text = version?.id.toString(),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = Icons.AutoMirrored.TwoTone.KeyboardArrowRight,
                contentDescription = null
            )
        }
    }
}