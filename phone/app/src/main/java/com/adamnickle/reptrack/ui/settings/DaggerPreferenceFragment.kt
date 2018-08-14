package com.adamnickle.reptrack.ui.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceFragmentCompat
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

abstract class DaggerPreferenceFragment: PreferenceFragmentCompat(), HasSupportFragmentInjector
{
    @Inject
    lateinit var childFragmentInjector: DispatchingAndroidInjector<Fragment>

    override fun onCreate( savedInstanceState: Bundle? )
    {
        AndroidSupportInjection.inject( this )
        super.onCreate( savedInstanceState )
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = childFragmentInjector
}