package de.number42.template.ui

import com.squareup.inject.assisted.dagger2.AssistedModule
import dagger.Module

@Module(
    includes = [ViewModelModule::class]
)
abstract class UiModule {



}

@AssistedModule
@Module(includes = [AssistedInject_ViewModelModule::class])
internal abstract class ViewModelModule
