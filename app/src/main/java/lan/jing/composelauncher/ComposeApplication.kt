package lan.jing.composelauncher

import android.app.Application
import android.content.Intent
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ComposeApplication:Application() {
    override fun onCreate() {
        super.onCreate()
        Thread.setDefaultUncaughtExceptionHandler {
            _, e ->
            e.printStackTrace()
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
    }
}