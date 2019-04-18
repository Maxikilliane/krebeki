package de.jott.krebeki

import android.app.Application

import dagger.BindsInstance
import dagger.Component
import de.jott.krebeki.data.ReleaseDataModule
import de.jott.krebeki.ui.UiModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
  ReleaseDataModule::class,
  UiModule::class
])
interface ReleaseAppComponent : AppComponent {
  @Component.Builder
  interface Builder {
    @BindsInstance fun application(application: Application): Builder
    fun build(): ReleaseAppComponent
  }
}

fun Application.createAppComponent() = DaggerReleaseAppComponent.builder()
    .application(this)
    .build()
