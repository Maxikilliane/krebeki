package de.jott.krebeki.ui.scan

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import de.jott.krebeki.R
import org.jetbrains.anko.intentFor
import java.io.IOException

class ScannerActivity : AppCompatActivity() {

  lateinit var surfaceQRScanner: SurfaceView
  lateinit var barcodeDetector: BarcodeDetector
  lateinit var cameraSource: CameraSource
  internal var scanResult = ""

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_scanner)

    initStuff() /* Calling this function to initialize components */

  }

  /* Function used to initialize components of activity */
  fun initStuff() {
    surfaceQRScanner = findViewById(R.id.surfaceQRScanner)
    /* Initializing objects */
    barcodeDetector = BarcodeDetector.Builder(this)
        .setBarcodeFormats(Barcode.ALL_FORMATS)
        .build()
    cameraSource = CameraSource.Builder(applicationContext, barcodeDetector)
        .setRequestedPreviewSize(1024, 768)
        .setAutoFocusEnabled(true)
        .build()

    /* Adding Callback method to SurfaceView */
    surfaceQRScanner.holder.addCallback(object : SurfaceHolder.Callback {
      override fun surfaceCreated(holder: SurfaceHolder) {
        try {
          /* Asking user to allow access of camera */
          if (ActivityCompat.checkSelfPermission(
                  applicationContext, Manifest.permission.CAMERA
              ) == PackageManager.PERMISSION_GRANTED
          ) {
            cameraSource.start(surfaceQRScanner.holder)
          } else {
            ActivityCompat.requestPermissions(
                this@ScannerActivity, arrayOf(Manifest.permission.CAMERA), 1024
            )
          }
        } catch (e: IOException) {
          Log.e("Camera start error-->> ", e.toString())
        }

      }

      override fun surfaceChanged(
        holder: SurfaceHolder,
        format: Int,
        width: Int,
        height: Int
      ) {
      }

      override fun surfaceDestroyed(holder: SurfaceHolder) {
        cameraSource.stop()
      }
    })

    /* Adding Processor to Barcode detector */
    barcodeDetector.setProcessor(object : Detector.Processor<Barcode> {
      override fun release() {

      }

      override fun receiveDetections(detections: Detector.Detections<Barcode>) {
        val barcodes = detections.detectedItems /* Retrieving QR Code */
        if (barcodes.size() > 0) {

          barcodeDetector.release() /* Releasing barcodeDetector */


          scanResult = barcodes.valueAt(0)
              .displayValue.toString() /* Retrieving text from QR Code */

          startActivity(intentFor<ScanResultActivity>().putExtra("ScanResult", scanResult))
        }
      }
    })
  }

  /* Initialize components again */
  public override fun onResume() {
    super.onResume()
    initStuff()
  }
}