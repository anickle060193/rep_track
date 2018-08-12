package com.adamnickle.reptrack.ui.settings

import android.os.Bundle
import android.support.v4.app.Fragment
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasFragmentInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

abstract class DaggerAppCompatPreferenceActivity: AppCompatPreferenceActivity(), HasFragmentInjector, HasSupportFragmentInjector
{
    @Inject
    lateinit var supportFragmentInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var frameworkFragmentInjector: DispatchingAndroidInjector<android.app.Fragment>

    override fun onCreate( savedInstanceState: Bundle? )
    {
        AndroidInjection.inject( this )
        super.onCreate( savedInstanceState )
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = supportFragmentInjector

    override fun fragmentInjector(): AndroidInjector<android.app.Fragment> = frameworkFragmentInjector
}