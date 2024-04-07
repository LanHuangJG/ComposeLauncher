package lan.jing.composelauncher

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.res.AssetManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory.Companion.instance
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import lan.jing.composelauncher.navhost.MainNavHost
import lan.jing.composelauncher.theme.ComposeLauncherTheme
import java.io.File
import kotlin.concurrent.thread

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    companion object{
        lateinit var dataPath:String
        lateinit var downloadPath:String
        lateinit var myAssets:AssetManager
    }
    @SuppressLint("SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataPath = dataDir.absolutePath
        enableEdgeToEdge()
        setContent {
            ComposeLauncherTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    MainNavHost()
                }
            }
        }
        myAssets = assets
        downloadPath = Environment.getExternalStorageDirectory().absolutePath+"/"+Environment.DIRECTORY_DOWNLOADS
        println(dataPath)
        println(downloadPath)
    }
}
