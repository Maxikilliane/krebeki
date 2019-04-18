package de.number42.template

import android.app.Application

import dagger.BindsInstance
import dagger.Component
import de.number42.template.data.ReleaseDataModule
import de.number42.template.ui.UiModule
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
