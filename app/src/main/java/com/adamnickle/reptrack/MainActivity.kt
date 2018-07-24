package com.adamnickle.reptrack

import android.os.Bundle
import android.view.WindowManager
import com.adamnickle.reptrack.ui.workouts.WorkoutsListFragment
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.main_activity.*

class MainActivity: DaggerAppCompatActivity()
{
    override fun onCreate( savedInstanceState: Bundle? )
    {
        super.onCreate( savedInstanceState )
        setContentView( R.layout.main_activity )
        setSupportActionBar( toolbar )

        if( BuildConfig.DEBUG )
        {
            window.addFlags( WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON )
        }

        supportFragmentManager
                .beginTransaction()
                .replace( R.id.main_content, WorkoutsListFragment.createInstance() )
                .commit()
    }
}