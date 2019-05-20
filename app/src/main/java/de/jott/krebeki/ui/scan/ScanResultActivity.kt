package de.jott.krebeki.ui.scan

import android.content.Intent.getIntent
import android.content.res.AssetManager
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.jakewharton.rxbinding3.view.clicks
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import com.uber.autodispose.autoDisposable
import de.jott.krebeki.R
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_scan_result.question1
import kotlinx.android.synthetic.main.activity_scan_result.video
import kotlinx.android.synthetic.main.toolbar.toolbar
import timber.log.Timber
import timber.log.debug
import java.io.FileNotFoundException

class ScanResultActivity : AppCompatActivity() {

  private lateinit var player: SimpleExoPlayer

  private lateinit var videoUri: String
  private lateinit var imageUri: String
  private var resumePos: Long = 0

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_scan_result)
setSupportActionBar(toolbar)
    supportActionBar?.title = "Mc Beth"
    val extras = intent.extras
    if (extras != null) {
      val scanResult = extras.getString("ScanResult") /* Retrieving text of QR Code */
    }

    videoUri = "file:///android_asset/1_video.webm"
    imageUri = "file:///android_asset/image.png"

    question1.clicks()
        .observeOn(AndroidSchedulers.mainThread())
        .autoDisposable(AndroidLifecycleScopeProvider.from(this))
        .subscribe {
          player.playWhenReady
        }
  }

  private fun initializePlayer() {
    player = ExoPlayerFactory.newSimpleInstance(this)

    video.player = player

    val artwork = try {
      // assetManager needs a filename, convert by removing the path
      val assetString = imageUri.replace("file:///android_asset/", "")
      val inputStream = assets.open(assetString)
      Drawable.createFromStream(inputStream, imageUri)
    } catch (e: FileNotFoundException) {
      Timber.debug { "no video found" }
    }

    video.useArtwork = true
    val dataSourceFactory = DefaultDataSourceFactory(
        this,
        Util.getUserAgent(this, "")
    )

    val videoSource = ExtractorMediaSource.Factory(dataSourceFactory)
        .createMediaSource(Uri.parse(videoUri))
    player.prepare(videoSource)
    player.seekTo(resumePos)
  }

  public override fun onSaveInstanceState(bundle: Bundle) {
    super.onSaveInstanceState(bundle)
  }

  public override fun onStart() {
    super.onStart()
    if (Util.SDK_INT > VERSION_CODES.LOLLIPOP) initializePlayer()
  }

  public override fun onStop() {
    super.onStop()
    if (Util.SDK_INT > VERSION_CODES.LOLLIPOP) player.release()
  }

  public override fun onPause() {
    super.onPause()
    if (Util.SDK_INT <= VERSION_CODES.LOLLIPOP) player.release()
  }

  public override fun onResume() {
    super.onResume()
    if (Util.SDK_INT <= VERSION_CODES.LOLLIPOP) initializePlayer()
  }

}

