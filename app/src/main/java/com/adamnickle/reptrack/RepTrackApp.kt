package com.adamnickle.reptrack

import com.adamnickle.reptrack.injection.component.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication

class RepTrackApp: DaggerApplication()
{
    override fun applicationInjector(): AndroidInjector<RepTrackApp> = DaggerAppComponent.builder().create( this )
}