package lan.jing.composelauncher.viewmodel

import android.os.Build
import android.os.Build.VERSION
import android.os.Environment
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import lan.jing.composelauncher.MainActivity.Companion.dataPath
import java.io.File
import javax.inject.Inject

@HiltViewModel
class MainNavHostViewModel @Inject constructor(
    @ApplicationContext val context: android.content.Context,
): ViewModel() {
    val state = MutableStateFlow(MainNavHostState())

    init {
        if (VERSION.SDK_INT > Build.VERSION_CODES.R) {
            if (Environment.isExternalStorageManager() && File("$dataPath/runtime").exists()) {
                state.update {
                    it.copy(startDestination = StartDestination.HOME)
                }
            } else {
                state.update {
                    it.copy(startDestination = StartDestination.RUNTIME)
                }

            }
        }
    }

    data class MainNavHostState(
        val startDestination: StartDestination = StartDestination.LOADING
    )

    enum class StartDestination {
        HOME,
        LOADING,
        RUNTIME
    }
}