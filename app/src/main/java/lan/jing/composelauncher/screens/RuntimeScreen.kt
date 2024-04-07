package lan.jing.composelauncher.screens

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Environment
import android.provider.Settings
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import lan.jing.composelauncher.viewmodel.RuntimeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RuntimeScreen(
    modifier: Modifier = Modifier,
    runtimeViewModel: RuntimeViewModel = hiltViewModel(),
    toHome: () -> Unit
) {
    val context = LocalContext.current
    Scaffold(
        topBar = {
            LargeTopAppBar(title = {
                Text(text = "运行库")
            })
        }
    ) {
        Column(
            Modifier.padding(it)
        ) {
            OutlinedCard(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                onClick = {
                    requestStoragePermission(context)
                }
            ) {
                Row(
                    Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "请授予我存储权限")
                    Spacer(modifier = Modifier.weight(1f))
                    OutlinedButton(onClick = {
                        requestStoragePermission(context)
                    }) {
                        Text(text = "授予权限")
                    }
                }
            }
            OutlinedCard(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                onClick = {
                    requestStoragePermission(context)
                }
            ) {
                Row(
                    Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "点我导入运行时")
                    Spacer(modifier = Modifier.weight(1f))
                    OutlinedButton(onClick = {
                        runtimeViewModel.loadRuntime()
                    }) {
                        Text(text = "导入")
                    }
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Row(
                Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Absolute.Right
            ) {
                Button(onClick = {
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.R) {
                        if (Environment.isExternalStorageManager().not()) {
                            Toast.makeText(context, "请先授予存储权限", Toast.LENGTH_SHORT).show()
                        } else {
                            toHome()
                        }
                    }
                }) {
                    Text(text = "完成")
                }
            }
        }
    }
}

private fun requestStoragePermission(context: Context) {
    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.R) {
        if (!Environment.isExternalStorageManager()) {
            val intent =
                Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
            intent.data =
                android.net.Uri.parse("package:${context.packageName}")
            context.startActivity(intent)
        }
    }
}