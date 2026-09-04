package com.zzzangantuk.podzzz

import android.Manifest
import android.content.ComponentName
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.zzzangantuk.podzzz.background.PlaybackService
import com.zzzangantuk.podzzz.manager.DatabaseManager
import com.zzzangantuk.podzzz.ui.extractDeepLink
import com.zzzangantuk.podzzz.ui.helper.LocalDatabase
import com.zzzangantuk.podzzz.ui.helper.LocalSettingsRepository
import com.zzzangantuk.podzzz.ui.vm.MediaPlayerViewModel
import com.google.common.util.concurrent.Futures.immediateFuture
import com.google.common.util.concurrent.MoreExecutors

class AppActivity : ComponentActivity() {

    private val db by lazy {
        DatabaseManager.build(this)
    }

    private val settingsRepository = SettingsRepository(this)
    private val mediaPlayerViewModel: MediaPlayerViewModel by viewModels()

    var mediaController: MediaController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mediaPlayerViewModel.passDB(db)

        val deepLink = intent.extractDeepLink()

        enableEdgeToEdge()
        setContent {
            // create default lists if they don't exist
            LaunchedEffect(Unit) {
                db.lists().createFavorites()
                db.lists().createHearLater()
            }

            val launcher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestPermission()
            ) { }

            // request notification permission
            LaunchedEffect(Unit) {
                if(Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return@LaunchedEffect
                launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }

            CompositionLocalProvider(
                LocalDatabase provides db,
                LocalSettingsRepository provides settingsRepository
            ) {
                PodzzzApp(
                    deepLink
                )
            }
        }
    }

    override fun onStart() {
        val sessionToken = SessionToken(this, ComponentName(this, PlaybackService::class.java))
        val controllerFuture = MediaController.Builder(this, sessionToken).buildAsync()
        controllerFuture.addListener(
            {
                mediaController = controllerFuture.get()
                mediaController?.let { mediaPlayerViewModel.registerMediaController(it) }
            },
            MoreExecutors.directExecutor()
        )

        super.onStart()
    }

    override fun onStop() {
        mediaPlayerViewModel.unregisterMediaController()

        mediaController?.let { controller ->
            MediaController.releaseFuture(immediateFuture(controller))
            mediaController = null
        }

        super.onStop()
    }
}
