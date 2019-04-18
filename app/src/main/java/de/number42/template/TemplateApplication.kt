package de.number42.template

import android.app.Activity
import android.app.Application
import android.os.Looper
import com.jakewharton.threetenabp.AndroidThreeTen
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.android.schedulers.AndroidSchedulers
import timber.log.LogcatTree
import timber.log.Timber

class TemplateApplication : Application() {

  lateinit var appComponent: AppComponent

  override fun onCreate() {
    super.onCreate()

//    if (LeakCanary.isInAnalyzerProcess(this)) {
//      // This process is dedicated to LeakCanary for heap analysis.
//      // You should not init your app in this process.
//      return
//    }
//
//    LeakCanary.install(this)


    val asyncMainThreadScheduler = AndroidSchedulers.from(Looper.getMainLooper(), true)
    RxAndroidPlugins.setInitMainThreadSchedulerHandler { asyncMainThreadScheduler }

    AndroidThreeTen.init(this)

    if (BuildConfig.DEBUG) {
      Timber.plant(LogcatTree())

//      StrictMode.setThreadPolicy(
//          StrictMode.ThreadPolicy.Builder()
//              .detectAll()
//              .penaltyLog()
//              .penaltyDeath()
//              .build()
//      )
//      StrictMode.setVmPolicy(
//          StrictMode.VmPolicy.Builder()
//              .detectAll()
//              .penaltyLog()
//              .penaltyDeath()
//              .build()
//      )

    }

    appComponent = createAppComponent()

  }


}

interface AppComponent {

}