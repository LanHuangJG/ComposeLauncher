package lan.jing.composelauncher.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import lan.jing.composelauncher.MainActivity.Companion.downloadPath
import lan.jing.composelauncher.model.Version
import zlc.season.rxdownload4.download
import zlc.season.rxdownload4.file
import zlc.season.rxdownload4.task.Task
import java.io.File
import javax.inject.Inject

@HiltViewModel
class VersionViewModel @Inject constructor(
    private val httpClient: HttpClient,
    @ApplicationContext val context: Context,
) : ViewModel() {
    val state = MutableStateFlow(VersionState())
    private lateinit var version: Version
    private lateinit var url: String
    private lateinit var assetsObject: JsonElement

    @SuppressLint("DefaultLocale")
    fun getVersion(url: String) {
        this.url = url
        viewModelScope.launch(Dispatchers.IO) {
            state.update {
                it.copy(isLoading = true)
            }
            version = httpClient.get(url).body()
            val assetsTotalSize = version.assetIndex?.totalSize?.toDouble()
                ?.div(1024)?.div(1024)
            //保留两位小鼠
            val assetsTotalSizeStr = String.format("%.2f", assetsTotalSize)
            val libsTotalSize = version.libraries?.sumOf {
                it?.downloads?.artifact?.size!!
            }
            val libsTotalSizeStr = String.format(
                "%.2f", libsTotalSize
                    ?.toDouble()
                    ?.div(1024)?.div(1024)
            )
            val mainJarSize = version.downloads?.client?.size
            val mainJarSizeStr = String.format(
                "%.2f", mainJarSize
                    ?.toDouble()
                    ?.div(1024)?.div(1024)
            )
            val assetsFile =
                File(downloadPath + "/" + "ComposeLauncher/" + version.id + "/assets/indexes/" + version.assetIndex?.id + ".json")
            if (assetsFile.exists()) {
                val json = assetsFile.readText()
                assetsObject = kotlinx.serialization.json.Json.parseToJsonElement(json)
                state.update {
                    it.copy(
                        isLoading = false,
                        version = version,
                        assetsTotalSize = assetsTotalSizeStr,
                        libsTotalSize = libsTotalSizeStr,
                        assetsObject = assetsObject,
                        mainJarSize = mainJarSizeStr
                    )
                }
            } else {
                state.update {
                    it.copy(
                        isLoading = false,
                        version = version,
                        assetsTotalSize = assetsTotalSizeStr,
                        libsTotalSize = libsTotalSizeStr,
                        mainJarSize = mainJarSizeStr
                    )
                }
            }
        }
    }

    fun downloadVersionIndex() {
        viewModelScope.launch(Dispatchers.IO) {
            val path =
                downloadPath + "/" + "ComposeLauncher/" + version.id + "/version"
            val disposable = url.download()
                .observeOn(Schedulers.io())
                .subscribeBy(
                    onNext = { progress ->
                        state.update {
                            it.copy(versionIndexDownloadProgress = progress.percent().toFloat())
                        }
                    },
                    onComplete = {
                        state.update {
                            it.copy(versionIndexDownloadProgress = 100f)
                        }
                        val file = url.file()
                        val destinationFile = File(path, version.id + ".json")
                        file.copyTo(destinationFile, true)
                    },
                    onError = {

                    }
                )
        }
    }

    //索引下载
    fun downloadAssetsIndex() {
        viewModelScope.launch(Dispatchers.IO) {
            if (!File(downloadPath + "/" + "ComposeLauncher/" + version.id + "/version/" + version.id + ".json").exists()) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "请先下载版本索引", Toast.LENGTH_SHORT).show()
                }
            } else {
                val path =
                    downloadPath + "/" + "ComposeLauncher/" + version.id + "/assets/indexes"
                Task(
                    url = version.assetIndex?.url!!,
                    saveName = version.assetIndex?.id + ".json",
                    savePath = path
                ).download()
                    .observeOn(Schedulers.io())
                    ?.subscribeBy(
                        onNext = { progress ->
                            state.update {
                                it.copy(assetsIndexDownloadProgress = progress.percent().toFloat())
                            }
                        },
                        onComplete = {
                            state.update {
                                it.copy(
                                    assetsIndexDownloadProgress =
                                    100f
                                )
                            }
                        },
                        onError = {
                            it.printStackTrace()
                        }
                    )
            }
        }
    }

    fun downloadAssets() {
        viewModelScope.launch(Dispatchers.IO) {
            //读取文件json
            val file =
                File(downloadPath + "/" + "ComposeLauncher/" + version.id + "/assets/indexes/" + version.assetIndex?.id + ".json")
            if (!file.exists()) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "请先下载资源索引", Toast.LENGTH_SHORT).show()
                }
            } else {
                val json = file.readText()
                val assetsObject = kotlinx.serialization.json.Json.parseToJsonElement(json)
                val objects = assetsObject.jsonObject["objects"]?.jsonObject
                var downloadIndex = 0f
                state.update {
                    it.copy(assetsDownloadProgress = 0f)
                }
                println(objects?.size)
                var index = 0
                objects?.forEach { (key, value) ->
                    val hash = value.jsonObject["hash"].toString().replace("\"", "")
                    val size = value.jsonObject["size"].toString()
                    val dir =
                        downloadPath + "/" + "ComposeLauncher/" + version.id + "/assets/objects/" + hash.substring(
                            0,
                            2
                        )
                    val url = "https://resources.download.minecraft.net/" + hash.substring(
                        0,
                        2
                    ) + "/" + hash
                    if (!File("$dir/$hash").exists() || File("$dir/$hash").length() != size.toLong()) {
                        val result = Task(
                            url = url,
                            saveName = hash,
                            savePath = dir
                        ).download()
                            .observeOn(Schedulers.io())
                            ?.subscribeBy(
                                onNext = { progress ->

                                },
                                onComplete = {
                                    index++
                                    println(key)
                                    state.update {
                                        it.copy(assetsDownloadProgress = index.toFloat() / objects.size.toFloat())
                                    }
                                    System.gc()
                                },
                                onError = {
                                    println(it.message)
                                }
                            )
                    } else {
                        index++
                        state.update {
                            it.copy(assetsDownloadProgress = index.toFloat() / objects.size.toFloat())
                        }
                    }
                    println("下载进度：$index/${objects.size}")
                }
            }

        }
    }

    fun downloadLibraries() {
        viewModelScope.launch(Dispatchers.IO) {
            val path = downloadPath + "/" + "ComposeLauncher/" + version.id + "/libraries"
            File(path).mkdirs()
            var index = 0
            state.update {
                it.copy(assetsDownloadProgress = 0f)
            }
            version.libraries?.forEach { library ->
                val url = library?.downloads?.artifact?.url
                val subPath = library?.downloads?.artifact?.path
                File("$path/$subPath").parentFile?.mkdirs()
                val result = Task(
                    url = url!!,
                    savePath = "$path/$subPath",
                ).download()
                    .observeOn(Schedulers.io())
                    ?.subscribeBy(
                        onNext = { progress ->

                        },
                        onComplete = {
                            index++
                            state.update {
                                it.copy(libsDownloadProgress = index.toFloat() / version.libraries!!.size.toFloat())
                            }
                            println("下载进度：$index/${version.libraries!!.size}")
                        },
                        onError = {
                            println(it.message)
                        }
                    )
            }
        }
    }

    fun downloadMainJar() {
        viewModelScope.launch(Dispatchers.IO) {
            val path =
                downloadPath + "/" + "ComposeLauncher/" + version.id + "/version"
            val disposable = version.downloads?.client?.url?.download()
                ?.observeOn(Schedulers.io())
                ?.subscribeBy(
                    onNext = { progress ->
                        state.update {
                            it.copy(mainJarDownloadProgress = progress.percent().toFloat())
                        }
                    },
                    onComplete = {
                        state.update {
                            it.copy(mainJarDownloadProgress = 100f)
                        }
                        val file = version.downloads?.client?.url?.file()
                        val destinationFile = File(path, version.id + ".jar")
                        file?.copyTo(destinationFile, true)
                    },
                    onError = {

                    }
                )
        }
    }

    data class VersionState(
        val isLoading: Boolean = true,
        val version: Version = Version(),
        val versionIndexDownloadProgress: Float = 0f,
        val assetsIndexDownloadProgress: Float = 0f,
        val assetsTotalSize: String = "",
        val libsTotalSize: String = "",
        val assetsObject: JsonElement = kotlinx.serialization.json.Json.parseToJsonElement("{}"),
        val mainJarSize: String = "",
        val mainJarDownloadProgress: Float = 0f,
        val assetsDownloadProgress: Float = 0f,
        val libsDownloadProgress: Float = 0f
    )
}