package org.righteffort.ryebread

import android.os.Bundle
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.FragmentActivity
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.util.Util
import androidx.media3.exoplayer.ExoPlayer
import org.righteffort.ryebread.databinding.ActivityPlayerBinding

class PlayerActivity : FragmentActivity() {

    private lateinit var binding: ActivityPlayerBinding
    private var player: ExoPlayer? = null

    companion object {
        const val EXTRA_URL = "extra_url"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        hideSystemUi()
    }

    // onStart is called when the activity becomes visible
    public override fun onStart() {
        super.onStart()
        if (Util.SDK_INT > 23) {
            initializePlayer()
        }
    }

    // onResume is called after onStart or when the app is brought back to the foreground
    public override fun onResume() {
        super.onResume()
        if ((Util.SDK_INT <= 23 || player == null)) {
            initializePlayer()
        }
    }

    // onPause is called when the app is moved to the background
    public override fun onPause() {
        super.onPause()
        if (Util.SDK_INT <= 23) {
            releasePlayer()
        }
    }
    
    // onStop is called when the activity is no longer visible
    public override fun onStop() {
        super.onStop()
        if (Util.SDK_INT > 23) {
            releasePlayer()
        }
    }

    private fun initializePlayer() {
        val streamUrl = intent.getStringExtra(EXTRA_URL) ?: return

        // Create an ExoPlayer instance.
        player = ExoPlayer.Builder(this).build().also { exoPlayer ->
            binding.playerView.player = exoPlayer

            // *** THIS IS THE KEY LINE FOR HIGH-QUALITY UPSCALING ***
            // It tells ExoPlayer to scale the video to fit the view, using the GPU
            // for a sharper image on high-resolution displays like 4K TVs.
            exoPlayer.setVideoScalingMode(C.VIDEO_SCALING_MODE_SCALE_TO_FIT)

            // Create a MediaItem from the M3U8 URL.
            val mediaItem = MediaItem.fromUri(streamUrl)

            // Set the MediaItem to be played.
            exoPlayer.setMediaItem(mediaItem)

            // Prepare the player and start playback when ready.
            exoPlayer.playWhenReady = true
            exoPlayer.prepare()
        }
    }

    private fun releasePlayer() {
        player?.let { exoPlayer ->
            // Release player resources to prevent memory leaks.
            exoPlayer.release()
        }
        player = null
    }

    // This function hides the system status bar and navigation bar for a fullscreen experience.
    private fun hideSystemUi() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, binding.playerView).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }
}
