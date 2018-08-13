package com.adamnickle.reptrack.ui.settings

import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.preference.ListPreference
import android.preference.Preference
import android.preference.PreferenceActivity
import android.support.annotation.XmlRes
import android.view.MenuItem
import android.view.WindowManager
import com.adamnickle.reptrack.BuildConfig
import com.adamnickle.reptrack.R
import javax.inject.Inject

class SettingsActivity : DaggerAppCompatPreferenceActivity()
{
    override fun onCreate( savedInstanceState: Bundle? )
    {
        super.onCreate( savedInstanceState )

        supportActionBar?.setDisplayHomeAsUpEnabled( true )

        if( BuildConfig.DEBUG )
        {
            window.addFlags( WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON )
        }
    }

    override fun onIsMultiPane(): Boolean
    {
        return ( resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK ) >= Configuration.SCREENLAYOUT_SIZE_XLARGE
    }

    override fun onBuildHeaders( target: List<PreferenceActivity.Header> )
    {
        loadHeadersFromResource( R.xml.pref_headers, target )
    }

    override fun isValidFragment( fragmentName: String ): Boolean
    {
        return UnitsPreferenceFragment::class.java.name == fragmentName
    }

    abstract class HelperPreferenceFragment(
            @XmlRes private val preferencesResId: Int,
            private val preferenceKeys: Array<String>
    ): DaggerPreferenceFragment()
    {
        @Inject
        lateinit var sharedPreferences: SharedPreferences

        override fun onCreate( savedInstanceState: Bundle? )
        {
            super.onCreate( savedInstanceState )
            setHasOptionsMenu( true )
            addPreferencesFromResource( preferencesResId )

            for( preferenceKey in preferenceKeys )
            {
                bindPreferenceSummaryToValue( findPreference( preferenceKey ) )
            }
        }

        override fun findPreference( key: CharSequence ): Preference
        {
            return super.findPreference( key ) ?: throw IllegalArgumentException( "Could not find preference for '$key' key." )
        }

        override fun onOptionsItemSelected( item: MenuItem ): Boolean = when( item.itemId )
        {
            android.R.id.home ->
            {
                startActivity( Intent( activity, SettingsActivity::class.java ) )
                true
            }
            else -> super.onOptionsItemSelected( item )
        }

        private fun bindPreferenceSummaryToValue(preference: Preference )
        {
            preference.onPreferenceChangeListener = bindPreferenceSummaryToValueListener

            val value = sharedPreferences.getString( preference.key, "" )

            bindPreferenceSummaryToValueListener.onPreferenceChange( preference, value )
        }
    }

    class UnitsPreferenceFragment : HelperPreferenceFragment(
            R.xml.pref_units,
            arrayOf( "acceleration_units" )
    )

    companion object
    {
        private val bindPreferenceSummaryToValueListener = Preference.OnPreferenceChangeListener { preference, value ->
            val stringValue = value.toString()

            if( preference is ListPreference )
            {
                val index = preference.findIndexOfValue( stringValue )

                if( index >= 0 )
                {
                    preference.summary = preference.entries[ index ]
                }
                else
                {
                    preference.summary = null
                }
            }
            else
            {
                preference.summary = stringValue
            }

            return@OnPreferenceChangeListener true
        }
    }
}
