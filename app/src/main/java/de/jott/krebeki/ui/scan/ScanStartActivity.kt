package de.jott.krebeki.ui.scan

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jakewharton.rxbinding3.view.clicks
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import com.uber.autodispose.autoDisposable
import de.jott.krebeki.R
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_start_scan.start_scan_button
import kotlinx.android.synthetic.main.toolbar.toolbar
import org.jetbrains.anko.intentFor


class ScanStartActivity : AppCompatActivity() {

  override fun onCreate(
    savedInstanceState: Bundle?
  ) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_start_scan)
    setSupportActionBar(toolbar)
    supportActionBar?.title = "Krebeki"

    start_scan_button.clicks()
        .observeOn(AndroidSchedulers.mainThread())
        .autoDisposable(AndroidLifecycleScopeProvider.from(this))
        .subscribe {
          startActivity(intentFor<ScannerActivity>())
        }

  }




}