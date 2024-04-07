package lan.jing.composelauncher.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.twotone.ArrowBack
import androidx.compose.material.icons.twotone.KeyboardArrowDown
import androidx.compose.material.icons.twotone.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.serialization.json.jsonObject
import lan.jing.composelauncher.viewmodel.VersionViewModel
import java.net.URLDecoder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VersionScreen(
    onBack: () -> Unit,
    version: String = "",
    url: String = "",
    versionViewModel: VersionViewModel = hiltViewModel()
) {
    val state by versionViewModel.state.collectAsState()
    var isAssetsExpanded by remember {
        mutableStateOf(false)
    }
    var isLibsExpanded by remember {
        mutableStateOf(false)
    }
    var isIndexShow by remember {
        mutableStateOf(false)
    }
    var isResourceShow by remember {
        mutableStateOf(false)
    }
    var isMainJarShow by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(key1 = Unit) {
        versionViewModel.getVersion(
            url = URLDecoder.decode(url, "utf-8")
        )
    }
    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(text = version)
            }, navigationIcon = {
                IconButton(onClick = {
                    onBack()
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.TwoTone.ArrowBack,
                        contentDescription = null
                    )
                }
            })
        },
        contentWindowInsets = WindowInsets(0)
    ) { paddingValues ->
        AnimatedContent(targetState = state.isLoading, label = "") { show ->
            if (show) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                Column(
                    Modifier
                        .padding(
                            paddingValues
                        )
                        .animateContentSize()
                ) {
                    OutlinedCard(
                        modifier = Modifier.padding(16.dp),
                        onClick = {
                            isIndexShow = !isIndexShow
                        }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(text = "索引文件")
                            Spacer(modifier = Modifier.weight(1f))
                            IconButton(onClick = {
                                isIndexShow = !isIndexShow
                            }) {
                                AnimatedContent(targetState = isIndexShow, label = "") {
                                    if (!it) {
                                        Icon(
                                            imageVector = Icons.TwoTone.KeyboardArrowDown,
                                            contentDescription = null
                                        )
                                    } else {
                                        Icon(
                                            imageVector = Icons.TwoTone.KeyboardArrowUp,
                                            contentDescription = null
                                        )
                                    }
                                }
                            }
                        }
                    }
                    AnimatedVisibility(isIndexShow) {
                        Column {
                            OutlinedCard(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Row(
                                    Modifier.padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(
                                        Modifier.weight(1f)
                                            .padding(0.dp,0.dp,16.dp,0.dp)
                                    ){
                                        Text(text = "版本索引")
                                        Spacer(modifier = Modifier.height(16.dp))
                                        LinearProgressIndicator(progress = {
                                            state.versionIndexDownloadProgress /100.0f
                                        })
                                    }
                                    Button(onClick = {
                                        versionViewModel.downloadVersionIndex()
                                    }) {
                                        Text(text = "下载")
                                    }
                                }
                            }
                            OutlinedCard(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Row(
                                    Modifier.padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(
                                        Modifier.weight(1f)
                                            .padding(0.dp,0.dp,16.dp,0.dp)
                                    ) {
                                        Text(text = "资源索引")
                                        Spacer(modifier = Modifier.height(16.dp))
                                        LinearProgressIndicator(progress = {
                                            state.assetsIndexDownloadProgress /100.0f
                                        })
                                    }
                                    Button(onClick = {
                                        versionViewModel.downloadAssetsIndex()
                                    }) {
                                        Text(text = "下载")
                                    }
                                }
                            }
                        }
                    }
                    OutlinedCard(
                        modifier = Modifier.padding(16.dp),
                        onClick = {
                            isResourceShow = !isResourceShow
                        }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(text = "资源文件")
                            Spacer(modifier = Modifier.weight(1f))
                            IconButton(onClick = {
                                isResourceShow = !isResourceShow
                            }) {
                                AnimatedContent(targetState = isResourceShow, label = "") {
                                    if (!it) {
                                        Icon(
                                            imageVector = Icons.TwoTone.KeyboardArrowDown,
                                            contentDescription = null
                                        )
                                    } else {
                                        Icon(
                                            imageVector = Icons.TwoTone.KeyboardArrowUp,
                                            contentDescription = null
                                        )
                                    }
                                }
                            }
                        }
                    }
                    AnimatedVisibility(visible = isResourceShow) {
                        Column {
                            OutlinedCard(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .clickable(
                                        indication = null,
                                        interactionSource = remember {
                                            MutableInteractionSource()
                                        },
                                    ) {
                                        isAssetsExpanded = !isAssetsExpanded
                                    }
                            ) {
                                Column(
                                    modifier = Modifier.animateContentSize()
                                ) {
                                    Row(
                                        Modifier.padding(16.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column(
                                            Modifier.weight(1f)
                                                .padding(0.dp,0.dp,16.dp,0.dp)
                                        ) {
                                            Text(
                                                text = "游戏资源 (共${state.assetsTotalSize}MB)"
                                            )
                                            Spacer(modifier = Modifier.height(16.dp))
                                            LinearProgressIndicator(progress = {
                                                state.assetsDownloadProgress
                                            })
                                        }
                                        Button(onClick = {
                                            versionViewModel.downloadAssets()
                                        }) {
                                            Text(text = "下载")
                                        }
                                    }
                                    if (isAssetsExpanded) {
                                        LazyColumn(
                                            Modifier
                                                .padding(16.dp)
                                        ) {
                                            state.assetsObject.jsonObject["objects"]?.jsonObject
                                                ?.forEach { (key, value) ->
                                                    val hash = value.jsonObject["hash"].toString()
                                                    val size = value.jsonObject["size"].toString()
                                                    item {
                                                        Text(
                                                            text = key,
                                                            modifier = Modifier.fillMaxWidth()
                                                        )
                                                    }
                                                }
                                        }
                                    }
                                }
                            }
                            OutlinedCard(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .clickable(
                                        indication = null,
                                        interactionSource = remember {
                                            MutableInteractionSource()
                                        },
                                    ) {
                                        isLibsExpanded = !isLibsExpanded
                                    }
                            ) {
                                Column(
                                    modifier = Modifier.animateContentSize()
                                ) {
                                    Row(
                                        Modifier.padding(16.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column(
                                            Modifier.weight(1f)
                                                .padding(0.dp,0.dp,16.dp,0.dp)
                                        ) {
                                            Text(
                                                text = "运行库 (共${state.libsTotalSize}MB)"
                                            )
                                            Spacer(modifier = Modifier.height(16.dp))
                                            LinearProgressIndicator(progress = {
                                                state.libsDownloadProgress
                                            })
                                        }
                                        Button(onClick = {
                                            versionViewModel.downloadLibraries()
                                        }) {
                                            Text(text = "下载")
                                        }
                                    }
                                    if (isLibsExpanded) {
                                        LazyColumn(
                                            Modifier
                                                .padding(16.dp)
                                        ) {
                                            items(state.version.libraries!!) {
                                                Text(
                                                    text = it?.name!!,
                                                    modifier = Modifier.fillMaxWidth()
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    OutlinedCard(
                        modifier = Modifier.padding(16.dp),
                        onClick = {
                            isMainJarShow = !isMainJarShow
                        }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(text = "主程序")
                            Spacer(modifier = Modifier.weight(1f))
                            IconButton(onClick = {
                                isMainJarShow = !isMainJarShow
                            }) {
                                AnimatedContent(targetState = isMainJarShow, label = "") {
                                    if (!it) {
                                        Icon(
                                            imageVector = Icons.TwoTone.KeyboardArrowDown,
                                            contentDescription = null
                                        )
                                    } else {
                                        Icon(
                                            imageVector = Icons.TwoTone.KeyboardArrowUp,
                                            contentDescription = null
                                        )
                                    }
                                }
                            }
                        }
                    }
                    AnimatedVisibility(visible = isMainJarShow) {
                        Column {
                            OutlinedCard(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Row(
                                    Modifier.padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column (
                                        Modifier.weight(1f)
                                            .padding(0.dp,0.dp,16.dp,0.dp)
                                    ){
                                        Text(text = "主程序 (共${state.mainJarSize}MB)")
                                        Spacer(modifier = Modifier.height(16.dp))
                                        LinearProgressIndicator(progress = {
                                            state.mainJarDownloadProgress /100.0f
                                        })
                                    }
                                    Button(onClick = {
                                        versionViewModel.downloadMainJar()
                                    }) {
                                        Text(text = "下载")
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