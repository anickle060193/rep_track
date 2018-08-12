package com.adamnickle.reptrack.ui.settings

import android.app.Fragment
import android.os.Bundle
import android.preference.PreferenceFragment
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasFragmentInjector
import javax.inject.Inject

abstract class DaggerPreferenceFragment: PreferenceFragment(), HasFragmentInjector
{
    @Inject
    lateinit var childFragmentInjector: DispatchingAndroidInjector<Fragment>

    override fun onCreate( savedInstanceState: Bundle? )
    {
        AndroidInjection.inject( this )
        super.onCreate( savedInstanceState )
    }

    override fun fragmentInjector(): AndroidInjector<Fragment> = childFragmentInjector
}