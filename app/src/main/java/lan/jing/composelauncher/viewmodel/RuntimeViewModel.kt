package lan.jing.composelauncher.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import lan.jing.composelauncher.MainActivity.Companion.dataPath
import lan.jing.composelauncher.MainActivity.Companion.downloadPath
import lan.jing.composelauncher.MainActivity.Companion.myAssets
import java.io.File
import javax.inject.Inject

@HiltViewModel
class RuntimeViewModel @Inject constructor(
    @ApplicationContext context: Context,
):ViewModel() {
    init {

    }
    fun loadRuntime(){
        viewModelScope.launch(Dispatchers.IO){
            if (!File("$dataPath/runtime").exists()) {
                val assets = myAssets.open("runtime.zip")
                val output = File("$dataPath/runtime.zip")
                output.writeBytes(assets.readBytes())
                val process = Runtime.getRuntime().exec("unzip $dataPath/runtime.zip -d $dataPath")
                val bufferedReader = process.inputStream.bufferedReader()
                var line: String?
                while (bufferedReader.readLine().also { line = it } != null) {
                    println(line)
                }
                //删除zip文件
                output.delete()
            }
            File("$downloadPath/ComposeLauncher").mkdirs()
        }
    }
}