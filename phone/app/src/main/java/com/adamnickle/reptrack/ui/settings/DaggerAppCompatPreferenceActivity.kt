package com.adamnickle.reptrack.ui.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
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

    @Suppress( "DEPRECATION" )
    @Inject
    lateinit var frameworkFragmentInjector: DispatchingAndroidInjector<android.app.Fragment>

    override fun onCreate( savedInstanceState: Bundle? )
    {
        AndroidInjection.inject( this )
        super.onCreate( savedInstanceState )
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = supportFragmentInjector

    @Suppress( "DEPRECATION" )
    override fun fragmentInjector(): AndroidInjector<android.app.Fragment> = frameworkFragmentInjector
}