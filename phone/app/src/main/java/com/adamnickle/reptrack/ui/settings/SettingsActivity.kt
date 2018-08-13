package com.adamnickle.reptrack.ui.settings

import android.os.Bundle
import android.view.WindowManager
import com.adamnickle.reptrack.BuildConfig
import com.adamnickle.reptrack.R
import dagger.android.support.DaggerAppCompatActivity

class SettingsActivity : DaggerAppCompatActivity()
{
    override fun onCreate( savedInstanceState: Bundle? )
    {
        super.onCreate( savedInstanceState )
        setContentView( R.layout.settings_activity )

        supportActionBar?.setDisplayHomeAsUpEnabled( true )

        if( BuildConfig.DEBUG )
        {
            window.addFlags( WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON )
        }
    }
}
