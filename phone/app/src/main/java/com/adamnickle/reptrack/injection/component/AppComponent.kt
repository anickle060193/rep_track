package com.adamnickle.reptrack.injection.component

import com.adamnickle.reptrack.RepTrackApp
import com.adamnickle.reptrack.injection.module.AppBuilderModule
import com.adamnickle.reptrack.injection.module.AppModule
import com.adamnickle.reptrack.injection.module.ViewModelFactoryModule
import com.adamnickle.reptrack.injection.module.ViewModelModule
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component( modules = [
    AndroidSupportInjectionModule::class,
    AppModule::class,
    AppBuilderModule::class,
    ViewModelFactoryModule::class,
    ViewModelModule::class
] )
interface AppComponent: AndroidInjector<RepTrackApp>
{
    @Component.Builder
    abstract class Builder: AndroidInjector.Builder<RepTrackApp>()
}