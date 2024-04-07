package lan.jing.composelauncher.viewmodel.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import lan.jing.composelauncher.model.Manifest
import javax.inject.Inject

@HiltViewModel
class DownloadViewModel @Inject constructor(
    private val httpClient: HttpClient,
) : ViewModel() {
    private val manifestAPI: String =
        "http://launchermeta.mojang.com/mc/game/version_manifest_v2.json"
    val state = MutableStateFlow(DownloadState())

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch(Dispatchers.IO) {
            state.update {
                it.copy(isLoading = true)
            }
            val manifest: Manifest = httpClient.get(manifestAPI).body()
            val release = manifest.versions?.filter { it?.type == "release" }
            val snapshot = manifest.versions?.filter { it?.type == "snapshot" }
            val alpha = manifest.versions?.filter { it?.type == "old_alpha" }
            state.update {
                it.copy(
                    isLoading = false,
                    release = release,
                    snapshot = snapshot,
                    alpha = alpha
                )
            }
        }
    }
}

data class DownloadState(
    val isLoading: Boolean = false,
    val release: List<Manifest.Version?>? = emptyList(),
    val snapshot: List<Manifest.Version?>? = emptyList(),
    val alpha: List<Manifest.Version?>? = emptyList(),

    )