package com.adamnickle.reptrack.injection.component

import com.adamnickle.reptrack.RepTrackApp
import com.adamnickle.reptrack.injection.module.*
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component( modules = [
    AndroidSupportInjectionModule::class,
    AppModule::class,
    AppBuilderModule::class,
    NetworkModule::class,
    ViewModelFactoryModule::class,
    ViewModelModule::class
] )
interface AppComponent: AndroidInjector<RepTrackApp>
{
    @Component.Builder
    abstract class Builder: AndroidInjector.Builder<RepTrackApp>()
}