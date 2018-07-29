package com.adamnickle.reptrack.injection.module

import android.arch.lifecycle.ViewModelProvider
import com.adamnickle.reptrack.ui.ViewModelFactory
import dagger.Binds
import dagger.Module

@Suppress( "unused" )
@Module
abstract class ViewModelFactoryModule
{
    @Binds
    abstract fun bindViewModelFactory( viewModelFactory: ViewModelFactory ): ViewModelProvider.Factory
}