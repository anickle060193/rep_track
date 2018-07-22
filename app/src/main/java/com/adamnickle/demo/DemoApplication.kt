package com.adamnickle.demo

import com.adamnickle.demo.injection.component.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication

class DemoApplication: DaggerApplication()
{
    override fun applicationInjector(): AndroidInjector<DemoApplication> = DaggerAppComponent.builder().create( this )
}