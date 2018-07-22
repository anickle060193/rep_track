package com.adamnickle.demo.injection.component

import com.adamnickle.demo.DemoApplication
import com.adamnickle.demo.injection.module.ActivityBuilderModule
import com.adamnickle.demo.injection.module.AppModule
import com.adamnickle.demo.injection.module.NetworkModule
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component( modules = [
    AndroidSupportInjectionModule::class,
    AppModule::class,
    ActivityBuilderModule::class,
    NetworkModule::class
] )
interface AppComponent: AndroidInjector<DemoApplication>
{
    @Component.Builder
    abstract class Builder: AndroidInjector.Builder<DemoApplication>()
}