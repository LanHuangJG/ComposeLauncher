package lan.jing.composelauncher.viewmodel.home

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import lan.jing.composelauncher.MainActivity
import lan.jing.composelauncher.MainActivity.Companion.dataPath
import lan.jing.composelauncher.MainActivity.Companion.downloadPath
import java.io.File
import javax.inject.Inject

@SuppressLint("StaticFieldLeak")
@HiltViewModel
class HomeViewModel @Inject constructor(
    @ApplicationContext context: Context,
) : ViewModel() {

    companion object {
        init {
            System.loadLibrary("composelauncher")
        }
    }

    private external fun startCompose(): String
    private external fun dlopen(path: String)
    private external fun setupJli(path: String)
    private external fun jliLaunch()
    private external fun redirectIO()
    private external fun startLogger()
    private external fun startMinecraft(args:Array<String>)
    var mArgs = ""
    fun loadLib() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                startLogger()
                val path = "$dataPath/runtime/jre17/lib"
                dlopen("$path/server/libjvm.so")
                dlopen("$path/libjava.so")
                dlopen("$path/libawt.so")
                dlopen("$path/libawt_headless.so")
                dlopen("$path/libnet.so")
                dlopen("$path/libnio.so")
                dlopen("$path/libfreetype.so")
                setupJli("$path/libjli.so")
                File(path).list()?.forEach {
                    if (it.endsWith(".so"))
                        dlopen("$path/$it")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun version() {
        viewModelScope.launch(Dispatchers.IO) {
            jliLaunch()
        }
    }

    fun generateArgs() {
        viewModelScope.launch(Dispatchers.IO) {
            val file = File("/storage/emulated/0/Download/ComposeLauncher/1.20.4/version/1.20.4.json")
            val json = file.readText()
            val jsonObject = Json.parseToJsonElement(json)
            val args = StringBuilder()
            jsonObject.jsonObject["libraries"]
                ?.jsonArray?.forEach {
                    val path = it.jsonObject["downloads"]?.jsonObject?.get("artifact")?.jsonObject?.get("path")?.jsonPrimitive?.content
                    if (path != null) {
                        args.append("$downloadPath/ComposeLauncher/1.20.4/libraries/$path:")
                    }
                }
            args.append("$downloadPath/ComposeLauncher/1.20.4/versions/1.20.4/1.20.4.jar")
            mArgs = args.toString()
            state.update {
                it.copy(args = args.toString())
            }

        }
    }

    fun start() {
        viewModelScope.launch(Dispatchers.IO) {
            startMinecraft(
                arrayOf(
                    mArgs
                )
            )
        }
    }

    val state = MutableStateFlow(HomeState())

    data class HomeState(
        val args: String = ""
    )
}