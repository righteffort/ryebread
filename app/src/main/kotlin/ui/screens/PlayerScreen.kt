package org.righteffort.ryebread.ui.screens

import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import org.righteffort.ryebread.MainActivity

@Composable
fun PlayerScreen(streamUrl: String) {
    val context = LocalContext.current

    // Create and remember the ExoPlayer instance
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            // Set the high-quality upscaling mode
            setVideoScalingMode(C.VIDEO_SCALING_MODE_SCALE_TO_FIT)
            setMediaItem(MediaItem.fromUri(streamUrl))
            playWhenReady = true
            prepare()
        }
    }

    // Handle the player's lifecycle and fullscreen mode
    DisposableEffect(Unit) {
        val window = (context as MainActivity).window
        val insetsController = WindowCompat.getInsetsController(window, window.decorView)

        // Enter fullscreen
        insetsController.apply {
            hide(WindowInsetsCompat.Type.systemBars())
            systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }

        onDispose {
            // Exit fullscreen and release the player
            insetsController.show(WindowInsetsCompat.Type.systemBars())
            exoPlayer.release()
        }
    }

    // Embed the Android PlayerView into the Compose UI
    AndroidView(
        factory = {
            PlayerView(context).apply {
                player = exoPlayer
                layoutParams = FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
            }
        }
    )
}
