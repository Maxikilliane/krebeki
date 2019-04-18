package de.number42.template

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import de.number42.template.data.DebugDataModule
import de.number42.template.ui.UiModule

import javax.inject.Singleton

@Singleton
@Component(modules = [
  DebugDataModule::class,
  UiModule::class
])
interface DebugAppComponent : AppComponent {
  @Component.Builder
  interface Builder {
    @BindsInstance fun application(application: Application): Builder
    fun build(): DebugAppComponent
  }
}

fun Application.createAppComponent() = DaggerDebugAppComponent.builder()
    .application(this)
    .build()
