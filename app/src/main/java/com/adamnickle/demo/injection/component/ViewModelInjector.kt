package com.adamnickle.demo.injection.component

import com.adamnickle.demo.injection.module.ActivityBuilderModule
import com.adamnickle.demo.injection.module.AppModule
import com.adamnickle.demo.injection.module.NetworkModule
import com.adamnickle.demo.ui.post.PostListViewModel
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component( modules = [
    AndroidSupportInjectionModule::class,
    AppModule::class,
    ActivityBuilderModule::class,
    NetworkModule::class
] )
interface ViewModelInjector
{
    fun inject( postListViewModel: PostListViewModel )

    @Component.Builder
    interface Builder
    {
        fun build(): ViewModelInjector

        @BindsInstance
        fun networkModule( networkModule: NetworkModule ): Builder
    }
}