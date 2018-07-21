package com.adamnickle.demo.injection.component

import com.adamnickle.demo.injection.module.NetworkModule
import com.adamnickle.demo.ui.post.PostListViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component( modules = [ NetworkModule::class ] )
interface ViewModelInjector
{
    fun inject( postListViewModel: PostListViewModel )

    @Component.Builder
    interface Builder
    {
        fun build(): ViewModelInjector

        fun networkModule( networkModule: NetworkModule ): Builder
    }
}