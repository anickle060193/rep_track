package com.adamnickle.demo.injection.module

import android.arch.lifecycle.ViewModelProvider
import com.adamnickle.demo.ui.ViewModelFactory
import dagger.Binds
import dagger.Module

@Module
abstract class ViewModelFactoryModule
{
    @Binds
    abstract fun bindViewModelFactory( viewModelFactory: ViewModelFactory ): ViewModelProvider.Factory
}