package de.jott.krebeki

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

class KrebekiApplication : Application() {

  lateinit var appComponent: AppComponent

  override fun onCreate() {
    super.onCreate()

    val asyncMainThreadScheduler = AndroidSchedulers.from(Looper.getMainLooper(), true)
    RxAndroidPlugins.setInitMainThreadSchedulerHandler { asyncMainThreadScheduler }

    AndroidThreeTen.init(this)

    if (BuildConfig.DEBUG) {
      Timber.plant(LogcatTree())


    }

    appComponent = createAppComponent()

  }


}

interface AppComponent {

}