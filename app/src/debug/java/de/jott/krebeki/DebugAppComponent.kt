package de.jott.krebeki

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import de.jott.krebeki.data.DebugDataModule
import de.jott.krebeki.ui.UiModule

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
