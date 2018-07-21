package com.adamnickle.demo.base

import android.arch.lifecycle.ViewModel
import com.adamnickle.demo.injection.component.DaggerViewModelInjector
import com.adamnickle.demo.injection.component.ViewModelInjector
import com.adamnickle.demo.injection.module.NetworkModule
import com.adamnickle.demo.ui.post.PostListViewModel

abstract class BaseViewModel: ViewModel()
{
    private val injector: ViewModelInjector = DaggerViewModelInjector
            .builder()
            .networkModule( NetworkModule )
            .build()

    init
    {
        inject()
    }

    private fun inject()
    {
        when( this )
        {
            is PostListViewModel -> injector.inject( this )
        }
    }
}